package Unlu.Poo.Uno;

import Unlu.Poo.Uno.Controlador.Controlador;
import Unlu.Poo.Uno.Modelo.JuegoUno;
import Unlu.Poo.Uno.Vista.VistaConsola;
import Unlu.Poo.Uno.Vista.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppClienteUno {
        public static void main(String[] args) {
            String[] opciones = {"Vista Gráfica", "Vista Consola"};
            int seleccion = JOptionPane.showOptionDialog(
                    null,
                    "¿Qué tipo de vista desea usar?",
                    "Seleccionar Vista",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );
            /////
            ArrayList<String> ips = Util.getIpDisponibles();
            String ip = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione la IP en la que escuchar� peticiones el cliente", "IP del cliente",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ips.toArray(),
                    null
            );
            String port = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione el puerto en el que escuchar� peticiones el cliente", "Puerto del cliente",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    9999
            );
            String ipServidor = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione la IP en la corre el servidor", "IP del servidor",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
            );
            String portServidor = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    8888
            );
            Controlador controlador = new Controlador();
            Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
            // Iniciar la vista seleccionada
            if (seleccion == 0) { // Vista Gráfica
                VistaGrafica vista = new VistaGrafica(controlador);
                controlador.setVista(vista);
                vista.setVisible(true);
            } else { // Vista Consola
                VistaConsola vista = new VistaConsola(controlador);
                controlador.setVista(vista);
                //vista.mostrarMensaje("Iniciando en modo consola...");
                vista.setVisible(true);
            }


            try {
                c.iniciar(controlador);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RMIMVCException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
}

