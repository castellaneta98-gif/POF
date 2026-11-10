package exception;

/**
 * Errore avvenuto durante la lettura o la validazione dei file CSV.
 * Il messaggio indica sempre quale file ha il problema e, quando possibile, anche la riga.
 */
public class CaricamentoFileException extends Exception {

    public CaricamentoFileException(String messaggio) {
        super(messaggio);
    }

    public CaricamentoFileException(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }
}
