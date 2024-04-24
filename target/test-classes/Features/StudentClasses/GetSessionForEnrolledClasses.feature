Feature: Test all scenarios of the api of Getting sessions for enrolled classes

  Scenario: Verify Getting sessions for Enrolled user into class
    Given   user send class contains sessions that user enrolled in
    When    Perform then api of get_sessions_for_enrolled_class
    And     Get Session for Enrolled Classes From Database
    Then    I verify the appearance of status code 200 and all sessions of enrolled class

  Scenario: verify sending class has no sessions
    Given   User Send Class that has no sessions
    When    Perform then api of get_sessions_for_enrolled_class
    And     Get Empty Session array for Enrolled Classes From Database
    Then    I verify the appearance of status code 200 and empty list of classes

  Scenario: Verify sending student is not enrolled into class
    Given   user send student is not enrolled in the class
    When    Perform then api of get_sessions_for_enrolled_class
    Then    I verify the appearance of status code 403 and user unauthorized

  Scenario: Verify sending class is not found
    Given   user send class is not exist
    When    Perform then api of get_sessions_for_enrolled_class
    Then    I verify the appearance of status code 404 and class not found