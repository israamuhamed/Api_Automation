Feature: Test all scenarios of the api Get educator profile

    Scenario: Verify getting data of valid educator Id
      Given   Getting the Educator Data From Database
      And     User Send valid educator Id
      When    Performing the Api of Get Educator Profile
      Then    I verify the appearance of status code 200 and Educator data returned

    Scenario: Verify sending Invalid educator id
      Given   User Send Invalid educator Id
      When    performing the api with invalid educator id
      Then    I verify the appearance of status code 400 and Educator Id not correct

    Scenario: Verify sending not active educator id
      Given   User Send not active educator
      When    performing the api with notActive educator token
      Then    I verify the appearance of status code 404 and Educator Id is not active

    Scenario: Verify sending deleted educator id
      Given   User Send deleted educator
      When    performing the api with deleted educator token
      Then    I verify the appearance of status code 404 and Educator Id is deleted

    Scenario: Verify sending unauthorized educator id
      Given   User Send unauthorized educator
      When    performing the api with invalid token
      Then    I verify the appearance of status code 403 and Educator is unauthorized