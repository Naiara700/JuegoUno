package Unlu.Poo.Uno.Modelo;

import java.io.Serializable;

public abstract class  Carta implements Serializable {
    protected ColorUno colorUno;

    public Carta(ColorUno colorUno){
        this.colorUno = colorUno;
    }

    public ColorUno getColor(){
        return colorUno;
    }

    public void setColor(ColorUno colorUno){
        this.colorUno = colorUno;
    }

    public abstract boolean esJugableSobre(Carta cartaTope, ColorUno colorUno);
}
