package StudentHomeScreen;

import io.restassured.RestAssured;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class StudentWallet {
   public static String access_token;
   public String providerId;

    public static String generate_payment_access_token() {
        RestAssured.baseURI = "https://auth.nagwa.com";
        RequestSpecification request =
                RestAssured.given()
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .formParam("grant_type", "client_credentials")
                        .formParam("scope", "payment.api")
                        .formParam("client_id", "frontend.nagwa")
                        .formParam("client_secret", "vRfcYrpq8M7QncjxaQSEY29u4wkkk9GAsnJ6CZBQphJJ9SNP6MScjTFSHIpadx7c");

        Response response = request.when()
                .post("/connect/token");
        access_token = response.then().extract().path("access_token");

        if (access_token == null) {
            System.out.println("The payment access Token Api gives Error : Access Token is Null");
        }
        return access_token;
    }

    public String NagwaClasses_Checkout(Long userId) throws InterruptedException {
        generate_payment_access_token();
        String Checkout_body_request ="{\"paymentMethod\":\"Fawry\",\"amount\":100,\"currency\":\"EGP\",\"userId\":"+ userId +",\"clientId\":\"713138260143\"," +
                "\"firstName\":\"Ahmed\",\"lastName\":\"Mohamed\",\"email\":\"m@m.com\",\"mobile\":\"011523232\",\"description\":\"test\",\"language\":\"en\"}";

        RestAssured.baseURI = "https://beta-payment-checkout-gateways.nagwa.com";
        RequestSpecification request =
                RestAssured.given()
                        .header("Content-Type", "application/json")
                        .header("api-version",1)
                        .header("accept","text/plain")
                        .header("Authorization","bearer " + access_token);

        Response response = request.body(Checkout_body_request)
                .post("/nagwaclasses-checkout");
        Thread.sleep(20000);
        response.prettyPrint();
        return providerId = response.then().extract().path("providerId");
    }

    public void test_call_back() throws InterruptedException {
        RestAssured.baseURI = "https://beta-payment-checkout-gateways.nagwa.com";
        RequestSpecification request =
                RestAssured.given()
                        .header("Content-Type", "application/json")
                        .header("api-version", 1)
                        .header("accept", "*/*")
                        .header("Authorization", "bearer " + access_token)
                        .queryParam("referenceNumber", providerId);
        try {
            Response response = request.post("/test_call_back");
            response.then().statusCode(HttpStatus.SC_OK);

            String responseBody = response.getBody().asString();

            if (responseBody.isEmpty()) {
                System.out.println("Response body is empty.");
            } else {
                response.prettyPrint();
                providerId = response.then().extract().path("providerId");
            }
        } catch (JsonPathException e) {
            System.out.println("Failed to parse the JSON document. Exception message: " + e.getMessage());
        }

    }

}