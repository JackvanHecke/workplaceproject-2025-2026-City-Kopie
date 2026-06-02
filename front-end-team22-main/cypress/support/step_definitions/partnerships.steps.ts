import { Given, When, Then } from "@badeball/cypress-cucumber-preprocessor";

function setupPartnershipStakeholderIntercepts() {
  cy.intercept("GET", "**/api/stakeholders").as("getStakeholders");
  cy.intercept("PUT", "**/api/stakeholders/*").as("updateStakeholder");
  cy.intercept("POST", "**/api/stakeholders").as("createStakeholder");
  cy.intercept("DELETE", "**/api/stakeholders/*").as("deleteStakeholder");
}

Given("I open the partnerships page", () => {
  setupPartnershipStakeholderIntercepts();
  cy.visit("/partnerships");
  cy.get('[data-cy="partnerships-page"]').should("exist");
  cy.wait("@getStakeholders");
});

Then("I should see the partnerships heading", () => {
  cy.get('[data-cy="partnerships-page-heading"]').should("be.visible");
});

Then(
  "the stakeholders backend should have been called for partnerships",
  () => {
    cy.wait("@getStakeholders");
  }
);

Then("I should see the partnership map", () => {
  cy.get('[data-cy="partnership-map-svg"]').should("be.visible");
});

Then(
  "I should see the stakeholders section under the map",
  () => {
    cy.get('[data-cy="partnerships-stakeholders-section"]').should("exist");
    cy.get('[data-cy="partnerships-stakeholders-heading"]').should(
      "be.visible"
    );
  }
);

Given("stakeholders are stubbed for the partnerships map", () => {
  const stubbed = [
    {
      id: 1,
      organisation: "Stub Org 1",
      contactPerson: "Person 1",
      email: "s1@example.com",
      phoneNumber: "0001",
      role: "Role 1",
      partnershipDecided: false,
    },
    {
      id: 2,
      organisation: "Stub Org 2",
      contactPerson: "Person 2",
      email: "s2@example.com",
      phoneNumber: "0002",
      role: "Role 2",
      partnershipDecided: true,
    },
  ];

  cy.intercept("GET", "**/api/stakeholders", {
    statusCode: 200,
    body: stubbed,
  }).as("getStakeholdersStubbed");

  cy.visit("/partnerships");
  cy.get('[data-cy="partnerships-page"]').should("exist");
  cy.wait("@getStakeholdersStubbed");
});

When("I click the first partnership bubble", () => {
  // First stakeholder has id 1 in stubbed data → bubble id 1
  cy.get('[data-cy="partnership-bubble-1"]').click();
});

Then("the update stakeholder backend call should have been made", () => {
  cy.wait("@updateStakeholder").its("request.body").should((body) => {
    // body includes at least partnershipDecided toggled
    expect(body).to.have.property("partnershipDecided");
  });
});

Then("the partnerships progress label should update", () => {
  cy.get('[data-cy="partnership-progress-label"]').should("exist");
  // We don't assert exact text, just that it is not empty / present
  cy.get('[data-cy="partnership-progress-label"]')
    .invoke("text")
    .should("match", /\d+\/\d+ · \d+%/);
});
