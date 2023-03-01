package be.technobel.ylorth.reservastock_rest.controller;

import be.technobel.ylorth.reservastock_rest.model.dto.ErrorDTO;
import be.technobel.ylorth.reservastock_rest.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFound(NotFoundException ex, HttpServletRequest req){

        ErrorDTO errorDTO = ErrorDTO.builder()
                .status( HttpStatus.NOT_FOUND )
                .message( ex.getMessage() )
                .requestMadeAt( LocalDateTime.now() )
                .URI( req.getRequestURI() )
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );

//        return new ResponseEntity<>(errorDTO, headers, HttpStatus.NOT_FOUND);
        return ResponseEntity.status( HttpStatus.NOT_FOUND )
                .headers( headers )
                .body( errorDTO );

    }

}
