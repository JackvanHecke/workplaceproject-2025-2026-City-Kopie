Feature: Navigation
  As a user
  I want to navigate and see the header correctly
  So that I can use the app

  Background:
    Given I visit the homepage

  Scenario: Navigate to the login page
    When I click the login link in the header
    Then I should be on the login page

  Scenario: Header layout looks correct
    Then the header should have 5 links
    And the header should have 1 image
    And the nav should have 4 links
    And the first nav link should have text "Home"
    And the second nav link should have text "KlantenDashboard"
    And the third nav link should have text "Stakeholders"
    And the fourth nav link should have text "Devices"
    And the last header link should have text "Login"
