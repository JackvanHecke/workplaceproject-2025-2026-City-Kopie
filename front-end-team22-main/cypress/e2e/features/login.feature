Feature: Login
  As a user
  I want to log in
  So that I can access the application

  Scenario: Successful login
    Given I visit the login page
    When I enter valid credentials and submit
    Then I should be redirected to the homepage
    And the token cookie should be set

  Scenario: Login with empty fields
    Given I visit the login page
    When I submit the form with empty fields
    Then I should see "Please fill out all fields"

  Scenario: Login with unknown user
    Given I visit the login page
    When I enter an unknown user and submit
    Then I should see "Unknown User"
