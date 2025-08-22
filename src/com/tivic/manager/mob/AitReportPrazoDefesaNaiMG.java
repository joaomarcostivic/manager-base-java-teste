package com.tivic.manager.mob;

import java.sql.Connection;
import java.text.SimpleDateFormat;

public class AitReportPrazoDefesaNaiMG implements IAitReportCalcularPrazoDefesaNAI{
	
	public String pegarDataDefesaPrimeiraVia(int cdAit, Connection connect) {
		try {	
			AitReportServicesNai.verificarDtPrazoDefesa(cdAit, connect);
			Ait ait = AitDAO.get(cdAit, connect);
			return new SimpleDateFormat("dd/MM/yyyy").format(ait.getDtPrazoDefesa().getTime());
		}
		catch (Exception e) {
			System.out.println("Erro in AitReportPrazoDefesaNaiMG > pegarDataDefesaPrimeiraVia()");
			e.printStackTrace();
			return null;
		}
		
	}
	

}
