Feature: Test all scenarios of the api Get Educator Session Report

  Scenario: Verify Get Educator Session Report Successfully
    Given   User Create Classes and Session for Educator to Get Session Report
    When    Performing the Api of Get educator session report
    Then    I verify the appearance of status code 200 and report data returned successfully
    # need to create the api of enroll student into session
