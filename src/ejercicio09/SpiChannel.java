package ejercicio09;

public class SpiChannel extends BaseSensorChannel {

    public SpiChannel(SensorConfig config) {
        super(config);
    }

    @Override
    public String getProtocol() {
        return "SPI";
    }

    @Override
    protected double doRead() {
        // Lectura simulada de un sensor de presión por SPI
        return 101.3 + (config.getRegisterAddress() % 5) * 0.1;
    }
}
