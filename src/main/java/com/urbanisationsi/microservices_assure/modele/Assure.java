package com.urbanisationsi.microservices_assure.modele;


import javax.persistence.Entity;

@Entity  
public class Assure extends Personne{
    
    private String dossierMedical;
    private Long numeroAssure;
    
    //Ne pas oublier //////////////
    public Assure() {
    	super();
    }
    //////////////////////////////
    
    // Getter dossierMedical
    public String getDossierMedical() {
      return dossierMedical;
    }
    // Setter dossierMedical
    public void setDossierMedical(String newDossierMedical) {
      this.dossierMedical = newDossierMedical;
    }
    
    // Getter numeroAssure 
    public Long getNumeroAssure () {
      return numeroAssure;
    }
    // Setter numeroAssure 
    public void setNumeroAssure(Long newNumeroAssure) {
      this.numeroAssure = newNumeroAssure;
    }


}
// Ne pas oublier les getter et les setter