Feature: Test all scenarios of the api of leave ongoing session

  Scenario: Verify user can leave the joined session successfully
    Given   user send valid data to leave the joined session
    When    Performing the Api leave on going session
    Then    The Response should contains status code 200 and message Successfully left the session

  Scenario: Verify user can't leave session he isn't part of it yet
    Given   User send session_id that he isn't part of
    When    Performing the Api leave on going session
    Then    I verify status code 404 and message student is not currently part of the session

  Scenario: Verify unauthorized user can't leave session
    Given   User Send Invalid StudentId to leave session
    When    Performing the Api leave on going session
    Then    I verify Status Code 403 And Error Message user unauthorized



