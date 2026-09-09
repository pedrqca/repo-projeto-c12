package model;

public class Pedido {
    private final int id;
    private final String nomePrato;
    private final int tempoPreparo;

    public Pedido(int id, String nomePrato, int tempoPreparo) {
        if (tempoPreparo < 0) {
            throw new IllegalArgumentException("O tempo de preparo nao pode ser negativo.");
        }
        this.id = id;
        this.nomePrato = nomePrato;
        this.tempoPreparo = tempoPreparo;
    }

    public int getId() { return id; }
    public String getNomePrato() { return nomePrato; }
    public int getTempoPreparo() { return tempoPreparo; }
}