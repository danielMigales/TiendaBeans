package modelo;

import bean.Pedido;
import bean.Producto;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel Migales Puertas
 *
 */
public class ConexionBD {

    //PARA USAR ESTA BASE DE DATOS HAY QUE CREAR EN EL SERVIDOR MYSQL LA BASE DE DATOS tiendaBeans. LAS TABLAS SE CREAN AUTOMATICAMENTE CUANDO SE INSERTE ALGO EN ELLAS
//datos de la base de datos 
    private static Connection conection;
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String BD = "tiendaBeans";
    private static final String TABLE1 = "productos";
    private static final String TABLE2 = "pedidos";
    private static final String TABLE3 = "compras";

    //constructor de la conexion
    public ConexionBD() {

        conection = null;
        try {
            Class.forName(DRIVER);
            conection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConexionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desconectar() {
        conection = null;
    }

    //insertar los datos en la base de datos productos (para introducir nuevos productos al stock de forma manual)
    public void insertarProductos() throws SQLException, ClassNotFoundException {

        //obtengo los datos a insertar mediante teclado usuario
        Scanner entradaString = new Scanner(System.in);
        Scanner entradaInt = new Scanner(System.in);
        Scanner entradaFloat = new Scanner(System.in);
        System.out.println("Introducir descripcion:");
        String descripcion = entradaString.nextLine();
        System.out.println("Introducir stock:");
        int stock = entradaInt.nextInt();
        System.out.println("Introducir stockmin:");
        int stockmin = entradaInt.nextInt();
        System.out.println("Introducir precio:");
        float pvp = entradaFloat.nextFloat();

        Class.forName(DRIVER);
        conection = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);

        //crea la tabla productos si no existe con los siguientes campos:
        PreparedStatement ps = (PreparedStatement) conection.prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE1
                + "(id SERIAL PRIMARY KEY, descripcion VARCHAR (150), stock int (11), stockmin int (11), pvp FLOAT )");
        ps.executeUpdate();
        System.out.println("Tabla " + TABLE1 + " creada o actualizada.");

        String sql = "INSERT INTO " + TABLE1 + "(descripcion, stock, stockmin, pvp) values ('" + descripcion + "', '"
                + stock + "', '" + stockmin + "', '" + pvp + "')";
        System.out.println(sql);

        try (Statement st = conection.createStatement()) {
            st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Datos añadidos a la tabla.");
            try (ResultSet rs = st.getGeneratedKeys()) {
                rs.next();
            }
        }
        System.out.println("\n***************************************************************************************************************************\n");
    }

    //lee los datos en la base de datos y los muestra por pantalla
    public void consultarProductos() throws SQLException {

        Statement st = null;
        String sql = "SELECT * FROM " + TABLE1 + ";";
        conection = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);
        try {
            st = conection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int resultados = 0;

            while (rs.next()) {
                int id = rs.getInt("id");
                String descripcion = rs.getString("descripcion");
                int stock = rs.getInt("stock");
                int stockmin = rs.getInt("stockmin");
                float pvp = rs.getFloat("pvp");
                System.out.println("ID: " + id + "\tDescripcion: " + descripcion + "\t\tStock: " + stock + "\t\tStock minimo: " + stockmin + "\t\tPvp: " + pvp + "\n");
                resultados++;
            }
            if (resultados == 0) {
                System.out.println("No se ha encontrado ningun resultado.");
                System.out.println("\n***************************************************************************************************************************\n");
            }
            rs.close();
            System.out.println("\n***************************************************************************************************************************\n");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    //Cuando se va a realizar un pedido nuevo el usuario ha de especificar solo el id del producto, y se hace un select para buscar ese producto y añadirlo al pedido
    public Producto buscarProducto(int id) throws SQLException {

        Statement st = null;
        String sql = "SELECT * FROM " + TABLE1 + " WHERE id = " + id + ";";
        //System.out.println(sql);
        conection = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);
        Producto producto1 = null;

        try {
            st = conection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int resultados = 0;

            while (rs.next()) {
                id = rs.getInt("id");
                String descripcion = rs.getString("descripcion");
                int stock = rs.getInt("stock");
                int stockmin = rs.getInt("stockmin");
                float pvp = rs.getFloat("pvp");
                resultados++;
                producto1 = new Producto(id, descripcion, stock, stockmin, pvp);
            }
            if (resultados == 0) {
                System.out.println("No se ha encontrado ningun resultado.");
                System.out.println("\n***************************************************************************************************************************\n");
            }
            rs.close();
            System.out.println("\n***************************************************************************************************************************\n");
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return producto1;
    }

    //Cuando se hace un pedido se crea o actualiza la tabla pedidos agregando un nuevo registro
    public void insertarPedidos(Pedido pedido) throws ClassNotFoundException, SQLException {

        Class.forName(DRIVER);
        conection = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);

        PreparedStatement ps = (PreparedStatement) conection.prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE2
                + "(numeroPedido SERIAL PRIMARY KEY , idProducto int(11), descripcion VARCHAR (150), stockActual int (11), pvp FLOAT, fecha DATE, cantidad int(11) )");
        ps.executeUpdate();
        System.out.println("Tabla " + TABLE2 + " creada o actualizada.");

        //El parametro que recibe este metodo es un objeto pedido que incluye un objeto producto. 
        //Extraigo los datos de ambos objetos para completar la query e insertar un nuevo registro
        Producto producto = pedido.getProducto();
        int idProducto = producto.getIdproducto();
        String descripcion = producto.getDescripcion();
        int stockActual = producto.getStockactual();
        float pvp = producto.getPvp();
        //conversion de la fecha actual para introducirla con el formato sql correcto
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date fechaActual = new Date();
        String fecha = dateFormat.format(fechaActual);
        int cantidad = pedido.getCantidad();

        String sql = "INSERT INTO " + TABLE2 + "(idProducto, descripcion, stockActual, pvp, fecha, cantidad ) values ('" + idProducto + "', '" + descripcion + "', '"
                + stockActual + "', '" + pvp + "', '" + fecha + "', '" + cantidad + "')";
        System.out.println(sql);

        try (Statement st = conection.createStatement()) {
            st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Datos añadidos a la tabla.");
            try (ResultSet rs = st.getGeneratedKeys()) {
                rs.next();
            }
        }
        System.out.println("\n***************************************************************************************************************************\n");
        //ACTUALIZAR TABLA DE PRODUCTOS DESCONTANDO EL PRODUCTO PEDIDO
    }

    public void insertarCompras(Producto producto) throws ClassNotFoundException, SQLException {

        Class.forName(DRIVER);
        conection = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);

        PreparedStatement ps = (PreparedStatement) conection.prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE3
                + "(idCompra SERIAL PRIMARY KEY , idProducto int(11), descripcion VARCHAR (150), cantidadCompra int (11), pvp FLOAT, fecha DATE )");
        ps.executeUpdate();
        System.out.println("Tabla " + TABLE3 + " creada o actualizada.");

        //extraigo del objeto pasado como parametro sus atributos para incluirlos en la tabla
        int idProducto = producto.getIdproducto();
        String descripcion = producto.getDescripcion();
        int cantidadCompra = producto.getStockminimo(); //haremos una compra del stock minimo que debemos tener
        float pvp = producto.getPvp();
        //conversion de la fecha actual para introducirla con el formato sql correcto
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date fechaActual = new Date();
        String fecha = dateFormat.format(fechaActual);

        String sql = "INSERT INTO " + TABLE3 + "(idProducto, descripcion, cantidadCompra, pvp, fecha ) values ('" + idProducto + "', '" + descripcion + "', '"
                + cantidadCompra + "', '" + pvp + "', '" + fecha + "')";
        System.out.println(sql);

        try (Statement st = conection.createStatement()) {
            st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Datos añadidos a la tabla.");
            try (ResultSet rs = st.getGeneratedKeys()) {
                rs.next();
            }
        }
        System.out.println("\n***************************************************************************************************************************\n");
    }

    public void consultarPedidos() throws SQLException {

        Statement st = null;
        String sql = "SELECT * FROM " + TABLE2 + ";";
        conection = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);
        try {
            st = conection.createStatement();
            try (ResultSet rs = st.executeQuery(sql)) {
                int resultados = 0;

                while (rs.next()) {

                    int numeroPedido = rs.getInt("numeroPedido");
                    int idProducto = rs.getInt("idProducto");
                    String descripcion = rs.getString("descripcion");
                    int stockActual = rs.getInt("stockActual");
                    float pvp = rs.getFloat("pvp");
                    String fecha = rs.getString("fecha");
                    int cantidad = rs.getInt("cantidad");

                    System.out.println("NumPedido: " + numeroPedido + "\tidProducto: " + idProducto + "\tDescripcion: " + descripcion
                            + "\t\tStock Actual: " + stockActual + "\t\tPvp: " + pvp + "\t\tFecha: " + fecha + "\t\tCantidad: " + cantidad + "\n");
                    resultados++;
                }
                if (resultados == 0) {
                    System.out.println("No se ha encontrado ningun resultado.");
                    System.out.println("\n***************************************************************************************************************************\n");
                }
            }
            System.out.println("\n***************************************************************************************************************************\n");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void consultarCompras() throws SQLException {

        Statement st = null;
        String sql = "SELECT * FROM " + TABLE3 + ";";
        conection = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);
        try {
            st = conection.createStatement();
            try (ResultSet rs = st.executeQuery(sql)) {
                int resultados = 0;

                while (rs.next()) {
                    int idCompra = rs.getInt("idCompra");
                    int idProducto = rs.getInt("idProducto");
                    String descripcion = rs.getString("descripcion");
                    int cantidadCompra = rs.getInt("cantidadCompra");
                    float pvp = rs.getFloat("pvp");
                    String fecha = rs.getString("fecha");
                    System.out.println("idCompra: " + idCompra + "\tidProducto: " + idProducto + "\tDescripcion: " + descripcion + "\t\tcantidadCompra: " + cantidadCompra
                            + "\t\tPvp: " + pvp + "\t\tfecha: " + fecha + "\n");
                    resultados++;
                }
                if (resultados == 0) {
                    System.out.println("No se ha encontrado ningun resultado.");
                    System.out.println("\n***************************************************************************************************************************\n");
                }
            }
            System.out.println("\n***************************************************************************************************************************\n");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void actualizarStockProductos(int id, int nuevoValor ) throws SQLException {

        Statement st = null;
        String sql = "UPDATE " + TABLE1 + " SET stock " + " = '" + nuevoValor + "' WHERE id = " + id + "";        
        conection = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);
        try {
            st = conection.createStatement();
            st.executeUpdate(sql);
            System.out.println("Tabla productos actualizada.");
            System.out.println(sql);
            System.out.println("El stock actual del producto " + id + " es de: " + nuevoValor);
            System.out.println("\n***************************************************************************************************************************\n");

        } finally {
            if (st != null) {
                st.close();
            }
        }

    }

}
