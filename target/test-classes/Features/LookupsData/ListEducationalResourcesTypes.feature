Feature: Test all scenarios of the api of List Educational Resources Types

  Scenario: Verify List Educational Resources Types returned successfully
    Given   Performing the Api of List Educational Resources Types
    And     Getting Educational Resources Types from database
    Then    I verify the appearance of status code 200 and Educational Resources Types returned successfully

