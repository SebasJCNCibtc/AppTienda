package com.proyecto.apptienda.entidad;

import java.io.Serializable;

public class Producto implements Serializable {

        private int codigoProd;
        private String nombreProd;
        private String descripcionProd;
        private double precioProd;

    @Override
    public String toString() {
        return nombreProd; // Devuelve el nombre del producto para mostrar en el Spinner
    }

    // Constructor vacío
    public Producto() {
    }
    // Constructor
    public Producto(String nombreProd, String descripcionProd, double precioProd) {
        this.nombreProd = nombreProd;
        this.descripcionProd = descripcionProd;
        this.precioProd = precioProd;
    }

    public int getCodigoProd() {
            return codigoProd;
        }

        public void setCodigoProd(int codigoProd) {
            this.codigoProd = codigoProd;
        }

        public String getNombreProd() {
            return nombreProd;
        }

        public void setNombreProd(String nombreProd) {
            this.nombreProd = nombreProd;
        }

        public String getDescripcionProd() {
            return descripcionProd;
        }

        public void setDescripcionProd(String descripcionProd) {
            this.descripcionProd = descripcionProd;
        }

        public double getPrecioProd() {
            return precioProd;
        }

        public void setPrecioProd(double precioProd) {
            this.precioProd = precioProd;
        }
}
