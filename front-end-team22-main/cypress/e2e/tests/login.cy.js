/// <reference types="cypress" />

// Welcome to Cypress!
//
// This spec file contains a variety of sample tests
// for a todo list app that are designed to demonstrate
// the power of writing tests in Cypress.
//
// To learn more about how Cypress works and
// what makes it such an awesome testing tool,
// please read our getting started guide:
// https://on.cypress.io/introduction-to-cypress

describe('example to-do app', () => {
    beforeEach(() => {
        // Cypress starts out with a blank slate for each test
        // so we must tell it to visit our website with the `cy.visit()` command.
        // Since we want to visit the same URL at the start of all our tests,
        // we include it in our beforeEach function so that it runs before each test
        cy.visit('http://localhost:8000/login')
    })
    it("should navigate to the login page", () => {
        cy.get("header a").last().click();
    })

    it("should look like intended", () => {
        cy.get("main").first().children().should("have.length", 2);
        cy.get("main").first().children().first().should("have.text", "Login Portal");
        cy.get("main").first().children().eq(1).should("match", "form");
        cy.get("main").first().children().eq(1).find("button").should("have.text", "Login");
        cy.get("main").first().children().eq(1).find("input").should("have.length", 2);
        cy.get("main").first().children().eq(1).find("input").eq(0).should("have.attr", "placeholder", "User");
        cy.get("main").first().children().eq(1).find("input").eq(1).should("have.attr", "placeholder", "Password");
    })

    it("should allow user to login and get a cookie", () => {
        cy.get("main").first().children().eq(1).find("input").eq(0).type("wiebe.delvaux@ucll.be").should("have.value", "wiebe.delvaux@ucll.be");
        cy.get("main").first().children().eq(1).find("input").eq(1).type("Wiebe123").should("have.value", "Wiebe123");
        cy.get("main").first().children().eq(1).find("button").click();
        // We should be on the main page now
        cy.url().should("eq", "http://localhost:8000/");
        // We should have a cookie called token
        cy.getCookie("token").should("exist");
    })

    it("should say when the user doesn't exist", () => {
        cy.get("main").first().children().eq(1).find("input").eq(0).type("noone").should("have.value", "noone");
        cy.get("main").first().children().eq(1).find("input").eq(1).type("testpassword").should("have.value", "testpassword");
        cy.get("main").first().children().eq(1).find("button").click();
        cy.get("main").first().find("p").should("have.text", "Unknown User");
    })

    it("should say when the password is wrong", () => {
        cy.get("main").first().children().eq(1).find("input").eq(0).type("wiebe.delvaux@ucll.be").should("have.value", "wiebe.delvaux@ucll.be");
        cy.get("main").first().children().eq(1).find("input").eq(1).type("wrongtestpassword").should("have.value", "wrongtestpassword");
        cy.get("main").first().children().eq(1).find("button").click();
        cy.get("main").first().find("p").should("have.text", "Incorrect Password");
    })
})
