package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {"src/test/resources/Features/AdminArea"}
        ,glue = "AdminArea"
        ,plugin = {"pretty", "html:target/Reports/TestReport_AdminArea.html"})

public class TestReport_AdminArea extends AbstractTestNGCucumberTests {
}
