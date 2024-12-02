package TALLERHILOS;

public class Hilo3 implements Runnable {

    @Override
    public void run() {
        Atleta atleta3 = new Atleta("Carlos");
        int distanciaRecorrida = 0;

        System.out.println(atleta3.getNombre() + " comienza a correr con una velocidad de " + atleta3.getVelocidad() + " km/s.");

        for (int tiempo = 1; distanciaRecorrida < 10000; tiempo++) {
            if (MainTallerAtletas.isCarreraTerminada()) {
                return;
            }
            distanciaRecorrida += atleta3.getVelocidad();
            System.out.println("Tiempo: " + tiempo + "s | " + atleta3.getNombre() + " ha recorrido " + distanciaRecorrida + " kms.");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("La carrera fue interrumpida.");
                return;
            }
            if (distanciaRecorrida >= 10000) {
                System.out.println(atleta3.getNombre() + " ha alcanzado la meta.");
                MainTallerAtletas.setGanador(atleta3.getNombre());
                break;
            }
        }
    }
}
