package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class AitDataNovoPrazo {
	
	public static final int JANEIRO 	= 0;
	public static final int FEVEREIRO 	= 1;
	public static final int MARCO		= 2;
	public static final int ABRIL		= 3;
	public static final int MAIO		= 4;
	public static final int	JUNHO		= 5;
	public static final int JULHO		= 6;
	public static final int AGOSTO		= 7;
	public static final int SETEMBRO	= 8;
	public static final int OUTUBRO		= 9;
	public static final int NOVEMBRO	= 10;
	public static final int DEZEMBRO	= 11;
	
	private int DataMesOriginal;
	private GregorianCalendar dtInicioPrazoOriginal;
	private GregorianCalendar dtFimPrazoOriginal;
	private GregorianCalendar dtInicioNovoPrazo;
	private GregorianCalendar dtFimNovoPrazo;
	
	public AitDataNovoPrazo() {}
	
	public AitDataNovoPrazo(int dataMesOriginal, GregorianCalendar dtInicioPrazoOriginal,
							GregorianCalendar dtFimPrazoOriginal, GregorianCalendar dtInicioNovoPrazo,
							GregorianCalendar dtFimNovoPrazo) {
		DataMesOriginal = dataMesOriginal;
		this.dtInicioPrazoOriginal = dtInicioPrazoOriginal;
		this.dtFimPrazoOriginal = dtFimPrazoOriginal;
		this.dtInicioNovoPrazo = dtInicioNovoPrazo;
		this.dtFimNovoPrazo = dtFimNovoPrazo;
	}

	public int getDataMesOriginal() {
		return DataMesOriginal;
	}
	public void setDataMesOriginal(int dataMesOriginal) {
		DataMesOriginal = dataMesOriginal;
	}
	public GregorianCalendar getDtInicioPrazoOriginal() {
		return dtInicioPrazoOriginal;
	}
	public void setDtInicioPrazoOriginal(GregorianCalendar dtInicioPrazoOriginal) {
		this.dtInicioPrazoOriginal = dtInicioPrazoOriginal;
	}
	public GregorianCalendar getDtFimPrazoOriginal() {
		return dtFimPrazoOriginal;
	}
	public void setDtFimPrazoOriginal(GregorianCalendar dtFimPrazoOriginal) {
		this.dtFimPrazoOriginal = dtFimPrazoOriginal;
	}
	public GregorianCalendar getDtInicioNovoPrazo() {
		return dtInicioNovoPrazo;
	}
	public void setDtInicioNovoPrazo(GregorianCalendar dtInicioNovoPrazo) {
		this.dtInicioNovoPrazo = dtInicioNovoPrazo;
	}
	public GregorianCalendar getDtFimNovoPrazo() {
		return dtFimNovoPrazo;
	}
	public void setDtFimNovoPrazo(GregorianCalendar dtFimNovoPrazo) {
		this.dtFimNovoPrazo = dtFimNovoPrazo;
	}

}
