Feature:  Test all Scenarios Of List Stages API

  Scenario: Validate that Stages Returned Successfully with valid data
      Given Get Stage Data From Database
      When  Performing The API of List Stages
      Then  The Stages Returned Successfully

  Scenario: Validate list stages with stage not exist
    Given   Get Stage Data From Database
    When    Performing The API of List Stages with stage not exist
    Then    I should see Status code 404 with error message stage not exist

  Scenario: Validate all Stages returned successfully
    Given   Get Stages count From Database
    When    Performing The API of List Stages To List All Stages
    Then    validate the total number of stages returned successfully

