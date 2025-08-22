package com.tivic.manager.mob.linharota;

import java.sql.Connection;
import java.util.List;

import com.tivic.manager.mob.LinhaRota;
import com.tivic.manager.rest.request.filter.Criterios;

public interface ILinhaRotaRepository {
	public LinhaRota insert(LinhaRota rota) throws Exception;
	public LinhaRota insert(LinhaRota rota, Connection connect) throws Exception;
	public LinhaRota update(LinhaRota rota) throws Exception;
	public LinhaRota update(LinhaRota rota, Connection connect) throws Exception;
	public LinhaRota get(int cdLinha, int cdRota) throws Exception;
	public LinhaRota get(int cdLinha, int cdRota, Connection connect) throws Exception;
	public List<LinhaRota> getAll() throws Exception;
	public List<LinhaRota> getAll(Connection connect) throws Exception;
	public List<LinhaRota> find(Criterios criterios) throws Exception;
	public List<LinhaRota> find(Criterios criterios, Connection connect) throws Exception;
	public LinhaRota delete(int cdLinha, int cdRota) throws Exception;
	public LinhaRota delete(int cdLinha, int cdRota, Connection connect) throws Exception;
}
