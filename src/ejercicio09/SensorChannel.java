package ejercicio09;

/**
 * Producto: contrato común de los canales de lectura de sensores.
 */
public interface SensorChannel extends AutoCloseable {

    String getProtocol();

    ChannelState getState();

    void open() throws SensorInitializationException;

    // Lee el valor del sensor (por ejemplo, una temperatura)
    double read();

    // Reintenta abrir el canal hasta agotar los intentos
    void reopen(int maxAttempts) throws SensorInitializationException;

    @Override
    void close();
}
