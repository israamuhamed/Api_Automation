Feature:  Testing All Scenarios For Mark Learning Record API

  Scenario: Verify Sending Valid Data To Mark Learning Record As Deleted Request
    Given   User Send Valid Data To MarkLearningRecordAsDeleted API
    When    Performing The API Of Mark Learning Record As Deleted API
    Then    Response Status Code Is 200 And Body Contains Learning Record Is Deleted Successfully

  Scenario: Verify Sending Invalid StudentId
    Given  User Send Invalid Student Id To Mark Learning Record Request
    When   Performing The API Of Mark Learning Record As Deleted API
    Then   Status Code Of Mark Request Is 403 And Body Have Error Message

  Scenario: Verify Sending Archived ClassId
    Given   User Send Archived ClassId To Mark Learning Record As Deleted API
    When    Performing The API Of Mark Learning Record As Deleted API
    Then    Response Code is 404 And Body Have Class Not Found Message

  Scenario: Verify Sending Deleted Learning Record
    Given   User Send Valid Data To MarkLearningRecordAsDeleted API
    When    Performing The API Of Mark Learning Record As Deleted API
    And     Performing The API Of Mark Learning Record As Deleted API
    Then    Response Code Is 404 And Body Have Learning Record Is Deleted Message
