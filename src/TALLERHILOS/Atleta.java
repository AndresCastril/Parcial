package TALLERHILOS;

import java.util.Random;

public class Atleta {

    private String nombre;
    private double velocidad;
    
    public Atleta(String nombre) {
        this.nombre = nombre;
        this.velocidad = new Random().nextInt(1000) + 1;;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }
    

}
