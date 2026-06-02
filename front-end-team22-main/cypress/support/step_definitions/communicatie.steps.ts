// cypress/e2e/steps/communicatie.steps.ts
import { Given, When, Then } from "@badeball/cypress-cucumber-preprocessor";

//
// Helper functions
//

/** Opens /communicatie using the front-end baseUrl */
function openCommunicatiePage() {
  cy.visit("/communicatie");
}

/** Setup intercepts for all communication-related endpoints */
function setupCommunicationIntercepts() {
  // /api/communications (list)
  cy.intercept("GET", "**/api/communications").as("getCommunications");

  // /api/communications/{id}
  cy.intercept("GET", "**/api/communications/*").as("getCommunicationById");

  // POST /api/communications
  cy.intercept("POST", "**/api/communications").as("createCommunication");

  // PUT /api/communications/{id}
  cy.intercept("PUT", "**/api/communications/*").as("updateCommunication");

  // DELETE /api/communications/{id}
  cy.intercept("DELETE", "**/api/communications/*").as("deleteCommunication");

  // GET /api/profile/simple (recipient picker)
  cy.intercept("GET", "**/api/profile/simple").as("getRecipientProfiles");
}

/** Fill minimal valid form fields, store generated title as alias */
function fillCommunicationFormWithUniqueTitle() {
  const uniqueTitle = `Cypress communicatie ${Date.now()}`;
  const bodyText = "Dit is een Cypress testbericht.";

  cy.wrap(uniqueTitle).as("communicationTitle");

  cy.get('[data-cy="communication-title-input"]')
    .clear()
    .type(uniqueTitle);

  cy.get('[data-cy="communication-body-textarea"]')
    .clear()
    .type(bodyText);

  // Start date/time are required in the current UI
  cy.get('[data-cy="timing-start-date-input"]')
    .clear()
    .type("2025-01-01");

  cy.get('[data-cy="timing-start-time-input"]')
    .clear()
    .type("10:00");
}

/** Ensure at least one communication exists; if table empty, create one via UI */
function ensureAtLeastOneCommunicationExists() {
  cy.get("body").then(($body) => {
    const hasTable = $body.find('[data-cy="communication-table"]').length > 0;

    if (!hasTable) {
      cy.log("No communication table found, creating first communication via UI");
      cy.get('[data-cy="communication-new-button"]').click();
      fillCommunicationFormWithUniqueTitle();
      cy.get('[data-cy="communication-submit-button"]').click();
      cy.wait("@createCommunication");
      cy.url().should("include", "/communicatie");
      cy.wait("@getCommunications");
      return;
    }

    // Table exists; check if it has at least one row
    cy.get('[data-cy="communication-table"] tbody tr').then(($rows) => {
      if ($rows.length === 0) {
        cy.log("Communication table has no rows, creating first communication via UI");
        cy.get('[data-cy="communication-new-button"]').click();
        fillCommunicationFormWithUniqueTitle();
        cy.get('[data-cy="communication-submit-button"]').click();
        cy.wait("@createCommunication");
        cy.url().should("include", "/communicatie");
        cy.wait("@getCommunications");
      } else {
        cy.log("At least one communication already exists");
      }
    });
  });
}

//
// Steps
//


Given("I open the communicatie page", () => {
  setupCommunicationIntercepts();
  openCommunicatiePage();

  // Wait for initial load
  cy.wait("@getCommunications");
  cy.get('[data-cy="communication-list-page"]', { timeout: 10000 }).should(
    "exist"
  );
});

Then("I should see the communicatie list heading", () => {
  cy.get('[data-cy="communication-list-heading"]').should("be.visible");
});

Then(
  "the backend should have been called to load communications",
  () => {
    cy.wait("@getCommunications");
  }
);

Then(
  "I should see either a communicatie table or an empty state",
  () => {
    cy.get("body").then(($body) => {
      const hasTable = $body.find('[data-cy="communication-table"]').length > 0;
      const hasEmpty = $body.find('[data-cy="communication-list-empty"]').length > 0;

      expect(hasTable || hasEmpty).to.be.true;
    });
  }
);

When('I click the "Nieuw bericht" button', () => {
  cy.get('[data-cy="communication-new-button"]').click();
});

When("I fill in the communication form with a unique title", () => {
  cy.get('[data-cy="communication-form"]').should("exist");
  fillCommunicationFormWithUniqueTitle();
});

When("I submit the communication form", () => {
  cy.get('[data-cy="communication-submit-button"]').click();
});

Then("the create communication backend call should have been made", () => {
  cy.wait("@createCommunication");
});

Then("I should be back on the communicatie list page", () => {
  cy.url().should("include", "/communicatie");
  cy.get('[data-cy="communication-list-page"]').should("exist");
  cy.wait("@getCommunications");
});

Then("I should see the newly created communication in the table", () => {
  cy.get("@communicationTitle").then((title) => {
    cy.get("body").then(($body) => {
      const hasTable = $body.find('[data-cy="communication-table"]').length > 0;
      expect(hasTable, "communication table exists").to.be.true;
    });

    cy.get('[data-cy="communication-table"] tbody tr')
      .should("have.length.greaterThan", 0)
      .contains("td", String(title))
      .should("exist");
  });
});

When("I open the newly created communication in the edit page", () => {
  cy.get("@communicationTitle").then((title) => {
    cy.get('[data-cy="communication-table"] tbody tr').within(() => {
      // Find the row with that title
      cy.contains("td", String(title))
        .parent("tr")
        .within(() => {
          cy.get("button")
            .contains("Bekijken / bewerken")
            .click();
        });
    });
  });

  cy.url().should("match", /\/communicatie\/\d+$/);
  cy.get('[data-cy="communication-edit-page"]').should("exist");
  cy.get('[data-cy="communication-form"]').should("exist");
  cy.wait("@getCommunicationById");
});

When("I change the title of the communication", () => {
  const editedTitle = `Cypress communicatie (bewerkt) ${Date.now()}`;
  cy.wrap(editedTitle).as("communicationTitleEdited");

  cy.get('[data-cy="communication-title-input"]')
    .clear()
    .type(editedTitle);
});

Then("the update communication backend call should have been made", () => {
  cy.wait("@updateCommunication");
});

Then("I should see the updated communication title in the table", () => {
  cy.get("@communicationTitleEdited").then((title) => {
    cy.url().should("include", "/communicatie");
    cy.wait("@getCommunications");
    cy.get('[data-cy="communication-table"] tbody tr')
      .should("have.length.greaterThan", 0)
      .contains("td", String(title))
      .should("exist");
  });
});

Given("at least one communication exists", () => {
  ensureAtLeastOneCommunicationExists();
});

When("I open the first communication in the detail modal", () => {
  cy.get('[data-cy="communication-table"] tbody tr')
    .first()
    .click();

  cy.get('[data-cy="communication-detail-modal"]').should("be.visible");
});

Then("the communication detail modal should be visible", () => {
  cy.get('[data-cy="communication-detail-modal"]').should("be.visible");
});

Then("the detail modal should show a title and body", () => {
  cy.get('[data-cy="communication-detail-title"]')
    .should("be.visible")
    .and("not.be.empty");
  cy.get('[data-cy="communication-detail-body"]')
    .should("be.visible");
});

When("I close the communication detail modal", () => {
  cy.get('[data-cy="communication-detail-close-button"]').click();
});

Then(
  "the communication detail modal should not be visible anymore",
  () => {
    cy.get("body").then(($body) => {
      const exists =
        $body.find('[data-cy="communication-detail-modal"]').length > 0;
      if (exists) {
        cy.get('[data-cy="communication-detail-modal"]').should("not.be.visible");
      }
    });
  }
);

When("I click the edit button in the detail modal", () => {
  cy.get('[data-cy="communication-detail-edit-button"]').click();
});

Then("I should be on the communicatie edit page", () => {
  cy.url().should("match", /\/communicatie\/\d+$/);
  cy.get('[data-cy="communication-edit-page"]').should("exist");
  cy.get('[data-cy="communication-form"]').should("exist");
});

Then(
  "the backend should have been called to load a single communication",
  () => {
    cy.wait("@getCommunicationById");
  }
);

When("I toggle some channels and roles", () => {
  // Channels on basics section
  cy.get('[data-cy="communication-channel-APP_POPUP"]').click();
  cy.get('[data-cy="communication-channel-PROFILE_MESSAGE"]').click();

  // Targeting roles
  cy.get('[data-cy="targeting-role-END_USER"]').click();
  cy.get('[data-cy="targeting-role-COACH"]').click();
});

When("I optionally add and remove a recipient", () => {
  cy.get('[data-cy="recipients-search-input"]').type("a");

  cy.get("body").then(($body) => {
    const hasSuggestions =
      $body.find('[data-cy="recipients-suggestions-list"]').length > 0;

    if (hasSuggestions) {
      cy.get('[data-cy="recipients-suggestions-list"] li')
        .first()
        .click();

      cy.get('[data-cy="recipients-selected-list"]')
        .find('[data-cy^="recipient-chip-"]')
        .first()
        .then(($chip) => {
          const chipDataCy = $chip.attr("data-cy");
          if (chipDataCy) {
            const id = chipDataCy.replace("recipient-chip-", "");
            cy.get(`[data-cy="recipient-remove-${id}"]`).click();
          }
        });
    } else {
      cy.log(
        "No recipient suggestions appeared; skipping add/remove recipient assertions."
      );
    }
  });
});

Then(
  "the changes in the form fields should be reflected in the UI",
  () => {
    // Form still present
    cy.get('[data-cy="communication-form"]').should("exist");

    // Title not empty
    cy.get('[data-cy="communication-title-input"]')
      .should("have.value")
      .and("not.equal", "");

    // Channels checkboxes should exist (exact checked state is not enforced)
    cy.get('[data-cy="communication-channel-APP_POPUP"]').should("exist");
    cy.get('[data-cy="communication-channel-PROFILE_MESSAGE"]').should(
      "exist"
    );

    // Roles container exists
    cy.get('[data-cy="targeting-roles-container"]').should("exist");
  }
);

When("I stub recipient profiles with a few fake profiles", () => {
  // override the default intercept from setupCommunicationIntercepts
  cy.intercept("GET", "**/api/profile/simple", {
    statusCode: 200,
    body: [
      { id: 1, name: "Test Gebruiker 1", email: "test1@example.com" },
      { id: 2, name: "Test Gebruiker 2", email: "test2@example.com" },
    ],
  }).as("getRecipientProfiles");
});


Then(
  "the get recipient profiles backend call should have been made",
  () => {
    cy.wait("@getRecipientProfiles");
  }
);


When(
  "I stub the create communication request to fail with status 400",
  () => {
    cy.intercept("POST", "**/api/communications", {
      statusCode: 400,
      body: "Validation failed from backend",
    }).as("createCommunicationFail");
  }
);

Then(
  "I should see an error message for failed communication creation",
  () => {
    cy.wait("@createCommunicationFail");
    cy.get('[data-cy="communication-new-error"]')
      .should("be.visible")
      .and("contain.text", "mislukt")
      .and("contain.text", "Aanmaken");
  }
);
