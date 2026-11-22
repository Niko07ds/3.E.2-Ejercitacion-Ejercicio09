package ejercicio09;

/**
 * Creador abstracto: método de fábrica + método plantilla que valida los parámetros
 * de hardware, crea el canal y registra las métricas iniciales.
 */
public abstract class SensorFactory {

    public abstract String getProtocol();

    // Método de fábrica
    protected abstract SensorChannel createChannel(SensorConfig config)
            throws SensorInitializationException;

    // Reglas de hardware propias de cada protocolo (direcciones, velocidades, etc.)
    protected void validateProtocolRules(SensorConfig config) throws SensorInitializationException {
    }

    public final SensorChannel create(SensorConfig config, SensorMetrics metrics)
            throws SensorInitializationException {
        try {
            validateCommonRules(config);
            validateProtocolRules(config);
            SensorChannel channel = createChannel(config);
            metrics.recordCreated(getProtocol(), channel.getState());
            return channel;
        } catch (SensorInitializationException e) {
            metrics.recordFailure(getProtocol());
            throw e;
        } catch (RuntimeException e) {
            metrics.recordFailure(getProtocol());
            throw new SensorInitializationException(
                    "Error inesperado al inicializar el protocolo " + getProtocol(), e);
        }
    }

    private void validateCommonRules(SensorConfig config) throws SensorInitializationException {
        if (config == null) {
            throw new SensorInitializationException("La configuración es obligatoria.");
        }
        if (config.getPortName() == null || config.getPortName().trim().isEmpty()) {
            throw new SensorInitializationException("El nombre del puerto es obligatorio.");
        }
        if (config.getSpeed() <= 0) {
            throw new SensorInitializationException("La velocidad del bus debe ser positiva.");
        }
        if (config.getRegisterAddress() < 0) {
            throw new SensorInitializationException(
                    "La dirección de memoria no puede ser negativa: " + config.getRegisterAddress());
        }
    }
}
