package com.example.gerald.informed_city;

public class Evento {

    private int numero;
    private String nombre;
    private String creador;
    private String categoria;
    private String descripcion;
    private float latitud;
    private float longitud;

    public Evento(){}

    public Evento(int numero,String nombre,String descripcion,String creador,String categoria,float latitud,float longitud){
        this.numero = numero;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creador = creador;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public void setNumero(int numero){
        this.numero=numero;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    public void setDescripcion(String descripcion){
        this.descripcion=descripcion;
    }
    public void setCreador(String creador){
        this.creador = creador;
    }
    public  void setCategoria(String categoria){
        this.categoria = categoria;
    }
    public void setLatitud(float latitud){
        this.latitud=latitud;
    }
    public void setLongitud(float longitud){
        this.longitud=longitud;
    }

    public String getNombre(){
        return this.nombre;
    }
    public String getDescripcion(){
        return this.descripcion;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public String getCreador() {
        return this.creador;
    }

    public int getNumero(){
        return this.numero;
    }
    public float getLatitud(){
        return this.latitud;
    }
    public float getLongitud(){
        return this.longitud;
    }
}
