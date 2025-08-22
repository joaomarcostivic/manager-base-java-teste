package com.tivic.manager.mob.funset.parts;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.funset.FunsetParametrosEntrada;
import com.tivic.manager.mob.funset.lists.FunsetFactoryList;
import com.tivic.manager.mob.funset.lists.FunsetList;
import com.tivic.manager.util.Util;

public class FunsetRegistroBuilder {

	private FunsetRegistro funsetRegistro;
	private FunsetParametrosEntrada funsetParametrosEntrada;
	
	
	public FunsetRegistroBuilder(FunsetParametrosEntrada funsetParametrosEntrada) {
		this.funsetParametrosEntrada = funsetParametrosEntrada;
	}
	
	public FunsetRegistro build() throws SQLException, Exception {
		FunsetResumo funsetResumo = new FunsetResumo("T");
		funsetRegistro.setFunsetCabecalho(getCabecalho());
		funsetRegistro.setListFunsetMultasPagas(getListMultasPagas(funsetParametrosEntrada, funsetResumo));
		funsetRegistro.setListFunsetRestituicao(getListRestituicao(funsetParametrosEntrada, funsetResumo));
		funsetRegistro.setFunsetResumo(funsetResumo);
		return funsetRegistro;
	}
	

	private FunsetCabecalho getCabecalho() {
		FunsetCabecalho funsetCabecalho = new FunsetCabecalho();
		funsetCabecalho.setTipoRegistro("H");
		funsetCabecalho.setCodigoOrgaoTransitoArrecadador(ParametroServices.getValorOfParametroAsString("MOB_CD_ORGAO_AUTUADOR", ""));
		funsetCabecalho.setMesCompetencia(getMesCompetencia());
		return funsetCabecalho;
	}

	private String getMesCompetencia() {
		String mes = Util.fillNum(Util.getDataAtual().get(Calendar.MONTH), 2);
		String ano = String.valueOf(Util.getDataAtual().get(Calendar.YEAR));
		return mes + ano;
	}

	private List<List<FunsetCampo>> getListMultasPagas(FunsetParametrosEntrada funsetParametrosEntrada, FunsetResumo funsetResumo) throws Exception, SQLException{
		FunsetList funsetListMultasPagas = FunsetFactoryList.generate(AitMovimentoServices.MULTA_PAGA);
		return funsetListMultasPagas.build(funsetParametrosEntrada, funsetResumo);
	}
	
	private List<List<FunsetCampo>> getListRestituicao(FunsetParametrosEntrada funsetParametrosEntrada, FunsetResumo funsetResumo) throws Exception, SQLException{
		FunsetList funsetListRestituicao = FunsetFactoryList.generate(AitMovimentoServices.DEVOLUCAO_PAGAMENTO);
		return funsetListRestituicao.build(funsetParametrosEntrada, funsetResumo);
	}

}
