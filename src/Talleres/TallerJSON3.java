package Talleres;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TallerJSON3 {
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
            List<JsonObject> productos = cargarProductos();
            JsonObject nuevoProducto = new JsonObject();
            nuevoProducto.addProperty("nombre", nombre);
            nuevoProducto.addProperty("precio", precio);
            nuevoProducto.addProperty("categoria", categoria);
            productos.add(nuevoProducto);

            guardarProductosEnArchivo(productos);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el producto: " + e.getMessage());
        }
    }

    private static void modificarProducto(int fila, String nuevoNombre, String nuevoPrecio, String nuevaCategoria) {
        try {
            List<JsonObject> productos = cargarProductos();
            JsonObject producto = productos.get(fila);
            producto.addProperty("nombre", nuevoNombre);
            producto.addProperty("precio", nuevoPrecio);
            producto.addProperty("categoria", nuevaCategoria);

            guardarProductosEnArchivo(productos);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al modificar el producto: " + e.getMessage());
        }
    }

    private static void eliminarProducto(int fila) {
        try {
            List<JsonObject> productos = cargarProductos();
            productos.remove(fila);

            guardarProductosEnArchivo(productos);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
        }
    }

    private static void actualizarTabla(DefaultTableModel model) {
        model.setRowCount(0);

        try {
            List<JsonObject> productos = cargarProductos();
            for (JsonObject producto : productos) {
                String nombre = producto.get("nombre").getAsString();
                String precio = producto.get("precio").getAsString();
                String categoria = producto.get("categoria").getAsString();
                model.addRow(new Object[]{nombre, precio, categoria});
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

    private static List<JsonObject> cargarProductos() {
        File jsonFile = new File("productos.json");
        if (!jsonFile.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(jsonFile)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<JsonObject>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static void guardarProductosEnArchivo(List<JsonObject> productos) {
        try (Writer writer = new FileWriter("productos.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(productos, writer);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar en archivo JSON: " + e.getMessage());
        }
    }
}
