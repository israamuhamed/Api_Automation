Feature:  Test all Scenarios Of List Grades API

  Scenario: Validate that The List of Grades Returned Successfully
    Given   Get Grades Data From Database
    When    Performing The API of List Grades With Params
    Then    The Grade Returned Successfully

  Scenario: Validate that The List of Grades not Returned with country not exist
    Given   Get Grades Data From Database
    When    Performing The API of List Grades with country not exist
    Then    I should see Status code 404 with error message country not exist

  Scenario: Validate that total numbers of active grades returned successfully
    Given   Get Grades count From Database
    When    Performing The API of List Grades To List All Grades
    Then    validate the total number of active grades returned successfully

