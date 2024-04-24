Feature: Test all scenarios of the api of Create Educational Resources

  Scenario: Verify create new educational resources successfully
    Given   Performing the Api of Create Educational Resources
    And     Getting educational resource from database
    Then    I verify the appearance of status code 200 and educational resource created successfully

  Scenario: Verify create educational resources with invalid data
    Given   Performing the Api of Create Educational Resources with invalid data
    Then    I verify the appearance of status code 400 and body data is not correct

  Scenario: Verify create educational resources with invalid token
    Given   Performing the Api of Create educational resource With invalid token
    Then    I verify the appearance of status code 403 and admin is not authorized

