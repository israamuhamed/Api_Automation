Feature: Test all scenarios of the api of Start Session for Educator

  Scenario: Verify Start Session For Educator successfully
    Given   User Create Classes and Session for Educator to start session
    When    Performing the Api of Start Session
    And     Get Educator Session data from database
    Then    I verify the appearance of status code 200 and session started successfully

  Scenario: Verify Start Session For Educator with session not exist
    Given   Performing the Api of Start Session with session not exist
    Then    I verify the appearance of status code 404 and session not found for start

  Scenario: Verify Start Session For Educator with educator not authorized
    Given   Performing the Api of Start Session with educator not authorized
    Then    I verify the appearance of status code 403 and educator unauthorized



