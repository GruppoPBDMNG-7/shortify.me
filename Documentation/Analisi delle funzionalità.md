#Analisi delle funzionalità

Il servizio realizzato permette di:

- ridurre un URL in uno short URL (con controlli sul tipo di URL, evitando l'inserimento di link a siti web pericolosi);
- visualizzare in anteprima l'URL al quale uno short reindirizza;
- creare short URL personalizzati (con controlli sulle parole non ammesse);
- visualizzare statistiche riguardanti uno short URL.

![funzionalità](https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/funziolalit%C3%A0.png)

La funzionalità di ispezione di uno short URL comprende sia la visualizzazione delle statistiche, sia la visualizzazione del long URL. Queste informazioni verranno richieste tutte una sola volta quando l'utente decide di ispezionare un'URL.

##Frequenza delle funzionalità
Ognuna delle funzionalità appena descritte viene eseguita con un certo livello di frequenza, utile a determinare la priorità da assegnare nello studio del modello da implementare.

Si può assumere con buona proabilità che l'operazione eseguita più spesso è quella di visita di uno short URL: nel corso della sua vita un solo short URL potrebbe accumulare milioni di visualizzazioni.

Anche la creazione di un nuovo short URL è una operazione sufficientemente frequente, specialmente quando si devono condividere su social network come Twitter, dove si rivelano di vitale importanza.

Infine le operazioni di anteprima di uno short URL e di visualizzazione delle statistiche sono quelle che ci si aspetta siano eseguite meno frequentemente, almeno dal punto di vista di utenti semplici. Tuttavia la funzionalità di visualizzazione delle statistiche potrebbe invece risultare molto importante per le imprese, che potrebbero decidere di utilizzare il sistema per tenere traccia delle visite effettuate: per questa categoria particolare di utenti potrebbe essere utile scaricare periodicamente i dati statistici.
