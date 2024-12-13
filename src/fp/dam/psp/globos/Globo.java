package fp.dam.psp.globos;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Globo {

    private final Lock lock = new ReentrantLock();
    private EstadoGlobo estado = EstadoGlobo.DESHINCHADO;
    private final int volMax;
    private int volumen;
    private final Consola consola;
    private final String nombre;

    public Globo(int id, int volMax, Consola consola) {
        nombre = "GLOBO " + id;
        this.volMax = volMax;
        this.consola = consola;
    }

    public String getNombre() {
        return nombre;
    }

    public EstadoGlobo getEstado() {
        return estado;
    }

    public void hinchar() {
        lock.lock();
        try {
            volumen++;
            if (volumen > volMax) {
                estado = EstadoGlobo.EXPLOTADO;
                consola.println(nombre + " " + estado);
                consola.actualizarGlobosExplotados();
            } else {
                consola.println(nombre + " " + estado + " " + volumen);
            }
        } finally {
            lock.unlock();
        }
    }

    public void pinchar() {
        lock.lock();
        try {
            volumen = 0;
            estado = EstadoGlobo.PINCHADO;
            consola.println(nombre + " " + estado + " POR " + Thread.currentThread().getName());
            consola.actualizarGlobosPinchados();
        } finally {
            lock.unlock();
        }
    }

    public void setHinchando() {
        lock.lock();
        try {
            estado = EstadoGlobo.HINCHANDO;
        } finally {
            lock.unlock();
        }
    }
}