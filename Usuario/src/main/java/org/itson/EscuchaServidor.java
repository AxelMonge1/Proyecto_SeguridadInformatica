package org.itson;

import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.SwingUtilities;
import listener.ListenerServidor;

public class EscuchaServidor implements Runnable {
    private final BufferedReader entrada;
    private final ListenerServidor listener;

    public EscuchaServidor(BufferedReader entrada, ListenerServidor listener) {
        this.entrada = entrada;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            String respuesta;
            while ((respuesta = entrada.readLine()) != null) {
                final String msg = respuesta;
                if (respuesta.startsWith("LOGIN_OK|")) {
                    String[] partes = respuesta.split("\\|", 3);
                    if (partes.length == 3) {
                        boolean esAdmin = partes[1].equals("ADMIN");
                        String textoBienvenida = partes[2];
                        SwingUtilities.invokeLater(() -> listener.onLoginExitoso(textoBienvenida, esAdmin));
                    }
                } else if (respuesta.startsWith("LOGIN_ERROR:")) {
                    String motivo = respuesta.substring("LOGIN_ERROR:".length());
                    SwingUtilities.invokeLater(() -> listener.onLoginFallido(motivo));
                }
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> listener.onErrorConexion("Se perdió la conexión con el servidor."));
        }
    }
}