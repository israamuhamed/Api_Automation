package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class EditClass {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateEducator educator = new CreateEducator();
    CreateClass class_data = new CreateClass();
    Faker fakeDate =new Faker();
    String classTitle = fakeDate.name().title();
    String New_Educator_First_name;
    String  New_Educator_Last_name;
    String New_Educator_Email;
    Database_Connection Connect = new Database_Connection();
    Response Edit_class;

    public Map<String, Object> pathParams = test.pathParams;
    String body_for_pay_full_class ;
    String Body_of_not_Exist_educator;

    @Given("User Send Valid Data To Edit Class API")
    public void Edit_Class() {
        class_data.Create_Class_full_pay();
        educator.Create_Educator();
        pathParams.put("class_id",class_data.Class_ID);
        body_for_pay_full_class = "{\"class_title\":\""+classTitle+"\",\"class_order\":2," +
                "\"class_description\":\"learncondingbypython\"," +
                "\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2024-02-10T00:00:00Z\"," +
                "\"class_enrollment_end_date\":\"2024-02-05T23:59:59Z\"," +
                "\"class_archive_date\":\"2025-03-01T00:00:00Z\"," +
                "\"class_start_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_end_date\":\"2025-02-10T00:00:00Z\"," +
                "\"class_semester_localization_key\":\"semester1\"," +
                "\"class_type\":\"type1\",\"class_seats_limit\":20," +
                "\"is_test_class\":true,\"educators\":[{\"educator_id\":"+educator.Educator_ID+",\"educator_first_name\":\""+educator.firstName+"\"," +
                "\"educator_last_name\":\""+educator.lastName+"\",\"educator_email\":\""+educator.Email+"\",\"educator_order\":20}]}" ;
    }
    @When("Performing The API Of Edit Class")
    public void edit_class_successfully(){
        System.out.println("class_id: =" + " " +class_data.Class_ID);
        Edit_class = test.sendRequest("PATCH", "/admin/classes/{class_id}", body_for_pay_full_class, data.Admin_Token);
    }

    @Then("Response Code of Edit Class Is 200 And Class Is Updated Successfully")
    public void validate_update_class_successfully_response()throws SQLException {
        Edit_class.then().assertThat().
                statusCode(HttpStatus.SC_OK)
                .body("message",containsString("class updated successfully."))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/EditClass.json")));
    }
    @And("The Data Of Educator Of The Class Is Updated in DataBase")
    public void validate_that_data_updated_in_DB()throws SQLException{
        ResultSet resultSet = Connect.connect_to_database("select * from public.classes c join public.classes_educators ce on ce.class_id = c.class_id \n" +
                "join educators e on e.educator_id = ce.educator_id where c.class_id ="+class_data.Class_ID+"");
        while (resultSet.next()){
            New_Educator_First_name = resultSet.getString("educator_first_name");
            New_Educator_Last_name = resultSet.getString("educator_last_name");
            New_Educator_Email = resultSet.getString("educator_email");
        }
        assertEquals(educator.firstName,New_Educator_First_name);
        assertEquals(educator.lastName,New_Educator_Last_name);
        assertEquals(educator.Email,New_Educator_Email);

    }

    @When("Performing The API of EditClass With Invalid Token")
    public void Edit_class_with_invalid_token(){
        Edit_class = test.sendRequest("PATCH", "/admin/classes/{class_id}", body_for_pay_full_class, data.refresh_token);
    }
    @Then("Response code of EditClass is 403 and body returns with error message")
    public void validate_editClass_response_with_InvalidToken(){
        test.Validate_Error_Messages(Edit_class,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("User Send Valid Body And Empty ClassId In Path Parameters")
    public void leave_classId_empty(){
        class_data.Create_Class_full_pay();
        educator.Create_Educator();
        pathParams.put("class_id"," ");
        body_for_pay_full_class = "{\"class_title\":\""+classTitle+"\",\"class_order\":2," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\"," +
                "\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2024-02-10T00:00:00Z\"," +
                "\"class_enrollment_end_date\":\"2024-02-05T23:59:59Z\"," +
                "\"class_archive_date\":\"2025-03-01T00:00:00Z\"," +
                "\"class_start_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_end_date\":\"2025-02-10T00:00:00Z\"," +
                "\"class_semester_localization_key\":\"First_Term\"," +
                "\"class_type\":\"ClassType\",\"class_seats_limit\":20," +
                "\"is_test_class\":true,\"educators\":[{\"educator_id\":"+educator.Educator_ID+",\"educator_first_name\":\""+educator.firstName+"\"," +
                "\"educator_last_name\":\""+educator.lastName+"\",\"educator_email\":\""+educator.Email+"\",\"educator_order\":20}]}";
    }
    @Then("Response Code of Edit Class Is 400 And Body Returns With Error Message")
    public void validate_empty_classId_response(){
        test.Validate_Error_Messages(Edit_class,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy",4002);
    }
    @Given("User Send Educator That Doesn't Exist to Edit Class_API")
    public void send_EducatorId_not_exist(){
        class_data.Create_Class_full_pay();
        educator.Create_Educator();
        pathParams.put("class_id",class_data.Class_ID);
        Body_of_not_Exist_educator = "{\"class_title\":\"Coding\",\"class_order\":2,\"class_description\":\"This class provides an introduction to programming concepts.\",\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"" +
                ",\"class_public_delist_date\":\"2024-02-10T00:00:00Z\",\"class_enrollment_end_date\":\"2024-02-05T23:59:59Z\"" +
                ",\"class_archive_date\":\"2024-03-01T00:00:00Z\",\"class_start_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_end_date\":\"2025-02-10T00:00:00Z\",\"class_semester_localization_key\":\"First_Term\"" +
                ",\"class_type\":\"ClassType\",\"class_seats_limit\":20,\"is_test_class\":true" +
                ",\"educators\":[{\"educator_id\":123456789012,\"educator_first_name\":\"fName\"," +
                "\"educator_last_name\":\"lName\",\"educator_email\":\"test@example.com\",\"educator_order\":20}]}";
    }
    @When("Performing The API Of Edit Class With EducatorId That Doesn't Exist")
    public void execute_request_not_exist_educator(){
        Edit_class = test.sendRequest("PATCH", "/admin/classes/{class_id}", Body_of_not_Exist_educator, data.Admin_Token);
    }
    @Then("Response Code of Edit Class Is 404 And Body Returns With Error Message That Educator Doesn't Exist")
    public void validate_not_Exist_Educator_response(){
        test.Validate_Error_Messages(Edit_class,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active",40413);
    }
    @Given("User Send Valid Body And Class Id That Doesn't Exist In Path Parameters")
    public void send_not_exist_classId(){
        class_data.Create_Class_full_pay();
        educator.Create_Educator();
        pathParams.put("class_id","123456789012");
        body_for_pay_full_class = "{\"class_title\":\""+classTitle+"\",\"class_order\":2," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\"," +
                "\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2024-02-10T00:00:00Z\"," +
                "\"class_enrollment_end_date\":\"2024-02-05T23:59:59Z\"," +
                "\"class_archive_date\":\"2024-03-01T00:00:00Z\"," +
                "\"class_start_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_end_date\":\"2024-02-10T00:00:00Z\"," +
                "\"class_semester_localization_key\":\"First_Term\"," +
                "\"class_type\":\"ClassType\",\"class_seats_limit\":20," +
                "\"is_test_class\":true,\"educators\":[{\"educator_id\":"+educator.Educator_ID+",\"educator_first_name\":\""+educator.firstName+"\"," +
                "\"educator_last_name\":\""+educator.lastName+"\",\"educator_email\":\""+educator.Email+"\",\"educator_order\":20}]}" ;
    }
    @Then("Response Code of Edit Class Is 404 And Body Returns With Error Message That Class Doesn't Exist")
    public void validate_not_Exist_Class_Response(){
        test.Validate_Error_Messages(Edit_class,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
    }
}
