package ejercicio09;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Punto centralizado de creación. Permite registrar nuevas factorías
 * en tiempo de ejecución sin modificar el código existente (Open/Closed).
 */
public class SensorFactoryRegistry {

    private final ConcurrentMap<String, SensorFactory> factories = new ConcurrentHashMap<>();
    private final SensorMetrics metrics;

    public SensorFactoryRegistry(SensorMetrics metrics) {
        this.metrics = metrics;
    }

    public void register(SensorFactory factory) {
        String key = factory.getProtocol().toUpperCase();
        if (factories.putIfAbsent(key, factory) != null) {
            throw new IllegalStateException("Ya existe una factoría para el protocolo " + key);
        }
        System.out.println("Factoría registrada: " + key);
    }

    public void unregister(String protocol) {
        factories.remove(protocol.toUpperCase());
    }

    public SensorChannel create(SensorConfig config) throws SensorInitializationException {
        SensorFactory factory = factories.get(config.getProtocol().toUpperCase());
        if (factory == null) {
            throw new SensorInitializationException(
                    "No hay factoría registrada para el protocolo: " + config.getProtocol());
        }
        return factory.create(config, metrics);
    }
}
