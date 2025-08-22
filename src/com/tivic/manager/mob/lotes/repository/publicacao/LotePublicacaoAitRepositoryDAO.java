package com.tivic.manager.mob.lotes.repository.publicacao;

import java.util.List;

import com.tivic.manager.mob.lotes.dao.publicacao.LotePublicacaoAitDAO;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacaoAit;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class LotePublicacaoAitRepositoryDAO implements ILotePublicacaoAitRepository {

    @Override
    public void insert(LotePublicacaoAit lotePublicacao, CustomConnection customConnection) throws Exception {
        int cdLotePublicacao = LotePublicacaoAitDAO.insert(lotePublicacao, customConnection.getConnection());
        if (cdLotePublicacao <= 0)
            throw new Exception("Erro ao inserir LotePublicacaoAit.");
        lotePublicacao.setCdLotePublicacao(cdLotePublicacao);
    }

    @Override
    public void update(LotePublicacaoAit lotePublicacao, CustomConnection customConnection) throws Exception {
        LotePublicacaoAitDAO.update(lotePublicacao, customConnection.getConnection());
    }

    @Override
    public LotePublicacaoAit delete(LotePublicacaoAit lotePublicacaoAit, CustomConnection customConnection) throws Exception {
        int codigoRetorno = LotePublicacaoAitDAO.delete(lotePublicacaoAit.getCdLotePublicacao(), lotePublicacaoAit.getCdAit(), customConnection.getConnection());
        if (codigoRetorno <= 0)
            throw new ValidacaoException("Erro ao deletar AIT do lote");
        return lotePublicacaoAit;
    }

    @Override
    public LotePublicacaoAit get(int cdLotePublicacao, int cdAit, CustomConnection customConnection) throws Exception {
        return LotePublicacaoAitDAO.get(cdLotePublicacao, cdAit, customConnection.getConnection());
    }

    @Override
    public List<LotePublicacaoAit> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
        Search<LotePublicacaoAit> search = new SearchBuilder<LotePublicacaoAit>("mob_lote_publicacao_ait")
                .searchCriterios(searchCriterios)
                .customConnection(customConnection)
                .build();
        return search.getList(LotePublicacaoAit.class);
    }

    @Override
    public List<LotePublicacaoAit> findByCdLotePublicacao(int cdLotePublicacao) throws Exception {
        SearchCriterios searchCriterios = new SearchCriterios();
        searchCriterios.addCriteriosEqualInteger("A.cd_lote_publicacao", cdLotePublicacao);
        Search<LotePublicacaoAit> search = new SearchBuilder<LotePublicacaoAit>("mob_lote_publicacao_ait A")
                .searchCriterios(searchCriterios)
                .build();
        return search.getList(LotePublicacaoAit.class);
    }
}
