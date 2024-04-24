Feature: Test all scenarios of the api List Educators Classes

  Scenario: Verify Educator classes data successfully
    Given   User Create Classes and Session for Educator to list classes for educator
    When    Performing the Api of list classes for educator
    And     Get Educator Classes and upcoming Session from database
    Then    I verify the appearance of status code 200 and Educator classes data returned

  Scenario: Verify List Educator Classes with invalid Educator id
    Given   Performing the Api of list classes for educator with educator not valid
    And     I verify the appearance of status code 400 and Educator ID is not correct

  Scenario: Verify List Educator classes with Educator id not exist
    Given   User Send EducatorId  not exist to list classes
    When    list classes for educator with educator not exist
    Then    I verify the appearance of status code 404 and educator is not exist





