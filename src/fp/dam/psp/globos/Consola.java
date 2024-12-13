package fp.dam.psp.globos;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Consola extends JPanel {

    JTextArea text;
    private final JLabel totalGlobosCreados;
    private final JLabel totalGlobosDeposito;
    private final JLabel totalGlobosHinchando;
    private final JLabel totalGlobosExplotados;
    private final JLabel totalGlobosPinchados;

    private static final Font f1;
    private static final Font f2;

    static {
        try {
            f1 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Consola.class.getResourceAsStream("/gaban.ttf"))).deriveFont(29f);
            f2 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Consola.class.getResourceAsStream("/arcade.ttf"))).deriveFont(39f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Consola(int rows, int columns) {
        super(new GridBagLayout());
        GridBagLayout l = (GridBagLayout) getLayout();
        GridBagConstraints c = new GridBagConstraints();
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        ));
        JScrollPane sp = new JScrollPane(text = new JTextArea(rows, columns), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.VERTICAL;
        l.setConstraints(sp, c);
        add(sp);
        JPanel panel = new JPanel(new GridLayout(12, 1));
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        ));
        panel.add(new Text("CREADOS", JLabel.CENTER, f1));
        panel.add(totalGlobosCreados = new Text("0", JLabel.CENTER, f2));
        panel.add(new Text("EN EL DEPÓSITO", JLabel.CENTER, f1));
        panel.add(totalGlobosDeposito = new Text("0", JLabel.CENTER, f2));
        panel.add(new Text("HINCHANDO", JLabel.CENTER, f1));
        panel.add(totalGlobosHinchando = new Text("0", JLabel.CENTER, f2));
        panel.add(new Text("EXPLOTADOS", JLabel.CENTER, f1));
        panel.add(totalGlobosExplotados = new Text("0", JLabel.CENTER, f2));
        panel.add(new Text("PINCHADOS", JLabel.CENTER, f1));
        panel.add(totalGlobosPinchados = new Text("0", JLabel.CENTER, f2));
        c.gridx = 1;
        c.gridy = 0;
        l.setConstraints(panel, c);
        add(panel);
    }

    public void println(String s) {
        SwingUtilities.invokeLater(() -> {
            text.append(s); text.append("\n");
        });
    }

    public void actualizarTotalGlobos(int n, int deshinchados, int hinchando) {
        SwingUtilities.invokeLater(() -> {
            totalGlobosCreados.setText(String.valueOf(n));
            totalGlobosHinchando.setText(String.valueOf(hinchando));
            totalGlobosDeposito.setText(String.valueOf(deshinchados + hinchando));
        });
    }

    public void actualizarGlobosDepositados(int deshinchados, int hinchando) {
        SwingUtilities.invokeLater(() -> {
            totalGlobosHinchando.setText(String.valueOf(hinchando));
            totalGlobosDeposito.setText(String.valueOf(deshinchados + hinchando));
        });
    }

    public void actualizarGlobosExplotados() {
        SwingUtilities.invokeLater(() -> {
            int n = Integer.parseInt(totalGlobosExplotados.getText()) + 1;
            totalGlobosExplotados.setText(String.valueOf(n));
        });
    }

    public void actualizarGlobosPinchados() {
        SwingUtilities.invokeLater(() -> {
            int n = Integer.parseInt(totalGlobosPinchados.getText()) + 1;
            totalGlobosPinchados.setText(String.valueOf(n));
        });
    }

    private static final class Text extends JLabel {
        Text(String text, int horizontalAlignment, Font f) {
            super(text, horizontalAlignment);
            setFont(f);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
    }

}
