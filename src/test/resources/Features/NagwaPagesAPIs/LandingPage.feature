Feature:  Testing All Scenarios Of LandingPage API

  Scenario: Validate That Data Of Values In Query Params Returns After Enter Valid Data In The Request
    Given  User Send CountryIso Code To Landing Page API
    Then   Stage and Grades Of The Data In Query Param In Landing Page API Return In Response Body

  Scenario: Validate that No Data Returns After Enter Invalid Data In Any Query Param
    Given  User send Invalid Country Code In query params of the request Landing Page
    Then   Response Code Of Landing Page Is 404 And Body Returns With Error Message

  Scenario: Validate that response code is 400 after execute request with missing data
    Given  User execute request Landing Page with missing query params
    Then   Response code of Landing Page is 400 and body returns with error message
