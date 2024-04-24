Feature:  Test all Scenarios Of List Stages API

  Scenario:  Validate that body returns with valid data after execute the request successfully
    Given User Create New Session
    When Performing The API of ListSessions
    Then The Session should return in response body

  Scenario:  Validate that response code returns with 401 And body with Error Message After Enter Invalid Token
    Given User Create New Session
    When  Performing the API of ListSessions With Invalid Token
    Then  Response Code of ListSessions Is 403 And Body Returns With Error Message

  Scenario: Validate that response code is 400 and body returns with error message after enter valid data in request params
    Given User Send Invalid Session_id In Request Params
    When  Performing The API of ListSessions With Invalid Params
    Then  Response Code Of ListSessions Is 404 and body returns with error message

  Scenario:  Validate That Response Code Is 400 After Leave Session_Id Empty
    Given  User Perform The Api Of ListSessions With Empty Session_ID
    Then   Response Code Of ListSessions Is 400 and body Returns With Error Message