describe("SideNavigation", () => {
  beforeEach(() => {
    cy.visit("http://localhost:8000/");
  });
  it("should look like intended", () => {
    cy.get("nav ul li").should("have.length", 8);
    cy.get("nav ul")
      .first()
      .children()
      .eq(0)
      .should("have.text", "Infrastructuur");
    cy.get("nav ul")
      .first()
      .children()
      .eq(1)
      .should("have.text", "Activiteiten");
    cy.get("nav ul")
      .first()
      .children()
      .eq(2)
      .should("have.text", "Communicatie");
    cy.get("nav ul").first().children().eq(3).should("have.text", "Monitoring");
    cy.get("nav ul").first().children().eq(4).should("have.text", "Onderhoud");
    cy.get("nav ul").first().children().eq(5).should("have.text", "Financiën");
    cy.get("nav ul")
      .first()
      .children()
      .eq(6)
      .should("have.text", "Partnerships");
    cy.get("nav ul")
      .first()
      .children()
      .eq(7)
      .should("have.text", "Instellingen");
  });

  it("should navigate to the corresponding page", () => {
    cy.get("nav ul").first().children().eq(0).click();
    cy.url().should("eq", "http://localhost:8000/infrastructuur");
    cy.get("nav ul").first().children().eq(1).click();
    cy.url().should("eq", "http://localhost:8000/activiteiten");
    cy.get("nav ul").first().children().eq(2).click();
    cy.url().should("eq", "http://localhost:8000/communicatie");
    cy.get("nav ul").first().children().eq(3).click();
    cy.url().should("eq", "http://localhost:8000/monitoring");
    cy.get("nav ul").first().children().eq(4).click();
    cy.url().should("eq", "http://localhost:8000/onderhoud");
    cy.get("nav ul").first().children().eq(5).click();
    cy.url().should("eq", "http://localhost:8000/financien");
    cy.get("nav ul").first().children().eq(6).click();
    cy.url().should("eq", "http://localhost:8000/partnerships");
    cy.get("nav ul").first().children().eq(7).click();
    cy.url().should("eq", "http://localhost:8000/instellingen");
  });
});
