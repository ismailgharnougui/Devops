package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllEtudiants() {
        List<Etudiant> etudiants = Arrays.asList(new Etudiant(), new Etudiant());
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    public void testAddEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant addedEtudiant = etudiantService.addEtudiant(etudiant);
        assertNotNull(addedEtudiant);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    public void testUpdateEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant updatedEtudiant = etudiantService.updateEtudiant(etudiant);
        assertNotNull(updatedEtudiant);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    public void testRetrieveEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));

        Etudiant foundEtudiant = etudiantService.retrieveEtudiant(1);
        assertNotNull(foundEtudiant);
        verify(etudiantRepository, times(1)).findById(1);
    }

    @Test
    public void testRemoveEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));

        etudiantService.removeEtudiant(1);
        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    public void testAssignEtudiantToDepartement() {
        Etudiant etudiant = new Etudiant();
        Departement departement = new Departement();

        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        when(departementRepository.findById(2)).thenReturn(Optional.of(departement));

        etudiantService.assignEtudiantToDepartement(1, 2);
        assertEquals(departement, etudiant.getDepartement());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    //@Test
    //public void testAddAndAssignEtudiantToEquipeAndContract() {
        //Etudiant etudiant = new Etudiant();
        //etudiant.setNomE("Mariem");
       // etudiant.setPrenomE("khamessi");
       // Contrat contrat = new Contrat();
       // Equipe equipe = new Equipe();
       // equipe.setEtudiants(new HashSet<>());
        //Contrat contrat = new Contrat(1, new Date(), new Date(), Specialite.CLOUD, false, 3000);
        //Equipe equipe = new equipe(2,"Team", Niveau.JUNIOR);

       // when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        //when(equipeRepository.findById(2)).thenReturn(Optional.of(equipe));
      // when(etudiantRepository.findByNomEAndPrenomE("Mariem", "khamessi")).thenReturn(etudiant);

        //Etudiant result = etudiantService.addAndAssignEtudiantToEquipeAndContract("Mariem","khamessi", 1, 2);
        //assertNotNull(result);
        //assertEquals(etudiant, contrat.getEtudiant());
        //assertTrue(equipe.getEtudiants().contains(etudiant));
   // }
  

    @Test
    public void testGetEtudiantsByDepartement() {
        List<Etudiant> etudiants = Arrays.asList(new Etudiant(), new Etudiant());
        when(etudiantRepository.findEtudiantsByDepartement_IdDepart(1)).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.getEtudiantsByDepartement(1);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(etudiantRepository, times(1)).findEtudiantsByDepartement_IdDepart(1);
    }
}
