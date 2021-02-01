package com.qa.test;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.base.TestBase;
import com.qa.pages.ShoppingPage;

public class ShoppingPageTest extends TestBase {

	ShoppingPage shoppingPage;

	//Fetching details from  TestBase Constructor
	public ShoppingPageTest() {
		super();
	}

	@BeforeMethod
	public void setup() {
		//Initializing Browser and webpage
		intialize();
		shoppingPage = new ShoppingPage();
	}

	@Test
	public void testShoppingCart() {
		
		//Expected Shopping cart values 
		String expectedMessageText = "Product successfully added to your shopping cart";		
		String expectedProductTitle = "Faded Short Sleeve T-shirts";
		String expectedProductDetail = "SKU : demo_1";
		String expectedProductColorAndSize = "Color : Orange, Size : S";
		String expectedProductQty = "1";
		String expectedProductPrice = "$16.51";

		//Executing verifyShoppingCart method to execute navigation steps
		shoppingPage.verifyShoppingCart();

		//Asserting Shopping cart data 
		SoftAssert softassert = new SoftAssert();
		softassert.assertEquals(shoppingPage.actualMessageText, expectedMessageText,"Incorrect Message text" );
		softassert.assertEquals(shoppingPage.actualProductTitle, expectedProductTitle, "Incorrect Product Title");
		softassert.assertEquals(shoppingPage.actualProductDetail, expectedProductDetail, "Incorrect Product details");
		softassert.assertEquals(shoppingPage.actualProductColorAndSize, expectedProductColorAndSize,
				"Incorrect Product Color and Size");
		softassert.assertEquals(shoppingPage.actualProductQty, expectedProductQty, "Incorrect Product Qty");
		softassert.assertEquals(shoppingPage.actualProductPrice, expectedProductPrice, "Incorrect Product Price");
		softassert.assertAll();
	}

	@AfterMethod
	public void tearDown() {
		//Closing browser
		driver.quit();
	}

}
