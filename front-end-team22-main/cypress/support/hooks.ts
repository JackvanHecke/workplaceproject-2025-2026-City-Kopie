// cypress/support/hooks.ts
import { Before } from "@badeball/cypress-cucumber-preprocessor";
import "./commands";

// Run before every scenario
Before(() => {
  cy.log("Before hook: logging in");
  cy.login(); 
});
