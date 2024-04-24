Feature: Test all scenarios of the api of Getting enrolled classes

  Scenario: Verify Getting all enrolled classes for student
    Given   Create classes and session for student
    And     user send user id to get all upcoming sessions
    When    Perform the api of Get_Enrolled_Classes
    And     Get Enrolled Classes from database
    Then    I Verify The appearance of status code 200 and all upcoming sessions

  Scenario: Verify sending invalid user id
    Given   user send invalid user id
    When    Perform the api of Get_Enrolled_Classes
    Then    I verify the appearance of  status code 403 and user unauthorized in getEnrolledClasses

