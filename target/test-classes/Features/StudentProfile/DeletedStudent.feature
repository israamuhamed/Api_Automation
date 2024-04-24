Feature: Test all scenarios of the api of Deleted Student

  Scenario: Verify Deleted Student successfully
    Given   User Send valid student Id to delete student
    When    Performing the Api of delete student
    Then    I verify the appearance of status code 200 and student deleted successfully
    And     make sure that the student deleted from database

  Scenario: Verify Deleted Student with student not exist
    Given   User Send Invalid student Id to delete student
    When    Performing the Api of delete student with student not exist
    Then    I verify the appearance of 404 status code and student id is not found

  Scenario: Verify Deleted Student with invalid student token
    Given   User Send Invalid student Id to delete student
    When    Performing the Api of delete student with not valid token
    Then    I verify the appearance of status code 403 and this student account is unauthorized

  Scenario: Verify Deleted Student with invalid student id
    Given   User Send special characters in student Id to delete account
    When    Performing the Api of delete student with not valid token
    Then    I verify the appearance of status code 400 and the student_id is Invalid

