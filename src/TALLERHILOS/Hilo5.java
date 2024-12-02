package TALLERHILOS;

public class Hilo5 implements Runnable {
    @Override
    public void run() {
        Atleta atleta5 = new Atleta("Luis");
        int distanciaRecorrida = 0;

        System.out.println(atleta5.getNombre() + " comienza a correr con una velocidad de " + atleta5.getVelocidad() + " km/s.");

        for (int tiempo = 1; distanciaRecorrida < 10000; tiempo++) {
            if (MainTallerAtletas.isCarreraTerminada()) {
                return;
            }
            distanciaRecorrida += atleta5.getVelocidad();
            System.out.println("Tiempo: " + tiempo + "s | " + atleta5.getNombre() + " ha recorrido " + distanciaRecorrida + " kms.");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("La carrera fue interrumpida.");
                return;
            }
            if (distanciaRecorrida >= 10000) {
                System.out.println(atleta5.getNombre() + " ha alcanzado la meta.");
                MainTallerAtletas.setGanador(atleta5.getNombre());
                break;
            }
        }
    }
}
