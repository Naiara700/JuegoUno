package Unlu.Poo.Uno.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public abstract class PilaDeCartas implements Serializable {
    protected ArrayList<Carta> cartas;
    protected int cantDeCartas;

    public PilaDeCartas(){
        this.cartas= new ArrayList<>();
    }

    public Carta sacarCarta() {
        if (cartas.isEmpty()) {//Verifico si esta vacia
            return null;//throw new IllegalStateException("No hay más cartas.");
        }
        return cartas.remove(cartas.size()-1);//Saco la ultima carta (no andaba bien con last)
    }

    public void agregarCarta(Carta carta){
        if(cantDeCartas!=108){
            cartas.add(carta);
            cantDeCartas++;
        }
    }

    public void vaciarCartas() {
        this.cartas.clear();
    }

    public void mezclarMazo() {
        Collections.shuffle(cartas);
    }

    public int getCantDeCartas() {
        return cantDeCartas;
    }

    public int cantDeCartas() {
        return cartas.size();
    }
}
