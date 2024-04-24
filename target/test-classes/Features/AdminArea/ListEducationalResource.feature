Feature:  Test all Scenarios Of ListEducationalResourceAPIs

  Scenario:  Validate that body returns with valid data after execute the request successfully
   Given User Create New Session and EducationalResource
   When Performing The API of ListEducationalResource
   Then All The EducationalResources and its session should return in response body

  Scenario:  Validate that response code returns with 401 And body with Error Message After Enter Invalid Token
   Given User Create New Session and EducationalResource
   When  Performing The API Of List Educational Resource With Invalid Token
   Then  Response Code Is 403 And Body Returns With Error Message

    Scenario: Validate that response code is 200 and body returns with EmptyList after enter Invalid data in request params
      Given User Send Invalid data In Request Params
      When  Performing The API of ListEducationalResource With Invalid Params
      Then  Response Code Is 200 and body returns with EmptyList