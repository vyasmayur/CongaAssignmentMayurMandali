package com.qa.apitest;

import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.asserts.*;

import com.qa.api.ApiBase;

import java.util.List;
import org.hamcrest.Matcher.*;
import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class RestAssuredTest extends ApiBase {

	public String id1;
	public String id2;
	
	//Fetching details from ApiBase Constructor
	public RestAssuredTest() {
		super();
	}
	
	@BeforeTest
	public void setup() {
		//Initializing API Details
		apiIntialize();
	}

	@Test(priority = 1)
	public void invalidKeyPostStation_401() {

		//Validate API without an API key should return the RESPONSE CODE: 401
		//and RESPONSE MESSAGE: "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info."
		RequestSpecification request = RestAssured.given().when().headers("Content-Type", "application/json");
		Response response = request.body(ApiBase.postBodyData("DEMO_TEST001", "Interview Station-123", 33.33, -111.43, 444))
							.post("stations");

		//Asserting response code and Message
		int actualStatusCode = response.statusCode();
		Assert.assertEquals(actualStatusCode, 401);
		JsonPath jsonPathEvaluator = response.jsonPath();
		String actualMessage = jsonPathEvaluator.get("message");
		Assert.assertEquals(actualMessage,
				"Invalid API key. Please see http://openweathermap.org/faq#error401 for more info.");
	}

	@Test(priority = 2)
	public void validatePostStation_201() {

		//Successfully register two stations with the API should return HTTP response code is 201. 
		RequestSpecification request = RestAssured.given().queryParam("appid", apiProp.getProperty("APIKey"))
				.headers("Content-Type", "application/json");

		Response response1 = request
				.body(ApiBase.postBodyData("DEMO_TEST001", "Interview Station-111", 33.33, -111.43, 444)).when()
				.post("stations");

		Response response2 = request
				.body(ApiBase.postBodyData("Interview1", "Interview Station-222", 33.44, -12.44, 444)).when()
				.post("stations");

		//Assert status code of API response and save response id in id1 
		JsonPath jsonPathEvaluator1 = response1.jsonPath();
		id1 = jsonPathEvaluator1.get("ID");
		int actualStatusCode1 = response1.statusCode();
		Assert.assertEquals(actualStatusCode1, 201, "Status code for first request is Wrong");

		//Assert status code of API response and save response id in id2
		JsonPath jsonPathEvaluator2 = response2.jsonPath();
		id2 = jsonPathEvaluator2.get("ID");
		int actualStatusCode2 = response2.statusCode();
		Assert.assertEquals(actualStatusCode2, 201, "Status code for Second request is Wrong");

	}

	@Test(priority = 3)
	public void validateGetStation() {

		//Verify that the stations were successfully stored in the DB 
		//and their values are the same as specified in the registration request in Test case 2. 
		Response response = RestAssured.given().param("appid", apiProp.getProperty("APIKey")).when().get("stations");
		
		List getId = response.getBody().path("id");
		List getExternalId = response.getBody().path("external_id");
		List getName = response.getBody().path("name");
		List getLongitude = response.getBody().path("longitude");
		List getLatitude = response.getBody().path("latitude");
		List getAltitude = response.getBody().path("altitude");

		
		boolean flag1 = false;
		boolean flag2 = false;
		
		for (int i = 0; i < getId.size(); i++) {
			if (getId.get(i).equals(id1)) {
				Assert.assertEquals(getExternalId.get(i), "DEMO_TEST001");
				Assert.assertEquals(getName.get(i), "Interview Station-111");
				Assert.assertEquals(getLongitude.get(i).toString(), "-111.43");
				Assert.assertEquals(getLatitude.get(i).toString(), "33.33");
				Assert.assertEquals(getAltitude.get(i).toString(), "444");
				flag1 = true;
			}
			if (getId.get(i).equals(id2)) {
				Assert.assertEquals(getExternalId.get(i), "Interview1");
				Assert.assertEquals(getName.get(i), "Interview Station-222");
				Assert.assertEquals(getLongitude.get(i).toString(), "-12.44");
				Assert.assertEquals(getLatitude.get(i).toString(), "33.44");
				Assert.assertEquals(getAltitude.get(i).toString(), "444");
				flag2 = true;
			}
		}

		Assert.assertTrue(flag1, "API Response not Matched for request 1");
		Assert.assertTrue(flag2, "API Response not Matched for request 2");

	}

	@Test(priority = 4)
	public void validateDeleteStation_204() {

		//Delete both of the created stations and verify that returned HTTP response is 204.
		RequestSpecification request = RestAssured.given().param("appid", apiProp.getProperty("APIKey"));
		Response response1 = request.delete("stations/" + id1);
		Response response2 = request.delete("stations/" + id2);

		//Assert status code of API response
		int actualStatusCode1 = response1.statusCode();
		int actualStatusCode2 = response2.statusCode();
		Assert.assertEquals(actualStatusCode1, 204, "API status not Matched for request 1");
		Assert.assertEquals(actualStatusCode2, 204, "API status not Matched for request 2");

	}

	@Test(priority = 5)
	public void validateDeleteStation_404() {

		//Repeat the test case 4 and verify that it should return HTTP response is 404
		//and message body should contains “message”: “Station not found". 
		RequestSpecification request = RestAssured.given().param("appid", apiProp.getProperty("APIKey"));
		Response response1 = request.delete("stations/" + id1);
		Response response2 = request.delete("stations/" + id2);

		//Assert status code of API response
		int actualStatusCode1 = response1.statusCode();
		int actualStatusCode2 = response1.statusCode();
		Assert.assertEquals(actualStatusCode1, 404, "API status not Matched for request 1");
		Assert.assertEquals(actualStatusCode2, 404, "API status not Matched for request 2" );

		//Assert API response Message
		JsonPath jsonPathEvaluator1 = response1.jsonPath();
		String actualMessage1 = jsonPathEvaluator1.get("message");
		JsonPath jsonPathEvaluator2 = response2.jsonPath();
		String actualMessage2 = jsonPathEvaluator2.get("message");
		Assert.assertEquals(actualMessage1, "Station not found", "API Message not Matched for request 1");
		Assert.assertEquals(actualMessage2, "Station not found", "API Message not Matched for request 2");
	}

}
