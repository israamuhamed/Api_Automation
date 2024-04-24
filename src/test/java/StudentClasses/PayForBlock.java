package StudentClasses;

import AdminArea.CreateClass;
import AdminArea.CreateEducator;
import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
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

public class PayForBlock {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Faker fakeData = new Faker();
    Educator_TestData data_of_admin = new Educator_TestData();
    CreateClass classData = new CreateClass();
    Database_Connection connect = new Database_Connection();
    CreateEducator educator= new CreateEducator();
    String user_token = data.Student_refresh_Token;
    String sessionTitle = fakeData.name().title();
    String classTitle = fakeData.name().title();
    Map<String,Object> pathParams = test.pathParams;
    Long EducatorId;
    public Long student_id = data.student_Id;

    public Long Class_ID;
    String valid_body;
    String  body_for_block_payment_class;
    Response PayForBlock;
    Response EnrollStudent;
    Response CreateBlockPaymentClass;
    String block1_price= "5";
    String block2_price="10";

    public Long Create_Class_BlockPayment() {
        educator.Create_Educator();
        EducatorId = educator.Educator_ID;
        System.out.println("educator " +EducatorId);
        System.out.println("class" + classTitle);

        body_for_block_payment_class = "{\"class_title\":\""+ classTitle +"\",\"meta_class_id\":785157898183,\"class_order\":1," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\",\"class_public_listing_date\":\"2024-01-28T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2025-01-23T00:00:00Z\",\"class_enrollment_end_date\":\"2025-01-30T00:00:00Z\",\"class_archive_date\":\"2025-01-22T00:00:00Z\"," +
                "\"class_payment_option_id\":2,\"class_block_count\":2,\"class_seats_limit\":10,\"is_test_class\":true,\"class_start_date\":\"2024-01-28T00:00:00Z\",\"class_end_date\":\"2027-12-28T00:00:00Z\"," +
                "\"class_semester_localization_key\":\"First_Term\",\"class_type\":\"Exam Prep\",\"subjects\":[{\"subject_id\":364128042486,\"class_subject_retail_price\":null," +
                "\"class_subject_discounted_price\":5,\"class_subject_session_price\":10,\"blocks\":[{\"class_block_number\":1,\"class_block_retail_price\":"+ block1_price +"}," +
                "{\"class_block_number\":2,\"class_block_retail_price\":"+ block1_price +"}]}]," +
                "\"educators\":[{\"educator_order\":1,\"educator_id\":"+ EducatorId +"}]}";

        CreateBlockPaymentClass = test.sendRequest("POST", "/admin/classes", body_for_block_payment_class, data_of_admin.Admin_Token);
        Class_ID = CreateBlockPaymentClass.then().extract().path("class_id");

        String create_session_for_block_1 ="{\"session_title\":\""+ sessionTitle +"\",\"session_start_date\":\"2025-02-01T18:00:00Z\"," +
                "\"session_end_date\":\"2025-03-01T20:00:00Z\",\"session_duration_in_minutes\":120,\"educator_id\":"+ EducatorId +"," +
                "\"meta_session_id\":123456789012,\"session_order\":1,\"is_test_session\":true,\"classes_subjects\":[{\"class_id\":"+ Class_ID +"," +
                "\"subject_id\":364128042486,\"block_number\":1}]}";

        String create_session_for_block_2 ="{\"session_title\":\""+ sessionTitle +"\",\"session_start_date\":\"2025-02-01T18:00:00Z\"," +
                "\"session_end_date\":\"2025-03-01T20:00:00Z\",\"session_duration_in_minutes\":120,\"educator_id\":"+ EducatorId +"," +
                "\"meta_session_id\":123456789012,\"session_order\":1,\"is_test_session\":true,\"classes_subjects\":[{\"class_id\":"+ Class_ID +"," +
                "\"subject_id\":364128042486,\"block_number\":2}]}";

        Response Create_Session_for_block1 = test.sendRequest("POST", "/admin/sessions", create_session_for_block_1, data_of_admin.Admin_Token);
        Response Create_Session_for_block2 = test.sendRequest("POST", "/admin/sessions", create_session_for_block_2, data_of_admin.Admin_Token);

        return Class_ID ;
    }

    @Given("User Enter Valid ClassId And Valid Blocks Value In Right Sequential")
    public void send_valid_data_to_payForBlock(){
        Create_Class_BlockPayment();
        System.out.println("Class_ID "+ Class_ID);
        pathParams.put("class_id",Class_ID);
        pathParams.put("student_id",student_id);
        EnrollStudent =  test.sendRequest("POST","/students/{student_id}/classes/{class_id}/enroll",null,user_token);
        EnrollStudent.prettyPrint();

    }
    @When("Performing The API Of PayForBlock")
    public void payFor_block_successfully(){
        valid_body = "{\"blocks\":[1,2]}";
        PayForBlock = test.sendRequest("POST","/students/{student_id}/classes/{class_id}/pay-block",valid_body,user_token);
    }
    @Then("Response Code Of PayForBlock Returns With 200 And Success Message And The Blocks That Student Have Bought")
    public void validate_buy_blocks_successfully()throws SQLException {
    int price_of_blocks = 0;
    ResultSet resultSet =  connect.connect_to_database("select class_block_discounted_price, csb.class_block_retail_price   " +
                "from public.classes_subjects cs join classes_subjects_blocks csb on csb.class_subject_id  = cs.class_subject_id \n" +
                "where cs.class_id ="+Class_ID+"\n");

        while (resultSet.next()){
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                price_of_blocks += resultSet.getInt(i);
            }
        }

        PayForBlock.prettyPrint();
        PayForBlock.then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message",containsString("Block payment successful."))
                .body("class_id",equalTo(Class_ID))
                .body("blocks",hasItems(1,2))
                .body("amount_paid",equalTo(price_of_blocks))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/PayForBlock.json")));
    }

    @When("Performing The API Of PayForBlock API With Blocks That Already Bought")
    public void send_blocks_already_bought(){
        PayForBlock = test.sendRequest("POST","/students/{student_id}/classes/{class_id}/pay-block",valid_body,user_token);
    }
    @Then("Response code of PayForBlock is 400 and body returns with error message")
    public void validate_already_bought_blocks_response(){
        test.Validate_Error_Messages(PayForBlock,HttpStatus.SC_BAD_REQUEST,"Block already paid.",40014);
    }

    @Given("User Send ClassId That Doesn't Allow PayForBlock Option")
    public void send_class_not_allow_pay_for_block(){
        classData.Create_Class_full_pay();
        Class_ID = classData.Class_ID;
        pathParams.put("class_id",Class_ID);
        pathParams.put("student_id",student_id);
        EnrollStudent =  test.sendRequest("POST","/students/{student_id}/classes/{class_id}/enroll",null,user_token);

    }
    @Then("Response Code Of PayForBlock And Body Returns With Message That Class Doesn't Allow PayForBlock")
    public void validate_not_block_payment_class(){
        test.Validate_Error_Messages(PayForBlock,HttpStatus.SC_UNPROCESSABLE_ENTITY , "Cannot pay the block. pay-per-block is not allowed for this class",42217);
    }
    @When("Performing The API Of Pay For Block With Invalid Token")
    public void pay_forBlock_with_invalidToken(){
        valid_body = "{\"blocks\":[1,2]}";
        PayForBlock = test.sendRequest("POST","/students/{student_id}/classes/{class_id}/pay-block",valid_body,data_of_admin.refresh_token);
    }
    @Then("Response Code Of PayForBlock Is 403 And Body Returns With Error Message")
    public void validate_unauthorized_response(){
        test.Validate_Error_Messages(PayForBlock , HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("User Send Valid ClassId And Expensive Blocks To The Request")
    public void send_expensive_classId(){
        block1_price="10000000";
        block2_price="10000000";
        Create_Class_BlockPayment();
        pathParams.put("class_id",Class_ID);
        pathParams.put("student_id",student_id);
        EnrollStudent =  test.sendRequest("POST","/students/{student_id}/classes/{class_id}/enroll",null,user_token);
    }
    @Then("Response Code Of PayForBlock Is 422 And Body Returns With Error Message contains insufficient wallet balance")
    public void validate_expensive_class_errorMessage(){
        test.Validate_Error_Messages(PayForBlock , HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot pay the block. insufficient student wallet balance.",42216);
    }
}
