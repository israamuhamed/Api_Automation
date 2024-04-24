Feature:  Test All Scenarios Of ResetSession API

  Scenario: Validate that response code is 200 and body returns with success message after execute request successfully
    Given  User Send Valid SessionId And Body To The API
    When   Performing The API of Reset Session
    Then   Response code of reset Session is 200 and body returns with success message and session dates is changed successfully

  Scenario: Validate that response code is 400 and body returns with error message after enter invalid sessionId in request parameter
    Given   User Send Invalid Session Id And Valid Body To The API
    When    Performing The API of Reset Session
    Then    Response code of reset session is 400 and body returns with error message

  Scenario: Validate that response code is 403 and body returns with error message after send invalid token to the API
      Given User Send Valid SessionId And Body To The API
      When  Performing The API of Reset Session With Invalid Token
      Then  Response code of reset session is 403 and body returns with error message

  Scenario: Validate that response code is 404 and body returns with error message after enter invalid sessionId
      Given User Send SessionId that doesn't exist And Valid Body To The API
      When  Performing The API of Reset Session
      Then  Response code of reset session is 404 and body returns with error message