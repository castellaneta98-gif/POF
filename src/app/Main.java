package app;

import exception.CaricamentoFileException;
import exception.OperazioneNonValidaException;
import io.CsvExporter;
import io.CsvLoader;
import model.Prodotto;
import model.Utente;
import model.Vendita;
import service.Gestionale;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/** Interfaccia da riga di comando: mostra il menu e legge l'input. La logica sta in Gestionale. */
public class Main {

    public static void main(String[] args) {
        Gestionale g = new Gestionale();
        try {
            g.carica("utenti.csv", "prodotti.csv", "vendite.csv");
            System.out.println("File caricati correttamente.");
        } catch (CaricamentoFileException e) {
            System.out.println("Errore nel caricamento dei file: " + e.getMessage());
            System.exit(1);
        }

        Scanner in = new Scanner(System.in);
        int scelta = -1;

        while (scelta != 0) {
            stampaMenu();
            scelta = leggiIntero(in, "Scelta: ");

            try {
                switch (scelta) {
                    case 1 -> stampaProdotti(g);
                    case 2 -> {
                        int idProdotto = leggiIntero(in, "ID prodotto: ");
                        int idUtente = leggiIntero(in, "ID utente: ");
                        Vendita v = g.compra(idProdotto, idUtente);
                        System.out.println("Acquisto completato. ID vendita: " + v.getId());
                    }
                    case 3 -> {
                        int idVendita = leggiIntero(in, "ID vendita: ");
                        g.restituisci(idVendita);
                        System.out.println("Prodotto restituito.");
                    }
                    case 4 -> {
                        int id = leggiIntero(in, "ID: ");
                        String nome = leggiTesto(in, "Nome: ");
                        String cognome = leggiTesto(in, "Cognome: ");
                        LocalDate nascita = leggiData(in, "Data di nascita (gg/mm/aaaa): ");
                        String indirizzo = leggiTesto(in, "Indirizzo: ");
                        String documento = leggiTesto(in, "Documento ID: ");
                        g.aggiungiUtente(new Utente(id, nome, cognome, nascita, indirizzo, documento));
                        System.out.println("Utente aggiunto.");
                    }
                    case 5 -> {
                        String nomeFile = CsvExporter.esportaProdotti(g.getProdottiDisponibili());
                        System.out.println("File creato: " + nomeFile);
                    }
                    case 0 -> System.out.println("Arrivederci!");
                    default -> System.out.println("Comando non valido.");
                }
            } catch (OperazioneNonValidaException e) {
                System.out.println("Errore: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Errore nella scrittura del file: " + e.getMessage());
            }
        }
    }

    private static void stampaMenu() {
        System.out.println("\n===== MENU =====");
        System.out.println("1 - Visualizza tutti i prodotti");
        System.out.println("2 - Compra un prodotto");
        System.out.println("3 - Restituisci un prodotto");
        System.out.println("4 - Aggiungi un nuovo utente");
        System.out.println("5 - Esporta i prodotti disponibili");
        System.out.println("0 - Esci");
    }

    private static void stampaProdotti(Gestionale g) {
        System.out.println(Prodotto.intestazione());
        for (Prodotto p : g.getProdotti()) {
            System.out.println(p);
        }
    }

    // Continua a chiedere finché l'utente non scrive un numero intero valido
    private static int leggiIntero(Scanner in, String messaggio) {
        while (true) {
            System.out.print(messaggio);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Inserisci un numero intero valido.");
            }
        }
    }

    // Continua a chiedere finché il testo non è vuoto (e non contiene ";", che romperebbe il CSV)
    private static String leggiTesto(Scanner in, String messaggio) {
        while (true) {
            System.out.print(messaggio);
            String t = in.nextLine().trim();
            if (t.isEmpty()) {
                System.out.println("Il campo non può essere vuoto.");
            } else if (t.contains(";")) {
                System.out.println("Il carattere ';' non è consentito.");
            } else {
                return t;
            }
        }
    }

    private static LocalDate leggiData(Scanner in, String messaggio) {
        while (true) {
            System.out.print(messaggio);
            try {
                LocalDate d = LocalDate.parse(in.nextLine().trim(), CsvLoader.FORMATO_DATA);
                if (d.isAfter(LocalDate.now())) {
                    System.out.println("La data non può essere nel futuro.");
                    continue;
                }
                return d;
            } catch (DateTimeParseException e) {
                System.out.println("Data non valida. Usa il formato gg/mm/aaaa.");
            }
        }
    }
}
