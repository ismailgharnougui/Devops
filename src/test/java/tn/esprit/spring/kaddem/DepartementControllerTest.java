package tn.esprit.spring.kaddem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.kaddem.controllers.DepartementRestController;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.services.IDepartementService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DepartementControllerTest {

    @Mock
    private IDepartementService departementService;

    @InjectMocks
    private DepartementRestController departementController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(departementController).build();
    }

    // Test pour récupérer tous les départements
    @Test
    public void testGetAllDepartements() throws Exception {
        Departement departement1 = new Departement(1, "Informatique");
        Departement departement2 = new Departement(2, "Mécanique");

        List<Departement> departements = Arrays.asList(departement1, departement2);
        when(departementService.retrieveAllDepartements()).thenReturn(departements);

        mockMvc.perform(get("/departement/retrieve-all-departements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nomDepart").value("Informatique"))
                .andExpect(jsonPath("$[1].nomDepart").value("Mécanique"));
    }

    // Test pour récupérer un département par ID
    @Test
    public void testGetDepartementById() throws Exception {
        Integer departementId = 1;
        Departement departement = new Departement(departementId, "Informatique");

        when(departementService.retrieveDepartement(departementId)).thenReturn(departement);

        mockMvc.perform(get("/departement/retrieve-departement/{departement-id}", departementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nomDepart").value("Informatique"));
    }

    // Test pour ajouter un département
    @Test
    public void testAddDepartement() throws Exception {
        Departement departement = new Departement(null, "Biologie");

        when(departementService.addDepartement(any(Departement.class))).thenReturn(departement);

        mockMvc.perform(post("/departement/add-departement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(departement)))
                .andExpect(status().isOk()) // Statut attendu est 200 OK
                .andExpect(jsonPath("$.nomDepart").value("Biologie"));
    }

    // Test pour supprimer un département par ID
    @Test
    public void testDeleteDepartement() throws Exception {
        Integer departementId = 1;

        doNothing().when(departementService).deleteDepartement(departementId);

        mockMvc.perform(delete("/departement/remove-departement/{departement-id}", departementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Statut attendu est 200 OK

        verify(departementService, times(1)).deleteDepartement(departementId);
    }

    // Test pour mettre à jour un département
    @Test
    public void testUpdateDepartement() throws Exception {
        Departement departement = new Departement(1, "Chimie");

        when(departementService.updateDepartement(any(Departement.class))).thenReturn(departement);

        mockMvc.perform(put("/departement/update-departement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(departement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDepart").value("Chimie"));
    }
}
