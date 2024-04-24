Feature: Test all scenarios of the api of unlock session

  Scenario: Verify unlock session for student successfully
    Given   User Send Session Id to unlock session for user
    When    Performing the Api of Unlock Session
    Then    I verify the appearance of status code 201 and Session successfully unlocked

  Scenario: Verify unlock session already unlocked
    Given   User Send Session Id to unlock session for user
    When    Performing the Api of Unlock Session
    And     Performing the Api of Unlock Session
    Then    I verify the appearance of status code 200 and Session already unlocked

  Scenario: Verify can't unlock session for user not authorized
    Given   User Send unauthorized student id
    When    Performing the Api of Unlock Session
    Then    The Response of unlockSession Should Contain Status Code 403 And Error Message Unauthorized

  Scenario: Verify sending student doesn't have sufficient wallet
    Given   student's wallet does not have sufficient wallet for unlock session
    When    Performing the Api of Unlock Session
    Then    The Response of unlockSession Should Contain Status Code 422 And Error Message insufficient student wallet balance

  Scenario: Verify sending class does not allow pay per session
    Given   class does not allow pay per session
    When    Performing the Api of Unlock Session
    Then    The Response Should Contain Status Code 422 And Error Message pay per session not allowed



