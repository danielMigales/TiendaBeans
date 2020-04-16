package modelo;

import Tiendabean.Producto;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel Migales Puertas
 *
 */
public class ConexionBD {

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

    //insertar los datos en la base de datos a partir del arraylist
    public void insertarProductos() throws SQLException, ClassNotFoundException {

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

        PreparedStatement ps = (PreparedStatement) conection.prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE1
                + "(id SERIAL PRIMARY KEY, descripcion VARCHAR (150), stock int (11), stockmin int (11), pvp DOUBLE )");
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

        System.out.println("\n************************************************************************\n");
    }

    //lee los datos en la base de datos y los muestra por pantalla
    public void consultaProductos() throws SQLException {

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
                System.out.println("ID: " + id + "\tDescripcion: " + descripcion + "\tStock: " + stock + "\t\tStock minimo: " + stockmin + "\t\tPvp: " + pvp + "\n");
                resultados++;
            }
            if (resultados == 0) {
                System.out.println("No se ha encontrado ningun resultado.");
                System.out.println("\n*********************************************************************\n");
            }
            rs.close();
            System.out.println("\n*************************************************************************\n");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public Producto buscarProducto(int id) throws SQLException {

        Statement st = null;
        String sql = "SELECT * FROM " + TABLE1 + " WHERE id = " + id + ";";
        System.out.println(sql);
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
                System.out.println("\n**************************************************\n");
            }
            rs.close();
            System.out.println("\n**************************************************\n");
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return producto1;
    }

    public void insertarPedidos() {

    }

}
