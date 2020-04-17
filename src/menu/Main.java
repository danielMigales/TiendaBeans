package menu;

/**
 *
 * @author Daniel Migales Puertas
 *
 */
//
//Ejercicio1.Tienda de zapatillas.
//A partir del ejemplo adjuntado y visto en clase de tienda online de zapatillas:
//
//- Añade una base de datos que almacene productos y pedidos, compras, donde
//
//	Productos: Sera el stock que dispone la tienda.
//	Pedidos: Los pedidos de productos que hagan los clientes.
//      Compras: Siempre que un producto llegue al stock mínimo, se deberá de lanzar un evento, que haga una compra de material de dicho producto.
//
//- Mejora el main, de manera que se pueda mostrar al usuario el catalogo de producto que dispone la tienda (Mostrar los productos de la base de datos)
//
//- Permite al usuario tramitar un pedido. (Inserta en la tabla pedidos y descuenta en la tabla productos)
//
//- En caso de que un pedido genere un stock mínimo, el sistema deberá de generar una compra de paliar el stock, dicho pedido debe ser del equivalente al stock mínimo que hay.
// (Crear una entrada en la tabla de compras. No hace falta que actualice la de productos.)
//
//
public class Main {

    public static void main(String[] args) {

        //inicia el menu principal
        Menu menu = new Menu();
        menu.menuPrincipal();

    }

}
