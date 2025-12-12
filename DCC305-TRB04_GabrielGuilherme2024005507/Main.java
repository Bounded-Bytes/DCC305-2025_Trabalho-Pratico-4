public class Main {
    public static void main(String[] args) throws InterruptedException {

        // PARTE I
        System.out.println("==== PARTE I: O Caos (Race Condition - sem protecao) ====");
        OnibusParte1 onibus1 = new OnibusParte1(5);
        Thread[] agentes1 = new Thread[7];

        for (int i = 0; i < 7; i++) {
            agentes1[i] = new Thread(new AgenteVenda("Agente-" + (i + 1), onibus1, 1));
            agentes1[i].start();
        }

        for (Thread t : agentes1) {
            t.join();
        }

        System.out.println("\nRESULTADO: " + onibus1.getAssentosDisponiveis() + " assentos (OVERBOOKING DETECTADO!)");

        Thread.sleep(2500); // Pausa entre as partes

        // PARTE II
        System.out.println("\n==== PARTE II: A Solucao com Blocos Sincronizados (Mutex) ====");
        OnibusParte2 onibus2 = new OnibusParte2(5);
        Thread[] agentes2 = new Thread[7];

        for (int i = 0; i < 7; i++) {
            agentes2[i] = new Thread(new AgenteVenda("Agente-" + (i + 1), onibus2, 2));
            agentes2[i].start();
        }

        for (Thread t : agentes2) {
            t.join();
        }

        System.out.println("\nRESULTADO: " + onibus2.getAssentosDisponiveis() + " assentos (SEM OVERBOOKING!)");

        Thread.sleep(2500); // Pausa entre as partes

        // PARTE III
        System.out.println("\n==== PARTE III: O Desafio de Casa (Wait e Notify) ====");
        OnibusParte3 onibus3 = new OnibusParte3(5);

        // Thread de cancelamento (Produtor)
        Thread threadCancelamento = new Thread(new Cancelamento(onibus3));
        threadCancelamento.start();

        // Agentes de venda (Consumidores)
        Thread[] agentes3 = new Thread[7];
        for (int i = 0; i < 7; i++) {
            agentes3[i] = new Thread(new AgenteVenda("Agente-" + (i + 1), onibus3, 3));
            agentes3[i].start();
        }

        for (Thread t : agentes3) {
            t.join();
        }
        threadCancelamento.join();

        System.out.println("\nRESULTADO: Todos os agentes foram atendidos com wait/notify!");

        Thread.sleep(2500); // Pausa entre as partes

        // PARTE IV
        System.out.println("\n==== PARTE IV: Controle de Fluxo com Semaforos ====");
        OnibusParte4 onibus4 = new OnibusParte4(5);
        Thread[] agentes4 = new Thread[7];

        for (int i = 0; i < 7; i++) {
            agentes4[i] = new Thread(new AgenteVenda("Agente-" + (i + 1), onibus4, 4));
            agentes4[i].start();
        }

        for (Thread t : agentes4) {
            t.join();
        }

        System.out.println("\nRESULTADO: Sistema limitou conexoes simultaneas a 3.");

        System.out.println("\n" + "=".repeat(70));
        System.out.println("SIMULACAO COMPLETA");
        System.out.println("=".repeat(70));
    }
}