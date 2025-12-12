class AgenteVenda implements Runnable {
    private String nome;
    private Object onibus;
    private int parte;

    public AgenteVenda(String nome, Object onibus, int parte) {
        this.nome = nome;
        this.onibus = onibus;
        this.parte = parte;
    }

    @Override
    public void run() {
        switch (parte) {
            case 1:
                ((OnibusParte1) onibus).reservarAssento(nome);
                break;
            case 2:
                ((OnibusParte2) onibus).reservarAssento(nome);
                break;
            case 3:
                ((OnibusParte3) onibus).reservarAssento(nome);
                break;
            case 4:
                ((OnibusParte4) onibus).reservarAssento(nome);
                break;
        }
    }
}