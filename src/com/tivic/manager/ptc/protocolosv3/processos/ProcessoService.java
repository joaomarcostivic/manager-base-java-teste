package com.tivic.manager.ptc.protocolosv3.processos;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class ProcessoService {
		
	public PagedResponse<ProcessosSearchDTO> find(SearchCriterios searchCriterios, boolean lgVencido) throws Exception {
		Search<ProcessosSearchDTO> searchProtocolos = searchProtocolos(searchCriterios, lgVencido);
		List<ProcessosSearchDTO> protocolos = searchProtocolos.getList(ProcessosSearchDTO.class);
		PagedResponse<ProcessosSearchDTO> protocoloList = new ProcessoSearchPaginatorBuilder(protocolos, 
				searchProtocolos
				.getRsm()
				.getTotal())
				.build();
		if(protocoloList.getItems().isEmpty())
			throw new NoContentException("Nenhum protocolo encontrado");
		return protocoloList;
	}
	
	private Search<ProcessosSearchDTO> searchProtocolos(SearchCriterios searchCriterios, boolean lgVencido) throws Exception {
		ItemComparator contendoMovimento = searchCriterios.getAndRemoveCriterio("CT.tp_status");
		ItemComparator dtInicialVencimento = searchCriterios.getAndRemoveCriterio("PTCV1.dt_protocolo");
		ItemComparator dtFinalVencimento = searchCriterios.getAndRemoveCriterio("PTCV2.dt_protocolo");
		ItemComparator aitDocumento = searchCriterios.getAndRemoveCriterio("ait_documento");
		Search<ProcessosSearchDTO> search = new SearchBuilder<ProcessosSearchDTO>("mob_ait_movimento A")
				.fields(" A.dt_movimento, A.tp_status, B.id_ait, B.cd_ait, B.tp_status as st_atual, B.dt_infracao, D.nr_documento, D.cd_documento, D.dt_protocolo, D.tp_documento, "
						+ " E.nm_tipo_documento, E.cd_tipo_documento, F.nm_fase, H.cd_situacao_documento, "
						+ " H.nm_situacao_documento, G.cd_apresentacao_condutor, "
						+ "	CASE WHEN F.nm_fase = 'Pendente' THEN D.dt_protocolo + INTERVAL '1 year' ELSE NULL END AS dt_prazo")
				.addJoinTable(" JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.cd_ait = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
				.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.addJoinTable(" JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" )
				.addJoinTable(" LEFT JOIN ptc_apresentacao_condutor G ON (D.cd_documento = G.cd_documento)")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" A.cd_ait IN " + incluirBuscaProtocoloVencido(lgVencido) 
													 + searchCriteriosAdicionais(contendoMovimento, dtInicialVencimento, dtFinalVencimento)
													 + setCriteriosDinamicos(aitDocumento))
				.orderBy(" D.dt_protocolo DESC ")
				.count()
				.build();
		return search;
	}
	
	private String incluirBuscaProtocoloVencido(boolean lgVencido) throws Exception {
	    String resultado = "(B.cd_ait) ";
	    if (lgVencido) {
	        resultado = " ( SELECT DISTINCT B1.cd_ait FROM mob_ait B1 "
	                + " JOIN mob_ait_movimento_documento C1 ON (C1.cd_ait = B1.cd_ait AND A.cd_movimento = C1.cd_movimento) "
	                + " JOIN ptc_documento D1 ON (C1.cd_documento = D1.cd_documento) "
	                + " WHERE " 
	                + "     D1.dt_protocolo > B1.dt_vencimento "
	                + "         AND B1.cd_ait = B.cd_ait "
	                + "   ) " ;
	    } 
	    return resultado;
	}	
	
	private String searchCriteriosAdicionais(ItemComparator contendoMovimento, ItemComparator dtInicialVencimento, ItemComparator dtFinalVencimento) throws ValidacaoException, Exception {
		String SQL = searchContendoMovimento(contendoMovimento) 
				+ searchDtVencimentoInicial(dtInicialVencimento)
				+ searchDtVencimentoFinal(dtFinalVencimento);
		return SQL;
	}
	
	private String searchContendoMovimento(ItemComparator contendoMovimento) throws ValidacaoException, Exception {
		String criterios = " ";
		if (Integer.valueOf(contendoMovimento.getValue()) > TipoStatusEnum.SITUACAO_NAO_DEFINIDA.getKey()) {
			criterios = " AND EXISTS"
				+ " ("
				+ " 	SELECT G.cd_ait, G.tp_status, G.dt_movimento FROM mob_ait_movimento G"
				+ "		WHERE  ( G.tp_status = " + contendoMovimento.getValue()   
				+ "					AND G.cd_ait = A.cd_ait"
				+ " 	)"
				+ "		AND B.tp_status = " + contendoMovimento.getValue()
				+ ")";
		}  
		return criterios;
	}
	
	private String searchDtVencimentoInicial(ItemComparator dtInicialVencimento) throws ValidacaoException, Exception {
		String criterios = " ";
		if (dtInicialVencimento != null) {
			criterios = " AND D.dt_protocolo + INTERVAL '1 year' >= '" + dtInicialVencimento.getValue() + "' AND F.nm_fase = 'Pendente'";
		} 
		return criterios;
	}
	
	private String searchDtVencimentoFinal(ItemComparator dtFinalVencimento) throws ValidacaoException, Exception {
		String criterios = " ";
		if (dtFinalVencimento != null) {
			criterios = " AND D.dt_protocolo + INTERVAL '1 year' <= '" + dtFinalVencimento.getValue() + "' AND F.nm_fase = 'Pendente'";
		} 
		return criterios;
	}
	
	private String setCriteriosDinamicos(ItemComparator aitDocumento) {
		String criterios = " ";
		if(aitDocumento != null) {
			criterios = " AND (B.id_ait = '" + aitDocumento.getValue() + "' OR D.nr_documento = '" + aitDocumento.getValue() + "')";
		}
		return criterios;
	}
}
