package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipeServiceTest {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    private Equipe equipeJunior;
    private Etudiant etudiant;
    private Contrat contrat1;
    private Contrat contrat2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        equipeJunior = new Equipe();
        equipeJunior.setNiveau(Niveau.JUNIOR);

        etudiant = new Etudiant();
        contrat1 = new Contrat();
        contrat2 = new Contrat();

        contrat1.setDateFinContrat(new java.util.Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 2)); // 2 years ago
        contrat1.setArchive(false);
        contrat2.setDateFinContrat(new java.util.Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 2)); // 2 years ago
        contrat2.setArchive(false);

        Set<Contrat> contrats = new HashSet<>(Arrays.asList(contrat1, contrat2));
        etudiant.setContrats(contrats);

        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant);
        equipeJunior.setEtudiants(etudiants);
    }

    @Test
    void testRetrieveAllEquipes() {
        // Mocking the findAll() method to return a list with 2 equipes
        Equipe equipeSenior = new Equipe();
        equipeSenior.setNiveau(Niveau.SENIOR);
        List<Equipe> equipes = Arrays.asList(equipeJunior, equipeSenior);

        when(equipeRepository.findAll()).thenReturn(equipes);

        // Call the method under test
        List<Equipe> result = equipeService.retrieveAllEquipes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void testAddEquipe() {
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipeJunior);
        Equipe addedEquipe = equipeService.addEquipe(equipeJunior);
        assertNotNull(addedEquipe);
        assertEquals(equipeJunior, addedEquipe);
        verify(equipeRepository, times(1)).save(equipeJunior);
    }

    @Test
    void testDeleteEquipe() {
        when(equipeRepository.findById(1)).thenReturn(java.util.Optional.of(equipeJunior));
        equipeService.deleteEquipe(1);
        verify(equipeRepository, times(1)).delete(equipeJunior);
    }

    @Test
    void testUpdateEquipe() {
        when(equipeRepository.save(equipeJunior)).thenReturn(equipeJunior);
        Equipe updatedEquipe = equipeService.updateEquipe(equipeJunior);
        assertEquals(equipeJunior, updatedEquipe);
        verify(equipeRepository, times(1)).save(equipeJunior);
    }

    @Test
    public void evoluerEquipes() {
        // Ensure that findAll returns a List
        List<Equipe> equipes = (List<Equipe>) equipeRepository.findAll();  // Assuming findAll() returns a List<Equipe>

        for (Equipe equipe : equipes) {
            if (equipe.getNiveau() == Niveau.JUNIOR) {
                equipe.setNiveau(Niveau.SENIOR);
                equipeRepository.save(equipe);
            }
        }
    }
}
