package com.tivic.manager.mob.lote.impressao.builders;

import java.util.Calendar;
import java.util.GregorianCalendar;
import com.tivic.sol.util.date.DateUtil;

public class GeradorDtPostagem {
	
	public String  gerar(GregorianCalendar dtCriacao) {
		GregorianCalendar dtPostagem = (GregorianCalendar) dtCriacao.clone();
		int diasUteis = 0;
		while(diasUteis < 3) {
			dtPostagem.add(Calendar.DAY_OF_MONTH, 1);
			int diaSemana = dtPostagem.get(Calendar.DAY_OF_WEEK);
			if(diaSemana != Calendar.SATURDAY && diaSemana != Calendar.SUNDAY)
				diasUteis++;
		}
		return DateUtil.formatDate(dtPostagem, "dd/MM/yyyy");
	}
}