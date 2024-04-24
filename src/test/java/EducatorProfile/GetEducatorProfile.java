package EducatorProfile;

import AdminArea.CreateEducator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import TestConfig.Database_Connection;
import TestConfig.TestBase;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.hasToString;
public class GetEducatorProfile {
    Educator_TestData data = new Educator_TestData();
    TestBase test = new TestBase();
    CreateEducator educator = new CreateEducator();
    Database_Connection Connect = new Database_Connection();
    public String Educator_Id = data.educator_id;
    public String Deleted_Educator = data.deleted_educator;
    public  String deleted_educator_token = data.refresh_token_for_deletedEducator;
     public String notActive_educator_token = data.refresh_token_for_notActiveEducator;
    public String educatorFirstName ;
    public String educatorLastName ;
    public String educatorEmail;
    Long educatorID;
    String educator_Email;
    String OTP;
    String EducatorRefreshToken;
    public Response Get_Educator_Profile;
    public Response Deleted_Educator_token;
    public Response NotActive_Educator_token;
    public Response unauthorized_Educator;
    public Map<String, Object> pathParams = test.pathParams;

    @Given("Getting the Educator Data From Database")
    public void get_educator_data_from_database() throws SQLException {
        educator.Create_Educator();
        educatorID = educator.Educator_ID;

        ResultSet GetEducatorEmail = Connect.connect_to_database("select educator_email from public.educators e where educator_id ="+ educatorID +"");
        while (GetEducatorEmail.next()) {
            educator_Email = GetEducatorEmail.getString("educator_email");};

        Response testOTP = test.sendRequest("POST", "/educators/auth/send-otp", "{\"email\":\""+ educator_Email +"\",\"language\":\"en\"}",data.Admin_Token);
        testOTP.prettyPrint();

        ResultSet GetEducatorOTP = Connect.Connect_to_OTP_Database("select \"Email\" ,\"Otp\"  from \"UserMailOtp\" umo where \"Email\" = '"+ educator_Email +"'");
        while (GetEducatorOTP.next()) {
            OTP = GetEducatorOTP.getString("Otp");};

        Response VerifyOTP = test.sendRequest("POST", "/educators/auth/verify-otp", "{\"email\":\""+ educator_Email +"\",\"otp\":\""+ OTP +"\"}",data.Admin_Token);
        VerifyOTP.prettyPrint();

        EducatorRefreshToken = VerifyOTP.then().extract().path("tokens.refresh_token");

        ResultSet resultSet = Connect.connect_to_database("select  educator_first_name, educator_last_name, educator_email from public.educators where educator_id ="+ educatorID + "");

        while (resultSet.next()) {
            educatorFirstName = resultSet.getString("educator_first_name");
            educatorLastName = resultSet.getString("educator_last_name");
            educatorEmail = resultSet.getString("educator_email");
            System.out.println("Educator_first_name: " + educatorFirstName + "Educator_last_name: " + educatorLastName + "Educator_email: " + educatorEmail);
        }
    }

    @When("Performing the Api of Get Educator Profile")
    public void Get_Educator_Profile() {
        Get_Educator_Profile = test.sendRequest("GET", "/educators/{educator_id}/profile", null,EducatorRefreshToken);
    }
    @Given("User Send valid educator Id")
    public void Sending_valid_EducatorId() {

        pathParams.put("educator_id",educatorID);
    }
    @Then("I verify the appearance of status code 200 and Educator data returned")
    public void Validate_Response_of_Getting_Educator_Profile() {
        Get_Educator_Profile.prettyPrint();
        Get_Educator_Profile.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorProfileSchemas/EducatorProfile.json")))
                .body("educator_first_name", hasToString(educatorFirstName), "educator_last_name", hasToString(educatorLastName), "educator_email", hasToString(educatorEmail));
    }
    @Given("User Send Invalid educator Id")
    public void user_send_invalid_educatorId()
    {pathParams.put("educator_id", "34325678622222444");
    }
    @When("performing the api with invalid educator id")
    public void send_notActive_educator_ID(){
        Get_Educator_Profile = test.sendRequest("GET", "/educators/{educator_id}/profile", null,data.refresh_token_for_notActiveEducator);
    }
    @Then("I verify the appearance of status code 400 and Educator Id not correct")
    public void Validate_Response_of_Invalid_EducatorId() {
        Response Educator_Profile = Get_Educator_Profile;
        test.Validate_Error_Messages(Educator_Profile,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }
    @Given("User Send not active educator")
    public void user_send_notActive_educatorId() {pathParams.put("educator_id", data.notActive_educator);
    }
    @When("performing the api with notActive educator token")
    public void send_notActive_educator_token(){
        NotActive_Educator_token = test.sendRequest("GET", "/educators/{educator_id}/profile", null,data.refresh_token_for_notActiveEducator);
    }
    @Then("I verify the appearance of status code 404 and Educator Id is not active")
    public void Validate_Response_of_notActive_EducatorId() {
         Response InActiveEducator = NotActive_Educator_token;
         test.Validate_Error_Messages(InActiveEducator,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active.",40413);
    }
    @Given("User Send deleted educator")
    public void user_send_deleted_educatorId() {pathParams.put("educator_id", Deleted_Educator);
    }
    @When("performing the api with deleted educator token")
    public void send_deleted_educator_token(){
        Deleted_Educator_token = test.sendRequest("GET", "/educators/{educator_id}/profile", null,deleted_educator_token);
    }
    @Then("I verify the appearance of status code 404 and Educator Id is deleted")
    public void Validate_Response_of_deleted_EducatorId() {
        Response DeletedEducator =Deleted_Educator_token;
        test.Validate_Error_Messages(DeletedEducator,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active.",40413);
    }
    @Given("User Send unauthorized educator")
    public void user_send_unauthorized_educatorId() {pathParams.put("educator_id",Educator_Id);
    }
    @When("performing the api with invalid token")
    public void send_unauthorized_educator(){
        unauthorized_Educator = test.sendRequest("GET", "/educators/{educator_id}/profile", null, notActive_educator_token);
    }
    @Then("I verify the appearance of status code 403 and Educator is unauthorized")
    public void Validate_Response_of_unauthorized_EducatorId() {
        Response unauthorizedEducator = unauthorized_Educator;
        test.Validate_Error_Messages(unauthorizedEducator,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

}
