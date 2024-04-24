Feature: Test all scenarios of the api Create Stage


  Scenario: Verify create new stage successfully
    Given   Getting data of a created country from database
    When    User send valid data to Create Stage API
    Then    I verify the appearance of status code 201 and Stage created successfully


  Scenario: Verify create new stage with invalid body
    Given   Getting data of a created country from database
    When    Performing the Api of Create Stage With invalid body
    Then    I verify the appearance of status code 400 and body data is incorrect


  Scenario: Verify create new stage using non existing country id
    Given   Performing the Api of Create Stage With invalid country id
    Then    I verify the appearance of status code 400 and country not exist


  Scenario: Verify create new stage with url already exist
    Given   Getting data of a created stage from database
    When    Performing the Api of Create Stage With existing stage url
    Then    I verify the appearance of status code 400 and stage url must be unique


  Scenario: Verify create new stage with order already exist
    Given   Getting data of a created stage from database
    When    Performing the Api of Create Stage With existing stage order
    Then    I verify the appearance of status code 400 and stage order must be unique


  Scenario: Verify create new stage with invalid token
    Given   Performing the Api of Create Stage with invalid token
    Then    I verify the appearance of status code 403 and user is unauthorized
