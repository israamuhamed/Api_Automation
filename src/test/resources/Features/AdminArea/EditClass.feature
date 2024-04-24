Feature:  Testing All Scenarios Of Edit Class API

  Scenario: Verify That Response Code is 200 and  Class Data and Educators Updated Successfully After Execute The Request
    Given  User Send Valid Data To Edit Class API
    When   Performing The API Of Edit Class
    Then   Response Code of Edit Class Is 200 And Class Is Updated Successfully
    And    The Data Of Educator Of The Class Is Updated in DataBase

  Scenario: Validate that response code is 403 and body returns with error message after send invalid token to the Edit Class API
    Given User Send Valid Data To Edit Class API
    When  Performing The API of EditClass With Invalid Token
    Then  Response code of EditClass is 403 and body returns with error message

    Scenario: Verify That Response code is 400 after  leave class_id parameter empty
    Given User Send Valid Body And Empty ClassId In Path Parameters
    When  Performing The API Of Edit Class
    Then  Response Code of Edit Class Is 400 And Body Returns With Error Message

  Scenario:  Verify That Response code is 404 after enter educatorId That Doesn't Exist
    Given  User Send Educator That Doesn't Exist to Edit Class_API
    When   Performing The API Of Edit Class With EducatorId That Doesn't Exist
    Then   Response Code of Edit Class Is 404 And Body Returns With Error Message That Educator Doesn't Exist

  Scenario:  Verify That Response code is 404 after enter Class_Id That Doesn't Exist
    Given  User Send Valid Body And Class Id That Doesn't Exist In Path Parameters
    When   Performing The API Of Edit Class
    Then   Response Code of Edit Class Is 404 And Body Returns With Error Message That Class Doesn't Exist