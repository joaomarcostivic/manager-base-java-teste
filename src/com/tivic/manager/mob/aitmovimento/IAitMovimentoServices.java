package com.tivic.manager.mob.aitmovimento;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.aitmovimentodocumento.DocumentoProcesso;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAitMovimentoServices {
	public AitMovimento getMovimentoTpStatus(int cdAit, int tpStatus) throws Exception;
	public AitMovimento getMovimentoToSuspensao(String nrProcesso, int cdAit) throws ValidacaoException;
	public AitMovimento save(AitMovimento aitMovimento) throws Exception;
	public AitMovimento save(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public PagedResponse<AitMovimentoDTO> findRemessa(SearchCriterios searchCriterios) throws Exception;
	public List<AitMovimento> getAllDefesas(int cdAit) throws Exception;
	void setDtPublicacaoDO(List<DocumentoProcesso> _movimentos, GregorianCalendar dtPublicacao) throws Exception;
	void setDtPublicacaoDO(List<DocumentoProcesso> _movimentos, GregorianCalendar dtPublicacao, CustomConnection customConnection) throws Exception;
	public void insert(AitMovimento aitMovimento) throws Exception;
	public void insert(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public void update(AitMovimento aitMovimento) throws Exception;
	public void update(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public AitMovimento get(int cdAit, int cdMovimento) throws Exception;
	public AitMovimento get(int cdAit, int cdMovimento, CustomConnection customConnection) throws Exception;
	public List<AitMovimento> getByAit(int cdAit) throws Exception;
	public List<AitMovimento> getByAit(int cdAit, CustomConnection customConnection) throws Exception;
	public List<AitMovimento> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitMovimento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<AitMovimentoDTO> getMovimentos (SearchCriterios searchCriterios) throws Exception;
	public List<AitMovimentoDTO> getMovimentos (SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
