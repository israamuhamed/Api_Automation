Feature: Test scenarios of the api Create Country

  Scenario: Verify create new country successfully
    Given   Performing the Api of Create Country With valid data
    Then    I verify the appearance of status code 200 and country created successfully

  Scenario: Verify create new country with country code already existed
    Given   Getting data of countries from database
    When    Performing the Api of Create Country With country code already existed
    Then    verify the appearance of status code 400 and error message With country code already existed

  Scenario: Verify create new country with country order already existed
    Given   Getting data of countries from database
    When    Performing the Api of Create Country With country order already existed
    Then    verify the appearance of status code 400 and error message With country order already existed

  Scenario: Verify create new country with country dial code already existed
    Given   Getting data of countries from database
    When    Performing the Api of Create Country With country dial code already existed
    Then    verify the appearance of status code 400 and error message With country dial code already existed

  Scenario: Verify create new country with country localization key already existed
    Given   Getting data of countries from database
    When    Performing the Api of Create Country With country localization key already existed
    Then    verify the appearance of status code 400 and error message With country localization key already existed

  Scenario: Verify create new country with invalid body
    Given   Performing the Api of Create Country With invalid parameter
    Then    verify the appearance of status code 400 and error message invalid request

  Scenario: Verify create new country with unauthorized data
    Given   Performing the Api of Create Country With unauthorized data
    Then    verify the appearance of status code 403 and error message unauthorized
