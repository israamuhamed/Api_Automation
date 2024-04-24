Feature:  Test All Scenarios For Get Download URLs For Educational Resources

  Scenario: Verify That ResourceId And DownloadLink Return In Response Body After Enter Valid Parameters
    Given User Send Valid Parameters To GetDownloadURLs Request
    When  Performing The APi Of GetDownload URLs Of Educational Resources
    And   Get educational resources data from database
    Then  Response Status Code Is 200 And Response Body Contains EducationalResourceId And DownloadLink

  Scenario: Verify Sending Invalid UserId To GetDownLoadEducationalResources Request
    Given User Send Invalid UserId To GetDownloadEducationalResources Request
    When  Performing The APi Of GetDownload URLs Of Educational Resources
    Then  Response Status Code Is 403 And Response Body Contains Error Message And No Links Returns

  Scenario: Verify Sending SessionId That Student Doesn't Have Access To
    Given    User Send SessionId That Student Doesn't Hae Access To
    When     Performing The APi Of GetDownload URLs Of Educational Resources
    Then     Response Status Code Is 403 And Body Returns With Error Message That Student Have No Access