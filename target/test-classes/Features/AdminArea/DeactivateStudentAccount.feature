Feature:  Test All Scenarios Of Deactivate Student API

  Scenario:  Validate That Response code is 200 and body returns with success message After enter valid student_id
    Given  Admin Send Valid Student Id In Request Parameter
    When   Performing The API Of Deactivate Student
    Then   Response code of Deactivate account API is 200 and body returns with success message

  Scenario: Validate response code is 400 and body returns with error message after leave student id empty
    Given Admin leave StudentId Empty in request parameters
    When  Performing The API Of Deactivate Student
    Then  Response code of Deactivate account API is 400 and body returns with error message

  Scenario: Validate response code is 403 and body returns with error message after enter invalid token
    Given Admin Send Valid Student Id In Request Parameter
    When  Performing The API Of Deactivate Student With Invalid Token
    Then  Response code of Deactivate account API is 403 and body returns with error message

  Scenario: Validate response code is 404 and body returns with error message after enter Student Id That Doesn't Exist
    Given Admin Enter StudentId That Doesn't Exist
    When  Performing The API Of Deactivate Student
    Then  Response code of Deactivate account API is 404 and body returns with error message