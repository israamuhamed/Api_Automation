package StudentParentAuth;

import EducatorProfile.Educator_TestData;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class SendEmailOTP {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    Faker fakeDate =new Faker();
    String student_userName = fakeDate.name().username();
    String student_email =student_userName + "@nagwa.com" ;
    String Valid_body_request;
    Response Send_Student_OTP ;

    @When("Performing the Api of Send Student OTP with valid data")
    public String Send_Student_OTP() {
        Valid_body_request = "{\"email\":\""+ student_email +"\",\"language\":\"en\"}";
        Send_Student_OTP = test.sendRequest("POST", "/auth/send-otp", Valid_body_request,data.Admin_Token);
        return student_email;
    }

    @Then("I verify the appearance of status code 200 and OTP sent to student mail")
    public void Validate_Response_of_send_OTP_to_student() {
        Send_Student_OTP.prettyPrint();
        Send_Student_OTP.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentParentAuthSchemas/SendStudentOTP.json")))
                .body("message", hasToString("OTP sent to email"),"message_id",equalTo(2001),"duration",equalTo(300),"resending_duration",equalTo(300));
    }

    @Then("I verify the appearance of status code 429 and rate exceeds")
    public void Validate_Response_of_send_OTP_more_times() {
        Response Too_Many_Requests = Send_Student_OTP;
        test.Validate_Error_Messages(Too_Many_Requests,429,"Rate limit exceeded",4291);
    }

    @When("Performing the Api of Send Student OTP with Invalid email")
    public void Send_Student_OTP_With_Invalid_Email() {
        String body_with_invalid_email = "{\"email\":\"user_example.com\",\"language\":\"en\"}";
        Send_Student_OTP = test.sendRequest("POST", "/auth/send-otp", body_with_invalid_email,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 400 and Invalid student email format")
    public void validate_Response_with_missing_email(){
        Response missing_email = Send_Student_OTP;
        test.Validate_Error_Messages(missing_email, HttpStatus.SC_BAD_REQUEST,"Invalid email format",4003);

    }

    @When("Performing the Api of Send Send OTP with Invalid language")
    public void Send_Educator_OTP_With_Invalid_Language() {
        String body_with_invalid_lang = "{\"email\":\"test.automation@nagwa.com\",\"language\":\"english\"}";
        Send_Student_OTP = test.sendRequest("POST", "/auth/send-otp", body_with_invalid_lang,data.Admin_Token);
    }
    @Then("I verify the appearance of status code 400 and Invalid Language")
    public void validate_Response_with_Invalid_Lang(){
        Response Invalid_lang = Send_Student_OTP;
        test.Validate_Error_Messages(Invalid_lang,HttpStatus.SC_BAD_REQUEST,"Send OTP failed. BadRequest",40011);
    }

    @When("Performing the Api of Send Student OTP with missing email input")
    public void Send_Student_OTP_With_Empty_Email() {
        String body_with_missing_email = "{\"email\":\"\",\"language\":\"en\"}";
        Send_Student_OTP = test.sendRequest("POST", "/auth/send-otp", body_with_missing_email,data.Admin_Token);
    }

}
