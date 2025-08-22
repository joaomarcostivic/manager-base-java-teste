package com.tivic.manager.mob.funset;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

public class FunsetInfos {

	public String getNomeArquivoFunset() {
		return "ARQM_" + getDataArquivo() + getOrgaoArrecadador() + "00";
	}

	private String getDataArquivo() {
		GregorianCalendar dtArquivo = Util.getDataAtual();
		return Util.formatDate(dtArquivo, "yyyy") + "_" + Util.formatDate(dtArquivo, "MM") + "_";
	}

	private String getOrgaoArrecadador() {
		return ParametroServices.getValorOfParametroAsString("MOB_CD_ORGAO_AUTUADOR", "") + "_";
	}

	public double aplicarTaxaRenainf(double vlPago) {
		return vlPago * 5 / 100;
	}

	public String formatValorMonetario(double vlMonetario) {
		return Util.formatNumber(vlMonetario, 2).replace(",", "");
	}

}
