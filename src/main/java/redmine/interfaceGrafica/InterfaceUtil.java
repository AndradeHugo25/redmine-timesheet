package redmine.interfaceGrafica;

public class InterfaceUtil {

    public static boolean isInterfaceClosed(DefaultGUI novaGui) {
        boolean executou = false;
        while (!executou) {
            try {
                synchronized (novaGui.getThreadB()) {
                    try {
                        novaGui.getThreadB().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    executou = true;
                }
            } catch (Exception ignored) {
            }
        }
        return true;
    }
}
