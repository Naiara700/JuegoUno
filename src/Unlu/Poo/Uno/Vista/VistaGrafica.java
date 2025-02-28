package Unlu.Poo.Uno.Vista;

import Unlu.Poo.Uno.Controlador.Controlador;
import Unlu.Poo.Uno.Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Locale;

public class VistaGrafica extends JFrame implements IVista{
    private Controlador controlador;
    private PanelFondo panelFondo;
    private String nombreJugador;
    private boolean menuJugadorPaso;
    private JLabel cartaLabelDescarte;
    private boolean puntos;

    public VistaGrafica(Controlador controlador) {
        this.controlador = controlador;
        configurarVentana();
        mostrarPantallaInicial();
    }
    public void  setPunto(boolean p){
        puntos = p;
    }

    private void configurarVentana() {
        setTitle("Juego UNO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 600));
        pack();//calcula el tamaño optimo
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));
    }

    private void mostrarPantallaInicial() {
        panelFondo = new PanelFondo("resources/gametogether.png");
        panelFondo.setLayout(null);
        add(panelFondo);
        //funciona con enter, ver si agrego espacio tambien
        panelFondo.setFocusable(true);
        panelFondo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mostrarMenuPrincipal();
                }
            }
        });
        panelFondo.requestFocusInWindow();
    }

    private void mostrarMenuPrincipal() {
        panelFondo = new PanelFondo("resources/menuPrincipal.png");
        puntos = false;
        double[][] botonesConfig = {
                {0.51, 0.08, 360, 75},
                {0.51, 0.28, 360, 75},
                {0.51, 0.48, 360, 75},
                {0.51, 0.68, 360, 75}
        };
        String[] acciones = {
                "agregarJugador",
                "mostrarJugadores",
                "comenzarPartida",
                "mostrarReglas"
        };

        for (int i = 0; i < botonesConfig.length; i++) {
            double[] config = botonesConfig[i];
            JButton boton = new JButton();
            boton.setOpaque(false);
            boton.setContentAreaFilled(false);
            boton.setBorderPainted(false);
            setearBoton(boton, config[0], config[1], (int) config[2], (int) config[3]);

            String accion = acciones[i];
            boton.addActionListener(e -> {
                switch (accion) {
                    case "agregarJugador":
                        mostrarAgregarJugadorVista();
                        break;
                    case "mostrarJugadores":
                        mostrarListaDeJugadores();
                        break;
                    case "comenzarPartida":
                        comenzarPartida();
                        break;
                    case "mostrarReglas":
                        mostrarReglas();
                        break;
                }
            });
            efectoMouse(boton);
            panelFondo.add(boton, JLayeredPane.PALETTE_LAYER);
        }

        getContentPane().removeAll();
        add(panelFondo);
        revalidate();
        repaint();
    }

    private void mostrarAgregarJugadorVista() {
            panelFondo = new PanelFondo("resources/agregarJugador.png");

            JTextField campoTexto = new JTextField();
            campoTexto.setOpaque(true);
            campoTexto.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Borde para visualización

            campoTexto.addActionListener(e -> {
                agregarJugador(campoTexto.getText());
            });

            JButton botonVolver = new JButton();
            botonVolver.setOpaque(false);
            botonVolver.setContentAreaFilled(false);
            botonVolver.setBorderPainted(false);
            botonVolver.addActionListener(e -> {
                mostrarMenuPrincipal();
            });
            efectoMouse(botonVolver);

            panelFondo.add(botonVolver, JLayeredPane.PALETTE_LAYER);
            panelFondo.add(campoTexto,JLayeredPane.PALETTE_LAYER);


            setearCampoTexto(campoTexto,0.089,0.58,470,40);
            setearBoton(botonVolver,0.023,0.84,50,40);

            getContentPane().removeAll();
            add(panelFondo);
            revalidate();
            repaint();
    }

    private void agregarJugador(String nombreJugador) {
        if (!nombreJugador.isEmpty()) {
            if (controlador.existeJugador(nombreJugador)) {
                notificarMensaje( "El nombre del jugador ya existe. Por favor, ingrese un nombre diferente.");
            } else {
                controlador.agregarJugador(nombreJugador);
                notificarMensaje("Jugador " + nombreJugador + " agregado.");
                this.nombreJugador = nombreJugador;
                mostrarMenuPrincipal();
            }
        } else {
            notificarMensaje("Por favor, ingrese un nombre válido.");
        }
    }

    private void mostrarListaDeJugadores() {
        panelFondo = new PanelFondo("resources/listaDeJugadores.png");

        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        for (Jugador j : controlador.getJugadores()) {
            modeloLista.addElement(j.getNombre());
        }

        JList<String> listaJugadores = new JList<>(modeloLista) {
            @Override
            public ListCellRenderer<? super String> getCellRenderer() {
                return new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                  boolean isSelected, boolean cellHasFocus) {
                        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        label.setOpaque(false);
                        label.setForeground(Color.WHITE);
                        label.setBackground(new Color(0, 0, 0, 0));
                        return label;
                    }
                };
            }
        };

        listaJugadores.setFont(new Font("Arial", Font.BOLD, 24));
        listaJugadores.setOpaque(false);
        listaJugadores.setSelectionBackground(new Color(255, 255, 255, 50));

        JScrollPane scrollPane = new JScrollPane(listaJugadores);
        setearScrollPanel(scrollPane,0.25,0.332,480,200);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);

        JButton botonVolver = new JButton();
        botonVolver.setOpaque(false);
        botonVolver.setContentAreaFilled(false);
        botonVolver.setBorderPainted(false);
        botonVolver.addActionListener(e -> mostrarMenuPrincipal());
        setearBoton(botonVolver,0.075,0.79,50,40);
        efectoMouse(botonVolver);

        //las capitas
        panelFondo.add(scrollPane, JLayeredPane.PALETTE_LAYER);
        panelFondo.add(botonVolver, JLayeredPane.PALETTE_LAYER);

        getContentPane().removeAll();
        add(panelFondo);
        revalidate();
        repaint();

    }

    private void mostrarReglas(){
        panelFondo = new PanelFondo("resources/reglas.png");
        JButton botonpasa = new JButton();
        botonpasa.setOpaque(false);
        botonpasa.setContentAreaFilled(false);
        botonpasa.setBorderPainted(false);
        botonpasa.addActionListener(e -> mostrarReglas2());
        setearBoton(botonpasa,0.91,0.81,50,40);
        efectoMouse(botonpasa);
        panelFondo.add(botonpasa, JLayeredPane.PALETTE_LAYER);

        getContentPane().removeAll();
        add(panelFondo);
        revalidate();
        repaint();
    }

    private void mostrarReglas2(){
        panelFondo = new PanelFondo("resources/reglas2.png");

        JButton botonpasa = new JButton();
        botonpasa.setOpaque(false);
        botonpasa.setContentAreaFilled(false);
        botonpasa.setBorderPainted(false);
        setearBoton(botonpasa,0.917,0.84,50,40);
        botonpasa.addActionListener(e -> mostrarReglas3());

        JButton botonvuelve = new JButton();
        botonvuelve.setOpaque(false);
        botonvuelve.setContentAreaFilled(false);
        botonvuelve.setBorderPainted(false);
        setearBoton(botonvuelve,0.917,0.71,50,40);
        botonvuelve.addActionListener(e -> mostrarReglas());

        efectoMouse(botonpasa);
        efectoMouse(botonvuelve);
        panelFondo.add(botonpasa, JLayeredPane.PALETTE_LAYER);
        panelFondo.add(botonvuelve, JLayeredPane.PALETTE_LAYER);

        getContentPane().removeAll();
        add(panelFondo);
        revalidate();
        repaint();
    }

    private void mostrarReglas3() {

        panelFondo = new PanelFondo("resources/reglas3.png");

        JButton botonpasa = new JButton();
        //botonpasa.setBounds(820, 505, 50, 40);
        botonpasa.setOpaque(false);
        botonpasa.setContentAreaFilled(false);
        botonpasa.setBorderPainted(false);
        setearBoton(botonpasa, 0.917, 0.84, 50, 40);
        botonpasa.addActionListener(e -> mostrarMenuPrincipal());

        JButton botonvuelve = new JButton();
        botonvuelve.setOpaque(false);
        botonvuelve.setContentAreaFilled(false);
        botonvuelve.setBorderPainted(false);
        setearBoton(botonvuelve, 0.917, 0.71, 50, 40);
        botonvuelve.addActionListener(e -> mostrarReglas2());

        efectoMouse(botonpasa);
        efectoMouse(botonvuelve);
        panelFondo.add(botonpasa, JLayeredPane.PALETTE_LAYER);
        panelFondo.add(botonvuelve, JLayeredPane.PALETTE_LAYER);

        getContentPane().removeAll();
        add(panelFondo);
        revalidate();
        repaint();
    }


    @Override
    public void setControlador(Controlador controlador) {
        
    }

    @Override
    public void reiniciarMano() {
        controlador.reiniciarMano();
    }

    @Override
    public void finJuego() {
        //puntos = true;
        limpiarPantalla();
        panelFondo = new PanelFondo("resources/ganador.png");
        panelFondo.setLayout(null);
        if (controlador.getGanador() != null) {
            JLabel labelGanador = new JLabel(controlador.getGanador().getNombre() + " !");
            labelGanador.setFont(new Font("Arial", Font.BOLD, 30));
            labelGanador.setForeground(Color.BLACK);
            labelGanador.setBounds(100, 250, 600, 50);
            panelFondo.add(labelGanador);
        }
        panelFondo.setFocusable(true);
        panelFondo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mostrarGracias();
                }
            }
        });
        panelFondo.requestFocusInWindow();
        getContentPane().removeAll();
        add(panelFondo);
        revalidate();
        repaint();
        // Asegurarse de que el panel tenga el foco después de añadirlo
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panelFondo.requestFocusInWindow();
            }
        });
    }
    private void mostrarGracias(){
        panelFondo = new PanelFondo("resources/graciasPorJugar.png");
        panelFondo.setLayout(null);
        panelFondo.setFocusable(true);
        panelFondo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mostrarMenuPrincipal();
                }
            }
        });
        panelFondo.requestFocusInWindow();
        getContentPane().removeAll();
        add(panelFondo);
        revalidate();
        repaint();
        // Asegurarse de que el panel tenga el foco después de añadirlo
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panelFondo.requestFocusInWindow();
            }
        });
    }

    @Override
    public void notificarMensaje(String string) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, string);
        });
    }

    @Override
    public void verTop() {

    }
    @Override
    public void habilitaBotones() {
        Component[] components = panelFondo.getComponents();
        for (Component component : components) {
            component.setEnabled(true);
        }
    }


    @Override
    public void limpiarPantalla() {

        if (panelFondo != null) {
            panelFondo.removeAll();
            panelFondo.revalidate();
            panelFondo.repaint();
        }
        getContentPane().removeAll();
        getContentPane().revalidate();
        getContentPane().repaint();

        Component glass = getRootPane().getGlassPane();
        if (glass instanceof Container) {
            ((Container) glass).removeAll();
            ((Container) glass).revalidate();
            ((Container) glass).repaint();
        }
        glass.setVisible(false);

    }

    @Override
    public void mostrarMenuEleguirColor() {
        ImageIcon icon = new ImageIcon("resources/seleccionaColor2.png");
        Image imagenFondo = icon.getImage();

        final JDialog dialog = new JDialog((Frame) null, "Elige un color", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);

        JPanel panelConFondo = new JPanel() {
            @Override// Pongo el fondoooooo
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelConFondo.setLayout(new FlowLayout());

        // Boton rojo
        JButton btnRojo = new JButton();
        btnRojo.setOpaque(false);
        btnRojo.setContentAreaFilled(false);
        btnRojo.setBorderPainted(false);
        efectoMouse(btnRojo);
        btnRojo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.solicitarColor(ColorUno.ROJO);
                    controlador.cambiarTurno();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                dialog.dispose();
            }
        });
        //Boton azul
        JButton btnAzul = new JButton();
        btnAzul.setOpaque(false);
        btnAzul.setContentAreaFilled(false);
        btnAzul.setBorderPainted(false);
        efectoMouse(btnAzul);
        btnAzul.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.solicitarColor(ColorUno.AZUL);
                    controlador.cambiarTurno();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                dialog.dispose();
            }
        });

        // Boto verde
        JButton btnVerde = new JButton();
        btnVerde.setOpaque(false);
        btnVerde.setContentAreaFilled(false);
        btnVerde.setBorderPainted(false);
        efectoMouse(btnVerde);
        btnVerde.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.solicitarColor(ColorUno.VERDE);
                    controlador.cambiarTurno();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                dialog.dispose();
            }
        });

        // Boton amarillo
        JButton btnAmarillo = new JButton();
        btnAmarillo.setOpaque(false);
        btnAmarillo.setContentAreaFilled(false);
        btnAmarillo.setBorderPainted(false);
        efectoMouse(btnAmarillo);
        btnAmarillo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.solicitarColor(ColorUno.AMARILLO);
                    controlador.cambiarTurno();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                dialog.dispose();
            }
        });

        panelConFondo.setLayout(null);//NO OLVIDAR NAIARAAAAAAAAAAAA

        btnRojo.setBounds(70, 50, 70, 30);
        btnAzul.setBounds(155, 90, 70, 30);
        btnVerde.setBounds(70, 90, 70, 30);
        btnAmarillo.setBounds(155, 50, 70, 30);
        panelConFondo.add(btnRojo);
        panelConFondo.add(btnAzul);
        panelConFondo.add(btnVerde);
        panelConFondo.add(btnAmarillo);

        dialog.setContentPane(panelConFondo);
        dialog.setVisible(true);
    }


    @Override
    public void mostrarPuntos(ArrayList<Jugador> jugadores) throws RemoteException {
        ImageIcon icon = new ImageIcon("resources/tablaDePuntos.png");
        Image imagenFondo = icon.getImage();

        final JDialog dialog = new JDialog((Frame) null, "Tabla de puntos", false);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);

        JPanel panelConFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        for (Jugador j : controlador.getJugadores()) {
            modeloLista.addElement(j.getNombre() + " = " + j.getPuntos() + " puntos");
        }
        JList<String> listaJugadores = new JList<>(modeloLista) {
            @Override
            public ListCellRenderer<? super String> getCellRenderer() {
                return new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        label.setOpaque(false);
                        label.setForeground(Color.WHITE);
                        label.setBackground(new Color(0, 0, 0, 0));
                        return label;
                    }
                };
            }
        };

        listaJugadores.setFont(new Font("Arial", Font.BOLD, 20));
        listaJugadores.setOpaque(false);
        listaJugadores.setSelectionBackground(new Color(255, 255, 255, 50));

        JScrollPane scrollPane = new JScrollPane(listaJugadores);
        setearScrollPanel(scrollPane, 0.25, 0.332, 480, 200);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);
        scrollPane.setBounds(50, 95, 400, 400);

        panelConFondo.setLayout(null);
        panelConFondo.add(scrollPane);

        dialog.setContentPane(panelConFondo);
        dialog.setVisible(true);
    }


    public void mostrarMazoYDescarte() {
        mostrarDescarte(controlador.getDescarte());
    }

    private void comenzarPartida(){
        boolean inicia = controlador.iniciarJuego();
        puntos = false;
        if (!inicia) {
            mostrarMenuPrincipal();
        }
    }

    private JLabel crearLabelImagen(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(ruta);
        Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(imagen));
    }

    private String obtenerRutaImagen(Carta carta) {
        if(carta instanceof CartaNumerica){
            return "resources/cartas/" + carta.getColor().toString().toLowerCase()  + ((CartaNumerica) carta).getNumero() + ".png";
        }else if(carta instanceof CartaEspecial){
            return "resources/cartas/"+carta.getColor().toString().toLowerCase()+((CartaEspecial) carta).getTipo().toString().toLowerCase()+".png";
        }
        return null;
    }

    @Override
    public void mostrarMano(ArrayList<Carta> mano) {
        /*if (puntos){
            return;
        }*/
        for (Component comp : panelFondo.getComponents()) {
            if (comp instanceof JPanel && "panelMano".equals(comp.getName())) {
                panelFondo.remove(comp);
            }
        }

        int totalCartas = mano.size();
        int panelAncho = getWidth() - 100;

        int cardWidth = 120, cardHeight = 160;
        boolean dosFilas = totalCartas > 10;
        if (dosFilas) {
            cardWidth = 110;
            cardHeight = 150;
        }

        JPanel panelMano = new JPanel();
        panelMano.setName("panelMano");
        panelMano.setLayout(null);
        panelMano.setOpaque(false);

        // Distribución de cartas: si hay dos filas se reparte el total entre ambas, sino se usa toda la fila
        int fila1Size = dosFilas ? totalCartas / 2 + (totalCartas % 2) : totalCartas;
        int fila2Size = dosFilas ? totalCartas / 2 : 0;

        // Calcular el overlap (superposición) en cada fila
        int overlapFila1 = (fila1Size > 1) ? Math.min(cardWidth - 30, (panelAncho - cardWidth) / (fila1Size - 1)) : 0;
        int overlapFila2 = (fila2Size > 1) ? Math.min(cardWidth - 30, (panelAncho - cardWidth) / (fila2Size - 1)) : 0;

        int desplazamientoArriba = 40;
        int yPosFilaSuperior = 0;
        int yPosFilaInferior = cardHeight / 2 - desplazamientoArriba;

        // Ajustar el alto del panel según el caso
        int panelAlto = dosFilas ? (cardHeight + yPosFilaInferior) : cardHeight;
        // Colocar el panel en la parte inferior de panelFondo (se puede ajustar el margen)
        int yBase = getHeight() - panelAlto - 20;
        panelMano.setBounds(50, yBase, panelAncho, panelAlto);

        if (dosFilas) {
            // Primero, agregar las cartas de la fila inferior (antigua fila 1)
            for (int i = 0; i < fila1Size; i++) {
                Carta carta = mano.get(i);
                JLabel cartaLabel = crearLabelImagen(obtenerRutaImagen(carta), cardWidth, cardHeight);
                // Calcular posición X de derecha a izquierda
                int xPos = panelAncho - cardWidth - (i * overlapFila1);
                cartaLabel.setBounds(xPos, yPosFilaInferior, cardWidth, cardHeight);
                panelMano.add(cartaLabel);
                hacerArrastrable(cartaLabel, i + 1);
            }

            // Luego, agregar las cartas de la fila superior (antigua fila 2)
            for (int i = fila1Size; i < totalCartas; i++) {
                Carta carta = mano.get(i);
                JLabel cartaLabel = crearLabelImagen(obtenerRutaImagen(carta), cardWidth, cardHeight);
                int indiceHorizontal = i - fila1Size;
                int xPos = panelAncho - cardWidth - (indiceHorizontal * overlapFila2);
                cartaLabel.setBounds(xPos, yPosFilaSuperior, cardWidth, cardHeight);
                panelMano.add(cartaLabel);
                hacerArrastrable(cartaLabel, i + 1);
            }
        } else {
            // Una sola fila: se muestran todas las cartas de derecha a izquierda en Y=0
            for (int i = 0; i < totalCartas; i++) {
                Carta carta = mano.get(i);
                JLabel cartaLabel = crearLabelImagen(obtenerRutaImagen(carta), cardWidth, cardHeight);
                int xPos = panelAncho - cardWidth - (i * overlapFila1);
                cartaLabel.setBounds(xPos, 0, cardWidth, cardHeight);
                panelMano.add(cartaLabel);
                hacerArrastrable(cartaLabel, i + 1);
            }
        }
        panelFondo.add(panelMano);
        panelFondo.revalidate();
        panelFondo.repaint();
    }

    private void actualizarFondoJugada(){
        panelFondo = new PanelFondo("resources/mesaJuego.png");
        panelFondo.setLayout(null);
        getContentPane().removeAll();
        add(panelFondo);
        agregarBotonPaso();
    }
    @Override
    public void mostrarMazo(PilaDeCartas mazo) {
        actualizarFondoJugada();
        if(mazo.cantDeCartas() > 1) {
            JButton mazoButton = new JButton();
            // Escalar la imagen y asignarla como ícono del botón
            ImageIcon icono = new ImageIcon("resources/cartas/cartaUno.png");
            Image imagen = icono.getImage().getScaledInstance(150, 190, Image.SCALE_SMOOTH);
            mazoButton.setIcon(new ImageIcon(imagen));

            // Configurar apariencia: sin fondo ni borde para simular una carta
            mazoButton.setContentAreaFilled(false);
            mazoButton.setBorderPainted(false);
            efectoMouse(mazoButton);
            // Ubicar el botón en el panel
            mazoButton.setBounds(266, 86, 250, 200);//si no no anda bien
            setearBoton(mazoButton,0.3,0.15,250,200);

            // Solo habilitar el botón si es el turno del jugador actual
            if(controlador.jugadorActual().equals(nombreJugador)) {
                mazoButton.setEnabled(true);
                efectoMouse(mazoButton);
            } else {
                mazoButton.setEnabled(false);
            }

            // Acción: cuando se presiona el botón se "levanta" una carta
            mazoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controlador.robarCarta();
                    menuJugadorPaso = true;
                }
            });
            panelFondo.add(mazoButton);
        } else {
            JLabel noHayMazo = new JLabel(new ImageIcon("resources/cartas/cartaUno.png"));
            panelFondo.add(noHayMazo);
        }
    }
    private void agregarBotonPaso() {
        JButton pasoButton = new JButton("Paso");
        setearBoton(pasoButton,0.2,0.30,100,40);
        pasoButton.setBackground(new Color(255, 127, 227, 255));
        // Personalizar apariencia (opcional)
        pasoButton.setContentAreaFilled(true);
        pasoButton.setBorderPainted(true);

        efectoMouse(pasoButton);

        pasoButton.setEnabled(menuJugadorPaso);

        // Acción al presionar el botón "Paso"
        pasoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    controlador.paso();
            }
        });
        panelFondo.add(pasoButton);
        panelFondo.revalidate();
        panelFondo.repaint();
    }

    @Override
    public void mostrarDescarte(Carta carta) {
        /*if(puntos){
            return;
        }*/
        if (cartaLabelDescarte != null) {
            panelFondo.remove(cartaLabelDescarte);
        }
        Carta nuevaDescarte = controlador.getDescarte();
        cartaLabelDescarte = crearLabelImagen(obtenerRutaImagen(nuevaDescarte), 150, 190);
        cartaLabelDescarte.setName("descarte");
        cartaLabelDescarte.setBounds(441, 86, 250, 200);
        setearDescarte(cartaLabelDescarte, 0.5, 0.15, 250, 200);

        /*cartaLabelDescarte.setEnabled(isTurno());
        cartaLabelDescarte.setFocusable(isTurno());*/

        panelFondo.add(cartaLabelDescarte);

        panelFondo.revalidate();
        panelFondo.repaint();
    }

    private void hacerArrastrable(JLabel cartaLabel, int indice) {
        cartaLabel.addMouseListener(new MouseAdapter() {
            // Guarda la posición original relativa al panelFondo
            Point posicionInicial;

            @Override
            public void mousePressed(MouseEvent e) {
                // Verifica si es el turno del jugador, si no, no permite mover la carta.
                if (!isTurno()) {
                    return;
                }
                // Guarda la posición original (dentro del panelFondo)
                posicionInicial = SwingUtilities.convertPoint(cartaLabel.getParent(), cartaLabel.getLocation(), panelFondo);
                // Obtiene el glass pane del JFrame y lo activa
                JRootPane root = getRootPane();
                Component glass = root.getGlassPane();
                glass.setVisible(true);
                // Convierte la posición de la carta al sistema de coordenadas del glass pane
                Point posEnGlass = SwingUtilities.convertPoint(cartaLabel.getParent(), cartaLabel.getLocation(), glass);
                cartaLabel.setLocation(posEnGlass);
                // Remueve la carta de su contenedor original y la agrega al glass pane
                ((Container) glass).setLayout(null);
                ((Container) glass).add(cartaLabel);
                glass.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isTurno()) {
                    return;
                }
                JRootPane root = getRootPane();
                Container glass = (Container) root.getGlassPane();
                Point dropPoint = SwingUtilities.convertPoint(cartaLabel, e.getPoint(), panelFondo);
                Rectangle rectDescarte = cartaLabelDescarte.getBounds();

                if (rectDescarte.contains(dropPoint)) {
                    try {
                        boolean puede = controlador.tirarCarta(indice);
                        if (puede) {
                            glass.remove(cartaLabel);
                        } else {
                            glass.remove(cartaLabel);
                            //glass.setVisible(false);
                            mostrarMano(controlador.getManoDeJugador());
                        }
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    // Soltó fuera del área: devolver la carta
                    glass.remove(cartaLabel);
                    mostrarMano(controlador.getManoDeJugador());
                }
                

            }
        });
        cartaLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Verifica que sea el turno, sino no se mueve la carta
                if (!isTurno()) {
                    return;
                }
                // Mientras se arrastra, actualizamos la posición de la carta en el glass pane
                JRootPane root = getRootPane();
                Component glass = root.getGlassPane();
                Point p = SwingUtilities.convertPoint(cartaLabel, e.getPoint(), glass);
                cartaLabel.setLocation(p.x - cartaLabel.getWidth() / 2, p.y - cartaLabel.getHeight() / 2);
            }
        });
    }

    //Manejo del turno
    private void esperarTurno(){
        Component[] components = panelFondo.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel && "descarte".equals(component.getName())) {
                continue;
            }
            component.setEnabled(false);
        }
    }
    private void esTurno(){
        //println("\nES TU TURNO "+ controlador.jugadorActual());
        menuJugadorPaso = false;
        Component[] components = panelFondo.getComponents();
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
        if(!controlador.jugadorActual().equals(nombreJugador)){
            esperarTurno();
        }
        else{
            esTurno();
            mostrarMano(controlador.getManoDeJugador());
            revalidate();
            repaint();
        }
    }

    //Utilidades
    private void efectoMouse(JButton boton){
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    private void setearBoton(JButton seteable, double anchof, double altof, int ancho, int alto){
        panelFondo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Calcula y asigna la nueva posición del botón según el tamaño del fondo
                seteable.setBounds(
                        (int)(panelFondo.getWidth() * anchof),  // 20% del ancho del fondo
                        (int)(panelFondo.getHeight() * altof), // 80% del alto del fondo
                        ancho, alto
                );
            }
        });
    }
    private void setearCampoTexto(JTextField seteable, double anchof, double altof, int ancho, int alto){
        panelFondo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Calcula y asigna la nueva posición del botón según el tamaño del fondo
                seteable.setBounds(
                        (int)(panelFondo.getWidth() * anchof),  // 20% del ancho del fondo
                        (int)(panelFondo.getHeight() * altof), // 80% del alto del fondo
                        ancho, alto
                );
            }
        });
    }
    private void setearScrollPanel(JScrollPane seteable, double anchof, double altof, int ancho, int alto){
        panelFondo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Calcula y asigna la nueva posición del botón según el tamaño del fondo
                seteable.setBounds(
                        (int)(panelFondo.getWidth() * anchof),  // 20% del ancho del fondo
                        (int)(panelFondo.getHeight() * altof), // 80% del alto del fondo
                        ancho, alto
                );
            }
        });
    }
    private void setearDescarte(JLabel descart, double anchof, double altof, int ancho, int alto) {
        panelFondo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                descart.setBounds(
                        (int)(panelFondo.getWidth() * anchof),
                        (int)(panelFondo.getHeight() * altof),
                        ancho, alto
                );
            }
        });
    }
}