Feature: Test all scenarios of the api update educator profile

  Scenario: Verify updating data successfully of educator
    Given   User Send valid educator Id for update
    When    Performing the Api of Update Educator Profile with valid data
    Then    I verify the appearance of status code 200 and Educator data updated
#    And     validate data saved successfully into db

  Scenario: Verify updating data of educator with special char
    Given   User Send valid educator Id for update
    When    Performing the Api of Update Educator Profile with special char
    Then    I verify the appearance of status code 200 and Educator data updated

  Scenario: Verify updating data of educator with empty fields
    Given   User Send valid educator Id for update
    When    Performing the Api of Update Educator Profile with empty body
    Then    I verify the appearance of status code 400 and Invalid request message

  Scenario: Verify sending Invalid educator id
    Given   User Send Invalid educator Id for update
    When    Performing the Api of Update Educator Profile with invalid token
    Then    I verify the appearance of status code 400 and Educator Id is  incorrect

  Scenario: Verify sending unauthorized educator id
    Given   User Send valid educator Id for update
    When    performing the api of update with invalid token
    Then    I verify the appearance of status code 403 and Educator unauthorized

  Scenario: Verify sending not active educator id
    Given   User Send not active educator for update
    When    performing the api of update with notActive educator token
    Then    I verify the appearance of status code 404 and Educator Id is deactivated

  Scenario: Verify sending deleted educator id
    Given   User Send deleted educator for update
    When    performing the api of update with deleted educator token
    Then    I verify the appearance of status code 404 and Educator Id deleted

