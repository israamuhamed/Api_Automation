Feature: Test all scenarios of the api of List Grades By Country

  Scenario: Verify List Grades by Countries returned successfully
    Given   Performing the Api of List Grade by Country
    And     Getting Grades by country from database
    Then    I verify the appearance of status code 200 and Grades returned successfully

