package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class AitDataPandemiaValidator {
	
	private List<AitDataNovoPrazo> dtPandemia;
	
	private List<AitDataNovoPrazo> initDatas() {
		List<AitDataNovoPrazo> datasPandemia = new ArrayList<AitDataNovoPrazo>();
		
		//DATAS PANDEMIA 2020 (ABRIL - NOVEMBRO)
		datasPandemia.add(new AitDataNovoPrazo(
				AitDataNovoPrazo.ABRIL,
				new GregorianCalendar(2020, 03, 01),
				new GregorianCalendar(2020, 03, 30),
				new GregorianCalendar(2021, 01, 01),
				new GregorianCalendar(2021, 01, 28)
				));
		
		datasPandemia.add(new AitDataNovoPrazo(
				AitDataNovoPrazo.MAIO,
				new GregorianCalendar(2020, 04, 01),
				new GregorianCalendar(2020, 04, 31),
				new GregorianCalendar(2021, 02, 01),
				new GregorianCalendar(2021, 02, 31)
				));
		
		datasPandemia.add(new AitDataNovoPrazo(
				AitDataNovoPrazo.JUNHO,
				new GregorianCalendar(2020, 05, 01),
				new GregorianCalendar(2020, 05, 30),
				new GregorianCalendar(2021, 03, 01),
				new GregorianCalendar(2021, 03, 30)
				));
		
		datasPandemia.add(new AitDataNovoPrazo(
				AitDataNovoPrazo.JULHO,
				new GregorianCalendar(2020, 06, 01),
				new GregorianCalendar(2020, 06, 31),
				new GregorianCalendar(2021, 04, 01),
				new GregorianCalendar(2021, 04, 31)
				));
		
		datasPandemia.add(new AitDataNovoPrazo(
				AitDataNovoPrazo.AGOSTO,
				new GregorianCalendar(2020, 07, 01),
				new GregorianCalendar(2020, 07, 31),
				new GregorianCalendar(2021, 05, 01),
				new GregorianCalendar(2021, 05, 30)
				));
		
		datasPandemia.add(new AitDataNovoPrazo(
				AitDataNovoPrazo.SETEMBRO,
				new GregorianCalendar(2020, 8, 01),
				new GregorianCalendar(2020, 8, 31),
				new GregorianCalendar(2021, 06, 01),
				new GregorianCalendar(2021, 06, 30)
				));
		
		datasPandemia.add(new AitDataNovoPrazo(
				AitDataNovoPrazo.OUTUBRO,
				new GregorianCalendar(2020, 9, 01),
				new GregorianCalendar(2020, 9, 31),
				new GregorianCalendar(2021, 07, 01),
				new GregorianCalendar(2021, 07, 30)
				));
		
		datasPandemia.add(new AitDataNovoPrazo(
				AitDataNovoPrazo.NOVEMBRO,
				new GregorianCalendar(2020, 10, 01),
				new GregorianCalendar(2020, 10, 31),
				new GregorianCalendar(2021, 8, 01),
				new GregorianCalendar(2021, 8, 30)
				));
		
		return datasPandemia;
	}
	
	public boolean validPeriodoPandemia(GregorianCalendar dtInfracao) {
		GregorianCalendar dtAtual = new GregorianCalendar();
		dtPandemia = initDatas();
		
		for(AitDataNovoPrazo mesPandemia : dtPandemia) {
			if(
					dtInfracao.after(mesPandemia.getDtInicioPrazoOriginal()) && 
					dtInfracao.before(mesPandemia.getDtFimPrazoOriginal()) &&
					dtAtual.after(mesPandemia.getDtInicioNovoPrazo()) &&
					dtAtual.before(mesPandemia.getDtFimNovoPrazo())
					) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean validPeriodoPandemiaGeral(GregorianCalendar dtInfracao) {
		System.out.println("Validação período pandemia geral");
		dtPandemia = initDatas();
		
		if(dtInfracao.after(dtPandemia.get(0).getDtInicioPrazoOriginal()) && dtInfracao.before(dtPandemia.get(7).getDtFimPrazoOriginal())) {
			System.out.println("Mês correto");
			return true;
		}
		
		System.out.println("Mês incorreto");
		return false;
	}

}
