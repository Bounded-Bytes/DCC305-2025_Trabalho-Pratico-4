public class OnibusParte3 {
    // Atributo
    private int assentosDisponiveis;

    // Construtor
    public OnibusParte3(int assentos) {
        this.assentosDisponiveis = assentos;
    }

    // Getter
    public int getAssentosDisponiveis() {
        return assentosDisponiveis;
    }

    // PARTE III: usar wait() quando estiver lotado, notifyAll() no cancelamento
    public synchronized void reservarAssento(String agente) {
        // Se não há vagas, aguarda até que um cancelamento aconteça
        while (assentosDisponiveis <= 0) {
            try {
                System.out.println("[PARTE III] " + agente + " aguardando vaga... (WAIT)");
                wait(); // Thread entra em espera e libera o lock
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Quando acordar, há uma vaga disponível
        System.out.println("[PARTE III] " + agente + " verificou: " + assentosDisponiveis + " assentos disponiveis.");

        // Simula latência entre verificação e decremento
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assentosDisponiveis--;
        System.out.println("[PARTE III] " + agente + " vendeu! Restam: " + assentosDisponiveis + " assentos.");
    }

    // Método para cancelar uma reserva e liberar um assento
    public synchronized void cancelarAssento(String motivo) {
        assentosDisponiveis++;
        System.out
                .println("[PARTE III] CANCELAMENTO: " + motivo + " | Assentos disponiveis: " + assentosDisponiveis);
        notifyAll(); // Acorda TODAS as threads que estão esperando
    }
}