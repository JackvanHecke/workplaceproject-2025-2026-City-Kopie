Feature: Stakeholders management

  Background:
    Given I am logged in as a client
    And I open the stakeholders page

  Scenario: Stakeholders list loads correctly
    Then I should see the stakeholders heading
    And the stakeholders backend should have been called
    And I should see either a stakeholders table or an empty state

  Scenario: I can create a new stakeholder
    When I click the Add Stakeholder button
    And I fill in the stakeholder form with valid data
    And I submit the stakeholder form
    Then the create stakeholder backend call should have been made
    And I should see the new stakeholder in the table

  Scenario: I can delete a stakeholder
    Given at least one stakeholder exists
    When I delete the first stakeholder
    Then the delete stakeholder backend call should have been made
