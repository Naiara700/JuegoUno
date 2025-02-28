package Unlu.Poo.Uno.Vista;

import Unlu.Poo.Uno.Controlador.Controlador;
import Unlu.Poo.Uno.Modelo.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {
    void setControlador(Controlador controlador);

    void reiniciarMano();

    void finJuego();

    void notificarMensaje(String string);

    void verTop();

    void mostrarDescarte(Carta carta) ;

    void mostrarMazo(PilaDeCartas mazo) ;


    void habilitaBotones();


    //void menuiniciado(String opcion);

    void verificarTurno();

    boolean isTurno();

    void mostrarMano(ArrayList<Carta> mano);

    void limpiarPantalla();

    void mostrarMenuEleguirColor();

    void mostrarPuntos(ArrayList<Jugador> jugadores)throws RemoteException;

    //ColorUno eleguirColor(String color) throws RemoteException;

     void mostrarMazoYDescarte();
     void setPunto(boolean p);
}
