export interface Order {
  id: number
  dish: string
  prepSeconds: number
}

export interface SimulationResult {
  quantidadeCozinheiros: number
  quantidadePedidos: number
  pedidosProcessados: number
  duracaoSegundos: number
  tempoMinimoTeorico: number
  diferencaSegundos: number
  diferencaPercentual: number
}