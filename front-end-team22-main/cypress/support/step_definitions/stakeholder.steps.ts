import { Given, When, Then } from "@badeball/cypress-cucumber-preprocessor";

function setupStakeholderIntercepts() {
  cy.intercept("GET", "**/api/stakeholders").as("getStakeholders");
  cy.intercept("POST", "**/api/stakeholders").as("createStakeholder");
  cy.intercept("PUT", "**/api/stakeholders/*").as("updateStakeholder");
  cy.intercept("DELETE", "**/api/stakeholders/*").as("deleteStakeholder");
}

Given("I open the stakeholders page", () => {
  setupStakeholderIntercepts();
  cy.visit("/stakeholders");
  cy.get('[data-cy="stakeholders-page"]').should("exist");
  cy.wait("@getStakeholders");
});

Then("I should see the stakeholders heading", () => {
  cy.get('[data-cy="stakeholders-heading"]').should("be.visible");
});

Then("the stakeholders backend should have been called", () => {
  cy.wait("@getStakeholders");
});

Then(
  "I should see either a stakeholders table or an empty state",
  () => {
    cy.get("body").then(($body) => {
      const hasTable = $body.find('[data-cy="stakeholder-table"]').length > 0;
      const hasEmpty = $body.find('[data-cy="stakeholder-empty-message"]').length > 0;
      expect(hasTable || hasEmpty).to.be.true;
    });
  }
);

When("I click the Add Stakeholder button", () => {
  cy.get('[data-cy="stakeholders-add-button"]').click();
  cy.get('[data-cy="stakeholder-form"]').should("exist");
});

When("I fill in the stakeholder form with valid data", () => {
  const timestamp = Date.now();
  const org = `Cypress Org ${timestamp}`;

  cy.wrap(org).as("stakeholderOrgName");

  cy.get('[data-cy="stakeholder-organisation-input"]')
    .clear()
    .type(org);
  cy.get('[data-cy="stakeholder-contact-input"]')
    .clear()
    .type("Test Contact");
  cy.get('[data-cy="stakeholder-email-input"]')
    .clear()
    .type(`test-${timestamp}@example.com`);
  cy.get('[data-cy="stakeholder-phone-input"]')
    .clear()
    .type("0123456789");
  cy.get('[data-cy="stakeholder-role-input"]')
    .clear()
    .type("Test Role");
});

When("I submit the stakeholder form", () => {
  cy.get('[data-cy="stakeholder-save-button"]').click();
});

Then(
  "the create stakeholder backend call should have been made",
  () => {
    cy.wait("@createStakeholder");
    cy.wait("@getStakeholders"); // refresh
  }
);

Then("I should see the new stakeholder in the table", () => {
  cy.get("@stakeholderOrgName").then((org) => {
    cy.get('[data-cy="stakeholder-table"] tbody')
      .contains("td", String(org))
      .should("exist");
  });
});

Given("at least one stakeholder exists", () => {
  // If there is none, create one quickly
  cy.get("body").then(($body) => {
    const hasTable = $body.find('[data-cy="stakeholder-table"]').length > 0;
    if (!hasTable) {
      cy.log("No stakeholder table yet, creating initial stakeholder via UI");
      cy.get('[data-cy="stakeholders-add-button"]').click();
      cy.get('[data-cy="stakeholder-form"]').should("exist");
      cy.get('[data-cy="stakeholder-organisation-input"]').type(
        "Initial Cypress Stakeholder"
      );
      cy.get('[data-cy="stakeholder-contact-input"]').type("Initial Contact");
      cy.get('[data-cy="stakeholder-email-input"]').type("initial@example.com");
      cy.get('[data-cy="stakeholder-phone-input"]').type("0000");
      cy.get('[data-cy="stakeholder-role-input"]').type("Initial Role");
      cy.get('[data-cy="stakeholder-save-button"]').click();
      cy.wait("@createStakeholder");
      cy.wait("@getStakeholders");
      return;
    }

    cy.get('[data-cy="stakeholder-table"] tbody tr').then(($rows) => {
      if ($rows.length === 0) {
        cy.log("Stakeholder table has no rows, creating initial stakeholder");
        cy.get('[data-cy="stakeholders-add-button"]').click();
        cy.get('[data-cy="stakeholder-form"]').should("exist");
        cy.get('[data-cy="stakeholder-organisation-input"]').type(
          "Initial Cypress Stakeholder"
        );
        cy.get('[data-cy="stakeholder-contact-input"]').type("Initial Contact");
        cy.get('[data-cy="stakeholder-email-input"]').type(
          "initial@example.com"
        );
        cy.get('[data-cy="stakeholder-phone-input"]').type("0000");
        cy.get('[data-cy="stakeholder-role-input"]').type("Initial Role");
        cy.get('[data-cy="stakeholder-save-button"]').click();
        cy.wait("@createStakeholder");
        cy.wait("@getStakeholders");
      }
    });
  });
});

When("I delete the first stakeholder", () => {
  cy.on("window:confirm", () => true);
  cy.get('[data-cy="stakeholder-table"] tbody tr')
    .first()
    .within(() => {
      cy.get('button[data-cy^="stakeholder-delete-button-"]').click();
    });
});

Then("the delete stakeholder backend call should have been made", () => {
  cy.wait("@deleteStakeholder");
  cy.wait("@getStakeholders");
});
