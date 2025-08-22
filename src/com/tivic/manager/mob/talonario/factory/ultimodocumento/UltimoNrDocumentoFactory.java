package com.tivic.manager.mob.talonario.factory.ultimodocumento;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class UltimoNrDocumentoFactory {    
    public final List<Integer> TP_TALAO_AIT = Arrays.asList(
            TipoTalaoEnum.TP_TALONARIO_ELETRONICO_TRANSITO.getKey(),
            TipoTalaoEnum.TP_TALONARIO_VIDEO_MONITORAMENTO.getKey(),
            TipoTalaoEnum.TP_TALONARIO_RADAR_ESTATICO.getKey(),
            TipoTalaoEnum.TP_TALONARIO_RADAR_FIXO.getKey(),
            TipoTalaoEnum.TP_TALONARIO_ZONA_AZUL.getKey()
        );

    public UltimoNrDocumentoFactory() { }
    
    public UltimoNrDocumentoStrategy getStrategy(Talonario talonario) throws Exception {
    	int tpTalao = talonario.getTpTalao();
    	
		if (TP_TALAO_AIT.contains(tpTalao)) {
			return new UltimoAitStrategy(talonario);
		}
		if (tpTalao == TipoTalaoEnum.TP_TALONARIO_ELETRONICO_RRD.getKey()) {
			return new UltimoRrdStrategy(buildNrDocumentoCriterios("nr_rrd", talonario), talonario);
		}
		if (tpTalao == TipoTalaoEnum.TP_TALONARIO_ELETRONICO_TRRAV.getKey()) {
			return new UltimoTrravStrategy(buildNrDocumentoCriterios("nr_trrav", talonario), talonario);
		}
		if (tpTalao == TipoTalaoEnum.TP_TALONARIO_ELETRONICO_BOAT.getKey()) {
			return new UltimoBoatStrategy(buildNrDocumentoCriterios("nr_boat", talonario), talonario);
		}
		
		throw new IllegalArgumentException("O tipo de talonário " + talonario.getTpTalao() + " não é suportado.");		
    }
    
	private SearchCriterios buildNrDocumentoCriterios(String column, Talonario talonario) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios(column, String.valueOf(talonario.getNrInicial()), ItemComparator.GREATER_EQUAL, Types.VARCHAR);
		searchCriterios.addCriterios(column, String.valueOf(talonario.getNrFinal()), ItemComparator.MINOR_EQUAL, Types.VARCHAR);
		
		return searchCriterios;
	}
}
