Feature: Test all scenarios of the api of unlock session
#
#  Scenario: Verify user enrolled into fully paid class successfully
#    Given   User enrolled into fully paid class successfully
#    When    Performing the Api of pay_for_full_class
#    And     Get class data from database
#    Then    I verify the appearance of status code 200 and Full class payment successful.

  Scenario: Verify user already purchased for class
    Given   User enrolled into fully paid class successfully
    When    Performing the Api of pay_for_full_class
    And     Performing the Api of pay_for_full_class
    Then    I verify the appearance of status code 400 and class already purchased

  Scenario: Verify unauthorized user can't enroll into fully paid class
    Given   User Send unauthorized user id
    When    Performing the Api of pay_for_full_class
    Then    The Response Should Contain Status Code 403 And Error Message Unauthorized

  Scenario: Verify user with Insufficient balance can't enroll into full paid class
    Given   student's wallet does not have sufficient funds for full class
    When    Performing the Api of pay_for_full_class
    Then    The Response Should Contain Status Code 400 And Error Message Insufficient balance for full class

  Scenario: check user can't enroll in class that not available or not listed
    Given   user try to enroll in class that not available or Archived
    When    Performing the Api of pay_for_full_class
    Then    The Response Should Contain Status Code 404 And Error Message Class not available.

  Scenario: check user can't enroll in class reached full capacity
    Given   user try to enroll in class have full capacity
    When    Performing the Api of pay_for_full_class
    Then    The Response Should Contain Status Code 400 And Error Message This class has reached full capacity.

