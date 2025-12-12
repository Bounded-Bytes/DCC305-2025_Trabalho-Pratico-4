public class OnibusParte2 {
    // Atributo
    private int assentosDisponiveis;

    // Construtor
    public OnibusParte2(int assentos) {
        this.assentosDisponiveis = assentos;
    }

    // Getter
    public int getAssentosDisponiveis() {
        return assentosDisponiveis;
    }

    // PARTE II: A Solução com Blocos Sincronizados (Mutex)
    // SOLUÇÃO: Bloco synchronized apenas na seção crítica (verificação +
    // decremento)
    public void reservarAssento(String agente) {
        // Pedaço não-crítico (pode ser executado em paralelo)
        System.out.println("[PARTE II] " + agente + " chegando para fazer operacao.");

        synchronized (this) {
            // Seção crítica (apenas uma thread por vez aqui)
            if (assentosDisponiveis > 0) {
                System.out.println(
                        "[PARTE II] " + agente + " verificou: " + assentosDisponiveis
                                + " assentos disponiveis. (sync-block)");

                // Simula latência entre verificação e decremento
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                assentosDisponiveis--;
                System.out.println(
                        "[PARTE II] " + agente + " vendeu! Restam: " + assentosDisponiveis + " assentos. (sync-block)");
            } else {
                System.out.println("[PARTE II] " + agente + ": sem vagas! (sync-block)");
            }
        } // Fim da seção crítica
    }
}