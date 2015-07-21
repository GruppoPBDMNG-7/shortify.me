#Implementazione
###Modello del database
Il database utilizzato nell’applicazione è Cassandra. I dati sono stati organizzati nella seguente maniera:
<pre>
CREATE KEYSPACE urlshortener WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE TABLE urlshortener.urls (
    short_url text PRIMARY KEY,
    long_url text
)

CREATE TABLE urlshortener.countrycounters (
    short_url text,
    country text,
    value counter,
    PRIMARY KEY (short_url, country)
) WITH caching = '{"keys":"ALL", "rows_per_partition":"10"}';

CREATE TABLE urlshortener.hourcounters (
    short_url text,
    hour timestamp,
    value counter,
    PRIMARY KEY (short_url, hour)
) WITH CLUSTERING ORDER BY (hour DESC) 
  AND caching = '{"keys":"ALL", "rows_per_partition":"10"}';

CREATE TABLE urlshortener.daycounters
    short_url text,
    day timestamp,
    value counter,
    PRIMARY KEY (short_url, day)
) WITH CLUSTERING ORDER BY (day DESC) 
  AND caching = '{"keys":"ALL", "rows_per_partition":"10"}';
   
CREATE TABLE urlshortener.uniquecounter (
    short_url text PRIMARY KEY,
    value counter
)
CREATE TABLE urlshortener.uniquecounterips (
    short_url text,
    ip text,
    PRIMARY KEY (short_url, ip)
)
</pre>
Si può osservare dallo schema appena descritto come i dati statistici che si è scelto di rilevare sono: contatore di visite per nazione, contatore di visite per giorno, per ora, e il contatore di visite uniche (dove un visitatore unico è rappresentato da un indirizzo ip), ciascuno di questi contatori è supportato da una propria column family.

La column family countrycounters contiene il numero di visite separato per nazione, mentre daycounters e hourcounters contengono le visite separate rispettivamente per ora (es. le visite fatte tra le 14:00 e le 15:00 del 15/07/2015), e per giorno.

Si può notare inoltre come i contatori siano aggiornati alla visita della pagina, invece che essere calcolati solo al momento della lettura di una coda di visite: questa scelta è stata condizionata da due fattori:
- La possibilità, offerta da Cassandra, e descritta nei paragrafi precedenti, di usare delle colonne counter che permettono un’aggiornamento consistente e con buone prestazioni anche nel caso di dati distribuiti
- La necessità di ridurre il quantitativo di righe che è necessario leggere, viste le prestazioni non eccellenti in lettura di Cassandra. La situazione è ancora più gravosa se consideriamo il numero di visite che potrebbero essere effettuate (che può arrivare nell’ordine di milioni).

Ogni tabella è dotata di una propria chiave primaria: nel caso di countrycounters, hourcounters, daycounters e uniquecountersip, la chiave è divisa in due parti:
 - Una partition key (ovvero short_url)
 - Una clustering key

Per ciascuna column family, tutte le righe che hanno la stessa partition key sono memorizzate nello stesso nodo (partizione), trattandole effettivamente come se fossero una singola riga, dotata di più colonne. (fonte: http://intellidzine.blogspot.it/2014/01/cassandra-data-modelling-primary-keys.html)

###DAO
È stata realizzata un'interfaccia per il Data Access Object, contenente tutti i metodi necessari per realizzare le varie funzionalità del sistema, e una sua implementazione, CassandraDAO.

CassandraDAO comunica con il database attraverso la libreria cassandra-driver-core fornita da Datastax (versione 2.1.5).

Per ottimizzare l'esecuzione delle query, e per evitare problemi relativi a tentativi di injection, sono stati utilizzati oggetti della classe PreparedStatement.

La classe CassandraSchema contiene tutte le costanti utilizzate per la definizione dello schema del database, per i nomi di keyspace, column family, e colonne.

###Statistiche
La classe Statistics è stata utilizzata per impacchettare tutti i dati relativi alle statistiche ottenuti dal database. Contiene i dati relativi al long url, lo short url, e tutti i contatori definiti in precedenza. Si occupa inoltre della serializzazione di queste statistiche in formato JSON, in maniera da poter quindi essere inviate in rete.

###Server
Il server è stato sviluppato utilizzando il framework **Spark java** la cui semplicità ha permesso una rapida costruzione delle componenti server principali. Attraverso l'utilizzo di semplici metodi di cattura delle richieste GET e POST effettuate dal client, si sono forniti diversi servizi cui accesso avviene attraverso una serie di API RESTful. 

La classe Bootstrap è l'entry point del server; la responsabilità principale consiste nel settaggio dei diversi *end points* e delle varie opzioni di configurazione del server.

La cattura delle richieste inviate dal client avviene nella classe Services, che per ognuna di esse esegue i diversi servizi di shortening disponibili. Per seperare le responsabilità della cattura delle richieste, dell'invio delle risposte al client, con eventuali messaggi di errore, dalla responsabilità di eseguire i servizi di shortening, chiamando il DAO per l'accesso al database, si è sviluppata la classe ShortenerServices, cui istanza creata una sola volta in Services contiene il riferimento ad un oggetto che implementa il DAO.

###Algoritmo per l'url shortener
L'algoritmo è stato implementato in modo tale da dover restituire sempre una stringa che corrisponda a 8 caratteri. Questi non sono casuali ma scelti da una funzione di encoding basata sulla base 64. L'encoding è composto dai caratteri : [a-z], [A-Z], [0-9], +, /.
<br>Il punto di partenza dell'algoritmo è il long url. Si prendono 8 caratteri random di esso e poi si fa l'encoding. La scelta di prendere questi caratteri random è risultata necessaria perchè, così facendo, si evitano conflitti tra utenti che vorrebbero creare uno short url partendo dallo stesso long url.

###Geolocalizzazione
La classe CountryIPInformation è stata utilizzata per poter trovare la nazione corrispondente ad un determinato ip. Utilizza un database che memorizza i vari range di ip attribuiti ad ogni nazione. In caso l'ip non corrispondesse a nessuna nazione viene restituita una stringa vuota.Abbiamo ritenuto necessaria questa funzionalità per i fini statistici del sistema.

###Controllo sulle parole
La classe WordChecker è stata utilizzata per fare filtro su parole non idonee al contesto come turpiloqui. Il filtro agisce sui custom url dove l'utente potrebbe inserire questa tipologia di parole. Agisce su 17 tipi di linguaggio la maggior parte europei.

###Controllo sui domini
La classe DomainChecker è stata utilizzata per fare filtro su domini che potrebbero risultare malevoli e utilizzati per fini diversi da quelli che il servizio tende a fornire. Il controllo è basato su una lista che rappresenta domini "high sensibility".