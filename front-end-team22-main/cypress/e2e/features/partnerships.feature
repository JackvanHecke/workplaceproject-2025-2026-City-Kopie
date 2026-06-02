Feature: Partnerships map and stakeholders

  Background:
    Given I am logged in as a client
    And I open the partnerships page

  Scenario: Partnerships page loads map and stakeholders
    Then I should see the partnerships heading
    And the stakeholders backend should have been called for partnerships
    And I should see the partnership map
    And I should see the stakeholders section under the map

  Scenario: Toggling a partnership bubble updates the stakeholder
    Given stakeholders are stubbed for the partnerships map
    When I click the first partnership bubble
    Then the update stakeholder backend call should have been made
    And the partnerships progress label should update
