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

public class CreateEducationalResource {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    Database_Connection Connect = new Database_Connection();
    Faker fakeDate =new Faker();
    String name = fakeDate.name().name();
    String cdn = fakeDate.name().name();
    Long fileTypeID = Long.valueOf(fakeDate.number().randomDigitNotZero());
    Long resourceTypeID = Long.valueOf(fakeDate.number().randomDigitNotZero());
    public Long resourceId;
    Response Create_Educational_Resources;
    Response Invalid_Educational_Resources;
    Response unauthorized_admin;
    Long ResourceID = Long.valueOf(String.format("%012d", fakeDate.number().randomNumber(12, true)));

    String Invalid_body = "{\"resource_id\":," +
            "\"name\":\"\"," +
            "\"cdn\":\"\"," +
            "\"bucket\":\"test_bucket_1\"," +
            "\"key\":\"NagwaClasses/510130705852/869165016176.zip\"," +
            "\"md5\":\"test1\"," +
            "\"is_test\":true," +
            "\"file_type_id\":2," +
            "\"resource_type_id\":," +
            "\"educational_resource_thumbnail_url\":\"https://example.com\"," +
            "\"educational_resource_order\":}";

    @Given("Performing the Api of Create Educational Resources")
    public Long  Create_new_educational_resources() throws InterruptedException {
         String valid_body = "{\"resource_id\":"+ ResourceID +"," +
                 "\"name\":\""+ name +"\"," +
                 "\"cdn\":\"https://handouts-materials.nagwa.com/\"," +
                 "\"bucket\":\"handouts-materials\"," +
                 "\"key\":\"NagwaClasses/510130705852/869165016176.zip\"," +
                 "\"md5\":\"test1\"," +
                 "\"is_test\":true," +
                 "\"file_type_id\":"+ fileTypeID +"," +
                 "\"resource_type_id\":1," +
                "\"educational_resource_thumbnail_url\":\"https://example.com\"," +
                 "\"educational_resource_order\":"+ resourceTypeID +"}";

        Create_Educational_Resources = test.sendRequest("POST", "/admin/educational-resources", valid_body, data.Admin_Token);
        Thread.sleep(8000);
        System.out.println(valid_body);
        Create_Educational_Resources.prettyPrint();
        return resourceId = Create_Educational_Resources.then().extract().path("resource_id");
    }

    @And("Getting educational resource from database")
    public void  get_educational_resource_from_db() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from educational_resources er where educational_resources_name = \'"+ name +"\' and educational_resource_cdn = \'"+ cdn +"\'");
        while (resultSet.next()) {

            resourceId = resultSet.getLong("educational_resource_id");
        }
    }

    @Then("I verify the appearance of status code 200 and educational resource created successfully")
    public void Validate_Response_of_create_Educational_resource() {
        Create_Educational_Resources.prettyPrint();
        Create_Educational_Resources.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/CreateEducationalResource.json")))
                .body("message", hasToString("Educational resource created/updated successfully."),"resource_id",equalTo(resourceId));
    }

    @Given("Performing the Api of Create Educational Resources with invalid data")
    public void  new_educational_resources_InvalidData() {
        Invalid_Educational_Resources = test.sendRequest("POST", "/admin/educational-resources", Invalid_body, data.Admin_Token);
    }

    @Then("I verify the appearance of status code 400 and body data is not correct")
    public void Validate_Response_of_Educational_resource_notValid() {
        Response invalidBodyData = Invalid_Educational_Resources;
        test.Validate_Error_Messages(invalidBodyData,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("Performing the Api of Create educational resource With invalid token")
    public void Create_resources_with_invalid_token() {
        String valid_body = "{\"resource_id\":"+ ResourceID +"," +
                "\"name\":\""+ name +"\"," +
                "\"cdn\":\"https://handouts-materials.nagwa.com/\"," +
                "\"bucket\":\"handouts-materials\"," +
                "\"key\":\"NagwaClasses/510130705852/869165016176.zip\"," +
                "\"md5\":\"test1\"," +
                "\"is_test\":true," +
                "\"file_type_id\":2," +
                "\"resource_type_id\":"+ fileTypeID +"," +
                "\"educational_resource_thumbnail_url\":\"https://example.com\"," +
                "\"educational_resource_order\":"+ resourceTypeID +"}";
        unauthorized_admin = test.sendRequest("POST", "/admin/classes", valid_body,data.refresh_token_for_notActiveEducator);
    }

    @Then("I verify the appearance of status code 403 and admin is not authorized")
    public void Validate_Response_of_unauthorized_userId(){
        Response unauthorizedUser = unauthorized_admin;
        test.Validate_Error_Messages(unauthorizedUser,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
}
