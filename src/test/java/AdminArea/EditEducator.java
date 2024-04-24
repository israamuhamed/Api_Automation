package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class EditEducator {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();

    CreateEducator educator = new CreateEducator();
    Faker fakeDate =new Faker();
    String Admin_token = data.Admin_Token;
    Long educator_Id;
    Response Edit_Educator;
    Response Unauth_Educator;
    public Long Educator_ID;
    String firstName = fakeDate.name().firstName();
    String lastName = fakeDate.name().lastName();
    public Map<String, Object> pathParams = test.pathParams;
    String Educator_userName = fakeDate.name().username();
    public String Email =Educator_userName + "@nagwa.com";

    String valid_body_request =
                    "{\"educator_first_name\":\"test_educator_first_name\"," +
                    "\"educator_last_name\":\"test_educator_last_name\"," +
                    "\"educator_email\":\""+ Email +"\"," +
                    "\"educator_bio\":\"Experienced educator passionate about technology and programming.\"," +
                    "\"educator_image_bucket\":\"educators-images\"," +
                    "\"educator_image_key\":\"123123123123/profile.jpg\"," +
                    "\"educator_image_cdn\":\"https://educators.images.com\"," +
                    "\"educator_is_active\":true," +
                    "\"educator_is_deleted\":false," +
                    "\"is_test_educator\":true}";

    @Given("User Create new Educator For Update")
    public void user_send_valid_educatorId() {
        educator.Create_Educator();
        educator_Id = educator.Educator_ID;
        pathParams.put("educator_id", educator_Id);
    }
    @When("Performing the Api of Edit Educator With valid data")
    public void Edit_Educator() {
        Edit_Educator = test.sendRequest("PATCH", "/admin/educators/{educator_id}", valid_body_request,Admin_token);
    }
    @Then("I verify the appearance of status code 200 and Educator updated successfully")
    public void Validate_Response_of_edit_Educator_successfully() {
        Edit_Educator.prettyPrint();
        Edit_Educator.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("message", hasToString("Profile updated successfully."))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/EditEducator.json")));
    }
    @When("Performing the Api of Edit Educator With Invalid email format")
    public void Edit_Educator_Invalid_email() {
        String Invalid_email_format =
                "{\"educator_first_name\":\"test_educator_first_name\"," +
                        "\"educator_last_name\":\"test_educator_last_name\"," +
                        "\"educator_email\":\"educator@nagwa\"," +
                        "\"educator_bio\":\"Experienced educator passionate about technology and programming.\"," +
                        "\"educator_image_bucket\":\"educators-images\"," +
                        "\"educator_image_key\":\"123123123123/profile.jpg\"," +
                        "\"educator_image_cdn\":\"https://educators.images.com\"," +
                        "\"educator_is_active\":true," +
                        "\"educator_is_deleted\":false," +
                        "\"is_test_educator\":true}";

        Edit_Educator = test.sendRequest("PATCH", "/admin/educators/{educator_id}", Invalid_email_format,Admin_token);
    }
    @Then("I verify the appearance of status code 400 and email is invalid")
    public void Validate_Response_of_edit_Educator_with_invalid_email() {
        test.Validate_Error_Messages(Edit_Educator,HttpStatus.SC_BAD_REQUEST,"Invalid email format.",4003);

    }
    @When("Performing the Api of Edit Educator With Invalid body")
    public void Edit_Educator_Invalid_body() {
        String Invalid_body =
                        "{\"educator_first_name\":," +
                        "\"educator_last_name\":," +
                        "\"educator_email\":," +
                        "\"educator_bio\":," +
                        "\"educator_image_bucket\":\"educators-images\"," +
                        "\"educator_image_key\":\"123123123123/profile.jpg\"," +
                        "\"educator_image_cdn\":\"https://educators.images.com\"," +
                        "\"educator_is_active\":true," +
                        "\"educator_is_deleted\":false," +
                        "\"is_test_educator\":true}";

        Edit_Educator = test.sendRequest("PATCH", "/admin/educators/{educator_id}", Invalid_body,Admin_token);
    }
    @Then("I verify the appearance of status code 400 and parameter is invalid")
    public void Validate_Response_of_edit_Educator_with_invalid_body() {
        test.Validate_Error_Messages(Edit_Educator,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }
    @When("Performing the Api of Edit Educator With invalid token")
    public void Edit_Educator_invalid_token() {
        Edit_Educator = test.sendRequest("PATCH", "/admin/educators/{educator_id}", valid_body_request,data.refresh_token);
    }
    @Then("I verify the appearance of status code 403 and unauthorized user")
    public void Validate_Response_of_edit_Educator_with_invalid_token() {
        test.Validate_Error_Messages(Edit_Educator,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

}
