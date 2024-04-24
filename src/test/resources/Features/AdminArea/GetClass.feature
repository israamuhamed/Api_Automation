Feature: Test all scenarios of the api Get Class

  Scenario: Verify get class with valid data successfully
    Given   User Send valid Class Id to get class data
    When    Performing the Api of Get Class
    And     Getting data of created class from database
    Then    I verify the appearance of status code 200 and class data returned successfully

  Scenario: Verify get class with Invalid class id
    Given   User Send Invalid Class Id to get class data
    When    Performing the Api of Get Class
    Then    I verify the appearance of status code 400 and class is not valid

  Scenario: Verify get class with not found class
    Given   User Send Class Id is not found
    When    Performing the Api of Get Class
    Then    I verify the appearance of status code 400 and class is not found

  Scenario: Verify get class with invalid admin token
    Given   User Send valid Class Id to get class data
    When    performing the api with invalid admin token
    Then    I verify the appearance of status code 403 and Admin is unauthorized

