package ejercicio09;

/**
 * Se lanza cuando un canal no puede configurarse, crearse o abrirse.
 */
public class SensorInitializationException extends Exception {

    public SensorInitializationException(String message) {
        super(message);
    }

    public SensorInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
