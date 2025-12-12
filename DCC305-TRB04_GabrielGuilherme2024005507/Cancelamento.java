class Cancelamento implements Runnable {
    private OnibusParte3 onibus;

    public Cancelamento(OnibusParte3 onibus) {
        this.onibus = onibus;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            onibus.cancelarAssento("Cliente desistiu");

            Thread.sleep(1000);
            onibus.cancelarAssento("Erro no pagamento");

            Thread.sleep(1000);
            onibus.cancelarAssento("Remarcacao de viagem");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}