package be.ucll.repository;

import be.ucll.model.*;
import be.ucll.model.enums.CommunicationCategory;
import be.ucll.model.enums.DeliveryChannel;
import be.ucll.model.enums.ProfileRole;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import be.ucll.model.MovementRoute;



@Component
public class DbInitializer {

    private final ProfileRepository profileRepository;
    private final PhaseRepository phaseRepository;
    private final ChecklistRepository checklistRepository;
    private final ExerciseRepository exerciseRepository;
    private final ProfileExerciseRepository profileExerciseRepository;
    private final LocationRepository locationRepository;
    private final CoachRepository coachRepository;
    private final StakeholderRepository stakeholderRepository;
    private final CompletedChecklistItemRepository completedChecklistItemRepository;
    private final CommunicationMessageRepository communicationMessageRepository;
    private final ProfileMessageRepository profileMessageRepository;
    private final WorkshopRepository workshopRepository;
    private final FAQRepository faqRepository;
    private final MovementRouteRepository movementRouteRepository;

    private final PartnershipCategoryRepository partnershipCategoryRepository;
    private final LocationPartnershipRepository locationPartnershipRepository;


    @Autowired
    public DbInitializer(
            ProfileRepository profileRepository,
            PhaseRepository phaseRepository,
            ChecklistRepository checklistRepository,
            ExerciseRepository exerciseRepository,
            ProfileExerciseRepository profileExerciseRepository,
            LocationRepository locationRepository,
            CoachRepository coachRepository,
            StakeholderRepository stakeholderRepository,
            CompletedChecklistItemRepository completedChecklistItemRepository,
            CommunicationMessageRepository communicationMessageRepository,
            ProfileMessageRepository profileMessageRepository,
            FAQRepository faqRepository,
            WorkshopRepository workshopRepository,
            MovementRouteRepository movementRouteRepository, PartnershipCategoryRepository partnershipCategoryRepository, LocationPartnershipRepository locationPartnershipRepository
    ) {
        this.profileRepository = profileRepository;
        this.phaseRepository = phaseRepository;
        this.checklistRepository = checklistRepository;
        this.exerciseRepository = exerciseRepository;
        this.profileExerciseRepository = profileExerciseRepository;
        this.locationRepository = locationRepository;
        this.coachRepository = coachRepository;
        this.stakeholderRepository = stakeholderRepository;
        this.completedChecklistItemRepository = completedChecklistItemRepository;
        this.communicationMessageRepository = communicationMessageRepository;
        this.profileMessageRepository = profileMessageRepository;
        this.faqRepository = faqRepository;
        this.workshopRepository = workshopRepository;
        this.movementRouteRepository = movementRouteRepository;
        this.partnershipCategoryRepository = partnershipCategoryRepository;
        this.locationPartnershipRepository = locationPartnershipRepository;
    }

    @Autowired
    private EntityManager entityManager;

    public void clearAll() {
        completedChecklistItemRepository.deleteAll();
        profileExerciseRepository.deleteAll();
        profileMessageRepository.deleteAll();
        communicationMessageRepository.deleteAll();

        locationPartnershipRepository.deleteAll();

        checklistRepository.deleteAll();
        exerciseRepository.deleteAll();
        coachRepository.deleteAll();
        stakeholderRepository.deleteAll();
        workshopRepository.deleteAll();
        locationRepository.deleteAll();

        partnershipCategoryRepository.deleteAll();

        faqRepository.deleteAll();
        phaseRepository.deleteAll();
        profileRepository.deleteAll();
        movementRouteRepository.deleteAll();
    }

    @SuppressWarnings("unused")
    @PostConstruct
    public void initialize() {
        clearAll();

        seedFaqs();
        seedProfiles();
        seedExercises();
        seedPhasesAndChecklists();
        seedMovementRoutes();
        seedLocationsAndWorkshops();
        seedCoaches();
        seedStakeholders();

        seedPartnershipCategories();
        seedLocationPartnerships();

        seedCommunicationAndProfileMessages();
        seedCompletedChecklistItems();
    }

    private Profile adminProfile, client1, client2, client3, client4, coachProfile, endUserProfile;
    private Checklist checklist1, checklist2, checklist6;
    private Location location1, location2;

    private List<Location> client1Locations = new java.util.ArrayList<>();
    private List<Location> client2Locations = new java.util.ArrayList<>();
    private List<Location> client3Locations = new java.util.ArrayList<>();
    private List<Location> client4Locations = new java.util.ArrayList<>();
    private MovementRoute routeSluispark, routeAbdijPark, routeBlaarmeersen, routeMiddelheim;

    private static final String DEFAULT_BENCH_PHOTO = "/uploads/benches/bench-1-beweegbank.jpg";


    private void seedFaqs() {
        FAQ faq1 = new FAQ("Is there a dark mode available", "No, currently there is no dark mode available in the application.");
        FAQ faq2 = new FAQ("Is there a way to track progress.", "No, progress tracking features are not the purpose of this application.");
        FAQ faq3 = new FAQ("How can i order a bench?", "You can order a bench through our website or by contacting our sales team directly.");
        FAQ faq4 = new FAQ("What is IpItUps contact information", "You can find our contact information on the Contact page of the website.");

        faqRepository.saveAll(List.of(faq1, faq2, faq3, faq4));
    }

    private void seedProfiles() {
        adminProfile = new Profile(
                "Admin", 25, "Admin@ipitup.be", "Male", "p", "BE",
                new BigDecimal("20.2"), 12, 6, 3, 150,
                LocalDateTime.parse("2023-02-13T08:45:14"),
                "$2a$10$salt123456789101112131",
                "$2a$10$salt12345678910111213uMVGcC3.O0CZ7reEpXx0zFlFHVQxkmD6"
        );
        adminProfile.setRole(ProfileRole.ADMIN);

        // 4 clients
        client1 = new Profile("Client One", 30, "client1@ipitup.be", "male", "p", "BE",
                new BigDecimal("25.0"), 0, 0, 0, 5, LocalDateTime.now(),
                "$2a$10$salt123456789101112131",
                "$2a$10$salt12345678910111213uMVGcC3.O0CZ7reEpXx0zFlFHVQxkmD6");        
        client1.setRole(ProfileRole.CLIENT);

        client2 = new Profile("Client Two", 31, "client2@ipitup.be", "female", "p", "BE",
                new BigDecimal("24.0"), 0, 0, 0, 5, LocalDateTime.now(),
                "$2a$10$salt123456789101112131",
                "$2a$10$salt12345678910111213uMVGcC3.O0CZ7reEpXx0zFlFHVQxkmD6");
        client2.setRole(ProfileRole.CLIENT);

        client3 = new Profile("Client Three", 32, "client3@ipitup.be", "male", "p", "BE",
                new BigDecimal("26.5"), 0, 0, 0, 5, LocalDateTime.now(),
                "$2a$10$salt123456789101112131",
                "$2a$10$salt12345678910111213uMVGcC3.O0CZ7reEpXx0zFlFHVQxkmD6");
        client3.setRole(ProfileRole.CLIENT);

        client4 = new Profile("Client Four", 33, "client4@ipitup.be", "female", "p", "BE",
                new BigDecimal("23.7"), 0, 0, 0, 5, LocalDateTime.now(),
                "$2a$10$salt123456789101112131",
                "$2a$10$salt12345678910111213uMVGcC3.O0CZ7reEpXx0zFlFHVQxkmD6");
        client4.setRole(ProfileRole.CLIENT);

        coachProfile = new Profile("Coach", 28, "Coach@ipitup.be", "Male", "p", "BE",
                new BigDecimal("20.5"), 0, 0, 0, 0, LocalDateTime.now(),
                "$2a$10$salt123456789101112131",
                "$2a$10$salt12345678910111213uMVGcC3.O0CZ7reEpXx0zFlFHVQxkmD6");
        coachProfile.setRole(ProfileRole.COACH);

        endUserProfile = new Profile("User", 32, "User@ipitup.be", "Male", "p", "BE",
                new BigDecimal("23.0"), 0, 0, 0, 0, LocalDateTime.now(),
                "$2a$10$salt123456789101112131",
                "$2a$10$salt12345678910111213uMVGcC3.O0CZ7reEpXx0zFlFHVQxkmD6");
        endUserProfile.setRole(ProfileRole.END_USER);

        profileRepository.saveAll(List.of(
                adminProfile, client1, client2, client3, client4, coachProfile, endUserProfile
        ));
    }



    private void seedExercises() {
        Exercise exercise1 = new Exercise("EX001", "Cardio", 1, 5, 1, 30);
        Exercise exercise2 = new Exercise("EX002", "Strength", 2, 2, 0, 20);

        exerciseRepository.saveAll(List.of(exercise1, exercise2));
    }

    private void seedPhasesAndChecklists() {
        Phase voorbereiding = new Phase("Voorbereiding");
        Phase opstart = new Phase("Opstart");
        Phase uitvoering = new Phase("Uitvoering");
        Phase opvolging = new Phase("Opvolging");

        // Voorbereiding
        checklist1 = new Checklist("Demo- of testfase", "Wanneer jullie organisatie interesse toont in het IPitup-beweegproject, kunnen jullie een demonstratie of testfase aanvragen. Tijdens deze periode wordt tijdelijk een mobiel beweegtoestel geplaatst (voor één of meerdere maanden) zodat jullie kunnen nagaan of er draagvlak is voor een beweegtoestel in de buurt en welke locatie het meest geschikt is. Met behulp van de Citizen Dialog Kit kunnen jullie feedback verzamelen van gebruikers.", voorbereiding);
        voorbereiding.addChecklist(checklist1);

        checklist2 = new Checklist("Goedkeuring samenwerking", "Wanneer jullie overtuigd zijn van het project en de financiering rond is, kunnen jullie overgaan tot aankoop van een beweegbank. In veel regio’s zijn er subsidies of steunmaatregelen die dit proces vergemakkelijken, zoals via Cera, Nike Community Impact Fund, de Vlaamse overheid, l’Agence nationale de Sport of Rabobank. In het portaal kunnen jullie offertes bekijken en opvolgen.", voorbereiding);
        voorbereiding.addChecklist(checklist2);

        Checklist checklist3 = new Checklist("Aanwijzing trekker(s)", "Om het project goed te begeleiden is het belangrijk dat er binnen jullie organisatie één of meerdere trekkers worden aangeduid. Zij volgen het project op, organiseren activiteiten en communiceren hierover. In het portaal kunnen jullie de gegevens van deze trekker(s) invullen: naam, functie, organisatie, e-mailadres, gsm en eventueel foto.", voorbereiding);
        voorbereiding.addChecklist(checklist3);

        Checklist checklist4 = new Checklist("Trekker volgt workshop", "Tijdens de IPitup-workshop leren trekkers hoe ze een lokaal beweegaanbod kunnen samenstellen dat aansluit bij hun doelgroepen. De workshop kan online of op locatie gevolgd worden. Het portaal toont wie wanneer deelnam, en herinnert automatisch wanneer een workshop te lang geleden is gevolgd.", voorbereiding);
        voorbereiding.addChecklist(checklist4);

        Checklist checklist5 = new Checklist("Trekker contacteert stakeholders", "De trekker brengt lokale organisaties of personen in kaart die kunnen bijdragen aan het beweegproject. In het portaal kunnen mogelijke stakeholders bekeken worden én eigen contacten toegevoegd worden (organisatie, contactpersoon, functie, e-mail, telefoon).", voorbereiding);
        voorbereiding.addChecklist(checklist5);

        // Opstart
        checklist6 = new Checklist("Kick-off met stakeholders", "De trekker organiseert een eerste overleg met stakeholders om rollen en samenwerkingen te bespreken. IPitup voorziet een presentatie en eventueel een participatiespel om de rollen te verduidelijken.", opstart);
        opstart.addChecklist(checklist6);

        Checklist checklist7 = new Checklist("Locatiebepaling beweegtoestel", "Bij het kiezen van de juiste locatie helpt IPitup met een checklist (passage, zichtbaarheid, toegankelijkheid, zon/schaduw, …). Deze checklist is in het portaal te raadplegen.", opstart);
        opstart.addChecklist(checklist7);

        Checklist checklist8 = new Checklist("Plaatsing beweegbank", "Na aankoop wordt de beweegbank geplaatst door IPitup, de aankoper of een partner. De trekker vult locatiegegevens en foto’s in zodat het toestel actief wordt in de app.", opstart);
        opstart.addChecklist(checklist8);

        Checklist checklist9 = new Checklist("Activatoren volgen trainersopleiding", "Activatoren leren tijdens een IPitup-opleiding hoe ze beweegactiviteiten kunnen organiseren. Na de opleiding ontvangen ze een certificaat als IPitup-trainer of -ambassadeur.", opstart);
        opstart.addChecklist(checklist9);

        // Uitvoering
        Checklist checklist10 = new Checklist("Officiële ingebruikname", "De trekker organiseert een inhuldiging om de officiële start te markeren. Via het portaal zijn draaiboeken en inspirerende voorbeelden te bekijken.", uitvoering);
        uitvoering.addChecklist(checklist10);

        Checklist checklist11 = new Checklist("Planning aanbod", "De trekker stelt een planning op met activiteiten of lessenreeksen rond de beweegbank. Deze kunnen in het portaal ingevoerd en later aangepast worden.", uitvoering);
        uitvoering.addChecklist(checklist11);

        Checklist checklist12 = new Checklist("Communicatie", "Het aanbod wordt bekendgemaakt via communicatiekanalen. In het portaal kunnen voorbeelden en eigen materialen gedeeld worden.", uitvoering);
        uitvoering.addChecklist(checklist12);

        Checklist checklist13 = new Checklist("Uitrol activiteitenaanbod", "De geplande activiteiten starten. Activatoren begeleiden de lessen of interventies. In het portaal kunnen deze ingevoerd en gekoppeld worden aan de beweegtoestellen.", uitvoering);
        uitvoering.addChecklist(checklist13);

        // Opvolging
        Checklist checklist14 = new Checklist("Evaluatieoverleg trekker, stakeholders en activatoren", "Regelmatige overlegmomenten helpen het project verbeteren. De trekker kan data en feedback noteren in het portaal.", opvolging);
        opvolging.addChecklist(checklist14);

        Checklist checklist15 = new Checklist("Evaluatieoverleg trekker en IPitup", "Minstens één keer per jaar is er overleg met IPitup over resultaten en app-data. De data zijn beschikbaar via het bestaande dashboard (Looker Studio).", opvolging);
        opvolging.addChecklist(checklist15);

        Checklist checklist16 = new Checklist("Onderhoud en veiligheidsinspectie", "Om de veiligheid te garanderen moeten de beweegtoestellen periodiek onderhouden en geïnspecteerd worden. Alle onderhoudsrapporten en facturen zijn via het portaal beschikbaar.", opvolging);
        opvolging.addChecklist(checklist16);

        // Save phases (cascade saves checklists)
        phaseRepository.saveAll(List.of(voorbereiding, opstart, uitvoering, opvolging));
    }

    private void seedMovementRoutes() {
        // --- Movement routes (for benches) ---
        routeSluispark = new MovementRoute();
        routeSluispark.setName("Sluispark lus");
        routeSluispark.setType("WALKING");
        routeSluispark.setFileUrl("/uploads/routes/sluispark-lus.gpx");
        MovementRoute routeAbdijPark = new MovementRoute();
        routeAbdijPark.setName("Abdij van Park rondje");
        routeAbdijPark.setType("WALKING");
        routeAbdijPark.setFileUrl("/uploads/routes/abdijpark-rondje.gpx");

        routeBlaarmeersen = new MovementRoute();
        routeBlaarmeersen.setName("Blaarmeersen loop");
        routeBlaarmeersen.setType("RUNNING");
        routeBlaarmeersen.setFileUrl("/uploads/routes/blaarmeersen-loop.gpx");

        MovementRoute routeMiddelheim = new MovementRoute();
        routeMiddelheim.setName("Middelheim park tour");
        routeMiddelheim.setType("WALKING");
        routeMiddelheim.setFileUrl("/uploads/routes/middelheim-tour.gpx");
        movementRouteRepository.saveAll(List.of(
                routeSluispark,
                routeAbdijPark,
                routeBlaarmeersen,
                routeMiddelheim
        ));
    }

    private Location makeLocation(
            String name,
            Profile ownerProfile,
            String city,
            String country,
            String street,
            String houseNr,
            String postalCode,
            String size,
            String type,
            boolean showInApp,
            boolean mobile,
            boolean isPublic,
            Double lat,
            Double lng,
            String stations,
            String tagsCsv,
            MovementRoute route,
            String photoUrl
    ) {
        Location l = new Location();
        l.setBenchName(name);
        l.setBenchOwner(ownerProfile.getEmail());   // ✅ critical for /locations/mine?owner=email
        l.setBenchCity(city);
        l.setBenchCountry(country);
        l.setBenchStreet(street);
        l.setBenchHouseNumber(houseNr);
        l.setBenchPostalCode(postalCode);
        l.setBenchSize(size);
        l.setBenchType(type);
        l.setConnectedRoutes(1);

        l.setShowInApp(showInApp);
        l.setMobile(mobile);
        l.setPublicAvailable(isPublic);

        l.setLatitude(lat);
        l.setLongitude(lng);

        l.setStations(stations);
        l.setTags(tagsCsv);

        l.setMovementRoute(route);
        l.setPhotoUrl(photoUrl);

        return l;
    }

    private void seedLocationsAndWorkshops() {
        // clear previous lists (important on re-run)
        client1Locations.clear();
        client2Locations.clear();
        client3Locations.clear();
        client4Locations.clear();

        // ---- Client 1 (4 locations) ----
        location1 = makeLocation(
                "Central Park Bench",
                client1,
                "New York",
                "USA",
                "Central Park West",
                "10",
                "10024",
                "Large",
                "Beweegbank",
                true, false, true,
                40.7812, -73.9665,
                "Pull-up bench,Dips bench,Core bench",
                "Park,Outdoor,NYC",
                routeSluispark,
                DEFAULT_BENCH_PHOTO
        );

        location2 = makeLocation(
                "Riverside Bench",
                client1,
                "Amsterdam",
                "Netherlands",
                "Amstelkade",
                "5",
                "1017",
                "Medium",
                "Beweegbank",
                true, true, true,
                52.3570, 4.9020,
                "Push-up bench,Step-up bench,Core bench",
                "Riverside,Urban,AMS",
                routeAbdijPark,
                DEFAULT_BENCH_PHOTO
        );

        Location c1_loc3 = makeLocation(
                "Leuven Sluispark Beweegbank",
                client1,
                "Leuven",
                "Belgium",
                "Sluispark",
                "13",
                "3000",
                "Large",
                "Beweegbank",
                true, false, true,
                50.888626, 4.700308,
                "Pull-up bench,Dips bench,Step-up bench,Core bench",
                "Park,Leuven,Sluispark",
                routeSluispark,
                DEFAULT_BENCH_PHOTO
        );

        Location c1_loc4 = makeLocation(
                "Leuven Abdij van Park Beweegbank",
                client1,
                "Leuven",
                "Belgium",
                "Abdij van Park",
                "1",
                "3001",
                "Medium",
                "Beweegbank",
                true, true, true,
                50.8555, 4.7390,
                "Push-up bench,Step-up bench,Core bench",
                "Park,Leuven,Abdij",
                routeAbdijPark,
                DEFAULT_BENCH_PHOTO
        );

        client1Locations.addAll(List.of(location1, location2, c1_loc3, c1_loc4));

        // ---- Client 2 (4 locations) ----
        Location c2_loc1 = makeLocation(
                "Gent Watersportbaan Beweegbank",
                client2,
                "Gent",
                "Belgium",
                "Zuiderlaan",
                "5",
                "9000",
                "Large",
                "Beweegbank",
                true, false, true,
                51.0375, 3.7030,
                "Pull-up bench,Step-up bench,Core bench",
                "Park,Gent,Watersportbaan",
                routeBlaarmeersen,
                DEFAULT_BENCH_PHOTO
        );

        Location c2_loc2 = makeLocation(
                "Antwerpen Middelheim Beweegbank",
                client2,
                "Antwerpen",
                "Belgium",
                "Middelheimlaan",
                "61",
                "2020",
                "Small",
                "Beweegbank",
                true, false, true,
                51.1760, 4.4100,
                "Push-up bench,Dips bench,Core bench",
                "Park,Antwerpen,Middelheim",
                routeMiddelheim,
                DEFAULT_BENCH_PHOTO
        );

        Location c2_loc3 = makeLocation(
                "Brussel Warande Beweegbank",
                client2,
                "Brussel",
                "Belgium",
                "Warandepark",
                "2",
                "1000",
                "Medium",
                "Beweegbank",
                true, false, true,
                50.8467, 4.3659,
                "Pull-up bench,Core bench",
                "Park,Brussel,Warande",
                routeSluispark,
                DEFAULT_BENCH_PHOTO
        );

        Location c2_loc4 = makeLocation(
                "Mechelen Dijlepad Beweegbank",
                client2,
                "Mechelen",
                "Belgium",
                "Dijlepad",
                "7",
                "2800",
                "Medium",
                "Beweegbank",
                true, true, true,
                51.0259, 4.4775,
                "Push-up bench,Step-up bench",
                "Urban,Mechelen,Dijle",
                routeAbdijPark,
                DEFAULT_BENCH_PHOTO
        );

        client2Locations.addAll(List.of(c2_loc1, c2_loc2, c2_loc3, c2_loc4));

        // ---- Client 3 (4 locations) ----
        Location c3_loc1 = makeLocation(
                "Hasselt Kapermolen Beweegbank",
                client3,
                "Hasselt",
                "Belgium",
                "Kapermolenpark",
                "1",
                "3500",
                "Medium",
                "Beweegbank",
                true, false, true,
                50.9347, 5.3382,
                "Pull-up bench,Dips bench",
                "Park,Hasselt,Kapermolen",
                routeBlaarmeersen,
                DEFAULT_BENCH_PHOTO
        );

        Location c3_loc2 = makeLocation(
                "Kortrijk Broel Beweegbank",
                client3,
                "Kortrijk",
                "Belgium",
                "Broelkaai",
                "12",
                "8500",
                "Small",
                "Beweegbank",
                true, true, true,
                50.8265, 3.2651,
                "Core bench,Step-up bench",
                "Urban,Kortrijk,Broel",
                routeMiddelheim,
                DEFAULT_BENCH_PHOTO
        );

        Location c3_loc3 = makeLocation(
                "Oostende Zeepark Beweegbank",
                client3,
                "Oostende",
                "Belgium",
                "Zeedijk",
                "20",
                "8400",
                "Large",
                "Beweegbank",
                true, false, true,
                51.2300, 2.9200,
                "Push-up bench,Pull-up bench",
                "Coast,Oostende,Sea",
                routeSluispark,
                "/uploads/benches/oostende-zee.jpg"
        );

        Location c3_loc4 = makeLocation(
                "Aalst Stadspark Beweegbank",
                client3,
                "Aalst",
                "Belgium",
                "Stadspark",
                "3",
                "9300",
                "Medium",
                "Beweegbank",
                true, false, true,
                50.9407, 4.0397,
                "Dips bench,Core bench",
                "Park,Aalst",
                routeAbdijPark,
                "/uploads/benches/aalst-stadspark.jpg"
        );

        client3Locations.addAll(List.of(c3_loc1, c3_loc2, c3_loc3, c3_loc4));

        // ---- Client 4 (4 locations) ----
        Location c4_loc1 = makeLocation(
                "Genk Molenvijver Beweegbank",
                client4,
                "Genk",
                "Belgium",
                "Molenvijverpark",
                "4",
                "3600",
                "Medium",
                "Beweegbank",
                true, true, true,
                50.9650, 5.5000,
                "Pull-up bench,Core bench",
                "Park,Genk,Molenvijver",
                routeBlaarmeersen,
                "/uploads/benches/genk-molenvijver.jpg"
        );

        Location c4_loc2 = makeLocation(
                "Turnhout Stadsbos Beweegbank",
                client4,
                "Turnhout",
                "Belgium",
                "Stadsbos",
                "8",
                "2300",
                "Small",
                "Beweegbank",
                true, false, true,
                51.3200, 4.9500,
                "Push-up bench,Step-up bench",
                "Forest,Turnhout",
                routeMiddelheim,
                "/uploads/benches/turnhout-stadsbos.jpg"
        );

        Location c4_loc3 = makeLocation(
                "Sint-Niklaas Park Beweegbank",
                client4,
                "Sint-Niklaas",
                "Belgium",
                "Parklaan",
                "15",
                "9100",
                "Medium",
                "Beweegbank",
                true, false, true,
                51.1650, 4.1500,
                "Dips bench,Core bench",
                "Urban,Sint-Niklaas",
                routeSluispark,
                "/uploads/benches/sint-niklaas-park.jpg"
        );

        Location c4_loc4 = makeLocation(
                "Roeselare Sterrebos Beweegbank",
                client4,
                "Roeselare",
                "Belgium",
                "Sterrebosdreef",
                "6",
                "8800",
                "Large",
                "Beweegbank",
                true, true, true,
                50.9500, 3.1300,
                "Pull-up bench,Dips bench,Core bench",
                "Park,Roeselare,Sterrebos",
                routeAbdijPark,
                "/uploads/benches/roeselare-sterrebos.jpg"
        );

        client4Locations.addAll(List.of(c4_loc1, c4_loc2, c4_loc3, c4_loc4));

        // Save all 16 locations
        List<Location> all = new java.util.ArrayList<>();
        all.addAll(client1Locations);
        all.addAll(client2Locations);
        all.addAll(client3Locations);
        all.addAll(client4Locations);

        locationRepository.saveAll(all);

        // ---- Workshops (keep your existing ones, still pointing at location1/location2) ----
        Workshop w1 = new Workshop("IPitup Basiscursus", true, LocalDate.now().plusDays(14), LocalDate.now().plusDays(21), LocalDate.now().plusDays(14), "BASIS",
                new BigDecimal("20.00"), "overschrijving", "IPitup vzw", "John Doe", true, "www.example.com/inschrijven", true, location1);

        Workshop w2 = new Workshop("IPitup Verdieping", true, LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(1).plusDays(7), LocalDate.now().plusMonths(1), "VERDIEPING",
                new BigDecimal("30.00"), "overschrijving", "IPitup vzw", "John Doe", true, "www.example.com/inschrijven", true, location1);

        Workshop w3 = new Workshop("IPitup Herhalingssessie", true, LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(3).plusDays(3), LocalDate.now().plusWeeks(3), "HERHALING",
                new BigDecimal("15.00"), "overschrijving", "IPitup vzw", "John Doe", false, "", true, location2);

        location1.addWorkshops(List.of(w1, w2));
        location2.addWorkshops(List.of(w3));

        workshopRepository.saveAll(List.of(w1, w2, w3));
    }


    private void seedCoaches() {
        // Coach 1
        Coach coach1 = new Coach(adminProfile);
        coach1.setBio("Experienced coach specializing in cardio and strength training.");
        coach1.addLocation(location1);
        coach1.addLocation(location2);

        CoachOffer offer1 = new CoachOffer();
        offer1.setOfferType("Group lessons");
        offer1.setTargetGroup("Adults 18-50");
        offer1.setDescription("High-intensity cardio group sessions");
        offer1.setPrice(25.0);
        offer1.setFreeOrPaid(CoachOffer.FeeType.paid);
        offer1.setStartDatetime(LocalDateTime.of(2025, 11, 10, 10, 0));
        offer1.setEndDatetime(LocalDateTime.of(2025, 11, 10, 11, 0));
        offer1.setRecurrence("Weekly");
        coach1.addOffer(offer1);

        CoachAvailability avail1 = new CoachAvailability();
        avail1.setAvailableFrom(LocalDateTime.of(2025, 11, 10, 10, 0));
        avail1.setAvailableTo(LocalDateTime.of(2025, 11, 10, 12, 0));
        avail1.setNote("Mon morning session");
        coach1.addAvailability(avail1);

        // Coach 2
        Coach coach2 = new Coach(client1);
        coach2.setBio("Personal trainer with 5+ years of experience in weightlifting.");
        coach2.addLocation(location2);

        CoachOffer offer2 = new CoachOffer();
        offer2.setOfferType("Personal training");
        offer2.setTargetGroup("Beginners");
        offer2.setDescription("One-on-one weightlifting and fitness coaching");
        offer2.setPrice(35.0);
        offer2.setFreeOrPaid(CoachOffer.FeeType.paid);
        offer2.setStartDatetime(LocalDateTime.of(2025, 11, 11, 14, 0));
        offer2.setEndDatetime(LocalDateTime.of(2025, 11, 11, 15, 0));
        offer2.setRecurrence("Once");
        coach2.addOffer(offer2);

        CoachAvailability avail2 = new CoachAvailability();
        avail2.setAvailableFrom(LocalDateTime.of(2025, 11, 11, 14, 0));
        avail2.setAvailableTo(LocalDateTime.of(2025, 11, 11, 16, 0));
        avail2.setNote("Wed afternoon session");
        coach2.addAvailability(avail2);

        // Coach 3
        Coach coach3 = new Coach(coachProfile);
        coach3.setBio("Yoga and pilates coach for all levels.");
        coach3.addLocation(location1);
        coach3.addLocation(location2);

        CoachOffer offer3 = new CoachOffer();
        offer3.setOfferType("Yoga classes");
        offer3.setTargetGroup("All levels");
        offer3.setDescription("Morning yoga sessions for flexibility and strength.");
        offer3.setPrice(20.0);
        offer3.setFreeOrPaid(CoachOffer.FeeType.paid);
        offer3.setStartDatetime(LocalDateTime.of(2025, 11, 12, 8, 0));
        offer3.setEndDatetime(LocalDateTime.of(2025, 11, 12, 9, 0));
        offer3.setRecurrence("Weekly");
        coach3.addOffer(offer3);

        CoachAvailability avail3 = new CoachAvailability();
        avail3.setAvailableFrom(LocalDateTime.of(2025, 11, 12, 8, 0));
        avail3.setAvailableTo(LocalDateTime.of(2025, 11, 12, 10, 0));
        avail3.setNote("Thu morning session");
        coach3.addAvailability(avail3);

        // Coach 4
        Coach coach4 = new Coach(endUserProfile);
        coach4.setBio("Nutrition and lifestyle coaching.");
        coach4.addLocation(location2);

        CoachOffer offer4 = new CoachOffer();
        offer4.setOfferType("Nutrition coaching");
        offer4.setTargetGroup("Adults 25-60");
        offer4.setDescription("Diet and lifestyle guidance for healthy living");
        offer4.setPrice(30.0);
        offer4.setFreeOrPaid(CoachOffer.FeeType.paid);
        offer4.setStartDatetime(LocalDateTime.of(2025, 11, 13, 12, 0));
        offer4.setEndDatetime(LocalDateTime.of(2025, 11, 13, 13, 0));
        offer4.setRecurrence("Monthly");
        coach4.addOffer(offer4);
    }

    private void seedPartnershipCategories() {
        if (partnershipCategoryRepository.count() > 0) return;

        List<PartnershipCategory> categories = List.of(
                new PartnershipCategory("SUBSIDIES", "Subsidiërende instanties", 0),
                new PartnershipCategory("PT_FITNESS", "Personal Trainers Fitnesscentra Sportcentra", 1),
                new PartnershipCategory("HEALTHY_LIVING", "Gezond Leven Lokaal gezondheidsoverleg Ziektezorg-verstrekkers", 2),

                new PartnershipCategory("REGIONAL_GOV", "Portregio, Sportservicebedrijf, Sport Vlaanderen, Bovenlokale overheden", 3),
                new PartnershipCategory("ADVISORY_COUNCILS", "Sport(advies)raad, Jeugdraad, Ouderenraad, Gezondheidsraad", 4),
                new PartnershipCategory("EDU_RESEARCH", "Scholen, Hogescholen, Universiteiten, Onderzoekscentra, Kenniscentra", 5),
                new PartnershipCategory("ASSOCIATIONS", "Buurt-, sport-, ouderen-, jeugd-verenigingen", 6),
                new PartnershipCategory("POLITICS", "Politiek Schepen / wethouder van sport, ouderen, gezondheid, ...", 7),
                new PartnershipCategory("BUSINESSES", "Bedrijven, Campings, Parkings", 8),
                new PartnershipCategory("MUNICIPAL_SERVICES", "Dienst Sport, Jeugd, Vrije tijd, Omgeving, Werken, Technische-groen-dienst", 9),
                new PartnershipCategory("IPITUP", "Ipitup", 10),
                new PartnershipCategory("MEDIA", "Pers, Media, Communicatiedienst, Rolmodellen, Influencers", 11),
                new PartnershipCategory("HEALTHCARE", "Artsenpraktijken, Kinesistenpraktijken, Wijkgezondheidscentra, Multidisciplinaire praktijken, Bewegen op verwijzing/recept, Ziekenhuizen, Apothekers, Dienstencentra, Woonzorgcentra, Assistentiewoningen, Gezond Leven Lokaal, Gezondheidsoverleg, Ziektezorgverstrekkers", 12)
        );

        partnershipCategoryRepository.saveAll(categories);
    }

    private void seedLocationPartnerships() {
        // safe guard
        if (locationPartnershipRepository.count() > 0) return;

        List<PartnershipCategory> categories = partnershipCategoryRepository.findAll();

        // helper
        java.util.function.Function<String, PartnershipCategory> byCode =
                code -> categories.stream()
                        .filter(c -> c.getCode().equals(code))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Missing category seed: " + code));

        // Example: mark a few decided partnerships for some benches
        Location l1 = client1Locations.get(0);
        Location l2 = client1Locations.get(1);
        Location l3 = client2Locations.get(0);

        LocationPartnership p1 = new LocationPartnership(l1, byCode.apply("IPITUP"), true);
        LocationPartnership p2 = new LocationPartnership(l1, byCode.apply("MUNICIPAL_SERVICES"), true);
        LocationPartnership p3 = new LocationPartnership(l1, byCode.apply("ASSOCIATIONS"), true);

        LocationPartnership p4 = new LocationPartnership(l2, byCode.apply("SUBSIDIES"), true);
        LocationPartnership p5 = new LocationPartnership(l2, byCode.apply("EDU_RESEARCH"), true);

        LocationPartnership p6 = new LocationPartnership(l3, byCode.apply("HEALTHCARE"), true);

        locationPartnershipRepository.saveAll(List.of(p1, p2, p3, p4, p5, p6));
    }



    private void seedStakeholders() {
        Stakeholder stakeholder1 = new Stakeholder("Sportdienst Leuven", "Jan Peeters", "jan.peeters@leuven.be", "016123456", "Beleidsmedewerker");
        Stakeholder stakeholder2 = new Stakeholder("GO! Atheneum", "Sofie De Smet", "sofie.desmet@goatheneum.be", "016654321", "Directeur");
        Stakeholder stakeholder3 = new Stakeholder("Zorgcentrum De Wingerd", "Tom Willems", "tom.willems@dewingerd.be", "016987654", "Zorgcoördinator");
        Stakeholder stakeholder4 = new Stakeholder("Buurtwerking Kessel-Lo", "Anja Claes", "anja.claes@buurtwerking.be", "016112233", "Community Manager");
        Stakeholder stakeholder5 = new Stakeholder("Sportclub Sparta", "Koen Jacobs", "koen.jacobs@sparta.be", "016445566", "Trainer");

        Stakeholder stakeholder6 = new Stakeholder("Personal Trainers", "Contact Personal Trainers", "personal.trainers@example.com", "0000 000 001", "Partner");
        Stakeholder stakeholder7 = new Stakeholder("Buurtverenigingen", "Contact Buurtvereniging", "buurtvereniging@example.com", "0000 000 002", "Partner");
        Stakeholder stakeholder8 = new Stakeholder("Scholen", "Contact Scholen", "scholen@example.com", "0000 000 003", "Partner");
        Stakeholder stakeholder9 = new Stakeholder("Bedrijven & campings", "Contact Bedrijven/Campings", "bedrijven.campings@example.com", "0000 000 004", "Partner");
        Stakeholder stakeholder10 = new Stakeholder("Subsidierende instanties", "Contact Subsidies", "subsidies@example.com", "0000 000 005", "Partner");
        Stakeholder stakeholder11 = new Stakeholder("iPitup", "Contact iPitup", "info@ipitup.example.com", "0000 000 006", "Partner");
        Stakeholder stakeholder12 = new Stakeholder("Gezond Leven", "Contact Gezond Leven", "gezond.leven@example.com", "0000 000 007", "Partner");
        Stakeholder stakeholder13 = new Stakeholder("Artsenpraktijken", "Contact Artsenpraktijken", "artsenpraktijken@example.com", "0000 000 008", "Partner");

        // Link stakeholders so EVERY client has stakeholders on their benches.
        // We'll distribute them across each client's 4 benches.
        // (This keeps stakeholder definitions intact, just links them.)

        // Client 1 benches
        stakeholder1.addLocation(client1Locations.get(0));
        stakeholder2.addLocation(client1Locations.get(1));
        stakeholder3.addLocation(client1Locations.get(2));
        stakeholder4.addLocation(client1Locations.get(3));

        // Client 2 benches
        stakeholder5.addLocation(client2Locations.get(0));
        stakeholder6.addLocation(client2Locations.get(1));
        stakeholder7.addLocation(client2Locations.get(2));
        stakeholder8.addLocation(client2Locations.get(3));

        // Client 3 benches
        stakeholder9.addLocation(client3Locations.get(0));
        stakeholder10.addLocation(client3Locations.get(1));
        stakeholder11.addLocation(client3Locations.get(2));

        // Client 4 benches
        stakeholder12.addLocation(client4Locations.get(0));
        stakeholder13.addLocation(client4Locations.get(1));

        // Optional: add a couple of common stakeholders everywhere (if you want)
        // stakeholder11.addLocation(client4Locations.get(2));
        // stakeholder10.addLocation(client1Locations.get(0));

        stakeholderRepository.saveAll(List.of(
                stakeholder1, stakeholder2, stakeholder3, stakeholder4, stakeholder5,
                stakeholder6, stakeholder7, stakeholder8, stakeholder9, stakeholder10,
                stakeholder11, stakeholder12, stakeholder13
        ));
    }


    private void seedCommunicationAndProfileMessages() {
        CommunicationMessage msg1 = new CommunicationMessage();
        msg1.setTitle("Nieuwjaars Challenge Start!");
        msg1.setBody("Doe mee met onze 30-dagen sportchallenge. Dagelijks bewegen = beloning!");
        msg1.setCategory(CommunicationCategory.CHALLENGE);
        msg1.setChannels(Set.of(DeliveryChannel.APP_POPUP, DeliveryChannel.PROFILE_MESSAGE));
        msg1.setStartsAt(LocalDateTime.now().plusHours(1));
        msg1.setEndsAt(LocalDateTime.now().plusDays(30));
        msg1.setActive(true);
        msg1.setCreatedBy(adminProfile);
        msg1.setMinAge(18);
        msg1.setMaxAge(65);
        msg1.setTargetRoles(Set.of(ProfileRole.END_USER));
        communicationMessageRepository.save(msg1);

        CommunicationMessage msg2 = new CommunicationMessage();
        msg2.setTitle("Inhuldiging nieuwe locatie Leuven");
        msg2.setBody("Kom naar onze feestelijke opening met gratis workshops en demo's!");
        msg2.setCategory(CommunicationCategory.INAUGURATION);
        msg2.setChannels(Set.of(
                DeliveryChannel.APP_POPUP,
                DeliveryChannel.DEVICE_POPUP));
        msg2.setStartsAt(LocalDateTime.now().plusDays(3).withHour(10).withMinute(0));
        msg2.setEndsAt(LocalDateTime.now().plusDays(3).withHour(16).withMinute(0));
        msg2.setActive(true);
        msg2.setCreatedBy(client1);
        msg2.setExplicitRecipients(Set.of(
                coachProfile,
                endUserProfile));
        communicationMessageRepository.save(msg2);

        CommunicationMessage msg3 = new CommunicationMessage();
        msg3.setTitle("Reminder: Nieuwe groepslessen inschrijvingen openen");
        msg3.setBody("Schrijf je in voor onze voorjaars groepslessen! Beperkte plaatsen beschikbaar.");
        msg3.setCategory(CommunicationCategory.COURSE_SERIES);
        msg3.setChannels(Set.of(
                DeliveryChannel.PROFILE_MESSAGE));
        msg3.setStartsAt(LocalDateTime.now().plusHours(6));
        msg3.setActive(true);
        msg3.setCreatedBy(adminProfile);
        msg3.setTargetRoles(Set.of(ProfileRole.COACH, ProfileRole.END_USER));
        communicationMessageRepository.save(msg3);

        CommunicationMessage msg4 = new CommunicationMessage();
        msg4.setTitle("App Feedback Gevraag!");
        msg4.setBody("Help ons verbeteren door een korte vragenlijst in te vullen — duurt 1 minuut.");
        msg4.setCategory(CommunicationCategory.OTHER);
        msg4.setChannels(Set.of(
                DeliveryChannel.APP_POPUP));
        msg4.setStartsAt(LocalDateTime.now().minusHours(2));
        msg4.setEndsAt(LocalDateTime.now().plusDays(2));
        msg4.setActive(true);
        msg4.setCreatedBy(adminProfile);
        msg4.setMinAge(16);
        communicationMessageRepository.save(msg4);

        // ===== PROFILE INBOX SEEDING =====
        // msg1 -> all profiles 1–4 (who have notifications enabled by default)
        ProfileMessage pm1p1 = new ProfileMessage(adminProfile, msg1);
        ProfileMessage pm1p2 = new ProfileMessage(client1, msg1);
        ProfileMessage pm1p3 = new ProfileMessage(coachProfile, msg1);
        ProfileMessage pm1p4 = new ProfileMessage(endUserProfile, msg1);

        // msg2 -> explicit recipients 3 and 4
        ProfileMessage pm2p3 = new ProfileMessage(coachProfile, msg2);
        ProfileMessage pm2p4 = new ProfileMessage(endUserProfile, msg2);

        // msg3 -> adminProfile and coachProfile
        ProfileMessage pm3p1 = new ProfileMessage(adminProfile, msg3);
        ProfileMessage pm3p3 = new ProfileMessage(coachProfile, msg3);

        // msg4 -> adminProfile and client1
        ProfileMessage pm4p1 = new ProfileMessage(adminProfile, msg4);
        ProfileMessage pm4p2 = new ProfileMessage(client1, msg4);

        profileMessageRepository.saveAll(List.of(
                pm1p1, pm1p2, pm1p3, pm1p4,
                pm2p3, pm2p4,
                pm3p1, pm3p3,
                pm4p1, pm4p2
        ));
    }

    private void seedCompletedChecklistItems() {
        // mark some checklist items as completed at a location
        CompletedChecklistItem completion1 = new CompletedChecklistItem(location1, checklist1);
        CompletedChecklistItem completion2 = new CompletedChecklistItem(location1, checklist2);
        CompletedChecklistItem completion6 = new CompletedChecklistItem(location1, checklist6);

        completedChecklistItemRepository.saveAll(List.of(completion1, completion2, completion6));

        location1.getCompletedChecklistItems().add(completion1);
        location1.getCompletedChecklistItems().add(completion2);
        location1.getCompletedChecklistItems().add(completion6);
    }
}
