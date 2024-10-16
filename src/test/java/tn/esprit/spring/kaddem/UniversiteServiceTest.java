package tn.esprit.spring.kaddem;

import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UniversiteServiceImplTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private DepartementRepository departementRepository; // Même si non utilisé dans addUniversite, nécessaire pour l'injection

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    @BeforeEach
    void setUp() {
        // Initialise les mocks avant chaque test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUniversite() {
        // Arrange
        Universite universite = new Universite();
        universite.setNomUniv("Université de Exemple");
        // Vous pouvez configurer d'autres propriétés si nécessaire

        // Simule le comportement de universiteRepository.save()
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        // Act
        Universite result = universiteService.addUniversite(universite);

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals("Université de Exemple", result.getNomUniv(), "Le nom de l'université doit correspondre");
        verify(universiteRepository, times(1)).save(universite);
    }
}
