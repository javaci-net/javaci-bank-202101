package net.javaci.bank202101.api.job;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import net.javaci.bank202101.db.model.ExchangeRate;
import net.javaci.bank202101.db.model.enumaration.AccountCurrency;

public class ExchangeRateParser {

	private Reader jsonReader;

	public ExchangeRateParser(Reader jsonReader) {
		this.jsonReader = jsonReader;
	}
	
	public List<ExchangeRate>  parse() {
		Map map = new Gson().fromJson(jsonReader, Map.class);
		if (!"success".equals(map.get("result")))
			return null;
		
		String dateStr = (String)map.get("time_last_update_utc");
		//Sun, 25 Apr 2021 00:00:01 +0000
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, dd MMM yyyy hh:mm:ss Z");
		LocalDate date = LocalDate.parse(dateStr, dtf);
		
		Map currMap = (Map)map.get("conversion_rates");
		List<ExchangeRate> list = new ArrayList<>();
		
		for (AccountCurrency ac : AccountCurrency.values()) {
			if (ac == AccountCurrency.USD || ac == AccountCurrency.GOLD)
				continue;

			Double rate = (Double)currMap.get(ac.code);
			ExchangeRate er = new ExchangeRate();
			er.setCurrency(ac);
			er.setDate(date);
			er.setRate(BigDecimal.valueOf(rate));
			list.add(er);
		}
		return list;
	}
}
