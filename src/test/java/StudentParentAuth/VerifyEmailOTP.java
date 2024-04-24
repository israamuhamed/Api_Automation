package StudentParentAuth;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class VerifyEmailOTP {
    Educator_TestData data = new Educator_TestData();
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    SendEmailOTP send = new SendEmailOTP();

    public String OTP;
    public String Email;
    boolean IsVerified;
    public String create_account_token;
    public String studentFirstName ;
    public String studentLastName ;
    public String studentEmail;
    Response Verify_Student_OTP;
    public void get_Student_OTP_from_database() throws SQLException {
        send.Send_Student_OTP();
        Email = send.student_email;
        ResultSet resultSet = Connect.Connect_to_OTP_Database("select \"Email\" ,\"Otp\" ,\"IsVerified\"  from \"UserMailOtp\" umo where \"Email\" ='"+ Email +"'\n");

        while (resultSet.next()) {
            Email = resultSet.getString("Email");
            OTP = resultSet.getString("Otp");
            IsVerified = resultSet.getBoolean("IsVerified");
            System.out.println("Email: " + Email + "OTP: " + OTP + " is verified" + IsVerified);
        }
    }

    public void get_Student_data_from_database() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from students s where student_email = \'"+Email+"\'");

        while (resultSet.next()) {
            studentFirstName = resultSet.getString("student_first_name");
            studentLastName = resultSet.getString("student_last_name");
            studentEmail = resultSet.getString("student_email");
            System.out.println("student_first_name: " + studentFirstName + " student_last_name: " + studentLastName + " student_email: " + studentEmail);
        }
    }


    public void getting_student_otp_email_from_db() throws SQLException {
        get_Student_OTP_from_database();
        get_Student_data_from_database();
}

    @When("Performing the Api of Verify Student OTP with valid data")
    public String Verify_Student_OTP() throws SQLException {
        getting_student_otp_email_from_db();
        String Valid_body_request = "{\"email\":\""+ Email +"\",\"otp\":\"" + OTP + "\"}";
        Verify_Student_OTP = test.sendRequest("POST", "/auth/verify-otp", Valid_body_request,data.Admin_Token);
            return create_account_token = Verify_Student_OTP.then().extract().path("create_account_token");
    }

    @Then("I verify the appearance of status code 200 and student authenticated")
    public void Validate_Response_of_verify_Student_OTP() {
        Verify_Student_OTP.prettyPrint();
            Verify_Student_OTP.then()
                    .statusCode(HttpStatus.SC_OK)
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentParentAuthSchemas/VerifyNewStudentOTP.json")))
                    .body("message", hasToString("New user, please create an account."),"message_id",hasToString("2002"));


    }

    @Given("Performing the Api of Verify Student OTP with Invalid OTP")
    public void Verify_Student_OTP_with_Invalid_OTP() throws SQLException {
        getting_student_otp_email_from_db();
        String Invalid_OTP = "{\"email\":\""+ Email +"\",\"otp\":\"123456\"}";
        Verify_Student_OTP = test.sendRequest("POST", "/auth/verify-otp", Invalid_OTP,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 401 and Invalid student OTP")
    public void Validate_Response_Invalid_Student_OTP() {
        Response Invalid_OTP = Verify_Student_OTP;
        test.Validate_Error_Messages(Invalid_OTP,HttpStatus.SC_UNAUTHORIZED,"Invalid OTP.",4014);
    }

}
