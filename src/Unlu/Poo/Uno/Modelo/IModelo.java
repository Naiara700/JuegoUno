package Unlu.Poo.Uno.Modelo;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IModelo extends IObservableRemoto {
    //Geters
    ArrayList<Jugador> getJugadores() throws RemoteException;
    int getCantronda()throws RemoteException;
    PilaDeCartas getMaso() throws RemoteException;
    Jugador getGanador() throws RemoteException;
    //int getTurno() throws RemoteException;
    PilaDeCartas getDescarte() throws RemoteException;
    //int getMiEstado() throws RemoteException;
    Jugador getGanadorDeMano() throws RemoteException;
    Jugador getJugadorActual()throws RemoteException;
    //int getCantCartasA()throws RemoteException;
    int getCartasDescarteCant()throws RemoteException;
    ColorUno getColorActual()throws RemoteException;
    Carta getCartaActual()throws RemoteException;

    Carta obtenerUltimaCartaTirada() throws RemoteException;
    ArrayList<Carta> obtenerMano()throws RemoteException;
    Jugador obtenerSiguienteJugador()throws RemoteException;


    //Seters
    void setSentidoDeRonda(boolean sentidoDeRonda)throws RemoteException;
    void setJugadorActual(Jugador jugador)throws RemoteException;
    void setColorActual(ColorUno color)throws RemoteException;


    //Funciones
    void repartir()throws RemoteException;
    //void avanzarTurno() throws RemoteException;
    void reiniciarRonda()throws RemoteException;
    void robarCarta()throws RemoteException;
    void agregarJugador(String name)throws RemoteException;
    void terminaMano()throws RemoteException;
    void reiniciaJuego()throws RemoteException;
    void cambiarTurno()throws RemoteException;

    //Booleans
    //boolean thereAreCartas()throws RemoteException;
    boolean tirarCarta(int numero)throws RemoteException;
    boolean isSentidoDeRonda()throws RemoteException;


    //Inicio
    boolean inicioJuego()throws RemoteException;

}
