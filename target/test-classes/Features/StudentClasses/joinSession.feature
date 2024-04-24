  Feature: Test all scenarios of the api of joining session

    Scenario: Verify session joined successfully for valid user
      Given   User Send The Post Request Of join session
      When    Performing the Api of Joining Session
      Then    The Response should contains status code 200 and correct session id

    Scenario: Verify that student can't join session in class that he haven't enrolled in
      Given   User Send Valid StudentId And ClassId That He Haven't Enrolled In
      When    Performing the Api of Joining Session
      Then    The Response for join session Should Contain Status Code 403 And Error Message Unauthorized

    Scenario: Verify that student cant join session that doesn't exist
      Given   Student Join Session IS not Exist
      When    Performing the Api of Joining Session
      Then    The Response Should Contain Status Code 404 And Error Message That Session Doesn't Exist

    Scenario: Verify that student cant join session for class doesn't exist
      Given   User send class id that not exist
      When    Performing the Api of Joining Session
      Then    The Response Should Contain Status Code 404 And Error Message That Class Doesn't Exist

    Scenario: Verify That Deactivated/Deleted Student Cant Join Session
      Given   User Send InActive StudentId
      When    Performing the Api of Joining Session
      Then    The Response Should Contain Status Code 403 And Error Message Student Is Deactivated or Not Exist

    Scenario: Verify That Student Cant Join Ended Session
      Given   User Send Ended SessionId
      When    Performing the Api of Joining Session
      Then    The Response Should Contains Status Code 422 And Error Message Session Is Ended

    Scenario: Verify That Student Cant Join NotStarted Session
      Given   User Send NotStarted SessionId
      When    Performing the Api of Joining Session
      Then    The Response Should Contains Status Code 422 And Error Message Session Haven't Started

    Scenario: Verify That KickedOut Student Cant Join Session
      Given   User Send KickedOut StudentId
      When    Performing the Api of Joining Session
      Then    The Response Should Contains StatusCode 422 And Error Message Student Is KickedOut

    Scenario: Verify That Student Cant Join Session That Isn't Related To The Class Or The Student
      Given   User Send SessionId That Doesn't Related To Class Or Student
      When    Performing the Api of Joining Session
      Then    The Response Should Contains Status Code 422 And Error Message Session Isn't Related To Class Or Student

    Scenario: Verify That Student Cant Join Session That Doesn't Have AccessRight On And The Class Doesn't Allow PayPerSession
      Given   User Send ClassId That Doesn't Allow PayPerSession And SessionId That Doesn't Have AccessRight On
      When    Performing the Api of Joining Session
      Then    The Response Should Contains Status Code 422 And Error Message The Class Doesn't Allow PayPerSession

    Scenario: Verify That Student Cant Join Session If His Wallet Is InSufficient
      Given   User Send StudentId With InSufficient Balance
      When    Performing the Api of Joining Session
      Then    The Response Should Contains Status Code 422 And Error Message Student Wallet Is Insufficient
