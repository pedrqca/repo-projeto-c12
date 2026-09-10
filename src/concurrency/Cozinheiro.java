package concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import model.Pedido;
import simulation.EventoSimulacao;

public class Cozinheiro implements Runnable {
    private final String nome;
    private final BlockingQueue<Pedido> filaPedidos;
    private final AtomicInteger pedidosProcessados;
    private final Consumer<EventoSimulacao> observador;

    public Cozinheiro(String nome, BlockingQueue<Pedido> filaPedidos,
            AtomicInteger pedidosProcessados, Consumer<EventoSimulacao> observador) {
        this.nome = nome;
        this.filaPedidos = filaPedidos;
        this.pedidosProcessados = pedidosProcessados;
        this.observador = observador;
    }

    @Override
    public void run() {
        Pedido pedido;
        while ((pedido = filaPedidos.poll()) != null) {
            publicar(EventoSimulacao.criar("PEDIDO_INICIADO", nome, pedido));
            PedidoRunnable preparo = new PedidoRunnable(pedido, nome);
            preparo.run();
            if (preparo.isConcluido()) {
                pedidosProcessados.incrementAndGet();
                publicar(EventoSimulacao.criar("PEDIDO_CONCLUIDO", nome, pedido));
            }
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }

    private void publicar(EventoSimulacao evento) {
        if (observador != null) observador.accept(evento);
    }
}