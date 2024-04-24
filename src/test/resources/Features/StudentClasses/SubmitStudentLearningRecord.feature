Feature:  All Scenarios Of SubmitStudentLearningRecord API

  Scenario: Verify Sending Valid Data In Request
    Given User Send Valid Data To Submit Student Learning Record Request
    When  Performing The API Of Submit Student Learning Record
    Then  Response Status Code Is 200 And Body Have StudentLearning RecordId

  Scenario: Verify Sending Invalid UserId
    Given User Send Invalid UserId To SubmitStudentLearningRecord Request
    When Performing The API Of Submit Student Learning Record
    Then Response Status Code Of SubmitStudentLearningRecord Is 403 and Body Have Error Message

  Scenario: Verify Sending Invalid SessionId
    Given User Send Invalid SessionId To SubmitStudentLearningRecord Request
    When  Performing The API Of Submit Student Learning Record
    Then  Response Status Code Is 403 And Error Message Contains No Access To Session

  Scenario: Verify Sending Invalid Body
    Given User Send Valid Parameters And Empty Body
    When  Performing The API With Empty Body
    Then  Response Code Is 400 And Body Have Clear Error Message
