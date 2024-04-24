Feature: Test all scenarios of the api verify educator OTP

  Scenario: Verify sending educator OTP successfully with valid data
    Given   Get Educator OTP and mail from database
    When    Performing the Api of Verify Educator OTP with valid data
    Then    I verify the appearance of status code 200 and user authenticated

  Scenario: Verify sending Invalid educator OTP
    Given   Get Educator OTP and mail from database
    When    Performing the Api of Verify Educator OTP with Invalid OTP
    Then    I verify the appearance of status code 401 and Invalid OTP