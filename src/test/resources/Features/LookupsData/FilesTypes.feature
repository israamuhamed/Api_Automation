Feature: Test all scenarios of the api of List Files Types

  Scenario: Verify List Files Types returned successfully
    Given   Performing the Api of List Files Types
    And     Getting Files Types from database
    Then    I verify the appearance of status code 200 and file types returned successfully

