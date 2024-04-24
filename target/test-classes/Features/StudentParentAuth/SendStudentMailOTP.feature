Feature: Test all scenarios of the api send student OTP

  Scenario: Verify sending student OTP successfully
    Given   Performing the Api of Send Student OTP with valid data
    Then    I verify the appearance of status code 200 and OTP sent to student mail

#  Scenario: Verify sending student OTP with rate exceeds
#    Given   Performing the Api of Send Student OTP with valid data
#    Then    I verify the appearance of status code 429 and rate exceeds

  Scenario: Verify sending student OTP with Invalid student mail
    Given   Performing the Api of Send Student OTP with Invalid email
    Then    I verify the appearance of status code 400 and Invalid student email format

  Scenario: Verify sending student OTP with Invalid language
    Given   Performing the Api of Send Send OTP with Invalid language
    Then    I verify the appearance of status code 400 and Invalid Language

  Scenario: Verify sending student OTP with missing email
    Given   Performing the Api of Send Student OTP with missing email input
    Then    I verify the appearance of status code 400 and Invalid student email format
