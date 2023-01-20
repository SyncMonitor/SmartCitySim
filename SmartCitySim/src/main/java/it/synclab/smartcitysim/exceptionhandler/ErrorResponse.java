package it.synclab.smartcitysim.exceptionhandler;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {
    private final int statusCode;
    private final String message;
    private final Instant timestamp;
    private final String exception;
    private final String path;
    private final String method;
}
