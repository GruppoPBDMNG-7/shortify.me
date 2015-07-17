#Scelta del database NoSQL
Il caso di studio prevede la realizzazione di un url shortener. Questo particolare sistema permette di ridurre la grandezza di un url e di distrubuirla a terzi per usufruirne. Il servizio reindirizzerà a l’url principale. Ciò che si dovrebbe fornire oltre alla riduzione dell’url, sono delle statistiche sugli accessi che vengono fatti in finestre temporali.
Questo prevede scritture che sono:


- Creazione nuovo short url;
- Aggiornamento dei dati per l’analisi statistica ogni volta che si utilizza uno short url;

e delle letture che sono:


- Creazione nuovo short url;
- Lettura del long url;
- Lettura dei dati per l’analisi statistica.

Il caso della Creazione del nuovo short url richiede sia una lettura che una scrittura perchè bisogna verificare prima se esiste già e poi scriverlo.

Per il caso di studio sarà necessario avere **un** **servizio** **sempre** **fruibile** da parte dell’utente e che permetta di accedere sempre ai dati. In oltre deve essere in grado di **gestire** adeguatamente **l’overload** del sistema o essere adeguato a un grande flusso di dati già nativamente.

Le considerazioni verranno fatte sul **CAP THEOREM** che prevede una classificazione rispetto alle 3 importanti **caratteristiche** di un database nosql : **consistenza**, **disponibilità** e **tolleranza** **per la partizione**. <br>Per definizione non è possibile avere un database che abbia tutte e 3 le caratteristiche ma al più 2. Per le considerazioni fatte in precedenza **le** **caratteristiche** **scelte** **sono** la **disponibilità** che garantisce un servizio sempre utilizzabile **e** la **tolleranza** **della partizione** che permette di utilizzare sempre il servizio anche se la partizione non comunica con le altre.

Ne risulta quindi che la consistenza è messa in secondo piano rispetto alle altre due proprietà, questo non vuol dire però che non sia assolutamente da prendere in considerazione. Piuttosto, considerato che i dati che verranno modificati più di frequente nel corso del tempo sono i contatori per le statistiche, si può affermare che per questi dati una consistenza immediata non è strettamente necessaria, e perciò si ritiene sufficiente una consistenza di tipo eventuale.


##Cassandra
La scelta di **cassandra** è una scelta solida grazie alla definizione **AP** dello stesso (Cap Theorem). La struttura di cassandra è formata da un anello (ring) di nodi che rappresentano ognuno una partizione diversa dello spazio. Ogni dato viene attribuito a un singolo nodo che viene replicato successivamente in altri nodi. E’ possibile creare anche dei nodi virtuali che permettono di distribuire meglio lo spazio dell’anello sui vari nodi. Cassandra ha come caratteristica principale il non avere un singolo Point-Of-Failure e avere quindi una ottima gestione in caso di node failure. La disponibilità fa in modo di avere sempre accesso al cluster nonostante un nodo fallisca. La **tolleranza della partizione** fa in modo di poter utilizzare i nodi anche se non possono comunicare tra di loro.<br>
Nel nostro specifico caso questo garantisce al cliente un servizio sempre fruibile e con rapidità. Rispetto ad altri database NoSQL la **scalabilità** di cassandra è **ottima** al crescere dei nodi.

<pre>
<img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/scalabilit%C3%A0.png" alt="Scalabilità"/>
</pre>
Le nostre analisi statistiche possono essere fatte grazie all’uso dei contatori in cassandra che analizzeremo in dettaglio.

###Uso contatori
Un difficoltà nell’uso dei contatori risiede nel sincronizzarli tra loro. Se arrivano due incrementi di un contatore c su due nodi, contemporaneamente, il vector clock non permette di decidere come agire. (c’è un errore nell’immagini l’incremento è per uno)
<pre>
<img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/count1.png" alt="Contatori#1"/><img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/count2.png" alt="Contatori#2"/>
</pre>
Questa situazione spiacevole è risolta da Cassandra 2.0 partizionando il contatore c in più parti attribuibile ciascuna ad un nodo singolo.
Quindi le varie parti vengono sincronizzate singolarmente, senza doverlo fare su tutte le parti del contatore contemporaneamente.
<pre>
<img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/count3.png" alt="Contatori#3"/><img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/count4.png" alt="Contatori#4"/>
</pre>
Fonte: [Distributed Counters in Cassandra
](http://www.datastax.com/wp-content/uploads/2011/07/cassandra_sf_counters.pdf)

Nonostante il miglioramento anche in cassandra 2.0 utilizzare bene i counter senza aver problemi era davvero difficile.<br> Dalla versione 2.1 di cassandra si è implementato un utilizzo diverso. I **counter** non venivano semplicemente aggiornati su un nodo qualsiasi ma ora viene fatto un **mutexed read-before-write**. Questo permette di non dover sommare i counter prima di fare un operazione e di avere tanti shard quanti i nodi che contengono il singolo counter. Per aggiornare un counter basta trovare il nodo con la versione più recente e sommare quello. Questo decremento di prestazione dovuto alla lettura viene ovviato con l’introduzione di una nuova cache esclusiva per i counters.<br>
Fonte: [Cassandra 2.1 : Better Implementation of Counters](http://www.datastax.com/dev/blog/whats-new-in-cassandra-2-1-a-better-implementation-of-counters "Cassandra 2.1 : Better Implementation of Counters")

Grazie anche a questo aggiornamento possiamo pensare di utilizzare Cassandra come database per il nostro caso di studio che riguarda uno shortener url. Infatti il caso prevede l’utilizzo di parecchi contatori che serviranno per fare analisi statica dei dati per quanto riguarda, per esempio, accessi giornalieri. 

###Prestazioni
Sulla base delle operazioni che saranno realizzate dall’url shortener, si effettua un confronto tra diverse tipologie di database NoSQL, per osservarne le prestazioni a fronte di diversi carichi di lavoro. In particolare, le tecnologie prese in considerazione in questo test sono Cassandra, HBase, e MongoDB.

####Condizione 1 - Scrittura dei dati
Questo test mira a valutare le prestazioni nel caso di un carico composto da sole scritture. Può essere paragonato ad un carico di inserimenti di nuovi url.
<pre>
<img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/scrittura1.png" alt="Scrittura#1"/>
</pre>
Si può osservare come in questo caso Cassandra sia la soluzione migliore, che permette inserimenti con un alto numero di operazioni al secondo, e soprattutto, una latenza molto bassa. HBase raggiunge dei risultati simili ma con una latenza costantemente maggiore, mentre MongoDB è quello che generalmente presenta le prestazioni meno elevate.

####Condizione 2 - Lettura e aggiornamento
Questo test sottopone i tre database ad un carico composto per metà da operazioni di lettura semplice, e per l’altra metà da operazioni complesse di lettura e aggiornamento di valori. Di particolare interesse nel nostro caso è la seconda categoria di operazioni, che può approssimare il carico derivante dalla visita di uno short url (composta da una lettura del record, e l’aggiornamento dei vari contatori).
<pre>
<img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/scrittura2.png" alt="Scrittura#2"/><img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/scrittura3.png" alt="Scrittura#3"/>
</pre>
Si riportano separatamente i risultati ottenuti per le operazioni di lettura e di aggiornamento. Si può osservare quindi come nel caso della lettura dei dati, sia Cassandra che HBase riportano risultati migliori sia per livello massimo di throughput, che per latenza, lasciando MongoDB all’ultimo posto. La situazione si ripete anche nel caso dell’aggiornamento, nel quale diventa ancora più marcata la differenza tra Cassandra/HBase e MongoDB, che rimane saldamente all’ultimo posto sia in termini di numero di operazioni al secondo massimo, che di latenza.
<pre>
Immagine:
<img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/scrittura4.png" alt="Scrittura#4"/>
</pre>
Questo grafico mostra i risultati ottenuti all’esecuzione delle operazioni complesse di lettura e aggiornamento. La situazione è molto simile a quella che abbiamo osservato nei due casi precedenti.
Possiamo concludere che per quanto riguarda operazioni di questo tipo HBase e Cassandra si rivelano quasi equivalenti, con Cassandra che offre solitamente una latenza leggermente minore, mentre HBase permette un throughput sensibilmente più alto. MongoDB offre invece le prestazioni peggiori, con livelli di latenza in alcuni casi inaccettabili.

####Condizione 3 - Lettura
Questo test sottopone i tre database ad un carico composto da sole letture. Può essere paragonato nel nostro caso ad una serie di operazioni di anteprima di uno short url, o per la lettura delle statistiche di un url.
<pre>
<img src="https://github.com/GruppoPBDMNG-7/shortify.me/blob/master/Documentation/images/scrittura5.png" alt="Scrittura#5"/>
</pre>
In questo caso si rileva come la latenza aumenti all’aumentare del livello del throughput in tutti e tre i casi, e che i tre database non si discostano molto per quanto riguarda la latenza media. In questo caso MongoDB presenta un andamento più lineare (senza picchi), e permette anche di raggiungere il throughput più alto. HBase permette latenze leggermente inferiori. Cassandra invece è ultima in quanto a throughput massimo.

###Conclusioni
I test qui visualizzati mostrano come ognuno dei diversi database abbia relativi pregi e difetti. Prendendo in considerazione le operazioni che saranno effettuate nel sistema, e soprattutto prendendo in considerazione la frequenza con la quale queste saranno effettuate, si può assumere che le operazioni di scrittura di un nuovo url e di visita di uno short url esistente saranno quelle eseguite più frequentemente, mentre la visualizzazione delle statistiche sarà l’operazione eseguita più di rado.
Si conclude quindi che Cassandra si è dimostrato essere il database migliore per queste tipologie di operazioni, sia per quanto riguarda la latenza, che per il throughput.

Fonte:[Evaluating NoSQL performance: Which database is right for your data?](http://jaxenter.com/evaluating-nosql-performance-which-database-is-right-for-your-data-107481.html "Evaluating NoSQL performance: Which database is right for your data?")
