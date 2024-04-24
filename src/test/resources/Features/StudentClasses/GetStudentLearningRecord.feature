Feature:  All Scenarios For Get Student Learning Records API

  Scenario: Verify That Student Learning Records Return Successfully
    Given   User Send Valid Parameters To Get Student Learning Records API
    When    Performing The API Of Get Student Learning Record
    Then    Response Status Code Is 200 And Response Body Contains Student Learning Record

  Scenario: Verify Sending Invalid User Id
    Given   User Send Invalid UserId To Get Student Learning Record API
    When    Performing The API Of Get Student Learning Record
    Then    Response Status Code Is 403 And Body Returns Withe Error Message With No Learning Records

  Scenario: Verify Sending Invalid Session Id
    Given   User Send Invalid SessionId To Get Student Learning Record API
    When    Performing The API Of Get Student Learning Record
    Then    Response Status Code Is 404 And Body Contains Have No Access To Session Error Message

  Scenario: Verify Sending Archived Class Id
    Given   User Send Archived ClassId To Get Student Learning Record API
    When    Performing The API Of Get Student Learning Record
    Then    Response Status Code Is 404 And Body Contains Class Not Found

  Scenario: Verify Deleted Learning Record
    Given   User send parameters of deleted Learning Record
    When    Performing The API Of Get Student Learning Record
    Then    Response Status Code Is 404 And Body Have Deleted Learning Record Message
