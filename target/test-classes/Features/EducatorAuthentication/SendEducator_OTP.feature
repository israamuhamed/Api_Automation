Feature: Test all scenarios of the api send educator OTP

  Scenario: Verify sending educator OTP successfully
    Given   Performing the Api of Send Educator OTP with valid data
    Then    I verify the appearance of status code 200 and OTP sent to email

#  Scenario: Verify sending educator OTP Too Many Times
#    Given   Performing the Api of Send Educator OTP with valid data
#    Then    I verify the appearance of status code 429 and Rate Limit Exceeds

  Scenario: Verify sending educator OTP with invalid email
    Given   Performing the Api of Send Educator OTP with Invalid email
    Then    I verify the appearance of status code 400 and Invalid email format

  Scenario: Verify sending educator OTP with invalid language
    Given   Performing the Api of Send Educator OTP with Invalid language
    Then    I verify the appearance of status code 400 and Invalid Invalid Language

  Scenario: Verify sending educator OTP with missing email input
    Given   Performing the Api of Send Educator OTP with missing email input
    Then    I verify the appearance of status code 400 and Invalid email format
