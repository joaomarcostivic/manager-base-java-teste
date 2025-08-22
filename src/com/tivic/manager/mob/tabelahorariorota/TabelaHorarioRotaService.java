package com.tivic.manager.mob.tabelahorariorota;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.TabelaHorarioRota;
import com.tivic.manager.mob.TabelaHorarioRotaDAO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ResultSetMapper;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class TabelaHorarioRotaService {

	public void save(TabelaHorarioRota tabelaHorarioRota, Connection connection) throws Exception {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if(TabelaHorarioRotaDAO.get(tabelaHorarioRota.getCdLinha(), tabelaHorarioRota.getCdTabelaHorario(), tabelaHorarioRota.getCdRota(), connection) == null) {
				insert(tabelaHorarioRota, connection);
			}
			else {
				update(tabelaHorarioRota, connection);
			}
			
			if (isConnectionNull)
				connection.commit();
			
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	private void insert(TabelaHorarioRota tabelaHorarioRota, Connection connection) throws Exception {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int code = TabelaHorarioRotaDAO.insert(tabelaHorarioRota, connection);
			if(code <= 0)
				throw new Exception("Erro ao inserir tabela horário rota");
			
			if (isConnectionNull)
				connection.commit();
			
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	

	private void update(TabelaHorarioRota tabelaHorarioRota, Connection connection) throws Exception {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int code = TabelaHorarioRotaDAO.update(tabelaHorarioRota, connection);
			if(code <= 0)
				throw new Exception("Erro ao atualizar tabela horário rota");
			
			if (isConnectionNull)
				connection.commit();
			
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	

	public void delete(TabelaHorarioRota tabelaHorarioRota) throws Exception {
		delete(tabelaHorarioRota, null);
	}
	
	public void delete(TabelaHorarioRota tabelaHorarioRota, Connection connection) throws Exception {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int code = TabelaHorarioRotaDAO.delete(tabelaHorarioRota.getCdLinha(), tabelaHorarioRota.getCdTabelaHorario(), tabelaHorarioRota.getCdRota(), connection);
			if(code <= 0)
				throw new Exception("Erro ao deletar tabela horário rota");
			
			if (isConnectionNull)
				connection.commit();
			
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public List<TabelaHorarioRota> findByHorarioLinha(int cdTabelaHorario, int cdLinha, int tpRota, Connection connection) throws Exception{

		Criterios crt = new Criterios();
		
		crt.add("A.cd_tabela_horario", Integer.toString(cdTabelaHorario), ItemComparator.EQUAL, Types.INTEGER);	
		crt.add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER);
		crt.add("B.tp_rota", Integer.toString(tpRota), ItemComparator.EQUAL, Types.INTEGER);
			
		ResultSetMapper<TabelaHorarioRota> rsm = new ResultSetMapper<TabelaHorarioRota>(Search.find("SELECT * FROM mob_tabela_horario_rota A "+
										"JOIN mob_linha_rota B ON A.cd_linha = B.cd_linha and A.cd_rota = B.cd_rota ", 
				crt, connection!=null ? connection : Conexao.conectar(), connection==null), TabelaHorarioRota.class);
		return rsm.toList();
	}
}
