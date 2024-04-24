package EducatorAuthentications;

import AdminArea.GetEducator;
import EducatorProfile.Educator_TestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import TestConfig.TestBase;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class SendEducator_OTP {
    Educator_TestData data = new Educator_TestData();
    GetEducator educator = new GetEducator();
    TestBase test = new TestBase();
    String Valid_body_request;
    String body_with_invalid_email = "{\"email\":\"user_example.com\",\"language\":\"en\"}";
    String body_with_invalid_lang;
    String body_with_missing_email = "{\"email\":\"\",\"language\":\"en\"}";
    Response Send_Educator_OTP;
    String Educator_Token = data.refresh_token;
    String educator_email;

    @When("Performing the Api of Send Educator OTP with valid data")
    public void Send_Educator_OTP() {
        educator.user_send_valid_educatorId();
        educator.Get_Educator();
        educator_email = educator.EducatorEmail;
        System.out.println(educator_email);
        Valid_body_request = "{\"email\":\""+ educator_email +"\",\"language\":\"en\"}";
        Send_Educator_OTP = test.sendRequest("POST", "/educators/auth/send-otp", Valid_body_request,Educator_Token);
    }
    @Then("I verify the appearance of status code 200 and OTP sent to email")
    public void Validate_Response_of_Send_Educator_OTP() {
        Send_Educator_OTP.prettyPrint();
        Send_Educator_OTP.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorAuthentication/SendEducator_OTP.json")))
                .body("message", hasToString("OTP sent to email"),"message_id",equalTo(2001),"duration",equalTo(300),"resend_duration",equalTo(300));
    }
    @Then("I verify the appearance of status code 429 and Rate Limit Exceeds")
    public void Verify_Sending_Too_Many_Requests() {
      Response Too_Many_Requests = Send_Educator_OTP;
      test.Validate_Error_Messages(Too_Many_Requests,429,"Rate limit exceeded",4291);
    }
    @When("Performing the Api of Send Educator OTP with Invalid email")
    public void Send_Educator_OTP_With_Invalid_Email() {
        Send_Educator_OTP = test.sendRequest("POST", "/educators/auth/send-otp", body_with_invalid_email,Educator_Token);
    }
    @When("Performing the Api of Send Educator OTP with Invalid language")
    public void Send_Educator_OTP_With_Invalid_Language() {
        educator.user_send_valid_educatorId();
        educator.Get_Educator();
        educator_email = educator.EducatorEmail;
        body_with_invalid_lang = "{\"email\":\""+ educator_email +"\",\"language\":\"english\"}";
        Send_Educator_OTP = test.sendRequest("POST", "/educators/auth/send-otp", body_with_invalid_lang,Educator_Token);

    }
    @Then("I verify the appearance of status code 400 and Invalid Invalid Language")
    public void validate_Response_with_Invalid_Lang(){
        Response Invalid_lang = Send_Educator_OTP;
        test.Validate_Error_Messages(Invalid_lang,HttpStatus.SC_BAD_REQUEST,"Send OTP failed. BadRequest",40011);
    }
    @When("Performing the Api of Send Educator OTP with missing email input")
    public void Send_Educator_OTP_With_Empty_Email() {

        Send_Educator_OTP = test.sendRequest("POST", "/educators/auth/send-otp", body_with_missing_email,Educator_Token);
    }
    @Then("I verify the appearance of status code 400 and Invalid email format")
    public void validate_Response_with_missing_email(){
        Response missing_email = Send_Educator_OTP;
        test.Validate_Error_Messages(missing_email, HttpStatus.SC_BAD_REQUEST,"Invalid email format",4003);

    }



}
