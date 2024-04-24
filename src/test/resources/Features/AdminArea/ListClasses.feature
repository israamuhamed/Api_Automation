Feature:  Test all Scenarios Of ListClassesAPI

  Scenario:  Validate that body returns with valid data after execute the request successfully
    Given User Create New Class
    When Performing The API of ListClasses
    Then The Class should return in response body

  Scenario:  Validate that response code returns with 401 And body with Error Message After Enter Invalid Token
    Given User Create New Class
    When  Performing the API of List Classes With Invalid Token
    Then  Response Code of ListClasses Is 403 And Body Returns With Error Message

  Scenario: Validate that response code is 400 and body returns with error message after enter valid data in request params
    Given User Send Invalid Class_Id In Request Params
    When  Performing The API of ListClasses With Invalid Params
    Then  Response Code Of ListClasses Is 400 and body returns with error message