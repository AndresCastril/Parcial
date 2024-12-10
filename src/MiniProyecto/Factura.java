
package MiniProyecto;

import java.util.List;


class Factura {
    String numeroFactura;
    String nombreCliente;
    String identificacion;
    String direccion;
    List<Producto> productos;
    double impuesto;
    double total;

    public Factura(String numeroFactura, String nombreCliente, String identificacion, String direccion, List<Producto> productos, double impuesto, double total) {
        this.numeroFactura = numeroFactura;
        this.nombreCliente = nombreCliente;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.productos = productos;
        this.impuesto = impuesto;
        this.total = total;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
    
}
