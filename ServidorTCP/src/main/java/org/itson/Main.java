package org.itson;

public class Main {
    static void main() {
        int puerto = 5000;
        int limiteUsuarios = 10;
        ServidorTCP servidor = new ServidorTCP(puerto, limiteUsuarios);
        servidor.iniciar();
    }
}
