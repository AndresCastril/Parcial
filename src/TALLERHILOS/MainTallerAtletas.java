package TALLERHILOS;

public class MainTallerAtletas {

    private static boolean carreraTerminada = false;
    private static String ganador = "";

    public static void main(String[] args) {
        Hilo1 hilo1 = new Hilo1();
        Hilo2 hilo2 = new Hilo2();
        Hilo3 hilo3 = new Hilo3();
        Hilo4 hilo4 = new Hilo4();
        Hilo5 hilo5 = new Hilo5();
        Thread thread1 = new Thread(hilo1);
        Thread thread2 = new Thread(hilo2);
        Thread thread3 = new Thread(hilo3);
        Thread thread4 = new Thread(hilo4);
        Thread thread5 = new Thread(hilo5);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();

        } catch (InterruptedException ex) {
            System.out.println("Error en uno de los hilos" + "");
        }

        if (!ganador.isEmpty()) {
            System.out.println("El ganador es: " + ganador + "!");
        } else {
            System.out.println("No hubo ganador.");
        }
    }

    public static synchronized void setGanador(String nombre) {
        if (!carreraTerminada) {
            ganador = nombre;
            carreraTerminada = true;
        }
    }

    public static synchronized boolean isCarreraTerminada() {
        return carreraTerminada;

    }

}
