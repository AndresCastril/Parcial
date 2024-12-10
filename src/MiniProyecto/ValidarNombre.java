/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MiniProyecto;

public class ValidarNombre {
    public void validarNombre(String nombre) throws ExcepcionNombre {
    
    if (!nombre.matches("[a-zA-Z\\s]+")) {
        throw new ExcepcionNombre("El nombre no puede contener números ni caracteres especiales.");
    }
    System.out.println("El nombre es válido.");
}

    
}
