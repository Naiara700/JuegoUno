package Unlu.Poo.Uno.Vista;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanelFondo extends JPanel {
    private Image imagenFondo;

    public PanelFondo(String rutaImagen) {
        cargarImagen(rutaImagen);
        setLayout(null);
        // Listener para forzar el repintado cuando se redimensiona el panel.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
    }

    public void cargarImagen(String rutaImagen) {
        try {
            ImageIcon icono = new ImageIcon(rutaImagen);
            imagenFondo = icono.getImage();
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
    }

   //despues ver que onda
    public void setImagenFondo(Image imagen) {
        this.imagenFondo = imagen;
        repaint();
    }

    //para que se adpte al tamaño
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

