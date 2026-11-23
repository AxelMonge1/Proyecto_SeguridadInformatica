package org.itson;
import java.time.LocalDateTime;

public class SesionUsuario {
    private String nombreUsuario;
    private boolean autenticado;
    private final LocalDateTime horaConexion;

    public SesionUsuario() {
        this.nombreUsuario = "Invitado";
        this.autenticado = false;
        this.horaConexion = LocalDateTime.now();
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario.trim();
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }

    public LocalDateTime getHoraConexion() {
        return horaConexion;
    }
}