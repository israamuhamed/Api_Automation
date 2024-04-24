Feature: Test all scenarios of the api Download educational resources

  Scenario: Verify Download Educational resources successfully
    Given   User Create Sessions and Educational Resources for Educator
    When    Performing the Api Download Educator Resources
    And     Get resources data from database
    Then    I verify the appearance of status code 200 and Educator Resources Downloaded

  Scenario: Verify Download Educational resources with invalid session id
    Given   User Create Educator without assigning any classes
    When    Performing the Api of download resources with invalid session id
    Then    I verify the appearance of status code 404 and session id is not found

  Scenario: Verify Download Educational resources with invalid token
    Given   Performing the Api of download educational resources with unauthorized educator
    Then    I verify the appearance of status code 403 and the educator is unauthorized




