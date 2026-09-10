package simulation;

import concurrency.Cozinheiro;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import model.Pedido;

public class Simulacao {
    public static final int MINIMO_COZINHEIROS = 1;
    public static final int MAXIMO_COZINHEIROS = 20;
    public static final double MARGEM_LIMITE_DESEMPENHO = 0.05;

    private final List<Pedido> pedidos;
    private final int quantidadeCozinheiros;

    public Simulacao(List<Pedido> pedidos, int quantidadeCozinheiros) {
        if (quantidadeCozinheiros < MINIMO_COZINHEIROS
            || quantidadeCozinheiros > MAXIMO_COZINHEIROS) {
            throw new IllegalArgumentException(
                "A quantidade de cozinheiros deve estar entre 1 e 20.");
        }
        this.pedidos = new ArrayList<>(pedidos);
        this.quantidadeCozinheiros = quantidadeCozinheiros;
    }

    public ResultadoSimulacao executar() throws InterruptedException {
        return executar(null);
    }

    public ResultadoSimulacao executar(Consumer<EventoSimulacao> observador)
            throws InterruptedException {
        BlockingQueue<Pedido> filaPedidos = new ArrayBlockingQueue<>(pedidos.size());
        filaPedidos.addAll(pedidos);
        AtomicInteger pedidosProcessados = new AtomicInteger();
        List<Thread> threads = new ArrayList<>();

        publicar(observador, EventoSimulacao.criar(
                "SIMULACAO_INICIADA", "", null));
        for (Pedido pedido : pedidos) {
            publicar(observador, EventoSimulacao.criar(
                "PEDIDO_AGUARDANDO", "", pedido));
        }

        for (int i = 1; i <= quantidadeCozinheiros; i++) {
            Thread thread = new Thread(
                    new Cozinheiro("COZINHEIRO " + i, filaPedidos,
                            pedidosProcessados, observador),
                    "cozinheiro-" + i);
            threads.add(thread);
        }

        long instanteInicial = System.nanoTime();
        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();
        long instanteFinal = System.nanoTime();

        int maiorTempoPreparo = pedidos.stream()
                .mapToInt(Pedido::getTempoPreparo)
                .max()
                .orElse(0);

        ResultadoSimulacao resultado = new ResultadoSimulacao(quantidadeCozinheiros, pedidos.size(),
                pedidosProcessados.get(), instanteInicial, instanteFinal,
                maiorTempoPreparo, MARGEM_LIMITE_DESEMPENHO);
        return resultado;
        }

        private void publicar(Consumer<EventoSimulacao> observador, EventoSimulacao evento) {
        if (observador != null) observador.accept(evento);
    }
}