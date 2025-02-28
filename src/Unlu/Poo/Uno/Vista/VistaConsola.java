package Unlu.Poo.Uno.Vista;

import Unlu.Poo.Uno.Controlador.Controlador;
import Unlu.Poo.Uno.Modelo.*;
import Unlu.Poo.Uno.Modelo.ColorUno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaConsola extends JFrame implements IVista {
    private Controlador controlador;
    private EstadoVista eVista;
    private String nombreJugador;
    private JButton enterButton;
    private JTextField ingresaTexto;
    private JTextArea saleTexto;
    private JPanel panelPrincipal;
    private boolean menuJugadorPaso;

    public VistaConsola(Controlador controlador) {
        this.controlador = controlador;
        panelPrincipal = new JPanel();
        enterButton = new JButton("Enviar");
        ingresaTexto = new JTextField(55);
        saleTexto = new JTextArea(30, 85);
        saleTexto.setEditable(false); // Hacer que el JTextArea no sea editable
        menuJugadorPaso = false;

        setTitle("UNOO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        panelPrincipal.add(new JLabel(""));
        panelPrincipal.add(new JScrollPane(saleTexto));
        panelPrincipal.add(ingresaTexto);
        panelPrincipal.add(enterButton);
        setContentPane(panelPrincipal);

        saleTexto.setFont(new Font("Courier New", Font.PLAIN, 14));

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarTexto();
            }
        });

        ingresaTexto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enviarTexto();
                }
            }
        });
        mostrarMenu();
    }

    private void iniciarPartida() {
        boolean inicia = controlador.iniciarJuego();
        if (!inicia) {
            mostrarMenu();
        }
    }

    private void enviarTexto() {
        println(ingresaTexto.getText());
        try {
            procesarEntrada(ingresaTexto.getText());
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
        ingresaTexto.setText("");
    }

    private void procesarEntrada(String entrada) throws RemoteException {
        switch (eVista) {
            case MENU:
                limpiarPantalla();
                menuiniciado(entrada);
                break;
            case AGREGAR_JUGADOR:
                agregarJugador();
                break;
            case MENU_JUGADOR:
                menuJugador(entrada);
                break;
            case MENU_JUGADOR_PASO:
                menujugadorPaso(entrada);
                break;
            case TIRAR_CARTA:
                tirarCarta(entrada);
                break;
            case CAMBIAR_COLOR:
                ColorUno colorUno = eleguirColor(entrada);
                cambiarColor(colorUno);
                controlador.cambiarTurno();
                break;
        }
    }

    private void agregarJugador() {
        if(!ingresaTexto.getText().equals("")) {
             nombreJugador= ingresaTexto.getText();
            if(!controlador.existeJugador(nombreJugador)){
                controlador.agregarJugador(nombreJugador);
                println("   Jugador agregado con exito");
                mostrarMenu();
            }
            else{
                println("   ESE JUGADOR YA ESTA EN EL JUEGO, INGRESE OTRO: ");
            }
        }
        else{
            println("   INGRESE UN NOMBRE VALIDO PARA EL JUGADOR: ");
        }
    }


    @Override
    public void notificarMensaje(String string) {
        println( string);
    }


    //ACCIONES DEL JUGADOR
    private void menuJugador(String opcion) {
            switch (opcion) {
                case "1"://robar carta
                    robarcarta();
                    break;
                case "2"://tirar carta
                    cambiarestado_cartas_tirar();
                    break;
                default:
                    print(" OPCION NO VALIDA, elija una opcion valida: ");
                    mostrarMenuJugador();
            }
    }
    private void menujugadorPaso(String entrada) {
        switch (entrada) {
            case "2"://tirar carta
                cambiarestado_cartas_tirar();
                break;
            case "3":
                pasar();//pasar
                break;
            default:
                print(" OPCION NO VALIDA, elija una opcion valida: ");
                mostrarMenujugadorPaso();
        }
    }

    private void robarcarta(){
        controlador.robarCarta();
        menuJugadorPaso = true;
        eVista = EstadoVista.MENU_JUGADOR_PASO;
        mostrarMenujugadorPaso();
    }

    private void tirarCarta(String numero)throws RemoteException {
        boolean puede;
        puede = controlador.tirarCarta(Integer.parseInt(numero));
        if(!puede){
            if(menuJugadorPaso){
                mostrarMenujugadorPaso();
            }else{
                mostrarMenuJugador();
            }
        }
    }

    private void pasar() {
        controlador.paso();
    }

    public void cambiarColor(ColorUno colorUno) throws RemoteException {
        controlador.solicitarColor(colorUno);
    }
    public ColorUno eleguirColor(String entrada) throws RemoteException {
        ColorUno colorUno = null;
        switch (entrada){
            case "1":
                colorUno = ColorUno.ROJO;
                break;
            case "2":
                colorUno = ColorUno.AMARILLO;
                break;
            case "3":
                colorUno = ColorUno.VERDE;
                break;
            case "4":
                colorUno = ColorUno.AZUL;
                break;
            default:
                print(" OPCION NO VALIDA, elija una opcion valida: ");
                mostrarMenuEleguirColor();
        }
        return colorUno;
    }


    @Override
    public void setControlador(Controlador controlador) {

    }

    @Override
    public void reiniciarMano() {
        println ("                    ╔══════════════════════════════════════════╗");
        println ("                    ║               REINICIAR MANO             ║");
        println ("                    ╚══════════════════════════════════════════╝");
        controlador.reiniciarMano();

    }

    @Override
    public void finJuego() {
        println ("                    ╔══════════════════════════════════════════╗");
        println ("                    ║               FIN DEL JUEGO              ║");
        println ("                    ╚══════════════════════════════════════════╝");
        println("              * GANADOR:"+ controlador.getGanador().getNombre() +"-> puntos"+ controlador.getGanador().getPuntos());
        println ("\n");
        println ("                    ╔══════════════════════════════════════════╗");
        println ("                    ║            GRACIAS POR JUGAR             ║");
        println ("                    ╚══════════════════════════════════════════╝");
        mostrarMenu();
    }

    @Override
    public void verTop() {

    }

    @Override
    public void habilitaBotones() {
        Component[] components = panelPrincipal.getComponents();
        for (Component component : components) {
            component.setEnabled(true);
        }
    }

    //MOSTRAR
    public void mostrarPuntos(ArrayList<Jugador> jugadores) throws RemoteException {
        println("   RONDA Nº "+ (controlador.getCantidadDeRondas()+1));
        println("   GANADOR DE LA MANO: "+ controlador.getGanadorMano());
        println("            ╔═══════════════════════════════════════╗");
        println("                        TABLA DE PUNTOS:");
        for (Jugador j:jugadores) {
            println("               "+j.getNombre()+": "+ j.getPuntos());
            //println("");
        }
        println("");
        println("            ╚═══════════════════════════════════════╝");
    }

    public void mostrarMenuEleguirColor() {
        eVista = EstadoVista.CAMBIAR_COLOR;
        println(" COLORES A ELEGIR:");
        println("1-ROJO");
        println("2-AMARILLO");
        println("3-VERDE");
        println("4-AZUL");
        print("Seleccione una opcion: ");
    }

    public void mostrarCartas(Carta carta) {
        String[] lineasCarta = generarLineasCarta(carta);
        for (String linea : lineasCarta) {
            println(linea);
        }
    }

    private String[] generarLineasCarta(Carta carta) {
        ArrayList<String> lineas = new ArrayList<>();
        String color = carta.getColor().toString();
        String nombreColor = color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase();

        String bordeSuperior = "╔═══════════╗";
        String bordeInferior = "╚═══════════╝";
        String espacioVacio = "║           ║";

        if (carta instanceof CartaNumerica) {
            int numero = ((CartaNumerica) carta).getNumero();
            String numeroIzquierda = String.format("%-2s", numero);
            String numeroDerecha = String.format("%2s", numero);

            lineas.add(bordeSuperior);
            lineas.add("║ " + numeroIzquierda + "        ║");
            lineas.add(espacioVacio);
            lineas.add(String.format("║ %-10s║", nombreColor));
            lineas.add(espacioVacio);
            lineas.add("║        " + numeroDerecha + " ║");
            lineas.add(bordeInferior);
        }
        else if (carta instanceof CartaEspecial) {
            String tipo = ((CartaEspecial) carta).getTipo().toString();
            String tipoString = tipo.substring(0, 1).toUpperCase() + tipo.substring(1).toLowerCase();

            lineas.add(bordeSuperior);
            lineas.add(espacioVacio);
            lineas.add(String.format("║%-10s ║", tipoString));
            lineas.add(espacioVacio);
            lineas.add(String.format("║%-10s ║", nombreColor));
            lineas.add(espacioVacio);
            lineas.add(bordeInferior);
        }

        return lineas.toArray(new String[0]);
    }

    public void mostrarMano(ArrayList<Carta> mano) {
        println("\nMANO DEL JUGADOR: " + controlador.getJugadorEnTurno().getNombre());

        ArrayList<String[]> todasLineas = new ArrayList<>();
        ArrayList<String> encabezados = new ArrayList<>();

        // Generar encabezados y líneas de cartas
        for (int i = 0; i < mano.size(); i++) {
            encabezados.add(String.format("   Carta %-2d  ", (i + 1)));
            todasLineas.add(generarLineasCarta(mano.get(i)));
        }
        // Imprimir encabezados en diagonal
        println(String.join("    ", encabezados));
        // Imprimir cartas lado a lado
        for (int linea = 0; linea < 7; linea++) { // 7 líneas por carta
            StringBuilder sb = new StringBuilder();
            for (String[] carta : todasLineas) {
                if (linea < carta.length) {
                    sb.append(carta[linea]);
                } else {
                    sb.append("            "); // Espacio para alineación
                }
                sb.append("    "); // Espacio entre cartas
            }
            println(sb.toString());
        }
    }
    @Override
    public void mostrarMazo(PilaDeCartas mazo) {
        println("MAZO");
        String bordeSuperior = "╔══════════╗";
        String bordeInferior = "╚══════════╝";
        String espacioVacio =  "║          ║";
        String uno = String.format("║    UNO   ║");

        if(mazo.cantDeCartas()>1) {
            String[] card = {
                    bordeSuperior,
                    espacioVacio,
                    espacioVacio,
                    uno,
                    espacioVacio,
                    espacioVacio,
                    bordeInferior
            };
            for (String line : card) {
                println(line);
            }
        }
        else{
            println ("                    ╔══════════════════════════════════════════╗");
            println ("                    ║                 MAZO VACIO               ║");
            println ("                    ╚══════════════════════════════════════════╝");
        }
    }
    private String[] getMazo(PilaDeCartas mazo) {
        //println("MAZO");
        String bordeSuperior = "╔═══════════╗";
        String bordeInferior = "╚═══════════╝";
        String espacioVacio =      "║           ║";
        String uno = String.format("║    UNO    ║");

        if(mazo.cantDeCartas()>1) {
            String[] card = {
                    bordeSuperior,
                    espacioVacio,
                    espacioVacio,
                    uno,
                    espacioVacio,
                    espacioVacio,
                    bordeInferior
            };
            return card;
        }
        else{
            return new String[]{
                    "                    ╔══════════════════════════════════════════╗",
                    "                    ║                 MAZO VACIO               ║",
                    "                    ╚══════════════════════════════════════════╝"
            };
        }
    }

    @Override
    public void mostrarDescarte(Carta carta){
        println("CARTA DE DESCARTE");
        mostrarCartas(carta);
    }
    private String[] getCartaDescartada(Carta carta){

        String[] lineasCarta = generarLineasCarta(carta);
        return lineasCarta;
    }

    public void mostrarMazoYDescarte(){
        String[] mazoLines =  getMazo(controlador.getMazo());
        String[] descarteLines = getCartaDescartada(controlador.getDescarte());
        println(("   MAZO:          DESCARTE:"));
        for (int i = 0; i < Math.max(mazoLines.length, descarteLines.length); i++) {
            String mazoLine = i < mazoLines.length ? mazoLines[i] : "            ";
            String descarteLine = i < descarteLines.length ? descarteLines[i] : "            ";
            println(mazoLine + "    " + descarteLine);
        }
    }

    @Override
    public void setPunto(boolean p) {

    }


    private void mostrarJugadores() {
        if(controlador.getJugadores().isEmpty()){
            println ("                    ╔══════════════════════════════════════════╗");
            println ("                    ║              NO HAY JUGADORES            ║");
            println ("                    ╚══════════════════════════════════════════╝");
        }
        else {
            for (Jugador j : controlador.getJugadores()) {
                println("               * "+j.getNombre());
            }
        }
        println("");
        mostrarMenu();
    }

    private void mostrarMenuJugador() {
        eVista = EstadoVista.MENU_JUGADOR;
        println("ACCIONES");
        println("1-Agarrar carta del mazo");
        println("2-Tirar carta");
        print("Seleccione una opcion: ");
    }

    private void cambiarestado_cartas_tirar(){
        eVista = EstadoVista.TIRAR_CARTA;
        println("Seleccione el numero de la carta que quiere tirar: ");

    }

    private void mostrarMenujugadorPaso() {
        eVista = EstadoVista.MENU_JUGADOR_PASO;
        println("ACCIONES");
        println("2-Tirar carta"); 
        println("3-Paso");
        print("Seleccione una opcion: ");
    }

    private void mostrarMenu(){
        String menu = """
                                ╔══════════════════════════════════════════╗
                                ║                    UNO                   ║
                                ╠══════════════════════════════════════════╣
                                ║               MENÚ PRINCIPAL             ║
                                ╠══════════════════════════════════════════╣
                                ║ 1. Agregar Jugadores                     ║
                                ║ 2. Mostrar Lista de Jugadores            ║
                                ║ 3. Comenzar Partida                      ║
                                ║ 4. Mostrar reglas del juego              ║
                                ║ 0. Salir del Juego                       ║
                                ╚══════════════════════════════════════════╝
            """;
        println(menu);
        eVista = EstadoVista.MENU;
    }

    //TURNO
    private void esperarTurno(){
        println("\nESPERANDO EL TURNO DEL JUGADOR "+ controlador.jugadorActual());
        println("");
        println("ESPERANDO");
        Component[] components = panelPrincipal.getComponents();
        for (Component component : components) {
            component.setEnabled(false);
        }
    }
    private void esTurno(){
        println("\nES TU TURNO "+ controlador.jugadorActual());
        menuJugadorPaso = false;
        Component[] components = panelPrincipal.getComponents();
        for (Component component : components) {
            component.setEnabled(true);
        }
    }
    public boolean isTurno(){
        boolean es=false;
        if(controlador.jugadorActual().equals(nombreJugador)){
            es=true;
        }
        return es;
    }
    public void verificarTurno() {
        //nombreJugador = controlador.getJugadorEnTurno().getNombre();//Esto es una chanchada para que ande
        if(!controlador.jugadorActual().equals(nombreJugador)){
            esperarTurno();
        }
        else{
            esTurno();
            mostrarMano(controlador.getManoDeJugador());
            mostrarMenuJugador();
        }
    }


    //MUNU PRINCIPAL
    private void menuiniciado(String opcion) {
        switch (opcion) {
            case "1":
                // Agregar Jugador
                println ("                    ╔══════════════════════════════════════════╗");
                println ("                    ║               AGREGAR JUGADOR            ║");
                println ("                    ╚══════════════════════════════════════════╝");
                println("   Nombre del jugador a agregar: ");
                eVista = EstadoVista.AGREGAR_JUGADOR;
                break;
            case "2":
                // mostrar lista de Jugadores
                println ("                    ╔══════════════════════════════════════════╗");
                println ("                    ║             LISTA DE JUGADORES           ║");
                println ("                    ╚══════════════════════════════════════════╝");
                mostrarJugadores();

                break;
            case "3":
                // Comenzar Juego
                iniciarPartida();
                break;
            case "4":
                //Mostar las reglas
                println ("                    ╔══════════════════════════════════════════╗");
                println ("                    ║             REGLAS DEL JUEGO             ║");
                println ("                    ╚══════════════════════════════════════════╝");
                println("OBJETIVO DEL JUEGO:");
                println("El objetivo de UNO es deshacerse de todas las cartas que se “roban” inicialmente, diciendo la palabra\n" +
                        "“UNO” cuando queda la última carta en la mano. El primero que llega a 500 puntos gana. Se recibe \n" +
                        "puntos por todas las cartas que los otros jugadores todavía tienen en sus manos (véase puntos).\n");
                println("DECORSO DEL JUEGO:");
                println("El primero jugador pone una carta de su mano al mazo de descartes. Aquí vale: Una carta sólo se \n" +
                        "puede superponer en una carta del mismo color o del mismo número. Las cartas negras son cartas de\n" +
                        "acción especiales con reglas particulares (ver cartas de acción). Si un jugador no puede poner la \n" +
                        "carta oportuna, tiene que tomar una carta de pena del mazo. Puede jugar esta carta ahora mismo, si \n" +
                        "la vale bien. Si no, es el turno del siguiente jugador. Quién pone la penúltima carta, debe decir “UNO” \n" +
                        "(que significa “Eins”) y señala que tiene sólo una última carta en la mano. Si un jugador lo olvida y el \n" +
                        "otro lo nota a tiempo (antes de que el siguiente jugador haya tomado o ha depuesto una carta) tiene \n" +
                        "que tomar dos cartas de pena. El ganador de la ronda es él que depone la última carta. Los puntos se\n" +
                        "suman y se comienza una nueva ronda.\n");
                println("ACCION DE LAS CARTAS");
                println("CARTA TOMA DOS \n" +
                        "Cuando se pone esta carta, el siguiente jugador debe tomar dos cartas y no puede \n" +
                        "deponer ninguna carta en esta ronda. Esta carta sólo puede superponer en una carta \n" +
                        "con el color apropiado u otras cartas “toma dos”.");
                println("CARTA DE REVERSA\n" +
                        "Con esta carta se cambia la dirección. Si se ha jugado por la izquierda, ahora se juega \n" +
                        "por la derecha y por la inversa. La carta sólo se puede superponer en una carta con \n" +
                        "color correspondiente o en una otra carta de retorno.\n");
                println("CARTA DE BLOQUEO\n" +
                        "Después de poner esta carta, el siguiente jugador será “saltado”. La carta sólo se puede\n" +
                        "superponer en una carta con color correspondiente o en una otra carta de intermisión. \n" );
                println("CARTA DE CAMBIA COLOR\n" +
                        "Con esta carta el jugador decide qué color sigue en el juego. También el color presente \n" +
                        "puede ser seleccionado. Una carta de elección de colores también se puede poner \n" +
                        "cuando el jugador puede poner una carta diferente.");
                println("CARTA DE TOMAR CUATRO COLORES \n" +
                        "Esta carta es la mejor. El jugador decide qué color sigue en el juego. Además, el \n" +
                        "siguiente jugador debe tomar cuatro cartas. No se puede deponer cualquier carta en \n" +
                        "esta ronda.");
                println("Puntos\n" +
                        "El jugador que ha puesto todas sus cartas primeramente, recibe los siguientes puntos por las cartas \n" +
                        "de sus jugadores que tienen todavía en la mano:\n" +
                        "Todas las cartas de números Valor\n" +
                        "Carta toma dos --> 20 puntos\n" +
                        "Carta de reversa --> 20 puntos\n" +
                        "Carta de bloqueo --> 20 puntos\n" +
                        "Carta de elección de colores --> 50 puntos\n" +
                        "Carta de tomar cuatro colores --> 50 puntos\n" +
                        "Quién llega a 500 puntos al primero gana el juego.\n");
                eVista = EstadoVista.MENU;
                mostrarMenu();
                break;
            case "0":
                System.exit(0);
                break;
            default:
                print(" OPCION NO VALIDA, elija una opcion valida: ");
                mostrarMenu();
        }
    }

    // Imprimir en pantalla
    private void print(String texto) {
        saleTexto.append(texto);
    }
    private void println(String texto) {
        print(texto + "\n");
    }

    //Limpiar pantalla
    public void limpiarPantalla() {
        saleTexto.setText("");
    }

}
