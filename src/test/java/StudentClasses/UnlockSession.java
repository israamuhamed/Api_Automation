package StudentClasses;

import AdminArea.CreateSession;
import StudentHomeScreen.EnrollStudentIntoClass;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class UnlockSession {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    EnrollStudentIntoClass studentEnrolled = new EnrollStudentIntoClass();
    JoinSession joinedSession = new JoinSession();
    String student_token = data.Student_refresh_Token;
    CreateSession classData = new CreateSession();
    Long student_Id = data.student_Id;
    Long class_id;
    Long session_id;
    Map<String, Object> pathParams = test.pathParams;
    public Response Unlock_Session;

    @Given("User Send Session Id to unlock session for user")
    public void unlock_session_for_user() throws SQLException {
        classData.Create_Session();
        class_id = classData.Class_ID;
        session_id = classData.sessionId;
        test.sendRequest("POST", "/students/"+ student_Id +"/classes/"+ class_id +"/enroll", null,student_token);

        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @When("Performing the Api of Unlock Session")
    public void Unlock_Session() {
        Unlock_Session = test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/unlock", null, student_token);
    }
    @Then("I verify the appearance of status code 201 and Session successfully unlocked")
    public void Validate_Response_of_unlocked_session() {
        Unlock_Session.prettyPrint();
        Unlock_Session.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/unlockSession.json")))
                .body("session_id", hasToString(session_id.toString()), "message", containsString("Session successfully unlocked."),
                        "message_id", equalTo(201), "student_id", hasToString(student_Id.toString()), "class_id", hasToString(class_id.toString()));
    }
    @Then("I verify the appearance of status code 200 and Session already unlocked")
    public void Validate_Response_of_Session_already_unlocked() {
        Unlock_Session.prettyPrint();
        Unlock_Session.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/unlockSession.json")))
                .body("session_id", hasToString(session_id.toString()), "message", containsString("Session already unlocked."),
                        "message_id", equalTo(200), "student_id", hasToString(student_Id.toString()), "class_id", hasToString(class_id.toString()));
    }
    @Given("User Send unauthorized student id")
    public void unauthorized_user() throws SQLException {
        classData.Create_Session();
        class_id = classData.Class_ID;
        session_id = classData.sessionId;
        pathParams.put("student_id", "123456789987");
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @Then("The Response of unlockSession Should Contain Status Code 403 And Error Message Unauthorized")
    public void Validate_Response_unlockSession_unauthorized_student() {
        Response unlockSession = Unlock_Session;
        test.Validate_Error_Messages(unlockSession, HttpStatus.SC_FORBIDDEN, "Unauthorized", 4031);
    }
    @Given("student's wallet does not have sufficient wallet for unlock session")
    public void insufficient_student_wallet_unlockSession() throws SQLException {
        studentEnrolled.create_student_and_class();
        studentEnrolled.Enroll_Student_Into_Class();
        student_Id = studentEnrolled.student_Id;
        class_id = studentEnrolled.Class_ID;
        session_id = studentEnrolled.Session_Id;
        student_token = studentEnrolled.student_refreshToken;

        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @Then("The Response of unlockSession Should Contain Status Code 422 And Error Message insufficient student wallet balance")
    public void Validate_Response_unlockSession_insufficient_student_wallet() {
        Response unlockSession = Unlock_Session;
        test.Validate_Error_Messages(unlockSession, HttpStatus.SC_UNPROCESSABLE_ENTITY, "Cannot unlock the session. insufficient student wallet balance.", 4228);
    }
    @Given("class does not allow pay per session")
    public void class_not_allow_pay_per_session() throws SQLException {
        joinedSession.Create_ClassFullPaid_With_Session();
        class_id = joinedSession.class_id;
        session_id = joinedSession.session_id;

        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
        pathParams.put("session_id", session_id);
    }
    @Then("The Response Should Contain Status Code 422 And Error Message pay per session not allowed")
    public void Validate_Response_class_not_allow_pay_per_session() {
        Response unlockSession = Unlock_Session;
        test.Validate_Error_Messages(unlockSession, HttpStatus.SC_UNPROCESSABLE_ENTITY, "Cannot unlock the session. pay per session not allowed for this class.", 4227);
    }

}
