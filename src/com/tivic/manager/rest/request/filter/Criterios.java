package com.tivic.manager.rest.request.filter;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;

public class Criterios extends ArrayList<ItemComparator> {
	private static final long serialVersionUID = 6193118012489030253L;
	
	public Criterios(String column, String value, int operation, int columnType) {
		super();
		this.add(new ItemComparator(column, value, operation, columnType));
	}
	
	public Criterios() { }

	public Criterios add(String column, String value, int operation, int columnType) {
		this.add(new ItemComparator(column, value, operation, columnType));
		return this;
	}
	
	public Criterios addEqualDate(String column, GregorianCalendar date) {
		GregorianCalendar dateInicial = (GregorianCalendar) date.clone();
		dateInicial.set(Calendar.HOUR_OF_DAY, 0);
		dateInicial.set(Calendar.MINUTE, 0);
		dateInicial.set(Calendar.SECOND, 0);
		GregorianCalendar dateFinal = (GregorianCalendar) date.clone();
		dateFinal.set(Calendar.HOUR_OF_DAY, 23);
		dateFinal.set(Calendar.MINUTE, 59);
		dateFinal.set(Calendar.SECOND, 59);
		this.add(new ItemComparator(column, Util.convCalendarStringSqlCompleto(dateInicial), ItemComparator.GREATER_EQUAL, Types.VARCHAR));
		this.add(new ItemComparator(column, Util.convCalendarStringSqlCompleto(dateFinal), ItemComparator.MINOR_EQUAL, Types.VARCHAR));
		return this;
	}

}
