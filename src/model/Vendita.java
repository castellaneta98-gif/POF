package model;

public class Vendita {
    private final int id;
    private final int idProdotto;
    private final int idUtente;

    public Vendita(int id, int idProdotto, int idUtente) {
        this.id = id;
        this.idProdotto = idProdotto;
        this.idUtente = idUtente;
    }

    public int getId() { return id; }
    public int getIdProdotto() { return idProdotto; }
    public int getIdUtente() { return idUtente; }

    @Override
    public String toString() {
        return "Vendita " + id + " | prodotto " + idProdotto + " | utente " + idUtente;
    }
}
