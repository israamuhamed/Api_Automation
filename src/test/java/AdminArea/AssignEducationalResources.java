package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class AssignEducationalResources {
    TestBase test = new TestBase();
    CreateSession session = new CreateSession();
    CreateEducationalResource resource = new CreateEducationalResource();
    Educator_TestData data = new Educator_TestData();
    Database_Connection Connect = new Database_Connection();
    public Long SessionID;
    public Long ResourceId;
    public Long Class_Id;
    public Long EducatorId;
    Response Assign_Educational_Resource;
    Response Assign_Resource_InvalidSession;
    Response Assign_Resource_InvalidResource;
    Response Invalid_Assign_Educational_Resources;
    Response unauthorized_admin;

    @Given("Performing the Api of Assign Educational Resources")
    public void  Create_new_Assign_resources() throws SQLException, InterruptedException {
        session.Create_Session();
        resource.Create_new_educational_resources();
        Class_Id = session.Class_ID;
        EducatorId = session.EducatorId;
        SessionID = session.sessionId;
        ResourceId = resource.resourceId;
        System.out.println("SessionID "+ SessionID +"ResourceId " + ResourceId);
        String valid_body = "{" +
                            "\"sessions_ids\":["+ SessionID +"]," +
                            "\"educational_resource_id\":"+ ResourceId +
                            "}";
        Assign_Educational_Resource = test.sendRequest("POST", "/admin/assign-educational-resource", valid_body, data.Admin_Token);
    }

    @And("Getting Assign educational resource from database")
    public void  get_educational_resource_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from sessions_educational_resources where session_id = \'"+ SessionID +"\' ");
        while (resultSet.next()) {
            SessionID = resultSet.getLong("session_id");
        }
        System.out.println("Session Id = " +SessionID);
    }

    @Then("I verify the appearance of status code 201 and Assign educational resource created successfully")
    public void Validate_Response_of_create_Educational_resource() {
        Assign_Educational_Resource.prettyPrint();
        Assign_Educational_Resource.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/AssignEducationalResources.json")))
                .body("message", hasToString("Educational resource assigned to session successfully."));
    }

    @Given("Performing the Api of Assign Educational Resources with session not found")
    public void  Create_Assign_resources_session_notFound() throws InterruptedException {
        resource.Create_new_educational_resources();
        ResourceId = resource.resourceId;
        SessionID = 123456789034L;
        String Invalid_session_body =   "{" +
                                        "\"sessions_ids\":["+ SessionID +"]," +
                                        "\"educational_resource_id\":"+ ResourceId +
                                        "}";
        Assign_Resource_InvalidSession = test.sendRequest("POST", "/admin/assign-educational-resource", Invalid_session_body, data.Admin_Token);
    }

    @Then("I verify the appearance of status code 404 and session not found")
    public void Validate_Response_of_session_notFound_into_resource() {
        Response InvalidSessionBody = Assign_Resource_InvalidSession;
        test.Validate_Error_Messages(InvalidSessionBody,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("Performing the Api of Assign Educational Resources with resource not found")
    public void  Create_Assign_resources_resource_notFound() throws SQLException {
        session.Create_Session();
        SessionID = session.sessionId;
        String Invalid_session_body =   "{" +
                                        "\"sessions_ids\":["+ SessionID +"]," +
                                        "\"educational_resource_id\":123456789034" +
                                        "}";
        Assign_Resource_InvalidResource = test.sendRequest("POST", "/admin/assign-educational-resource", Invalid_session_body, data.Admin_Token);
    }

    @Then("I verify the appearance of status code 404 and resource not found")
    public void Validate_Response_of_resource_notFound() {
        Response InvalidResourceBody = Assign_Resource_InvalidResource;
        test.Validate_Error_Messages(InvalidResourceBody,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("Performing the Api of Assign Educational Resources with invalid data")
    public void  new_assign_educational_resources_InvalidData() {
        String  Invalid_body = "{" +
                "\"sessions_ids\":[???]," +
                "\"educational_resource_id\":????" +
                "}";
        Invalid_Assign_Educational_Resources = test.sendRequest("POST", "/admin/educational-resources", Invalid_body, data.Admin_Token);
    }

    @Then("I verify the appearance of status code 400 and bodyData is not correct")
    public void Validate_Response_of_Assign_Educational_resource_notValid() {
        Response invalidBodyData = Invalid_Assign_Educational_Resources;
        test.Validate_Error_Messages(invalidBodyData,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("Performing the Api of Create Assign educational resource With invalid token")
    public void Create_Assign_resources_with_invalid_token() throws SQLException {
        session.Create_Session();
        SessionID = session.sessionId;
        String Invalid_session_body = "{\"sessions_ids\":["+ SessionID +"]," +
                                       "\"educational_resource_id\":123456789034}";
        unauthorized_admin = test.sendRequest("POST", "/admin/classes", Invalid_session_body,data.refresh_token_for_notActiveEducator);
    }

    @Then("I verify the appearance of status code 403 and adminId is not authorized")
    public void Validate_Response_of_unauthorized_userId(){
        Response unauthorizedUser = unauthorized_admin;
        test.Validate_Error_Messages(unauthorizedUser,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }


}