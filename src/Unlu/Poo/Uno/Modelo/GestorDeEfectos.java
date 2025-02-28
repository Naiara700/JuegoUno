package Unlu.Poo.Uno.Modelo;

import java.rmi.RemoteException;

public class GestorDeEfectos{
    private JuegoUno juego;
    public GestorDeEfectos(JuegoUno juego) { this.juego = juego; }

    //LOGICA DE LAS CARTAS
    public void cambiarRonda() throws RemoteException {
        // Invertir la dirección utilizando el setter
        juego.setSentidoDeRonda(!juego.isSentidoDeRonda());
        if (juego.getJugadores().size()==2){
            bloqueo();
        }
    }
    public void bloqueo() throws RemoteException {
        juego.setJugadorActual(juego.obtenerSiguienteJugador());
    }
    public void mas4() throws RemoteException {
        Jugador siguienteJugador= juego.obtenerSiguienteJugador();
        for(int i = 0; i<4; i++) {//le hago robar 4. Ver si lo puedo mejorar
            siguienteJugador.robaCarta(juego.getMaso().sacarCarta());
        }
        bloqueo();
    }
    public void mas2() throws RemoteException {
        Jugador siguienteJugador= juego.obtenerSiguienteJugador();
        for(int i = 0; i<2; i++) {//le hago robar 2. Ver como sacar el for
            siguienteJugador.robaCarta(juego.getMaso().sacarCarta());
        }
        bloqueo();
    }
}
