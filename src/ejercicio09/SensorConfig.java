package ejercicio09;

/**
 * Configuración inmutable de un canal de lectura: puerto, dirección del dispositivo,
 * dirección de memoria (registro) y velocidad del bus.
 */
public final class SensorConfig {

    private final String protocol;
    private final String portName;
    private final int deviceAddress;
    private final int registerAddress;
    private final int speed;

    public SensorConfig(String protocol, String portName, int deviceAddress, int registerAddress, int speed) {
        this.protocol = protocol;
        this.portName = portName;
        this.deviceAddress = deviceAddress;
        this.registerAddress = registerAddress;
        this.speed = speed;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getPortName() {
        return portName;
    }

    public int getDeviceAddress() {
        return deviceAddress;
    }

    public int getRegisterAddress() {
        return registerAddress;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return protocol + " en " + portName + " (dispositivo=0x" + Integer.toHexString(deviceAddress)
                + ", registro=0x" + Integer.toHexString(registerAddress) + ", velocidad=" + speed + ")";
    }
}
