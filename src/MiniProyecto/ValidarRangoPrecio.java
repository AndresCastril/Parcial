
package MiniProyecto;

public class ValidarRangoPrecio {
    public void validarRango(double precio) throws ExcepcionRangoPrecio {
        if (precio < 50) { 
            throw new ExcepcionRangoPrecio("El precio no es razonable. Debe ser más de 50 pesos.");
        }
        System.out.println("El precio esta dentro de un rango razonable.");
    }
}
