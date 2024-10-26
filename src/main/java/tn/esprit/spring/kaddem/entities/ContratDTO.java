package tn.esprit.spring.kaddem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.spring.kaddem.entities.Specialite;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContratDTO {
    private Integer idContrat;
    private Date dateDebutContrat;
    private Date dateFinContrat;
    private Specialite specialite;
    private Boolean archive;
    private Integer montantContrat;
    private Integer etudiantId; // Optional: pour stocker l'ID de l'étudiant associé, si nécessaire
}
