class PedidoRunnable implements Runnable {
    private int idPedido;
    private String nomePrato;
    private int tempoPreparo;

    public PedidoRunnable(int idPedido, String nomePrato, int tempoPreparo) {
        this.idPedido = idPedido;
        this.nomePrato = nomePrato;
        this.tempoPreparo = tempoPreparo;
    }

    @Override
    public void run() {
        System.out.println(" [INICIO] Pedido #" + idPedido + " (" + nomePrato + ") começou a ser preparado.");
        try {
            Thread.sleep(tempoPreparo * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" [CONCLUIDO] Pedido #" + idPedido + " (" + nomePrato + ") esta pronto!");
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- SISTEMA DE GERENCIAMENTO DE PEDIDOS (RESTAURANTE) ---\n");

        Thread t1 = new Thread(new PedidoRunnable(1, "Hamburguer", 3));
        Thread t2 = new Thread(new PedidoRunnable(2, "Batata Frita", 1));
        Thread t3 = new Thread(new PedidoRunnable(3, "Pizza", 5));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("\nTodos os pedidos foram entregues com sucesso!");
    }
}