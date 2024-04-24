Feature: Test all scenarios of the api Get Educators Sessions

  Scenario: Verify Educator Sessions returned successfully
    Given   User Create Classes and Session for Educator to Get Sessions for educator
    When    Performing the Api of Get sessions for educator
    And     Get Educator's Sessions from database
    Then    I verify the appearance of status code 200 and Educator Sessions data returned successfully

  Scenario: Verify Get Educator Sessions with invalid session Id
    Given   User Create Classes only for Educator to Get Sessions
    When    Performing the Api of Get sessions for educator with invalid session
    Then    I verify the appearance of status code 404 and Session is not related to the class

  Scenario: Verify Get Educator Sessions with invalid class Id
    Given   User Create Educator without assigning any classes and get sessions for him
    When    Performing the Api of Get sessions for educator with invalid class
    Then    I verify the appearance of status code 404 and class is not found

  Scenario: Verify Get Educator Sessions with invalid token
    Given   Performing the Api of Get sessions for educator with unauthorized educator
    Then    I verify the appearance of status code 403 and unauthorized educatorId

