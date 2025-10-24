package org.sound.exception;

public class InternalSoundAnalysisException extends RuntimeException {

    public InternalSoundAnalysisException(String message) {
        super(message);
    }

    public InternalSoundAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
