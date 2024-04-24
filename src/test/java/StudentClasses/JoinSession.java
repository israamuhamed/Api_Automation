package StudentClasses;

import AdminArea.CreateClass;
import AdminArea.CreateSession;
import EducatorProfile.Educator_TestData;
import EducatorsSessionsActions.KickOutStudent;
import EducatorsSessionsActions.StartSession;
import StudentProfile.CreateStudent;
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

public class JoinSession {
    TestBase test = new TestBase();
    Student_TestData student = new Student_TestData();
    CreateStudent studentData = new CreateStudent();
    Educator_TestData adminData = new Educator_TestData();
    CreateClass classData = new CreateClass();
    CreateSession session = new CreateSession();
    StartSession sessionData = new StartSession();
    KickOutStudent student_KickedOut = new KickOutStudent();
    String student_token = student.Student_refresh_Token;
    Database_Connection connect = new Database_Connection();
    Long student_Id = student.student_Id;
    Long session_id;
    Long class_id;
    Long educator_Id;
    String educator_Email;
    String EducatorRefreshToken;
    String OTP;
    Map<String,Object> pathParams = test.pathParams;
    public Response joinSession ;
    public void startSessionForJoin() throws SQLException {
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
    }
    @When("Performing the Api of Joining Session")
    public void Join_Session() {
        joinSession =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/join",null,student_token);
    }
    @Given("User Send The Post Request Of join session")
    public void join_session_In_Enrolled_Class() throws SQLException {
        startSessionForJoin();
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @Then("The Response should contains status code 200 and correct session id")
    public void Validate_Response_of_session_In_Enrolled_Class (){
        joinSession.prettyPrint();
        joinSession.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/JoinSession.json")))
                .body("session_id" ,hasToString(session_id.toString()));
    }
    @Given("User Send Valid StudentId And ClassId That He Haven't Enrolled In")
    public void unauthorized_student () throws SQLException {
        startSessionForJoin();
        pathParams.put("student_id", "123456789987");
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @Then("The Response for join session Should Contain Status Code 403 And Error Message Unauthorized")
    public void Validate_Response_for_unauthorized_student (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("Student Join Session IS not Exist")
    public void Session_Not_Found () throws SQLException {
        startSessionForJoin();
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", "123456789987");
    }
    @Then("The Response Should Contain Status Code 404 And Error Message That Session Doesn't Exist")
    public void Validate_Response_For_Not_Found_Session (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_NOT_FOUND,"session not found or not eligible for display.",4048);
    }
    @Given("User send class id that not exist")
    public void Class_Not_Found () throws SQLException {
        startSessionForJoin();
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", "123456789098");
        pathParams.put("session_id",session_id);
    }
    @Then("The Response Should Contain Status Code 404 And Error Message That Class Doesn't Exist")
    public void Validate_Response_For_Class_Not_Exist (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
    }
    @Given("User Send InActive StudentId")
    public void Student_Not_Found_OR_NotActive () throws SQLException {
        student_token = student.student_refreshToken_not_exist;
        student_Id = student.student_not_exist;
        startSessionForJoin();
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id",session_id);
    }
    @Then("The Response Should Contain Status Code 403 And Error Message Student Is Deactivated or Not Exist")
    public void Validate_Response_For_Student_NotActive (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_NOT_FOUND,"Student with the specified ID does not exist or is not active.",4041);
        }
    @Given("User Send Ended SessionId")
    public void Ended_Session () throws SQLException {
        startSessionForJoin();
        Response End_Session = test.sendRequest("POST", "/educators/"+ educator_Id +"/sessions/"+ session_id +"/end", null,EducatorRefreshToken);
        End_Session.prettyPrint();

        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id",session_id);
    }
    @Then("The Response Should Contains Status Code 422 And Error Message Session Is Ended")
    public void Validate_Response_For_Ended_Session (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. session is ended.",4224);
    }
    @Given("User Send NotStarted SessionId")
    public void Not_Started_Session () throws SQLException {
        session.Create_Session();
        class_id = session.Class_ID;
        session_id = session.sessionId;
        test.sendRequest("POST", "/students/"+ student_Id +"/classes/"+ class_id +"/enroll", null,student_token);

        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id",session_id);
    }
    @Then("The Response Should Contains Status Code 422 And Error Message Session Haven't Started")
    public void Validate_Response_For_Not_Started_Session (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. session not started yet.",4227);
    }
    @Given("User Send KickedOut StudentId")
    public void Kicked_Out_Student_From_Session () throws SQLException, InterruptedException {
        student_KickedOut.student_join_session();
        student_KickedOut.Kick_Out();
        class_id = student_KickedOut.class_id;
        session_id = student_KickedOut.session_id;

        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id",session_id);
    }
    @Then("The Response Should Contains StatusCode 422 And Error Message Student Is KickedOut")
    public void Validate_Response_For_KickOut_Student (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. student kicked out from session.",4225);
    }
    @Given("User Send SessionId That Doesn't Related To Class Or Student")
    public void Session_Not_Related_To_Student () throws SQLException, InterruptedException {
        startSessionForJoin();
        sessionData.Create_Session_for_educator();
        sessionData.Start_Session();
        System.out.println("session_id "+ session_id);
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id",sessionData.session_id);
    }
    @Then("The Response Should Contains Status Code 422 And Error Message Session Isn't Related To Class Or Student")
    public void Validate_Response_Session_Not_Related_To_Student (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. session not related to this class or this student",4223);
    }
    public void Create_ClassFullPaid_With_Session() throws SQLException {
        classData.Create_Class_full_pay();
        educator_Id = classData.EducatorId;
        class_id = classData.Class_ID;

        String valid_body ="{\"session_title\":\"TestSession\",\"session_start_date\":\"2025-02-01T18:00:00Z\"," +
                "\"session_end_date\":\"2025-03-01T20:00:00Z\",\"session_duration_in_minutes\":120,\"educator_id\":"+ educator_Id +"," +
                "\"meta_session_id\":123456789012,\"session_order\":1,\"is_test_session\":true,\"classes_subjects\":[{\"class_id\":"+ class_id +"," +
                "\"subject_id\":364128042486,\"block_number\":null}]}";

        Response Create_Session = test.sendRequest("POST", "/admin/sessions", valid_body, adminData.Admin_Token);
        Create_Session.prettyPrint();
        session_id = Create_Session.then().extract().path("session_id");
        System.out.println("session_id "+session_id);

        Response Enroll_Student_Into_Class = test.sendRequest("POST", "/students/"+ student_Id +"/classes/"+ class_id +"/enroll", null,student_token);
        Enroll_Student_Into_Class.prettyPrint();

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

        Response Start_Session = test.sendRequest("POST", "/educators/"+ educator_Id +"/sessions/"+ session_id +"/start", null,EducatorRefreshToken);
        Start_Session.prettyPrint();

    }
    @Given("User Send ClassId That Doesn't Allow PayPerSession And SessionId That Doesn't Have AccessRight On")
    public void Pay_Per_Session_Not_Allowed () throws SQLException {
        Create_ClassFullPaid_With_Session();
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @Then("The Response Should Contains Status Code 422 And Error Message The Class Doesn't Allow PayPerSession")
    public void Validate_Response_For_Pay_Per_Session_Not_Allowed (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. pay per session not allowed for this class",4222);
    }
    @Given("User Send StudentId With InSufficient Balance")
    public void Insufficient_Student_Wallet () throws SQLException {
        studentData.Create_Student();
        student_Id = studentData.studentId;
        student_token = studentData.student_refreshToken;
        System.out.println("student_Id "+student_Id+" student_token "+student_token);
        startSessionForJoin();

        pathParams.put("student_id",student_Id);
        pathParams.put("class_id",class_id);
        pathParams.put("session_id",session_id);
    }
    @Then("The Response Should Contains Status Code 422 And Error Message Student Wallet Is Insufficient")
    public void Validate_Response_For_Insufficient_Balance (){
        Response join_SessionResponse = joinSession;
        test.Validate_Error_Messages(join_SessionResponse,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot join the session. insufficient student wallet balance.",4226);
    }
}
