package xyz.catuns.edupulse.quiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class GeminiGenerationException extends ErrorResponseException {
    public GeminiGenerationException(String message) {
        this(message, null);
    }
    public GeminiGenerationException(String message, Throwable cause) {
        super(HttpStatus.SERVICE_UNAVAILABLE, cause);
        super.setDetail(message);
    }

}
