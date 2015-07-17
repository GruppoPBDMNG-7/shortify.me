#Analisi dei costi delle operazioni
Ogni operazione effettuata verrà analizzata in termini di letture e scritture dei dati nel database; in base a tale analisi successivamente verrà discusso il database NoSQL da utilizzare, quello cioè che risulta essere il più adatto, in termini di efficienza, al costo delle operazioni. Qui di seguito, per ogni operazione, è riportata un’analisi dei costi:

- ridurre un long url in uno short url richiede la scrittura nel DB di questi due valori;
- visitare uno short url richiede una lettura nel DB per avere il long url, ed una serie di scritture in base alle statistiche da aggiornare;
- visualizzare l’anteprima di uno short url e le statistiche richiede una sola lettura nel DB;
- la creazione di short url personalizzati richiede una lettura nel DB per verificare se tale short url non sia già presente, e una scrittura per la memorizzazione.
