/**
 * 
 */
package com.urbanisation_si.microservices_assure.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.urbanisation_si.microservices_assure.modele.Assure;

/**
 * @author Patrice
 *
 */
public interface AssureRepository extends CrudRepository<Assure, Integer> {
	
	// J2- 2
	@Query("from Assure a where a.numeroAssure = :na")
	List<Assure>  rechercherAssureNumeroAssure(@Param("na") Long numeroAssure);
	
	// J2- 3
	@Query("from Assure a where a.numeroPersonne = :np")
	List<Assure>  rechercherAssureNumeroPersonne(@Param("np") Long numeroPersonne);
	
	List<Assure> findByNumeroPersonne(Long numeroPersonne); 
	
	// J2- 4
	List<Assure> findByNomAndPrenom(String nom, String prenom);
	
	// J2- 5
	List<Assure> findByNomLike(String chaine);
	
	// J2- 6
	List<Assure> findByDateNaissanceBefore(LocalDate date);
	
	// J2- 7	
	List<Assure> findByNomContaining(String chaine);
}
