package service;

import exception.CaricamentoFileException;
import exception.OperazioneNonValidaException;
import io.CsvLoader;
import model.Prodotto;
import model.Utente;
import model.Vendita;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Tiene i dati in memoria e contiene la logica delle operazioni.
 * Ogni collezione è una Map con l'ID come chiave: la ricerca per ID è immediata
 * e un ID duplicato si individua subito. LinkedHashMap mantiene l'ordine di inserimento.
 */
public class Gestionale {
    private Map<Integer, Utente> utenti = new LinkedHashMap<>();
    private Map<Integer, Prodotto> prodotti = new LinkedHashMap<>();
    private Map<Integer, Vendita> vendite = new LinkedHashMap<>();

    // ---------- caricamento ----------

    public void carica(String fileUtenti, String fileProdotti, String fileVendite)
            throws CaricamentoFileException {

        Map<Integer, Utente> nuoviUtenti =
                indicizzaPerId(CsvLoader.caricaUtenti(fileUtenti), Utente::getId, fileUtenti, "Utente");
        Map<Integer, Prodotto> nuoviProdotti =
                indicizzaPerId(CsvLoader.caricaProdotti(fileProdotti), Prodotto::getId, fileProdotti, "Prodotto");
        Map<Integer, Vendita> nuoveVendite =
                indicizzaPerId(CsvLoader.caricaVendite(fileVendite), Vendita::getId, fileVendite, "Vendita");

        // Ogni vendita deve riferirsi a un prodotto e a un utente esistenti
        for (Vendita v : nuoveVendite.values()) {
            if (!nuoviProdotti.containsKey(v.getIdProdotto())) {
                throw new CaricamentoFileException(fileVendite + ": la vendita " + v.getId()
                        + " fa riferimento a un prodotto inesistente (ID " + v.getIdProdotto() + ").");
            }
            if (!nuoviUtenti.containsKey(v.getIdUtente())) {
                throw new CaricamentoFileException(fileVendite + ": la vendita " + v.getId()
                        + " fa riferimento a un utente inesistente (ID " + v.getIdUtente() + ").");
            }
        }

        // Si sostituiscono i dati solo se tutti i controlli sono andati a buon fine
        utenti = nuoviUtenti;
        prodotti = nuoviProdotti;
        vendite = nuoveVendite;
    }

    /** Mette gli elementi in una Map con l'ID come chiave; se trova un ID doppio lancia un errore. */
    private static <T> Map<Integer, T> indicizzaPerId(List<T> elementi, Function<T, Integer> estraiId,
                                                      String file, String tipo)
            throws CaricamentoFileException {
        Map<Integer, T> mappa = new LinkedHashMap<>();
        for (T elemento : elementi) {
            int id = estraiId.apply(elemento);
            if (mappa.containsKey(id)) {
                throw new CaricamentoFileException(file + ": " + tipo + " con ID " + id + " duplicato.");
            }
            mappa.put(id, elemento);
        }
        return mappa;
    }

    // ---------- consultazione ----------

    public Collection<Prodotto> getProdotti() {
        return Collections.unmodifiableCollection(prodotti.values());
    }

    public List<Prodotto> getProdottiDisponibili() {
        List<Prodotto> disponibili = new ArrayList<>();
        for (Prodotto p : prodotti.values()) {
            if (p.isDisponibile()) {
                disponibili.add(p);
            }
        }
        return disponibili;
    }

    // ---------- operazioni ----------

    public Vendita compra(int idProdotto, int idUtente) throws OperazioneNonValidaException {
        Prodotto p = prodotti.get(idProdotto);
        if (p == null) {
            throw new OperazioneNonValidaException("Prodotto " + idProdotto + " inesistente.");
        }
        if (!utenti.containsKey(idUtente)) {
            throw new OperazioneNonValidaException("Utente " + idUtente + " inesistente.");
        }
        if (!p.isDisponibile()) {
            throw new OperazioneNonValidaException("Il prodotto non è disponibile.");
        }

        Vendita v = new Vendita(prossimoIdVendita(), idProdotto, idUtente);
        vendite.put(v.getId(), v);
        p.setDisponibile(false);
        return v;
    }

    public void restituisci(int idVendita) throws OperazioneNonValidaException {
        Vendita v = vendite.remove(idVendita);
        if (v == null) {
            throw new OperazioneNonValidaException("Vendita " + idVendita + " inesistente.");
        }
        Prodotto p = prodotti.get(v.getIdProdotto());
        if (p != null) {
            p.setDisponibile(true);
        }
    }

    public void aggiungiUtente(Utente nuovo) throws OperazioneNonValidaException {
        if (utenti.containsKey(nuovo.getId())) {
            throw new OperazioneNonValidaException("Esiste già un utente con ID " + nuovo.getId() + ".");
        }
        utenti.put(nuovo.getId(), nuovo);
    }

    private int prossimoIdVendita() {
        int max = 0;
        for (int id : vendite.keySet()) {
            if (id > max) {
                max = id;
            }
        }
        return max + 1;
    }
}
