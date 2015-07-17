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
