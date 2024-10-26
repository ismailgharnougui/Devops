package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContratServiceImplTest {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddContrat() {
        Contrat contrat = new Contrat(new Date(), new Date(), Specialite.CLOUD, false, 2000);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat addedContrat = contratService.addContrat(contrat);
        assertNotNull(addedContrat);
        assertEquals(Specialite.CLOUD, addedContrat.getSpecialite());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    public void testUpdateContrat() {
        Contrat contrat = new Contrat(1, new Date(), new Date(), Specialite.IA, false, 3000);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat updatedContrat = contratService.updateContrat(contrat);
        assertNotNull(updatedContrat);
        assertEquals(Specialite.IA, updatedContrat.getSpecialite());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    public void testRetrieveContrat() {
        Contrat contrat = new Contrat(1, new Date(), new Date(), Specialite.RESEAUX, false, 4000);
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        Contrat foundContrat = contratService.retrieveContrat(1);
        assertNotNull(foundContrat);
        assertEquals(4000, foundContrat.getMontantContrat());
        verify(contratRepository, times(1)).findById(1);
    }

    @Test
    public void testRemoveContrat() {
        Contrat contrat = new Contrat(1, new Date(), new Date(), Specialite.SECURITE, false, 2500);
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        
        contratService.removeContrat(1);
        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    public void testAffectContratToEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomE("Dupont");
        etudiant.setPrenomE("Jean");
        etudiant.setContrats(new HashSet<>());

        Contrat contrat = new Contrat(1, new Date(), new Date(), Specialite.CLOUD, false, 3000);

        when(etudiantRepository.findByNomEAndPrenomE("Dupont", "Jean")).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(1)).thenReturn(contrat);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat result = contratService.affectContratToEtudiant(1, "Dupont", "Jean");
        assertNotNull(result);
        assertEquals(etudiant, result.getEtudiant());
        verify(contratRepository, times(1)).save(contrat);
    }
}
