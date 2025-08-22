package com.tivic.manager.mob.arquivomovimento;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ArquivoMovimentoService implements IArquivoMovimentoService {

	@Override
	public ArquivoMovimentoDTO getMovimentoPendente(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			ArquivoMovimentoDTO arquivoMovimento = getMovimentoPendente(searchCriterios, customConnection);
			customConnection.finishConnection();
			return arquivoMovimento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public ArquivoMovimentoDTO getMovimentoPendente(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ArquivoMovimentoDTO arquivoMovimentoDTO = new ArquivoMovimentoDTO();
		Search<ArquivoMovimentoDTO> movimentoPendente = new SearchBuilder<ArquivoMovimentoDTO>("mob_arquivo_movimento A")
				.fields("A.*, B.*, C.*, D.*")
				.addJoinTable("JOIN mob_ait_movimento B ON (A.cd_movimento = B.cd_movimento AND A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN mob_ait C ON (A.cd_ait = C.cd_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_erro_retorno D ON (D.nr_erro = B.nr_erro)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		if(!movimentoPendente.getList(ArquivoMovimentoDTO.class).isEmpty()) {
			arquivoMovimentoDTO = movimentoPendente.getList(ArquivoMovimentoDTO.class).get(0);
		}
		return arquivoMovimentoDTO;
	}
}
