import { Given, When, Then } from "@badeball/cypress-cucumber-preprocessor";

/* --------------------------------------
 * Helper selectors
 * -------------------------------------- */

const selectors = {
  user: "[data-cy=login-user]",
  password: "[data-cy=login-password]",
  submit: "[data-cy=login-submit]",
  error: "[data-cy=login-error]",
};

/* --------------------------------------
 * Steps
 * -------------------------------------- */

Given("I visit the login page", () => {
  cy.visit("/login");
  cy.location("pathname").should("eq", "/login");
});

When("I enter valid credentials and submit", () => {
  const email = "jack.vanhecke@ucll.be";
  const password = "Jack123";

  cy.get(selectors.user).clear().type(email);
  cy.get(selectors.password).clear().type(password);
  cy.get(selectors.submit).click();

  // App redirects via window.location.href
  cy.location("pathname", { timeout: 10000 }).should("eq", "/");
});

Then("I should be redirected to the homepage", () => {
  cy.location("pathname").should("eq", "/");
});

Then("the token cookie should be set", () => {
  cy.getCookie("token")
    .should("exist")
    .and((cookie) => {
      expect(cookie!.value).to.have.length.greaterThan(0);
    });
});

When("I submit the form with empty fields", () => {
  cy.get(selectors.user).clear();
  cy.get(selectors.password).clear();
  cy.get(selectors.submit).click();
});

Then("I should see {string}", (text: string) => {
  cy.get(selectors.error).should("be.visible").and("contain.text", text);
});

When("I enter an unknown user and submit", () => {
  cy.get(selectors.user).clear().type("noone@example.com");
  cy.get(selectors.password).clear().type("doesntmatter");
  cy.get(selectors.submit).click();

  cy.get(selectors.error, { timeout: 5000 }).should("be.visible");
});
