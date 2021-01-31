package com.qa.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.base.TestBase;

public class ShoppingPage extends TestBase {

	// Page Factory Elements

	// Sign In link
	@FindBy(css = "a[title='Log in to your customer account']")
	WebElement signinlnk;

	// Username text box
	@FindBy(id = "email")
	WebElement username;

	// Password text box
	@FindBy(id = "passwd")
	WebElement password;

	// Sign In button
	@FindBy(id = "SubmitLogin")
	WebElement loginbtn;

	// T-shirt link
	@FindBy(linkText = "T-SHIRTS")
	WebElement tshirtlnk;

	// product link
	@FindBy(css = "a.product-name[title='Faded Short Sleeve T-shirts']")
	WebElement productlnk;

	// Add to cart link
	@FindBy(css = "button[name='Submit']")
	WebElement addtocartlnk;

	// Product successfully added... text
	@FindBy(css = "div[class='layer_cart_product col-xs-12 col-md-6'] h2")
	WebElement messagetext;

	// Total qty added in cart
	@FindBy(css = "#layer_cart_product_quantity")
	WebElement Totalqty;

	// Proceed to checkout link
	@FindBy(css = "a[title='Proceed to checkout']")
	WebElement checkoutlnk;

	// Product title
	@FindBy(css = "td.cart_description>p.product-name")
	WebElement producttitle;

	// Product detail
	@FindBy(css = "small.cart_ref")
	WebElement productdetail;

	// Product color
	@FindBy(css = "td[class='cart_description'] small a")
	WebElement productcolor;

	// Product quantity
	@FindBy(css = "input[class^='cart_quantity_input']")
	WebElement productqty;

	// Product price
	@FindBy(css = "span[id^=total_product_price]")
	WebElement productprice;

	// Page Factory Init
	public ShoppingPage() {
		PageFactory.initElements(driver, this);
	}

	public String ActualMessageText;
	public String Actualproducttitle;
	public String Actualproductdetail;
	public String Actualproductcolorandsize;
	public String Actualproductqty;
	public String Actualproductprice;

	
	public void verifySoppingCart() {

		// Login to Shopping cart Application
		// Go to T-shirts and Add product 'Faded Short Sleeve T-shirts' to cart
		// Verify Product details on checkout page.
		signinlnk.click();
		username.sendKeys(prop.getProperty("username"));
		password.sendKeys(prop.getProperty("password"));
		loginbtn.click();

		WebDriverWait wait = new WebDriverWait(driver, 20);

		wait.until(ExpectedConditions.visibilityOf(tshirtlnk)).click();
		productlnk.click();
		addtocartlnk.click();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String fetchMessageText = js.executeScript("return arguments[0].lastChild.textContent;", messagetext)
				.toString();
		ActualMessageText = fetchMessageText.trim();

		checkoutlnk.click();
		Actualproducttitle = producttitle.getText();
		Actualproductdetail = productdetail.getText();
		Actualproductcolorandsize = productcolor.getText();
		Actualproductqty = productqty.getAttribute("value");
		Actualproductprice = productprice.getText();

	}

}
