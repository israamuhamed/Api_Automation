package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


    @CucumberOptions(features = "src/test/resources/Features/EducatorProfile"
                    ,glue = "EducatorProfile"
                    ,plugin = {"pretty", "html:target/Reports/TestReport_EducatorProfile.html"})

    public class TestReport_EducatorProfile extends AbstractTestNGCucumberTests {

    }




