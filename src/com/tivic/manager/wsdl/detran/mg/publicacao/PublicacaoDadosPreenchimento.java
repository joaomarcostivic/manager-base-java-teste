package com.tivic.manager.wsdl.detran.mg.publicacao;

import java.util.GregorianCalendar;

import com.tivic.manager.wsdl.DefinidorDataPrazo;

public class PublicacaoDadosPreenchimento {
	
	public static void preencherDatasViaBaseEstadual(PublicacaoDadosRetorno retorno, String idAit) {
		try {
			if (retorno == null || idAit == null || idAit.trim().isEmpty()) return;
			DefinidorDataPrazo definidor = new DefinidorDataPrazo();
			GregorianCalendar novaDataDefesa = definidor.getDataLimiteDefesa(idAit, retorno.getDataNovoPrazoDefesa());
			GregorianCalendar novaDataFici   = definidor.getDataLimiteDefesa(idAit, retorno.getDataNovoPrazoFici());
			GregorianCalendar novaDataJari   = definidor.getDataLimiteRecurso(idAit, retorno.getDataNovoPrazoJari());
			retorno.setDataNovoPrazoDefesa(novaDataDefesa);
			retorno.setDataNovoPrazoFici(novaDataFici);
			retorno.setDataNovoPrazoJari(novaDataJari);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
