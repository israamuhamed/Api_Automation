Feature: Test All Scenarios Of Edit Session API

  Scenario: Validate that response code is 200 and session is updated successfully after enter valid data
    Given  Admin Send Valid SessionId And Body To EditSession API
    When   Performing The API of EditSession
    Then   Response code of EditSession is 200 and body returns with success message

  Scenario: Validate that response code is 400 and body returns with error message after enter invalid sessionId in request parameter
    Given   User Send Invalid Session Id And Valid Body To The EditSession API
    When    Performing The API of EditSession
    Then    Response code of EditSession is 400 and body returns with error message

  Scenario: Validate that response code is 403 and body returns with error message after send invalid token to the API
    Given Admin Send Valid SessionId And Body To EditSession API
    When  Performing The API of Edit Session With Invalid Token
    Then  Response code of EditSession is 403 and body returns with error message

  Scenario: Validate that response code is 404 and body returns with error message after enter sessionId That Doesn't Exist
    Given User Send SessionId that doesn't exist And Valid Body To EditSession API
    When  Performing The API of EditSession
    Then  Response code of EditSession is 404 and body returns with error message That Session Doesnt Exist

  Scenario: Validate that response code is 404 and body returns with error message after enter Educator That Doesn't Exist
    Given User Send Educator that doesn't exist In Request Body To EditSession API
    When  Performing The API of EditSession With Eductor Id That Doesn't Exist In Request Body
    Then  Response code of EditSession is 404 and body returns with error message That Educator Doesn't Exist