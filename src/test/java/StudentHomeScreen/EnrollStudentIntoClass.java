package StudentHomeScreen;

import AdminArea.CreateClass;
import AdminArea.CreateSession;
import EducatorProfile.Educator_TestData;
import StudentProfile.CreateStudent;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class EnrollStudentIntoClass {
    TestBase test = new TestBase();
    CreateSession classData = new CreateSession();
    CreateStudent student = new CreateStudent();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Response Enroll_Student_Into_Class;
    public Long Class_ID;
    Long class_id;
    Long student_id;
    public Long student_Id;
    public Long Educator_Id;
    public Long Session_Id;
    public String student_refreshToken;

    @Given("User Send Valid student Id and class Id to enroll student into class")
    public void create_student_and_class ()throws SQLException {
       classData.Create_Session();
       Class_ID = classData.Class_ID;
       Educator_Id = classData.EducatorId;
       Session_Id = classData.sessionId;
       student.Create_Student();
       student_Id = student.studentId;
       pathParams.put("student_id",student_Id);
       pathParams.put("class_id",Class_ID);

    }
    @When("Performing the Api Enroll Student Into Class")
    public void Enroll_Student_Into_Class(){
        student_refreshToken = student.student_refreshToken;
        Enroll_Student_Into_Class = test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/enroll", null,student_refreshToken);
    }

    @Then("I verify the appearance of status code 200 and student enrolled to class")
    public void Validate_Response_enroll_student_successfully(){
        Enroll_Student_Into_Class.prettyPrint();
        Enroll_Student_Into_Class.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentHomeScreen/EnrollStudentIntoClass.json")))
                .body("message", hasToString("Student successfully enrolled in the class."),"class_id", equalTo(Class_ID.toString())
                ,"student_id", equalTo(student_Id.toString()));
    }

    @And("Verify enrollment done successfully into database")
    public void Get_student_classes_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from classes_students cs where student_id = "+ student_Id +"");

        while (resultSet.next()) {
             class_id = resultSet.getLong("class_id");
             student_id = resultSet.getLong("student_id");
        }
        Assert.assertEquals(Class_ID,class_id);
        Assert.assertEquals(student_Id,student_id);
    }

    @Given("User Send Valid student Id and invalid class Id to enroll student into class")
    public void create_student_and_invalid_class ()throws SQLException {
        student.Create_Student();
        student_Id = student.studentId;
        pathParams.put("student_id",student_Id);
        pathParams.put("class_id","123456789012");

    }

    @Then("I verify the appearance of status code 400 and class not exist for enrollment")
    public void Validate_Response_enroll_student_into_invalid_class(){
        Response Invalid_class = Enroll_Student_Into_Class;
        test.Validate_Error_Messages(Invalid_class,HttpStatus.SC_BAD_REQUEST,"Class not eligible for enrollment. Student is not in the same country as the class.",4004);
    }

    @When("Performing the Api Enroll Student Into Class with invalid token")
    public void Enroll_Student_Into_Class_invalid_token(){
        Enroll_Student_Into_Class = test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/enroll", null,data.refresh_token);
    }

    @Then("I verify the appearance of status code 403 and this student is not authorized")
    public void Validate_Response_enroll_student_invalid_token(){
        Response Invalid_token = Enroll_Student_Into_Class;
        test.Validate_Error_Messages(Invalid_token,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }


}
