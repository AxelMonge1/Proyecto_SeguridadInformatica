package org.itson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class UsuarioTCP {
    private final String host;
    private final int puerto;

    public UsuarioTCP(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public void conectar() {
        try (
                Socket socket = new Socket(host, puerto);
                BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);
                Scanner scannerConsola = new Scanner(System.in)
        ) {
            Thread hiloEscucha = new Thread(new EscuchaServidor(entradaServidor));
            hiloEscucha.setDaemon(true);
            hiloEscucha.start();
            while (scannerConsola.hasNextLine()) {
                String texto = scannerConsola.nextLine();
                salidaServidor.println(texto);
                if (texto.equalsIgnoreCase("/SALIR")) {
                    break;
                }
            }
        } catch (ConnectException e) {
            System.err.println("No se pudo conectar");
        } catch (IOException e) {
            System.err.println("Error de red: " + e.getMessage());
        }
    }
}
