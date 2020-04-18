package menu;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.ConexionBD;
import modelo.GestionTienda;

/**
 *
 * @author Daniel Migales Puertas
 *
 */
public class Menu {

    String ANSI_BLACK = "\u001B[30m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSI_PURPLE = "\u001B[35m";
    String ANSI_CYAN = "\u001B[36m";
    String ANSI_WHITE = "\u001B[37m";
    String ANSI_RESET = "\u001B[0m";

    //Menu y llamada a metodos para las funciones del programa
    public void menuPrincipal() {

        Scanner entrada = new Scanner(System.in);
        int seleccion;
        boolean salir = true;

        do {
            System.out.println("\n************************************************************MENU PRINCIPAL*****************************************************\n");
            System.out.println(ANSI_BLUE + "1. Mostrar catalogo de productos." + ANSI_RESET); //carga la tabla productos de la base de datos
            System.out.println(ANSI_BLUE + "2. Hacer pedido. " + ANSI_RESET); //Inserta en la tabla pedidos y descuenta en la tabla productos
            System.out.println(ANSI_BLUE + "3. Añadir producto nuevo a la base de datos (tabla Productos). " + ANSI_RESET); //introduce un producto en la base de datos de productos
            System.out.println(ANSI_BLUE + "4. Consultar pedidos realizados (mostrar tabla pedidos). " + ANSI_RESET);
            System.out.println(ANSI_BLUE + "5. Consultar compras realizadas (mostrar tabla compras). " + ANSI_RESET);
            System.out.println(ANSI_BLUE + "6 Salir del programa.\n" + ANSI_RESET);
            System.out.println("Seleccione una opcion:");
            seleccion = entrada.nextInt();
            System.out.println("\n*******************************************************************************************************************************\n");

            switch (seleccion) {
                case 1:
                try {
                    ConexionBD conexion = new ConexionBD();
                    conexion.consultarProductos();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

                case 2:
                    GestionTienda pedido = new GestionTienda();
                    pedido.hacerPedido();
                    break;

                case 3:
                try {
                    ConexionBD conexion = new ConexionBD();
                    conexion.insertarProductos();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

                case 4:
                    try {
                    ConexionBD conexion = new ConexionBD();
                    conexion.consultarPedidos();
                } catch (SQLException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

                case 5:
                    
                    try {
                    ConexionBD conexion = new ConexionBD();
                    conexion.consultarCompras();
                } catch (SQLException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

                case 6:
                    salir = false;
                    break;

            }
        } while (salir);
    }

}
