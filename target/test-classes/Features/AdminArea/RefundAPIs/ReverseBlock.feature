Feature: Test all scenarios of the api of Reverse Block

  Scenario: Verify user can't refund partial block
    Given   user pay for blocks in block payment class
    And     Refund partial block of total paid blocks
    Then    I verify the appearance of status code 403 and Cannot return partial reverse

  Scenario: Verify user can refund the total blocks that bought
    Given   user pay for blocks in block payment class
    And     Refund all block of total paid blocks
    Then    I verify the appearance of status code 200 and total blocks refunded successfully
    And     Verify that the enrollment of student canceled successfully and reverse seats decreased
    And     Verify that the student wallet transactions updated successfully

  Scenario: Reverse blocks of class not enrolled in it
    Given   user pay for blocks in block payment class
    And     Refund all block of not enrolled class
    Then    I verify the appearance of status code 403 and not enrolled in the class

  Scenario: Reverse blocks of class with unauthorized student
    Given   user pay for blocks in block payment class
    And     Refund all block with unauthorized student
    Then    I verify the appearance of status code 403 and student in not enrolled in the class

