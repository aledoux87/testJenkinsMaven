/**
 * 
 */
package com.urbanisation_si.microservices_assure.http.controleur;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
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

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.urbanisation_si.microservices_assure.configuration.ApplicationPropertiesConfiguration;
import com.urbanisation_si.microservices_assure.dao.AssureRepository;
import com.urbanisation_si.microservices_assure.exceptions.AssureIntrouvableException;
import com.urbanisation_si.microservices_assure.modele.Assure;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Patrice
 *
 */
//J3- 8
@Api(description = "API pour les opérations CRUD pour les assurés")
@RestController  // et pas @Controller sinon ne traduit pas les retours des objets Java en JSON
@RequestMapping(path="/previt")  
public class AssureControleur implements HealthIndicator {
	@Autowired  
	private AssureRepository assureRepository;
	@Autowired    
    ApplicationPropertiesConfiguration appProperties; 

	// J2 - 0
	Logger log = LoggerFactory.getLogger(this.getClass());

	// J2 - 1
	@GetMapping(path="/Assure/{id}")
	//	public Optional<Assure> rechercherAssureId(@PathVariable Integer id) {
	//J3- 8
	@ApiOperation(value = "Recherche un assuré grâce à son ID à condition que celui-ci existe.")
	public MappingJacksonValue  rechercherAssureId(@PathVariable Integer id) {
		//		return assureRepository.findById(id);
		//J2- 13

		Optional<Assure> assure =  assureRepository.findById(id);

		//J2- 14
		if (!assure.isPresent()) throw new AssureIntrouvableException("L'assure avec l'id " + id + " n'existe pas !"); 

		FilterProvider listeFiltres = creerFiltre("filtreDynamiqueJson", "dossierMedical");

		Assure a = assure.get();
		ArrayList<Assure> ar = new ArrayList<Assure>();
		ar.add(assure.get());

		return filtrerAssures(ar, listeFiltres);
	}

	// J2 - 2
	@GetMapping(path="/Assure/numeroAssure/{numeroAssure}")
	public List<Assure> rechercherAssureNumeroAssure(@PathVariable Long numeroAssure) {

		List<Assure> assures =  assureRepository.rechercherAssureNumeroAssure(numeroAssure);

		//		if (assures.isEmpty()) throw new AssureIntrouvableException("L'assure avec le numero " + numeroAssure + " n'existe pas !"); 

		log.info("--------------------------- Récupération de l'assuré avec numéro assuré = {}", numeroAssure);

		return assures;
	}


	@PostMapping(path="/ajouterAssure")
	// J3- 4 @Valid
	public ResponseEntity<Void> creerAssure(@Valid @RequestBody Assure assure) {
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

	// Pas besoin d'utiliser @ResponseBody pour la valeur de  retour dans un @RestController, Spring le fait par défaut !  
	@GetMapping(path="/listerLesAssures")  
	public @ResponseBody Iterable<Assure> getAllAssures() {
		
		log.info("--------------------------- Liste des assurés");
		
		Iterable<Assure> assuresIterable = assureRepository.findAll();
        List assuresList = StreamSupport 
                .stream(assuresIterable.spliterator(), false) 
                .collect(Collectors.toList()); 
        List<Assure> listeLimitee = assuresList.subList(0, appProperties.getLimiteNombreAssure());
        return listeLimitee;
	}

	// J2- 3
	@GetMapping(path="/Assure/numeroPersonne/{numeroPersonne}")
	public List<Assure> rechercherAssureNumeroPersonne(@PathVariable Long numeroPersonne) {
		return assureRepository.findByNumeroPersonne(numeroPersonne);    
	}

	// J2- 4
	@GetMapping(path="/rechercherAssureNomPrenom/{nom}/{prenom}")
	public List<Assure> rechercherAssureNomPrenom(@PathVariable String nom, @PathVariable String prenom) {
		
		log.info("--------------------------- Recherche assurés par nom et prénom");
		
		List<Assure> assuresList  = assureRepository.findByNomAndPrenom(nom, prenom);
		return assuresList;

	}

	// J2- 5
	@GetMapping(path="/Assure/nomLike/{chaine}")
	public List<Assure>  rechercherAssureContientChaine(@PathVariable String chaine) {

		return  assureRepository.findByNomLike("%"+chaine+"%");

	}

	// J2- 6
	@GetMapping(path="/Assure/avantDateNaissance/{sdate}")
	public List<Assure> rechercherAssureAvantDateNaissance
	(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate sdate) {

		return assureRepository.findByDateNaissanceBefore(sdate);

	}

	//	@GetMapping(path="/Assure/avantDateNaissance/{sdate}")
	//	public List<Assure> rechercherAssureAvantDateNaissance(@PathVariable String sdate) throws ParseException {
	//		
	//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	//		Date date = df.parse(sdate);
	//		
	//		return assureRepository.findByDateNaissanceBefore(date);
	//
	//	}

	// J2- 7
	@GetMapping(path="/Assure/nomContient/{chaine}")
	public List<Assure>  rechercherAssureContientChaine2(@PathVariable String chaine) {

		return  assureRepository.findByNomContaining(chaine);

	}


	// J3- 1
	@DeleteMapping (path="/Assure/{id}")     
	public void supprimerAssurer(@PathVariable Integer id) {
		assureRepository.deleteById(id);        
	}

	// J3- 2
	@PutMapping (path="/modifierAssure")    
	public void modifierAssure(@RequestBody Assure assure) {

		assureRepository.save(assure);
	}

	//J2- 13
	public FilterProvider creerFiltre(String nomDuFiltre, String attribut) {

		SimpleBeanPropertyFilter unFiltre;
		if (attribut != null) {
			unFiltre = SimpleBeanPropertyFilter.serializeAllExcept(attribut);
		}
		else {
			unFiltre = SimpleBeanPropertyFilter.serializeAll();
		}

		return new SimpleFilterProvider().addFilter(nomDuFiltre, unFiltre);
	}

	//J2- 13
	public MappingJacksonValue filtrerAssures(List<Assure> assures, FilterProvider listeFiltres) {

		MappingJacksonValue assuresFiltres = new MappingJacksonValue(assures);

		assuresFiltres.setFilters(listeFiltres);

		return assuresFiltres;
	}
	
	@Override
	public Health health() {
		return Health.down().build();
	}

}
