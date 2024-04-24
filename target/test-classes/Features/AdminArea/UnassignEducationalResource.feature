Feature: All Scenarios Of UnAssignEducationalResource API

  Scenario:  Validate that educationalResource Unassigned Successfully after execute the request successfully
  Given  User Send Valid SessionId and EducationalResourceId
   When   Performing The API of UnAssignEducationalResource
   Then   Response code is 200 and body returns with success message

 Scenario:  Validate that educationalResource UnAssigned Successfully after enter list of sessions contains session that educationalResource isn't assigned to
   Given  User Send List Of Sessions contain session that educational resource doesn't assigned to
    When   Performing The API of UnAssignEducationalResource With Body Contains Invalid SessionId
    Then   Response code is 207 and body returns with success message and list that educational resource isn't assigned to

  Scenario: Validate response code and body after enter invalid token
    Given   User Send Valid SessionId and EducationalResourceId
    When    Performing The API of UnAssignEducationalResource With Invalid Token
    Then    Response Code Of UnAssignEducationalResource Is 403 And Body Returns With Error Message

  Scenario: Validate response code and body after enter invalid request body
     Given  User Send Invalid Body To Request With EducationalResource Isn't Assigned To Session
     When   Performing The API Of UnAssignEducationalResource With Invalid Body
     Then   Response code Of UnAssignEducationalResource is 400 and Body Returns With Error Message