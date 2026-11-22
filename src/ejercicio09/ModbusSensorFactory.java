package ejercicio09;

public class ModbusSensorFactory extends SensorFactory {

    @Override
    public String getProtocol() {
        return "MODBUS";
    }

    @Override
    protected void validateProtocolRules(SensorConfig config) throws SensorInitializationException {
        if (config.getDeviceAddress() < 1 || config.getDeviceAddress() > 247) {
            throw new SensorInitializationException("ModBus: la dirección del esclavo debe estar entre 1 y 247.");
        }
        if (config.getRegisterAddress() > 65535) {
            throw new SensorInitializationException("ModBus: el registro debe estar entre 0 y 65535.");
        }
        int speed = config.getSpeed();
        if (speed != 9600 && speed != 19200 && speed != 115200) {
            throw new SensorInitializationException("ModBus: velocidad no soportada (" + speed + " baudios).");
        }
    }

    @Override
    protected SensorChannel createChannel(SensorConfig config) {
        return new ModbusChannel(config);
    }
}
