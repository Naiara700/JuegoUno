package Unlu.Poo.Uno.Modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class JuegoUno extends ObservableRemoto implements IModelo{
    private static JuegoUno instancia;
    private int cantronda = 0;
    private MazoPrincipal mazo;
    private PilaDeDescarte descarte;
    private ArrayList<Jugador> jugadores;
    private Jugador ganador;
    private final int MAX_PUNTOS = 500;
    private Jugador jugadorActual;
    private boolean sentidoDeRonda; //sentido horario
    private ColorUno colorUnoActual;
    private Carta cartaActual; //ver si me sirve mas
    private GestorDeEfectos gestorDeEfectos;
    private Jugador ganadorDeMano;
    public static JuegoUno getInstance(){
        if(instancia == null){
            instancia = new JuegoUno();
        }
        return instancia;
    }

    public JuegoUno() {
        this.jugadores = new ArrayList<>();
        this.mazo = null;
        this.descarte = null;
        this.ganador = null;
        this.gestorDeEfectos= new GestorDeEfectos(this);
        this.ganadorDeMano = null;
        this.sentidoDeRonda = true;
    }

    @Override
    public ArrayList<Jugador> getJugadores() throws RemoteException {
        return this.jugadores;
    }

    @Override
    public int getCantronda() throws RemoteException {
        return cantronda;
    }

    @Override
    public PilaDeCartas getMaso() throws RemoteException {
        return this.mazo;
    }

    @Override
    public Jugador getGanador() throws RemoteException {
        return this.ganador;
    }


    @Override
    public PilaDeCartas getDescarte() throws RemoteException {
        return null;
    }


    @Override
    public Jugador getGanadorDeMano() throws RemoteException {
        return ganadorDeMano;
    }

    public Carta getCartaActual()throws RemoteException{
        return cartaActual;
    }

    @Override
    public Jugador getJugadorActual() throws RemoteException {
        return jugadorActual;
    }


    @Override
    public int getCartasDescarteCant() throws RemoteException {
        return descarte.cantDeCartas();
    }

    @Override
    public ColorUno getColorActual() throws RemoteException {
        return colorUnoActual;
    }

    @Override
    public Carta obtenerUltimaCartaTirada() throws RemoteException {
        cartaActual = descarte.obtenerUltimaCarta();
        return cartaActual;
    }

    @Override
    public ArrayList<Carta> obtenerMano() throws RemoteException {
        return jugadorActual.getMano().obtenerCartas();
    }

    @Override
    public Jugador obtenerSiguienteJugador() throws RemoteException {
        int indiceActual = jugadores.indexOf(jugadorActual);
        int siguienteIndice = 0;
        if (jugadores.size() > 2) {
            if (sentidoDeRonda) {
                siguienteIndice = (indiceActual + 1) % jugadores.size();
            } else {
                siguienteIndice = (indiceActual - 1 + jugadores.size()) % jugadores.size();
            }
        } else {
            // Caso especial para 2 jugadores
            siguienteIndice = (indiceActual + 1) % jugadores.size();
        }
        return jugadores.get(siguienteIndice);
    }

    @Override
    public void setSentidoDeRonda(boolean sentidoDeRonda) throws RemoteException {
        this.sentidoDeRonda = sentidoDeRonda;
    }

    @Override
    public void setJugadorActual(Jugador jugador) throws RemoteException {
        jugadorActual=jugador;
    }

    @Override
    public void setColorActual(ColorUno colorUno) throws RemoteException {
        colorUnoActual = colorUno;
        notificarObservadores(EventosUno.CAMBIACOLOR);
    }

    @Override
    public void repartir() throws RemoteException {
        for(int i =0; i<3;i++){//repartir 7 cartas cada uno
            for(Jugador j : jugadores){
                j.robaCarta(mazo.sacarCarta());
            }
        }
        descarte.agregarCarta(mazo.sacarCarta());//le pongo la primer carta
        cartaActual= descarte.obtenerUltimaCarta();
        while (cartaActual instanceof CartaEspecial /*&& ( ((CartaEspecial) cartaActual).getTipo() == TipoDeCartaEspecial.CAM_COLOR|| ((CartaEspecial) cartaActual).getTipo() == TipoDeCartaEspecial.MASCUATRO)*/) {
            descarte.vaciarCartas();
            descarte.agregarCarta(mazo.sacarCarta());
            cartaActual = descarte.obtenerUltimaCarta();
        }
        /*if(cartaActual instanceof CartaEspecial){
            procesarEfectosCartaEspecial((CartaEspecial)cartaActual);
        }*/
    }

    @Override
    public void reiniciarRonda() throws RemoteException {
        cantronda++;
        descarte.vaciarCartas();
        for(Jugador j : jugadores){
            j.limpiarMano();
        }
        this.sentidoDeRonda = true;
        mazo = new MazoPrincipal();
        mazo.mezclarMazo();
        if(cantronda<jugadores.size()) {
            jugadorActual = jugadores.get(cantronda);
        }
        else{
            jugadorActual=jugadores.get(0);
        }
        repartir();
        notificarObservadores(EventosUno.JUEGO_INICIADO);
    }

    @Override
    public void robarCarta() throws RemoteException {
        if (mazo.isVacio()) {
            descarte.reciclarEnMazo(mazo);
        }
        jugadorActual.robaCarta(mazo.sacarCarta());
        notificarObservadores(EventosUno.CARTA_ROBADA);
    }

    @Override
    public void agregarJugador(String name) throws RemoteException {
        if (this.jugadores.size() < 10) {
            this.jugadores.add(new Jugador(name));
        }
        else {
            notificarObservadores(EventosUno.JUGADORES_MAXIMOS);
        }
    }

    @Override
    public void terminaMano() throws RemoteException {
        if (jugadorActual.getMano().cantidadDeCartas() == 0) {
            calcularPuntos(); // Calcula los puntos del jugador actual (los suma)
            if (jugadorActual.getPuntos() >= MAX_PUNTOS) {
                this.ganador = jugadorActual;
                notificarObservadores(EventosUno.FIN_JUEGO);
            } else {
                notificarObservadores(EventosUno.PUNTOS_CAMBIADOS);
            }
        }
    }

    private void calcularPuntos() {
        int puntos = 0;
        for (Jugador jugador : jugadores) {
            if (!jugador.equals(jugadorActual)) {//sumo todos menos el actual. Va a ser el ganador
                puntos += jugador.getMano().calcularPuntosM();
            }
        }
        jugadorActual.aumentarPuntos(puntos);
    }


    @Override
    public void reiniciaJuego() throws RemoteException {
        descarte.vaciarCartas();
        for(Jugador j : jugadores){
            j.limpiarMano();
        }
        this.sentidoDeRonda = true;
        this.cantronda = 0;
    }

    @Override
    public void cambiarTurno() throws RemoteException {
        jugadorActual.setTurno(false);
        jugadorActual= obtenerSiguienteJugador();
        jugadorActual.setTurno(true);
        notificarObservadores(EventosUno.TURNO_CAMBIADO);
    }


    @Override
    public boolean tirarCarta(int numero) throws RemoteException {
        Carta carta;
        carta = jugadorActual.getMano().obtenerCartas().get(numero - 1);
        // Validar si el jugador tiene la carta seleccionada
        if (!jugadorActual.getMano().contieneCarta(carta)) {
            return false;
        }
        // Validar si la carta es jugable
        if (!carta.esJugableSobre(cartaActual, colorUnoActual)) {
            return false;
        }
        // Jugar la carta
        jugadorActual.desacartar(numero - 1);
        descarte.agregarCarta(carta);
        colorUnoActual = carta.getColor();
        this.cartaActual = carta; // Actualizar la carta en juego

        // Aplicar efectos si es una carta especial
        if (carta.getClass() == CartaEspecial.class) {
            procesarEfectosCartaEspecial((CartaEspecial) carta);
        }

        // Verificar si el jugador ganó la mano
        if (jugadorActual.getMano().cantidadDeCartas() == 0) {
            ganadorDeMano = jugadorActual;
            terminaMano();
            return true;
        }

        // Verificar si el jugador tiene una sola carta (UNO)
        if (jugadorActual.getMano().cantidadDeCartas() == 1) {
            notificarObservadores(EventosUno.UNO);
        }
        return true;
    }


    private void procesarEfectosCartaEspecial(CartaEspecial carta) throws RemoteException {
        if (jugadorActual.getMano().cantidadDeCartas() == 0) { // La mano ha terminado, no solicitar color.
            return; }
        switch (carta.getTipo()) {
            case BLOQUEO:
                gestorDeEfectos.bloqueo();
                notificarObservadores(EventosUno.BLOQUEO);
                break;

            case REVERSA:
                gestorDeEfectos.cambiarRonda();
                notificarObservadores(EventosUno.CAMBIARONDA);
                break;

            case MASDOS:
                gestorDeEfectos.mas2();
                notificarObservadores(EventosUno.MAS2);
                break;

            case MASCUATRO:
                // Notifica que se necesita un color antes de forzar el robo de cartas
                gestorDeEfectos.mas4();
                break;
        }
    }

    @Override
    public boolean isSentidoDeRonda() throws RemoteException {
        return sentidoDeRonda;
    }
    @Override
    public boolean inicioJuego() throws RemoteException {
        if (this.jugadores.size() >= 2) { // 2 jugadores mínimo
            for(Jugador j : jugadores){
                j.volverA0Puntos();
            }
            this.mazo = new MazoPrincipal();
            this.mazo.mezclarMazo();
            this.ganador=null;
            this.ganadorDeMano = null;
            this.descarte = new PilaDeDescarte();
            jugadorActual = jugadores.get(0);
            jugadorActual.setTurno(true);// Empieza el turno con el primer jugador
            this.repartir();
            notificarObservadores(EventosUno.JUEGO_INICIADO);
        } else {
            notificarObservadores(EventosUno.FALTAN_JUGADORES);
            return false;
        }
        return true;
    }

}
