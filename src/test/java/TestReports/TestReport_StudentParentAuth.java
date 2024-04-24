package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/Features/StudentParentAuth"
                ,glue = "StudentParentAuth"
                , plugin = {"pretty", "html:target/Reports/TestReport_StudentParentAuth.html"})


public class TestReport_StudentParentAuth extends AbstractTestNGCucumberTests {
}
