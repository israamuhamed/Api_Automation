package EducatorAuthentications;

import EducatorProfile.Educator_TestData;
import EducatorProfile.GetEducatorProfile;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import TestConfig.TestBase;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import TestConfig.Database_Connection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;


public class VerifyEducator_OTP {
    Educator_TestData data = new Educator_TestData();
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    GetEducatorProfile profile = new GetEducatorProfile();
    SendEducator_OTP educatorOTP = new SendEducator_OTP();
    String Educator_Token = data.refresh_token;
    String firstNameEducator;
    String lastNameEducator ;
    String OTP;
    String Email;
    String educator_mail;
    public String Educator_Refresh_Token;

    Response Verify_Educator_OTP;
    public void get_educator_OTP_from_database() throws SQLException {
        educatorOTP.Send_Educator_OTP();
        educator_mail = educatorOTP.educator_email;
        ResultSet resultSet = Connect.Connect_to_OTP_Database("select \"Email\" ,\"Otp\"  from \"UserMailOtp\" umo where \"Email\" = '"+ educator_mail +"'");

        while (resultSet.next()) {
            Email = resultSet.getString("Email");
            OTP = resultSet.getString("Otp");
            System.out.println("Email: " + Email + "OTP: " + OTP );
        }
    }

    @And("Get Educator OTP and mail from database")
    public void getting_educator_otp_email_from_db() throws SQLException {
        get_educator_OTP_from_database();
        ResultSet resultSet = Connect.connect_to_database("select  educator_first_name, educator_last_name, educator_email from public.educators where educator_email ='"+ educator_mail +"'");

        while (resultSet.next()) {
            firstNameEducator = resultSet.getString("educator_first_name");
            lastNameEducator = resultSet.getString("educator_last_name");
        }

    }

    @When("Performing the Api of Verify Educator OTP with valid data")
    public String Verify_Educator_OTP() {
        String Valid_body_request = "{\"email\":\""+ Email +"\",\"otp\":\"" + OTP + "\"}";
        Verify_Educator_OTP = test.sendRequest("POST", "/educators/auth/verify-otp", Valid_body_request,Educator_Token);
        System.out.println(Valid_body_request);
        return  Educator_Refresh_Token = Verify_Educator_OTP.then().extract().path("tokens.refresh_token");
    }

    @Then("I verify the appearance of status code 200 and user authenticated")
    public void Validate_Response_of_verify_Educator_OTP() {
        Verify_Educator_OTP.prettyPrint();
        Verify_Educator_OTP.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorAuthentication/VerifyEducator_OTP.json")))
                .body("message", hasToString("Existing user authenticated."),"message_id",equalTo(2001),
                        "data.email",hasToString(Email),"data.first_name",hasToString(firstNameEducator),"data.last_name",hasToString(lastNameEducator),
                        "data.role",hasToString("educator"));
    }

    @Given("Performing the Api of Verify Educator OTP with Invalid OTP")
    public void Verify_Educator_OTP_with_Invalid_OTP() {
        String Invalid_OTP = "{\"email\":\""+ Email +"\",\"otp\":\"123456\"}";
        Verify_Educator_OTP = test.sendRequest("POST", "/educators/auth/verify-otp", Invalid_OTP,Educator_Token);
    }

    @Then("I verify the appearance of status code 401 and Invalid OTP")
    public void Validate_Response_Invalid_Educator_OTP() {
        Response Invalid_OTP = Verify_Educator_OTP;
        test.Validate_Error_Messages(Invalid_OTP,HttpStatus.SC_UNAUTHORIZED,"Invalid OTP.",4014);
    }

}
