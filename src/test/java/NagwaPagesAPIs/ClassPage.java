package NagwaPagesAPIs;

import EducatorProfile.Educator_TestData;
import StudentClasses.Student_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import StudentClasses.Student_TestData;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassPage {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Educator_TestData data_admin = new Educator_TestData();
    Response ClassPage;
    Long student_id = data.student_Id;
    String user_token = data.Student_refresh_Token;
    Database_Connection connect = new Database_Connection();
    Long class_id;
    String class_title;
    Integer class_payment_option;
    String class_description;
    public ClassPage()throws SQLException{
        get_class_data();
    }

    public void get_class_data()throws SQLException {
        ResultSet resultSet = connect.connect_to_database("\n" +
                "select * from public.classes c where c.class_archive_date > now() and c.class_public_listing_date < now()\n");
        while (resultSet.next()){
            class_id = resultSet.getLong("class_id");
            class_title= resultSet.getString("class_title");
            class_payment_option= resultSet.getInt("class_payment_option_id");
            class_description =resultSet.getString("class_description");
        }
    }
    @Given("User Send Valid ClassId To The ClassPage API")
    public void send_class_id_to_classPage_API(){
        System.out.println(class_id);
        ClassPage = test.sendRequest("GET","/web/class-page?class-id="+class_id,null,user_token);
    }
    @Then("Class Of ClassPage API Return In Response Body")
    public void validate_class_page_response_with_no_student(){
        ClassPage.prettyPrint();
        ClassPage.then().assertThat().statusCode(HttpStatus.SC_OK)
                .body("class_id",equalTo(class_id))
                .body("class_title",containsString(class_title))
                .body("class_payment_option_id",equalTo(class_payment_option))
                .body("class_description",containsString(class_description))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/NagwaPagesAPIs/ClassPage.json")));
    }
    @Given("User send valid ClassId and StudentId in query params of the request")
    public void send_classId_and_studentId_to_request(){
        ClassPage = test.sendRequest("GET","/web/class-page?class-id="+class_id+"&student-id="+student_id,null,user_token);
    }
    @Given("User execute request Class Page with missing query params")
    public void send_request_with_missing_queryParam(){
        ClassPage = test.sendRequest("GET","/web/class-page?class-id=",null,user_token);
    }
    @Then("Response code of Class Page is 400 and body returns with error message")
    public void validate_missing_param_response(){
        test.Validate_Error_Messages(ClassPage , HttpStatus.SC_BAD_REQUEST , "Invalid request. Please check the path parameters and request context for accuracy",4002);
    }
    @Given("User enter invalid value in query params Of Class Page API")
    public void send_not_exist_classId(){
        ClassPage = test.sendRequest("GET","/web/class-page?class-id=123456789012",null,user_token);
    }
    @Then("Response code of Class Page is 404 and body returns with error message that class not found")
    public void validate_not_Exist_class_Response(){
        test.Validate_Error_Messages(ClassPage , HttpStatus.SC_NOT_FOUND , "Class not found or not eligible for display.",4046);
    }
}
