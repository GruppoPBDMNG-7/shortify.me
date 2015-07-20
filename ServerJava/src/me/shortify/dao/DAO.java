package me.shortify.dao;

import java.util.Calendar;

public interface DAO {
	/**
	 * Controlla se un url è presente nel Database o meno
	 * @param shortUrl lo short url da verificare
	 * @return true se lo short url è presente, false altrimenti
	 */
	public boolean checkUrl(String shortUrl);
	/**
	 * Inserisce un url nel database.
	 * @param shortUrl short url da inserire
	 * @param longUrl long url da iserire
	 */
	public void putUrl(String shortUrl, String longUrl);
	/**
	 * Aggiorna i contatori delle visite dello short url selezionato
	 * @param shortUrl short url di cui bisogna aggiornare i contatori
	 * @param country paese di provenienza della visita
	 * @param ip ip di provenienza della visita
	 * @param date data e ora in cui si è verificata la visita
	 */
	public void updateUrlStatistics(String shortUrl, String country, String ip, Calendar date);
	/**
	 * Restituisce il long url associato ad uno short url, se questo esiste.
	 * @param shortUrl short url di cui si intende prendere il long url
	 * @return
	 */
	public String getUrl(String shortUrl);
	/**
	 * Ottiene l'oggetto contenente tutte le statistiche dello short url selezionato.
	 * @param shortUrl short url di cui si vogliono ottenere le statistiche
	 * @param date data e ora a cui avviene la richiesta
	 * @param nOfHours numero di ore precedenti alla data corrente, delle quali
	 * 		  si intende ottenere i contatori per ora. Ad esempio, se la data corrente
	 * 		  è 2015-07-20 8:00, e il parametro è 24, si otterranno tutti i 
	 * 		  contatori delle visite a partire da quello del 2015-07-19 8:00.
	 * @return
	 */
	public Statistics getStatistics(String shortUrl, Calendar date, int nOfHours);
	/**
	 * Chiude il collegamento al database e libera le eventuali risorse occupate.
	 */
	public void close();
}
