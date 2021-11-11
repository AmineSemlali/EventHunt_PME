package de.pme.eventhunt.storage.repository;

public class ObjectNotFoundException extends Exception{
    public ObjectNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
