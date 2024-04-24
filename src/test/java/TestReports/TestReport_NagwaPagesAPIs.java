package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {"src/test/resources/Features/NagwaPagesAPIs"}
        ,glue = "NagwaPagesAPIs"
        ,plugin = {"pretty", "html:target/Reports/TestReport_NagwaPagesAPIs.html"})

public class TestReport_NagwaPagesAPIs extends AbstractTestNGCucumberTests {

}
