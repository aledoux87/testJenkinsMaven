/**
 * 
 */
package com.urbanisation_si.microservices_assure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Patrice
 * //J2- 14
 *
 */
//J2- 14
//@ResponseStatus(HttpStatus.NOT_FOUND)
//J2- 15
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AssureIntrouvableException extends RuntimeException  {

	public AssureIntrouvableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
