package Talleres;

import java.awt.event.*;
import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;

public class TallerXMLDOM2 {

    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("Gestión de Productos");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(null);

            JLabel textNombre = new JLabel("NOMBRE");
            textNombre.setBounds(10, 20, 80, 25);
            frame.add(textNombre);

            JTextField campoNombre = new JTextField();
            campoNombre.setBounds(100, 20, 165, 25);
            frame.add(campoNombre);

            JLabel textPrecio = new JLabel("PRECIO");
            textPrecio.setBounds(10, 60, 80, 25);
            frame.add(textPrecio);

            JTextField campoPrecio = new JTextField();
            campoPrecio.setBounds(100, 60, 165, 25);
            frame.add(campoPrecio);

            JLabel textCategoria = new JLabel("CATEGORÍA");
            textCategoria.setBounds(10, 100, 80, 25);
            frame.add(textCategoria);

            JTextField campoCategoria = new JTextField();
            campoCategoria.setBounds(100, 100, 165, 25);
            frame.add(campoCategoria);

            JButton botonGuardar = new JButton("Guardar");
            botonGuardar.setBounds(10, 140, 100, 25);
            frame.add(botonGuardar);

            JButton botonModificar = new JButton("Modificar");
            botonModificar.setBounds(120, 140, 100, 25);
            frame.add(botonModificar);

            JButton botonEliminar = new JButton("Eliminar");
            botonEliminar.setBounds(230, 140, 100, 25);
            frame.add(botonEliminar);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Nombre");
            model.addColumn("Precio");
            model.addColumn("Categoría");

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(10, 180, 760, 300);
            frame.add(scrollPane);

            // Evento para guardar un nuevo producto
            botonGuardar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    guardarProducto(campoNombre.getText(), campoPrecio.getText(), campoCategoria.getText());
                    actualizarTabla(model);
                    limpiarCampos(campoNombre, campoPrecio, campoCategoria);
                }
            });

            // Evento para modificar un producto
            botonModificar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int filaSeleccionada = table.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        modificarProducto(filaSeleccionada, campoNombre.getText(), campoPrecio.getText(), campoCategoria.getText());
                        actualizarTabla(model);
                        limpiarCampos(campoNombre, campoPrecio, campoCategoria);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Selecciona un producto para modificar.");
                    }
                }
            });

            // Evento para eliminar un producto
            botonEliminar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int filaSeleccionada = table.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        eliminarProducto(filaSeleccionada);
                        actualizarTabla(model);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Selecciona un producto para eliminar.");
                    }
                }
            });

            // Cargar datos iniciales
            actualizarTabla(model);

            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void guardarProducto(String nombre, String precio, String categoria) {
        try {
            File xmlFile = new File("productos.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;

            if (xmlFile.exists()) {
                doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
            } else {
                doc = builder.newDocument();
                Element root = doc.createElement("Productos");
                doc.appendChild(root);
            }

            Element root = (Element) doc.getElementsByTagName("Productos").item(0);

            Element producto = doc.createElement("Producto");
            Element nombreElem = doc.createElement("nombre");
            nombreElem.appendChild(doc.createTextNode(nombre));
            producto.appendChild(nombreElem);

            Element precioElem = doc.createElement("precio");
            precioElem.appendChild(doc.createTextNode(precio));
            producto.appendChild(precioElem);

            Element categoriaElem = doc.createElement("categoria");
            categoriaElem.appendChild(doc.createTextNode(categoria));
            producto.appendChild(categoriaElem);

            root.appendChild(producto);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(domSource, result);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el producto: " + e.getMessage());
        }
    }

    private static void modificarProducto(int fila, String nuevoNombre, String nuevoPrecio, String nuevaCategoria) {
        try {
            File xmlFile = new File("productos.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList productos = doc.getElementsByTagName("Producto");
            Element producto = (Element) productos.item(fila);

            producto.getElementsByTagName("nombre").item(0).setTextContent(nuevoNombre);
            producto.getElementsByTagName("precio").item(0).setTextContent(nuevoPrecio);
            producto.getElementsByTagName("categoria").item(0).setTextContent(nuevaCategoria);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(domSource, result);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al modificar el producto: " + e.getMessage());
        }
    }

    private static void eliminarProducto(int fila) {
        try {
            File xmlFile = new File("productos.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList productos = doc.getElementsByTagName("Producto");
            Element producto = (Element) productos.item(fila);
            producto.getParentNode().removeChild(producto);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(domSource, result);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
        }
    }

    private static void actualizarTabla(DefaultTableModel model) {
        model.setRowCount(0);

        try {
            File xmlFile = new File("productos.xml");
            if (!xmlFile.exists()) {
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList productos = doc.getElementsByTagName("Producto");

            for (int i = 0; i < productos.getLength(); i++) {
                Node nodo = productos.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;
                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                    String precio = elemento.getElementsByTagName("precio").item(0).getTextContent();
                    String categoria = elemento.getElementsByTagName("categoria").item(0).getTextContent();
                    model.addRow(new Object[]{nombre, precio, categoria});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la tabla: " + e.getMessage());
        }
    }

    private static void limpiarCampos(JTextField campoNombre, JTextField campoPrecio, JTextField campoCategoria) {
        campoNombre.setText("");
        campoPrecio.setText("");
        campoCategoria.setText("");
    }
}
