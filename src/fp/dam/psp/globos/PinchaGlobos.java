package fp.dam.psp.globos;

public class PinchaGlobos extends HiloPausable {

    private final Deposito deposito;

    public PinchaGlobos(int id, Deposito deposito) {
        super("PG " + id);
        this.deposito = deposito;
    }

    @Override
    protected void tarea() {
        try {
            checkPausa();
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            interrupt();
            return;
        }

        Globo globo = deposito.getHinchando();
        if (globo != null) {
            globo.pinchar();
        }
    }
}

