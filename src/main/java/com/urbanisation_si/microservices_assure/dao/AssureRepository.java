/**
 * 
 */
package com.urbanisation_si.microservices_assure.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.urbanisation_si.microservices_assure.modele.Assure;

/**
 * @author Patrice
 *
 */
//interface qui hérite de CrudRepository
public interface AssureRepository extends CrudRepository<Assure, Integer> {
	
	/** JPQL**/
	@Query("from Assure a where a.numeroAssure = :na ")
	public List<Assure> rechercherAssureNumeroAssure(@Param("na") Long numeroAssure);
	
	@Query("from Assure a where a.numeroPersonne = :na ")
	public List<Assure> rechercherAssureNumeroPersonne(@Param("na") Long numeroPersonne);
	

	public List<Assure> findByNomAndPrenom(String nom, String prenom);
	
	public List<Assure> findByNomContaining(String nom);
	
	public List<Assure> findByDateNaissanceBefore(Date dateNaissance);
}
