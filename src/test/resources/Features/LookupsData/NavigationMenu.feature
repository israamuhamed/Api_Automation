Feature: Test all scenarios of the api of Navigation Menu

  Scenario: Verify navigation menu returned successfully
    Given   Performing the Api of Navigation Menu
    And     Getting Countries ,stages and languages  from database
    Then    I verify the appearance of status code 200 and Menu returned successfully
