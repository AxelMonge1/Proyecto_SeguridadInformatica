package org.itson;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorTCP {
    private final int puerto;
    private final int maxConexiones;
    private final AtomicInteger conexionesActivas;
    private final ExecutorService poolHilos;
    private static final List<Usuario> usuariosRegistrados = new ArrayList<>();
    private static final Set<String> usuariosConectados = ConcurrentHashMap.newKeySet();

    public ServidorTCP(int puerto, int maxConexiones) {
        this.puerto = puerto;
        this.maxConexiones = maxConexiones;
        this.conexionesActivas = new AtomicInteger(0);
        this.poolHilos = Executors.newFixedThreadPool(maxConexiones);
        usuariosRegistrados.add(new Usuario("AxelMonge", "Pass1234"));
    }
    
    public static String validarLogin(String nombre, String contrasenia) {
        for (Usuario u : usuariosRegistrados) {
            if (u.getNombre().equals(nombre) && u.getContrasenia().equals(contrasenia)) {
                if (!usuariosConectados.add(nombre)) {
                    return "Este usuario ya tiene una sesión activa";
                }
                return "OK";
            }
        }
        return "Usuario o contraseña incorrectos";
    }

    public static void liberarUsuario(String nombre) {
        if (nombre != null) {
            usuariosConectados.remove(nombre);
        }
    }

    public void iniciar() {
        System.out.println("Iniciando servidor TCP en el puerto " + puerto);
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor listo");
            while (true) {
                Socket socketCliente = serverSocket.accept();
                if (conexionesActivas.get() >= maxConexiones) {
                    rechazarCliente(socketCliente);
                    continue;
                }
                conexionesActivas.incrementAndGet();
                poolHilos.execute(new ManejadorCliente(socketCliente, conexionesActivas, maxConexiones));
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        } finally {
            detener();
        }
    }

    public void detener() {
        poolHilos.shutdown();
    }

    private void rechazarCliente(Socket socket) {
        try (PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {
            salida.println("Servidor lleno. Intenta más tarde.");
            System.out.println("Conexión rechazada desde " + socket.getInetAddress().getHostAddress() + " (Límite alcanzado).");
        } catch (IOException e) {
            System.err.println("Error al enviar aviso de rechazo: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}