package com.sdsu.edu.cms.cmsservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConferenceNotFoundException extends  RuntimeException {
    public ConferenceNotFoundException(String message) {
        super(message);
    }
}
