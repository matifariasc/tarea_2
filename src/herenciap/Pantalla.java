/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package herenciap;

import java.util.Map;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author mfarias
 */
public class Pantalla extends javax.swing.JFrame {
    
    private String[] marcasAuto = {"Toyota","Hyundai" ,"Suzuki", "Ford", "Chevrolet","Honda"};
    private String[] marcasMoto = {"Yamaha", "Suzuki", "Honda","KTM","BMW","Kawasaki"};
    private String[] marcasCamion = {"Volvo","Iveco","Mercedes","Isuzu", "MAN","Scania"};
    
    private String[] accesoriosAuto = {"Sin elegir","Puertas", "Luces"};
    private String[] accesoriosMoto = {"Sin elegir","Pedales", "Espejos"};
    private String[] accesoriosCamion = {"Sin elegir","Tolva", "Ejes"};
    
    private Map<String, Integer> preciosAccesoriosAuto = Map.of(
        "Puertas", 1000,
        "Luces", 600
    );

    private Map<String, Integer> preciosAccesoriosMoto = Map.of(
        "Pedales", 1000,
        "Espejos", 8000
    );

    private Map<String, Integer> preciosAccesoriosCamion = Map.of(
        "Tolva o Caja", 10000,
        "Ejes", 8000
    );

    private Map<String, Integer> preciosMarcasAuto = Map.of(
        "Toyota", 19000,
        "Hyundai", 16000,
        "Suzuki", 12000,
        "Ford", 17000,
        "Chevrolet", 18000,
        "Honda", 21000
    );

    private Map<String, Integer> preciosMarcasMoto = Map.of(
        "Yamaha", 14000,
        "Suzuki", 8000,
        "Honda", 15000,
        "KTM", 5000,
        "BMW", 18000,
        "Kawasaki", 13000
    );

    private Map<String, Integer> preciosMarcasCamion = Map.of(
        "Volvo", 100000,
        "Iveco", 85000,
        "Mercedes", 110000,
        "Isuzu", 70000,
        "MAN", 105000,
        "Scania", 95000
    );
    private int currentRow = -1;
    private void mostrarInformacionInicial() {
        Connection conn = DatabaseConnection.getConnection();

        try {
            // Cliente que compró más vehículos
            String maxVehiculosSQL = "SELECT c.nombre, c.rut, SUM(d.cantidad) AS total_vehiculos " +
                                     "FROM Clientes c " +
                                     "JOIN Compras com ON c.cliente_id = com.cliente_id " +
                                     "JOIN Detalle_Compras d ON com.compra_id = d.compra_id " +
                                     "JOIN Vehiculos v ON d.vehiculo_id = v.vehiculo_id " +
                                     "GROUP BY c.cliente_id " +
                                     "ORDER BY total_vehiculos DESC " +
                                     "LIMIT 1";
            PreparedStatement pstmtMaxVehiculos = conn.prepareStatement(maxVehiculosSQL);
            ResultSet rsMaxVehiculos = pstmtMaxVehiculos.executeQuery();
            String clienteMaxVehiculos = "";
            if (rsMaxVehiculos.next()) {
                clienteMaxVehiculos = "Cliente que compró más vehículos: " + rsMaxVehiculos.getString("nombre") +
                                      " (" + rsMaxVehiculos.getString("rut") + ") con " +
                                      rsMaxVehiculos.getInt("total_vehiculos") + " vehículos.\n";
            }

            // Cliente que compró más accesorios
            String maxAccesoriosSQL = "SELECT c.nombre, c.rut, SUM(d.cantidad) AS total_accesorios " +
                                      "FROM Clientes c " +
                                      "JOIN Compras com ON c.cliente_id = com.cliente_id " +
                                      "JOIN Detalle_Compras d ON com.compra_id = d.compra_id " +
                                      "JOIN Accesorios a ON d.accesorio_id = a.accesorio_id " +
                                      "GROUP BY c.cliente_id " +
                                      "ORDER BY total_accesorios DESC " +
                                      "LIMIT 1";
            PreparedStatement pstmtMaxAccesorios = conn.prepareStatement(maxAccesoriosSQL);
            ResultSet rsMaxAccesorios = pstmtMaxAccesorios.executeQuery();
            String clienteMaxAccesorios = "";
            if (rsMaxAccesorios.next()) {
                clienteMaxAccesorios = "Cliente que compró más accesorios: " + rsMaxAccesorios.getString("nombre") +
                                       " (" + rsMaxAccesorios.getString("rut") + ") con " +
                                       rsMaxAccesorios.getInt("total_accesorios") + " accesorios.\n";
            }

            // Cantidad total de vehículos comprados
            String totalVehiculosSQL = "SELECT SUM(d.cantidad) AS total_vehiculos " +
                                       "FROM Detalle_Compras d " +
                                       "JOIN Vehiculos v ON d.vehiculo_id = v.vehiculo_id";
            PreparedStatement pstmtTotalVehiculos = conn.prepareStatement(totalVehiculosSQL);
            ResultSet rsTotalVehiculos = pstmtTotalVehiculos.executeQuery();
            int totalVehiculos = 0;
            if (rsTotalVehiculos.next()) {
                totalVehiculos = rsTotalVehiculos.getInt("total_vehiculos");
            }
            String totalVehiculosMsg = "Cantidad total de vehículos comprados: " + totalVehiculos + "\n";

            // Promedio de ventas totales por cantidad de clientes
            String promedioVentasSQL = "SELECT AVG(com.total) AS promedio_ventas " +
                                       "FROM Compras com";
            PreparedStatement pstmtPromedioVentas = conn.prepareStatement(promedioVentasSQL);
            ResultSet rsPromedioVentas = pstmtPromedioVentas.executeQuery();
            double promedioVentas = 0;
            if (rsPromedioVentas.next()) {
                promedioVentas = rsPromedioVentas.getDouble("promedio_ventas");
            }
            String promedioVentasMsg = "Promedio de ventas totales por cantidad de clientes: " + promedioVentas + "\n";

            // Mostrar mensaje
            String mensaje = clienteMaxVehiculos + clienteMaxAccesorios + totalVehiculosMsg + promedioVentasMsg;
            JOptionPane.showMessageDialog(this, mensaje);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener la información inicial: " + e.getMessage());
        }
    }

    /**
     * Creates new form Pantalla
     */
    public Pantalla() {
        initComponents();
        mostrarInformacionInicial();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        grupoBotones = new javax.swing.ButtonGroup();
        grupobotones_remoto = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        text_name = new javax.swing.JTextField();
        text_rut = new javax.swing.JTextField();
        guardar = new javax.swing.JButton();
        boton_vehiculo = new javax.swing.JRadioButton();
        boton_accesorios = new javax.swing.JRadioButton();
        tipo_vehiculo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        tipo_accesorios = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        tipo_marca = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        text_cantidad = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        text_año = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        agregar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        control_si = new javax.swing.JRadioButton();
        control_no = new javax.swing.JRadioButton();
        subtotal = new javax.swing.JTextField();
        total = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        descuento = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        boton_marca = new javax.swing.JButton();
        boton_ano = new javax.swing.JButton();
        boton_remoto = new javax.swing.JButton();
        boton_camion = new javax.swing.JButton();
        boton_auto = new javax.swing.JButton();
        boton_moto = new javax.swing.JButton();
        limpiar_tabla = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(0, 102, 255));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setToolTipText("");

        jLabel1.setText("NOMBRE");

        jLabel2.setText("RUT");

        text_name.setName("text_name"); // NOI18N
        text_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_nameActionPerformed(evt);
            }
        });

        text_rut.setName("text_rut"); // NOI18N
        text_rut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_rutActionPerformed(evt);
            }
        });

        guardar.setText("Guardar");
        guardar.setName("guardar"); // NOI18N
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        grupoBotones.add(boton_vehiculo);
        boton_vehiculo.setText("VEHICULO");
        boton_vehiculo.setName("boton_vehiculo"); // NOI18N
        boton_vehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_vehiculoActionPerformed(evt);
            }
        });

        grupoBotones.add(boton_accesorios);
        boton_accesorios.setText("ACCESORIOS");
        boton_accesorios.setName("boton_accesorios"); // NOI18N
        boton_accesorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_accesoriosActionPerformed(evt);
            }
        });

        tipo_vehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecionar","auto", "moto", "camion" }));
        tipo_vehiculo.setName("tipo_vehiculo"); // NOI18N
        tipo_vehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipo_vehiculoActionPerformed(evt);
            }
        });

        jLabel3.setText("VEHICULO");

        tipo_accesorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipo_accesoriosActionPerformed(evt);
            }
        });

        jLabel4.setText("ACCESORIOS");

        tipo_marca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipo_marcaActionPerformed(evt);
            }
        });

        jLabel5.setText("MARCA");

        jLabel6.setText("CANTIDAD");

        text_cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_cantidadActionPerformed(evt);
            }
        });

        jLabel7.setText("AÑO");

        text_año.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_añoActionPerformed(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/herenciap/img.jpeg"))); // NOI18N

        agregar.setText("AGREGAR");
        agregar.setName("salir"); // NOI18N
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        jLabel12.setText("CONTROL REMOTO");

        grupobotones_remoto.add(control_si);
        control_si.setText("SI");
        control_si.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                control_siActionPerformed(evt);
            }
        });

        grupobotones_remoto.add(control_no);
        control_no.setText("NO");
        control_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                control_noActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(text_name, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(text_rut)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boton_vehiculo)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(boton_accesorios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tipo_marca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tipo_accesorios, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tipo_vehiculo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(text_cantidad)
                            .addComponent(text_año)))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(control_si)
                                .addGap(18, 18, 18)
                                .addComponent(control_no))
                            .addComponent(jLabel12))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(agregar)
                                .addGap(18, 18, 18)
                                .addComponent(guardar)
                                .addGap(13, 13, 13))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(text_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(text_rut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(boton_vehiculo)
                            .addComponent(boton_accesorios))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tipo_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tipo_accesorios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tipo_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(text_año, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(control_si)
                            .addComponent(control_no))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agregar)
                            .addComponent(guardar))
                        .addGap(4, 4, 4)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        subtotal.setEditable(false);

        total.setEditable(false);

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "VEHICULO", "MARCA", "ACC 1", "CANT", "ACC 2", "CANT", "CONTROL", "AÑO", "PRECIO", "CANTIDAD", "IMPORTE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabla);

        jLabel13.setText("SUBTOTAL");

        descuento.setEditable(false);

        jLabel14.setText("DESCUENTO");

        jLabel15.setText("TOTAL");

        jPanel3.setBackground(new java.awt.Color(0, 204, 153));

        jLabel8.setText("REPORTES");

        boton_marca.setText("MARCA");
        boton_marca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_marcaActionPerformed(evt);
            }
        });

        boton_ano.setText("AÑO");
        boton_ano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_anoActionPerformed(evt);
            }
        });

        boton_remoto.setText("CONTROL REMOTO");
        boton_remoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_remotoActionPerformed(evt);
            }
        });

        boton_camion.setText("CAMION");
        boton_camion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_camionActionPerformed(evt);
            }
        });

        boton_auto.setText("AUTO");
        boton_auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_autoActionPerformed(evt);
            }
        });

        boton_moto.setText("MOTO");
        boton_moto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_motoActionPerformed(evt);
            }
        });

        limpiar_tabla.setText("Limpiar Tabla");
        limpiar_tabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiar_tablaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boton_ano)
                            .addComponent(boton_remoto)
                            .addComponent(boton_marca)
                            .addComponent(limpiar_tabla))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boton_auto)
                            .addComponent(boton_camion)
                            .addComponent(boton_moto))))
                .addContainerGap(126, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_marca)
                    .addComponent(boton_camion))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_ano)
                    .addComponent(boton_auto))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_remoto)
                    .addComponent(boton_moto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(limpiar_tabla)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 837, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boton_camionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_camionActionPerformed
        // TODO add your handling code here:
        obtenerDatosPorTipo("camion");
    }//GEN-LAST:event_boton_camionActionPerformed

    private void boton_motoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_motoActionPerformed
        // TODO add your handling code here:
        obtenerDatosPorTipo("moto");
    }//GEN-LAST:event_boton_motoActionPerformed

    private void boton_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_marcaActionPerformed
        // TODO add your handling code here:
        String marca = (String) tipo_marca.getSelectedItem();
        obtenerDatosPorMarca(marca);
        
    }//GEN-LAST:event_boton_marcaActionPerformed

    private void text_añoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_añoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_añoActionPerformed

    private void text_cantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_cantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_cantidadActionPerformed

    private void boton_vehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_vehiculoActionPerformed
        // TODO add your handling code here:
        control_si.setEnabled(true);
        control_no.setEnabled(true);
        tipo_vehiculo.setEnabled(true);
        tipo_accesorios.setEnabled(false);
        text_año.setEnabled(true);
    }//GEN-LAST:event_boton_vehiculoActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        // TODO add your handling code here:
        boolean found = false;
        int existingRowIndex = -1;

        String vehiculo = (String) tipo_vehiculo.getSelectedItem();
        String marca = (String) tipo_marca.getSelectedItem();
        String acc1 = (String) tipo_accesorios.getSelectedItem();
        String anio = text_año.getText();
        String cantidadStr = text_cantidad.getText();
        String control = control_si.isSelected() ? "SI" : "NO";

        int cantidad = Integer.parseInt(cantidadStr);
        int precio = 0;

        // Obtener el precio de la marca y accesorio
        switch (vehiculo.toLowerCase()) {
            case "auto":
                precio = preciosMarcasAuto.getOrDefault(marca, 0);
                break;
            case "moto":
                precio = preciosMarcasMoto.getOrDefault(marca, 0);
                break;
            case "camion":
                precio = preciosMarcasCamion.getOrDefault(marca, 0);
                break;
        }

        int precioAccesorio = 0;
        if (tipo_accesorios.isEnabled()) {
            switch (vehiculo.toLowerCase()) {
                case "auto":
                    precioAccesorio = preciosAccesoriosAuto.getOrDefault(acc1, 0);
                    break;
                case "moto":
                    precioAccesorio = preciosAccesoriosMoto.getOrDefault(acc1, 0);
                    break;
                case "camion":
                    precioAccesorio = preciosAccesoriosCamion.getOrDefault(acc1, 0);
                    break;
            }
        }

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(vehiculo) && model.getValueAt(i, 1).equals(marca) && model.getValueAt(i, 7).equals(anio)) {
                found = true;
                existingRowIndex = i;
                break;
            }
        }

        if (found) {
            int existingCant1 = (int) model.getValueAt(existingRowIndex, 3);
            int existingCant2 = (int) model.getValueAt(existingRowIndex, 5);
            if (model.getValueAt(existingRowIndex, 2).equals(acc1)) {
                model.setValueAt(existingCant1 + cantidad, existingRowIndex, 3);
            } else if (model.getValueAt(existingRowIndex, 4).equals(acc1) || model.getValueAt(existingRowIndex, 4).equals("-")) {
                model.setValueAt(acc1, existingRowIndex, 4);
                model.setValueAt(existingCant2 + cantidad, existingRowIndex, 5);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se pueden agregar más de dos accesorios diferentes por vehículo.");
            }
        } else {
            model.addRow(new Object[]{vehiculo, marca, acc1, cantidad, "-", 0, control, anio, precio + precioAccesorio, cantidad, (precio + precioAccesorio) * cantidad});
        }

        // Actualizar subtotal y descuento
        actualizarTotales();
    }

    private void actualizarTotales() {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        int subtotalValue = 0;
        int cantidadTotal = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            int cantidadVehiculos = (int) model.getValueAt(i, 9);
            int importe = (int) model.getValueAt(i, 10);
            subtotalValue += importe;
            cantidadTotal += cantidadVehiculos;
        }

        int descuentoValue = calcularDescuento(subtotalValue, cantidadTotal);
        subtotal.setText(String.valueOf(subtotalValue));
        descuento.setText(String.valueOf(descuentoValue));
        total.setText(String.valueOf(subtotalValue - descuentoValue));
        text_cantidad.setText("");
    }//GEN-LAST:event_agregarActionPerformed
private int calcularDescuento(int subtotal, int cantidadTotal) {
    int descuento = 0;
    if (cantidadTotal > 10000) {
        descuento = (int) (subtotal * 0.10);
    } else if (cantidadTotal >= 5000) {
        descuento = (int) (subtotal * 0.05);
    } else if (cantidadTotal >= 1000) {
        descuento = (int) (subtotal * 0.03);
    } else if (cantidadTotal < 1000) {
        descuento = (int) (subtotal * 0.01);
    }
    return descuento;
}

private void calcularSubtotalYDescuento() {
    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    int subtotalValue = 0;
    int cantidadTotal = 0;

    for (int i = 0; i < model.getRowCount(); i++) {
        subtotalValue += (int) model.getValueAt(i, 10);
        cantidadTotal += (int) model.getValueAt(i, 9);
    }

    int descuentoValue = calcularDescuento(subtotalValue, cantidadTotal);
    int totalValue = subtotalValue - descuentoValue;

    subtotal.setText(String.valueOf(subtotalValue));
    descuento.setText(String.valueOf(descuentoValue));
    total.setText(String.valueOf(totalValue));
}
private String obtenerResumenDetalle(String nombre, String rut, int subtotal, int descuento, int total) {
    int cantidadVehiculos = 0;
    int cantidadAccesorios = 0;
    int totalPrecioAccesorios = 0;
    int totalPrecioVehiculos = 0;

    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        cantidadVehiculos += (int) model.getValueAt(i, 9);
        if (!model.getValueAt(i, 2).equals("-")) {
            cantidadAccesorios += (int) model.getValueAt(i, 3);
            totalPrecioAccesorios += preciosAccesoriosAuto.getOrDefault((String) model.getValueAt(i, 2), 0) * (int) model.getValueAt(i, 3);
        }
        if (!model.getValueAt(i, 4).equals("-")) {
            cantidadAccesorios += (int) model.getValueAt(i, 5);
            totalPrecioAccesorios += preciosAccesoriosAuto.getOrDefault((String) model.getValueAt(i, 4), 0) * (int) model.getValueAt(i, 5);
        }
        totalPrecioVehiculos += (int) model.getValueAt(i, 8) * (int) model.getValueAt(i, 9);
    }

    StringBuilder resumen = new StringBuilder();
    resumen.append("CLIENTE: ").append(nombre).append("\n");
    resumen.append("RUT: ").append(rut).append("\n");
    resumen.append("CANTIDAD (Vehículos Total): ").append(cantidadVehiculos).append("\n");
    resumen.append("CANTIDAD (Accesorios Total): ").append(cantidadAccesorios).append("\n");
    resumen.append("Total precio Accesorios: ").append(totalPrecioAccesorios).append("\n");
    resumen.append("TOTAL precio Vehiculos: ").append(totalPrecioVehiculos).append("\n");
    resumen.append("TOTAL con descuento ").append(descuento).append("%: ").append(total - descuento).append("\n");
    resumen.append("TOTAL GENERAL: ").append(total).append("\n");

    return resumen.toString();
}


private boolean esAccesorio1(String accesorio) {
    return accesorio.equals("Puertas") || accesorio.equals("Pedales") || accesorio.equals("Tolva");
}

private boolean esAccesorio2(String accesorio) {
    return accesorio.equals("Luces") || accesorio.equals("Espejos") || accesorio.equals("Ejes");
}
    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        // TODO add your handling code here:
        String nombre = text_name.getText();
        String rut = text_rut.getText();

        if (nombre.isEmpty() || rut.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Nombre y Rut son obligatorios.");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        try {
            conn.setAutoCommit(false); // Start transaction

            // Insert into Clientes table
            String insertClienteSQL = "INSERT INTO Clientes (nombre, rut) VALUES (?, ?)";
            PreparedStatement pstmtCliente = conn.prepareStatement(insertClienteSQL, Statement.RETURN_GENERATED_KEYS);
            pstmtCliente.setString(1, nombre);
            pstmtCliente.setString(2, rut);
            pstmtCliente.executeUpdate();

            ResultSet rs = pstmtCliente.getGeneratedKeys();
            int clienteId = 0;
            if (rs.next()) {
                clienteId = rs.getInt(1);
            }

            // Calcular subtotal y descuento
            int subtotalValue = Integer.parseInt(subtotal.getText());
            int descuentoValue = Integer.parseInt(descuento.getText());
            int totalValue = Integer.parseInt(total.getText());

            // Insert into Compras table
            String insertCompraSQL = "INSERT INTO Compras (cliente_id, fecha, subtotal, descuento, total) VALUES (?, CURDATE(), ?, ?, ?)";
            PreparedStatement pstmtCompra = conn.prepareStatement(insertCompraSQL, Statement.RETURN_GENERATED_KEYS);
            pstmtCompra.setInt(1, clienteId);
            pstmtCompra.setInt(2, subtotalValue);
            pstmtCompra.setInt(3, descuentoValue);
            pstmtCompra.setInt(4, totalValue);
            pstmtCompra.executeUpdate();

            rs = pstmtCompra.getGeneratedKeys();
            int compraId = 0;
            if (rs.next()) {
                compraId = rs.getInt(1);
            }

            // Insert into Vehiculos and Accesorios and Detalle_Compras table
            for (int i = 0; i < model.getRowCount(); i++) {
                String vehiculo = (String) model.getValueAt(i, 0);
                String marca = (String) model.getValueAt(i, 1);
                String acc1 = (String) model.getValueAt(i, 2);
                int cant1 = (int) model.getValueAt(i, 3);
                String acc2 = (String) model.getValueAt(i, 4);
                int cant2 = (int) model.getValueAt(i, 5);
                String control = (String) model.getValueAt(i, 6);
                String anio = (String) model.getValueAt(i, 7);
                int precio = (int) model.getValueAt(i, 8);
                int cantidad = (int) model.getValueAt(i, 9);
                int importe = (int) model.getValueAt(i, 10);

                // Validar el año antes de intentar insertarlo
                int anioFabricacion;
                try {
                    anioFabricacion = Integer.parseInt(anio);
                    if (anioFabricacion < 1901 || anioFabricacion > 2155) {
                        throw new NumberFormatException("Año fuera de rango");
                    }
                } catch (NumberFormatException e) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El año de fabricación debe ser un número entre 1901 y 2155.");
                    return;
                }

                String insertVehiculoSQL = "INSERT INTO Vehiculos (tipo, marca, cantidad_ruedas, control_remoto, anio_fabricacion) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmtVehiculo = conn.prepareStatement(insertVehiculoSQL, Statement.RETURN_GENERATED_KEYS);
                pstmtVehiculo.setString(1, vehiculo);
                pstmtVehiculo.setString(2, marca);
                pstmtVehiculo.setInt(3, cantidad);
                pstmtVehiculo.setBoolean(4, control.equals("SI"));
                pstmtVehiculo.setInt(5, anioFabricacion);
                pstmtVehiculo.executeUpdate();

                rs = pstmtVehiculo.getGeneratedKeys();
                int vehiculoId = 0;
                if (rs.next()) {
                    vehiculoId = rs.getInt(1);
                }

                // Insertar datos en Detalle_Compras para el vehículo
                String insertDetalleSQLVehiculo = "INSERT INTO Detalle_Compras (compra_id, vehiculo_id, accesorio_id, cantidad, precio_unitario, tipo) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmtDetalleVehiculo = conn.prepareStatement(insertDetalleSQLVehiculo);
                pstmtDetalleVehiculo.setInt(1, compraId);
                pstmtDetalleVehiculo.setInt(2, vehiculoId);
                pstmtDetalleVehiculo.setNull(3, java.sql.Types.INTEGER);
                pstmtDetalleVehiculo.setInt(4, cantidad);
                pstmtDetalleVehiculo.setInt(5, precio);
                pstmtDetalleVehiculo.setString(6, "Vehículo");
                pstmtDetalleVehiculo.executeUpdate();

                if (!acc1.equals("-")) {
                    String insertAccesorioSQL1 = "INSERT INTO Accesorios (vehiculo_id, tipo_accesorio, precio) VALUES (?, ?, ?)";
                    PreparedStatement pstmtAccesorio1 = conn.prepareStatement(insertAccesorioSQL1, Statement.RETURN_GENERATED_KEYS);
                    pstmtAccesorio1.setInt(1, vehiculoId);
                    pstmtAccesorio1.setString(2, acc1);
                    pstmtAccesorio1.setInt(3, preciosAccesoriosAuto.getOrDefault(acc1, 0));
                    pstmtAccesorio1.executeUpdate();

                    rs = pstmtAccesorio1.getGeneratedKeys();
                    int accesorioId1 = 0;
                    if (rs.next()) {
                        accesorioId1 = rs.getInt(1);
                    }

                    String insertDetalleSQL1 = "INSERT INTO Detalle_Compras (compra_id, vehiculo_id, accesorio_id, cantidad, precio_unitario, tipo) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmtDetalle1 = conn.prepareStatement(insertDetalleSQL1);
                    pstmtDetalle1.setInt(1, compraId);
                    pstmtDetalle1.setInt(2, vehiculoId);
                    pstmtDetalle1.setInt(3, accesorioId1);
                    pstmtDetalle1.setInt(4, cant1);
                    pstmtDetalle1.setInt(5, preciosAccesoriosAuto.getOrDefault(acc1, 0));
                    pstmtDetalle1.setString(6, "Accesorio");
                    pstmtDetalle1.executeUpdate();
                }

                if (!acc2.equals("-")) {
                    String insertAccesorioSQL2 = "INSERT INTO Accesorios (vehiculo_id, tipo_accesorio, precio) VALUES (?, ?, ?)";
                    PreparedStatement pstmtAccesorio2 = conn.prepareStatement(insertAccesorioSQL2, Statement.RETURN_GENERATED_KEYS);
                    pstmtAccesorio2.setInt(1, vehiculoId);
                    pstmtAccesorio2.setString(2, acc2);
                    pstmtAccesorio2.setInt(3, preciosAccesoriosAuto.getOrDefault(acc2, 0));
                    pstmtAccesorio2.executeUpdate();

                    rs = pstmtAccesorio2.getGeneratedKeys();
                    int accesorioId2 = 0;
                    if (rs.next()) {
                        accesorioId2 = rs.getInt(1);
                    }

                    String insertDetalleSQL2 = "INSERT INTO Detalle_Compras (compra_id, vehiculo_id, accesorio_id, cantidad, precio_unitario, tipo) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmtDetalle2 = conn.prepareStatement(insertDetalleSQL2);
                    pstmtDetalle2.setInt(1, compraId);
                    pstmtDetalle2.setInt(2, vehiculoId);
                    pstmtDetalle2.setInt(3, accesorioId2);
                    pstmtDetalle2.setInt(4, cant2);
                    pstmtDetalle2.setInt(5, preciosAccesoriosAuto.getOrDefault(acc2, 0));
                    pstmtDetalle2.setString(6, "Accesorio");
                    pstmtDetalle2.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction

            // Mostrar resumen detallado
            javax.swing.JOptionPane.showMessageDialog(this, "Datos guardados correctamente.\n" + obtenerResumenDetalle(nombre, rut, subtotalValue, descuentoValue, totalValue));
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar los datos: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback transaction on error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto commit
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_guardarActionPerformed

    private void text_rutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_rutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_rutActionPerformed

    private void text_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_nameActionPerformed

    private void boton_remotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_remotoActionPerformed
        // TODO add your handling code here:
        obtenerDatosPorControlRemoto(); 
    }//GEN-LAST:event_boton_remotoActionPerformed

    private void boton_autoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_autoActionPerformed
        // TODO add your handling code here:
        obtenerDatosPorTipo("auto");
    }//GEN-LAST:event_boton_autoActionPerformed

    private void control_siActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_control_siActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_control_siActionPerformed

    private void control_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_control_noActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_control_noActionPerformed

    private void boton_accesoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_accesoriosActionPerformed
        // TODO add your handling code here:
         control_si.setEnabled(false);
        control_no.setEnabled(false);
        tipo_vehiculo.setEnabled(false);
        tipo_accesorios.setEnabled(true);
        text_año.setEnabled(false);
    }//GEN-LAST:event_boton_accesoriosActionPerformed

    private void tipo_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipo_marcaActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tipo_marcaActionPerformed

    private void tipo_vehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipo_vehiculoActionPerformed
        // TODO add your handling code here:
        String tipo = (String) tipo_vehiculo.getSelectedItem();
        
        switch (tipo.toLowerCase()) {
            case "auto":
                tipo_marca.setModel(new javax.swing.DefaultComboBoxModel<>(marcasAuto));
                tipo_accesorios.setModel(new javax.swing.DefaultComboBoxModel<>(accesoriosAuto));
                break;
            case "moto":
                tipo_marca.setModel(new javax.swing.DefaultComboBoxModel<>(marcasMoto));
                tipo_accesorios.setModel(new javax.swing.DefaultComboBoxModel<>(accesoriosMoto));
                break;
            case "camion":
                tipo_marca.setModel(new javax.swing.DefaultComboBoxModel<>(marcasCamion));
                tipo_accesorios.setModel(new javax.swing.DefaultComboBoxModel<>(accesoriosCamion));
                break;
            default:
                tipo_marca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{}));
                tipo_accesorios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{}));
                break;
        }
    }//GEN-LAST:event_tipo_vehiculoActionPerformed

    private void tipo_accesoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipo_accesoriosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipo_accesoriosActionPerformed

    private void boton_anoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_anoActionPerformed
        // TODO add your handling code here:
        String anio = text_año.getText();
        obtenerDatosPorAnio(anio);
    }//GEN-LAST:event_boton_anoActionPerformed

    private void limpiar_tablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limpiar_tablaActionPerformed
        // TODO add your handling code here:
        limpiarTabla();
    }//GEN-LAST:event_limpiar_tablaActionPerformed

private void obtenerDatosPorTipo(String tipo) {
    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    model.setRowCount(0); // Limpiar la tabla

    String query = "SELECT v.tipo, v.marca, d.cantidad, d.precio_unitario, d.precio_unitario * d.cantidad as importe, c.fecha " +
                   "FROM Detalle_Compras d " +
                   "JOIN Vehiculos v ON d.vehiculo_id = v.vehiculo_id " +
                   "JOIN Compras c ON d.compra_id = c.compra_id " +
                   "WHERE v.tipo = ? AND c.fecha = CURDATE()";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, tipo);
        ResultSet rs = pstmt.executeQuery();

        int cantidadTotal = 0;
        int ventasTotales = 0;

        while (rs.next()) {
            String marca = rs.getString("marca");
            int cantidad = rs.getInt("cantidad");
            int precioUnitario = rs.getInt("precio_unitario");
            int importe = rs.getInt("importe");
            cantidadTotal += cantidad;
            ventasTotales += importe;

            model.addRow(new Object[]{tipo, marca, "-", cantidad, "-", 0, "-", "-", precioUnitario, cantidad, importe});
        }

        javax.swing.JOptionPane.showMessageDialog(this, "Cantidad de " + tipo + " vendidos al día: " + cantidadTotal +
                                                  "\nVentas totales diarias: " + ventasTotales);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void obtenerDatosPorMarca(String marca) {
    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    model.setRowCount(0); // Limpiar la tabla

    String query = "SELECT v.tipo, v.marca, d.cantidad, d.precio_unitario, d.precio_unitario * d.cantidad as importe, c.fecha " +
                   "FROM Detalle_Compras d " +
                   "JOIN Vehiculos v ON d.vehiculo_id = v.vehiculo_id " +
                   "JOIN Compras c ON d.compra_id = c.compra_id " +
                   "WHERE v.marca = ? AND c.fecha = CURDATE()";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, marca);
        ResultSet rs = pstmt.executeQuery();

        int cantidadTotal = 0;
        int ventasTotales = 0;

        while (rs.next()) {
            String tipo = rs.getString("tipo");
            int cantidad = rs.getInt("cantidad");
            int precioUnitario = rs.getInt("precio_unitario");
            int importe = rs.getInt("importe");
            cantidadTotal += cantidad;
            ventasTotales += importe;

            model.addRow(new Object[]{tipo, marca, "-", cantidad, "-", 0, "-", "-", precioUnitario, cantidad, importe});
        }

        javax.swing.JOptionPane.showMessageDialog(this, "Cantidad de vehículos de la marca " + marca + " vendidos al día: " + cantidadTotal +
                                                  "\nVentas totales diarias: " + ventasTotales);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void obtenerDatosPorAnio(String anio) {
    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    model.setRowCount(0); // Limpiar la tabla

    String query = "SELECT v.tipo, v.marca, d.cantidad, d.precio_unitario, d.precio_unitario * d.cantidad as importe, c.fecha " +
                   "FROM Detalle_Compras d " +
                   "JOIN Vehiculos v ON d.vehiculo_id = v.vehiculo_id " +
                   "JOIN Compras c ON d.compra_id = c.compra_id " +
                   "WHERE v.anio_fabricacion = ? AND c.fecha = CURDATE()";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, anio);
        ResultSet rs = pstmt.executeQuery();

        int cantidadTotal = 0;
        int ventasTotales = 0;

        while (rs.next()) {
            String tipo = rs.getString("tipo");
            String marca = rs.getString("marca");
            int cantidad = rs.getInt("cantidad");
            int precioUnitario = rs.getInt("precio_unitario");
            int importe = rs.getInt("importe");
            cantidadTotal += cantidad;
            ventasTotales += importe;

            model.addRow(new Object[]{tipo, marca, "-", cantidad, "-", 0, "-", anio, precioUnitario, cantidad, importe});
        }

        javax.swing.JOptionPane.showMessageDialog(this, "Cantidad de vehículos del año " + anio + " vendidos al día: " + cantidadTotal +
                                                  "\nVentas totales diarias: " + ventasTotales);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void obtenerDatosPorControlRemoto() {
    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    model.setRowCount(0); // Limpiar la tabla

    String query = "SELECT v.tipo, v.marca, d.cantidad, d.precio_unitario, d.precio_unitario * d.cantidad as importe, c.fecha " +
                   "FROM Detalle_Compras d " +
                   "JOIN Vehiculos v ON d.vehiculo_id = v.vehiculo_id " +
                   "JOIN Compras c ON d.compra_id = c.compra_id " +
                   "WHERE v.control_remoto = TRUE AND c.fecha = CURDATE()";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        ResultSet rs = pstmt.executeQuery();

        int cantidadTotal = 0;
        int ventasTotales = 0;

        while (rs.next()) {
            String tipo = rs.getString("tipo");
            String marca = rs.getString("marca");
            int cantidad = rs.getInt("cantidad");
            int precioUnitario = rs.getInt("precio_unitario");
            int importe = rs.getInt("importe");
            cantidadTotal += cantidad;
            ventasTotales += importe;

            model.addRow(new Object[]{tipo, marca, "-", cantidad, "-", 0, "SI", "-", precioUnitario, cantidad, importe});
        }

        javax.swing.JOptionPane.showMessageDialog(this, "Cantidad de vehículos con control remoto vendidos al día: " + cantidadTotal +
                                                  "\nVentas totales diarias: " + ventasTotales);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
private void limpiarTabla() {
    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    model.setRowCount(0); // Esto limpiará todas las filas de la tabla
}

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pantalla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pantalla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pantalla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pantalla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pantalla().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregar;
    private javax.swing.JRadioButton boton_accesorios;
    private javax.swing.JButton boton_ano;
    private javax.swing.JButton boton_auto;
    private javax.swing.JButton boton_camion;
    private javax.swing.JButton boton_marca;
    private javax.swing.JButton boton_moto;
    private javax.swing.JButton boton_remoto;
    private javax.swing.JRadioButton boton_vehiculo;
    private javax.swing.JRadioButton control_no;
    private javax.swing.JRadioButton control_si;
    private javax.swing.JTextField descuento;
    private javax.swing.ButtonGroup grupoBotones;
    private javax.swing.ButtonGroup grupobotones_remoto;
    private javax.swing.JButton guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JButton limpiar_tabla;
    private javax.swing.JTextField subtotal;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField text_año;
    private javax.swing.JTextField text_cantidad;
    private javax.swing.JTextField text_name;
    private javax.swing.JTextField text_rut;
    private javax.swing.JComboBox<String> tipo_accesorios;
    private javax.swing.JComboBox<String> tipo_marca;
    private javax.swing.JComboBox<String> tipo_vehiculo;
    private javax.swing.JTextField total;
    // End of variables declaration//GEN-END:variables
}
