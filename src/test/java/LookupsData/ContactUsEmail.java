package LookupsData;

import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import StudentClasses.Student_TestData;

import java.io.File;

import static org.hamcrest.Matchers.hasToString;

public class ContactUsEmail {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Response Send_ContactUs;
    String valid_body_request = "{\"first_name\":\"John\",\"last_name\":\"Doe\",\"email\":\"student_parent@nagwa.com\",\"email_body\":\"Automation Test For nagwa.com\"}";

    @Given("Performing the Api of Sent Contact Us Email")
    public void Send_ContactUS_Email() {
        Send_ContactUs = test.sendRequest("POST", "/contact-us/email", valid_body_request,data.Parent_refreshToken );

    }

    @Then("I verify the appearance of status code 200 and message sent successfully")
    public void Validate_Response_of_Send_Contactus() {
        Send_ContactUs.prettyPrint();
        Send_ContactUs.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/LookupsData/ContactusEmail.json")))
                .body("message", hasToString("Your email has been successfully sent. We will get back to you shortly."));
    }

    @Given("Performing the Api of Sent Contact Us Email with invalid email")
    public void Send_ContactUS_InvalidEmail() {
        String Invalid_email = "{\"first_name\":\"John\",\"last_name\":\"Doe\",\"email\":\"@nagwa.com\",\"email_body\":\"I would like to inquire about...\"}";
        Send_ContactUs = test.sendRequest("POST", "/contact-us/email", Invalid_email, data.Parent_refreshToken);

    }
    @Then("I verify the appearance of status code 400 and invalid email message")
    public void Validate_Response_of_invalid_email_contactus() {
        test.Validate_Error_Messages(Send_ContactUs,HttpStatus.SC_BAD_REQUEST,"Invalid email format.",4003);
    }

    @Given("Performing the Api of Sent Contact Us Email with invalid body")
    public void Send_ContactUS_InvalidBody() {
        String Invalid_body = "{\"first_name\":,\"last_name\":,\"email\":\"@nagwa.com\",\"email_body\":\"I would like to inquire about...\"}";
        Send_ContactUs = test.sendRequest("POST", "/contact-us/email", Invalid_body, data.Parent_refreshToken);

    }
    @Then("I verify the appearance of status code 400 and invalid body message")
    public void Validate_Response_of_invalidBody() {
        test.Validate_Error_Messages(Send_ContactUs,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

}
