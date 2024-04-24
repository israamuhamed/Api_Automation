Feature: Test scenarios of the api Create Grade

  Scenario: Verify create new Grade successfully
    Given   Getting grade data from database
    And     Performing the Api of Create Grade With valid data
    Then    verify the appearance of status code 201 and grade created successfully

  Scenario: Verify create Grade with grade title already exist
    Given   Getting grade data from database
    And     Performing the Api of Create Grade With grade title already exist
    Then    verify the appearance of status code 400 and grade title must be unique per stage

  Scenario: Verify create Grade with grade url text already exist in same stage
    Given   Getting grade data from database
    And     Performing the Api of Create Grade With grade url text already exist in same stage
    Then    verify the appearance of status code 400 and grade url text must be unique per stage

  Scenario: Verify create Grade with grade order already exist in same stage
    Given   Getting grade data from database
    And     Performing the Api of Create Grade With grade order already exist in same stage
    Then    verify the appearance of status code 400 and grade order must be unique per stage

  Scenario: Verify create new Grade with invalid body
    Given   Performing the Api of Create Grade With invalid parameter
    Then    verify the appearance status code 400 and error message invalid request

  Scenario: Verify create new Grade with unauthorized data
    Given   Performing the Api of Create Grade With unauthorized data
    Then    verify the appearance status code 403 and error message unauthorized

