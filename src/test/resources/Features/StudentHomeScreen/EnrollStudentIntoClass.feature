Feature: Test all scenarios of the api Enroll Student Into Class

  Scenario: Verify Enroll Student Into Class Successfully
    Given   User Send Valid student Id and class Id to enroll student into class
    When    Performing the Api Enroll Student Into Class
    Then    I verify the appearance of status code 200 and student enrolled to class
#    And     Verify enrollment done successfully into database


  Scenario: Verify Enroll Student Into Invalid Class
    Given   User Send Valid student Id and invalid class Id to enroll student into class
    When    Performing the Api Enroll Student Into Class
    Then    I verify the appearance of status code 400 and class not exist for enrollment

  Scenario: Verify Enroll Student Into Class with invalid token
    Given   User Send Valid student Id and invalid class Id to enroll student into class
    When    Performing the Api Enroll Student Into Class with invalid token
    Then    I verify the appearance of status code 403 and this student is not authorized



