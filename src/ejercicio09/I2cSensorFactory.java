package ejercicio09;

public class I2cSensorFactory extends SensorFactory {

    @Override
    public String getProtocol() {
        return "I2C";
    }

    @Override
    protected void validateProtocolRules(SensorConfig config) throws SensorInitializationException {
        if (config.getDeviceAddress() < 0x03 || config.getDeviceAddress() > 0x77) {
            throw new SensorInitializationException("I2C: la dirección de 7 bits debe estar entre 0x03 y 0x77.");
        }
        if (config.getRegisterAddress() > 255) {
            throw new SensorInitializationException("I2C: el registro debe estar entre 0 y 255.");
        }
        int speed = config.getSpeed();
        if (speed != 100000 && speed != 400000) {
            throw new SensorInitializationException("I2C: velocidad no soportada (" + speed + " Hz).");
        }
    }

    @Override
    protected SensorChannel createChannel(SensorConfig config) {
        return new I2cChannel(config);
    }
}
