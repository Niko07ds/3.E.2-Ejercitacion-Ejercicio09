package ejercicio09;

/**
 * Ciclo de vida de un canal de lectura de sensores.
 */
public enum ChannelState {
    CREATED,
    OPEN,
    CLOSED,
    FAILED
}
