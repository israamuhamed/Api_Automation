Feature:  Test All Scenarios Of GetEducationalResourcesOfSession API

  Scenario: Verify That EducationalResources Of Session Return After Enter Valid Parameters
    Given   User Send Valid Parameters To The Request
    When    Performing The Api Of GetEducationalResources
    And     get educational resource data of session from database
    Then    The Response Should Contain Status Code 200 And The Educational Resources Of The Session

  Scenario: Verify Sending Invalid UserId
    Given   User Send Invalid UserId In The Request
    When    Performing The Api Of GetEducationalResources
    Then    The Response Should Contain Status Code 403 And Error Message

  Scenario: Verify Sending SessionId That Contains No EducationalResources
    Given   User Send SessionId That Doesn't Have Educational Resources
    When    Performing The Api Of GetEducationalResources
    Then    The Response Should Contains Status Code 404 And Message That No Educational resources Found