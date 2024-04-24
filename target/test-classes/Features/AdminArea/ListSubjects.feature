Feature:  Test all Scenarios Of List Subjects API


  Scenario: Validate that The List of Subjects returned Successfully
    Given   Getting subjects data from database
    When    Performing the API of List Subjects with Parameters
    Then    Subject is returned successfully


  Scenario: Validate that the List of Subjects not returned with country, stage or grade not exist
    Given   Getting subjects data from database
    When    Performing The API of List Subjects with country, stage and grade not exist
    Then    I should see Status code 404 with error and no subjects found


  Scenario: Validate that total number of active subjects returned successfully
    Given   Getting subjects count from database
    When    Performing the API of List Subjects to list All subjects
    Then    Total number of subjects is returned successfully

