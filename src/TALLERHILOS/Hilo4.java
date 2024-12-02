package TALLERHILOS;

public class Hilo4 implements Runnable {
    @Override
    public void run() {
        Atleta atleta4 = new Atleta("Laura");
        int distanciaRecorrida = 0;

        System.out.println(atleta4.getNombre() + " comienza a correr con una velocidad de " + atleta4.getVelocidad() + " km/s.");

        for (int tiempo = 1; distanciaRecorrida < 10000; tiempo++) {
            if (MainTallerAtletas.isCarreraTerminada()) {
                return;
            }
            distanciaRecorrida += atleta4.getVelocidad();
            System.out.println("Tiempo: " + tiempo + "s | " + atleta4.getNombre() + " ha recorrido " + distanciaRecorrida + " kms.");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("La carrera fue interrumpida.");
                return;
            }
            if (distanciaRecorrida >= 10000) {
                System.out.println(atleta4.getNombre() + " ha alcanzado la meta.");
                MainTallerAtletas.setGanador(atleta4.getNombre());
                break;
            }
        }
    }
}

