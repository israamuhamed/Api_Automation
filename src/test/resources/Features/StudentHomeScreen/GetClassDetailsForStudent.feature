Feature: Test all scenarios of the api of Get Class Details

  Scenario: Verify Getting Class Details of Student Successfully
    Given   Getting student already enrolled into class
    When    Performing the Api of Get Class Details
    And     Getting class details from database
    Then    I verify the appearance of status code 404 and this class is test class
#    Then    I verify the appearance of status code 200 and student classes details returned

  Scenario: Verify Getting Class Details with invalid class
    Given   Performing the Api of Get Class Details with invalid class
    Then    I verify the appearance of status code 400 and this class is not exist

  Scenario: Verify Getting Class Details with student not authorized
    Given   Performing the Api of Get Class Details with student not exist
    Then    I verify the appearance of status code 403 and this student is not_authorized

  Scenario: Verify Getting Class Details with invalid student and class
    Given   Performing the Api of Get Class Details with invalid student and class
    Then    I verify the appearance of status code 400 and invalid params



