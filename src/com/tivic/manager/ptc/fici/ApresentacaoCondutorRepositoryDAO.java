package com.tivic.manager.ptc.fici;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.util.ResultSetMapper;
import javax.ws.rs.BadRequestException;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ResultSetMap;

public class ApresentacaoCondutorRepositoryDAO implements ApresentacaoCondutorRepository {

	@Override
	public ApresentacaoCondutor insert(ApresentacaoCondutor objeto) throws BadRequestException, Exception {
		return insert(objeto, new CustomConnection());
	}

	public ApresentacaoCondutor insert(ApresentacaoCondutor objeto, CustomConnection customConnection)
			throws BadRequestException, Exception {
		int result = ApresentacaoCondutorDAO.insert(objeto, customConnection.getConnection());

		if (result <= 0) {
			throw new BadRequestException("Erro ao inserir Apresentação de Condutor");
		}

		return objeto;

	}

	@Override
	public ApresentacaoCondutor update(ApresentacaoCondutor objeto) throws BadRequestException, Exception {
		return update(objeto, new CustomConnection());
	}

	@Override
	public ApresentacaoCondutor update(ApresentacaoCondutor objeto, CustomConnection customConnection)
			throws BadRequestException, Exception {
		int result = ApresentacaoCondutorDAO.update(objeto, customConnection.getConnection());

		if (result <= 0) {
			throw new BadRequestException("Erro ao atualizar Apresentação de Condutor");
		}

		return objeto;
	}

	@Override
	public ApresentacaoCondutor delete(ApresentacaoCondutor objeto) throws BadRequestException, Exception {
		return delete(objeto, new CustomConnection());
	}

	public ApresentacaoCondutor delete(ApresentacaoCondutor objeto, CustomConnection customConnection)
			throws BadRequestException, Exception {
		int result = ApresentacaoCondutorDAO.delete(objeto.getCdDocumento(), customConnection.getConnection());

		if (result <= 0) {
			throw new BadRequestException("Erro ao deletar Apresentação de Condutor");
		}

		return objeto;
	}

	@Override
	public ApresentacaoCondutor get(int cdApresentacaoCondutor) throws BadRequestException, Exception {
		return get(cdApresentacaoCondutor, new CustomConnection());
	}

	public ApresentacaoCondutor get(int cdApresentacaoCondutor, CustomConnection customConnection)
			throws BadRequestException, Exception {
		ApresentacaoCondutor apresentacaoCondutor = ApresentacaoCondutorDAO.get(cdApresentacaoCondutor,
				customConnection.getConnection());
		
		if (apresentacaoCondutor == null) {
			throw new BadRequestException("Nenhuma Apresentação de Condutor encontrada");
		}

		return apresentacaoCondutor;
	}

	@Override
	public List<ApresentacaoCondutor> getAll() throws BadRequestException, Exception {
		return getAll(new CustomConnection());
	}

	public List<ApresentacaoCondutor> getAll(CustomConnection customConnection) throws BadRequestException, Exception {
		ResultSetMap rsm = ApresentacaoCondutorDAO.getAll(customConnection.getConnection());

		if (rsm.next()) {
			List<ApresentacaoCondutor> apresentacaoCondutor = new ResultSetMapper<ApresentacaoCondutor>(rsm,
					ApresentacaoCondutor.class).toList();
			return apresentacaoCondutor;
		} else {
			throw new BadRequestException("Nenhuma Apresentação de Condutor encontrada");
		}
	}

	@Override
	public List<ApresentacaoCondutor> find(SearchCriterios criterios) throws BadRequestException, Exception {
		return find(criterios, new CustomConnection());
	}

	public List<ApresentacaoCondutor> find(SearchCriterios criterios, CustomConnection customConnection) throws BadRequestException, Exception {
		Search<ApresentacaoCondutor> search = new SearchBuilder<ApresentacaoCondutor>("ptc_apresentacao_condutor").searchCriterios(criterios).build();
		return search.getList(ApresentacaoCondutor.class);
	}

}
