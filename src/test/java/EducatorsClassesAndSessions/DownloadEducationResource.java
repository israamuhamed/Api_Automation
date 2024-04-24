package EducatorsClassesAndSessions;

import AdminArea.CreateEducationalResource;
import AdminArea.CreateEducator;
import AdminArea.GetClass;
import AdminArea.GetSession;
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

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class DownloadEducationResource {
    TestBase test = new TestBase();
    GetSession session =new GetSession();
    CreateEducator educator = new CreateEducator();
    CreateEducationalResource resource = new CreateEducationalResource();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    String educator_Email;
    Long educatorID;
    Response Download_Educator_resources;
    String EducatorRefreshToken;
    String OTP;
    Long Class_ID;
    Integer session_educational_resource_id;
    Long session_id;
    String educational_resource_md5;
    Integer educational_resource_type_id;
    String educational_resource_type;
    Long educational_resource_id;
    Long ResourceId;

    @Given("User Create Sessions and Educational Resources for Educator")
    public void Create_Session_with_resources_for_educator () throws SQLException, InterruptedException {
        session.user_send_valid_sessionID();
        session.Get_Session();
        educatorID = session.educatorID;
        session_id = session.SessionID;
        Class_ID = session.ClassID;
        System.out.println("session_id" + session_id);
        resource.Create_new_educational_resources();
        ResourceId = resource.resourceId;
        System.out.println("ResourceId "+ResourceId);
        String valid_body = "{\"sessions_ids\":["+ session_id +"],\"educational_resource_id\":"+ ResourceId +"}";
        test.sendRequest("POST", "/admin/assign-educational-resource", valid_body, data.Admin_Token);
    }

    @When("Performing the Api Download Educator Resources")
    public void Download_educator_resources() throws SQLException {

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

        pathParams.put("educator_id", educatorID);
        pathParams.put("session_id",session_id);

        String valid_body = "{\"educational_resources_ids\":["+ ResourceId +"]}";
        Download_Educator_resources = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/download-resources", valid_body,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 200 and Educator Resources Downloaded")
    public void Validate_Response_of_Download_Resources() {
        Download_Educator_resources.prettyPrint();
        Download_Educator_resources.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorClassesAndSession/DownloadEducationalResources.json")))
                .body("resources.educational_resource_id",hasItem(hasToString(educational_resource_id.toString())),
                        "resources.educational_resource_md5",hasItem(hasToString(educational_resource_md5)),
                        "resources.educational_resource_type_id",hasItem(equalTo(educational_resource_type_id)),
                        "resources.session_educational_resource_id",hasItems(equalTo(session_educational_resource_id)));
    }

    @And("Get resources data from database")
    public void get_Educator_resources_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database(" select * from sessions_educational_resources ser\n" +
                "    join educational_resources er\n" +
                "    on er.educational_resource_id = ser.educational_resource_id\n" +
                "    join educational_resources_types ert\n" +
                "    on er.educational_resource_type_id  = ert.educational_resource_type_id\n" +
                "    where er.educational_resource_id ="+ ResourceId +"");

        while (resultSet.next()) {
            educational_resource_id = resultSet.getLong("educational_resource_id");
            educational_resource_md5 = resultSet.getString("educational_resource_md5");
            educational_resource_type_id = resultSet.getInt("educational_resource_type_id");
            educational_resource_type = resultSet.getString("educational_resource_type");
            session_educational_resource_id = resultSet.getInt("session_educational_resource_id");


        }
    }

    @Given("User Create Educator without assigning any classes")
    public void Create_educator_only() {
        educator.Create_Educator();
        educatorID = educator.Educator_ID;

    }

    @When("Performing the Api of download resources with invalid session id")
    public void List_Educator_sessions_invalid_class() throws SQLException {

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

        pathParams.put("educator_id", educatorID);
        pathParams.put("session_id","123456789076");
        String body_request = "{\"educational_resources_ids\":[123456789098]}";

        Download_Educator_resources = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/download-resources", body_request,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 404 and session id is not found")
    public void Validate_Response_of_Educator_sessions_with_invalid_class() {
        Response Invalid_session = Download_Educator_resources;
        test.Validate_Error_Messages(Invalid_session,HttpStatus.SC_NOT_FOUND,"Session not found or not eligible for display.",4048);

    }

    @Given("Performing the Api of download educational resources with unauthorized educator")
    public void List_Educator_sessions_unauthorized_Educator() {
        pathParams.put("educator_id", "123456789012");
        pathParams.put("session_id","123456789076");
        String body_request = "{\"educational_resources_ids\":[123456789098]}";
        Download_Educator_resources = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/download-resources", body_request,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 403 and the educator is unauthorized")
    public void Validate_Response_of_Educator_sessions_with_unauthorized_user() {
        Response Invalid_token = Download_Educator_resources;
        test.Validate_Error_Messages(Invalid_token,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);

    }




}
