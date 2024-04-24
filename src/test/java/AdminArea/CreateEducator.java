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

public class CreateEducator {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    Faker fakeDate =new Faker();
    String Admin_token = data.Admin_Token;
    Response Create_Educator;
    Response Unauth_Educator;
    public Long Educator_ID;
    String firstName = fakeDate.name().firstName();
    String lastName = fakeDate.name().lastName();
    String Educator_userName = fakeDate.name().username();
    public String Email =Educator_userName + "@nagwa.com";
    Long Id = Long.valueOf(String.format("%012d", fakeDate.number().randomNumber(12, true)));

    String valid_body_request = "{\"educator_id\":"+ Id +"," +
            "\"educator_first_name\":\""+ firstName +"\"," +
            "\"educator_last_name\":\""+ lastName +"\"," +
            "\"educator_email\":\""+ Email +"\"," +
            "\"educator_bio\":\"Experienced educator passionate about technology and programming.\"," +
            "\"educator_is_active\":true," +
            "\"educator_image_bucket\":\"educators-images\"," +
            "\"educator_image_key\":\"123123123123/profile.jpg\"," +
            "\"educator_image_cdn\":\"https://educators.images.com\"," +
            "\"is_test_educator\":true}";



    @Given("Performing the Api of Create Educator With valid data")
    public Long Create_Educator() {
        Create_Educator = test.sendRequest("POST", "/admin/educators", valid_body_request,Admin_token);
        Educator_ID = Create_Educator.then().extract().path("educator_id");
        System.out.println("Request_body_Educator: "+ valid_body_request);
        System.out.println("Educator_ID "+ Educator_ID);
        return Educator_ID = Create_Educator.then().extract().path("educator_id");
    }

    @Then("I verify the appearance of status code 200 and Educator created successfully")
    public void Validate_Response_of_create_Educator_successfully() {
        Create_Educator.prettyPrint();
        Create_Educator.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/CreateEducator.json")))
                .body("message", hasToString("Educator created successfully."),"educator_id",equalTo(Educator_ID));
    }
    @Given("Performing the Api of Create Educator With Invalid email")
    public void Create_Educator_with_Invalid_mail() {
        String Invalid_email_In_Body = "{\"educator_id\":"+ Id +"," +
                "\"educator_first_name\":\""+ firstName +"\"," +
                "\"educator_last_name\":\""+ lastName +"\"," +
                "\"educator_email\":\"email.com\"," +
                "\"educator_bio\":\"Experienced educator passionate about technology and programming.\"," +
                "\"educator_is_active\":true," +
                "\"educator_image_bucket\":\"educators-images\"," +
                "\"educator_image_key\":\"123123123123/profile.jpg\"," +
                "\"educator_image_cdn\":\"https://educators.images.com\"," +
                "\"is_test_educator\":true}";

        Create_Educator = test.sendRequest("POST", "/admin/educators", Invalid_email_In_Body,Admin_token);
    }
    @Then("I verify the appearance of status code 400 and Educator email is not correct")
    public void Validate_Response_of_Invalid_Educator_mail() {
        Response Invalid_email = Create_Educator;
        test.Validate_Error_Messages(Invalid_email,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the email parameter and request context for accuracy",4002);
    }
    @Given("Performing the Api of Create Educator With Invalid body request")
    public void Create_Educator_with_Invalid_body() {
        String Invalid_body = "{\"educator_id\":"+ Id +"," +
                "\"educator_first_name\":\"\"," +
                "\"educator_last_name\":\"\"," +
                "\"educator_email\":\"\"," +
                "\"educator_bio\":\"Experienced educator passionate about technology and programming.\"," +
                "\"educator_is_active\":true," +
                "\"educator_image_bucket\":\"educators-images\"," +
                "\"educator_image_key\":\"123123123123/profile.jpg\"," +
                "\"educator_image_cdn\":\"https://educators.images.com\"}";

        Create_Educator = test.sendRequest("POST", "/admin/educators", Invalid_body,Admin_token);
    }
    @Then("I verify the appearance of status code 400 and Educator data is not correct")
    public void Validate_Response_of_Invalid_Educator_data() {
        Response Invalid_data = Create_Educator;
        test.Validate_Error_Messages(Invalid_data,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the email parameter and request context for accuracy",4002);
    }
    @Given("Performing the Api of Create Educator With email already exist")
    public void Create_Educator_with_email_exist() {
        String Email_already_exist = "{\"educator_id\":"+ Id +"," +
                "\"educator_first_name\":\""+ firstName +"\"," +
                "\"educator_last_name\":\""+ lastName +"\"," +
                "\"educator_email\":\"intutor1@nagwaclassesus.com\"," +
                "\"educator_bio\":\"Experienced educator passionate about technology and programming.\"," +
                "\"educator_is_active\":true," +
                "\"educator_image_bucket\":\"educators-images\"," +
                "\"educator_image_key\":\"123123123123/profile.jpg\"," +
                "\"educator_image_cdn\":\"https://educators.images.com\"," +
                "\"is_test_educator\":true}";

        Create_Educator = test.sendRequest("POST", "/admin/educators", Email_already_exist,Admin_token);
    }
    @Then("I verify the appearance of status code 409 and this email already exist")
    public void Validate_Response_of_email_exist() {
        Response exist_email = Create_Educator;
        test.Validate_Error_Messages(exist_email,409,"The email provided is already associated with another account.",4093);
    }
    @Given("Performing the Api of Create Educator With invalid token")
    public void Create_Educator_with_invalid_token() {
        Unauth_Educator = test.sendRequest("POST", "/admin/educators", valid_body_request,data.refresh_token_for_deletedEducator);
    }
    @Then("I verify the appearance of status code 403 and Educator is not authorized")
    public void Validate_Response_of_unauthorized_EducatorId() {
        Response unauthorizedEducator = Unauth_Educator;
        test.Validate_Error_Messages(unauthorizedEducator,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }



}
