Feature: Test all scenarios of the api of Deactivate Student

  Scenario: Verify Deactivate Student successfully
    Given   User Send valid student Id to deactivate student
    When    Performing the Api of deactivate student
    Then    I verify the appearance of status code 200 and student deactivated successfully

  Scenario: Verify Deactivate Student with student not exist
    Given   User Send Invalid student Id to deactivate student
    When    Performing the Api of deactivate student with student not exist
    Then    I verify the appearance of 404 status code and student is not found

  Scenario: Verify Deactivate Student with invalid student token
    Given   User Send Invalid student Id to deactivate student
    When    Performing the Api of deactivate student with not valid token
    Then    I verify the appearance of status code 403 and this student is unauthorized

  Scenario: Verify Deactivate Student with invalid student id
    Given   User Send special characters in student Id to deactivate student
    When    Performing the Api of deactivate student with not valid token
    Then    I verify the appearance of status code 400 and the path param is Invalid

