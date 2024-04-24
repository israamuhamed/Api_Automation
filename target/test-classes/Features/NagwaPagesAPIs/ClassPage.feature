Feature:  Testing All Scenarios Of ClassPage API

  Scenario: Validate That Class Of Query Param  Returns After Enter Valid Data In The Request
    Given  User Send Valid ClassId To The ClassPage API
    Then   Class Of ClassPage API Return In Response Body

  Scenario: Validate that the Class and Student Info returns in response body after enter studentId
    Given  User send valid ClassId and StudentId in query params of the request
    Then   Class Of ClassPage API Return In Response Body

  Scenario: Validate that response code is 400 after execute request with missing data
    Given  User execute request Class Page with missing query params
    Then   Response code of Class Page is 400 and body returns with error message

  Scenario: Validate that response code is 404 after enter invalid value in query params
    Given  User enter invalid value in query params Of Class Page API
    Then   Response code of Class Page is 404 and body returns with error message that class not found
