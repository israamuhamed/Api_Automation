Feature: Test all scenarios of the api of Update Student

  Scenario: Verify Update student data successfully
    Given   User Send valid student Id to update profile
    When    Performing the Api of Update Student Profile
    And     I verify the appearance of status code 200 and student data updated
    Then    Validate data updated successfully into database

  Scenario: Verify Update student with Invalid data
    Given   User Send valid student Id to update profile
    When    Performing the Api of Update Student Profile with invalid data
    Then    I verify the appearance of status code 400 and student data Invalid

  Scenario: Verify Update student with Invalid Grade id
    Given   User Send valid student Id to update profile
    When    Performing the Api of Update Student Profile with invalid grade
    Then    I verify the appearance of status code 422 and grade id Invalid

  Scenario: Verify Update student with Invalid student id
    Given   User Send Invalid student Id to update profile
    When    Performing the Api of Update Student with invalid student id
    Then    I verify the appearance of status code 403 and student is unauthorized

  Scenario: Verify Update student with student id not found
    Given   User Send Invalid student Id to update profile
    When    Performing the Api of Update Student Profile with student not exist
    Then    I verify the appearance of status code 404 and student is not found


