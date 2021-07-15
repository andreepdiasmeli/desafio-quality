package desafio_quality.exceptions;

public class PropertyHasNoRoomsException extends RuntimeException{

    public PropertyHasNoRoomsException(Long propertyId){
        super("Property with id " + propertyId + " has no rooms.");
    }
}
