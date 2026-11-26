/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson;

/**
 *
 * @author axelm
 */
public class Usuario {
    private String nombre;
    private String contrasenia;
    private boolean esAdmin;

    public Usuario() {
    }

    public Usuario(String nombre, String contrasenia, boolean esAdmin) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.esAdmin = esAdmin;
    }

    public boolean isAdmin() {
        return esAdmin;
    }

    public void setAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}