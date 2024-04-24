package EducatorsClassesAndSessions;

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

public class ListEducatorClasses {
    TestBase test = new TestBase();
    GetSession session =new GetSession();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Long SessionID;
    String educator_Email;
    Long educatorID;
    Response List_Educator_Classes;
    String EducatorRefreshToken;
    String OTP;
    Long Class_ID;
    Long class_id;
    Long subject_id;
    Long grade_id;
    Long session_id;
    Integer session_duration_in_minutes;
    String class_title;
    String session_title;


    @And("Get Educator Classes and upcoming Session from database")
    public void get_Educator_classes_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select *  from sessions s \n" +
                "join classes_subjects_sessions css \n" +
                "on s.session_id = css.session_id \n" +
                "join classes_subjects cs \n" +
                "on cs.class_subject_id = css.class_subject_id \n" +
                "join classes c \n" +
                "on c.class_id = cs.class_id \n" +
                "join subjects s2 \n" +
                "on s2.subject_id = cs.subject_id \n" +
                "where cs.class_id = "+ Class_ID +" and s.session_start_date >= current_timestamp");


        while (resultSet.next()) {
            class_id = resultSet.getLong("class_id");
            class_title = resultSet.getString("class_title");
            subject_id = resultSet.getLong("subject_id");
            grade_id = resultSet.getLong("grade_id");
            session_id = resultSet.getLong("session_id");
            session_title = resultSet.getString("session_title");
            session_duration_in_minutes = resultSet.getInt("session_duration_in_minutes");
        }

    }
    @Given("User Create Classes and Session for Educator to list classes for educator")
    public void Create_Session_for_educator ()throws SQLException{
    session.user_send_valid_sessionID();
    session.Get_Session();
    educatorID = session.educatorID;

    }

    @When("Performing the Api of list classes for educator")
    public void List_Educator_classes() throws SQLException {
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
            List_Educator_Classes = test.sendRequest("GET", "/educators/{educator_id}/classes", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 200 and Educator classes data returned")
    public void Validate_Response_of_Educator_classes_returned_successfully() {
        List_Educator_Classes.prettyPrint();
        List_Educator_Classes.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorClassesAndSession/ListEducatorClasses.json")))
                .body("classes.class_id",hasItem(equalTo(class_id)),"classes.class_title",hasItem(hasToString(class_title)),"classes.class_subjects.subject_id",hasItems(hasItem(equalTo(subject_id))),
                        "classes.class_subjects.grade_id",hasItems(hasItem(equalTo(grade_id))),"upcoming_sessions.class_id",hasItem(equalTo(class_id)),"upcoming_sessions.class_title",hasItem(hasToString(class_title)),
                        "upcoming_sessions.session_id",hasItem(equalTo(session_id)),"upcoming_sessions.session_title",hasItem(hasToString(session_title)),
                        "upcoming_sessions.session_duration_in_minutes",hasItem(equalTo(session_duration_in_minutes)));

    }

    @When("Performing the Api of list classes for educator with educator not valid")
    public void List_Educator_classes_Educator_not_exist() {
        pathParams.put("educator_id", "educatorID");
        List_Educator_Classes = test.sendRequest("GET", "/educators/{educator_id}/classes", null, data.Admin_Token);
    }

    @Then("I verify the appearance of status code 400 and Educator ID is not correct")
    public void Validate_Response_of_Educator_classes_Educator_not_correct() {
        Response Invalid_ID = List_Educator_Classes;
        test.Validate_Error_Messages(Invalid_ID,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("User Send EducatorId  not exist to list classes")
    public void Sending_Educator_not_exist() throws SQLException {
        pathParams.put("educator_id",data.notActive_educator);
    }

    @When("list classes for educator with educator not exist")
    public void get_Student_wallet_student_not_exist() throws SQLException {
        List_Educator_Classes = test.sendRequest("GET", "/educators/{educator_id}/classes", null, data.refresh_token_for_notActiveEducator);
    }

    @Then("I verify the appearance of status code 404 and educator is not exist")
    public void Validate_Response_of_Get_student_Wallet_NotFound_student() {
        Response invalid_data = List_Educator_Classes;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active",40413);
    }
}
