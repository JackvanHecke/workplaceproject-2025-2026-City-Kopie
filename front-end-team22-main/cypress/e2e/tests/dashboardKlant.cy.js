describe("Dashboard Klant", () => {
  beforeEach(() => {
    cy.visit("http://localhost:8000/dashboardklant");
  });

  it("should display the dashboard klant page", () => {
    cy.contains("h1", "Stappenplan").should("exist");
    cy.contains("p", "Volg je voortgang en voltooi alle stappen").should("exist");
  });

it("should show a checklist per step title.", () => {
  // Wait for progress bar and checklist content
  cy.contains("span", "Totale voortgang", { timeout: 10000 }).should("exist");
  
  // Verify each section title
  cy.contains("h2", "Voorbereiding").should("exist");
  cy.contains("label", "Demo- of testfase (optioneel)", { timeout: 10000 }).should("exist");
  cy.contains("h2", "Opstart").should("exist");
  cy.contains("h2", "Uitvoering").should("exist");
  cy.contains("h2", "Opvolging").should("exist");
});

});
