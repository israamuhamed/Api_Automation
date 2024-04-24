Feature: Test all scenarios of the api of List Classes For Student

  Scenario: Verify List Classes For Student Successfully
    Given   enroll student into class
    When    Performing the Api of List Classes
    And     Get Student Classes from database
    Then    I verify the appearance of status code 200 and the classes list returned successfully

  Scenario: Verify List Classes For Student with student not authorized
    Given   Performing the Api of List Class with student not authorized
    Then    I verify the appearance of status code 403 and this student is un_authorized

  Scenario: Verify List Classes For Student with student not valid
    Given   Performing the Api of List Class with invalid student id
    Then    I verify the appearance of status code 400 and the student id is not valid






