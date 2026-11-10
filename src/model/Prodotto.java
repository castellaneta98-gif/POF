package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Prodotto {
    private static final String FORMATO_RIGA = "%-3s | %-10s | %-20s | %-12s | %10s | %s";

    private final int id;
    private final String nome;
    private final LocalDate dataInserimento;
    private final double prezzo;
    private final String marca;
    private boolean disponibile; // l'unico campo che può cambiare (acquisto / restituzione)

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

    /** Intestazione della tabella, con le stesse larghezze delle righe stampate da toString(). */
    public static String intestazione() {
        return String.format(FORMATO_RIGA, "ID", "Data ins.", "Nome", "Marca", "Prezzo", "Disponibile");
    }

    // Ordine richiesto dalla consegna: ID, Data inserimento, Nome, Marca, Prezzo, Disponibile
    @Override
    public String toString() {
        return String.format(FORMATO_RIGA, id, getDataFormattata(), nome, marca,
                getPrezzoFormattato(), disponibile ? "SI" : "NO");
    }
}
