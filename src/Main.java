import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Gestionale g = new Gestionale();
        try {
            g.carica("utenti.csv", "prodotti.csv", "vendite.csv");
            System.out.println("File caricati correttamente.");
        } catch (Exception e) {
            System.out.println("Errore nel caricamento dei file: " + e.getMessage());
            return;
        }

        Scanner in = new Scanner(System.in);
        int scelta = -1;

        while (scelta != 0) {
            stampaMenu();
            scelta = leggiIntero(in, "Scelta: ");

            try {
                switch (scelta) {
                    case 1 -> g.stampaProdotti();
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
                    case 5 -> System.out.println("File creato: " + g.esportaDisponibili());
                    case 0 -> System.out.println("Arrivederci!");
                    default -> System.out.println("Comando non valido.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Errore imprevisto: " + e.getMessage());
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