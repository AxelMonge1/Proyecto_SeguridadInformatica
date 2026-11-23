package org.itson;

public class Main {
    static void main() {
        String host = "localhost";
        int puerto = 5000;
        UsuarioTCP usuario = new UsuarioTCP(host, puerto);
        usuario.conectar();
    }
}
