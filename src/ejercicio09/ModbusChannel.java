package ejercicio09;

public class ModbusChannel extends BaseSensorChannel {

    public ModbusChannel(SensorConfig config) {
        super(config);
    }

    @Override
    public String getProtocol() {
        return "MODBUS";
    }

    @Override
    protected double doRead() {
        // Lectura simulada de un registro de retención
        return 20.0 + (config.getRegisterAddress() % 10) * 0.5;
    }
}
