package com.tivic.manager.ptc.portal.credencialestacionamento.validations;

import java.util.List;

import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorSolicitacaoPendente implements Validator<CartaoEstacionamentoRequest> {
	
	private ParametroRepositoryDAO parametroRepository;
	
	public ValidatorSolicitacaoPendente() {
		parametroRepository = new ParametroRepositoryDAO();
	}

	@Override
	public void validate(CartaoEstacionamentoRequest documentoCartaoIdosoRequest, CustomConnection customConnection) throws Exception {
		List<Documento> documentos = getSolicitacaoByCpf(documentoCartaoIdosoRequest.getNrCpfRequerente(), customConnection);
		if(!documentos.isEmpty() && documentos.get(0).getCdFase() == parametroRepository.getValorOfParametroAsInt("CD_FASE_PENDENTE", customConnection)) {
			throw new Exception("Não foi possível realizar a solicitação, pois, existe uma solicitação pendente para este CPF.");
		}
	}
	
	private List<Documento> getSolicitacaoByCpf(String nrCpf, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("A.nr_cpf", nrCpf);
		Search<Documento> search = new SearchBuilder<Documento>("grl_pessoa_fisica A")
				.addJoinTable("JOIN ptc_documento_pessoa B ON (A.cd_pessoa = B.cd_pessoa)")
				.addJoinTable("JOIN ptc_documento C ON (B.cd_documento = C.cd_documento)")
				.searchCriterios(searchCriterios)
				.orderBy("C.dt_protocolo DESC")
				.customConnection(customConnection)
				.build();

		return search.getList(Documento.class);
	}

}
