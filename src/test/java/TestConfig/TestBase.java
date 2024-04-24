package TestConfig;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestBase {

	public static String access_token;

	public Map<String,Object> pathParams = new HashMap<String, Object>();

	public static String  generate_access_token(String refresh_token) {
		EnvironmentSetup.setEnvironment(EnvironmentSetup.env);
		RequestSpecification request =
				RestAssured.given()
						.header("Content-Type","application/json")
						.header("Authorization",refresh_token);

		Response response = request.when()
				.post("/token/refresh");
		access_token = response.then().extract().path("access_token");

		if (access_token == null) {
			System.out.println("The Refresh Token Api gives Error : Access Token is Null");
		}

		return access_token;
	}

	public Response sendRequest(String method, String endpoint,Object requestBody,String refresh_token ) {
		String Access_token = generate_access_token(refresh_token);
		RequestSpecification request = RestAssured.given()
				.header("Content-Type", "application/json")
				.header("Authorization", Access_token);

		if (pathParams != null && !pathParams.isEmpty()) {
			request.pathParams(pathParams);
		}

		switch (method.toUpperCase()) {
			case "POST":
				if (requestBody != null ) {
					return request.body(requestBody).when().post(endpoint);
				} else {
					return request.when().post(endpoint);
				}
			case "GET":
				return request.when().get(endpoint);
			case "PUT":
				if (requestBody != null ) {
					return request.body(requestBody).when().put(endpoint);
				} else {
					return request.when().put(endpoint);
				}
			case "DELETE":
				return request.when().delete(endpoint);
			case "PATCH":
				if (requestBody != null ) {
					return request.body(requestBody).when().patch(endpoint);
				} else {
					return request.when().patch(endpoint);
				}
			default:
				throw new IllegalArgumentException("Unsupported HTTP method: " + method);
		}
	}

	public void Validate_Error_Messages (Response response,Integer statusCode , String error_message ,Integer error_id ) {
		response.prettyPrint();
		response.then()
				.statusCode(statusCode)
				.assertThat()
				.body("error_message" ,containsString(error_message) ,"error_id" ,equalTo(error_id) );
	}



}