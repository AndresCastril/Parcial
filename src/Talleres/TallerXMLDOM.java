package Talleres;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;

public class TallerXMLDOM {

    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("ventana");
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
            botonGuardar.setBounds(100, 140, 100, 25);
            frame.add(botonGuardar);

            
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Nombre");
            model.addColumn("Precio");
            model.addColumn("Categoría");

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(10, 180, 760, 300);
            frame.add(scrollPane);

            
            botonGuardar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
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
                        Element nombre = doc.createElement("nombre");
                        nombre.appendChild(doc.createTextNode(campoNombre.getText()));
                        producto.appendChild(nombre);

                        Element precio = doc.createElement("precio");
                        precio.appendChild(doc.createTextNode(campoPrecio.getText()));
                        producto.appendChild(precio);

                        Element categoria = doc.createElement("categoria");
                        categoria.appendChild(doc.createTextNode(campoCategoria.getText()));
                        producto.appendChild(categoria);

                        
                        root.appendChild(producto);

                        
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        DOMSource domSource = new DOMSource(doc);
                        StreamResult result = new StreamResult(xmlFile);
                        transformer.transform(domSource, result);

                        System.out.println("Producto anadido al archivo XML.");

                       
                        actualizarTabla(model);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            
            actualizarTabla(model);

            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
    private static void actualizarTabla(DefaultTableModel model) {
        // Limpiar la tabla
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

            
            NodeList productList = doc.getElementsByTagName("Producto");

           
            for (int i = 0; i < productList.getLength(); i++) {
                Node productNode = productList.item(i);
                if (productNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element productElement = (Element) productNode;
                    String nombre = productElement.getElementsByTagName("nombre").item(0).getTextContent();
                    String precio = productElement.getElementsByTagName("precio").item(0).getTextContent();
                    String categoria = productElement.getElementsByTagName("categoria").item(0).getTextContent();

                  
                    model.addRow(new Object[]{nombre, precio, categoria});
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
