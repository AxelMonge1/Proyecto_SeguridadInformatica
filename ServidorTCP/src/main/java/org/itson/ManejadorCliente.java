package org.itson;
import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ManejadorCliente implements Runnable {
    private final Socket socket;
    private final SesionUsuario sesion;
    private final AtomicInteger conexionesActivas;
    private final int maxConexiones;

    public ManejadorCliente(Socket socket, AtomicInteger conexionesActivas, int maxConexiones) {
        this.socket = socket;
        this.sesion = new SesionUsuario();
        this.conexionesActivas = conexionesActivas;
        this.maxConexiones = maxConexiones;
    }

    @Override
    public void run() {
        String ipCliente = socket.getInetAddress().getHostAddress();
        System.out.println("Nueva conexión desde " + ipCliente +
                " | Activos: " + conexionesActivas.get() + "/" + maxConexiones);
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            if (!ejecutarInicioSesion(entrada, salida)) {
                return;
            }
            salida.println("Bienvenido, " + sesion.getNombreUsuario() + ". Escribe '/SALIR' para desconectarte.");
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                if (mensaje.equalsIgnoreCase("/SALIR")) {
                    salida.println("Desconectando");
                    break;
                }
                System.out.println("[" + sesion.getNombreUsuario() + "]: " + mensaje);
                salida.println("Eco del servidor: " + mensaje);
            }
        } catch (IOException e) {
            System.out.println("Conexión perdida abruptamente con " + sesion.getNombreUsuario() + " (" + ipCliente + ")");
        } finally {
            cerrarConexion();
        }
    }

    private boolean ejecutarInicioSesion(BufferedReader entrada, PrintWriter salida) throws IOException {
        salida.println("Ingresa tu nombre de usuario:");
        String alias = entrada.readLine();
        if (alias == null) {
            return false;
        }
        sesion.setNombreUsuario(alias);
        //Aqui se va a validar la contraseña
        sesion.setAutenticado(true);
        return true;
    }

    private void cerrarConexion() {
        try {
            socket.close();
        } catch (IOException ignored) {}
        int restantes = conexionesActivas.decrementAndGet();
        System.out.println("Cliente desconectado (" + sesion.getNombreUsuario() + ")");
    }
}