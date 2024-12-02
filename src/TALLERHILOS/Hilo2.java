package TALLERHILOS;

public class Hilo2 implements Runnable {

    @Override
    public void run() {
        Atleta atleta2 = new Atleta("Ana");
        int distanciaRecorrida = 0;

        System.out.println(atleta2.getNombre() + " comienza a correr con una velocidad de " + atleta2.getVelocidad() + " km/s.");

        for (int tiempo = 1; distanciaRecorrida < 10000; tiempo++) {
            if (MainTallerAtletas.isCarreraTerminada()) {
                return;
            }
            distanciaRecorrida += atleta2.getVelocidad();
            System.out.println("Tiempo: " + tiempo + "s | " + atleta2.getNombre() + " ha recorrido " + distanciaRecorrida + " kms.");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("La carrera fue interrumpida.");
                return;
            }
            if (distanciaRecorrida >= 10000) {
                System.out.println(atleta2.getNombre() + " ha alcanzado la meta.");
                MainTallerAtletas.setGanador(atleta2.getNombre());
                break;
            }
        }
    }
}
