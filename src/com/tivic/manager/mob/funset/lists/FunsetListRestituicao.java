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

public class FunsetListRestituicao implements FunsetList {

	FunsetBuscaAits funsetBuscaAits;
	FunsetInfos funsetInfos;
	
	public FunsetListRestituicao() {
		funsetBuscaAits = new FunsetBuscaAits();
		funsetInfos = new FunsetInfos();
	}
	
	@Override
	public List<List<FunsetCampo>> build(FunsetParametrosEntrada funsetParametrosEntrada, FunsetResumo funsetResumo) throws SQLException, Exception {
		List<List<FunsetCampo>> listaLinhasRegistro = new ArrayList<List<FunsetCampo>>();
		List<FunsetAitDTO> funsetAits = funsetBuscaAits.find(funsetParametrosEntrada, AitMovimentoServices.DEVOLUCAO_PAGAMENTO);
		for (FunsetAitDTO funsetAitDTO : funsetAits) {
			List<FunsetCampo> listFunsetCampo = new ArrayList<FunsetCampo>();
			
			listFunsetCampo.add(getTipoRegistro(funsetAitDTO));
			listFunsetCampo.add(getAit(funsetAitDTO));
			listFunsetCampo.add(getRenavan(funsetAitDTO));
			listFunsetCampo.add(getCodigoOrgaoAutuador(funsetAitDTO));
			listFunsetCampo.add(getCodigoOrgaoFiscalizador(funsetAitDTO));
			listFunsetCampo.add(getValorRestituido(funsetAitDTO));
			listFunsetCampo.add(getMotivo(funsetAitDTO));
			listFunsetCampo.add(getCodigoInfracao(funsetAitDTO));
			
			funsetResumo.addQuantidadeRestituicoes();
			funsetResumo.addTotalRestituido(funsetAitDTO.getVlPago());
			
			
			listaLinhasRegistro.add(listFunsetCampo);
		}
		return listaLinhasRegistro;
	}

	private FunsetCampo getTipoRegistro(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Tipo de Registro");
		funsetCampo.setTamanho(1);
		funsetCampo.setValorCampo("R");
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
	

	private FunsetCampo getValorRestituido(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Valor Arrecadado");
		funsetCampo.setTamanho(7);
		funsetCampo.setValorCampo(funsetInfos.formatValorMonetario(funsetAitDTO.getVlPago()));
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
		return funsetCampo;
	}

	private FunsetCampo getMotivo(FunsetAitDTO funsetAitDTO) {
		FunsetCampo funsetCampo = new FunsetCampo();
		funsetCampo.setTipoRegistro("Motivo");
		funsetCampo.setTamanho(1);
		funsetCampo.setValorCampo("1");//TODO por enquanto fixo enquanto se decide
		funsetCampo.setFunsetValidation(new FunsetValidationObrigatorio());
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

}
