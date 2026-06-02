Feature: Communication management

  # Scenarios assume you are logged in (global Before hook does cy.login())
  # but we still keep an explicit step for readability.

  Background:
    Given I am logged in as a client
    And I open the communicatie page

  Scenario: The communicatie list page loads correctly
    Then I should see the communicatie list heading
    And the backend should have been called to load communications
    And I should see either a communicatie table or an empty state

  Scenario: I can create and then see a new communication message
    When I click the "Nieuw bericht" button
    And I fill in the communication form with a unique title
    And I submit the communication form
    Then the create communication backend call should have been made
    And I should be back on the communicatie list page
    And I should see the newly created communication in the table

  Scenario: I can create and then edit a communication message
    When I click the "Nieuw bericht" button
    And I fill in the communication form with a unique title
    And I submit the communication form
    And I open the newly created communication in the edit page
    And I change the title of the communication
    And I submit the communication form
    Then the update communication backend call should have been made
    And I should see the updated communication title in the table

  Scenario: I can open and close the communication detail modal from the list
    Given at least one communication exists
    When I open the first communication in the detail modal
    Then the communication detail modal should be visible
    And the detail modal should show a title and body
    When I close the communication detail modal
    Then the communication detail modal should not be visible anymore

  Scenario: I can navigate to edit from the detail modal
    Given at least one communication exists
    When I open the first communication in the detail modal
    And I click the edit button in the detail modal
    Then I should be on the communicatie edit page
    And the backend should have been called to load a single communication

  Scenario: I can use targeting, channels and recipients fields
    When I click the "Nieuw bericht" button
    And I stub recipient profiles with a few fake profiles
    And I fill in the communication form with a unique title
    And I toggle some channels and roles
    And I optionally add and remove a recipient
    Then the changes in the form fields should be reflected in the UI
    And the get recipient profiles backend call should have been made

  Scenario: Backend error when creating a communication shows an error message
    When I click the "Nieuw bericht" button
    And I stub the create communication request to fail with status 400
    And I fill in the communication form with a unique title
    And I submit the communication form
    Then I should see an error message for failed communication creation
