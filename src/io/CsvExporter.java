package io;

import model.Prodotto;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/** Scrive su file CSV i prodotti ricevuti. */
public class CsvExporter {

    /**
     * Crea il file prodotti_gg_mm_aaaa.csv nella cartella corrente.
     * @return il nome del file creato
     */
    public static String esportaProdotti(Collection<Prodotto> prodotti) throws IOException {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        String nomeFile = "prodotti_" + data + ".csv";

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(nomeFile), StandardCharsets.UTF_8))) {
            pw.println("ID;Data inserimento;Nome;Marca;Prezzo");
            for (Prodotto p : prodotti) {
                pw.println(p.getId() + ";" + p.getDataFormattata() + ";" + p.getNome() + ";"
                        + p.getMarca() + ";" + p.getPrezzoFormattato());
            }
        }
        return nomeFile;
    }
}
