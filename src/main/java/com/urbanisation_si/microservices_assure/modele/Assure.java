/**
 * 
 */
package com.urbanisation_si.microservices_assure.modele;

import java.time.LocalDate;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * @author Patrice
 *
 */
@Entity
// J2- 12
//@JsonIgnoreProperties(value = {"dossierMedical", "numeroAssure"})
//J2- 13
//Attention toutes les méthodes du contrôleur seront impactées.
//Il faut les modifier sinon on a une erreur à l'exécution !
//Pour le TD on ne modifie et teste que rechercherAssureId.
//
//Mettre en commentaire pour annuler les filtres, pour simplifier la suite.
//On laisse rechercherAssureId en l'état qui ne filtrera plus !
//Le dossier médical sera bien ramené dans la requête.
//@JsonFilter("filtreDynamiqueJson")
public class Assure extends Personne {

	public Assure() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Long numeroAssure;
	
	// J2- 11
//	@JsonIgnore
	private String dossierMedical;

	public Long getNumeroAssure() {
		return numeroAssure;
	}

	public void setNumeroAssure(Long numeroAssure) {
		this.numeroAssure = numeroAssure;
	}

	public String getDossierMedical() {
		return dossierMedical;
	}

	public void setDossierMedical(String dossierMedical) {
		this.dossierMedical = dossierMedical;
	}

	public Assure(Integer id, String nom, String prenom, Long numeroPersonne, LocalDate dateNaissance, Long numeroAssure,
			String dossierMedical) {
		super(id, nom, prenom, numeroPersonne, dateNaissance);
		this.numeroAssure = numeroAssure;
		this.dossierMedical = dossierMedical;
	}

	public Assure(Integer id, String nom, String prenom, Long numeroPersonne, LocalDate dateNaissance) {
		super(id, nom, prenom, numeroPersonne, dateNaissance);
		// TODO Auto-generated constructor stub
	}

	
	
	

}
