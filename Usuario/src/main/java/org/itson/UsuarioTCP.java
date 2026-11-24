package org.itson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import listener.ListenerServidor;

public class UsuarioTCP {
    private final String host;
    private final int puerto;
    private Socket socket;
    private PrintWriter salidaServidor;
    private BufferedReader entradaServidor;

    public UsuarioTCP(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public void conectar(ListenerServidor listener) throws IOException {
        socket = new Socket(host, puerto);
        entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salidaServidor = new PrintWriter(socket.getOutputStream(), true);
        Thread hiloEscucha = new Thread(new EscuchaServidor(entradaServidor, listener));
        hiloEscucha.setDaemon(true);
        hiloEscucha.start();
    }

    public void enviarCredenciales(String usuario, String contrasenia) {
        if (salidaServidor != null) {
            salidaServidor.println("LOGIN|" + usuario + "|" + contrasenia);
        }
    }

    public void desconectar() {
        try {
            if (salidaServidor != null) salidaServidor.println("SALIR");
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }
}