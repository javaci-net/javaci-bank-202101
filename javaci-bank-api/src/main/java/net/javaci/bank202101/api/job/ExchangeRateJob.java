package net.javaci.bank202101.api.job;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import net.javaci.bank202101.db.dao.impl.ExchangeRateDaoImpl;
import net.javaci.bank202101.db.model.ExchangeRate;

@Slf4j
public class ExchangeRateJob implements Job {

	private static final String API_KEY  = "4149f55af2ac524805bc8dd6";
	
	private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";
		
	@Autowired
    private ExchangeRateDaoImpl exchangeRateDao;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("ExchangeRateJob started");
		Reader jsonReader = null;
		try {
			jsonReader = callExchangeRateApi();
		} catch (Exception e) {
			log.error("Exception ocured" ,e );
			return;
		}
		ExchangeRateParser parser = new ExchangeRateParser(jsonReader);
		List<ExchangeRate> exchageRates = parser.parse();
		if (exchageRates == null) {
			log.error("Exchange rate list is null");
			return;
		}
		for (ExchangeRate er : exchageRates) {
			if (exchangeRateDao.existsByDateAndCurrency(er.getDate(), er.getCurrency())) {
				continue;
			}
			exchangeRateDao.save(er);
		}
		
	}
	
	private Reader callExchangeRateApi() throws MalformedURLException, IOException {
		HttpURLConnection req = (HttpURLConnection)new URL(API_URL).openConnection();
		req.connect();
		
		return new InputStreamReader((InputStream)req.getContent());
		
		
	}

}
