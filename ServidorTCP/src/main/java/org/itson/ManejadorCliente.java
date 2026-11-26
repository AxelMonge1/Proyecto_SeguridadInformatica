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
        String linea;
        while ((linea = entrada.readLine()) != null) {
            if (linea.equalsIgnoreCase("/SALIR")) {
                return false;
            }
            if (linea.startsWith("LOGIN|")) {
                String[] partes = linea.split("\\|");
                if (partes.length == 3) {
                    String user = partes[1];
                    String pass = partes[2];
                    String resultado = ServidorTCP.validarLogin(user, pass);

                    if (resultado.equals("OK_ADMIN") || resultado.equals("OK_USER")) {
                        sesion.setNombreUsuario(user);
                        sesion.setAutenticado(true);
                        String rol = resultado.equals("OK_ADMIN") ? "ADMIN" : "USER";
                        salida.println("LOGIN_OK|" + rol + "|Bienvenido, " + user);
                        return true;
                    } else {
                        salida.println("LOGIN_ERROR:" + resultado);
                        if (resultado.contains("sesión activa")) {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void cerrarConexion() {
        try {
            socket.close();
        } catch (IOException e) {}
        if (sesion.isAutenticado()) {
            ServidorTCP.liberarUsuario(sesion.getNombreUsuario());
        }
        int restantes = conexionesActivas.decrementAndGet();
        System.out.println("Cliente desconectado (" + sesion.getNombreUsuario() + ")");
    }
}