package EducatorsClassesAndSessions;

import AdminArea.CreateEducationalResource;
import AdminArea.CreateEducator;
import AdminArea.GetClass;
import AdminArea.GetSession;
import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
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
import static org.hamcrest.Matchers.hasToString;

public class EducatorSessionReport {
    TestBase test = new TestBase();
    GetSession session =new GetSession();
    GetClass classData =new GetClass();
    CreateEducator educator = new CreateEducator();
    CreateEducationalResource resource = new CreateEducationalResource();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    String educator_Email;
    Long educatorID;
    Response Get_Educator_Session_Report;
    String EducatorRefreshToken;
    String OTP;
    Long Class_ID;
    Long class_id;
    Long sessionId;
    Long session_id;
    Integer session_duration_in_minutes;
    String class_title;
    String session_title;
    Integer educational_resource_type_id;
    String educational_resource_type;
    Long educational_resource_id;
    String educational_resource_name;
    Long educator_id;
    Long ResourceId;

    @Given("User Create Classes and Session for Educator to Get Session Report")
    public void Get_Educator_Session_Report () throws SQLException, InterruptedException {
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
        Response assign = test.sendRequest("POST", "/admin/assign-educational-resource", valid_body, data.Admin_Token);
        assign.prettyPrint();
    }

    @When("Performing the Api of Get educator session report")
    public void Get_Educator_session_report() throws SQLException {

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
        pathParams.put("class_id",Class_ID);
        pathParams.put("session_id",session_id);

        Get_Educator_Session_Report = test.sendRequest("GET", "/educators/{educator_id}/classes/{class_id}/sessions/{session_id}/report", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 200 and report data returned successfully")
    public void Validate_Response_of_Get_Educator_Report() {
        Get_Educator_Session_Report.prettyPrint();
        Get_Educator_Session_Report.then()
                .statusCode(HttpStatus.SC_OK);

    }
}
