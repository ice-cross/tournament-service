package pl.ick.tournament_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.ick.tournament_service.entity.AgeGroup;
import pl.ick.tournament_service.entity.Rider;
import pl.ick.tournament_service.entity.Tournament;
import pl.ick.tournament_service.entity.TournamentRider;
import pl.ick.tournament_service.model.answer.CreateRiderAnswer;
import pl.ick.tournament_service.model.request.RiderRegistrationRequest;
import pl.ick.tournament_service.repository.RiderRepository;
import pl.ick.tournament_service.utils.mappers.RiderMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RiderServiceTest {
    @Mock
    private RiderRepository riderRepository;

    @Mock
    private AgeGroupService ageGroupService;

    @Mock
    private RiderMapper riderMapper;

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private RiderService riderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterRiderSuccessfully() {
        // given
        RiderRegistrationRequest request = new RiderRegistrationRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBirthDate(LocalDate.of(2005, 5, 20));
        request.setEventName("Summer Cup");

        AgeGroup ageGroup = new AgeGroup();
        ageGroup.setId(1L);

        Rider rider = new Rider();
        rider.setId(10L);
        rider.setAgeGroup(ageGroup);

        Tournament tournament = new Tournament();
        tournament.setId(100L);

        when(riderRepository.findByLastNameAndBirthDate("Doe", LocalDate.of(2005, 5, 20)))
                .thenReturn(Optional.empty());
        when(ageGroupService.getCorrespondingAgeGroup(LocalDate.of(2005, 5, 20)))
                .thenReturn(ageGroup);
        when(riderMapper.toEntity(any(), eq(ageGroup), isNull())).thenReturn(rider);
        when(riderRepository.save(any(Rider.class))).thenReturn(rider);
        when(tournamentService.findTournamentByEventAndGroup("Summer Cup", ageGroup))
                .thenReturn(tournament);

        // when
        CreateRiderAnswer result = this.riderService.registerRider(request);

        // then
        assertNotNull(result);
        assertEquals("Rider registered successfully", result.message());
        assertEquals(10L, result.id());
        verify(riderRepository, atLeastOnce()).save(any(Rider.class));
        verify(tournamentService).recalculateTournamentType(100L);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenErrorOccurs() {
        // given
        RiderRegistrationRequest request = new RiderRegistrationRequest();
        request.setFirstName("Error");
        request.setLastName("Case");
        request.setBirthDate(LocalDate.of(2010, 1, 1));
        request.setEventName("Winter Cup");

        when(riderRepository.findByLastNameAndBirthDate(any(), any()))
                .thenThrow(new RuntimeException("Database failure"));

        // when + then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                riderService.registerRider(request)
        );

        assertTrue(exception.getMessage().contains("Error during rider registration"));
    }

    @Test
    void shouldNotDuplicateTournamentRegistration() {
        // given
        RiderRegistrationRequest request = new RiderRegistrationRequest();
        request.setFirstName("Anna");
        request.setLastName("Smith");
        request.setBirthDate(LocalDate.of(2008, 3, 15));
        request.setEventName("Championship");

        AgeGroup ageGroup = new AgeGroup();
        ageGroup.setId(2L);

        Tournament tournament = new Tournament();
        tournament.setId(200L);

        Rider rider = new Rider();
        rider.setId(20L);
        rider.setAgeGroup(ageGroup);
        TournamentRider existingTr = new TournamentRider();
        existingTr.setTournament(tournament);
        rider.setTournamentRiders(new HashSet<>());
        rider.getTournamentRiders().add(existingTr);

        when(riderRepository.findByLastNameAndBirthDate("Smith", LocalDate.of(2008, 3, 15)))
                .thenReturn(Optional.of(rider));
        when(tournamentService.findTournamentByEventAndGroup("Championship", ageGroup))
                .thenReturn(tournament);

        // when
        CreateRiderAnswer result = this.riderService.registerRider(request);

        // then
        assertEquals("Rider registered successfully", result.message());
        verify(riderRepository, never()).save(rider); // no new save, already registered
        verify(tournamentService).recalculateTournamentType(200L);
    }
}