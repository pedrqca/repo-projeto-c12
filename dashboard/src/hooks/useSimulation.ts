import { useState } from 'react'
import { runSimulation } from '../services/simulationApi'
import type { SimulationResult } from '../types/simulation'

type SimulationStatus = 'idle' | 'loading' | 'success' | 'error'

export function useSimulation() {
  const [status, setStatus] = useState<SimulationStatus>('idle')
  const [result, setResult] = useState<SimulationResult | null>(null)
  const [history, setHistory] = useState<SimulationResult[]>([])

  async function execute(cooks: number) {
    setStatus('loading')
    try {
      const nextResult = await runSimulation(cooks)
      setResult(nextResult)
      setHistory((current) => [...current.filter((item) => item.quantidadeCozinheiros !== cooks), nextResult].sort((a, b) => a.quantidadeCozinheiros - b.quantidadeCozinheiros))
      setStatus('success')
    } catch { setStatus('error') }
  }

  return { status, result, history, execute }
}