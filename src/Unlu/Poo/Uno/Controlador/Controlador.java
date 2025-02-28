package Unlu.Poo.Uno.Controlador;

import Unlu.Poo.Uno.Modelo.*;
import Unlu.Poo.Uno.Vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controlador implements IControladorRemoto {
    private IVista vista;
    private boolean RobarCarta;
    private IModelo juegoUno;
    private boolean juegoIniciado = false;
    public Controlador() {
        //this.juegoUno =  juegoUno;//ahora es remoto
        //this.juegoUno.registrar(this);
        RobarCarta = true;
    }
    public void setVista(IVista vista2) {
        this.vista = vista2;
    }

    public void agregarJugador(String name) {
        try {
            juegoUno.agregarJugador(name);
        }catch (RemoteException e){
            vista.notificarMensaje("Se produjo una excepción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Jugador getGanador(){
        Jugador ganador = null;
        try {
            ganador= juegoUno.getGanador();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ganador;
    }

    public String getGanadorMano(){
        String ganador = null;
        try {
            ganador = juegoUno.getGanadorDeMano().getNombre();
        }catch (RemoteException e) {
            e.printStackTrace();
        }
        return  ganador;
    }

    public boolean iniciarJuego(){
        boolean inicia=false;
        try {
            inicia = juegoUno.inicioJuego();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return inicia;
    }

    public ArrayList<Jugador> getJugadores(){
        ArrayList<Jugador> jugadores = new ArrayList<>(); ;
        try{
            // Lista vacía como fallback
            jugadores = juegoUno.getJugadores();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return jugadores;
    }


    public Carta getDescarte(){
        try {
            return juegoUno.obtenerUltimaCartaTirada();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Jugador getJugadorEnTurno(){
        Jugador jugador = null;
        try {
            jugador = juegoUno.getJugadorActual();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return jugador;
    }

    public void robarCarta() {
        if(RobarCarta) {
            try {
                juegoUno.robarCarta();
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
        RobarCarta=false;
    }

    public ArrayList<Carta> getManoDeJugador(){
        ArrayList<Carta> mano = new ArrayList<>();
        try{
            mano = juegoUno.obtenerMano();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return mano;
    }

    public boolean tirarCarta(int numero) throws RemoteException {
        boolean x = false;
        if(numero>getManoDeJugador().size()|| numero<1){//ver si hay que poner el +1 o nop
            vista.notificarMensaje("NO TIENES LA CARTA NUMERO "+ numero);
            return false;
        }
        Carta carta = juegoUno.getJugadorActual().getMano().obtenerCartas().get(numero - 1);
        ColorUno colorUno = juegoUno.getColorActual();
        Carta cartaActual = juegoUno.getCartaActual();
        if (carta.esJugableSobre(cartaActual, colorUno)){
            // Lógica para jugar la carta
            x = juegoUno.tirarCarta(numero);
            if(x) {
                RobarCarta = true;
            }else {
                RobarCarta = false;
            }
            if(!isEspecial()){
                cambiarTurno();
            }else{
                vista.mostrarMenuEleguirColor();
            }
            return true;
        } else {
            vista.notificarMensaje("LA CARTA NO ES JUGABLE.");
            return false;
        }
    }

    public boolean isEspecial() throws RemoteException {
        Carta carta = juegoUno.getCartaActual();
        ColorUno colorUno = carta.getColor();
        return colorUno == ColorUno.NEGRO;
    }


    public void reiniciarMano(){
        try {
            juegoIniciado= false;
            juegoUno.reiniciarRonda();
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void reiniciarPartida(){
        try {
            juegoIniciado= false;
            juegoUno.reiniciaJuego();
        }catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String jugadorActual(){
        String nombre=null;
        try {
            Jugador jugadorAct = juegoUno.getJugadorActual();
            nombre=jugadorAct.getNombre();
        } catch (RemoteException e) {
            vista.notificarMensaje("upss");
            e.printStackTrace();
        }
        return nombre;
    }

    public void cambiarTurno(){
        try {
            juegoUno.cambiarTurno();
        }catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void actualizarVistaTurno() {
        if(jugadorActual()!=null){
            vista.verificarTurno();
        }
    }

    public void solicitarColor(ColorUno nuevoColorUno) throws RemoteException {
        juegoUno.setColorActual(nuevoColorUno);
    }

    public ColorUno getColorActual() throws RemoteException {
        return juegoUno.getColorActual();
    }

    public void paso(){
        try {
            if (!RobarCarta) {
                juegoUno.cambiarTurno();
            }
            RobarCarta = true;
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public PilaDeCartas getMazo(){
        try {
            return juegoUno.getMaso();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Carta getCartaActual(){
        try{
            return juegoUno.getCartaActual();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existeJugador(String nombreJugador) {
        boolean existe = false;
        try {
            ArrayList<Jugador> jugadores = new ArrayList<>();
            if (juegoUno== null){
                vista.notificarMensaje("el juego esta en nulo");
            }
            jugadores = juegoUno.getJugadores();
            for (Jugador j : jugadores) {
                if (j.getNombre().equals(nombreJugador)) {
                    existe = true;
                }
            }
        } catch (Exception e) {
            vista.notificarMensaje("Se produjo una excepción: " + e.getMessage());
            e.printStackTrace();
        }
        return existe;
    }

    public int getCantidadDeRondas(){
        int cant = 0;
        try{
            cant = juegoUno.getCantronda();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return cant;
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.juegoUno=(IModelo) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object evento) throws RemoteException {
        switch (evento) {
            case EventosUno.JUEGO_INICIADO:
                if(!juegoIniciado) {
                    vista.mostrarMazo(getMazo());
                    vista.mostrarDescarte(getDescarte());
                    actualizarVistaTurno();
                    juegoIniciado = true;
                }
                break;

            case EventosUno.FIN_JUEGO:
                vista.mostrarPuntos(getJugadores());
                vista.habilitaBotones();
                vista.finJuego();
               // vista.setPunto(false);
                reiniciarPartida();
                break;

            case EventosUno.PUNTOS_CAMBIADOS:
                vista.limpiarPantalla();
                vista.mostrarPuntos(getJugadores());
                vista.reiniciarMano();
                break;

            case EventosUno.JUGADORES_MAXIMOS:
                vista.notificarMensaje("Se ha alcanzado el máximo de jugadores.");
                break;

            case EventosUno.FALTAN_JUGADORES:
                vista.notificarMensaje("FALTAN JUGADORES PARA INICIAR EL JUEGO...");
                break;

            case EventosUno.CARTA_ROBADA:
                if (vista.isTurno())
                    vista.mostrarMano(getManoDeJugador());
                break;

            case EventosUno.TURNO_CAMBIADO:
                //vista.limpiarPantalla();//Ver como hacer para cuando cambian los puntos
                //vista.notificarMensaje("TURNO_CAMBIADO");
                vista.mostrarMazoYDescarte();
                //vista.mostrarDescarte(getDescarte());
                actualizarVistaTurno();
                break;
            case EventosUno.MISMO_NOMBRE:
                vista.notificarMensaje("El nombre del jugador ya está en uso. Elige otro nombre.");
                break;

            case EventosUno.MAS4:
                vista.notificarMensaje("¡El jugador: "+ jugadorActual() +" debe robar 4 cartas!");

                break;

            case EventosUno.MAS2:
                vista.notificarMensaje("¡El jugador: "+ jugadorActual() +" debe robar 2 cartas!");
                break;

            case EventosUno.BLOQUEO:
                vista.notificarMensaje("¡Turno bloqueado!");
                break;

            case EventosUno.CAMBIARONDA:
                vista.notificarMensaje("Se ha cambiado el sentido de la ronda.");
                break;

            case EventosUno.UNO:
                vista.notificarMensaje("¡UNO! Al jugador " + jugadorActual() +" le queda una carta.");
                break;
            case EventosUno.CAMBIACOLOR:
                vista.notificarMensaje("El color a cambiado a: "+ getColorActual().toString());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + evento);
        }
    }
}
