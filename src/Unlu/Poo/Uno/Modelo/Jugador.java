package Unlu.Poo.Uno.Modelo;

import java.io.Serializable;

public class Jugador implements Serializable {
    private String nombre;
    private Mano mano;
    private int puntos;
    protected boolean turno;
    private int victorias;

    public Jugador(String nombre){
        this.nombre = nombre;
        this.puntos=0;
        this.victorias = 0;
        this.mano = new Mano();
        this.turno = false;
    }

    //Gets
    public String getNombre() {
        return nombre;
    }
    public int getPuntos() {
        return puntos;
    }
    public boolean isTurno() {
        return this.turno;
    }
    public Mano getMano(){
        return mano;
    }
    public int getVictorias(){
        return this.victorias;
    }


    public void aumentarPuntos(int puntos) {
        this.puntos += puntos;
    }
    public void aumentaVictoria() {
        this.victorias++;
    }


    public void robaCarta(Carta carta){
        mano.agregarCarta(carta);
    }
    public Carta desacartar(int indice){
        return mano.jugarCarta(indice);
    }

    public boolean UNO(){
        return mano.cantidadDeCartas()==1;
    }

    public void setMano(Mano mano){
        this.mano = mano;
    }
    public void setTurno(boolean turno){this.turno=turno;}
    public void volverA0Puntos(){//Ver que onda. Esto no deberia estar aca
        this.puntos=0;
    }
    public void limpiarMano(){
        mano.limpar();
    }

    public int getCantCartas() {
        return this.mano.cantidadDeCartas();
    }

}
