package com.tivic.manager.mob;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;


import sol.dao.ResultSetMap;

public class AitReportCriarCodigoBarras implements IAitReportCriarCodigoBarrasNip{

	@Override
	public String criarCodigoBarras(ResultSetMap rsm, HashMap<String, Object> paramns, Connection connect) {
		ItemCodigoBarrasMG itemCodigoBarras = new ItemCodigoBarrasMG();
		DateFormat dataFormatoJuliana = new SimpleDateFormat ("yyDDD");	
		String codigoBarras = "";
		
		if (rsm.next()) {
			itemCodigoBarras.setDigitoPlaca(buscarUltimoDigitoPlaca(rsm.getString("NR_PLACA")));
			itemCodigoBarras.setNrProcessamento(rsm.getString("NR_CONTROLE"));
			itemCodigoBarras.setIdentificadorArrecadacao("8");
			itemCodigoBarras.setIdentificadorSegmento("7");
			itemCodigoBarras.setIdentificadorValorReferencia("6");
			itemCodigoBarras.setValorInfracao(tratarValor(buscarValorInfracao(rsm.getString("VL_MULTA_DESCONTO"), rsm.getString("VL_MULTA"))));
			itemCodigoBarras.setCodigoFebraban((String) paramns.get("NR_CD_FEBRABAN"));
			itemCodigoBarras.setDtVencimento(dataFormatoJuliana.format(rsm.getTimestamp ("DT_VENCIMENTO_BOLETO")));
			itemCodigoBarras.setNrProcessamento(acrescentarZerosAEsquesrda (itemCodigoBarras.getNrProcessamento(), 9));			
			itemCodigoBarras.setOrgaoAutuador((String) paramns.get ("MOB_CD_ORGAO_AUTUADOR"));
			itemCodigoBarras.setCodigoInfracao(buscarCodigoInfracao(rsm.getString ("NR_COD_DETRAN")));
		}
		rsm.beforeFirst();
		
		codigoBarras = itemCodigoBarras.formarCodigoBarras();
		
		return codigoBarras;
	}
	
	private static String buscarUltimoDigitoPlaca(String nrPlaca) {
		String ultimoDigito = null;
		ultimoDigito = nrPlaca.substring(nrPlaca.length() - 1, nrPlaca.length());
		return ultimoDigito;
	}
	
	private static String buscarValorInfracao(String vlMultaComDesconto, String vlMultaSemDesconto) {
		String valor = "";
		valor = vlMultaComDesconto != null ? vlMultaComDesconto : vlMultaSemDesconto;		
		BigDecimal roudValor = new BigDecimal(valor).setScale(2, RoundingMode.HALF_EVEN);
		return String.valueOf(roudValor);
	}
	
	private static String buscarCodigoInfracao(String nrCodDetran) {
		if (nrCodDetran.length() > 0) {
			nrCodDetran = nrCodDetran.substring(0, nrCodDetran.length() - 1);
		}
		return nrCodDetran;
	}
	
	private static String tratarValor (String valor) {
		String valorInfracao = "";
		String[] separaValor = valor.split("\\D+");
		String valor1 = separaValor[0];
		String valor2 = separaValor[1];
		int tamanhoInteiro = 9;
		int tamanhoDescimal = 2;
		valorInfracao = acrescentarZerosAEsquesrda(valor1, tamanhoInteiro) + acrescentarZerosAEsquesrda(valor2, tamanhoDescimal);
		return valorInfracao;
	}
	
	private static String acrescentarZerosAEsquesrda(String objetoInicial, int tamanhoFinal) {
		int size = tamanhoFinal;
		String zerosAEsquerda = "";
		size -= objetoInicial.length();
		for (int i = 0; i < size; i++) {
			zerosAEsquerda += "0";
		}
		return zerosAEsquerda + objetoInicial;
	}
	
}
