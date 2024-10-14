package tn.esprit.spring.kaddem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DepartementServiceImplTest {

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveAllDepartements() {
        // Given
        Departement depart1 = new Departement(1, "Informatique");
        Departement depart2 = new Departement(2, "Mécanique");

        List<Departement> departements = Arrays.asList(depart1, depart2);
        when(departementRepository.findAll()).thenReturn(departements);

        // When
        List<Departement> result = departementService.retrieveAllDepartements();

        // Then
        assertEquals(2, result.size());
        assertEquals(departements, result);
    }

    @Test
    public void testAddDepartement() {
        // Given
        Departement depart = new Departement(3, "Électronique");
        when(departementRepository.save(depart)).thenReturn(depart);

        // When
        Departement result = departementService.addDepartement(depart);

        // Then
        assertEquals(depart, result);
    }

    @Test
    public void testUpdateDepartement() {
        // Given
        Departement depart = new Departement(1, "Génie Civil");
        when(departementRepository.save(depart)).thenReturn(depart);

        // When
        Departement result = departementService.updateDepartement(depart);

        // Then
        assertEquals(depart, result);
    }

    @Test
    public void testRetrieveDepartement() {
        // Given
        Integer departId = 1;
        Departement depart = new Departement(departId, "Informatique");
        when(departementRepository.findById(departId)).thenReturn(Optional.of(depart));

        // When
        Departement result = departementService.retrieveDepartement(departId);

        // Then
        assertEquals(depart, result);
    }

    @Test
    public void testDeleteDepartement() {
        // Given
        Integer departId = 1;
        Departement depart = new Departement(departId, "Informatique");
        when(departementRepository.findById(departId)).thenReturn(Optional.of(depart));

        // When
        departementService.deleteDepartement(departId);

        // Then
        verify(departementRepository, times(1)).delete(depart);
    }
}

