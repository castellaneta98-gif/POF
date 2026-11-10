package io;

import exception.CaricamentoFileException;
import model.Prodotto;
import model.Utente;
import model.Vendita;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Legge i 3 file CSV (separatore ";") e li trasforma in oggetti. */
public class CsvLoader {

    // "d/M/yyyy" accetta sia 23/4/2021 sia 22/07/2021
    public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("d/M/yyyy");

    /** Dice come trasformare le colonne di UNA riga in un oggetto (Utente, Prodotto o Vendita). */
    @FunctionalInterface
    private interface ConvertitoreRiga<T> {
        T converti(String[] colonne);
    }

    // ---------- metodi pubblici ----------

    // Colonne: ID;Nome;Cognome;Data di nascita;Indirizzo;Documento ID
    public static List<Utente> caricaUtenti(String percorso) throws CaricamentoFileException {
        return carica(percorso, 6, c -> new Utente(
                Integer.parseInt(c[0].trim()),
                c[1].trim(),
                c[2].trim(),
                LocalDate.parse(c[3].trim(), FORMATO_DATA),
                c[4].trim(),
                c[5].trim()));
    }

    // Colonne reali del file: ID;Nome;Data di inserimento;Prezzo;Marca;Disponibile
    public static List<Prodotto> caricaProdotti(String percorso) throws CaricamentoFileException {
        return carica(percorso, 6, c -> new Prodotto(
                Integer.parseInt(c[0].trim()),
                c[1].trim(),
                LocalDate.parse(c[2].trim(), FORMATO_DATA),
                parsePrezzo(c[3]),
                c[4].trim(),
                parseDisponibile(c[5])));
    }

    // Colonne: ID;ID Prodotto;ID Utente
    public static List<Vendita> caricaVendite(String percorso) throws CaricamentoFileException {
        return carica(percorso, 3, c -> new Vendita(
                Integer.parseInt(c[0].trim()),
                Integer.parseInt(c[1].trim()),
                Integer.parseInt(c[2].trim())));
    }

    // ---------- lettura generica ----------

    /**
     * Legge un file, salta intestazione e righe vuote e converte ogni riga con il convertitore.
     * Se qualcosa va storto lancia una CaricamentoFileException con file e numero di riga.
     */
    private static <T> List<T> carica(String percorso, int colonneAttese, ConvertitoreRiga<T> convertitore)
            throws CaricamentoFileException {

        Path file = Paths.get(percorso);
        List<T> risultato = new ArrayList<>();
        int numeroRiga = 1; // la riga 1 è l'intestazione

        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            br.readLine(); // salta l'intestazione

            String riga;
            while ((riga = br.readLine()) != null) {
                numeroRiga++;

                // salta le righe vuote, anche quelle fatte solo di ";" (tipiche dei file salvati da Excel)
                if (riga.replace(";", "").isBlank()) continue;

                String[] colonne = riga.split(";", -1);
                if (colonne.length < colonneAttese) {
                    throw new CaricamentoFileException(percorso + ", riga " + numeroRiga
                            + ": attese " + colonneAttese + " colonne, trovate " + colonne.length + ".");
                }

                try {
                    risultato.add(convertitore.converti(colonne));
                } catch (NumberFormatException e) {
                    throw new CaricamentoFileException(percorso + ", riga " + numeroRiga
                            + ": valore numerico non valido o mancante.", e);
                } catch (DateTimeParseException e) {
                    throw new CaricamentoFileException(percorso + ", riga " + numeroRiga
                            + ": data non valida ('" + e.getParsedString() + "'), atteso gg/mm/aaaa.", e);
                } catch (IllegalArgumentException e) {
                    throw new CaricamentoFileException(percorso + ", riga " + numeroRiga
                            + ": " + e.getMessage(), e);
                }
            }

        } catch (NoSuchFileException e) {
            throw new CaricamentoFileException("File non trovato: " + percorso
                    + ". Cartella corrente: " + Paths.get("").toAbsolutePath()
                    + ". Copia i tre file CSV in questa cartella oppure avvia il programma"
                    + " dalla cartella che li contiene.", e);
        } catch (IOException e) {
            throw new CaricamentoFileException("Impossibile leggere " + percorso
                    + " (controlla che non sia aperto in un altro programma e che sia salvato in UTF-8).", e);
        }

        return risultato;
    }

    // ---------- conversioni di singoli campi ----------

    /** "2,15 €" -> 2.15 */
    private static double parsePrezzo(String testo) {
        String pulito = testo.replaceAll("[^0-9,.]", "").replace(",", ".");
        return Double.parseDouble(pulito); // se è vuoto lancia NumberFormatException
    }

    /** "SI" -> true, "NO" -> false, qualsiasi altro valore è un errore. */
    private static boolean parseDisponibile(String testo) {
        String valore = testo.trim();
        if (valore.equalsIgnoreCase("SI")) return true;
        if (valore.equalsIgnoreCase("NO")) return false;
        throw new IllegalArgumentException("il campo Disponibile deve essere SI o NO, trovato '" + valore + "'.");
    }
}
