package ejercicio09;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Métricas de estado operativo por protocolo, seguras para alta concurrencia.
 */
public class SensorMetrics {

    private final ConcurrentMap<String, LongAdder> created = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, LongAdder> failed = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, LongAdder> initialStates = new ConcurrentHashMap<>();

    public void recordCreated(String protocol, ChannelState initialState) {
        created.computeIfAbsent(protocol, key -> new LongAdder()).increment();
        initialStates.computeIfAbsent(protocol + " en estado " + initialState, key -> new LongAdder())
                .increment();
    }

    public void recordFailure(String protocol) {
        failed.computeIfAbsent(protocol, key -> new LongAdder()).increment();
    }

    public void printSummary() {
        Map<String, String> lines = new TreeMap<>();
        for (Map.Entry<String, LongAdder> entry : created.entrySet()) {
            lines.put(entry.getKey(), "creados=" + entry.getValue().sum());
        }
        for (Map.Entry<String, LongAdder> entry : failed.entrySet()) {
            String previous = lines.containsKey(entry.getKey()) ? lines.get(entry.getKey()) + ", " : "";
            lines.put(entry.getKey(), previous + "fallidos=" + entry.getValue().sum());
        }
        for (Map.Entry<String, String> line : lines.entrySet()) {
            System.out.println("  " + line.getKey() + " -> " + line.getValue());
        }
        for (Map.Entry<String, LongAdder> entry : new TreeMap<>(initialStates).entrySet()) {
            System.out.println("  Estado inicial: " + entry.getKey() + " = " + entry.getValue().sum());
        }
    }
}
