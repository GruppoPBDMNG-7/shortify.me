#Test


Abbiamo sostenuto vari test di tipo whitebox per quanto riguarda le seguenti classi:


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
Restituisce una stringa contenente tutti i dati relativi alle statistiche, realizzata in formato JSON. Obiettivo del test è quello di verificare che la struttura dell'oggetto restituito fosse corretta. Si sono utilizzati dei casi di test tipici, contenenti insiemi di dati standard, e un caso limite, corrispondente ad un oggetto non contenente nessun contatore.
