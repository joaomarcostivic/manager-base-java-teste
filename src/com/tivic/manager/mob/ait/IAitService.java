package com.tivic.manager.mob.ait;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.EfeitoSuspensivoDTO;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitDTO;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitSneDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IAitService {
	public Ait insert(Ait ait) throws Exception;
	public Ait insert(Ait ait, CustomConnection customConnection) throws Exception;
	public Ait update(Ait ait) throws Exception;
	public Ait update(Ait ait, CustomConnection customConnection) throws Exception;
	public boolean hasAit(String idAit) throws Exception;
	public boolean hasAit(String idAit, CustomConnection customConnection) throws Exception;
	public Ait updateDetran(Ait ait) throws Exception;
	public Ait updateDetran(Ait ait, CustomConnection customConnection) throws Exception;
	public Ait get(int cdAit) throws Exception;
	public Ait get(int cdAit, CustomConnection customConnection) throws Exception;
	public Ait getById(String idAit) throws Exception;
	public Ait getById(String idAit, CustomConnection customConnection) throws Exception;
	public List<Ait> find(SearchCriterios searchCriterios) throws Exception;
	public List<Ait> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<Ait> getByData(GregorianCalendar dataInicial, GregorianCalendar dataFinal, int limit, int offset) throws Exception;
	public List<Ait> getByData(GregorianCalendar dataInicial, GregorianCalendar dataFinal, int limit, int offset, CustomConnection customConnection) throws Exception;
	public PagedResponse<Ait> findPagedResponse(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<Ait> findPagedResponse(SearchCriterios searchCriterios, CustomConnection customConnection)	throws Exception;
	public Search<EfeitoSuspensivoDTO> findEfeitoSuspensivo(SearchCriterios searchCriterios) throws Exception;
	public Search<EfeitoSuspensivoDTO> findEfeitoSuspensivo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<ServicoDetranDTO> SuspenderInfracao(List<EfeitoSuspensivoDTO> efeitoSuspensivoListDto) throws Exception;
	public Ait save(Ait ait) throws Exception;
	public Ait save(Ait ait, CustomConnection customConnection) throws Exception;
	public PagedResponse<RelatorioAitDTO> filtrarListagemAits(SearchCriterios searchCriterios, boolean lgExcetoCanceladas, Integer tpEquipamento, String radar) throws ValidacaoException, Exception;
	public void cancelarAit(AitMovimento aitMovimento) throws Exception;
	public void cancelarAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public void gerarCancelamento(AitMovimento aitMovimento) throws Exception;
	public void gerarCancelamento(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public void cancelarListaAit(List<AitMovimento> aitMovimentoList) throws Exception;
	public void cancelarListaAit(List<AitMovimento> aitMovimentoList, CustomConnection customConnection) throws Exception;
	public PagedResponse<RelatorioAitSneDTO> filtrarAitsOpcaoSne(SearchCriterios searchCriterios) throws ValidacaoException, Exception;
	public boolean eventoHasAit(int cdEvento) throws Exception;
	public boolean eventoHasAit(int cdEvento, CustomConnection customConnection) throws Exception;
	public void definirComoInconsistente(Ait ait) throws Exception;
	public void reverterConsistencia(Ait ait) throws Exception;
}
