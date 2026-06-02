import { Given, When, Then } from "@badeball/cypress-cucumber-preprocessor";

Given("I visit the home page", () => {
  cy.visit("http://localhost:8000/");
});

Then(
  "the side navigation should have {int} items with correct labels",
  (count: number) => {
    const labels = [
      "Infrastructuur",
      "Activiteiten",
      "Communicatie",
      "Monitoring",
      "Onderhoud",
      "Financiën",
      "Partnerships",
      "Instellingen",
    ];

    cy.get("nav ul li").should("have.length", count);

    labels.forEach((label, index) => {
      cy.get("nav ul").first().children().eq(index).should("have.text", label);
    });
  }
);

When("I click on the side navigation item {string}", (label: string) => {
  cy.get("nav ul").first().contains(label).click();
});

Then("I should be navigated to {string}", (url: string) => {
  cy.url().should("eq", `http://localhost:8000${url}`);
});
