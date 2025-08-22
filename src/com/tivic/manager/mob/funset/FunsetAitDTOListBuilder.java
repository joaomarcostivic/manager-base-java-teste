package com.tivic.manager.mob.funset;

import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.search.Search;

import sol.dao.ResultSetMap;

public class FunsetAitDTOListBuilder {

	private Search search;
	
	public FunsetAitDTOListBuilder(Search search) {
		this.search = search;
	}
	
	public List<FunsetAitDTO> build(){
		List<FunsetAitDTO> funsetAits = new ArrayList<FunsetAitDTO>();
		ResultSetMap rsmAits = this.search.getRsm();
		while(rsmAits.next()) {
			FunsetAitDTO funsetAitDTO = new FunsetAitDTO();
			funsetAitDTO.setNrPlaca(rsmAits.getString("nr_placa"));
			funsetAitDTO.setNrAit(rsmAits.getString("nr_ait"));
			funsetAitDTO.setSgUfVeiculo(rsmAits.getString("sg_uf_veiculo"));
			funsetAitDTO.setVlMulta(rsmAits.getDouble("vl_multa"));
			funsetAitDTO.setDtInfracao(rsmAits.getGregorianCalendar("dt_infracao"));
			funsetAitDTO.setNrCodigoBarras(rsmAits.getString("nr_codigo_barras"));
			funsetAitDTO.setLgDetranFebraban(rsmAits.getInt("lg_detran_febraban"));
			funsetAitDTO.setDtVencimento(rsmAits.getGregorianCalendar("dt_vencimento"));
			funsetAitDTO.setNrRenainf(rsmAits.getString("nr_renainf"));
			funsetAitDTO.setNrControle(rsmAits.getString("nr_controle"));
			funsetAitDTO.setTpStatusAtual(rsmAits.getInt("tp_status_atual"));
			funsetAitDTO.setTpCancelamento(rsmAits.getInt("tp_cancelamento"));
			funsetAitDTO.setNrRenavan(rsmAits.getString("nr_renavan"));
			funsetAitDTO.setNrCodDetran(rsmAits.getString("nr_cod_detran"));
			funsetAitDTO.setVlInfracao(rsmAits.getDouble("vl_infracao"));
			funsetAitDTO.setStDevolucao(rsmAits.getInt("st_devolucao"));
			funsetAitDTO.setDtPagamento(rsmAits.getGregorianCalendar("dt_pagamento"));
			funsetAitDTO.setVlPago(rsmAits.getDouble("vl_pago"));
			funsetAitDTO.setVlFunset(rsmAits.getDouble("vl_funset"));
			funsetAitDTO.setVlDetran(rsmAits.getDouble("vl_detran_arrecadador"));
			funsetAitDTO.setVlDenatran(rsmAits.getDouble("vl_denatran"));
			funsetAitDTO.setVlRepasse(rsmAits.getDouble("vl_repasse"));
			funsetAits.add(funsetAitDTO);
		}
		return funsetAits;
	}
	
}
