package concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import model.Pedido;

public class Cozinheiro implements Runnable {
    private final String nome;
    private final BlockingQueue<Pedido> filaPedidos;
    private final AtomicInteger pedidosProcessados;

    public Cozinheiro(String nome, BlockingQueue<Pedido> filaPedidos,
            AtomicInteger pedidosProcessados) {
        this.nome = nome;
        this.filaPedidos = filaPedidos;
        this.pedidosProcessados = pedidosProcessados;
    }

    @Override
    public void run() {
        Pedido pedido;
        while ((pedido = filaPedidos.poll()) != null) {
            PedidoRunnable preparo = new PedidoRunnable(pedido, nome);
            preparo.run();
            if (preparo.isConcluido()) {
                pedidosProcessados.incrementAndGet();
            }
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }
}