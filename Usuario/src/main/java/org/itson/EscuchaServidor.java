package org.itson;

import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.SwingUtilities;
import listener.ListenerServidor;

public class EscuchaServidor implements Runnable {
    private final BufferedReader entradaServidor;
    private final ListenerServidor listener;

    public EscuchaServidor(BufferedReader entradaServidor, ListenerServidor listener) {
        this.entradaServidor = entradaServidor;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            String respuesta;
            while ((respuesta = entradaServidor.readLine()) != null) {
                final String msg = respuesta;

                SwingUtilities.invokeLater(() -> {
                    if (msg.startsWith("LOGIN_OK:")) {
                        listener.onLoginExitoso(msg.replace("LOGIN_OK:", ""));
                    } else if (msg.startsWith("LOGIN_ERROR:")) {
                        listener.onLoginFallido(msg.replace("LOGIN_ERROR:", ""));
                    } else if (msg.startsWith("ERROR:")) {
                        listener.onErrorConexion(msg.replace("ERROR:", ""));
                    }
                });
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> 
                listener.onErrorConexion("Se perdió la conexión con el servidor.")
            );
        }
    }
}