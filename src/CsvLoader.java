import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Legge i 3 file CSV (separatore ";") e li trasforma in oggetti. */
public class CsvLoader {

    // "d/M/yyyy" accetta sia 23/4/2021 sia 22/07/2021
    public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("d/M/yyyy");

    public static List<Utente> caricaUtenti(String percorso) throws IOException {
        List<Utente> lista = new ArrayList<>();
        for (String[] c : leggiRighe(percorso)) {
            lista.add(new Utente(
                    Integer.parseInt(c[0].trim()),
                    c[1].trim(),
                    c[2].trim(),
                    LocalDate.parse(c[3].trim(), FORMATO_DATA),
                    c[4].trim(),
                    c[5].trim()));
        }
        return lista;
    }

    // Colonne reali del file: ID;Nome;Data di inserimento;Prezzo;Marca;Disponibile
    public static List<Prodotto> caricaProdotti(String percorso) throws IOException {
        List<Prodotto> lista = new ArrayList<>();
        for (String[] c : leggiRighe(percorso)) {
            lista.add(new Prodotto(
                    Integer.parseInt(c[0].trim()),
                    c[1].trim(),
                    LocalDate.parse(c[2].trim(), FORMATO_DATA),
                    parsePrezzo(c[3]),
                    c[4].trim(),
                    c[5].trim().equalsIgnoreCase("SI")));
        }
        return lista;
    }

    public static List<Vendita> caricaVendite(String percorso) throws IOException {
        List<Vendita> lista = new ArrayList<>();
        for (String[] c : leggiRighe(percorso)) {
            lista.add(new Vendita(
                    Integer.parseInt(c[0].trim()),
                    Integer.parseInt(c[1].trim()),
                    Integer.parseInt(c[2].trim())));
        }
        return lista;
    }

    /** "2,15 €" -> 2.15 */
    private static double parsePrezzo(String testo) {
        String pulito = testo.replaceAll("[^0-9,.]", "").replace(",", ".");
        return Double.parseDouble(pulito);
    }

    /** Legge il file, salta l'intestazione e le righe vuote, spezza ogni riga su ";". */
    private static List<String[]> leggiRighe(String percorso) throws IOException {
        List<String[]> righe = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(percorso), StandardCharsets.UTF_8)) {
            br.readLine(); // intestazione
            String riga;
            while ((riga = br.readLine()) != null) {
                if (riga.replace(";", "").isBlank()) continue; // salta anche righe tipo ";;;;;"
                righe.add(riga.split(";", -1)); // -1 mantiene anche le colonne vuote finali
            }
        }
        return righe;
    }
}
