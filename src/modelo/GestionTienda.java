package modelo;

import Tiendabean.Pedido;
import Tiendabean.Producto;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 *
 */
public class GestionTienda {

    public void hacerPedido() {

        Scanner entradaInt = new Scanner(System.in);
        System.out.println("Selecione el Id del producto a comprar:");
        int id = entradaInt.nextInt();
        System.out.println("Selecione la cantidad de productos a comprar:");
        int cantidad = entradaInt.nextInt();

        //Se realiza un pedido
        Pedido pedido1 = new Pedido();
        ConexionBD conexion = new ConexionBD();
        Producto producto1;
        try {
            producto1 = conexion.buscarProducto(id);
            pedido1.setProducto(producto1);
            producto1.addPropertyChangeListener(pedido1);//Aviso de que esta accion puede generar un cambio
            pedido1.setCantidad(cantidad);
            int nuevo_stock = producto1.getStockactual() - pedido1.getCantidad(); //Decremento el stock
            producto1.setStockactual(nuevo_stock); //cambiamos el stock

            System.out.printf("PEDIDO REALIZADO -->Compro: " + pedido1.getCantidad() + " de " + pedido1.getProducto().getDescripcion() + "\n");

        } catch (SQLException ex) {
            Logger.getLogger(GestionTienda.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
