# Gestionale Java da riga di comando

Applicazione Java **da terminale** per gestire utenti, prodotti e vendite. All'avvio carica in memoria tre file CSV e permette di visualizzare i prodotti, acquistarli, restituirli, aggiungere utenti ed esportare i prodotti ancora disponibili.

Progetto realizzato come esercizio del corso Java di start2impact.

---

## Indice

- [Funzionalità](#funzionalità)
- [Requisiti](#requisiti)
- [Come eseguire il progetto](#come-eseguire-il-progetto)
- [Menu dei comandi](#menu-dei-comandi)
- [Formato dei file CSV](#formato-dei-file-csv)
- [Struttura del progetto](#struttura-del-progetto)
- [Controlli sull'input](#controlli-sullinput)
- [Note e scelte di progetto](#note-e-scelte-di-progetto)
- [Autore](#autore)

---

## Funzionalità

- **Caricamento automatico** dei file `utenti.csv`, `prodotti.csv` e `vendite.csv` allo startup (separatore `;`).
- **Visualizzazione** di tutti i prodotti presenti nel sistema.
- **Acquisto** di un prodotto da parte di un utente: viene creata una nuova vendita con ID assegnato dal programma e il prodotto passa da disponibile (`SI`) a non disponibile (`NO`).
- **Restituzione** di un prodotto acquistato: la vendita viene rimossa e il prodotto torna disponibile.
- **Aggiunta** di un nuovo utente all'elenco.
- **Esportazione** in un file CSV dei soli prodotti ancora disponibili, con la data corrente nel nome del file.
- Menu sempre visibile fino a quando l'utente sceglie di uscire.

---

## Requisiti

- **Java 17 o superiore** (JDK per sviluppare, JRE/JDK per eseguire il solo JAR).
- Per verificare la versione installata: `java -version`

---

## Come eseguire il progetto

> I tre file CSV devono trovarsi **nella cartella da cui si avvia il programma**, perché vengono cercati con percorso relativo (`utenti.csv`, `prodotti.csv`, `vendite.csv`).

### Opzione 1: da IntelliJ IDEA

1. Clona il repository o scarica lo ZIP da GitHub e apri la cartella come progetto in IntelliJ.
2. Imposta un JDK 17+ in *File > Project Structure > Project*.
3. Verifica che `utenti.csv`, `prodotti.csv` e `vendite.csv` siano nella cartella principale del progetto (non dentro `src`).
4. Apri `src/Main.java` e premi il triangolo verde accanto a `main`.

### Opzione 2: dal file JAR

Se il progetto contiene un JAR (ad esempio nella sezione *Releases* del repository):

1. Metti `../../Downloads/POF.jar` e i tre file CSV nella stessa cartella.
2. Apri un terminale in quella cartella.
3. Avvia:

```bash
java -jar POF.jar
```

### Opzione 3: da terminale, compilando i sorgenti

Dalla cartella principale del progetto:

```bash
javac -d out src/*.java
java -cp out Main
```

---

## Menu dei comandi

All'avvio, dopo il messaggio `File caricati correttamente.`, compare il menu:

```
===== MENU =====
1 - Visualizza tutti i prodotti
2 - Compra un prodotto
3 - Restituisci un prodotto
4 - Aggiungi un nuovo utente
5 - Esporta i prodotti disponibili
0 - Esci
Scelta:
```

Dopo ogni operazione il menu viene mostrato di nuovo, fino all'inserimento di `0`.

| Comando | Operazione | Dati richiesti |
|---|---|---|
| `1` | Mostra tutti i prodotti | nessuno |
| `2` | Acquista un prodotto | ID prodotto, ID utente |
| `3` | Restituisce un prodotto acquistato | ID vendita |
| `4` | Aggiunge un utente | ID, Nome, Cognome, Data di nascita, Indirizzo, Documento ID |
| `5` | Esporta i prodotti disponibili | nessuno |
| `0` | Esce dal programma | nessuno |

### 1 - Visualizza tutti i prodotti

Stampa per ogni prodotto: ID, data di inserimento, nome, marca, prezzo e disponibilità (esempio di output):

```
ID  | Inserimento | Nome            | Marca      |    Prezzo | Disponibile
1   | 22/07/2021 | Latte di mandorla | Bjorg      |    2,15 € | SI
2   | 01/06/2021 | Lenticchie      | Probios    |   11,41 € | SI
3   | 02/03/2021 | Ceci            | Valfrutta  |    1,69 € | NO
4   | 31/12/2020 | Farina di cocco | Bioasia    |    9,99 € | SI
5   | 23/04/2021 | Piselli         | Findus     |    4,39 € | NO
```

### 2 - Compra un prodotto

Chiede l'ID del prodotto e l'ID dell'utente. Se i dati sono validi:

- viene creata una nuova vendita con ID pari al massimo ID esistente + 1;
- il campo *Disponibile* del prodotto passa da `SI` a `NO`;
- il programma mostra l'ID della vendita creata.

### 3 - Restituisci un prodotto

Chiede l'ID della vendita. Il prodotto collegato torna disponibile (`SI`) e la vendita viene eliminata dall'elenco.

### 4 - Aggiungi un nuovo utente

Chiede in sequenza ID, nome, cognome, data di nascita (formato `gg/mm/aaaa`), indirizzo e documento ID, quindi aggiunge l'utente all'elenco in memoria.

### 5 - Esporta i prodotti disponibili

Crea nella cartella da cui è avviato il programma un file `prodotti_gg_mm_aaaa.csv`, ad esempio `prodotti_04_11_2026.csv`, contenente solo i prodotti con disponibilità `SI`:

```
ID;Data inserimento;Nome;Marca;Prezzo
1;22/07/2021;Latte di mandorla;Bjorg;2,15 €
2;01/06/2021;Lenticchie;Probios;11,41 €
```

---

## Formato dei file CSV

Tutti i file usano il separatore `;`, hanno una riga di intestazione e codifica UTF-8.

**utenti.csv**

```
ID;Nome;Cognome;Data di nascita;Indirizzo;Documento ID
1;Mario;Rossi;23/02/1990;Via Roma 15, Torino;AS 348945 0
```

**prodotti.csv** (ordine reale delle colonne nel file)

```
ID;Nome;Data di inserimento;Prezzo;Marca;Disponibile
1;Latte di mandorla;22/07/2021;2,15 €;Bjorg;SI
```

**vendite.csv**

```
ID;ID Prodotto;ID Utente
1;3;1
```

Collegamenti tra i file:

- `vendite.csv → ID Utente` corrisponde a `utenti.csv → ID`
- `vendite.csv → ID Prodotto` corrisponde a `prodotti.csv → ID`

> Nella visualizzazione e nell'esportazione i campi del prodotto sono mostrati nell'ordine richiesto dalla consegna (ID, Data inserimento, Nome, Marca, Prezzo), che è diverso dall'ordine delle colonne nel file `prodotti.csv`.

---

## Struttura del progetto

```
.
├── src/
│   ├── Main.java          # menu, lettura e controllo dell'input
│   ├── Gestionale.java    # dati in memoria e logica dei comandi
│   ├── CsvLoader.java     # lettura dei CSV e conversione in oggetti
│   ├── Utente.java        # classe corrispondente a utenti.csv
│   ├── Prodotto.java      # classe corrispondente a prodotti.csv
│   └── Vendita.java       # classe corrispondente a vendite.csv
├── utenti.csv
├── prodotti.csv
├── vendite.csv
├── .gitignore
└── README.md
```

| Classe | Responsabilità |
|---|---|
| `Main` | Mostra il menu, legge i comandi da tastiera e ripete la richiesta se l'input non è valido |
| `Gestionale` | Contiene le liste di utenti, prodotti e vendite e implementa acquisto, restituzione, aggiunta utente ed esportazione |
| `CsvLoader` | Legge i file, salta intestazione e righe vuote, converte date e prezzi |
| `Utente`, `Prodotto`, `Vendita` | Classi modello con campi privati, costruttore, getter e `toString` |

---

## Controlli sull'input

- I campi numerici vengono richiesti di nuovo finché non viene inserito un numero intero valido.
- **Acquisto:** il prodotto e l'utente devono esistere e il prodotto deve essere disponibile.
- **Restituzione:** la vendita deve esistere.
- **Nuovo utente:** l'ID non deve essere già in uso; nome, cognome, indirizzo e documento non possono essere vuoti né contenere il carattere `;` (romperebbe il formato CSV); la data deve essere valida e non nel futuro.
- Le righe vuote nei CSV (anche quelle composte solo da `;`) vengono ignorate in fase di caricamento.
- In caso di errore viene mostrato un messaggio e il programma torna al menu.

---

## Note e scelte di progetto

- **Persistenza:** le modifiche (acquisti, restituzioni, nuovi utenti) restano **solo in memoria** e vengono perse alla chiusura. I CSV originali non vengono mai modificati. L'unico file scritto dal programma è quello creato dal comando 5.
- **Date:** vengono lette con il formato `d/M/yyyy` (quindi anche `23/4/2021`) e mostrate come `gg/mm/aaaa`.
- **Prezzi:** nei file compaiono come `2,15 €`; nel programma sono memorizzati come numeri decimali e mostrati con due cifre decimali e il simbolo `€`.
- **ID vendita:** assegnato automaticamente come massimo ID esistente + 1.
- **ID utente:** inserito manualmente dall'utente e controllato per evitare duplicati.




