// cypress/support/step_definitions/auth.steps.ts
import { Given } from "@badeball/cypress-cucumber-preprocessor";

Given("I am logged in as a client", () => {
  // The real login is done in the global Before hook.
  // Here we just assert/assume that we're logged in.

  cy.log("Using global Before hook for login");

  // Optional safety check so this step isn't completely empty:
  cy.getCookie("token").should("exist");
});
