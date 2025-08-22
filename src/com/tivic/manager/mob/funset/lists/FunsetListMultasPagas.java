package com.tivic.manager.mob.funset.lists;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.funset.FunsetAitDTO;
import com.tivic.manager.mob.funset.FunsetBuscaAits;
import com.tivic.manager.mob.funset.FunsetInfos;
import com.tivic.manager.mob.funset.FunsetParametrosEntrada;
import com.tivic.manager.mob.funset.parts.FunsetCampo;
import com.tivic.manager.mob.funset.parts.FunsetResumo;
import com.tivic.manager.mob.funset.validations.FunsetValidationObrigatorio;
import com.tivic.manager.util.Util;

public class FunsetListMultasPagas implements FunsetList {

	FunsetBuscaAits funsetBuscaAits;
	FunsetInfos funsetInfos;
	
	public FunsetListMultasPagas() {
		funsetBuscaAits = new FunsetBuscaAits();
		funsetInfos = new FunsetInfos();
	}
	
	@Override
	public List<List<FunsetCampo>> build(FunsetParametrosEntrada funsetParametrosEntrada, FunsetResumo funsetResumo) throws SQLException, Exception {
		List<List<FunsetCampo>> listaLinhasRegistro = new ArrayList<List<FunsetCampo>>();
		List<FunsetAitDTO> funsetAits = funsetBuscaAits.find(funsetParametrosEntrada, AitMovimentoServices.MULTA_PAGA);
		for (FunsetAitDTO funsetAitDTO : funsetAits) {
			List<FunsetCampo> listFunsetCampo = new ArrayList<FunsetCampo>();
			
			listFunsetCampo.add(getTipoRegistro(funsetAitDTO));
			listFunsetCampo.add(getAit(funsetAitDTO));
			listFunsetCampo.add(getRenavan(funsetAitDTO));
			listFunsetCampo.add(getCodigoOrgaoAutuador(funsetAitDTO));
			listFunsetCampo.add(getCodigoOrgaoFiscalizador(funsetAitDTO));
			listFunsetCampo.add(getCodigoRenainf(funsetAitDTO));
			listFunsetCampo.add(getCodigoInfraest(funsetAitDTO));
			listFunsetCampo.add(getCodigoInfracao(funsetAitDTO));
			listFunsetCampo.add(getValorInfracao(funsetAitDTO));
			listFunsetCampo.add(getValorArrecadado(funsetAitDTO));
			listFunsetCampo.add(getValorFunset(funsetAitDTO));
			listFunsetCampo.add(getValorTaxaRenainf(funsetAitDTO));
			listFunsetCampo.add(getDataArrecadacao(funsetAitDTO));
			listFunsetCampo.add(getDataRepasseFunset(funsetAitDTO));
			listFunsetCampo.add(getCodigoRetencaoFunset(funsetAitDTO));
			listFunsetCampo.add(getTipoRepasseFunset(funsetAitDTO));
			listFunsetCampo.add(getCodigoBancoArrecadador(funsetParametrosEntrada.getNrCodigoBancoArrecadador()));
			listFunsetCampo.add(getIdentificacaoNotificacaoBaixa(funsetAitDTO));
			
			funsetResumo.addQuantidadeMultas();
			funsetResumo.addTotalArrecadacao(funsetAitDTO.getVlPago());
			funsetResumo.addTotalRepasseFunset(funsetInfos.aplicarTaxaRenainf(funsetAitDTO.getVlPago()));
			
			listaLinhasRegistro.add(listFunsetCampo);
		}
		return listaLinhasRegistro;
	}
	
	private FunsetCampo getTipoRegistro(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Tipo de Registro");
		funsetCampo.setTamanho(1);
		funsetCampo.setValorCampo("M");
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getAit(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("AIT");
		funsetCampo.setTamanho(10);
		funsetCampo.setValorCampo(funsetAitDTO.getNrAit());
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getRenavan(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("RENAVAN");
		funsetCampo.setTamanho(11);
		funsetCampo.setValorCampo(funsetAitDTO.getNrRenavan());
		return funsetCampo;
	}

	private FunsetCampo getCodigoOrgaoAutuador(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Código do Orgão Autuador");
		funsetCampo.setTamanho(6);
		funsetCampo.setValorCampo(ParametroServices.getValorOfParametroAsString("MOB_CD_ORGAO_AUTUADOR", ""));
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getCodigoOrgaoFiscalizador(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Código do Orgão Fiscalizador");
		funsetCampo.setTamanho(6);
		funsetCampo.setValorCampo("");
		return funsetCampo;
	}

	private FunsetCampo getCodigoRenainf(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Código RENAINF");
		funsetCampo.setTamanho(11);
		funsetCampo.setValorCampo(funsetAitDTO.getNrRenainf());
		return funsetCampo;
	}

	private FunsetCampo getCodigoInfraest(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Código INFRAEST");
		funsetCampo.setTamanho(11);
		funsetCampo.setValorCampo("");//TODO Campo opcional
		return funsetCampo;
	}

	private FunsetCampo getCodigoInfracao(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Código Infração");
		funsetCampo.setTamanho(5);
		funsetCampo.setValorCampo(funsetAitDTO.getNrCodDetran());
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getValorInfracao(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Valor Infração");
		funsetCampo.setTamanho(7);
		funsetCampo.setValorCampo(funsetInfos.formatValorMonetario(funsetAitDTO.getVlMulta()));
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getValorArrecadado(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Valor Arrecadado");
		funsetCampo.setTamanho(7);
		funsetCampo.setValorCampo(funsetInfos.formatValorMonetario(funsetAitDTO.getVlPago()));
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getValorFunset(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Valor FUNSET");
		funsetCampo.setTamanho(7);
		funsetCampo.setValorCampo(funsetInfos.formatValorMonetario(funsetInfos.aplicarTaxaRenainf(funsetAitDTO.getVlPago())));
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getValorTaxaRenainf(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Valor da taxa RENAINF");
		funsetCampo.setTamanho(7);
		funsetCampo.setValorCampo("0");
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}
	
	private FunsetCampo getDataArrecadacao(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Data Arrecadação");
		funsetCampo.setTamanho(8);
		funsetCampo.setValorCampo(Util.formatDate(funsetAitDTO.getDtPagamento(), "yyyyMMdd"));
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getDataRepasseFunset(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Data Repasse FUNSET");
		funsetCampo.setTamanho(8);
		funsetCampo.setValorCampo("");//TODO Campo não é necessário pois o código de retenção é 1
		return funsetCampo;
	}

	private FunsetCampo getCodigoRetencaoFunset(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Código Retenção FUNSET");
		funsetCampo.setTamanho(1);
		funsetCampo.setValorCampo("1");//TODO valor fixo - Banco Retém
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getTipoRepasseFunset(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Tipo Repasse FUNSET");
		funsetCampo.setTamanho(1);
		funsetCampo.setValorCampo("2");//TODO valor fixo - GRU SPB-Banco – Mensagem STN TES 0034
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getCodigoBancoArrecadador(String nrCodigoBancoArrecadador) throws Exception {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Código Banco Arrecadador");
		funsetCampo.setTamanho(3);
		funsetCampo.setValorCampo(nrCodigoBancoArrecadador);
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getIdentificacaoNotificacaoBaixa(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Identificação da notificação para baixa");
		funsetCampo.setTamanho(10);
		funsetCampo.setValorCampo(funsetAitDTO.getNrCodigoBarras());
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}


}
