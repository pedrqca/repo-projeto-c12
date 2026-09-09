import type { Order, SimulationResult } from '../types/simulation'

export const orders: Order[] = [
  { id: 1, dish: 'Hambúrguer', prepSeconds: 3 }, { id: 2, dish: 'Batata frita', prepSeconds: 1 },
  { id: 3, dish: 'Pizza', prepSeconds: 5 }, { id: 4, dish: 'Lasanha', prepSeconds: 7 },
  { id: 5, dish: 'Hot dog', prepSeconds: 2 }, { id: 6, dish: 'Salada', prepSeconds: 2 },
  { id: 7, dish: 'Sopa', prepSeconds: 4 }, { id: 8, dish: 'Macarrão', prepSeconds: 6 },
  { id: 9, dish: 'Tacos', prepSeconds: 3 }, { id: 10, dish: 'Risoto', prepSeconds: 5 },
  { id: 11, dish: 'Nuggets', prepSeconds: 2 }, { id: 12, dish: 'Bife acebolado', prepSeconds: 8 },
  { id: 13, dish: 'Panqueca', prepSeconds: 4 }, { id: 14, dish: 'Sanduíche', prepSeconds: 1 },
  { id: 15, dish: 'Peixe grelhado', prepSeconds: 6 }, { id: 16, dish: 'Coxinha', prepSeconds: 2 },
  { id: 17, dish: 'Escondidinho', prepSeconds: 5 }, { id: 18, dish: 'Wrap', prepSeconds: 3 },
  { id: 19, dish: 'Calzone', prepSeconds: 7 }, { id: 20, dish: 'Brownie', prepSeconds: 4 },
]

const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export async function runSimulation(cooks: number): Promise<SimulationResult> {
  const response = await fetch(`${API_URL}/api/simulacoes`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ quantidadeCozinheiros: cooks }),
  })

  const body = (await response.json()) as SimulationResult | { erro?: string }
  if (!response.ok) {
    throw new Error('erro' in body && body.erro ? body.erro : 'A API recusou a simulacao.')
  }
  return body as SimulationResult
}