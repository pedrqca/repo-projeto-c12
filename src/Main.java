// Classe que implementa a interface Runnable para definir a tarefa a ser executada por cada thread
class PedidoRunnable implements Runnable {
    private int idPedido;
    private String nomePrato;
    private int tempoPreparo;

    /**
     * CONSTRUTOR:
     * Recebe e armazena os dados específicos de cada pedido (ID, nome e tempo de preparo)
     * quando um novo objeto PedidoRunnable é criado.
     */
    public PedidoRunnable(int idPedido, String nomePrato, int tempoPreparo) {
        this.idPedido = idPedido;
        this.nomePrato = nomePrato;
        this.tempoPreparo = tempoPreparo;
    }

    /**
     * MÉTODO run():
     * Obrigatório ao implementar a interface Runnable. Define o ciclo de vida e a rotina
     * que será executada de forma independente quando a thread for iniciada.
     */
    @Override
    public void run() {
        System.out.println(" [INICIO] Pedido #" + idPedido + " (" + nomePrato + ") começou a ser preparado.");
        try {
            // Thread.sleep(): Pausa a execução da thread atual durante o tempo especificado (em ms).
            // Simula o tempo que o cozinheiro leva preparando o prato sem travar as outras threads.
            Thread.sleep(tempoPreparo * 1000L);
        } catch (InterruptedException e) {
            // Captura exceções caso a thread seja interrompida inesperadamente enquanto está em sleep.
            e.printStackTrace();
        }
        System.out.println(" [CONCLUIDO] Pedido #" + idPedido + " (" + nomePrato + ") esta pronto!");
    }
}

public class Main {
    /**
     * MÉTODO main():
     * Ponto de entrada da aplicação (Thread Principal).
     * Responsável por instanciar, disparar e sincronizar as threads filhas.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- SISTEMA DE GERENCIAMENTO DE PEDIDOS (RESTAURANTE) ---\n");

        // Criação dos objetos Thread passando a rotina Runnable como parâmetro
        Thread t1 = new Thread(new PedidoRunnable(1, "Hamburguer", 3));
        Thread t2 = new Thread(new PedidoRunnable(2, "Batata Frita", 1));
        Thread t3 = new Thread(new PedidoRunnable(3, "Pizza", 5));

        // Método .start(): Notifica o Sistema Operacional para alocar recursos e iniciar a thread,
        // chamando internamente o método run() de forma concorrente/assíncrona.
        t1.start();
        t2.start();
        t3.start();

        // Método .join(): Faz a Thread Principal (main) pausar e aguardar a conclusão
        // das threads t1, t2 e t3 antes de continuar a execução das linhas seguintes.
        t1.join();
        t2.join();
        t3.join();

        System.out.println("\nTodos os pedidos foram entregues com sucesso!");
    }
}