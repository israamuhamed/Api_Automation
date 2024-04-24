package StudentClasses;

import AdminArea.AssignEducationalResources;
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

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class GetDownloadURLsForEducationalResources {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Educator_TestData educatorData = new Educator_TestData();
    AssignEducationalResources sessionData = new AssignEducationalResources();
    Long student_id = data.student_Id;
    Database_Connection connect = new Database_Connection();
    public Long class_id;
    public Long session_id;
    public Long educator_Id;
    String educator_Email;
    public String EducatorRefreshToken;
    String OTP;
    public Long resource_id;
    String student_refresh_token = data.Student_refresh_Token;
    Map<String,Object> PathParams = test.pathParams;
    Response Get_download_URLs_Of_Educational_Resources;
    String valid_request_body;
    Long educational_resource_id;
    Integer session_educational_resource_id;
    String educational_resource_md5 ;
    String educational_resource_type;
    Integer educational_resource_type_id;

    public void joinSessionToDownloadResources () throws SQLException, InterruptedException {
        sessionData.Create_new_Assign_resources();
        class_id = sessionData.Class_Id;
        session_id = sessionData.SessionID;
        resource_id = sessionData.ResourceId;
        educator_Id = sessionData.EducatorId;
        student_id = data.student_Id;


        ResultSet GetEducatorEmail = connect.connect_to_database("select educator_email from public.educators e where educator_id ="+ educator_Id +"");
        while (GetEducatorEmail.next()) {
            educator_Email = GetEducatorEmail.getString("educator_email");};

        Response testOTP = test.sendRequest("POST", "/educators/auth/send-otp", "{\"email\":\""+ educator_Email +"\",\"language\":\"en\"}",educatorData.Admin_Token);
        testOTP.prettyPrint();

        ResultSet GetEducatorOTP = connect.Connect_to_OTP_Database("select \"Email\" ,\"Otp\"  from \"UserMailOtp\" umo where \"Email\" = '"+ educator_Email +"'");
        while (GetEducatorOTP.next()) {
            OTP = GetEducatorOTP.getString("Otp");};

        Response VerifyOTP = test.sendRequest("POST", "/educators/auth/verify-otp", "{\"email\":\""+ educator_Email +"\",\"otp\":\""+ OTP +"\"}",educatorData.Admin_Token);
        VerifyOTP.prettyPrint();
        EducatorRefreshToken = VerifyOTP.then().extract().path("tokens.refresh_token");
        Response Start_Session = test.sendRequest("POST", "/educators/"+ educator_Id +"/sessions/"+ session_id +"/start", null,EducatorRefreshToken);
        Start_Session.prettyPrint();
        Response Enroll_Student_Into_Class = test.sendRequest("POST", "/students/"+ student_id +"/classes/"+ class_id +"/enroll", null,student_refresh_token);
        Response joinSession =  test.sendRequest("POST", "/students/"+ student_id +"/classes/"+ class_id +"/sessions/"+ session_id +"/join",null,student_refresh_token);
        joinSession.prettyPrint();
        Response End_Session = test.sendRequest("POST", "/educators/"+ educator_Id +"/sessions/"+ session_id +"/end", null,EducatorRefreshToken);
        End_Session.prettyPrint();
    }

    @And("Get educational resources data from database")
    public void get_data_of_session_Educational_resource_from_database() throws SQLException {
        ResultSet resultSet = connect.connect_to_database("select * from sessions_educational_resources ser \n" +
                "join educational_resources er \n" +
                "on er.educational_resource_id = ser.educational_resource_id \n" +
                "join educational_resources_types ert \n" +
                "on er.educational_resource_type_id = ert.educational_resource_type_id \n" +
                "where ser.session_id = "+ session_id +"");

        while (resultSet.next()) {
            educational_resource_id = resultSet.getLong("educational_resource_id");
            session_educational_resource_id = resultSet.getInt("session_educational_resource_id");
            educational_resource_md5 = resultSet.getString("educational_resource_md5");
            educational_resource_type = resultSet.getString("educational_resource_type");
            educational_resource_type_id = resultSet.getInt("educational_resource_type_id");

        }
    }

    @When("Performing The APi Of GetDownload URLs Of Educational Resources")
    public void Get_Download_URLs(){
        valid_request_body =  "{\"educational_resources_ids\":["+ resource_id +"]}";
        Get_download_URLs_Of_Educational_Resources = test.sendRequest("POST","/students/{student_id}/classes/{class_id}/sessions/{session_id}/download-resources",valid_request_body,student_refresh_token);
    }

    @Given("User Send Valid Parameters To GetDownloadURLs Request")
    public void Get_Download_URLs_Of_Educational_Resources() throws SQLException, InterruptedException {
        joinSessionToDownloadResources ();
        PathParams.put("student_id", student_id);
        PathParams.put("class_id", class_id);
        PathParams.put("session_id", session_id);
    }
    @Then("Response Status Code Is 200 And Response Body Contains EducationalResourceId And DownloadLink")
    public void Validate_GetDownloadURLs_Response(){
        Get_download_URLs_Of_Educational_Resources.prettyPrint();
        Get_download_URLs_Of_Educational_Resources.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetDownloadURLsForEducationalResources.json")))
                .body("resources.educational_resource_id",hasItem(hasToString(educational_resource_id.toString())),"resources.educational_resource_type",hasItem(hasToString(educational_resource_type)),
                "resources.educational_resource_md5",hasItem(hasToString(educational_resource_md5)),"resources.educational_resource_type_id",hasItem(equalTo(educational_resource_type_id)),
                        "resources.session_educational_resource_id",hasItem(equalTo(session_educational_resource_id )));
    }

    @Given("User Send Invalid UserId To GetDownloadEducationalResources Request")
    public void Unauthorized_Student_for_download_EducationalResources() throws SQLException, InterruptedException {
        sessionData.Create_new_Assign_resources();
        class_id = sessionData.Class_Id;
        session_id = sessionData.SessionID;
        resource_id = sessionData.ResourceId;
        PathParams.put("student_id", "123456786433");
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
    }
    @Then("Response Status Code Is 403 And Response Body Contains Error Message And No Links Returns")
    public void Validate_Unauthorized_Response(){
        test.Validate_Error_Messages(Get_download_URLs_Of_Educational_Resources , HttpStatus.SC_FORBIDDEN , "Unauthorized", 4031);
    }
    @Given("User Send SessionId That Student Doesn't Hae Access To")
    public void have_no_access_to_session() throws SQLException, InterruptedException {
        sessionData.Create_new_Assign_resources();
        class_id = sessionData.Class_Id;
        session_id = sessionData.SessionID;
        resource_id = sessionData.ResourceId;
        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id","123456789811");
    }

    @Then("Response Status Code Is 403 And Body Returns With Error Message That Student Have No Access")
    public void validate_have_no_access_response(){
        test.Validate_Error_Messages(Get_download_URLs_Of_Educational_Resources , HttpStatus.SC_FORBIDDEN , "Student does not have access to the resources of the requested session or class", 4036);
    }
}

