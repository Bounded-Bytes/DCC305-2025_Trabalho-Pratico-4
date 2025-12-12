public class OnibusParte1 {
    // Atributo
    private int assentosDisponiveis;

    // Construtor
    public OnibusParte1(int assentos) {
        this.assentosDisponiveis = assentos;
    }

    // Getter
    public int getAssentosDisponiveis() {
        return assentosDisponiveis;
    }

    // PARTE I: O Caos (Race Condition)
    // PROBLEMA: Sem sincronização - múltiplas threads podem passar por aqui ao
    // mesmo tempo
    public void reservarAssento(String agente) {
        if (assentosDisponiveis > 0) {
            System.out.println(
                    "[PARTE I] " + agente + " verificou: " + assentosDisponiveis + " assentos disponiveis. (caos)");

            // Simula latência entre verificação e decremento
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assentosDisponiveis--;
            System.out
                    .println("[PARTE I] " + agente + " vendeu! Restam: " + assentosDisponiveis + " assentos. (caos)");
        } else {
            System.out.println("[PARTE I] " + agente + ": sem vagas! (caos)");
        }
    }
}