package AdminArea;

import EducatorProfile.Educator_TestData;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import TestConfig.TestBase;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class CreateClass {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateEducator educator = new CreateEducator();
    Faker fakeDate =new Faker();
    String classTitle = fakeDate.name().title();
    public Long EducatorId;
    public Long Class_ID;
    Response unauthorized_user;
    Response Invalid_body_data;
    Response Create_class;
    String body_for_pay_full_class ;
    String invalid_body = "{\"class_title\":\""+ classTitle +"\"," +
            "\"meta_class_id\":785157898183," +
            "\"class_order\":1," +
            "\"class_description\":\"This class provides an introduction to programming concepts.\"," +
            "\"class_public_listing_date\":\"\"," +
            "\"class_public_delist_date\":\"\"," +
            "\"class_enrollment_end_date\":\"\"," +
            "\"class_archive_date\":\"\",\"" +
            "class_payment_option_id\":1," +
            "\"class_block_count\":null," +
            "\"class_seats_limit\":10," +
            "\"is_test_class\":true," +
            "\"class_start_date\":\"\"," +
            "\"class_end_date\":\"2025-01-28T00:00:00Z\"," +
            "\"class_semester_localization_key\":\"First_Term\"," +
            "\"class_type\":\"Exam Prep\"," +
            "\"subjects\":[{\"subject_id\":793174170262," +
            "\"class_subject_retail_price\":50," +
            "\"class_subject_discounted_price\":null," +
            "\"class_subject_session_price\":10," +
            "\"blocks\":[]}," +
            "{\"subject_id\":787192832597," +
            "\"class_subject_retail_price\":30," +
            "\"class_subject_discounted_price\":null," +
            "\"class_subject_session_price\":5," +
            "\"blocks\":[]}]," +
            "\"educators\":" +
            "[{\"educator_order\":1," +
            "\"educator_id\":"+ EducatorId +"}]}";


    @Given("Performing the Api of Create class full pay With valid data")
    public Long Create_Class_full_pay() {
        educator.Create_Educator();
        EducatorId = educator.Educator_ID;
        System.out.println("educator " +EducatorId);
        System.out.println("class" +classTitle);
        body_for_pay_full_class = "{\"class_title\":\""+ classTitle +"\"," +
                "\"meta_class_id\":785157898183," +
                "\"class_order\":1," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\"," +
                "\"class_public_listing_date\":\"2024-01-28T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2025-01-23T00:00:00Z\"," +
                "\"class_enrollment_end_date\":\"2025-01-30T00:00:00Z\"," +
                "\"class_archive_date\":\"2025-01-22T00:00:00Z\",\"" +
                "class_payment_option_id\":1," +
                "\"class_block_count\":null," +
                "\"class_seats_limit\":10," +
                "\"is_test_class\":true," +
                "\"class_start_date\":\"2025-01-28T00:00:00Z\"," +
                "\"class_end_date\":\"2025-12-28T00:00:00Z\"," +
                "\"class_semester_localization_key\":\"First_Term\"," +
                "\"class_type\":\"Exam Prep\"," +
                "\"subjects\":[{\"subject_id\":364128042486," +
                "\"class_subject_retail_price\":50," +
                "\"class_subject_discounted_price\":null," +
                "\"class_subject_session_price\":10," +
                "\"blocks\":[]}]," +
                "\"educators\":[{\"educator_order\":1," +
                "\"educator_id\":"+ EducatorId +"}]}";


        Create_class = test.sendRequest("POST", "/admin/classes", body_for_pay_full_class, data.Admin_Token);
        return Class_ID = Create_class.then().extract().path("class_id");
    }

    @Given("Performing the Api of Create class per session With valid data")
    public Long Create_Class_per_session() {
        educator.Create_Educator();
        EducatorId = educator.Educator_ID;
        String body_for_per_session_class = "{\"class_title\":\""+ classTitle +"\"," +
                "\"meta_class_id\":785157898183," +
                "\"class_order\":1," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\"," +
                "\"class_public_listing_date\":\"2024-01-28T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2025-01-23T00:00:00Z\"," +
                "\"class_enrollment_end_date\":\"2025-01-30T00:00:00Z\"," +
                "\"class_archive_date\":\"2025-01-22T00:00:00Z\"," +
                "\"class_payment_option_id\":3," +
                "\"class_block_count\":null," +
                "\"class_seats_limit\":10," +
                "\"is_test_class\":true," +
                "\"class_start_date\":\"2025-01-28T00:00:00Z\"," +
                "\"class_end_date\":\"2025-12-28T00:00:00Z\",\"" +
                "class_semester_localization_key\":\"First_Term\"," +
                "\"class_type\":\"Exam Prep\"," +
                "\"subjects\":[{\"subject_id\":364128042486," +
                "\"class_subject_retail_price\":50," +
                "\"class_subject_discounted_price\":null," +
                "\"class_subject_session_price\":10," +
                "\"blocks\":[]}]," +
                "\"educators\":[{\"educator_order\":1," +
                "\"educator_id\":"+ EducatorId +"}]}";
            System.out.println("Create Class Per Session "+body_for_per_session_class);
        Create_class = test.sendRequest("POST", "/admin/classes", body_for_per_session_class, data.Admin_Token);
        return Class_ID = Create_class.then().extract().path("class_id");
    }

    @Given("Performing the Api of Create class block payment With valid data")
    public Long Create_Class_Block_Payment() {
        educator.Create_Educator();
        EducatorId = educator.Educator_ID;
        System.out.println("educator " +EducatorId);
        System.out.println("class" + classTitle);
        String body_for_block_payment_class = "{\"class_title\":\""+ classTitle +"\"," +
                "\"meta_class_id\":785157898183," +
                "\"class_order\":1," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\"," +
                "\"class_public_listing_date\":\"2024-01-28T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2025-01-23T00:00:00Z\"," +
                "\"class_enrollment_end_date\":\"2025-01-30T00:00:00Z\"," +
                "\"class_archive_date\":\"2025-01-22T00:00:00Z\"," +
                "\"class_payment_option_id\":2," +
                "\"class_block_count\":2," +
                "\"class_seats_limit\":10," +
                "\"is_test_class\":true," +
                "\"class_start_date\":\"2025-01-28T00:00:00Z\"," +
                "\"class_end_date\":\"2025-12-28T00:00:00Z\"," +
                "\"class_semester_localization_key\":\"First_Term\"," +
                "\"class_type\":\"Exam Prep\"," +
                "\"subjects\":[{\"subject_id\":364128042486," +
                "\"class_subject_retail_price\":null," +
                "\"class_subject_discounted_price\":5," +
                "\"class_subject_session_price\":10," +
                "\"blocks\":[{\"class_block_number\":1," +
                "\"class_block_retail_price\":20}," +
                "{\"class_block_number\":2," +
                "\"class_block_retail_price\":30}]}]," +
                "\"educators\":[{\"educator_order\":1," +
                "\"educator_id\":"+ EducatorId +"}]}";


        System.out.println(body_for_block_payment_class);
        Create_class = test.sendRequest("POST", "/admin/classes", body_for_block_payment_class, data.Admin_Token);
        return Class_ID = Create_class.then().extract().path("class_id");
    }

    @Then("I verify the appearance of status code 201 and class created successfully")
    public void Validate_Response_of_create_class_successfully() {
        System.out.println(Class_ID);
        Create_class.prettyPrint();
        Create_class.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/CreateClass.json")))
                .body("message", hasToString("Class created successfully."),"class_id",equalTo(Class_ID));
    }

    @Given("Performing the Api of Create class With invalid token")
    public void Create_class_with_invalid_token() {
        body_for_pay_full_class = "{\"class_title\":\"Introduction to Programming\"," +
                "\"meta_class_id\":123123123123," +
                "\"class_order\":1," +
                "\"class_description\":\"This class provides an introduction to programming concepts.\"," +
                "\"class_public_listing_date\":\"2024-01-10T00:00:00Z\"," +
                "\"class_public_delist_date\":\"2025-02-10T00:00:00Z\"," +
                "\"class_enrollment_end_date\":\"2025-02-05T23:59:59Z\"," +
                "\"class_archive_date\":\"2025-03-01T00:00:00Z\"," +
                "\"class_payment_option_id\":1,\"class_block_count\":0," +
                "\"class_seats_limit\":50," +
                "\"is_test_class\":true," +
                "\"subjects\":[{\"subject_id\":123456789012," +
                "\"class_subject_retail_price\":30," +
                "\"class_subject_discounted_price\":30," +
                "\"class_subject_session_price\":10," +
                "\"blocks\":[]}," +
                "{\"subject_id\":787192832597," +
                "\"class_subject_retail_price\":30," +
                "\"class_subject_discounted_price\":20," +
                "\"class_subject_session_price\":10," +
                "\"blocks\":[]}]," +
                "\"educators\":[{\"educator_id\":328148357020," +
                "\"educator_order\":20}]}";
        unauthorized_user = test.sendRequest("POST", "/admin/classes", body_for_pay_full_class,data.refresh_token_for_notActiveEducator);
    }

    @Then("I verify the appearance of status code 403 and user is not authorized")
    public void Validate_Response_of_unauthorized_userId(){
        Response unauthorizedUser = unauthorized_user;
        test.Validate_Error_Messages(unauthorizedUser,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

    @Given("Performing the Api of Create class With invalid body")
    public void Create_class_with_invalid_body() {
        Invalid_body_data = test.sendRequest("POST", "/admin/classes", invalid_body,data.refresh_token_for_notActiveEducator);
    }

    @Then("I verify the appearance of status code 400 and body incorrect")
    public void Validate_Response_of_unauthorized_EducatorId(){
        Response invalidBodyData = Invalid_body_data;
        test.Validate_Error_Messages(invalidBodyData,HttpStatus.SC_BAD_REQUEST,"Class Creation failed, invalid request body.",40016);
    }



}