import java.util.Calendar;
import java.util.HashMap;

import me.shortify.dao.DAO;
import me.shortify.dao.Statistics;


public class FakeDAO implements DAO {

	@Override
	public boolean checkUrl(String shortUrl) {
		return shortUrl.equals("urlEsistente");
	}

	@Override
	public void putUrl(String shortUrl, String longUrl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUrlStatistics(String shortUrl, String country, String ip,
			Calendar date) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getUrl(String shortUrl) {
		
		if (shortUrl.equals("url1")) {
			return "long1";
		}
		
		
		return "";
	}

	@Override
	public Statistics getStatistics(String shortUrl) {
		
		if (shortUrl.equals("urlEsistente")) {
			return new Statistics("long", "urlEsistente", new HashMap(), new HashMap(), new HashMap(), 0);
		}
		
		return null;
	}
	
	public static final void main(String a[]) {
		System.out.println(new FakeDAO().getStatistics("prova").toJson());
	}

}
