package de.shnyder.moose.config;

import org.openapitools.model.ApiResultErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.shnyder.moose.MooseError;

@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(MooseError.class)
	public ResponseEntity handleException(MooseError e) {
		// log exception
		ApiResultErrorModel body = new ApiResultErrorModel();
		body.errorCode("errorCode" + e.get_errorCode()).errorMsg(e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
	}
}