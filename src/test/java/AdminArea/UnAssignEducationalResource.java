package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.sql.SQLException;

public class UnAssignEducationalResource {

    TestBase test = new TestBase();
    AssignEducationalResources assign = new AssignEducationalResources();
    Educator_TestData data = new Educator_TestData();
    Response UnAssign_EducationalResource;
    String valid_Body;
    Long invalid_session_id = 123456789000L;
    Long invalid_educational_resource = 123456745678L;
    String Body_with_invalid_session;
    String Body_with_invalid_resource;
    @Given("User Send Valid SessionId and EducationalResourceId")
    public void unAssign_EducationalResource_valid_Data() throws SQLException, InterruptedException {
        assign.Create_new_Assign_resources();
         valid_Body = "{\"sessions_ids\":["+ assign.SessionID +"],\"educational_resource_id\":"+ assign.ResourceId +"}";
    }
    @When("Performing The API of UnAssignEducationalResource")
    public void unAssign_Educational_Resource_Successfully(){
        String valid_Body = "{\"sessions_ids\":["+ assign.SessionID +"]," +
                            "\"educational_resource_id\":"+ assign.ResourceId +"}";
        UnAssign_EducationalResource = test.sendRequest("POST","/admin/unassign-educational-resource", valid_Body , data.Admin_Token);
    }
    @Then("Response code is 200 and body returns with success message")
    public void validate_valid_unAssignEducational_Response_body(){
        System.out.println(assign.SessionID + " " + assign.ResourceId);
        UnAssign_EducationalResource.then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message",containsString(" unassigned successfully"))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/UnAssignEducationalResource.json")));
    }
    @Given("User Send List Of Sessions contain session that educational resource doesn't assigned to")
    public void Define_request_body_with_invalid_session_id() throws SQLException, InterruptedException {
        assign.Create_new_Assign_resources();
        Body_with_invalid_session = "{\"sessions_ids\":["+ assign.SessionID +","+ invalid_session_id +"],\"educational_resource_id\":"+ assign.ResourceId +"}";
    }
    @When("Performing The API of UnAssignEducationalResource With Body Contains Invalid SessionId")
    public void unAssignEducationalResource_With_Invalid_Session(){
        UnAssign_EducationalResource = test.sendRequest("POST","/admin/unassign-educational-resource", Body_with_invalid_session , data.Admin_Token);
    }
    @Then("Response code is 207 and body returns with success message and list that educational resource isn't assigned to")
    public void Validate_UnAssignEducationalResource_With_invalid_session_Response(){
        UnAssign_EducationalResource.prettyPrint();
        UnAssign_EducationalResource.then()
                .assertThat()
                .statusCode(HttpStatus.SC_MULTI_STATUS)
                .body("message",containsString("Educational resource unassigned successfully with failed records."))
                .body("failed_records",hasItem(invalid_session_id))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/UnAssignEducationalResourcWithFaileRecords.json")));
    }
    @When("Performing The API of UnAssignEducationalResource With Invalid Token")
    public void UnAssignEducationalResource_With_Invalid_Token(){
        UnAssign_EducationalResource = test.sendRequest("POST","/admin/unassign-educational-resource", valid_Body , data.refresh_token);
    }
    @Then("Response Code Of UnAssignEducationalResource Is 403 And Body Returns With Error Message")
    public void validate_UnAssignEducationalResource_unauthorized(){
       test.Validate_Error_Messages(UnAssign_EducationalResource,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("User Send Invalid Body To Request With EducationalResource Isn't Assigned To Session")
    public void invalid_educational_resource_id() throws SQLException, InterruptedException {
        assign.Create_new_Assign_resources();
        Body_with_invalid_resource = "{\"sessions_ids\":["+ assign.SessionID +"],\"educational_resource_id\":"+ invalid_educational_resource +"}";
    }
    @When("Performing The API Of UnAssignEducationalResource With Invalid Body")
    public void send_request_with_invalid_educational_resource(){
        UnAssign_EducationalResource = test.sendRequest("POST","/admin/unassign-educational-resource", Body_with_invalid_resource , data.Admin_Token);
    }
    @Then("Response code Of UnAssignEducationalResource is 400 and Body Returns With Error Message")
    public void Validate_response_of_invalid_Educational_resource_id(){
        test.Validate_Error_Messages(UnAssign_EducationalResource,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy",4002);
    }
}
