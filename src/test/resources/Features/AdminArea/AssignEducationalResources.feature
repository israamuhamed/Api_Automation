Feature: Test all scenarios of the api of Create Assign Educational Resources

  Scenario: Verify create new Assign educational resources successfully
    Given   Performing the Api of Assign Educational Resources
    And     Getting Assign educational resource from database
    Then    I verify the appearance of status code 201 and Assign educational resource created successfully

  Scenario: Verify create new Assign educational resources with session not exist
    Given   Performing the Api of Assign Educational Resources with session not found
    Then    I verify the appearance of status code 404 and session not found

  Scenario: Verify create new Assign educational resources with resource not exist
    Given   Performing the Api of Assign Educational Resources with resource not found
    Then    I verify the appearance of status code 404 and resource not found

  Scenario: Verify create new Assign educational resources with invalid data
    Given   Performing the Api of Assign Educational Resources with invalid data
    Then    I verify the appearance of status code 400 and bodyData is not correct

  Scenario: Verify create new Assign educational resources with invalid token
    Given   Performing the Api of Create Assign educational resource With invalid token
    Then    I verify the appearance of status code 403 and adminId is not authorized

