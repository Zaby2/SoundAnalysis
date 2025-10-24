package org.sound.exception;

public class InternalSoundStreamingException extends RuntimeException {

    public InternalSoundStreamingException(String message) {
        super(message);
    }

    public InternalSoundStreamingException(String message, Throwable cause) {
        super(message, cause);
    }
}
