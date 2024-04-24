Feature: Test all scenarios of the api Get Session

  Scenario: Verify Get session data successfully
    Given   User Send valid session Id to get session data
    When    Performing the Api of Get session
    And     Getting data of created session from database
    Then    I verify the appearance of status code 200 and session data returned successfully

  Scenario: Verify Get session api with invalid session id in the path
    Given   send invalid session id in the path
    When    Performing the Api of Get session
    Then    I verify the appearance of status code 400 and invalid path

  Scenario: Verify Get session api with session id not exist
    Given   send session id is not exist
    When    Performing the Api of Get session
    Then    I verify the appearance of status code 404 and session is eligible to display

  Scenario: Verify Get session api with unauthorized user
    Given   User Send valid session Id to get session data
    When    Performing the Api of Get session with invalid token
    Then    I verify the appearance of status code 403 and Unauthorized


