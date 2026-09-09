import { AlertCircle, CheckCircle2, Clock3, Gauge, ListChecks } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Header } from './components/Header'
import { LoadingSimulation } from './components/LoadingSimulation'
import { MetricCard } from './components/MetricCard'
import { OrderList } from './components/OrderList'
import { PerformanceStatus } from './components/PerformanceStatus'
import { SimulationChart } from './components/SimulationChart'
import { SimulationControls } from './components/SimulationControls'
import { SimulationHistory } from './components/SimulationHistory'
import { orders } from './services/simulationApi'
import { useSimulation } from './hooks/useSimulation'

function App() {
  const [cooks, setCooks] = useState(5)
  const [loadingIndex, setLoadingIndex] = useState(0)
  const { status, result, history, execute } = useSimulation()
  const isLoading = status === 'loading'

  useEffect(() => {
    if (!isLoading) return

    const timer = window.setInterval(() => {
      setLoadingIndex((current) => (current + 1) % orders.length)
    }, 900)

    return () => window.clearInterval(timer)
  }, [isLoading])

  const activeOrder = orders[loadingIndex]
  const queue = Array.from({ length: 4 }, (_, index) => orders[(loadingIndex + index + 1) % orders.length])

  return (
    <main className="min-h-screen overflow-hidden bg-ink text-slate-100">
      <div className="mx-auto max-w-[1500px] px-5 py-8 sm:px-8 lg:px-12 lg:py-12">
        <Header />

        <div className="mt-8 grid gap-5 lg:mt-10 lg:grid-cols-[minmax(0,1fr)_minmax(420px,0.95fr)] lg:items-start">
          <div className="space-y-5">
            <SimulationControls
              cooks={cooks}
              loading={isLoading}
              onChange={setCooks}
              onRun={() => void execute(cooks)}
            />

            {status === 'error' && (
              <div role="alert" className="flex items-center gap-3 rounded-lg border border-[#8f5a55] bg-[#8e1b1b]/20 px-4 py-3 text-sm text-[#f0c4b7]">
                <AlertCircle size={18} />
                Não foi possível executar a simulação. Tente novamente.
              </div>
            )}

            {status === 'loading' && (
              <LoadingSimulation cooks={cooks} activeOrder={activeOrder} queue={queue} />
            )}

            {result ? (
              <>
                <div className="grid gap-3 sm:grid-cols-2">
                  <MetricCard label="Tempo total" value={`${result.duracaoSegundos.toFixed(2)}s`} detail="Duração real da execução" icon={Clock3} accent="red" />
                  <MetricCard label="Mínimo teórico" value={`${result.tempoMinimoTeorico.toFixed(2)}s`} detail="Pedido mais demorado da fila" icon={Gauge} accent="gold" />
                  <MetricCard label="Pedidos processados" value={`${result.pedidosProcessados} / ${result.quantidadePedidos}`} detail="Nenhum pedido ficou para trás" icon={ListChecks} accent="green" />
                  <MetricCard label="Diferença" value={`${result.diferencaPercentual.toFixed(2)}%`} detail={`${result.diferencaSegundos.toFixed(2)}s acima do mínimo`} icon={CheckCircle2} accent="green" />
                </div>
                <PerformanceStatus result={result} />
              </>
            ) : !isLoading ? (
              <div className="panel border-dashed p-8 text-center text-sm text-[#b9adad]">
                Configure a quantidade de cozinheiros e execute uma simulação real para visualizar os dados.
              </div>
            ) : null}

            <SimulationHistory history={history} />
          </div>

          <div className="space-y-5">
            <SimulationChart history={history} />
            <OrderList orders={orders} />
          </div>
        </div>

        <footer className="mt-10 border-t border-[#493537] pt-5 text-xs text-[#847678]">
          Fonte dos resultados: Simulacao.java via API HTTP Java · nenhuma medição mockada
        </footer>
      </div>
    </main>
  )
}

export default App
