package fp.dam.psp.globos;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class HiloPausable extends Thread {

    private final Lock lock = new ReentrantLock();
    private final Condition pausadoCondition = lock.newCondition();
    private boolean pausado;

    public HiloPausable(String nombre) {
        super(nombre);
    }

    public void pausaOnOff() {
        lock.lock();
        try {
            pausado = !pausado;
            if (!pausado) {
                pausadoCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    protected void checkPausa() {
        lock.lock();
        try {
            while (pausado) {
                pausadoCondition.await();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            checkPausa();
            if (!isInterrupted()) {
                tarea();
            }
        }
    }

    protected abstract void tarea();
}
