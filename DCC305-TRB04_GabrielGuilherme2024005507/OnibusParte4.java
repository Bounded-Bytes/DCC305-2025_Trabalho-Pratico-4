import java.util.concurrent.Semaphore;

public class OnibusParte4 {
    // Atributos
    private int assentosDisponiveis;
    private Semaphore semaforoConexoes = new Semaphore(3); // Máximo 3 conexões simultâneas

    // Construtor
    public OnibusParte4(int assentos) {
        this.assentosDisponiveis = assentos;
    }

    // Getter
    public int getAssentosDisponiveis() {
        return assentosDisponiveis;
    }

    // PARTE IV: Controle de Fluxo com Semáforos
    // SOLUÇÃO: Limita o número de agentes que podem acessar o sistema ao mesmo
    // tempo
    public void reservarAssento(String agente) {
        try {
            System.out.println("[PARTE IV] " + agente + " tentando entrar no sistema...");
            semaforoConexoes.acquire(); // "Segurança da balada" - só 3 agentes por vez
            System.out.println("[PARTE IV] " + agente + " ENTROU no sistema ("
                    + (3 - semaforoConexoes.availablePermits()) + "/3 conexoes)");

            // Seção crítica protegida por synchronized
            synchronized (this) {
                if (assentosDisponiveis > 0) {
                    System.out.println(
                            "[PARTE IV] " + agente + " verificou: " + assentosDisponiveis + " assentos disponiveis.");

                    // Simula latência entre verificação e decremento
                    Thread.sleep(100);

                    assentosDisponiveis--;
                    System.out.println(
                            "[PARTE IV] " + agente + " vendeu! Restam: " + assentosDisponiveis + " assentos.");
                } else {
                    System.out.println("[PARTE IV] " + agente + ": sem vagas!");
                }
            } // Fim do bloco synchronized

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforoConexoes.release(); // Libera a permissão
            System.out.println("[PARTE IV] " + agente + " SAIU do sistema.");
        }
    }
}