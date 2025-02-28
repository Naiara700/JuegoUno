package Unlu.Poo.Uno.Modelo;

public class CartaNumerica extends Carta{
    private int numero;

    public CartaNumerica(ColorUno colorUno, int numero) {
        super(colorUno);
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public boolean esJugableSobre(Carta cartaTope, ColorUno colorUno) {
        if (this.colorUno == cartaTope.getColor()||this.colorUno == colorUno) {
            return true;
        }
        else if(cartaTope instanceof CartaNumerica) {
            return (this.numero == ((CartaNumerica) cartaTope).getNumero());
        }
        return false;
    }
}
