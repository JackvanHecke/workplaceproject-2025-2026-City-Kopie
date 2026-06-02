import { Given, When, Then } from "@badeball/cypress-cucumber-preprocessor";

/* --------------------------------------
 * Helper functions
 * -------------------------------------- */

/** Attempts to click various possible nav selectors, fallback to direct visit */
function navigateToPartnerships() {
  cy.get("body").then(($body) => {
    const hasTypoNav = $body.find('[data-cy="nav-parterships"]').length > 0;
    const hasGoodNav = $body.find('[data-cy="nav-partnerships"]').length > 0;
    const hasTextNav =
      $body
        .find("a")
        .filter((i, el) => (el.textContent || "").trim() === "Partnerships")
        .length > 0;

    if (hasTypoNav) {
      cy.get('[data-cy="nav-parterships"]').click();
    } else if (hasGoodNav) {
      cy.get('[data-cy="nav-partnerships"]').click();
    } else if (hasTextNav) {
      cy.contains("a", "Partnerships").click();
    } else {
      cy.visit("/partnerships");
    }
  });
}

/** Checks if the coaches table exists */
function coachesTableExists() {
  return cy
    .get("body")
    .then(($body) => $body.find('[data-cy="coaches-table"]').length > 0);
}

/** Prefer data-cy selectors but gracefully fallback */
function clickRefreshButton() {
  cy.get("body").then(($body) => {
    if ($body.find('[data-cy="refresh-coaches"]').length) {
      cy.get('[data-cy="refresh-coaches"]').click();
    } else {
      cy.get('[aria-label="Refresh coaches"]').click();
    }
  });
}

/** Prefer data-cy offer button, fallback to title selector */
function openFirstOfferDetails() {
  cy.get("@coachesTable")
    .find("tbody > tr")
    .first()
    .within(() => {
      if (Cypress.$('[data-cy^="coach-offer-button-"]').length) {
        cy.get('[data-cy^="coach-offer-button-"]').first().click();
      } else {
        cy.get('button[title="Open offer details"]').first().click();
      }
    });
}

/** Prefer data-cy edit button, fallback to visible edit button */
function openFirstCoachEditModal() {
  cy.get("@coachesTable")
    .find("tbody > tr")
    .first()
    .within(() => {
      if (Cypress.$('[data-cy^="coach-edit-"]').length) {
        cy.get('[data-cy^="coach-edit-"]').first().click();
      } else {
        cy.contains("button", "Edit").click();
      }
    });
}

/** Detect & assert modal visibility using all supported selectors */
function assertOfferModalVisible() {
  cy.get("body").then(($body) => {
    if ($body.find('[data-cy="offer-details-modal"]').length) {
      cy.get('[data-cy="offer-details-modal"]').should("be.visible");
    } else {
      cy.get("div.fixed").should("exist").and("be.visible");
    }
  });

  cy.get('[data-cy="offer-modal-title"]').should("exist");
}

/** Detect & assert coach edit modal is visible */
function assertCoachEditModalVisible() {
  cy.get("body").then(($body) => {
    if ($body.find('[data-cy="coach-edit-modal"]').length) {
      cy.get('[data-cy="coach-edit-modal"]').should("be.visible");
    } else {
      cy.contains("h2", "Edit Coach").should("be.visible");
    }
  });
}

/** Perform the edit input + save */
function editCoachName(newName: string) {
  if (Cypress.$('[data-cy="coach-edit-name"]').length) {
    cy.get('[data-cy="coach-edit-name"]').clear().type(newName);
    cy.get('[data-cy="coach-edit-save"]').click();
  } else {
    cy.contains("h2", "Edit Coach").should("be.visible");
    cy.get("input:visible").first().clear().type(newName);
    cy.contains("button", "Save").click();
  }
}

/* --------------------------------------
 * Steps
 * -------------------------------------- */


Given("I open the app root", () => {
  cy.visit("/");
  cy.wait(200);

  coachesTableExists().then((exists) => {
    if (!exists) {
      cy.log("coaches table not on root — navigating to Partnerships");
      navigateToPartnerships();
    }

    cy.get('[data-cy="coaches-table"]', { timeout: 10000 }).should("exist");
  });
});

Given("the server returns an initial coaches list", () => {
  cy.intercept("GET", "**/coaches**", { fixture: "coaches-initial.json" }).as(
    "getCoachesInitial"
  );
});

When("I find the coaches table", () => {
  cy.get('[data-cy="coaches-table"]').as("coachesTable");
  cy.get("@coachesTable").should("exist");
});

Then("I should see at least one coach row", () => {
  cy.get("@coachesTable")
    .find("tbody > tr")
    .should("have.length.greaterThan", 0);
});

When("I click the refresh button", () => {
  cy.intercept("GET", "**/coaches**", { fixture: "coaches-updated.json" }).as(
    "getCoachesUpdated"
  );

  clickRefreshButton();
  cy.wait("@getCoachesUpdated");
});

Then("the table should update with the new coaches list", () => {
  cy.get("@coachesTable")
    .find("tbody > tr")
    .should("have.length.greaterThan", 0)
    .first()
    .within(() => {
      cy.contains("Coach C (new)").should("exist");
    });
});

When("I open the first offer details", () => {
  openFirstOfferDetails();
});

Then("the offer details modal should be visible", () => {
  assertOfferModalVisible();
});

Then("I should see the offer's type in the modal", () => {
  cy.get('[data-cy="offer-modal-title"]').should(
    "contain.text",
    "Group lessons"
  );
});

When("I open the edit modal for the first coach", () => {
  openFirstCoachEditModal();
  assertCoachEditModalVisible();
});

When("I change the coach name to {string} and save", (newName: string) => {
  editCoachName(newName);
});

Then("I should see {string} in the table", (newName: string) => {
  cy.get("@coachesTable")
    .find("tbody > tr")
    .first()
    .should("contain.text", newName);
});
