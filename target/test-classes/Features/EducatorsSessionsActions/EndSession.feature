Feature: Test all scenarios of the api of End Session for Educator

  Scenario: Verify End Session For Educator successfully
    Given   User Start new session
    When    Performing the Api of End Session
    Then    I verify the appearance of status code 200 and session Ended successfully

  Scenario: Verify End Session For Educator with invalid token
    Given   User Start new session
    When    Performing the Api of End Session with invalid token
    Then    I verify the appearance of status code 403 and educator is unauthorized

  Scenario: Verify End Session For Educator with session not exist
    Given   User Start new session
    When    Performing the Api of End Session with session not exist
    Then    I verify the appearance of status code 404 and session is found to end

  Scenario: Verify Start Session For Educator with ended session
    Given   Performing the Api of Start Session with ended session
    Then    I verify the appearance of status code 404 and session is ended



