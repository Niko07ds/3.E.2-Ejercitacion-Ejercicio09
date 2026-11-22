package ejercicio09;

public class SpiSensorFactory extends SensorFactory {

    @Override
    public String getProtocol() {
        return "SPI";
    }

    @Override
    protected void validateProtocolRules(SensorConfig config) throws SensorInitializationException {
        if (config.getDeviceAddress() < 0 || config.getDeviceAddress() > 3) {
            throw new SensorInitializationException("SPI: el chip select debe estar entre 0 y 3.");
        }
        if (config.getRegisterAddress() > 255) {
            throw new SensorInitializationException("SPI: el registro debe estar entre 0 y 255.");
        }
        if (config.getSpeed() > 10_000_000) {
            throw new SensorInitializationException("SPI: la velocidad máxima es de 10 MHz.");
        }
    }

    @Override
    protected SensorChannel createChannel(SensorConfig config) {
        return new SpiChannel(config);
    }
}
