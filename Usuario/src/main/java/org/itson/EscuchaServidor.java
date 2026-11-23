package org.itson;

import java.io.BufferedReader;
import java.io.IOException;

public class EscuchaServidor implements Runnable{
    private final BufferedReader entradaServidor;

    public EscuchaServidor(BufferedReader entradaServidor) {
        this.entradaServidor = entradaServidor;
    }

    @Override
    public void run() {
        try {
            String respuesta;
            while ((respuesta = entradaServidor.readLine()) != null) {
                System.out.println("\n" + respuesta);
                if (respuesta.startsWith("ERROR:") || respuesta.startsWith("Desconectando")) {
                    System.exit(0);
                }
                System.out.print("> ");
            }
        } catch (IOException e) {
            System.out.println("\nSe cerró la conexión con el servidor.");
            System.exit(0);
        }
    }
}
