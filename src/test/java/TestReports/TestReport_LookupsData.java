package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/Features/LookupsData"
        ,glue = "LookupsData"
        ,plugin = {"pretty", "html:target/Reports/TestReport_LookupsData.html"})

public class TestReport_LookupsData extends AbstractTestNGCucumberTests {
}
