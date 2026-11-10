package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utente {
    private final int id;
    private final String nome;
    private final String cognome;
    private final LocalDate dataNascita;
    private final String indirizzo;
    private final String documentoId;

    public Utente(int id, String nome, String cognome, LocalDate dataNascita,
                  String indirizzo, String documentoId) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.indirizzo = indirizzo;
        this.documentoId = documentoId;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public LocalDate getDataNascita() { return dataNascita; }
    public String getIndirizzo() { return indirizzo; }
    public String getDocumentoId() { return documentoId; }

    @Override
    public String toString() {
        return id + " | " + nome + " " + cognome + " | "
                + dataNascita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " | " + indirizzo + " | " + documentoId;
    }
}
