Feature: Test all scenarios of the api Create Class

  Scenario: Verify create new full pay class successfully
    Given   Performing the Api of Create class full pay With valid data
    Then    I verify the appearance of status code 201 and class created successfully

  Scenario: Verify create new class per session successfully
    Given   Performing the Api of Create class per session With valid data
    Then    I verify the appearance of status code 201 and class created successfully

  Scenario: Verify create new class block payment successfully
    Given   Performing the Api of Create class block payment With valid data
    Then    I verify the appearance of status code 201 and class created successfully

  Scenario: Verify create new class with invalid token
    Given   Performing the Api of Create class With invalid token
    Then    I verify the appearance of status code 403 and user is not authorized

  Scenario: Verify create new class with invalid body
    Given   Performing the Api of Create class With invalid body
    Then    I verify the appearance of status code 400 and body incorrect


