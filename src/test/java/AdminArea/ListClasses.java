package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import static org.hamcrest.Matchers.*;

public class ListClasses {
     TestBase test = new TestBase();
     Educator_TestData data = new Educator_TestData();
     CreateClass class_data = new CreateClass();
     Response List_Classes;
     public Long class_id;

    @Given("User Create New Class")
    public void create_new_class(){
        class_data.Create_Class_per_session();
    }
    @When("Performing The API of ListClasses")
    public void List_classes(){
        List_Classes = test.sendRequest("GET", "/admin/classes?class-id=" + class_data.Class_ID,null , data.Admin_Token);
    }
    @Then("The Class should return in response body")
    public void validate_response_body(){
        List_Classes.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.class_id",hasItem(class_data.Class_ID))
                .body("data.class_title",hasItem(hasToString(class_data.classTitle)))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListClasses.json")));
    }
    @When("Performing the API of List Classes With Invalid Token")
    public void List_classes_with_invalid_token(){
        List_Classes = test.sendRequest("GET", "/admin/classes?class-id=" + class_data.Class_ID,null , data.refresh_token);
    }
    @Then("Response Code of ListClasses Is 403 And Body Returns With Error Message")
    public void ListClasses_validate_unAuthorizedResponse(){
        test.Validate_Error_Messages(List_Classes,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("User Send Invalid Class_Id In Request Params")
    public void define_invalid_class_id(){
        class_id = 12345632111L;
    }
    @When("Performing The API of ListClasses With Invalid Params")
    public void send_invalid_class_id(){
        List_Classes = test.sendRequest("GET", "/admin/classes?class-id="+ class_id,null , data.Admin_Token);
    }
    @Then("Response Code Of ListClasses Is 400 and body returns with error message")
    public void ListClasses_Validate_invalid_param(){
        test.Validate_Error_Messages(List_Classes,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy",4002);
    }
}
