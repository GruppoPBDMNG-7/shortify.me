#Modello dei dati
Per modello dei dati si intende una rappresentazione degli elementi sotto analisi e le relazioni esistenti tra essi. Esistono diversi modelli dei dati utilizzati dai database NoSQL ed ognuno di essi permette differenti metodi di interazione tra applicazione e database. Per il sistema di shortening che si è realizzato si è utilizzato come modello dei dati il **column family**.

I database NoSQL che sono realizzati secondo questo modello memorizzano i dati come un hashmap a due livelli: attraverso una prima chiave, che identifica una riga, si ottiene l'*aggregate*; tale *row aggregate* è a sua volta un hash map e, quindi, attraverso una seconda chiave si ottiene il dato di interesse, che può essere un dato complesso, un oggetto aggregato. La seconda chiave è un identificatore di colonna. È possibile quindi, attraverso opportune operazioni messe a diposizione dal database, ottenere sia una intera riga, sia il valore di una singola colonna o di più colonne. 

La scelta di questo tipo di modello dei dati è stata maggiormente influenzata dalla possibilità di aggiungere ad una riga un numero indefinito di colonne, e che più colonne possono essere raggrupate in famiglie di colonne: ciò ha permesso la creazione di **wide row** che per ogni short url memorizzato sono state utilizzate per la memorizzazione delle statistiche su un URL.

Nel database, quindi, ogni riga è uno short URL; una volta che ne viene richiesto uno al database, si seleziona la riga corrispondente e si ottiene il corrispondente long URL filtrando sulla colonna che lo contiene. Ad ogni riga sono associate, oltre alla colonna del long URL, anche delle *column family* usate per la memorizzazione dei dati sulle statistiche di un URL, come il numero di click assoluto e il numero di visite per Paese; questi dati sono stati divisi nelle seguenti *column family*: 

- una column family contenente tutti i contatori per ora, una colonna per ora;
- una column family contenente i contatori per giorno, con una colonna per giorno;
- una column family contenente i contatori per nazione, ogni colonna contiene il numero di visite effettuate dalla nazione corrispondente;
- una column family contenente un contatore delle visite uniche, ovvero il numero di visite effettuate da utenti distinti;
- una column family contenente una lista di ip che hanno visitato l'url, a supporto del contatore delle visite uniche. Ogni colonna contiene un ip.

**Nota**: un gran numero di column family potrebbe minare le performance del db come spiegato [qui](http://hbase.apache.org/0.94/book/number.of.cfs.html)
