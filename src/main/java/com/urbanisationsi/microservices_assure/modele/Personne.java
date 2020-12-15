package com.urbanisationsi.microservices_assure.modele;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity   //JPA de base
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Personne {
    @Id    
    @GeneratedValue(strategy=GenerationType.AUTO)     
    private Integer id;
    private String nom;
    private String prenom;
    private Long numeroPersonne;
    private Date dateNaissance;
    //Les colonnes sont automatiquement crée en séparant les noms composé par un "_"
    
    
    //ne pas oublier //////////////////    
    public Personne () {
    	super();
    }
    
    /* Pas utile dans notre cas
     public Personne (Integer id,String nom,String prenom,Long numeroPersonne,Date dateNaissance) {
    	super();
    	this.id = id;
    	this.nom = nom;
    	this.prenom = prenom;
    	this.numeroPersonne = numeroPersonne;
    	this.dateNaissance = dateNaissance;
    }*/
    //////////////////////////////////
    
    // Getter id
    public Integer getId() {
      return id;
    }
    // Setter id
    public void setId(Integer newId) {
      this.id = newId;
    }
    
    // Getter nom
    public String getNom() {
      return nom;
    }
    // Setter nom
    public void setNom(String newNom) {
      this.nom = newNom;
    }
    
    // Getter prenom
    public String getPrenom() {
      return prenom;
    }
    // Setter prenom
    public void setPrenom(String newPrenom) {
      this.prenom = newPrenom;
    }
    
    // Getter numeroPersonne
    public Long getNumeroPersonne() {
      return numeroPersonne;
    }
    // Setter numeroPersonne
    public void setNumeroPersonne(Long newNumeroPersonne) {
      this.numeroPersonne = newNumeroPersonne;
    }
    
    // Getter dateNaissance
    public Date getDateNaissance() {
      return dateNaissance;
    }
    // Setter dateNaissance
    public void setDateNaissance(Date newDateNaissance) {
      this.dateNaissance = newDateNaissance;
    }

}
