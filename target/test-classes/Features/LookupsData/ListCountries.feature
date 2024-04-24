Feature: Test all scenarios of the api of List Countries

  Scenario: Verify List Countries returned successfully
    Given   Performing the Api of List Countries
    And     Getting Countries List from database
    Then    I verify the appearance of status code 200 and countries returned successfully


