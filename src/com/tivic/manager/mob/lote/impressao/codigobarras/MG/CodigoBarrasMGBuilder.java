package com.tivic.manager.mob.lote.impressao.codigobarras.MG;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.codigobarras.CodigoBarras;
import com.tivic.sol.report.ReportCriterios;

public class CodigoBarrasMGBuilder {
	private CodigoBarras codigoBarras;
	
	public CodigoBarrasMGBuilder(DadosNotificacao dadosNotificacao, ReportCriterios reportCriterios) {
		codigoBarras = new CodigoBarras();
		DateFormat dataFormatoJuliana = new SimpleDateFormat ("yyDDD");	
		codigoBarras.setDigitoPlaca(dadosNotificacao.getNrPlaca().substring(dadosNotificacao.getNrPlaca().length() - 1, dadosNotificacao.getNrPlaca().length()));
		codigoBarras.setNrProcessamento(dadosNotificacao.getNrControle());
		codigoBarras.setIdentificadorArrecadacao("8");
		codigoBarras.setIdentificadorSegmento("7");
		codigoBarras.setIdentificadorValorReferencia("6");
		codigoBarras.setValorInfracao(buscarValorInfracao(dadosNotificacao.getVlMultaComDesconto().toString(), dadosNotificacao.getVlMulta().toString()));
		codigoBarras.setCodigoFebraban(String.valueOf(reportCriterios.getParametros().get("NR_CD_FEBRABAN")));
		codigoBarras.setDtVencimento(dataFormatoJuliana.format(dadosNotificacao.getDtVencimento().getTime()));
		codigoBarras.setNrProcessamento(acrescentarZerosAEsquesrda (codigoBarras.getNrProcessamento(), 9));
		codigoBarras.setOrgaoAutuador(String.valueOf(reportCriterios.getParametros().get("MOB_CD_ORGAO_AUTUADOR")));
		codigoBarras.setCodigoInfracao((String.valueOf(dadosNotificacao.getNrCodDetran()).substring(0, String.valueOf(dadosNotificacao.getNrCodDetran()).length() - 1)));
	}
	
	private String buscarValorInfracao(String vlMultaComDesconto, String vlMultaSemDesconto) {
		String valor = vlMultaComDesconto != null ? vlMultaComDesconto : vlMultaSemDesconto;		
		BigDecimal roudValor = new BigDecimal(valor).setScale(2, RoundingMode.HALF_EVEN);
		return tratarValor(String.valueOf(roudValor));
	}
	
	private String tratarValor (String valor) {
		String[] separaValor = valor.split("\\D+");
		String valor1 = separaValor[0];
		String valor2 = separaValor[1];
		int tamanhoInteiro = 9;
		int tamanhoDescimal = 2;
		String valorInfracao = acrescentarZerosAEsquesrda(valor1, tamanhoInteiro) + acrescentarZerosAEsquesrda(valor2, tamanhoDescimal);
		return valorInfracao;
	}
	
	private String acrescentarZerosAEsquesrda(String objetoInicial, int tamanhoFinal) {
		int size = tamanhoFinal;
		String zerosAEsquerda = "";
		size -= objetoInicial.length();
		for (int i = 0; i < size; i++) {
			zerosAEsquerda += "0";
		}
		return zerosAEsquerda + objetoInicial;
	}
	
	public CodigoBarras build() {
		return codigoBarras;
	}
}
