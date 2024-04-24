package LookupsData;

import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import StudentClasses.Student_TestData;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class ListEducationalResourcesTypes {
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    Student_TestData data = new Student_TestData();
    Response Educational_Resources_Types;
    Integer educational_resource_type_id ;
    String educational_resource_type;
    boolean is_listed;
    boolean available_post_session;

    @Given("Performing the Api of List Educational Resources Types")
    public void Educational_Resources_Types() {
        Educational_Resources_Types = test.sendRequest("GET", "/resources/types", null, data.Parent_refreshToken);
    }

    @And("Getting Educational Resources Types from database")
    public void getEducational_Resources_Types () throws SQLException {
        ResultSet EducationalResourcesTypes = Connect.connect_to_database("select * from educational_resources_types ert \n" +
                "where educational_resource_type_id = 1");

        while (EducationalResourcesTypes.next()) {
            educational_resource_type_id = EducationalResourcesTypes.getInt("educational_resource_type_id");
            educational_resource_type = EducationalResourcesTypes.getString("educational_resource_type");
            is_listed = EducationalResourcesTypes.getBoolean("is_listed_resource_type");
            available_post_session = EducationalResourcesTypes.getBoolean("available_post_session");

        }
    }

    @Then("I verify the appearance of status code 200 and Educational Resources Types returned successfully")
    public void Validate_Response_of_List_Educational_Resources_Types() {
        Educational_Resources_Types.prettyPrint();
        Educational_Resources_Types.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/LookupsData/ListEducationalResourcesTypes.json")))
                .body("educational_resource_type_id[0]", equalTo(educational_resource_type_id)
                        ,"educational_resource_type[0]",hasToString(educational_resource_type)
                        ,"is_listed[0]",equalTo(is_listed),"available_post_session[0]",equalTo(available_post_session));
    }

}
