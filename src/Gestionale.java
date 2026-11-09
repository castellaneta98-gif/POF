import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Tiene i dati in memoria e contiene la logica delle operazioni. */
public class Gestionale {
    private List<Utente> utenti = new ArrayList<>();
    private List<Prodotto> prodotti = new ArrayList<>();
    private List<Vendita> vendite = new ArrayList<>();

    public void carica(String fileUtenti, String fileProdotti, String fileVendite) throws CaricamentoFileException {
        utenti = CsvLoader.caricaUtenti(fileUtenti);
        prodotti = CsvLoader.caricaProdotti(fileProdotti);
        vendite = CsvLoader.caricaVendite(fileVendite);
    }

    // ---------- comando 1 ----------
    public void stampaProdotti() {
        System.out.println("ID  | Inserimento | Nome            | Marca      |    Prezzo | Disponibile");
        for (Prodotto p : prodotti) {
            System.out.println(p);
        }
    }

    // ---------- comando 2 ----------
    public Vendita compra(int idProdotto, int idUtente) {
        Prodotto p = trovaProdotto(idProdotto);
        if (p == null) throw new IllegalArgumentException("Prodotto " + idProdotto + " inesistente.");
        if (trovaUtente(idUtente) == null) throw new IllegalArgumentException("Utente " + idUtente + " inesistente.");
        if (!p.isDisponibile()) throw new IllegalArgumentException("Il prodotto non è disponibile.");

        Vendita v = new Vendita(prossimoIdVendita(), idProdotto, idUtente);
        vendite.add(v);
        p.setDisponibile(false);
        return v;
    }

    // ---------- comando 3 ----------
    public void restituisci(int idVendita) {
        Vendita v = trovaVendita(idVendita);
        if (v == null) throw new IllegalArgumentException("Vendita " + idVendita + " inesistente.");

        Prodotto p = trovaProdotto(v.getIdProdotto());
        if (p != null) p.setDisponibile(true);
        vendite.remove(v);
    }

    // ---------- comando 4 ----------
    public void aggiungiUtente(Utente nuovo) {
        if (trovaUtente(nuovo.getId()) != null) {
            throw new IllegalArgumentException("Esiste già un utente con ID " + nuovo.getId() + ".");
        }
        utenti.add(nuovo);
    }

    // ---------- comando 5 ----------
    public String esportaDisponibili() throws IOException {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        String nomeFile = "prodotti_" + data + ".csv";

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(nomeFile), StandardCharsets.UTF_8))) {
            pw.println("ID;Data inserimento;Nome;Marca;Prezzo");
            for (Prodotto p : prodotti) {
                if (p.isDisponibile()) {
                    pw.println(p.getId() + ";" + p.getDataFormattata() + ";" + p.getNome() + ";"
                            + p.getMarca() + ";" + p.getPrezzoFormattato());
                }
            }
        }
        return nomeFile;
    }

    // ---------- metodi di supporto ----------
    private Prodotto trovaProdotto(int id) {
        for (Prodotto p : prodotti) if (p.getId() == id) return p;
        return null;
    }

    private Utente trovaUtente(int id) {
        for (Utente u : utenti) if (u.getId() == id) return u;
        return null;
    }

    private Vendita trovaVendita(int id) {
        for (Vendita v : vendite) if (v.getId() == id) return v;
        return null;
    }

    private int prossimoIdVendita() {
        int max = 0;
        for (Vendita v : vendite) if (v.getId() > max) max = v.getId();
        return max + 1;
    }
}