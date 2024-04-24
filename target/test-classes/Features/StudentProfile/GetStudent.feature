Feature: Test all scenarios of the api of Get Student

  Scenario: Verify get student data successfully
    Given   User Send valid student Id to get student
    When    Performing the Api of Get Student Profile
    Then    I verify the appearance of status code 200 and student data returned

  Scenario: Verify get student with invalid id contains more than 12 digits
    Given   User Send Invalid student Id
    When    Performing the Api of Get Student Profile
    Then    I verify the appearance of status code 422 and Id is incorrect

  Scenario: Verify get student with unauthorized student id
    Given   User Send unauthorized student Id
    When    Performing the Api of Get Student Profile
    Then    I verify the appearance of status code 403 and Id is unauthorized

  Scenario: Verify get student with inactive student id
    Given   User Send inactive student Id
    When    Performing the Api of Get Student Profile with inactive student
    Then    I verify the appearance of status code 404 and Id is inactive

