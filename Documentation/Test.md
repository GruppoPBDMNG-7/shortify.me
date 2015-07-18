#Test


Abbiamo sostenuto vari test di tipo whitebox per quanto riguarda le seguenti classi:


1.  Algorithm (pkg: me.shortify.utils.shortenerUrl) testando il metodo 
				
		-buildShortUrl 
Costruzione dello shortUrl partendo da una stringa che rappresenta il long url.In particolare si è testato se questo metodo restituisce short url uguali per stringhe con lo stesso carattere ripetuto (es. "aaaaaa") e short url diversi per un long url uguale.

2. WordChecker (pkg: me.shortify.utils.filter) testando il metodo
		
		-isBadWord
Controlla se una parola è o meno accettata secondo una blacklist suddivisa per lingua. In particolare si è testato se riconosceva una "bad word" per ogni lingua e se una parola normale non risultasse come negativa.

3. DomainChecker (pkg: me.shortify.utils.filter) testando il metodo

		-isBadDomain
Controlla se un dominio di un url è accettato o meno secondo una blacklist di [domini sospetti](https://isc.sans.edu/suspicious_domains.html). In particolare si è testato se preso un dominio inserito nella blacklist, scritto in modi diversi(www.badDomain.com piuttosto che https://badDomain.com), venisse sempre riconosciuto come dominio non buono. 

4. CoutryIPInformation 
					