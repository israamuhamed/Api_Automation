Feature:  Test all Scenarios Of ListSessionsAPI

  Scenario:  Validate that body returns with valid data after execute the request successfully
    Given User Create New Educator
    When Performing The API of ListEducators
    Then The Educator should return in response body

  Scenario:  Validate that response code returns with 401 And body with Error Message After Enter Invalid Token
    Given User Create New Educator
    When  Performing the API of ListEducators With Invalid Token
    Then  Response Code of ListEducators Is 403 And Body Returns With Error Message

  Scenario: Validate that response code is 404 and body returns with error message after enter valid data in request params
    Given User Send Invalid Educator_id In Request Params
    When  Performing The API of ListEducators With Invalid Params
    Then  Response Code Of ListEducators Is 404 and body returns with error message

  Scenario:  Validate That Response Code Is 400 After Leave Educator_ID Empty
   Given  User Perform The Api Of ListEducators With Empty Educator_Id
   Then   Response Code Of ListEducators Is 400 and body Returns With Error Message