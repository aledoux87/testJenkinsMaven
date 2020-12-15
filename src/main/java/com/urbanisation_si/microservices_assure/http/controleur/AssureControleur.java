/**
 * 
 */
package com.urbanisation_si.microservices_assure.http.controleur;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.urbanisation_si.microservices_assure.dao.AssureRepository;
import com.urbanisation_si.microservices_assure.modele.Assure;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Patrice
 *
 */
@Api(description = "API pour les opérations CRUD pour les assurés")  //Permet de documenter (swagger)   
@RestController  // et pas @Controller sinon ne traduit pas les retours des objets Java en JSON
@RequestMapping(path="/previt")  
public class AssureControleur {
	@Autowired  
	private AssureRepository assureRepository;
	
	//Pour sortir de l'information du processus (erreur, avertissement, historique, ...)
	Logger log = LoggerFactory.getLogger(this.getClass());//Création du log pour l'affichage de message dans la console

	@PostMapping(path="/ajouterAssure")
	public ResponseEntity<Void> creerAssure(@Valid @RequestBody Assure assure) {
		log.info("--------> Appel creerAssure");// Pour afficher des messages
		Assure assureAjoute = assureRepository.save(assure);

		if (assureAjoute == null)
			return ResponseEntity.noContent().build();

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(assureAjoute.getId())
				.toUri();

		return ResponseEntity.created(uri).build(); 
	}

	//*********************************
	//*    Version sans filtre
	//*********************************
	
	
	@GetMapping(path="/listerLesAssures")  
	public @ResponseBody Iterable<Assure> getAllAssures() {
		return assureRepository.findAll();
	}
	
	@ApiOperation(value = "Recherche un assuré grâce à son ID à condition que celui-ci existe.") 
	@GetMapping(path="/Assure/{id}")
	public Optional<Assure> rechercherAssureId(@PathVariable Integer id) { // @PathVariable signale que c'est le même que dans l'uri
		log.info("--------> Appel Assure");
		return assureRepository.findById(id);    
	}
	
	@GetMapping(path="/Assure/numeroAssure/{numeroAssure}")
	public List<Assure> rechercherAssureNumeroAssure(@PathVariable Long numeroAssure){
		return assureRepository.rechercherAssureNumeroAssure(numeroAssure);    
	}

	@GetMapping(path="/Assure/numeroPersonne/{numeroPersonne}")
	public List<Assure> rechercherAssureNumeroPersonne(@PathVariable Long numeroPersonne){
		return assureRepository.rechercherAssureNumeroPersonne(numeroPersonne);
	}
	
	@GetMapping(path="/Assure/nomPrenom/{nom}/{prenom}")
	public List<Assure> findByNomAndPrenom(@PathVariable String nom, @PathVariable String prenom){
		return assureRepository.findByNomAndPrenom(nom, prenom);
	}
	
	@GetMapping(path="/Assure/nomContain/{chaine}")
	public List<Assure> findByNomContaining(@PathVariable String chaine){ //On peut utiliser avec find...like et %+chaine+%"
		return assureRepository.findByNomContaining(chaine);
	}
	
	@GetMapping(path="/Assure/dateAvant/{dateNaissance}")
	public List<Assure> findByDateNaissanceBefore(@PathVariable String dateNaissance){
		Date date1 = new Date();
		try {
			date1 = new SimpleDateFormat("yyyyMMdd").parse(dateNaissance);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return assureRepository.findByDateNaissanceBefore(date1);
	}
	
	@DeleteMapping (path="/Assure/{id}")     
    public void supprimerAssurer(@PathVariable Integer id) {
     assureRepository.deleteById(id);        
    }
	
	@PutMapping (path="/modifierAssure")    
    public void modifierAssure(@RequestBody Assure assure) {

      assureRepository.save(assure);
    }
	
	
	//*********************************
	//*    Version avec filtre
	//*********************************
	
	/*
	@GetMapping(path="/listerLesAssures")  
	public MappingJacksonValue getAllAssures() {
		Iterable<Assure> assure = assureRepository.findAll();
		
		if (assure == null) {
			throw new AssureIntrouvableException("Il n'y a pas d'assurés");
		}
		else {
			//Création du filtre
			FilterProvider listeFiltree = creerFiltre("filtreDynamiqueJson", "nom");			

			List<Assure> ar = new ArrayList<Assure>();
			ar.addAll((Collection<? extends Assure>) assure);// Ici on ajoute une collection car on a tous les éléments de la table assure
			
			return filtrerAssures(ar, listeFiltree);
		}
		
	}
	
	
	@GetMapping(path="/Assure/{id}")
	public MappingJacksonValue rechercherAssureId(@PathVariable Integer id) { // @PathVariable signale que c'est le même que dans l'uri
		Optional<Assure> assure = assureRepository.findById(id);
		
		if (!assure.isPresent()) throw new AssureIntrouvableException("L'assure avec l'id =" + id + " n'existe pas!");
		
		//Création du filtre
		FilterProvider listeFiltree = creerFiltre("filtreDynamiqueJson", "null");
		
		Assure a = assure.get();
		List<Assure> ar = new ArrayList<Assure>();//Ici on ajoute un unique Assure de la table assure
		ar.add(a);
		
		return filtrerAssures(ar, listeFiltree);
	}	

	@GetMapping(path="/Assure/numeroAssure/{numeroAssure}")
	public MappingJacksonValue rechercherAssureNumeroAssure(@PathVariable Long numeroAssure){
		Iterable<Assure> assure = assureRepository.rechercherAssureNumeroAssure(numeroAssure);
		if (assure == null) {
			throw new AssureIntrouvableException("Il n'y a pas d'assurés");
		}
		else {
			//Création du filtre
			FilterProvider listeFiltree = creerFiltre("filtreDynamiqueJson", "nom");			

			List<Assure> ar = new ArrayList<Assure>();
			ar.addAll((Collection<? extends Assure>) assure);
			
			return filtrerAssures(ar, listeFiltree);
		}
	}

	@GetMapping(path="/Assure/numeroPersonne/{numeroPersonne}")
	public MappingJacksonValue rechercherAssureNumeroPersonne(@PathVariable Long numeroPersonne){
		Iterable<Assure> assure = assureRepository.rechercherAssureNumeroPersonne(numeroPersonne);
		if (assure == null) {
			throw new AssureIntrouvableException("Il n'y a pas d'assurés");
		}
		else {
			//Création du filtre
			FilterProvider listeFiltree = creerFiltre("filtreDynamiqueJson", "nom");			

			List<Assure> ar = new ArrayList<Assure>();
			ar.addAll((Collection<? extends Assure>) assure);
			
			return filtrerAssures(ar, listeFiltree);
		}
	}
	
	@GetMapping(path="/Assure/nomPrenom/{nom}/{prenom}")
	public MappingJacksonValue findByNomAndPrenom(@PathVariable String nom, @PathVariable String prenom){
		Iterable<Assure> assure = assureRepository.findByNomAndPrenom(nom, prenom);
		if (assure == null) {
			throw new AssureIntrouvableException("Il n'y a pas d'assurés");
		}
		else {
			//Création du filtre
			FilterProvider listeFiltree = creerFiltre("filtreDynamiqueJson", "nom");			

			List<Assure> ar = new ArrayList<Assure>();
			ar.addAll((Collection<? extends Assure>) assure);
			
			return filtrerAssures(ar, listeFiltree);
		}
	}
	
	@GetMapping(path="/Assure/nomContain/{chaine}")
	public MappingJacksonValue findByNomContaining(@PathVariable String chaine){
		Iterable<Assure> assure = assureRepository.findByNomContaining(chaine);
		if (assure == null) {
			throw new AssureIntrouvableException("Il n'y a pas d'assurés");
		}
		else {
			//Création du filtre
			FilterProvider listeFiltree = creerFiltre("filtreDynamiqueJson", "prenom");			

			List<Assure> ar = new ArrayList<Assure>();
			ar.addAll((Collection<? extends Assure>) assure);
			
			return filtrerAssures(ar, listeFiltree);
		}
	}
	
	
	@GetMapping(path="/Assure/dateAvant/{dateNaissance}")
	public MappingJacksonValue findByDateNaissanceBefore(@PathVariable String dateNaissance){
		Date date1 = new Date();
		try {
			date1 = new SimpleDateFormat("yyyyMMdd").parse(dateNaissance);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Iterable<Assure> assure = assureRepository.findByDateNaissanceBefore(date1);
		if (assure == null) {
			throw new AssureIntrouvableException("Il n'y a pas d'assurés");
		}
		else {
			//Création du filtre
			FilterProvider listeFiltree = creerFiltre("filtreDynamiqueJson", "nom");			

			List<Assure> ar = new ArrayList<Assure>();
			ar.addAll((Collection<? extends Assure>) assure);
			
			return filtrerAssures(ar, listeFiltree);
		}
	}
	*/

	
	/**
	 * Création d'un filtre dynamique 
	 * @param nomFiltre
	 * @param attribut
	 * @return FilterProvider
	 */
	/*public FilterProvider creerFiltre(String nomFiltre, String attribut) {
		SimpleBeanPropertyFilter unFiltre;
		if (attribut == null) {
			unFiltre = SimpleBeanPropertyFilter.serializeAll(); // si l'attribut est null je ne filtre rien donc je serialize tout
		}
		else {
			unFiltre = SimpleBeanPropertyFilter.serializeAllExcept(attribut);
		}
		return new SimpleFilterProvider().addFilter(nomFiltre, unFiltre);		
	}*/
	
	/**
	 * Renvoi de la liste filtrée
	 * @param assures
	 * @param listeFiltres
	 * @return MappingJacksonValue
	 */
	/*public MappingJacksonValue filtrerAssures(List<Assure> assures, FilterProvider listeFiltres) {
		MappingJacksonValue assuresFiltres = new MappingJacksonValue(assures);
		assuresFiltres.setFilters(listeFiltres);
		return assuresFiltres;
	}
	*/

	
}
