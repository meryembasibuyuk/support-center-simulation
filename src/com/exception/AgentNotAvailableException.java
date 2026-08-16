package com.exception;

/**
 * Bir Agent'a oturum atanmaya calisilirken kapasite dolu, durum uygun degil
 * veya transfer hedefi musait degilse firlatilir.
 */
public class AgentNotAvailableException extends RuntimeException {
    public AgentNotAvailableException(String message) {
        super(message);
    }
}
