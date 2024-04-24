Feature: Test all scenarios of the api of Create Student

  Scenario: Verify create new Student successfully
    Given   Performing the Api of Create Student With valid data
    When    Get student data from database
    Then    I verify the appearance of status code 201 and Student created successfully
    And     Performing the Api of Verify Student OTP with already Auth Student
    Then    I verify the appearance of status code 200 and student already authenticated

  Scenario: Verify create student with invalid grade
    Given   Performing the Api of Create Student With grade not exist
    Then    I verify the appearance of status code 400 and Invalid Grade

  Scenario: Verify create student with invalid data
    Given   Performing the Api of Create Student With invalid data
    Then    I verify the appearance of status code 400 and Invalid data

  Scenario: Verify create student with unauthorized student
    Given   Performing the Api of Create Student With unauthorized student
    Then    I verify the appearance of status code 400 and student is unauthorized

