package StudentHomeScreen;

import AdminArea.GetSession;
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

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class ListClassesForStudent {
    TestBase test = new TestBase();
    CreateStudent student = new CreateStudent();
    GetSession session = new GetSession();
    GetClassDetailsForStudent StudentClasses = new GetClassDetailsForStudent();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Map<String, Object> pathParams_List_Classes = test.pathParams;
    Response Enroll_Student_Into_Class;
    Response List_Classes;
    Long Class_ID;
    Long class_id;
    Long student_Id;
    Long session_Id;
    Long educator_Id;
    String student_refreshToken;
    String class_title ;
    Integer class_payment_option_id;
    String class_payment_option_name ;
    Long educator_id;
    Long session_id;
    Long subject_id ;

    @And("Get Student Classes from database")
    public void get_Student_classes_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from classes_students cs \n" +
                "join classes c \n" +
                "on c.class_id = cs.class_id \n" +
                "join class_payment_options cpo \n" +
                "on c.class_payment_option_id = cpo.class_payment_option_id \n" +
                "join classes_subjects cs2 \n" +
                "on cs2.class_id = c.class_id \n" +
                "join classes_subjects_sessions css \n" +
                "on css.class_subject_id = cs2.class_subject_id \n" +
                "join classes_educators ce \n" +
                "on ce.class_id = c.class_id \n" +
                "where cs.student_id ="+ student_Id +"");

        while (resultSet.next()) {
            class_id = resultSet.getLong("class_id");
            class_title = resultSet.getString("class_title");
            class_payment_option_id = resultSet.getInt("class_payment_option_id");
            class_payment_option_name = resultSet.getString("class_payment_option_name");
            educator_id = resultSet.getLong("educator_id");
            session_id = resultSet.getLong("session_id");
            subject_id = resultSet.getLong("subject_id");

        }
    }

    @Given("enroll student into class")
    public void enroll_student_into_class_has_sessions() throws SQLException {
       StudentClasses.enroll_student_into_class_has_sessions();
    }

    @When("Performing the Api of List Classes")
    public void List_Class_Details(){
        student_refreshToken = StudentClasses.student_refreshToken;
        student_Id = StudentClasses.student_Id;
        pathParams.put("student_id",student_Id);
        List_Classes = test.sendRequest("GET", "/students/{student_id}/classes", null,student_refreshToken);
    }

    @Then("I verify the appearance of status code 200 and the classes list returned successfully")
    public void Validate_Response_list_classes_successfully () {
        List_Classes.prettyPrint();
        List_Classes.then()
                .statusCode(HttpStatus.SC_OK);
//                .assertThat()
//                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentHomeScreen/ListClassesForStudent.json")))
//                .body("[0].class_id", equalTo(class_id), "[0].class_title", hasToString(class_title), "[0].class_payment_option_id", equalTo(class_payment_option_id),
//                        "[0].class_educators.educator_id", hasItem(equalTo(educator_id)),"[0].class_payment_option_name", hasToString(class_payment_option_name), "[0].student_is_enrolled", equalTo(true),
//                      "[0].class_subjects.subject_id", hasItem(equalTo(subject_id)));
    }

    @When("Performing the Api of List Class with student not authorized")
    public void Get_Class_Details_with_student_not_exist() throws SQLException {
        pathParams.put("student_id","123456789012");
        List_Classes = test.sendRequest("GET", "/students/{student_id}/classes", null,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 403 and this student is un_authorized")
    public void Validate_Response_Get_details_with_notExist_student(){
        Response Invalid_student = List_Classes;
        test.Validate_Error_Messages(Invalid_student,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

    @When("Performing the Api of List Class with invalid student id")
    public void Get_Class_Details_with_student_not_valid() throws SQLException {
        pathParams.put("student_id","123456789");
        List_Classes = test.sendRequest("GET", "/students/{student_id}/classes", null,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 400 and the student id is not valid")
    public void Validate_Response_Get_details_with_notValid_student(){
        Response Invalid_student = List_Classes;
        test.Validate_Error_Messages(Invalid_student,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4001);
    }




}
