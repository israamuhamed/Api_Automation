package AdminArea.RefundAPIs;

import EducatorProfile.Educator_TestData;
import StudentClasses.PayForBlock;
import StudentClasses.Student_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.*;

public class ReverseBlock {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    Student_TestData studentData = new Student_TestData();
    PayForBlock BlockClass = new PayForBlock();
    Database_Connection Connect = new Database_Connection();
    Long student_id;
    Long class_id;
    Long class_ID;
    Integer class_seats_reserved;
    Integer student_wallet_transaction_type_id;
    Long student_wallet_transaction_item_id;
    String student_wallet_transaction_item;
    Long student_wallet_transaction_id;
    Long transaction_id;
    Response Reverse_Block;

    @Given("user pay for blocks in block payment class")
    public void send_valid_data_to_payForBlock() {
        BlockClass.send_valid_data_to_payForBlock();
        BlockClass.payFor_block_successfully();
        student_id = BlockClass.student_id;
        class_id = BlockClass.Class_ID;
    }

    @When("Refund partial block of total paid blocks")
    public void reverse_partial_blocks(){
       String invalid_body = "{\"class_id\":"+ class_id +"," +
                "\"student_id\":"+ student_id +"," +
                "\"blocks\":[1]}";
        Reverse_Block  = test.sendRequest("POST", "/admin/block-reverse", invalid_body,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 403 and Cannot return partial reverse")
    public void Validate_Response_of_Reverse_partialBlocks(){
        test.Validate_Error_Messages(Reverse_Block, HttpStatus.SC_FORBIDDEN,"cannot refund blocks because user purchased blocks after this block.",4036);
    }

    @When("Refund all block of total paid blocks")
    public void reverse_All_blocks(){
        String valid_body = "{\"class_id\":"+ class_id +"," +
                "\"student_id\":"+ student_id +"," +
                "\"blocks\":[1,2]}";
        Reverse_Block  = test.sendRequest("POST", "/admin/block-reverse", valid_body,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 200 and total blocks refunded successfully")
    public void Validate_Response_of_Reverse_AllBlocks(){
        Reverse_Block.prettyPrint();
        Reverse_Block.then()
                .statusCode(HttpStatus.SC_OK)
                .body("blocks", hasItems(1, 2))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/RefundAPIs/ReverseBlock.json")));

    }

    @And("Verify that the enrollment of student canceled successfully and reverse seats decreased")
    public void Validate_Reverse_Seats_Decreased() throws SQLException {

        ResultSet Classes = Connect.connect_to_database("select class_seats_reserved from classes c \n" +
                "where class_id = "+ class_id +"");
        ResultSet student_classes = Connect.connect_to_database("select class_id from classes_students \n" +
                "where class_id ="+ class_id +"");

       while(Classes.next()) {
           class_seats_reserved = Classes.getInt("class_seats_reserved");
            }
        while (student_classes.next()) {
            class_ID = student_classes.getLong("class_id");
            }
        Assert.assertEquals(class_seats_reserved,0);
        Assert.assertEquals(class_ID,null);

    }

    @And("Verify that the student wallet transactions updated successfully")
    public void Validate_wallet_transactions_updated() throws SQLException {
        ResultSet students_wallets_transactions = Connect.connect_to_database("select * from students_wallets_transactions swt \n" +
                "join students_wallets sw \n" +
                "on swt.student_wallet_id = sw.student_wallet_id \n" +
                "where sw.student_id ="+ student_id +" \n" +
                "and student_wallet_transaction_type_id=7 \n" +
                "order by student_wallet_transaction_created_at desc " +
                "limit 1");

        while (students_wallets_transactions.next()) {
            student_wallet_transaction_type_id = students_wallets_transactions.getInt("student_wallet_transaction_type_id");
            student_wallet_transaction_item_id = students_wallets_transactions.getLong("student_wallet_transaction_item_id");
            student_wallet_transaction_item = students_wallets_transactions.getString("student_wallet_transaction_item");

        }
        System.out.println("student_wallet_transaction_type_id "+student_wallet_transaction_type_id +" student_wallet_transaction_item_id "+student_wallet_transaction_item_id+" student_wallet_transaction_item "+student_wallet_transaction_item);

            JsonObject student_wallet_transaction_itemJSON = JsonParser.parseString(student_wallet_transaction_item).getAsJsonObject();
            transaction_id = student_wallet_transaction_itemJSON.get("transaction_id").getAsLong();
       System.out.println("transaction_id " + transaction_id);

        ResultSet students_transactions = Connect.connect_to_database("select * from students_wallets_transactions swt \n" +
                "join students_wallets sw \n" +
                "on swt.student_wallet_id = sw.student_wallet_id \n" +
                "where sw.student_id ="+ student_id +" \n" +
                "and student_wallet_transaction_type_id=1 \n" +
                "order by student_wallet_transaction_created_at desc " +
                "limit 1");

        while (students_transactions.next()) {
            student_wallet_transaction_id = students_transactions.getLong("student_wallet_transaction_id");
            System.out.println("student_wallet_transaction_id "+ student_wallet_transaction_id);
        }
        Assert.assertEquals(student_wallet_transaction_type_id, 7);
        Assert.assertEquals(student_wallet_transaction_item_id, student_wallet_transaction_id);
        Assert.assertEquals(transaction_id, student_wallet_transaction_id);
    }

    @When("Refund all block of not enrolled class")
    public void reverse_All_blocks_not_enrolled_Class(){
        String valid_body = "{\"class_id\":123456789012," +
                "\"student_id\":"+ student_id +"," +
                "\"blocks\":[1,2]}";
        Reverse_Block  = test.sendRequest("POST", "/admin/block-reverse", valid_body,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 403 and not enrolled in the class")
    public void Validate_Response_of_not_enrolled_class(){
        test.Validate_Error_Messages(Reverse_Block, HttpStatus.SC_FORBIDDEN,"Unauthorized access. Student does not have access to the resources of the requested class.",4036);
    }

    @When("Refund all block with unauthorized student")
    public void reverse_All_blocks_with_unauthStudent(){
        String valid_body = "{\"class_id\":"+ class_id +"," +
                "\"student_id\": "+ studentData.student_not_exist+" ," +
                "\"blocks\":[1,2]}";
        Reverse_Block  = test.sendRequest("POST", "/admin/block-reverse", valid_body,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 403 and student in not enrolled in the class")
    public void Validate_Response_of_not_enrolled_student(){
        test.Validate_Error_Messages(Reverse_Block, HttpStatus.SC_FORBIDDEN,"Unauthorized access. Student does not have access to the resources of the requested class.",4036);
    }

}