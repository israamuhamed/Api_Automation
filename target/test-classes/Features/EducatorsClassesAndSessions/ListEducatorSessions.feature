Feature: Test all scenarios of the api List Educators Sessions

  Scenario: Verify Get List Educator Sessions data successfully
    Given   User Create Classes and Session for Educator to list Sessions for educator
    When    Performing the Api of list sessions for educator
    And     Get Educator Sessions from database
    Then    I verify the appearance of status code 200 and Educator Sessions data returned

  Scenario: Verify Get List Educator Sessions data empty
    Given   User Create Classes only for Educator to list Sessions
    When    Performing the Api of list sessions for educator with empty data
    And     Get Educator Classes from database
    Then    I verify the appearance of status code 200 and Educator Sessions data is empty

  Scenario: Verify Get List Educator Sessions with class id not exist
    Given   User Create Educator without assigning any classes to him
    When    Performing the Api of list sessions for educator with invalid class
    Then    I verify the appearance of status code 404 and class is not exist

  Scenario: Verify Get List Educator Sessions with unauthorized Educator
    Given   User Create Educator without assigning any classes to him
    When    Performing the Api of list sessions for educator with unauthorized educator
    Then    I verify the appearance of status code 403 and unauthorized educator





