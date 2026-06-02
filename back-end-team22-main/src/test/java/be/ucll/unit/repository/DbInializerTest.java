//package be.ucll.unit.repository;
//
//import be.ucll.model.*;
//import be.ucll.model.enums.ProfileRole;
//import be.ucll.repository.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InOrder;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class DbInitializerTest {
//
//    @Mock private ProfileRepository profileRepository;
//    @Mock private PhaseRepository phaseRepository;
//    @Mock private ChecklistRepository checklistRepository;
//    @Mock private ExerciseRepository exerciseRepository;
//    @Mock private ProfileExerciseRepository profileExerciseRepository;
//    @Mock private LocationRepository locationRepository;
//    @Mock private CoachRepository coachRepository;
//    @Mock private StakeholderRepository stakeholderRepository;
//    @Mock private CompletedChecklistItemRepository completedChecklistItemRepository;
//    @Mock private CommunicationMessageRepository communicationMessageRepository;
//    @Mock private ProfileMessageRepository profileMessageRepository;
//    @Mock private FAQRepository faqRepository;
//    @Mock private WorkshopRepository workshopRepository;
//
//    @InjectMocks
//    private DbInitializer dbInitializer;
//
//    static <T> Stream<T> iteratorToFiniteStream(final Iterator<T> iterator) {
//        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
//    }
//
//    @SuppressWarnings("unchecked")
//    @BeforeEach
//    void setUp() {
//        reset(
//            profileRepository, phaseRepository, checklistRepository, exerciseRepository,
//            profileExerciseRepository, locationRepository, coachRepository,
//            stakeholderRepository, completedChecklistItemRepository,
//            communicationMessageRepository, profileMessageRepository, faqRepository, workshopRepository
//        );
//    }
//
//    @Test
//    void clearAll_DeletesAllEntitiesInCorrectOrder() {
//        // Act
//        dbInitializer.clearAll();
//
//        // Verify delete order using InOrder
//        InOrder inOrder = inOrder(
//                completedChecklistItemRepository,
//                profileExerciseRepository,
//                profileMessageRepository,
//                communicationMessageRepository,
//                checklistRepository,
//                exerciseRepository,
//                coachRepository,
//                stakeholderRepository,
//                workshopRepository,
//                locationRepository,
//                faqRepository,
//                phaseRepository,
//                profileRepository
//        );
//
//        inOrder.verify(completedChecklistItemRepository).deleteAll();
//        inOrder.verify(profileExerciseRepository).deleteAll();
//        inOrder.verify(profileMessageRepository).deleteAll();
//        inOrder.verify(communicationMessageRepository).deleteAll();
//        inOrder.verify(checklistRepository).deleteAll();
//        inOrder.verify(exerciseRepository).deleteAll();
//        inOrder.verify(coachRepository).deleteAll();
//        inOrder.verify(stakeholderRepository).deleteAll();
//        inOrder.verify(workshopRepository).deleteAll();
//        inOrder.verify(locationRepository).deleteAll();
//        inOrder.verify(faqRepository).deleteAll();
//        inOrder.verify(phaseRepository).deleteAll();
//        inOrder.verify(profileRepository).deleteAll();
//    }
//
//    @Test
//    void initialize_SavesCorrectEntities() {
//        // Act
//        dbInitializer.initialize();
//
//        // Verify FAQ creation (4 items)
//        verify(faqRepository).saveAll(argThat(list -> list.spliterator().getExactSizeIfKnown() == 4));
//
//        // Verify Profile creation (4 profiles)
//        verify(profileRepository).saveAll(argThat(list -> {
//            if (list.spliterator().getExactSizeIfKnown() != 4) return false;
//            Profile profile = ((List<Profile>)list).get(0);
//            return "Jack van Hecke".equals(profile.getName()) &&
//                   profile.getRole() == ProfileRole.END_USER;
//        }));
//
//        // Verify Exercises (2 items)
//        // verify(exerciseRepository).saveAll(argThat(list -> list.spliterator().getExactSizeIfKnown() == 2));
//
//        // Verify Phases and Checklists
//        verify(phaseRepository).saveAll(argThat(iterable -> {
//            List<Phase> list = new ArrayList<>();
//            iterable.forEach(list::add);  // convert Iterable to List
//            return list.size() == 4 &&
//                    list.stream().anyMatch(p ->
//                            "Voorbereiding".equals(p.getName()) &&
//                                    p.getChecklists().size() == 5
//                    );
//        }));
//
//
//                // Verify Locations (2 locations)
//        verify(locationRepository).saveAll(argThat(list -> {
//            List<Location> l = toList(list);
//            if (l.size() != 2) return false;
//            Location loc = l.get(0);
//            return "Central Park Bench".equals(loc.getBenchName()) &&
//                loc.getConnectedRoutes() == 5;
//        }));
//
//        // Verify Coaches (4 with offers/availability)
//        verify(coachRepository).saveAll(argThat(coaches -> {
//            List<Coach> c = toList(coaches);
//            if (c.size() != 4) return false;
//            Coach coach = c.get(0);
//            return coach.getOffers().size() == 1 &&
//                coach.getAvailability().size() == 1 &&
//                coach.getLocations().size() == 2;
//        }));
//
//        // Verify Stakeholders (13 including partners)
//        verify(stakeholderRepository).saveAll(argThat(stakeholders -> {
//            List<Stakeholder> s = toList(stakeholders);
//            return s.size() == 13 &&
//                s.stream().anyMatch(x -> "Partner".equals(x.getRole()));
//        }));
//
//        // Verify Communication Messages (4 messages)
//        //verify(communicationMessageRepository).saveAll(argThat(messages -> {
//            //List<CommunicationMessage> m = toList(messages);
//            //return m.size() == 4 &&
//            //    m.get(0).getCategory() == CommunicationCategory.CHALLENGE;
//        //}));
//
//        // Verify Completed Checklist Items (3 items)
//        //verify(completedChecklistItemRepository).saveAll(argThat(items ->
//        //    toList(items).size() == 3
//        //));
//
//        // Verify Profile Messages (should be 10)
//        //verify(profileMessageRepository).saveAll(argThat(msgs ->
//        //    toList(msgs).size() == 10
//        //));
//
//    }
//
//    private static <T> List<T> toList(Iterable<T> iterable) {
//        if (iterable == null) return Collections.emptyList();
//        return StreamSupport.stream(iterable.spliterator(), false)
//                            .collect(Collectors.toList());
//    }
//
//
//    @Test
//    void initialize_RunTwice_CallsClearAllBeforeSeeding() {
//        // Act - Initialize twice
//        dbInitializer.initialize();
//        dbInitializer.initialize();
//
//        // Verify clearAll called twice before seeding
//        verify(faqRepository, times(2)).deleteAll();
//        verify(faqRepository, times(2)).saveAll(anyList());
//    }
//}