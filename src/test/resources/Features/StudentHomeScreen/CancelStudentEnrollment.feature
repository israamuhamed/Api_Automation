Feature: Test all scenarios of the api Cancel Student Enrollment

  Scenario: Verify Cancel Student Enrollment Successfully
    Given   Create new user and enroll him into class
    When    Performing the Api of Cancel Enrollment
    Then    I verify the appearance of status code 200 and Enrollment canceled successfully

  Scenario: Verify Cancel Student Enrollment with invalid class
    Given   User Send Valid student Id and invalid class Id to cancel student enrollment
    Then    I verify the appearance of status code 400 and class not exist for cancellation

  Scenario: Verify Cancel Student Enrollment with invalid class and student
    Given   User Send InValid student Id and invalid class Id to cancel student enrollment
    Then    I verify the appearance of status code 400 and class or student is not correct

  Scenario: Verify Cancel Student Enrollment with invalid token
    Given   User Send invalid token to cancel student enrollment
    Then    I verify the appearance of status code 403 and token is not correct




