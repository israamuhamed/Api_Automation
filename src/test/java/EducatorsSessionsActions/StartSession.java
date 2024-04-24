package EducatorsSessionsActions;

import AdminArea.CreateEducationalResource;
import AdminArea.CreateEducator;
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

public class StartSession {
    TestBase test = new TestBase();
    GetSession session =new GetSession();
    CreateEducator educator = new CreateEducator();
    CreateEducationalResource resource = new CreateEducationalResource();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    String educator_Email;
    Long educatorID;
    Response Start_Session;
    String EducatorRefreshToken;
    String OTP;
    Long Class_ID;
    public Long session_id;
    Long ResourceId;
    String agora_resource_id;
    String agora_sid;

    @Given("User Create Classes and Session for Educator to start session")
    public void Create_Session_for_educator () throws SQLException, InterruptedException {
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

    @When("Performing the Api of Start Session")
    public Long Start_Session() throws SQLException {

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

        Start_Session = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/start", null,EducatorRefreshToken);
        return session_id = Start_Session.then().extract().path("session_id");
    }

    @And("Get Educator Session data from database")
    public void get_Educator_session_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from sessions s \n" +
                "join agora_sessions as2 \n" +
                "on s.session_id = as2.session_id \n" +
                "where s.session_id ="+ session_id +"");


        while (resultSet.next()) {
            session_id = resultSet.getLong("session_id");
            agora_resource_id = resultSet.getString("agora_resource_id");
            agora_sid = resultSet.getString("agora_sid");

        }
    }

    @Then("I verify the appearance of status code 200 and session started successfully")
    public void Validate_Response_of_start_session_successfully() {
        Start_Session.prettyPrint();
        Start_Session.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorsSessionsActions/StartSession.json")))
                .body("session_id",equalTo(session_id),"agora_resource_id",equalTo(agora_resource_id),"agora_sid",equalTo(agora_sid));
    }

    @When("Performing the Api of Start Session with session not exist")
    public void Start_Session_session_not_exist() throws SQLException {
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

        pathParams.put("educator_id", educatorID);
        pathParams.put("session_id","123456789012");

        Start_Session = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/start", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 404 and session not found for start")
    public void Validate_Response_start_session_with_invalid_session() {
        Response Invalid_session = Start_Session;
        test.Validate_Error_Messages(Invalid_session,HttpStatus.SC_NOT_FOUND,"Session not found or not eligible for display.",4048);

    }

    @When("Performing the Api of Start Session with educator not authorized")
    public void Start_Session_session_not_authorized_Educator() throws SQLException {
        pathParams.put("educator_id", "123456789012");
        pathParams.put("session_id","123456789012");
        Start_Session = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/start", null,data.refresh_token_for_deletedEducator);
    }

    @Then("I verify the appearance of status code 403 and educator unauthorized")
    public void Validate_Response_start_session_with_invalid_token() {
        Response Invalid_token = Start_Session;
        test.Validate_Error_Messages(Invalid_token,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);

    }



}
