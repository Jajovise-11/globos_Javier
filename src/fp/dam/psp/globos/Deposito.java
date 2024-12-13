package fp.dam.psp.globos;

import java.util.concurrent.*;
import java.util.Random;

public class Deposito extends Thread {
    private final BlockingQueue<Globo> deshinchados;
    private final BlockingQueue<Globo> hinchando;
    private final int maxGlobos;
    private final int maxH;
    private final Consola consola;
    private int total = 0;
    private final Random r = new Random();

    public Deposito(int maxGlobos, int maxH, Consola consola) {
        super("REPONEDOR");
        this.maxGlobos = maxGlobos;
        this.maxH = maxH;
        this.consola = consola;

        this.deshinchados = new LinkedBlockingQueue<>(maxGlobos);
        this.hinchando = new LinkedBlockingQueue<>(maxH);
    }

    public void reponer() {
        try {
            Globo globo = new Globo(++total, 5, consola);
            deshinchados.put(globo);
            consola.actualizarTotalGlobos(total, deshinchados.size(), hinchando.size());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Globo getDeshinchado() {
        try {
            Globo globo = deshinchados.take();
            globo.setHinchando();
            hinchando.put(globo);
            consola.println(globo.getNombre() + " ENTREGADO A " + Thread.currentThread().getName());
            consola.actualizarGlobosDepositados(deshinchados.size(), hinchando.size());
            return globo;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void retirar(Globo globo) {
        hinchando.remove(globo);
    }

    public Globo getHinchando() {
        try {
            return hinchando.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

}
