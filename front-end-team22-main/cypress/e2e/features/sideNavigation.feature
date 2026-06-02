Feature: Side Navigation
  Verify the side navigation labels and navigation functionality

  Scenario: Side navigation displays correct labels
    Given I visit the home page
    Then the side navigation should have 8 items with correct labels

  Scenario Outline: Navigate to pages via side navigation
    Given I visit the home page
    When I click on the side navigation item "<label>"
    Then I should be navigated to "<url>"

    Examples:
      | label           | url               |
      | Infrastructuur  | /infrastructuur  |
      | Activiteiten    | /activiteiten    |
      | Communicatie    | /communicatie    |
      | Monitoring      | /monitoring      |
      | Onderhoud       | /onderhoud       |
      | Financiën       | /financien       |
      | Partnerships    | /partnerships    |
      | Instellingen    | /instellingen    |
