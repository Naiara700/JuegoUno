package Unlu.Poo.Uno.Modelo;

public class PilaDeDescarte extends PilaDeCartas{
    public void reciclarEnMazo(MazoPrincipal mazo) {
        while (!cartas.isEmpty()) {
            mazo.agregarCarta(sacarCarta());
        }
        mazo.mezclarMazo();
    }

    public Carta obtenerUltimaCarta(){
        if(cartas.isEmpty()){
            return null;
        }
        return cartas.getLast();
    }
}
