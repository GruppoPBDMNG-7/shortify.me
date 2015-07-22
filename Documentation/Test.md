#Test


Abbiamo sostenuto vari test per quanto riguarda le seguenti classi:


#####1.  Algorithm (pkg: me.shortify.utils.shortenerUrl) testando il metodo 
				
		-buildShortUrl 
Costruzione dello shortUrl partendo da una stringa che rappresenta il long url.In particolare si è testato se questo metodo restituisce short url uguali per stringhe con lo stesso carattere ripetuto (es. "aaaaaa") e short url diversi per un long url uguale.

#####2. WordChecker (pkg: me.shortify.utils.filter) testando il metodo
		
		-isBadWord
Controlla se una parola è o meno accettata secondo una blacklist suddivisa per lingua. In particolare si è testato se riconosceva una "bad word" per ogni lingua e se una parola normale non risultasse come negativa.

#####3. DomainChecker (pkg: me.shortify.utils.filter) testando il metodo

		-isBadDomain
Controlla se un dominio di un url è accettato o meno secondo una blacklist di [domini sospetti](https://isc.sans.edu/suspicious_domains.html). In particolare si è testato se preso un dominio inserito nella blacklist, scritto in modi diversi(www.badDomain.com piuttosto che https://badDomain.com), venisse sempre riconosciuto come dominio non buono. 

#####4. CoutryIPInformation (pkg: me.shortify.utils.geoLocation) testando il metodo:
 
		-getCountry
Associa a un particolare IP una stringa che rappresenta la nazionalità.(Es. IT,US,GB,DE). In particolare si è testato con 6 ip di nazionalità diverse, ma conosciute a priori, se venivano riconosciute in maniera corretta.

#####5. CassandraDAO	(pkg: me.shortify.dao) testando i metodi:

		-checkUrl
Controlla se uno short url è già presente nel database. In particolare si è testato che il valore restituito sia true se è presente e false se assente.

		-putUrl
Inserisce una coppia short url - long url all'interno del database. In particolare si è testato se l'inserimento di una coppia non esistente venga fatto in maniera corretta e che risulti, dopo l'utilizzo del metodo, memorizzata.	

		-updateUrlStatistics		
Aggiorna i valori legati alle statistiche di uno short url. In particolare si è testato se, dopo aver creato un nuovo short url e aggiornate le statistiche per un click, i valori riscontrati fossero coerenti con l'update fatto.

		-getStatistics
Restituisce l'oggetto Statistics che contiene campi, i quali rappresentano le varie statistiche di uno short url. In particolare si è testato	se, dopo aver creato un nuovo short url e aggiornate le statistiche per un click, i valori riscontrati fossero coerenti con l'update fatto e se avvenisse un ulteriore update con lo stesso ip non debba esser contato.	

		-getUrl
Restituisce una stringa che rappresenta il long url associato a uno short url. In particolare si è testato che restituisse una stringa vuota se lo short url fosse inesistente e una stringa prestabilita con uno short url prestabilito. 
		
#####6. Statistics (pkg: me.shortify.dao) testando il metodo:
		-toJson
Restituisce una stringa contenente tutti i dati relativi alle statistiche, realizzata in formato JSON. Obiettivo del test è quello di verificare che la struttura dell'oggetto restituito sia corretta. Si sono utilizzati dei casi di test tipici, contenenti insiemi di dati standard, e un caso limite, corrispondente ad un oggetto non contenente nessun contatore.

#####7. ShortenerServices (pkg: me.shortify.sparkserver) testando i metodi:
		-conversioneURL
Restituisce una stringa, in formato JSON, con lo short URL creato dall'algoritmo. Obiettivo del test è quello di verificare che tale stringa rispetti il formato stabilito, ovvero:

 `{"shortUrl":"<codice short url>"}`.

Dato che l'algoritmo utilizzato genera diversi codici di short url per una stessa stringa in modo casuale, non è stato possibile confrontare direttamente i due valori, quello atteso e quello ottenuto. Quindi, quando non era presente nel JSON di partenza, dato in input al metodo, un custom text, il metodo di test confrontava solamente la lunghezza della striga ottenuta dalla lunghezza attesa (ovvero 23 caratteri, contando anche le partentesi, le virgolette, i caratteri della stringa "shortUrl"). Se il metodo di conversione terminava senza eccezioni, il risultato doveva essere una stringa di quel tipo, la cui lunghezza non varia mai. 

Tuttavia, se nel JSON in input al metodo era specificato anche un custom text per la creazione di short url personalizzati, allora il valore atteso che doveva essere confrontato con quello ottenuto era del seguente tipo:

`{"shortUrl":"<custom text inserito>"}"`

In questo caso si è fatto un confronto lettera per lettera delle due stringhe.

Il metodo di conversione, inoltre, lancia le seguenti eccezioni:

- BadURLException (lanciata quando il long url è una stringa vuota oppure appartiene ad una *black list* di url non sicuri);
- BadCustomURLException (lanciata quando il custo text specificato era presente in una *black list* di parole non consentite);
- URLExistsException (lanciata quando lo short url generato dall'algoritmo, dopo 10 tentativi, risultata essere già presente nel database, oppure quando il custom text specificato era già presente nel database).

Le condizioni che, se verificate, lanciano queste eccezioni sono state testate con oppurtuni casi di test; questo per verificare se effettivamente viene generata l'eccezione che ci si aspettava solamente con determinati input "errati". Per fare questi test, quindi, nel metodo di test, si sono predisposti dei gestori dell'eccezioni affiché queste venissero catturate; quando si verificava un'eccezione, nell'opportuno blocco di *catch* si controllava se quella eccezione era prevista, cioè che si è verificata durante l'esecuzione di uno specifico caso di test che doveva lanciarla. 

		-visitaURL
Restituisce il long url a cui uno short si riferisce. Gli input di questo metodo sono una stringa per lo short url visitato, e una stringa per l'indirizzo IP dell'host visitante.

Per tutti i test della classe ShortenerServices si è utilizzato un FakeDAO, classe che realizza l'interfaccia DAO dando una implementazione fittizia ai suoi metodi. Dato che il metodo di visita di un URL utilizza il metodo del DAO per cercare l'url nel database, nel FakeDAO l'unico short URL che restituisce un long url è "url1". Nei casi di test, quindi, si è testata la visita di un url esistente, e la visita di un url non esistente, non presente cioè nel database. Per quest'ultimo caso si è catturata l'eccezione *ShortURLNotFoundException*.

		-ispezionaURL
Restituisce una stringa in formato JSON contenente le statistiche per uno short url dato in input. Anche l'input è una stringa in formato JSON di questo tipo:

`{shorturl:<codice short url>}`

Per uno specifico url, "urlEsistente", nel FakeDAO viene creato un oggetto di classe Statistics che viene restituito al metodo; questo oggetto viene successivamente convertito in stringa (sempre in formato JSON) e viene confrontato con il valore atteso (lettera per lettera). Si sono testati anche i casi in cui lo short url non esistesse o era una stringa vuota catturando rispettivamente le eccezioni *ShortURLNotFoundException* e *BadURLException*.