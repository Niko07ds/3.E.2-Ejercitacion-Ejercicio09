package ejercicio09;

/**
 * Base común: gestiona el estado y el ciclo de vida (abrir, reabrir, cerrar).
 * Los métodos de estado son synchronized para ser seguros con varios hilos.
 */
public abstract class BaseSensorChannel implements SensorChannel {

    protected final SensorConfig config;
    private ChannelState state = ChannelState.CREATED;
    private int openAttempts = 0;

    protected BaseSensorChannel(SensorConfig config) {
        this.config = config;
    }

    protected abstract double doRead();

    @Override
    public synchronized ChannelState getState() {
        return state;
    }

    @Override
    public synchronized void open() throws SensorInitializationException {
        if (state == ChannelState.OPEN) {
            return;
        }
        openAttempts++;
        try {
            simulateHardware();
            state = ChannelState.OPEN;
        } catch (SensorInitializationException e) {
            state = ChannelState.FAILED;
            throw e;
        }
    }

    @Override
    public synchronized double read() {
        if (state != ChannelState.OPEN) {
            throw new IllegalStateException("El canal " + getProtocol() + " no está abierto.");
        }
        return doRead();
    }

    @Override
    public synchronized void reopen(int maxAttempts) throws SensorInitializationException {
        SensorInitializationException lastError = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("  [" + getProtocol() + "] Intento de apertura " + attempt
                        + " de " + maxAttempts);
                open();
                System.out.println("  [" + getProtocol() + "] Canal abierto correctamente.");
                return;
            } catch (SensorInitializationException e) {
                lastError = e;
                System.out.println("  [" + getProtocol() + "] Falló: " + e.getMessage());
            }
        }
        throw new SensorInitializationException(
                "No se pudo abrir el canal tras " + maxAttempts + " intentos.", lastError);
    }

    @Override
    public synchronized void close() {
        if (state == ChannelState.OPEN) {
            state = ChannelState.CLOSED;
        }
    }

    // Simulación de hardware: "FAULTY" nunca responde; "BUSY" está ocupado los primeros 2 intentos
    private void simulateHardware() throws SensorInitializationException {
        if (config.getPortName().contains("FAULTY")) {
            throw new SensorInitializationException("Puerto sin respuesta: " + config.getPortName());
        }
        if (config.getPortName().startsWith("BUSY") && openAttempts <= 2) {
            throw new SensorInitializationException("Puerto ocupado, intento " + openAttempts);
        }
    }
}
