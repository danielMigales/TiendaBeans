package Tiendabean;

import java.beans.*;
import java.io.Serializable;

/**
 *
 *
 */
public class Producto implements Serializable {//BEAN FUENTE

    private String descripcion;
    private int idproducto;
    private int stockactual;//PROPIEDAD COMPARTIDA QUE Si esta propiedad cambia, susceptible de lanzar un evento
    private int stockminimo;
    private float pvp;

    private PropertyChangeSupport propertySupport;//BEAN FUENTE

    public Producto() {//BEAN FUENTE
        propertySupport = new PropertyChangeSupport(this);
    }

    public Producto(int idproducto, String descripcion,
            int stockactual, int stockminimo, float pvp) {
        propertySupport = new PropertyChangeSupport(this);//BEAN FUENTE
        this.idproducto = idproducto;
        this.descripcion = descripcion;
        this.stockactual = stockactual;
        this.stockminimo = stockminimo;
        this.pvp = pvp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getStockactual() {
        return stockactual;
    }

    public void setStockactual(int valorNuevo) {//BEAN FUENTE
        int valorAnterior = this.stockactual;
        this.stockactual = valorNuevo;

        if (this.stockactual < getStockminimo()) //hay que realizar compra de material
        {
            System.out.printf("SOY EL BEAN FUENTE: Se ha alacanzado el stock minimo, paso info al receptor %n ");

            propertySupport.firePropertyChange("stockactual",//LANZAMOS EL EVENTO AL RECEPTOR ENVIANDOLE DATOS
                    valorAnterior, this.stockactual);

        }

    }

    public int getStockminimo() {
        return stockminimo;
    }

    public void setStockminimo(int stockminimo) {
        this.stockminimo = stockminimo;
    }

    public float getPvp() {
        return pvp;
    }

    public void setPvp(float pvp) {
        this.pvp = pvp;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

}
