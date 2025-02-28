package Unlu.Poo.Uno.Modelo;

public class CartaEspecial extends Carta{
    private TipoDeCartaEspecial tipo;
    public CartaEspecial(ColorUno colorUno, TipoDeCartaEspecial tipo) {
        super(colorUno);
        this.tipo= tipo;
    }

    public TipoDeCartaEspecial getTipo(){
        return tipo;
    }

    @Override
    public boolean esJugableSobre(Carta cartaTope, ColorUno colorUno) {
        if (this.tipo== TipoDeCartaEspecial.MASCUATRO || this.tipo == TipoDeCartaEspecial.CAM_COLOR) {
            return true;
        }else if(this.colorUno ==cartaTope.getColor()||this.colorUno == colorUno){
            return true;
        }else if(cartaTope instanceof CartaEspecial){
            return (this.tipo==((CartaEspecial)cartaTope).getTipo());
        }
        return false;
    }
}
