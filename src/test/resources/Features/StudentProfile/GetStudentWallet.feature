Feature: Test all scenarios of the api of Get Student wallet

  Scenario: Verify get student wallet successfully
    Given   User Send valid student Id to get wallet
    When    Performing the Api of Get Student Wallet
    Then    I verify the appearance of status code 200 and student wallet data returned

  Scenario: Verify get student wallet with student not exist
    Given   User Send Invalid StudentId to get student wallet
    When    Performing the Api of get wallet with student not exist
    Then    I verify the appearance of status code 404 and student is not exist

  Scenario: Verify get student wallet with student not authorized
    Given   User Send Invalid StudentId to get student wallet
    When    Performing the Api of get wallet with student that not authorized
    Then    I verify the appearance of status code 403 and student not auth

  Scenario: Verify get student wallet with invalid student ID
    Given   User Send Invalid std Id to get student wallet
    When    Performing the Api of get wallet with student that not authorized
    Then    I verify the appearance of status code 400 and Id is incorrect
