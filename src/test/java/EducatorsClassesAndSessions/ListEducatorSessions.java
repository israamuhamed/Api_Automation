package EducatorsClassesAndSessions;

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

public class ListEducatorSessions {
    TestBase test = new TestBase();
    GetSession session =new GetSession();
    GetClass classData =new GetClass();
    CreateEducator educator = new CreateEducator();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Long SessionID;
    String educator_Email;
    Long educatorID;
    Response List_Educator_Sessions;
    String EducatorRefreshToken;
    String OTP;
    Long Class_ID;
    Long class_id;
    Integer class_block_number;
    Long session_id;
    Integer session_duration_in_minutes;
    String class_title;
    String session_title;

    @And("Get Educator Sessions from database")
    public void get_Educator_sessions_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select *  from sessions s \n" +
                "join classes_subjects_sessions css \n" +
                "on s.session_id = css.session_id \n" +
                "join classes_subjects cs \n" +
                "on cs.class_subject_id = css.class_subject_id \n" +
                "join classes c \n" +
                "on c.class_id = cs.class_id \n" +
                "where cs.class_id = "+ Class_ID +" ");


        while (resultSet.next()) {
            class_id = resultSet.getLong("class_id");
            class_title = resultSet.getString("class_title");
            class_block_number = resultSet.getInt("class_block_number");
            session_id = resultSet.getLong("session_id");
            session_title = resultSet.getString("session_title");
            session_duration_in_minutes = resultSet.getInt("session_duration_in_minutes");
            System.out.println("class_id**********" + class_id);
        }

    }

    @Given("User Create Classes and Session for Educator to list Sessions for educator")
    public void Create_Session_for_educator ()throws SQLException {
        session.user_send_valid_sessionID();
        session.Get_Session();
        educatorID = session.educatorID;

    }

    @When("Performing the Api of list sessions for educator")
    public void List_Educator_sessions() throws SQLException {
        SessionID = session.SessionID;
        Class_ID = session.ClassID;

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
        List_Educator_Sessions = test.sendRequest("GET", "/educators/{educator_id}/classes/{class_id}/sessions", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 200 and Educator Sessions data returned")
    public void Validate_Response_of_Educator_sessions_returned_successfully() {
        List_Educator_Sessions.prettyPrint();
        List_Educator_Sessions.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorClassesAndSession/ListEducatorSessions.json")))
                .body("class_id",equalTo(class_id),"class_title",hasToString(class_title),"sessions.class_block_number",hasItem(equalTo(null)),
                        "sessions.session_id",hasItem(equalTo(session_id)),"sessions.session_title",hasItem(hasToString(session_title)),
                        "sessions.session_duration_in_minutes",hasItem(equalTo(session_duration_in_minutes)));

    }

    @Given("User Create Classes only for Educator to list Sessions")
    public void Create_classes_for_educator() {
        classData.user_send_valid_classId();
        classData.Get_Class();
        educatorID = classData.EducatorID;
        Class_ID = classData.classID;

    }

    @When("Performing the Api of list sessions for educator with empty data")
    public void List_Educator_sessions_empty() throws SQLException {

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
        List_Educator_Sessions = test.sendRequest("GET", "/educators/{educator_id}/classes/{class_id}/sessions", null,EducatorRefreshToken);
    }



    @Then("I verify the appearance of status code 200 and Educator Sessions data is empty")
    public void Validate_Response_of_Educator_sessions_returned_empty() {
        List_Educator_Sessions.prettyPrint();
        List_Educator_Sessions.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorClassesAndSession/ListEducatorSessions.json")))
                .body("class_id",equalTo(class_id),"class_title",hasToString(class_title),"sessions",empty());

    }


    @And("Get Educator Classes from database")
    public void get_Educator_classes_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select *  from classes c\n" +
                "join classes_subjects cs \n" +
                "on c.class_id = cs.class_id \n" +
                "where c.class_id = "+ Class_ID +" \n");


        while (resultSet.next()) {
            class_id = resultSet.getLong("class_id");
            class_title = resultSet.getString("class_title");
        }

    }

    @Given("User Create Educator without assigning any classes to him")
    public void Create_educator_only() {
        educator.Create_Educator();
        educatorID = educator.Educator_ID;

    }

    @When("Performing the Api of list sessions for educator with invalid class")
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
        pathParams.put("class_id","123456789076");
        List_Educator_Sessions = test.sendRequest("GET", "/educators/{educator_id}/classes/{class_id}/sessions", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 404 and class is not exist")
    public void Validate_Response_of_Educator_sessions_with_invalid_class() {
        Response Invalid_class = List_Educator_Sessions;
        test.Validate_Error_Messages(Invalid_class,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
    }

    @When("Performing the Api of list sessions for educator with unauthorized educator")
    public void List_Educator_sessions_unauthorized_Educator() {
        pathParams.put("educator_id", educatorID);
        pathParams.put("class_id","123456789076");
        List_Educator_Sessions = test.sendRequest("GET", "/educators/{educator_id}/classes/{class_id}/sessions", null,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 403 and unauthorized educator")
    public void Validate_Response_of_Educator_sessions_with_unauthorized_user() {
        Response Invalid_class = List_Educator_Sessions;
        test.Validate_Error_Messages(Invalid_class,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);

    }



}
