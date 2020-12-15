/**
 * 
 */
package com.urbanisation_si.microservices_assure;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.urbanisation_si.microservices_assure.dao.AssureRepository;
import com.urbanisation_si.microservices_assure.modele.Assure;

/**
 * @author Patrice
 * 	// J2- 10
 *
 */
@Component
public class InitialisationAssure implements ApplicationRunner  {
	
	@Autowired  
	private AssureRepository assureRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
//		Assure a1 = new Assure();
//		a1.setNumeroPersonne(100L);
//		a1.setNumeroAssure(1000L);
//		a1.setNom("Martin");
//		a1.setPrenom("Marie");
//		a1.setDateNaissance(LocalDate.of(2000,Month.MARCH,29));
//		a1.setDossierMedical("Asthmatique");
//		assureRepository.save(a1);
//		
//		Assure a2 = new Assure();
//		a2.setNumeroPersonne(200L);
//		a2.setNumeroAssure(2000L);
//		a2.setNom("Martin");
//		a2.setPrenom("Marine");
//		a2.setDateNaissance(LocalDate.of(1990,Month.JANUARY,19));
//		a2.setDossierMedical("Néant");
//		assureRepository.save(a2);
		
	}

}
