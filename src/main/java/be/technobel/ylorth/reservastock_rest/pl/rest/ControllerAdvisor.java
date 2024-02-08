package be.technobel.ylorth.reservastock_rest.pl.rest;

import be.technobel.ylorth.reservastock_rest.pl.models.Error;
import be.technobel.ylorth.reservastock_rest.bll.exception.NotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Error> handleResourceNotFound(NotFoundException ex, HttpServletRequest req) {

        Error error = Error.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .requestMadeAt(LocalDateTime.now())
                .URI(req.getRequestURI())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(headers)
                .body(error);

    }

}