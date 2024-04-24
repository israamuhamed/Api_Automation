package ParentAccount;

import StudentClasses.Student_TestData;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import StudentClasses.Student_TestData;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class CreateParentAccount {
    TestBase test = new TestBase();
    Faker fakeDate =new Faker();
    Student_TestData parentData = new Student_TestData();
    Response Create_Educator;
    public Long parent_id;
    String Parent_Access_Token;
    String firstName = fakeDate.name().firstName();
    String lastName = fakeDate.name().lastName();
    public String Email ="Parent_userName@nagwa.com";
    String valid_body_request ="{\"parent_first_name\":\""+ firstName +"\",\"parent_last_name\":\""+ lastName +"\",\"parent_email\":\""+ Email +"\",\"parent_country_id\":102123867837}";

    @Given("Performing the Api of Create parent With valid data")
    public Long Create_Educator() {
        System.out.println(valid_body_request);
        Create_Educator = test.sendRequest("POST", "/parents/create", valid_body_request, parentData.Parent_refreshToken);
        Parent_Access_Token = Create_Educator.then().extract().path("tokens.access_token");
        return parent_id = Create_Educator.then().extract().path("data.parent_id");
    }

    @Then("I verify the appearance of status code 200 and parent created successfully")
    public void Validate_Response_of_create_Parent_successfully() {
        Create_Educator.prettyPrint();
        Create_Educator.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/CreateEducator.json")))
                .body("message", hasToString("Parent account created successfully."),"parent_id",equalTo(parent_id));
    }
}
