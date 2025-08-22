package com.tivic.manager.mob.aitmovimento;

import java.util.GregorianCalendar;

public interface IDataMovimentoAVencer {
	GregorianCalendar calcularVencimento(int cdAit) throws Exception;
}