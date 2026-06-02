Feature: Coaches table

  # Scenarios assume you are logged in as a client (cy.loginAsClient or global Before hook)
  Background:
    Given I am logged in as a client
    And I open the app root

  Scenario: The coaches rows are displayed on the table
    When I find the coaches table
    Then I should see at least one coach row

  Scenario: Once I click refresh new coaches are loaded
    Given the server returns an initial coaches list
    When I find the coaches table
    And I click the refresh button
    Then the table should update with the new coaches list

  Scenario: When I click on an offer the modal is loaded
    When I find the coaches table
    And I open the first offer details
    Then the offer details modal should be visible
    And I should see the offer's type in the modal

  @skip   # this test is known to be flaky / not implemented on backend yet; remove @skip when fixed
  Scenario: When I edit a coach the changes are saved (expected to fail until save is implemented)
    When I find the coaches table
    And I open the edit modal for the first coach
    And I change the coach name to "Edited Coach Name" and save
    Then I should see "Edited Coach Name" in the table
