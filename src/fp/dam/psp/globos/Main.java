package fp.dam.psp.globos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends JFrame implements WindowListener {

    private final Consola consola;

    private final ArrayList<HiloPausable> hilos = new ArrayList<>();

    Main() {

        super("GLOBOS");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 2),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )));

        getContentPane().add(new ToolBar(), BorderLayout.NORTH);
        getContentPane().add(consola = new Consola(40, 50), BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main()::iniciar);
    }

    private void iniciar() {
        setVisible(true);

        Deposito deposito = new Deposito(10, 3, consola);
        hilos.add(new PinchaGlobos(1, deposito));
        for (int i = 1; i <= 5; i++) {
            hilos.add(new HinchaGlobos(i, deposito));
        }

        hilos.forEach(Thread::start);
    }

    private void finalizar(ActionEvent e) {

        hilos.forEach(Thread::interrupt);
        hilos.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        });

        dispose();
        System.exit(0);
    }

    private void pausaOnOff() {
        hilos.forEach(HiloPausable::pausaOnOff);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        finalizar(null);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    private class ToolBar extends JToolBar {
        ToolBar() {
            setFloatable(false);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            add(new BotonPausa(
                    new ImageIcon(Objects.requireNonNull(Main.class.getResource("/pause.png"))),
                    new ImageIcon(Objects.requireNonNull(Main.class.getResource("/play.png")))
            ));
            JButton exit = new JButton(new ImageIcon(Objects.requireNonNull(Main.class.getResource("/exit.png"))));
            exit.addActionListener(Main.this::finalizar);
            add(exit);
        }
    }

    private class BotonPausa extends JToggleButton {
        private final ImageIcon iconoPausa;
        private final ImageIcon iconoReanudar;

        BotonPausa(ImageIcon iconoPausa, ImageIcon iconoReanudar) {
            setIcon(this.iconoPausa = iconoPausa);
            this.iconoReanudar = iconoReanudar;
            addActionListener(this::pausarReanudar);
        }

        private void pausarReanudar(ActionEvent e) {

            setIcon(isSelected() ? iconoReanudar : iconoPausa);
            pausaOnOff();
        }
    }
}
