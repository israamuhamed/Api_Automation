Feature: Test all scenarios of the api Edit Educator

  Scenario: Verify edit new educator successfully
    Given   User Create new Educator For Update
    When    Performing the Api of Edit Educator With valid data
    Then    I verify the appearance of status code 200 and Educator updated successfully

  Scenario: Verify edit new educator with invalid email format
    Given   User Create new Educator For Update
    When    Performing the Api of Edit Educator With Invalid email format
    Then    I verify the appearance of status code 400 and email is invalid

  Scenario: Verify edit new educator with invalid body
    Given   User Create new Educator For Update
    When    Performing the Api of Edit Educator With Invalid body
    Then    I verify the appearance of status code 400 and parameter is invalid

  Scenario: Verify edit new educator with invalid token
    Given   User Create new Educator For Update
    When    Performing the Api of Edit Educator With invalid token
    Then    I verify the appearance of status code 403 and unauthorized user


