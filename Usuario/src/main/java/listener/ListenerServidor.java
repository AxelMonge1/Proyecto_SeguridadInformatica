/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package listener;

/**
 *
 * @author axelm
 */
public interface ListenerServidor {
    void onLoginExitoso(String mensaje);
    void onLoginFallido(String motivo);
    void onErrorConexion(String error);
}
