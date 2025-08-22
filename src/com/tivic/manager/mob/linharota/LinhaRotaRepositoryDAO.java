package com.tivic.manager.mob.linharota;

import java.sql.Connection;
import java.util.List;

import com.tivic.manager.fta.Rota;
import com.tivic.manager.mob.LinhaRota;
import com.tivic.manager.mob.LinhaRotaDAO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ResultSetMapper;

import sol.dao.ResultSetMap;

public class LinhaRotaRepositoryDAO implements ILinhaRotaRepository {

	@Override
	public LinhaRota insert(LinhaRota linhaRota) throws Exception {
		return insert(linhaRota, null);
	}

	@Override
	public LinhaRota insert(LinhaRota linhaRota, Connection connect) throws Exception {
		int cdLinhaRota = LinhaRotaDAO.insert(linhaRota, connect);
		if(cdLinhaRota < 0)
			throw new Exception("Erro ao inserir linhaRota");
		return linhaRota;
	}

	@Override
	public LinhaRota update(LinhaRota linhaRota) throws Exception {
		return update(linhaRota, null);
	}

	@Override
	public LinhaRota update(LinhaRota linhaRota, Connection connect) throws Exception {
		int retorno = LinhaRotaDAO.update(linhaRota, connect);
		if(retorno < 0)
			throw new Exception("Erro ao atualizar linhaRota");
		return linhaRota;
	}

	@Override
	public LinhaRota get(int cdLinha, int cdRota) throws Exception {
		return get(cdLinha, cdRota, null);
	}

	@Override
	public LinhaRota get(int cdLinha, int cdRota, Connection connect) throws Exception {
		return LinhaRotaDAO.get(cdLinha, cdRota, connect);
	}

	@Override
	public List<LinhaRota> getAll() throws Exception {
		return getAll(null);
	}

	@Override
	public List<LinhaRota> getAll(Connection connect) throws Exception {
		ResultSetMap rsm = LinhaRotaDAO.getAll(connect);
		return new ResultSetMapper<LinhaRota>(rsm, LinhaRota.class).toList();
	}

	@Override
	public List<LinhaRota> find(Criterios criterios) throws Exception {
		return find(null);
	}

	@Override
	public List<LinhaRota> find(Criterios criterios, Connection connect) throws Exception {
		ResultSetMap rsm = LinhaRotaDAO.find(criterios, connect);
		return new ResultSetMapper<LinhaRota>(rsm, LinhaRota.class).toList();
	}

	@Override
	public LinhaRota delete(int cdLinha, int cdRota) throws Exception {
		return delete(cdLinha, cdRota, null);
	}

	@Override
	public LinhaRota delete(int cdLinha, int cdRota, Connection connect) throws Exception {
		LinhaRota linhaRota = get(cdLinha, cdRota);
		int retorno = LinhaRotaDAO.delete(cdLinha, cdRota, connect);
		if(retorno <= 0)
			throw new Exception("Erro ao remover linhaRota");
		return linhaRota;
	}

}
