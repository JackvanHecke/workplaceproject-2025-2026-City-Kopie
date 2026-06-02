describe("Navigation", () => {
  beforeEach(() => {
    cy.visit("http://localhost:8000/");
  });
  it("should navigate to the login page on click login button", () => {
    cy.get("header a").last().click();
    cy.url().should("eq", "http://localhost:8000/login");
  });
  it("should look like intended", () => {
    cy.get("header a").first().should("have.text", "Login");
    cy.get("header a").should("have.length", 3);
    cy.get("header img").should("have.length", 1);
    cy.get("header nav a").should("have.length", 2);

    cy.get("nav a").first().should("have.text", "Home");
    cy.get("nav a").last().should("have.text", "KlantenDashboard");

    cy.get("header a").last().should("have.text", "Login");
  });
});
