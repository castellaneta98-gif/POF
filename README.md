# Gestionale Java da riga di comando

Applicazione Java **da terminale** per gestire utenti, prodotti e vendite. All'avvio carica in memoria tre file CSV e permette di visualizzare i prodotti, acquistarli, restituirli, aggiungere utenti ed esportare i prodotti ancora disponibili.

Progetto realizzato come esercizio del corso Java di start2impact.

---

## Indice

- [Funzionalità](#funzionalità)
- [Requisiti](#requisiti)
- [Come compilare ed eseguire](#come-compilare-ed-eseguire)
- [Menu dei comandi](#menu-dei-comandi)
- [Formato dei file CSV](#formato-dei-file-csv)
- [Struttura del progetto](#struttura-del-progetto)
- [Gestione degli errori](#gestione-degli-errori)
- [Scelte di progetto](#scelte-di-progetto)
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

- **JDK 17 o superiore** per compilare il progetto (serve il comando `javac`).
- Per eseguire solo il file JAR basta Java 17 o superiore.
- Per verificare la versione installata:

```bash
java -version
javac -version
```

---

## Come compilare ed eseguire

> **Importante:** i tre file CSV (`utenti.csv`, `prodotti.csv`, `vendite.csv`) devono trovarsi **nella cartella da cui si avvia il programma**, perché vengono cercati con percorso relativo. Nel repository sono già presenti nella cartella principale: avvia i comandi da lì.

### 1. Scaricare il progetto

```bash
git clone https://github.com/castellaneta98-gif/POF.git
cd POF
```

In alternativa, scarica lo ZIP dalla pagina del repository (pulsante **Code > Download ZIP**), estrailo e apri un terminale nella cartella estratta.

### 2. Compilare

Dalla cartella principale del progetto (quella che contiene `src` e i file CSV):

```bash
javac -d out -sourcepath src src/app/Main.java
```

Il comando compila `Main` e tutte le classi di cui ha bisogno e crea i file compilati nella cartella `out`. Funziona allo stesso modo su Windows (PowerShell o Prompt dei comandi), macOS e Linux.

### 3. Eseguire

Sempre dalla cartella principale del progetto:

```bash
java -cp out app.Main
```

Deve comparire il messaggio `File caricati correttamente.` seguito dal menu.

### Alternativa: esecuzione da IntelliJ IDEA

1. Apri la cartella del progetto con *File > Open*.
2. Imposta un JDK 17 o superiore in *File > Project Structure > Project*.
3. Verifica che `src` sia contrassegnata come *Sources Root* (cartella blu).
4. Apri `src/app/Main.java` e premi il triangolo verde accanto al metodo `main`.

I file CSV devono stare nella cartella principale del progetto (non dentro `src`).

### Alternativa: esecuzione dal file JAR

Il file `POF.jar` è fornito nel pacchetto di consegna (non è incluso nel repository). Per usarlo:

1. Metti `POF.jar` e i tre file CSV nella **stessa cartella**.
2. Apri un terminale in quella cartella.
3. Avvia:

```bash
java -jar POF.jar
```

> Il JAR va lanciato dal terminale e non con un doppio clic: essendo un programma da riga di comando, la finestra si chiuderebbe subito.

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

Stampa per ogni prodotto: ID, data di inserimento, nome, marca, prezzo e disponibilità. Esempio di output con i dati forniti:

```
ID  | Data ins.  | Nome                 | Marca        |     Prezzo | Disponibile
1   | 22/07/2021 | Latte di mandorla    | Bjorg        |     2,15 € | SI
2   | 01/06/2021 | Lenticchie           | Probios      |    11,41 € | SI
3   | 02/03/2021 | Ceci                 | Valfrutta    |     1,69 € | NO
4   | 31/12/2020 | Farina di cocco      | Bioasia      |     9,99 € | SI
5   | 23/04/2021 | Piselli              | Findus       |     4,39 € | NO
```

### 2 - Compra un prodotto

Chiede l'ID del prodotto e l'ID dell'utente. Se i dati sono validi:

- viene creata una nuova vendita con ID pari al massimo ID esistente + 1;
- il campo *Disponibile* del prodotto passa da `SI` a `NO`;
- il programma mostra l'ID della vendita creata.

Esempio:

```
Scelta: 2
ID prodotto: 1
ID utente: 2
Acquisto completato. ID vendita: 3
```

### 3 - Restituisci un prodotto

Chiede l'ID della vendita. Il prodotto collegato torna disponibile (`SI`) e la vendita viene eliminata dall'elenco.

### 4 - Aggiungi un nuovo utente

Chiede in sequenza ID, nome, cognome, data di nascita (formato `gg/mm/aaaa`), indirizzo e documento ID, quindi aggiunge l'utente all'elenco in memoria.

### 5 - Esporta i prodotti disponibili

Crea nella cartella da cui è avviato il programma un file `prodotti_gg_mm_aaaa.csv`, ad esempio `prodotti_09_11_2026.csv`, contenente solo i prodotti con disponibilità `SI`:

```
ID;Data inserimento;Nome;Marca;Prezzo
1;22/07/2021;Latte di mandorla;Bjorg;2,15 €
2;01/06/2021;Lenticchie;Probios;11,41 €
4;31/12/2020;Farina di cocco;Bioasia;9,99 €
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
│   ├── app/
│   │   └── Main.java                          # menu, lettura e controllo dell'input
│   ├── service/
│   │   └── Gestionale.java                    # dati in memoria e logica dei comandi
│   ├── model/
│   │   ├── Utente.java                        # classe corrispondente a utenti.csv
│   │   ├── Prodotto.java                      # classe corrispondente a prodotti.csv
│   │   └── Vendita.java                       # classe corrispondente a vendite.csv
│   ├── io/
│   │   ├── CsvLoader.java                     # lettura dei CSV e conversione in oggetti
│   │   └── CsvExporter.java                   # scrittura del CSV dei prodotti disponibili
│   └── exception/
│       ├── CaricamentoFileException.java      # errori di lettura/validazione dei CSV
│       └── OperazioneNonValidaException.java  # errori delle operazioni richieste dall'utente
├── utenti.csv
├── prodotti.csv
├── vendite.csv
├── .gitignore
└── README.md
```

| Package | Responsabilità |
|---|---|
| `app` | Interfaccia da riga di comando: mostra il menu, legge l'input da tastiera e lo valida. Non contiene logica di business |
| `service` | `Gestionale` mantiene i dati in memoria e implementa acquisto, restituzione, aggiunta utente e consultazione |
| `model` | Classi del dominio (`Utente`, `Prodotto`, `Vendita`): campi privati, costruttore, getter e `toString` |
| `io` | Lettura dei file CSV (`CsvLoader`) e scrittura del file esportato (`CsvExporter`) |
| `exception` | Eccezioni personalizzate |

---

## Gestione degli errori

### Input dell'utente

- I campi numerici vengono richiesti di nuovo finché non viene inserito un numero intero valido.
- **Acquisto:** il prodotto e l'utente devono esistere e il prodotto deve essere disponibile.
- **Restituzione:** la vendita deve esistere.
- **Nuovo utente:** l'ID non deve essere già in uso; nome, cognome, indirizzo e documento non possono essere vuoti né contenere il carattere `;` (romperebbe il formato CSV); la data deve essere valida e non nel futuro.
- In caso di errore viene mostrato un messaggio e il programma torna al menu.

### Caricamento dei file

Se un file non può essere caricato, il programma mostra un messaggio che indica il file e, quando possibile, la riga con il problema, e termina con codice di errore 1. Casi gestiti:

- file mancante (il messaggio indica anche la cartella da cui è stato avviato il programma);
- numero di colonne errato;
- valore numerico non valido o mancante;
- data non valida;
- campo `Disponibile` diverso da `SI` o `NO`;
- ID duplicato nello stesso file;
- vendita che fa riferimento a un prodotto o a un utente inesistente.

Le righe vuote nei CSV (anche quelle composte solo da `;`, tipiche dei file salvati da Excel) vengono ignorate.

---

## Scelte di progetto

- **Strutture dati:** utenti, prodotti e vendite sono memorizzati in `Map<Integer, ...>` con l'ID come chiave (`LinkedHashMap`, per mantenere l'ordine di inserimento del file). Questo rende immediata la ricerca per ID e permette di individuare subito gli ID duplicati.
- **Separazione delle responsabilità:** l'interfaccia (`app`), la logica (`service`), i dati (`model`) e l'accesso ai file (`io`) sono in package separati.
- **Persistenza:** le modifiche (acquisti, restituzioni, nuovi utenti) restano **solo in memoria** e vengono perse alla chiusura. I CSV originali non vengono mai modificati. L'unico file scritto dal programma è quello creato dal comando 5.
- **Date:** vengono lette con il formato `d/M/yyyy` (quindi anche `23/4/2021`) e mostrate come `gg/mm/aaaa`.
- **Prezzi:** nei file compaiono come `2,15 €`; nel programma sono memorizzati come numeri decimali e mostrati con due cifre decimali e il simbolo `€`.
- **ID vendita:** assegnato automaticamente come massimo ID esistente + 1.
- **ID utente:** inserito manualmente dall'utente e controllato per evitare duplicati.

---

## Autore

Giuseppe Castellaneta - [github.com/castellaneta98-gif](https://github.com/castellaneta98-gif)




