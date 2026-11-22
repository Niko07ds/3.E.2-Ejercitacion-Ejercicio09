package ejercicio09;

public class I2cChannel extends BaseSensorChannel {

    public I2cChannel(SensorConfig config) {
        super(config);
    }

    @Override
    public String getProtocol() {
        return "I2C";
    }

    @Override
    protected double doRead() {
        // Lectura simulada de un sensor de temperatura por I2C
        return 22.0 + (config.getDeviceAddress() % 8) * 0.25;
    }
}
