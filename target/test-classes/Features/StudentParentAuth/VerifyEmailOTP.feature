Feature: Test all scenarios of the api verify student OTP

  Scenario: Verify student OTP with valid data
    When    Performing the Api of Verify Student OTP with valid data
    Then    I verify the appearance of status code 200 and student authenticated

  Scenario: Verify student OTP with invalid OTP
    When    Performing the Api of Verify Student OTP with Invalid OTP
    Then    I verify the appearance of status code 401 and Invalid student OTP

