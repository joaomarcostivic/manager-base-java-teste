package com.tivic.manager.mob.lotes.repository.publicacao;

import java.util.List;

import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacaoAit;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ILotePublicacaoAitRepository {
    public void insert(LotePublicacaoAit lotePublicacaoAit, CustomConnection customConnection) throws Exception;
    public void update(LotePublicacaoAit lotePublicacaoAit, CustomConnection customConnection) throws Exception;
    public LotePublicacaoAit delete(LotePublicacaoAit lotePublicacaoAit, CustomConnection customConnection) throws Exception;
    public LotePublicacaoAit get(int cdLotePublicacao, int cdAit, CustomConnection customConnection) throws Exception;
    public List<LotePublicacaoAit> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
    public List<LotePublicacaoAit> findByCdLotePublicacao(int cdLotePublicacao) throws Exception;
}
