package modelo;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public void insertarProductos(String descripcion, int stock, int stockmin, double pvp) throws SQLException, ClassNotFoundException {

        Class.forName(DRIVER);
        conection = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);

        String sql = "INSERT INTO " + TABLE1 + "(id, descripcion, stock, stockmin, pvp) values ('" + descripcion + "', '"
                + stock + "', '" + stockmin + "', '" + pvp + "')";
        System.out.println(sql);

        try (Statement st = conection.createStatement()) {
            st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Datos añadidos a la tabla.");
            try (ResultSet rs = st.getGeneratedKeys()) {
                rs.next();
            }
        }

        System.out.println("\n**************************************************\n");
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
                double pvp = rs.getDouble("pvp");
                System.out.println("ID: " + id + "\nDescripcion: " + descripcion + "\nstock: " + stock + "\nstockmin: " + stockmin + "\npvp: " + pvp + "\n");
                resultados++;
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
    }

}
