import { Given, When, Then } from "@badeball/cypress-cucumber-preprocessor";

Given("I visit the homepage", () => {
  cy.visit("http://localhost:8000/");
});

When("I click the login link in the header", () => {
  cy.get("header a").contains("Login").click();
});

Then("I should be on the login page", () => {
  cy.url().should("eq", "http://localhost:8000/login");
});

Then("the header should have {int} links", (count: number) => {
  cy.get("header a").should("have.length", count);
});

Then("the header should have {int} image", (count: number) => {
  cy.get("header img").should("have.length", count);
});

Then("the nav should have {int} links", (count: number) => {
  cy.get("header nav a").should("have.length", count);
});

Then("the first nav link should have text {string}", (text: string) => {
  cy.get("nav a").eq(0).should("have.text", text);
});

Then("the second nav link should have text {string}", (text: string) => {
  cy.get("nav a").eq(1).should("have.text", text);
});

Then("the third nav link should have text {string}", (text: string) => {
  cy.get("nav a").eq(2).should("have.text", text);
});

Then("the fourth nav link should have text {string}", (text: string) => {
  cy.get("nav a").eq(3).should("have.text", text);
});

Then("the last header link should have text {string}", (text: string) => {
  cy.get("header a").last().should("have.text", text);
});

Then("the first header link should have text {string}", (text: string) => {
  cy.get("header a").first().should("have.text", text);
});
