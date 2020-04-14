package modelo;

import Tiendabean.Pedido;
import Tiendabean.Producto;

/**
 *
 * @author Daniel
 *
 */
public class GestionTienda {

    public void gestion() {

        //Introduzco los  productos de mi tienda
        Producto producto1 = new Producto(1, "Zapatillas nike", 3, 3, 100);//id, descripcion, stock, stockmin, pvp
        Producto producto2 = new Producto(2, "Zapatillas Reebok", 5, 2, 90);//id, descripcion, stock, stockmin, pvp
        Producto producto3 = new Producto(3, "Zapatillas Adidas", 15, 3, 120);//id, descripcion, stock, stockmin, pvp

        //Se realiza un pedido de unas zapatillas nike
        Pedido pedido1 = new Pedido();
        pedido1.setProducto(producto1);
        producto1.addPropertyChangeListener(pedido1);//Aviso de que esta accion puede generar un cambio
        pedido1.setCantidad(1);
        int nuevo_stock = producto1.getStockactual() - pedido1.getCantidad(); //Decremento el stock
        producto1.setStockactual(nuevo_stock); //cambiamos el stock

        System.out.printf("PEDIDO REALIZADO -->Compro: " + pedido1.getCantidad() + " de " + pedido1.getProducto().getDescripcion() + "\n");

    }
}
