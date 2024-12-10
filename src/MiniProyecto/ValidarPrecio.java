
package MiniProyecto;

public class ValidarPrecio {

    
    public void validarNumero(String precio) throws ExcepcionPrecio {
        try {
            Double.parseDouble(precio);
        } catch (NumberFormatException e) {
            throw new ExcepcionPrecio("El precio debe ser un numero valido!!.");
        }
        System.out.println("El precio es un numero valido.");
    }


}
