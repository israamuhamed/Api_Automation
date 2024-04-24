Feature: Test all scenarios of the api of submit session feedback

    Scenario: Verify sending session feedback with valid score
        Given   User Send feedback for session
        When    Performing the Api of submit session feedback with valid score
        Then    The Response should contains status code 200 and message Feedback successfully submitted

    Scenario: Verify sending session feedback with Invalid score
        Given   User Send feedback for session
        When    Performing the Api of submit session feedback with invalid score
        Then    The Response should contains status code 400 and message Invalid feedback score

    Scenario: Verify sending session feedback with Invalid studentId
        Given   User Send Invalid StudentId to submit feedback
        When    Performing the Api of submit session feedback with valid score
        Then    The Response submit feedback  with invalid user Should Contain Status Code 403 And Error Message Unauthorized

    Scenario: Verify sending session feedback with kicked out student from session
        Given   User Send studentId kicked out from session to submit feedback
        When    Performing the Api of submit session feedback with valid score
        Then    The Response of invalid user Should Contain Status Code 403 And Error Message Unauthorized access

    Scenario: Verify sending session feedback with not participate student into session
        Given   User Send studentId not participate into session to submit feedback
        When    Performing the Api of submit session feedback with valid score
        Then    The Response of not participate student Should Contain Status Code 403 And Error Message Unauthorized access