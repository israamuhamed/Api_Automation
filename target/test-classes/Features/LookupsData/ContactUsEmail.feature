Feature: Test all scenarios of the api of Send Contact us Email

  Scenario: Verify send contact us email successfully
    Given   Performing the Api of Sent Contact Us Email
    Then    I verify the appearance of status code 200 and message sent successfully

  Scenario: Verify send contact us email with invalid mail
    Given   Performing the Api of Sent Contact Us Email with invalid email
    Then    I verify the appearance of status code 400 and invalid email message

  Scenario: Verify send contact us email with invalid body
    Given   Performing the Api of Sent Contact Us Email with invalid body
    Then    I verify the appearance of status code 400 and invalid body message
