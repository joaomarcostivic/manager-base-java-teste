package com.tivic.manager.mob.ait.aitArquivo;

import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitArquivoRepositoryDAO implements IAitArquivoRepository {

	@Override
	public AitArquivo insert(AitArquivo AitArquivo, CustomConnection connection) throws Exception {
		int cdArquivo = AitArquivoDAO.insert(AitArquivo, connection.getConnection());
		if (cdArquivo <= 0) {
			throw new ValidacaoException("Erro ao inserir relação Ait Arquivo.");
		}
		AitArquivo.setCdArquivo(cdArquivo);
		return AitArquivo;
	}
	
	@Override
	public void update(AitArquivo AitArquivo, CustomConnection customConnection) throws Exception {
		int result = AitArquivoDAO.update(AitArquivo, customConnection.getConnection());
		if(result < 0)
			throw new ValidacaoException("Erro ao atualizar relacação Ait Arquivo.");
	}
	
	@Override
	public void delete(int cdArquivo, CustomConnection connection) throws Exception {
		int result = AitArquivoDAO.delete(cdArquivo, connection.getConnection());
		if (result <= 0) {
			throw new ValidacaoException("Erro ao deletar uma relação de Ait Arquivo.");
		}
	}
	
	@Override
	public AitArquivo get(int cdArquivo) throws Exception {
		return get(cdArquivo, new CustomConnection());
	}

	@Override
	public AitArquivo get(int cdArquivo, CustomConnection customConnection) throws Exception{
		return AitArquivoDAO.get(cdArquivo, customConnection.getConnection());
	}
	
	@Override
	public List<AitArquivo> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitArquivo> search = new SearchBuilder<AitArquivo>("mob_ait_arquivo")
			.searchCriterios(searchCriterios)
			.customConnection(customConnection)
			.orderBy("cd_arquivo")
			.build();
		return search.getList(AitArquivo.class);
	}
	
	@Override
	public Search<TipoArquivoDTO> findArquivosAit(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<TipoArquivoDTO> aitArquivos = findArquivosAit(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitArquivos;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<TipoArquivoDTO> findArquivosAit(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<TipoArquivoDTO> search = new SearchBuilder<TipoArquivoDTO>("mob_ait_arquivo A")
				.fields("B.cd_arquivo, B.nm_documento, B.nm_arquivo, B.ds_arquivo, C.nm_tipo_arquivo")
				.addJoinTable("JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo)")
				.addJoinTable("LEFT OUTER JOIN grl_tipo_arquivo C ON (B.cd_tipo_arquivo = C.cd_tipo_arquivo)")
        		.searchCriterios(searchCriterios)
				.orderBy(" A.cd_arquivo DESC ")
				.count()
				.customConnection(customConnection)
				.build();
		return search;	
	}

}
