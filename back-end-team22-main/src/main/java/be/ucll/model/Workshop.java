package be.ucll.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "WORKSHOPS")
public class Workshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAAM")
    private String naam;

    @Column(name = "ZICHTBAAR_IN_APP")
    private boolean zichtbaarInApp;

    @Column(name = "START_DATUM")
    private LocalDate startDatum;

    @Column(name = "EIND_DATUM")
    private LocalDate eindDatum;

    @Column(name = "TIJDSTIP")
    private LocalDate tijdstip;

    @Column(name = "DOELGROEP")
    private String doelgroep;

    @Column(name = "PRIJS", scale = 2)
    private BigDecimal prijs;

    @Column(name = "BETALEN_VIA")
    private String betalenVia;

    @Column(name = "ORGANISATOR")
    private String organisator;

    @Column(name = "CONTACT_PERSOON")
    private String contactPersoon;

    @Column(name = "INSCHRIJVEN_VERPLICHT")
    private boolean inschrijvenVerplicht;

    @Column(name = "INSCHRIJVEN_VIA")
    private String inschrijvenVia;

    @Column(name = "COMMUNICATIE_VIA_APP")
    private boolean communicatieViaApp;

    @ManyToOne
    @JoinColumn(name = "LOCATION_BENCH_ID")
    @JsonManagedReference
    private Location location;

    protected Workshop() {}

    public Workshop(String naam,
                    boolean zichtbaarInApp,
                    LocalDate startDatum,
                    LocalDate eindDatum,
                    LocalDate tijdstip,
                    String doelgroep,
                    BigDecimal prijs,
                    String betalenVia,
                    String organisator,
                    String contactPersoon,
                    boolean inschrijvenVerplicht,
                    String inschrijvenVia,
                    boolean communicatieViaApp,
                    Location location) {
        this.naam = naam;
        this.zichtbaarInApp = zichtbaarInApp;
        this.startDatum = startDatum;
        this.eindDatum = eindDatum;
        this.tijdstip = tijdstip;
        this.doelgroep = doelgroep;
        this.prijs = prijs;
        this.betalenVia = betalenVia;
        this.organisator = organisator;
        this.contactPersoon = contactPersoon;
        this.inschrijvenVerplicht = inschrijvenVerplicht;
        this.inschrijvenVia = inschrijvenVia;
        this.communicatieViaApp = communicatieViaApp;
        this.location = location;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNaam() { return naam; }
    public void setNaam(String naam) { this.naam = naam; }

    public boolean isZichtbaarInApp() { return zichtbaarInApp; }
    public void setZichtbaarInApp(boolean zichtbaarInApp) { this.zichtbaarInApp = zichtbaarInApp; }

    public LocalDate getStartDatum() { return startDatum; }
    public void setStartDatum(LocalDate startDatum) { this.startDatum = startDatum; }

    public LocalDate getEindDatum() { return eindDatum; }
    public void setEindDatum(LocalDate eindDatum) { this.eindDatum = eindDatum; }

    public LocalDate getTijdstip() { return tijdstip; }
    public void setTijdstip(LocalDate tijdstip) { this.tijdstip = tijdstip; }

    public String getDoelgroep() { return doelgroep; }
    public void setDoelgroep(String doelgroep) { this.doelgroep = doelgroep; }

    public BigDecimal getPrijs() { return prijs; }
    public void setPrijs(BigDecimal prijs) { this.prijs = prijs; }

    public String getBetalenVia() { return betalenVia; }
    public void setBetalenVia(String betalenVia) { this.betalenVia = betalenVia; }

    public String getOrganisator() { return organisator; }
    public void setOrganisator(String organisator) { this.organisator = organisator; }

    public String getContactPersoon() { return contactPersoon; }
    public void setContactPersoon(String contactPersoon) { this.contactPersoon = contactPersoon; }

    public boolean isInschrijvenVerplicht() { return inschrijvenVerplicht; }
    public void setInschrijvenVerplicht(boolean inschrijvenVerplicht) { this.inschrijvenVerplicht = inschrijvenVerplicht; }

    public String getInschrijvenVia() { return inschrijvenVia; }
    public void setInschrijvenVia(String inschrijvenVia) { this.inschrijvenVia = inschrijvenVia; }

    public boolean isCommunicatieViaApp() { return communicatieViaApp; }
    public void setCommunicatieViaApp(boolean communicatieViaApp) { this.communicatieViaApp = communicatieViaApp; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}
