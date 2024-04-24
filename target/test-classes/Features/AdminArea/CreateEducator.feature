Feature: Test all scenarios of the api Create Educator

  Scenario: Verify create new educator successfully
    Given   Performing the Api of Create Educator With valid data
    Then    I verify the appearance of status code 200 and Educator created successfully

  Scenario: Verify create new educator with invalid email
    Given   Performing the Api of Create Educator With Invalid email
    Then    I verify the appearance of status code 400 and Educator email is not correct

  Scenario: Verify create new educator without first or last names
    Given   Performing the Api of Create Educator With valid data
    Then    I verify the appearance of status code 200 and Educator created successfully

  Scenario: Verify create new educator with email already exist
    Given   Performing the Api of Create Educator With email already exist
    Then    I verify the appearance of status code 409 and this email already exist

  Scenario: Verify create new educator with invalid token
    Given   Performing the Api of Create Educator With invalid token
    Then   I verify the appearance of status code 403 and Educator is not authorized