package EducatorsSessionsActions;

import AdminArea.CreateSession;
import EducatorProfile.Educator_TestData;
import StudentClasses.Student_TestData;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class KickOutStudent {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Database_Connection connect = new Database_Connection();
    Educator_TestData adminData = new Educator_TestData();
    CreateSession session = new CreateSession();
    String student_token = data.Student_refresh_Token;
    Map<String, Object> pathParams = test.pathParams;
    String educator_Email;
    Response Kick_OUt;
    String EducatorRefreshToken;
    String OTP;
    public Long class_id;
    Long educator_Id;
    public Long session_id;
    Long student_Id = data.student_Id;

    @Given("student join started session")
    public void student_join_session () throws SQLException {
        session.Create_Session();
        class_id = session.Class_ID;
        session_id = session.sessionId;
        educator_Id = session.EducatorId;


        ResultSet GetEducatorEmail = connect.connect_to_database("select educator_email from public.educators e where educator_id ="+ educator_Id +"");
        while (GetEducatorEmail.next()) {
            educator_Email = GetEducatorEmail.getString("educator_email");};

        Response testOTP = test.sendRequest("POST", "/educators/auth/send-otp", "{\"email\":\""+ educator_Email +"\",\"language\":\"en\"}",adminData.Admin_Token);
        testOTP.prettyPrint();

        ResultSet GetEducatorOTP = connect.Connect_to_OTP_Database("select \"Email\" ,\"Otp\"  from \"UserMailOtp\" umo where \"Email\" = '"+ educator_Email +"'");
        while (GetEducatorOTP.next()) {
            OTP = GetEducatorOTP.getString("Otp");};

        Response VerifyOTP = test.sendRequest("POST", "/educators/auth/verify-otp", "{\"email\":\""+ educator_Email +"\",\"otp\":\""+ OTP +"\"}",adminData.Admin_Token);
        VerifyOTP.prettyPrint();
        EducatorRefreshToken = VerifyOTP.then().extract().path("tokens.refresh_token");

        Response Enroll_Student_Into_Class = test.sendRequest("POST", "/students/"+ student_Id +"/classes/"+ class_id +"/enroll", null,student_token);
        Enroll_Student_Into_Class.prettyPrint();

        Response Start_Session = test.sendRequest("POST", "/educators/"+ educator_Id +"/sessions/"+ session_id +"/start", null,EducatorRefreshToken);
        Start_Session.prettyPrint();
        Response joinSession =  test.sendRequest("POST", "/students/"+ student_Id +"/classes/"+ class_id +"/sessions/"+ session_id +"/join",null,student_token);
        joinSession.prettyPrint();

    }

    @When("Performing the Api of Kick Out Student From Session")
    public void  Kick_Out()  {
        pathParams.put("educator_id",educator_Id);
        pathParams.put("session_id",session_id);
        pathParams.put("student_id",student_Id);

        Kick_OUt = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/kickout/{student_id}",null,EducatorRefreshToken);
        Kick_OUt.prettyPrint();
    }

    @Then("I verify the appearance of status code 200 and student kicked out successfully")
    public void Validate_Response_of_end_session_successfully() {
        Kick_OUt.prettyPrint();
        Kick_OUt.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorsSessionsActions/KickOutStudent.json")))
                .body("session_id",equalTo(session_id),"message",hasToString("Student successfully kicked out from the session."),
                        "student_id",equalTo(student_Id));
    }

    @Then("I verify the appearance of status code 404 and student is not currently part of the session")
    public void Validate_Response_start_session_with_invalid_session() {
        Response Invalid_session = Kick_OUt;
        test.Validate_Error_Messages(Invalid_session,HttpStatus.SC_NOT_FOUND,"Session not found or student is not currently part of the session.",40412);

    }

    @When("Performing the Api of Kick Out Student From Session With session not exist")
    public void Kick_Out_Session_NotFound()  {
        Response EndSession =  test.sendRequest("POST", "/educators/"+educator_Id+"/sessions/"+session_id+"/end",null,EducatorRefreshToken);
        EndSession.prettyPrint();
        pathParams.put("educator_id",educator_Id);
        pathParams.put("session_id",session_id);
        pathParams.put("student_id",student_Id);

        Kick_OUt = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/kickout/{student_id}",null,EducatorRefreshToken);
        Kick_OUt.prettyPrint();
    }

    @Then("I verify the appearance of status code 422 and session is not eligible or ended")
    public void Validate_Response_kickOut_with_invalid_session() {
        Response Invalid_session = Kick_OUt;
        test.Validate_Error_Messages(Invalid_session,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Session not eligible or already ended.",4224);

    }

    @When("Performing the Api of Kick Out Student From Session With educator not exist")
    public void KickOut_Student_educator_NotFound()  {
        pathParams.put("educator_id",adminData.notActive_educator);
        pathParams.put("session_id",session_id);
        pathParams.put("student_id",student_Id);

        Kick_OUt = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/kickout/{student_id}",null,adminData.refresh_token_for_notActiveEducator);
        Kick_OUt.prettyPrint();
    }

    @Then("I verify the appearance of status code 404 and educator is not assigned to this session")
    public void Validate_Response_kickOut_with_Educator_notFound() {
        Response Invalid_session = Kick_OUt;
        test.Validate_Error_Messages(Invalid_session,HttpStatus.SC_FORBIDDEN,"Unauthorized access. Educator is not assigned to this session.",40311);

    }

}
