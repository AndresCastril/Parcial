
package TALLERHILOS;


public class Hilo1 implements Runnable {
    

    @Override
    public void run() {
        Atleta atleta1 = new Atleta("Oscar");
        int distanciaRecorrida = 0;

        System.out.println(atleta1.getNombre() + " comienza a correr con una velocidad de " + atleta1.getVelocidad() + " km/s.");

        for (int tiempo = 1; distanciaRecorrida < 10000; tiempo++) {
            if (MainTallerAtletas.isCarreraTerminada()) {
                return;
            }
            distanciaRecorrida += atleta1.getVelocidad();
            System.out.println("Tiempo: " + tiempo + "s | " + atleta1.getNombre() + " ha recorrido " + distanciaRecorrida + " kms.");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("La carrera fue interrumpida.");
                return;
            }
            if (distanciaRecorrida >= 10000) {
                System.out.println(atleta1.getNombre() + " ha alcanzado la meta.");
                MainTallerAtletas.setGanador(atleta1.getNombre());  // Establece al ganador
                break;  
            }
        }

       
    }

}
