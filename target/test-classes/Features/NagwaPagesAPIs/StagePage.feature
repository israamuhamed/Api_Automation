Feature:  Testing All Scenarios Of StagePage API

  Scenario: Validate That Data Of Values In Query Params Returns After Enter Valid Data In The Request
    Given  User Send CountryIso Code And Data To Stage Page API
    Then   Stage Of The Data In Query Param In Stage Page  API Return In Response Body

  Scenario: Validate that No Data Returns After Enter Invalid Data In Any Query Param
    Given  User send Invalid Data In query params of the request Stage Page
    Then   Response Code Of Stage Page Is 404 And Body Returns With Error Message

  Scenario: Validate that response code is 400 after execute request with missing data
    Given  User execute request Stage Page with missing query params
    Then   Response code of Stage Page is 400 and body returns with error message
