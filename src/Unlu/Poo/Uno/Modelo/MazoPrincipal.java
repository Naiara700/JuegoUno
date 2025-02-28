package Unlu.Poo.Uno.Modelo;

public class MazoPrincipal extends PilaDeCartas{
    public MazoPrincipal(){
        super();
        ColorUno[] colores = {ColorUno.ROJO, ColorUno.AMARILLO, ColorUno.VERDE, ColorUno.AZUL};
        TipoDeCartaEspecial[] tipos = {TipoDeCartaEspecial.BLOQUEO,TipoDeCartaEspecial.MASDOS,TipoDeCartaEspecial.REVERSA};
        TipoDeCartaEspecial []negras = {TipoDeCartaEspecial.CAM_COLOR,TipoDeCartaEspecial.MASCUATRO};
        for(int j=0; j<2;j++){// 2 cartas de cada uno
            for(int i = 1; i<10; i++){//Agregar de la 1 a la 9
                for(ColorUno colorUno : colores){
                    Carta carta = new CartaNumerica(colorUno,i);
                    agregarCarta(carta);
                }
            }
            for(TipoDeCartaEspecial tipo: tipos){
                for(ColorUno colorUno :colores){
                    Carta carta = new CartaEspecial(colorUno,tipo);
                    agregarCarta(carta);
                }
            }

        }
        for (TipoDeCartaEspecial tipo : negras) {
            for (int i = 0; i < 4; i++) { // 4 de cada carta negra
                Carta carta = new CartaEspecial(ColorUno.NEGRO, tipo);
                agregarCarta(carta);
            }
        }
        for (ColorUno colorUno : colores) {//Agregar los 4 0
            Carta carta = new CartaNumerica(colorUno, 0);
            agregarCarta(carta);
        }

    }

    public boolean isVacio() {
        return cartas.isEmpty();
    }
}
