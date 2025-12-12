# 🚌 Sistema AMATUR - Controle de Concorrência em Java

**Disciplina:** Programação Orientada a Objetos  
**Aluno:** [Seu Nome]  
**Tema:** Controle de Concorrência e Sincronização de Threads

---

## 📋 Sumário

1. [Contextualização](#contextualização)
2. [Parte I - Race Condition (O Caos)](#parte-i---race-condition-o-caos)
3. [Parte II - Synchronized (Mutex)](#parte-ii---synchronized-mutex)
4. [Parte III - Wait e Notify](#parte-iii---wait-e-notify)
5. [Parte IV - Semáforos](#parte-iv---semáforos)
6. [Conclusões](#conclusões)

---

## 🎯 Contextualização

A AMATUR enfrenta problemas de **overbooking** em rotas concorridas devido à falta de controle de concorrência. Múltiplos agentes de venda tentam reservar a mesma poltrona simultaneamente, resultando em mais vendas do que assentos disponíveis.

Este projeto simula esse cenário caótico e implementa soluções progressivas utilizando técnicas de sincronização em Java.

---

## 🔴 Parte I - Race Condition (O Caos)

### 📊 Saída do Console (Problema)

```
======================================================================
PARTE I: DEMONSTRANDO RACE CONDITION (O CAOS)
======================================================================

[PARTE I] Agente-1 verificou: 5 assentos disponíveis
[PARTE I] Agente-3 verificou: 5 assentos disponíveis
[PARTE I] Agente-2 verificou: 5 assentos disponíveis
[PARTE I] Agente-5 verificou: 5 assentos disponíveis
[PARTE I] Agente-4 verificou: 5 assentos disponíveis
[PARTE I] Agente-7 verificou: 5 assentos disponíveis
[PARTE I] Agente-6 verificou: 5 assentos disponíveis
[PARTE I] ✓ Agente-1 RESERVOU! Restam: 4 assentos
[PARTE I] ✓ Agente-3 RESERVOU! Restam: 3 assentos
[PARTE I] ✓ Agente-2 RESERVOU! Restam: 2 assentos
[PARTE I] ✓ Agente-5 RESERVOU! Restam: 1 assentos
[PARTE I] ✓ Agente-4 RESERVOU! Restam: 0 assentos
[PARTE I] ✓ Agente-7 RESERVOU! Restam: -1 assentos  ⚠️ OVERBOOKING!
[PARTE I] ✓ Agente-6 RESERVOU! Restam: -2 assentos  ⚠️ OVERBOOKING!

⚠️ RESULTADO: -2 assentos (OVERBOOKING DETECTADO!)
```

### 🔍 Análise do Problema

**O que aconteceu?**

1. **7 threads** foram criadas, mas há apenas **5 assentos**
2. Todas as threads executaram a verificação `if (assentosDisponiveis > 0)` **simultaneamente**
3. Como não há sincronização, **todas viram 5 assentos disponíveis**
4. O `Thread.sleep(100)` simula latência, mas as threads já passaram pela verificação
5. Todas decrementam o contador, resultando em **-2 assentos** (overbooking de 2 passagens!)

**Por que isso é um Race Condition?**

- **Race Condition** ocorre quando o resultado depende da **ordem de execução** das threads
- A operação **"verificar e decrementar"** não é **atômica** (indivisível)
- Entre verificar e decrementar, outras threads podem interferir

**Analogia:** É como várias pessoas vendo a mesma vaga de estacionamento ao mesmo tempo e todas tentarem estacionar nela!

---

## 🟢 Parte II - Synchronized (Mutex)

### 📊 Saída do Console (Solução)

```
======================================================================
PARTE II: SOLUÇÃO COM SYNCHRONIZED (MUTEX)
======================================================================

[PARTE II] Agente-1 verificou: 5 assentos disponíveis
[PARTE II] ✓ Agente-1 RESERVOU! Restam: 4 assentos
[PARTE II] Agente-2 verificou: 4 assentos disponíveis
[PARTE II] ✓ Agente-2 RESERVOU! Restam: 3 assentos
[PARTE II] Agente-3 verificou: 3 assentos disponíveis
[PARTE II] ✓ Agente-3 RESERVOU! Restam: 2 assentos
[PARTE II] Agente-4 verificou: 2 assentos disponíveis
[PARTE II] ✓ Agente-4 RESERVOU! Restam: 1 assentos
[PARTE II] Agente-5 verificou: 1 assentos disponíveis
[PARTE II] ✓ Agente-5 RESERVOU! Restam: 0 assentos
[PARTE II] ✗ Agente-6 - SEM VAGAS! (0 assentos)
[PARTE II] ✗ Agente-7 - SEM VAGAS! (0 assentos)

✓ RESULTADO: 0 assentos (SEM OVERBOOKING!)
```

### 🔐 Como o Synchronized Resolveu o Problema?

#### **Conceito de Mutex (Mutual Exclusion)**

O **synchronized** cria um **mutex (lock)** sobre o objeto, garantindo que:

1. **Apenas UMA thread** pode executar o bloco sincronizado por vez
2. As outras threads ficam **bloqueadas** esperando o lock ser liberado
3. A operação se torna **atômica** (indivisível)

#### **Implementação Inteligente**

```java
synchronized(this) {  // Lock no objeto atual (onibus)
    if (assentosDisponiveis > 0) {
        // Seção crítica protegida
        Thread.sleep(100);
        assentosDisponiveis--;
    }
}
```

**Por que usar bloco ao invés do método inteiro?**

- ✅ **Performance:** Só protege a **seção crítica** (verificação + modificação)
- ✅ **Flexibilidade:** Código fora do bloco pode executar em paralelo
- ❌ Sincronizar o método inteiro (`synchronized void reservarAssento()`) seria menos eficiente

#### **Fluxo Corrigido**

1. Thread 1 adquire o lock → verifica → decrementa → libera o lock
2. Thread 2 adquire o lock → verifica o **novo valor** → decrementa → libera
3. Thread 6 adquire o lock → verifica (0 assentos) → **não decrementa** → libera

**Resultado:** Inconsistência eliminada! ✅

---

## 🟡 Parte III - Wait e Notify

### 📊 Saída do Console (Produtor-Consumidor)

```
======================================================================
PARTE III: WAIT E NOTIFY (PRODUTOR-CONSUMIDOR)
======================================================================

[PARTE III] Agente-1 verificou: 5 assentos disponíveis
[PARTE III] ✓ Agente-1 RESERVOU! Restam: 4 assentos
[PARTE III] Agente-2 verificou: 4 assentos disponíveis
[PARTE III] ✓ Agente-2 RESERVOU! Restam: 3 assentos
[PARTE III] Agente-3 verificou: 3 assentos disponíveis
[PARTE III] ✓ Agente-3 RESERVOU! Restam: 2 assentos
[PARTE III] Agente-4 verificou: 2 assentos disponíveis
[PARTE III] ✓ Agente-4 RESERVOU! Restam: 1 assentos
[PARTE III] Agente-5 verificou: 1 assentos disponíveis
[PARTE III] ✓ Agente-5 RESERVOU! Restam: 0 assentos
[PARTE III] Agente-6 aguardando vaga... (WAIT)
[PARTE III] Agente-7 aguardando vaga... (WAIT)
[PARTE III] 🔄 CANCELAMENTO: Cliente desistiu | Assentos disponíveis: 1
[PARTE III] Agente-6 verificou: 1 assentos disponíveis
[PARTE III] ✓ Agente-6 RESERVOU! Restam: 0 assentos
[PARTE III] 🔄 CANCELAMENTO: Erro no pagamento | Assentos disponíveis: 1
[PARTE III] Agente-7 verificou: 1 assentos disponíveis
[PARTE III] ✓ Agente-7 RESERVOU! Restam: 0 assentos
[PARTE III] 🔄 CANCELAMENTO: Remarcação de viagem | Assentos disponíveis: 1

✓ RESULTADO: Todos os agentes foram atendidos com wait/notify!
```

### ⏸️ Como Wait/Notify Economiza CPU?

#### **Problema: Busy-Waiting (Espera Ativa)**

Sem `wait()`, uma thread sem sucesso ficaria em **loop infinito**:

```java
// ❌ PÉSSIMA PRÁTICA - Busy-waiting
while (assentosDisponiveis <= 0) {
    // Fica verificando milhões de vezes por segundo
    // Consome 100% da CPU sem fazer nada útil!
}
```

#### **Solução: Wait/Notify (Espera Passiva)**

```java
while (assentosDisponiveis <= 0) {
    wait(); // ✅ Thread DORME e NÃO consome CPU!
}
```

**Como funciona?**

1. **wait():**
   - Thread **libera o lock** (outras podem entrar)
   - Entra em estado **WAITING** (não consome CPU)
   - É colocada na **wait set** do objeto

2. **notifyAll():**
   - Acorda **todas** as threads na wait set
   - Elas competem pelo lock novamente
   - Quando pegam o lock, saem do `wait()` e continuam

#### **Analogia do Restaurante**

- **Busy-waiting:** Cliente fica perguntando "Minha mesa está pronta?" a cada segundo (chato e ineficiente)
- **Wait/Notify:** Cliente senta na sala de espera e o garçom **chama** quando a mesa fica disponível (civilizado e eficiente)

#### **Economia de CPU**

| Método | Uso de CPU | Estado da Thread |
|--------|-----------|------------------|
| **Busy-waiting** | 🔴 100% (loop ativo) | RUNNABLE |
| **wait()** | 🟢 0% (dormindo) | WAITING |

**Vantagem:** Milhares de threads podem esperar sem sobrecarregar o sistema!

---

## 🟣 Parte IV - Semáforos

### 📊 Saída do Console (Controle de Conexões)

```
======================================================================
PARTE IV: CONTROLE DE FLUXO COM SEMÁFOROS
======================================================================

[PARTE IV] Agente-1 tentando entrar no sistema...
[PARTE IV] Agente-2 tentando entrar no sistema...
[PARTE IV] Agente-3 tentando entrar no sistema...
[PARTE IV] 🚪 Agente-1 ENTROU no sistema (1/3 conexões)
[PARTE IV] 🚪 Agente-2 ENTROU no sistema (2/3 conexões)
[PARTE IV] 🚪 Agente-3 ENTROU no sistema (3/3 conexões)
[PARTE IV] Agente-4 tentando entrar no sistema...  ⏳ Aguardando...
[PARTE IV] Agente-5 tentando entrar no sistema...  ⏳ Aguardando...
[PARTE IV] Agente-1 verificou: 5 assentos disponíveis
[PARTE IV] ✓ Agente-1 RESERVOU! Restam: 4 assentos
[PARTE IV] 🚪 Agente-1 SAIU do sistema
[PARTE IV] 🚪 Agente-4 ENTROU no sistema (3/3 conexões)
[PARTE IV] Agente-2 verificou: 4 assentos disponíveis
[PARTE IV] ✓ Agente-2 RESERVOU! Restam: 3 assentos
[PARTE IV] 🚪 Agente-2 SAIU do sistema
[PARTE IV] 🚪 Agente-5 ENTROU no sistema (3/3 conexões)
[PARTE IV] Agente-3 verificou: 3 assentos disponíveis
[PARTE IV] ✓ Agente-3 RESERVOU! Restam: 2 assentos
[PARTE IV] 🚪 Agente-3 SAIU do sistema

✓ RESULTADO: Sistema limitou conexões simultâneas a 3!
```

### 🚦 Semáforo: "Segurança da Balada"

#### **Conceito**

Um **Semaphore** controla o número de **permissões disponíveis**:

```java
Semaphore semaforoConexoes = new Semaphore(3); // 3 permissões
```

#### **Operações**

1. **acquire()** - Pega uma permissão (decrementa o contador)
   - Se contador > 0: Permissão concedida
   - Se contador = 0: Thread **bloqueia** até alguém liberar

2. **release()** - Devolve a permissão (incrementa o contador)
   - Acorda uma thread que estava esperando

#### **Analogia**

Imagine uma balada com capacidade para **3 pessoas**:

- 🚶 Pessoa tenta entrar → **acquire()** (pega uma ficha)
- 👥 3 pessoas dentro → Capacidade cheia!
- 🚶 4ª pessoa → **Fica na fila** (sem fichas disponíveis)
- 🚪 Alguém sai → **release()** (devolve a ficha)
- ✅ Próxima pessoa da fila pode entrar!

#### **Diferença: Semaphore vs Synchronized**

| Característica | Synchronized | Semaphore |
|---------------|-------------|-----------|
| **Permissões** | 1 (lock binário) | N (configurável) |
| **Uso** | Proteger dados | Controlar acesso |
| **Threads simultâneas** | 1 | N |

**Neste projeto:**
- **Synchronized** protege `assentosDisponiveis` (dados compartilhados)
- **Semaphore** limita conexões ao servidor (controle de recursos)

---

## 🎓 Conclusões

### **Lições Aprendidas**

1. **Race Conditions** são bugs silenciosos e perigosos em sistemas concorrentes
2. **Synchronized** garante exclusão mútua em seções críticas
3. **Wait/Notify** implementa comunicação eficiente entre threads (produtor-consumidor)
4. **Semaphores** controlam acesso a recursos limitados

### **Aplicações Reais**

- 🎫 **Sistemas de reserva** (hotéis, passagens, ingressos)
- 🏦 **Bancos de dados** (transações ACID)
- 🌐 **Servidores web** (pool de conexões)
- 🎮 **Jogos multiplayer** (sincronização de estado)

### **Próximos Passos**

- Estudar **java.util.concurrent** (ExecutorService, CountDownLatch, CyclicBarrier)
- Aprender sobre **locks explícitos** (ReentrantLock, ReadWriteLock)
- Praticar **padrões de concorrência** (Producer-Consumer, Readers-Writers)

---

## 📚 Referências

- Oracle Java Documentation - Concurrency
- Material da Aula-22 - Programação Orientada a Objetos
- Java Concurrency in Practice (Brian Goetz)

---

**Desenvolvido para a disciplina de Programação Orientada a Objetos**  
**Data:** Dezembro de 2025