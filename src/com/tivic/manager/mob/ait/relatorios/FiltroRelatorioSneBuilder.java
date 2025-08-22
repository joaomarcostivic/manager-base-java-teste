package com.tivic.manager.mob.ait.relatorios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lote.impressao.TipoAdesaoSneEnum;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;
import com.tivic.sol.util.date.conversors.SqlFormat;
import sol.dao.ItemComparator;

public class FiltroRelatorioSneBuilder {
	private List<String> filtros;
	private HashMap<String, Object> hashDatas;
	 
	public FiltroRelatorioSneBuilder(SearchCriterios searchCriterios) throws Exception {
		this.filtros = new ArrayList<String>();
		this.hashDatas = new HashMap<String, Object>();
	}
	
	public FiltroRelatorioSneBuilder construirFiltro(SearchCriterios searchCriterios) {
		searchCriterios.getCriterios().forEach(criterio -> {
			if(criterio.getValue() == null && criterio.getValue().trim().equals("")) {
				return;
			}
			checkTpStatus(criterio);
			checkTpStatusContendoMovimento(criterio);
			checkDtInicialMovimento(criterio);
			checkDtFinalMovimento(criterio);
			checkDtInfracao(criterio);
			checkNrPlaca(criterio);
			checkDtInfracao(criterio);
			checkNrCodDetran(criterio);
			checkTpAdesao(criterio);
		});
		
		if (!this.hashDatas.isEmpty())
			getFiltroDtInfracao();
		return this;
	}

	private void checkTpStatus(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.tp_status"))
			filtros.add("Situação atual: " + TipoStatusEnum.valueOf(Integer.valueOf(criterio.getValue())));	
	}
	
	private void checkTpStatusContendoMovimento(ItemComparator criterio) {
		if(criterio.getColumn().equals("B.tp_status") && Integer.valueOf(criterio.getValue()) != TipoStatusEnum.SITUACAO_NAO_DEFINIDA.getKey())
			filtros.add("Contendo movimento: " + TipoStatusEnum.valueOf(Integer.valueOf(criterio.getValue())));	
	}
	
	private void checkDtInicialMovimento(ItemComparator criterio) {
		if(criterio.getColumn().equals("B.dt_movimento_inicial"))
			filtros.add("Data inicial de movimento: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
	}
	
	private void checkDtFinalMovimento(ItemComparator criterio) {
		if(criterio.getColumn().equals("B.dt_movimento_final"))
			filtros.add("Data final de movimento: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
	}
	
	private void checkNrPlaca(ItemComparator criterio) {
		if(criterio.getColumn().equals("B.nr_placa"))
			filtros.add("Placa: " + criterio.getValue());	
	}
	
	private void getFiltroDtInfracao() {
		if (hashDatas.get("DATA_INICIAL_INFRACAO") != null)
			this.filtros.add(String.valueOf(hashDatas.get("DATA_INICIAL_INFRACAO")));
		if (hashDatas.get("DATA_FINAL_INFRACAO") != null)
			this.filtros.add(String.valueOf(hashDatas.get("DATA_FINAL_INFRACAO")));
	}
	
	private void checkDtInfracao(ItemComparator criterio) {
		if(criterio.getColumn().equals("B.dt_infracao") && hashDatas.get("DATA_INICIAL_INFRACAO") == null) {
			this.hashDatas.put("DATA_INICIAL_INFRACAO", "Data inicial de autuacao: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
		}
		else if(criterio.getColumn().equals("B.dt_infracao") && hashDatas.get("DATA_FINAL_INFRACAO") == null) {
			this.hashDatas.put("DATA_FINAL_INFRACAO", "Data final de autuacao: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
		}
	}

	private void checkNrCodDetran(ItemComparator criterio) {
		if(criterio.getColumn().equals("C.nr_cod_detran"))
			filtros.add("Cód. Infração: " + criterio.getValue());	
	} 
	
	private void checkTpAdesao(ItemComparator criterio) {
		if(criterio.getColumn().equals("B.st_adesao_sne"))
			filtros.add("Tipo de Adesão a SNE: " + TipoAdesaoSneEnum.valueOf(Integer.valueOf(criterio.getValue())));	
	}
	
	public List<String> build(){
		return filtros;
	}
}
