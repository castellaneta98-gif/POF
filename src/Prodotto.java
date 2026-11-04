import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Prodotto {
    private int id;
    private String nome;
    private LocalDate dataInserimento;
    private double prezzo;
    private String marca;
    private boolean disponibile;

    public Prodotto(int id, String nome, LocalDate dataInserimento,
                    double prezzo, String marca, boolean disponibile) {
        this.id = id;
        this.nome = nome;
        this.dataInserimento = dataInserimento;
        this.prezzo = prezzo;
        this.marca = marca;
        this.disponibile = disponibile;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public LocalDate getDataInserimento() { return dataInserimento; }
    public double getPrezzo() { return prezzo; }
    public String getMarca() { return marca; }
    public boolean isDisponibile() { return disponibile; }
    public void setDisponibile(boolean disponibile) { this.disponibile = disponibile; }

    public String getDataFormattata() {
        return dataInserimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getPrezzoFormattato() {
        return String.format(Locale.ITALY, "%.2f €", prezzo);
    }

    // Ordine richiesto dalla consegna: ID, Data inserimento, Nome, Marca, Prezzo, Disponibile
    @Override
    public String toString() {
        return String.format("%-3d | %-10s | %-15s | %-10s | %9s | %s",
                id, getDataFormattata(), nome, marca, getPrezzoFormattato(),
                disponibile ? "SI" : "NO");
    }
}