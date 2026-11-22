package ejercicio09;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Fábrica avanzada de sensores ambientales para IoT");

        SensorMetrics metrics = new SensorMetrics();
        SensorFactoryRegistry registry = new SensorFactoryRegistry(metrics);

        registry.register(new ModbusSensorFactory());
        registry.register(new I2cSensorFactory());

        System.out.println("\n--- Creación y lectura de sensores ---");
        try (SensorChannel modbus = registry.create(
                new SensorConfig("modbus", "COM3", 17, 100, 9600))) {
            modbus.open();
            System.out.println("MODBUS estado: " + modbus.getState());
            System.out.println("MODBUS lectura: " + String.format("%.1f", modbus.read()) + " °C");
        }

        System.out.println("\n--- Protocolo aún no registrado ---");
        SensorConfig spiConfig = new SensorConfig("spi", "/dev/spidev0.0", 1, 16, 1_000_000);
        try {
            registry.create(spiConfig);
        } catch (SensorInitializationException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Registro de una nueva factoría en tiempo de ejecución ---");
        registry.register(new SpiSensorFactory());
        try (SensorChannel spi = registry.create(spiConfig)) {
            spi.open();
            System.out.println("SPI lectura: " + String.format("%.1f", spi.read()) + " kPa");
        }

        System.out.println("\n--- Validaciones de hardware ---");
        SensorConfig[] invalidConfigs = {
                new SensorConfig("modbus", "COM4", 300, 100, 9600),
                new SensorConfig("i2c", "/dev/i2c-1", 0x80, 0, 100000),
                new SensorConfig("spi", "/dev/spidev0.1", 1, 16, 20_000_000),
                new SensorConfig("i2c", "/dev/i2c-1", 0x48, -4, 100000),
                new SensorConfig("can", "can0", 1, 0, 500000)
        };
        for (SensorConfig config : invalidConfigs) {
            try {
                registry.create(config);
            } catch (SensorInitializationException e) {
                System.out.println("Rechazado (" + config.getProtocol() + "): " + e.getMessage());
            }
        }

        System.out.println("\n--- Puerto ocupado y reapertura ---");
        try (SensorChannel busy = registry.create(
                new SensorConfig("i2c", "BUSY-/dev/i2c-2", 0x48, 0, 100000))) {
            try {
                busy.open();
            } catch (SensorInitializationException e) {
                System.out.println("Apertura inicial fallida (estado " + busy.getState() + "): " + e.getMessage());
            }
            busy.reopen(3);
            System.out.println("Estado final: " + busy.getState());
            System.out.println("I2C lectura: " + String.format("%.1f", busy.read()) + " °C");
        }

        System.out.println("\n--- Alta concurrencia: 40 canales con 8 hilos ---");
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<Double>> results = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            final int id = i;
            results.add(pool.submit(() -> {
                boolean useModbus = id % 2 == 0;
                SensorConfig config = useModbus
                        ? new SensorConfig("modbus", "COM" + (id % 4 + 1), 1 + id, id, 19200)
                        : new SensorConfig("i2c", "/dev/i2c-" + (id % 3), 0x10 + id, id, 400000);
                try (SensorChannel channel = registry.create(config)) {
                    channel.open();
                    return channel.read();
                }
            }));
        }
        int readings = 0;
        for (Future<Double> result : results) {
            result.get();
            readings++;
        }
        pool.shutdown();
        System.out.println("Lecturas obtenidas: " + readings);

        System.out.println("\n--- Métricas de estado operativo ---");
        metrics.printSummary();
    }
}
