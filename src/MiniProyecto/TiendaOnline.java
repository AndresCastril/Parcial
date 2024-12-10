package MiniProyecto;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TiendaOnline {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tienda Online");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(null);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(10, 20, 100, 25);
        frame.add(lblCodigo);

        JTextField campoCodigo = new JTextField();
        campoCodigo.setBounds(120, 20, 165, 25);
        frame.add(campoCodigo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 60, 100, 25);
        frame.add(lblNombre);

        JTextField campoNombre = new JTextField();
        campoNombre.setBounds(120, 60, 165, 25);
        frame.add(campoNombre);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(10, 100, 100, 25);
        frame.add(lblPrecio);

        JTextField campoPrecio = new JTextField();
        campoPrecio.setBounds(120, 100, 165, 25);
        frame.add(campoPrecio);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(10, 140, 100, 25);
        frame.add(lblCategoria);

        JTextField campoCategoria = new JTextField();
        campoCategoria.setBounds(120, 140, 165, 25);
        frame.add(campoCategoria);

        JButton btnCrearProducto = new JButton("Crear Producto");
        btnCrearProducto.setBounds(10, 180, 150, 25);
        frame.add(btnCrearProducto);

        JButton btnCargarProductos = new JButton("Cargar Productos");
        btnCargarProductos.setBounds(170, 180, 150, 25);
        frame.add(btnCargarProductos);

        JButton btnEliminarProducto = new JButton("Eliminar Producto");
        btnEliminarProducto.setBounds(330, 180, 150, 25);
        frame.add(btnEliminarProducto);

        JButton btnEditarProducto = new JButton("Editar Producto");
        btnEditarProducto.setBounds(490, 180, 150, 25);
        frame.add(btnEditarProducto);

        DefaultTableModel modelProductos = new DefaultTableModel();
        modelProductos.addColumn("Código");
        modelProductos.addColumn("Nombre");
        modelProductos.addColumn("Precio");
        modelProductos.addColumn("Categoría");

        JTable tablaProductos = new JTable(modelProductos);
        JScrollPane scrollProductos = new JScrollPane(tablaProductos);
        scrollProductos.setBounds(10, 220, 960, 300);
        frame.add(scrollProductos);

        btnCrearProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (productoExiste(campoCodigo.getText())) {
                        JOptionPane.showMessageDialog(frame, "El código ya existe. Intente con otro.");
                    } else {
                        ValidarPrecio validarPrecio = new ValidarPrecio();
                        ValidarRangoPrecio validarRango = new ValidarRangoPrecio();
                        ValidarNombre validarNombre = new ValidarNombre();
                        String nombre = campoNombre.getText();
                        String precio = campoPrecio.getText();

                        if (precio.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "El precio no puede estar vacío.");
                            return;
                        }

                        double precioDouble = Double.parseDouble(precio);

                        validarPrecio.validarNumero(precio);
                        validarRango.validarRango(precioDouble);
                        validarNombre.validarNombre(nombre);

                        guardarProductoXml(campoCodigo.getText(), campoNombre.getText(),
                                precioDouble, campoCategoria.getText());
                        actualizarTablaProductos(modelProductos);
                    }
                } catch (ExcepcionPrecio ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (ExcepcionRangoPrecio ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (ExcepcionNombre ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        btnCargarProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actualizarTablaProductos(modelProductos);
            }
        });

        btnEliminarProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaProductos.getSelectedRow();
                if (row >= 0) {
                    String codigo = modelProductos.getValueAt(row, 0).toString();
                    eliminarProductoXml(codigo);
                    actualizarTablaProductos(modelProductos);
                } else {
                    JOptionPane.showMessageDialog(frame, "Seleccione un producto para eliminar.");
                }
            }
        });

        btnEditarProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaProductos.getSelectedRow();
                if (row >= 0) {
                    String codigo = modelProductos.getValueAt(row, 0).toString();
                    String nuevoNombre = JOptionPane.showInputDialog(frame, "Nuevo nombre:",
                            modelProductos.getValueAt(row, 1).toString());
                    String nuevoPrecio = JOptionPane.showInputDialog(frame, "Nuevo precio:",
                            modelProductos.getValueAt(row, 2).toString());
                    String nuevaCategoria = JOptionPane.showInputDialog(frame, "Nueva categoría:",
                            modelProductos.getValueAt(row, 3).toString());

                    if (nuevoNombre != null && nuevoPrecio != null && nuevaCategoria != null) {
                        editarProductoXml(codigo, nuevoNombre, Double.parseDouble(nuevoPrecio), nuevaCategoria);
                        actualizarTablaProductos(modelProductos);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Seleccione un producto para editar.");
                }
            }
        });

        JButton btnComprar = new JButton("COMPRAR");
        btnComprar.setBounds(10, 540, 150, 25);
        frame.add(btnComprar);

        btnComprar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (modelProductos.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(frame, "No hay productos para generar una factura.");
                    return;
                }

                JFrame facturaFrame = new JFrame("Generar Factura");
                facturaFrame.setSize(400, 400);
                facturaFrame.setLayout(null);

                JLabel lblFactura = new JLabel("Número de Factura:");
                lblFactura.setBounds(10, 20, 150, 25);
                facturaFrame.add(lblFactura);

                JTextField campoFactura = new JTextField();
                campoFactura.setBounds(160, 20, 200, 25);
                facturaFrame.add(campoFactura);

                JLabel lblNombre = new JLabel("Nombre:");
                lblNombre.setBounds(10, 60, 150, 25);
                facturaFrame.add(lblNombre);

                JTextField campoNombre = new JTextField();
                campoNombre.setBounds(160, 60, 200, 25);
                facturaFrame.add(campoNombre);

                JLabel lblId = new JLabel("Identificación:");
                lblId.setBounds(10, 100, 150, 25);
                facturaFrame.add(lblId);

                JTextField campoId = new JTextField();
                campoId.setBounds(160, 100, 200, 25);
                facturaFrame.add(campoId);

                JLabel lblDireccion = new JLabel("Dirección:");
                lblDireccion.setBounds(10, 140, 150, 25);
                facturaFrame.add(lblDireccion);

                JTextField campoDireccion = new JTextField();
                campoDireccion.setBounds(160, 140, 200, 25);
                facturaFrame.add(campoDireccion);

                JButton btnGenerar = new JButton("Generar Factura");
                btnGenerar.setBounds(10, 200, 150, 25);
                facturaFrame.add(btnGenerar);

                btnGenerar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String numeroFactura = campoFactura.getText();
                        String nombreCliente = campoNombre.getText();
                        String identificacion = campoId.getText();
                        String direccion = campoDireccion.getText();

                        if (numeroFactura.isEmpty() || nombreCliente.isEmpty() || identificacion.isEmpty() || direccion.isEmpty()) {
                            JOptionPane.showMessageDialog(facturaFrame, "Todos los campos son obligatorios.");
                            return;
                        }

                        List<Producto> productos = new ArrayList<>();
                        double total = 0;

                        for (int i = 0; i < modelProductos.getRowCount(); i++) {
                            String codigo = modelProductos.getValueAt(i, 0).toString();
                            String nombre = modelProductos.getValueAt(i, 1).toString();
                            double precio = Double.parseDouble(modelProductos.getValueAt(i, 2).toString());
                            String categoria = modelProductos.getValueAt(i, 3).toString();

                            productos.add(new Producto(codigo, nombre, precio, categoria));
                            total += precio;
                        }

                        double impuesto = total * 0.19; // 19% 
                        double totalFinal = total + impuesto;

                        Factura factura = new Factura(numeroFactura, nombreCliente, identificacion, direccion, productos, impuesto, totalFinal);

                        File carpetaFacturas = new File("facturas");
                        if (!carpetaFacturas.exists()) {
                            carpetaFacturas.mkdir();
                        }

                        File archivoFactura = new File(carpetaFacturas, "factura_" + numeroFactura + ".json");

                        try (FileWriter writer = new FileWriter(archivoFactura)) {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            gson.toJson(factura, writer);
                            JOptionPane.showMessageDialog(facturaFrame, "Factura generada y guardada exitosamente.");
                            facturaFrame.dispose();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(facturaFrame, "Error al guardar la factura.");
                        }
                    }
                });

                facturaFrame.setVisible(true);
            }
        });
        JButton btnFacturas = new JButton("FACTURAS");
        btnFacturas.setBounds(150, 540, 150, 25);
        btnFacturas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarFacturas();
            }
        });
        frame.add(btnFacturas);

        frame.setVisible(true);
    }

    private static void mostrarFacturas() {

        JFrame ventanaFacturas = new JFrame("Facturas Generadas");
        ventanaFacturas.setSize(800, 600);
        ventanaFacturas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaFacturas.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Cliente", "Total", "direccion"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        cargarFacturas(tableModel);

        ventanaFacturas.add(scrollPane, BorderLayout.CENTER);

        ventanaFacturas.setVisible(true);
    }

    private static void cargarFacturas(DefaultTableModel tableModel) {
        String directorioFacturas = "facturas";
        File carpeta = new File(directorioFacturas);

        if (!carpeta.exists() || !carpeta.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Directorio de facturas no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".json"));
        if (archivos != null) {
            Gson gson = new Gson();

            for (File archivo : archivos) {
                try (FileReader reader = new FileReader(archivo)) {

                    Factura factura = gson.fromJson(reader, Factura.class);

                    tableModel.addRow(new Object[]{
                        factura.numeroFactura,
                        factura.nombreCliente,
                        factura.total,
                        factura.direccion,
                        factura.impuesto,
                        factura.identificacion
                    });
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al leer archivo: " + archivo.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static void guardarProductoXml(String codigo, String nombre, double precio, String categoria) {
        try {
            File archivoXml = new File("productos.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            if (archivoXml.exists()) {
                try {
                    doc = dBuilder.parse(archivoXml);
                    doc.getDocumentElement().normalize();
                } catch (FileNotFoundException e) {
                    throw new FileNotFoundException("El archivo 'productos.xml' no se encontró, aunque existe físicamente.");
                }
            } else {
                doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("Productos");
                doc.appendChild(rootElement);
            }

            Element nuevoProducto = doc.createElement("producto");

            Element codigoElem = doc.createElement("codigo");
            codigoElem.appendChild(doc.createTextNode(codigo));
            nuevoProducto.appendChild(codigoElem);

            Element nombreElem = doc.createElement("nombre");
            nombreElem.appendChild(doc.createTextNode(nombre));
            nuevoProducto.appendChild(nombreElem);

            Element precioElem = doc.createElement("precio");
            precioElem.appendChild(doc.createTextNode(Double.toString(precio)));
            nuevoProducto.appendChild(precioElem);

            Element categoriaElem = doc.createElement("categoria");
            categoriaElem.appendChild(doc.createTextNode(categoria));
            nuevoProducto.appendChild(categoriaElem);

            doc.getDocumentElement().appendChild(nuevoProducto);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivoXml);
            transformer.transform(source, result);

            System.out.println("Producto guardado exitosamente.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el producto: " + e.getMessage());
        }
    }

    private static void actualizarTablaProductos(DefaultTableModel model) {
        try {
            File archivoXml = new File("productos.xml");
            if (!archivoXml.exists()) {
                System.out.println("El archivo XML no existe.");
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXml);
            doc.getDocumentElement().normalize();

            NodeList productos = doc.getElementsByTagName("producto");

            model.setRowCount(0);

            for (int i = 0; i < productos.getLength(); i++) {
                Element producto = (Element) productos.item(i);

                String codigo = obtenerContenidoSeguro(producto, "codigo");
                String nombre = obtenerContenidoSeguro(producto, "nombre");
                String precio = obtenerContenidoSeguro(producto, "precio");
                String categoria = obtenerContenidoSeguro(producto, "categoria");

                model.addRow(new Object[]{codigo, nombre, precio, categoria});
            }
            System.out.println("Tabla actualizada exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los productos: " + e.getMessage());
        }
    }

    private static boolean productoExiste(String codigo) {
        File file = new File("productos.xml");
        if (!file.exists()) {
            return false;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("Producto");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                if (codigo.equals(obtenerContenidoSeguro(element, "Codigo"))) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void eliminarProductoXml(String codigo) {
        try {
            File archivoXml = new File("productos.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXml);

            doc.getDocumentElement().normalize();
            NodeList productos = doc.getElementsByTagName("producto");

            for (int i = 0; i < productos.getLength(); i++) {
                Element producto = (Element) productos.item(i);
                if (obtenerContenidoSeguro(producto, "codigo").equals(codigo)) {
                    producto.getParentNode().removeChild(producto);
                    break;
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivoXml);
            transformer.transform(source, result);

            System.out.println("Producto eliminado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void editarProductoXml(String codigo, String nuevoNombre, double nuevoPrecio, String nuevaCategoria) {
        try {
            File archivoXml = new File("productos.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXml);

            doc.getDocumentElement().normalize();
            NodeList productos = doc.getElementsByTagName("producto");

            for (int i = 0; i < productos.getLength(); i++) {
                Element producto = (Element) productos.item(i);
                if (obtenerContenidoSeguro(producto, "codigo").equals(codigo)) {
                    producto.getElementsByTagName("nombre").item(0).setTextContent(nuevoNombre);
                    producto.getElementsByTagName("precio").item(0).setTextContent(Double.toString(nuevoPrecio));
                    producto.getElementsByTagName("categoria").item(0).setTextContent(nuevaCategoria);
                    break;
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivoXml);
            transformer.transform(source, result);

            System.out.println("Producto editado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String obtenerContenidoSeguro(Element element, String tagName) {
        try {
            return element.getElementsByTagName(tagName).item(0).getTextContent();
        } catch (Exception e) {
            return "";
        }
    }

}
