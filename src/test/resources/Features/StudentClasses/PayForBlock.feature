Feature: Testing All Scenarios For Pay For Block API

  Scenario: Validate that student can buy block successfully
   Given   User Enter Valid ClassId And Valid Blocks Value In Right Sequential
   When    Performing The API Of PayForBlock
   Then    Response Code Of PayForBlock Returns With 200 And Success Message And The Blocks That Student Have Bought

  Scenario: Validate that student cant buy blocks that already bought
    Given   User Enter Valid ClassId And Valid Blocks Value In Right Sequential
    And     Performing The API Of PayForBlock
    When    Performing The API Of PayForBlock API With Blocks That Already Bought
    Then    Response code of PayForBlock is 400 and body returns with error message

  Scenario: Validate that student cant buy blocks in class that doesn't allow PayForBlock option
    Given  User Send ClassId That Doesn't Allow PayForBlock Option
    When   Performing The API Of PayForBlock
    Then   Response Code Of PayForBlock And Body Returns With Message That Class Doesn't Allow PayForBlock

  Scenario: Verify Sending Invalid Token
    Given User Enter Valid ClassId And Valid Blocks Value In Right Sequential
    When  Performing The API Of Pay For Block With Invalid Token
    Then  Response Code Of PayForBlock Is 403 And Body Returns With Error Message

  Scenario: Verify Send Expensive Blocks To The Request
    Given  User Send Valid ClassId And Expensive Blocks To The Request
    When   Performing The API Of PayForBlock
    Then   Response Code Of PayForBlock Is 422 And Body Returns With Error Message contains insufficient wallet balance
