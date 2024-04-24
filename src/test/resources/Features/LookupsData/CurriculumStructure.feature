Feature: Test all scenarios of the api of Curriculum Structure

  Scenario: Verify Curriculum Structure returned successfully
    Given   Performing the Api of Curriculum Structure
    And     Getting Countries,Stages,Grades and Subjects from database
    Then    I verify the appearance of status code 200 and Curriculum Structure returned successfully

