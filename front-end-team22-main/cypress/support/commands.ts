/// <reference types="cypress" />
// ***********************************************
// This example commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
//
// declare global {
//   namespace Cypress {
//     interface Chainable {
//       login(email: string, password: string): Chainable<void>
//       drag(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       dismiss(subject: string, options?: Partial<TypeOptions>): Chainable<Element>
//       visit(originalFn: CommandOriginalFn, url: string, options: Partial<VisitOptions>): Chainable<Element>
//     }
//   }
// }



/// cypress/support/commands.ts
/// <reference types="cypress" />
// cypress/support/commands.ts

declare global {
  namespace Cypress {
    interface Chainable {
      login(overrides?: { email?: string; password?: string }): Chainable<void>;
      loginAsClient(): Chainable<void>;
      logout(): Chainable<void>;
    }
  }
}

Cypress.Commands.add("login", (overrides = {}) => {
  const email = overrides.email || Cypress.env("TEST_USER_EMAIL") || "jack.vanhecke@ucll.be";
  const password = overrides.password || Cypress.env("TEST_USER_PASSWORD") || "Jack123";

  // call plugin task that returns token string
  const chain = cy.task<string>("performLogin", { email, password }).then((token) => {
    if (!token) throw new Error("performLogin did not return a token");
    cy.setCookie("token", token, { path: "/" });
    cy.visit("/"); // let app pick up cookie/session
    // explicit void return
    return;
  });

  // cast final Chainable<string|void> -> Chainable<void> to satisfy TS
  return chain as unknown as Cypress.Chainable<void>;
});

Cypress.Commands.add("loginAsClient", () => {
  return cy.login({ email: "jack.vanhecke@ucll.be", password: "Jack123" });
});

Cypress.Commands.add("logout", () => {
  cy.clearCookie("token");
  cy.visit("/login");
});

export {};
