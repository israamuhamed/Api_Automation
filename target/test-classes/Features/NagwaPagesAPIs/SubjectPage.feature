Feature:  Testing All Scenarios Of SubjectPage API

 Scenario: Validate That All Subjects Related To The Country Returns After Enter Valid Country Iso Code
   Given  User Send Valid Country Iso Code To The Request
   Then   All Subjects Of The Country Return In Response Body

 Scenario:  Validate that the subject and country and class returns in response body after enter studentId
    Given    User send validId and data in query params of the request
    Then     All Subjects Of The Country Return In Response Body

 Scenario:  Validate that response code is 400 after execute request with missing data
    Given   User execute request with missing query params
    Then    Response code is 400 and body returns with error message

  Scenario:  Validate that response code is 404 after enter invalid value in query params
    Given   User enter invalid value in query params
    Then    Response code of Subject Page is 404 and body returns with error message that no subjects found
