package StudentClasses;

import AdminArea.CreateEducator;
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

public class PayForFullClass {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Educator_TestData AdminData = new Educator_TestData();
    CreateEducator educator = new CreateEducator();
    CreateStudent student = new CreateStudent();
    String user_token = data.Student_refresh_Token;
    Database_Connection Connect = new Database_Connection();
    JoinSession joinedSession = new JoinSession();
    Long student_Id = data.student_Id;
    Long class_id;
    String Full_Capacity_Class;
    Integer amount_paid_for_class;
    Map<String,Object> pathParams = test.pathParams;
    public Response pay_for_full_class ;

    @And("Get class data from database")
    public void get_classes_data_from_database()throws SQLException {

        ResultSet resultSet = Connect.connect_to_database("select * from classes_subjects cs where class_id = "+ class_id +"");
        while (resultSet.next()) {
            amount_paid_for_class = resultSet.getInt("class_subject_retail_price");
        }
    }
    @When("Performing the Api of pay_for_full_class")
    public void pay_for_full_class() {
        pay_for_full_class =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/pay-full",null,user_token);
    }
    @Given("User enrolled into fully paid class successfully")
    public void successful_payment_for_full_class() throws SQLException {
        joinedSession.Create_ClassFullPaid_With_Session();
        class_id= joinedSession.class_id;
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id",class_id);
    }
    @Then("I verify the appearance of status code 200 and Full class payment successful.")
    public void Validate_Response_of_success_payment_fullClass () {
        pay_for_full_class.prettyPrint();
        pay_for_full_class.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/payForFullClass.json")))
                .body("class_id" ,  hasToString(class_id.toString()) , "message" ,containsString("Full class payment successful."),
                        "amount_paid",equalTo(amount_paid_for_class),
                        "currency",hasToString("EGP"));
    }
    @Then("When it's test class it should return with error message and response code 400")
    public void test_class_case(){
        test.Validate_Error_Messages(pay_for_full_class, HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display",4046);
    }
    @Then("I verify the appearance of status code 400 and class already purchased")
    public void Validate_Response_already_purchased_Class () {
        Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_BAD_REQUEST,"Class already purchased.",4004);
    }
//    @Given("User enrolled into fully paid class that already enrolled in")
//    public void send_class_that_student_already_bought(){
//        pathParams.put("student_id", student_Id);
//        pathParams.put("class_id",purchased_class);
//    }
    @Given("User Send unauthorized user id")
    public void unauthorized_user() throws SQLException {
        joinedSession.Create_ClassFullPaid_With_Session();
        class_id= joinedSession.class_id;
        pathParams.put("student_id", "123456789012");
        pathParams.put("class_id", class_id);
    }
    @Then("The Response Should Contain Status Code 403 And Error Message Unauthorized")
    public void Validate_Response_unauthorized_student (){
        Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("student's wallet does not have sufficient funds for full class")
    public void Insufficient_wallet_for_full_class() throws SQLException {
        joinedSession.Create_ClassFullPaid_With_Session();
        class_id = joinedSession.class_id;
        student.Create_Student();
        student_Id = student.studentId;
        user_token = student.student_refreshToken;
        test.sendRequest("POST", "/students/"+ student_Id +"/classes/"+ class_id +"/enroll", null,user_token);
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_id);
    }
    @Then("The Response Should Contain Status Code 400 And Error Message Insufficient balance for full class")
    public void Validate_Response_Insufficient_wallet_for_full_class (){
        Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_BAD_REQUEST,"Insufficient wallet balance for full class payment.",4008);
    }
    @Given("user try to enroll in class that not available or Archived")
    public void class_is_archived() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", data.Archived_Class);
    }
    @Then("The Response Should Contain Status Code 404 And Error Message Class not available.")
    public void Validate_Response_class_not_available(){
        Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
    }
    @Given("user try to enroll in class have full capacity")
    public void full_capacity_class() {
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", data.Class_Has_No_Seats);
    }
    @Then("The Response Should Contain Status Code 400 And Error Message This class has reached full capacity.")
    public void Validate_Response_full_capacity_class(){
        Response PayForFullClass = pay_for_full_class;
        test.Validate_Error_Messages(PayForFullClass,HttpStatus.SC_BAD_REQUEST,"This class has reached full capacity, and no seats are currently open.",4006);
    }


}
