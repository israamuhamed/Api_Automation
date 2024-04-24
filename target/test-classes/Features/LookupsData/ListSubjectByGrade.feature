Feature: Test all scenarios of the api of List Grades By Country

  Scenario: Verify List Grades by Countries returned successfully
    Given   Performing the Api of List Subject by Grade
    And     Getting Subjects by Grades from database
    Then    I verify the appearance of status code 200 and Subjects returned successfully

