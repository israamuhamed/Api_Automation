Feature: Test all scenarios of the api Get Educator

  Scenario: Verify get educator with valid data successfully
    Given   User Send valid educator Id to get educator data
    When    Performing the Api of Get Educator
    Then    I verify the appearance of status code 200 and Educator data returned successfully

  Scenario: Verify get educator with invalid educator id
    Given   User Send invalid educator Id to get educator data
    When    Performing the Api of Get Educator with invalid educator id
    Then    I verify the appearance of status code 400 and path is incorrect

  Scenario: Verify get educator with invalid educator id containing special char
    Given   User Send special char in educator Id
    When    Performing the Api of Get Educator with invalid educator id
    Then    I verify the appearance of status code 400 and path is incorrect

  Scenario: Verify get the educator with invalid token
    Given   User Send unauthorized educatorID to GetEducator Api
    When    performing the api of GetEducator with invalid token
    Then    I verify the appearance of status code 403 and EducatorID is unauthorized

  Scenario: Verify get educator Api with not active educator
    Given   User Send not active educatorID to GetEducator
    When    performing the api of GetEducator with notActive educator token
    Then    I verify the appearance of status code 404 and Educator is not active

  Scenario: Verify get educator api with deleted Educator
    Given   User Send deleted educatorID
    When    performing the api of GetEducator with deleted educator
    Then    I verify the appearance of status code 404 and EducatorID is deleted


