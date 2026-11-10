package exception;

/**
 * Errore causato da un'operazione dell'utente che non si può eseguire
 * (prodotto inesistente, prodotto non disponibile, ID già usato...).
 */
public class OperazioneNonValidaException extends Exception {

    public OperazioneNonValidaException(String messaggio) {
        super(messaggio);
    }
}
