// cypress/support/index.d.ts
declare namespace Cypress {
  interface Chainable {
    login(email?: string, password?: string): Chainable<void>;
    loginAsClient(): Chainable<void>;
    logout(): Chainable<void>;
    waitUntil(
      conditionFn: () => boolean | Promise<boolean> | Chainable<boolean>,
      options?: { interval?: number; timeout?: number; errorMsg?: string }
    ): Chainable<void>;
  }
}
