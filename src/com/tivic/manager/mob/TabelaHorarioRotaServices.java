package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TabelaHorarioRotaServices {

	public static Result save(TabelaHorarioRota tabelaHorarioRota){
		return save(tabelaHorarioRota, null, null);
	}

	public static Result save(TabelaHorarioRota tabelaHorarioRota, AuthData authData){
		return save(tabelaHorarioRota, authData, null);
	}

	public static Result save(TabelaHorarioRota tabelaHorarioRota, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tabelaHorarioRota==null)
				return new Result(-1, "Erro ao salvar. TabelaHorarioRota é nulo");

			int retorno;
			TabelaHorarioRota thr = TabelaHorarioRotaDAO.get(tabelaHorarioRota.getCdLinha(), tabelaHorarioRota.getCdTabelaHorario(), tabelaHorarioRota.getCdRota(), connect);
			if(thr==null){
				retorno = TabelaHorarioRotaDAO.insert(tabelaHorarioRota, connect);
			}
			else {
				retorno = TabelaHorarioRotaDAO.update(tabelaHorarioRota, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TABELAHORARIOROTA", tabelaHorarioRota);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(TabelaHorarioRota tabelaHorarioRota) {
		return remove(tabelaHorarioRota.getCdLinha(), tabelaHorarioRota.getCdTabelaHorario(), tabelaHorarioRota.getCdRota());
	}
	public static Result remove(int cdLinha, int cdTabelaHorario, int cdRota){
		return remove(cdLinha, cdTabelaHorario, cdRota, false, null, null);
	}
	public static Result remove(int cdLinha, int cdTabelaHorario, int cdRota, boolean cascade){
		return remove(cdLinha, cdTabelaHorario, cdRota, cascade, null, null);
	}
	public static Result remove(int cdLinha, int cdTabelaHorario, int cdRota, boolean cascade, AuthData authData){
		return remove(cdLinha, cdTabelaHorario, cdRota, cascade, authData, null);
	}
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}
	
	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM mob_tabela_horario_rota";
			
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT * FROM mob_tabela_horario_rota";
			
			pstmt = connect.prepareStatement(sql);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdLinha, int cdTabelaHorario, int cdRota, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = TabelaHorarioRotaDAO.delete(cdLinha, cdTabelaHorario, cdRota, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tabela_horario_rota");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getHorarios (int cdTabelaHorario, int cdLinha, int cdRota) {
		return getHorarios(cdTabelaHorario, cdLinha, cdRota, null);
	}
	public static ResultSetMap getHorarios (int cdTabelaHorario, int cdLinha, int cdRota, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String, Object> register = new HashMap<String, Object>();
			ResultSetMap rsmHorariosOrdenados = new ResultSetMap();
			
			pstmt = connect.prepareStatement("SELECT cd_horario FROM mob_horario WHERE cd_tabela_horario = " + cdTabelaHorario + 
																						" AND cd_linha = " + cdLinha + 
																						" AND cd_rota = " + cdRota + 
																						" GROUP BY cd_horario");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while (rsm.next()) {
				pstmt = connect.prepareStatement("SELECT * FROM mob_horario WHERE cd_horario = " + rsm.getInt("CD_HORARIO") + 
																			" AND cd_tabela_horario = " + cdTabelaHorario + 
																			" AND cd_linha = " + cdLinha + 
																			" AND cd_rota = " + cdRota + 
																			" ORDER BY cd_horario ");
				
				ResultSetMap rsmHorarios = new ResultSetMap(pstmt.executeQuery());
				
				register = new HashMap<String, Object>();
				while (rsmHorarios.next()) {
					register.put("CD_LINHA", rsmHorarios.getInt("CD_LINHA"));
					register.put("CD_ROTA", rsmHorarios.getInt("CD_ROTA"));
					register.put("CD_TABELA_HORARIO", rsmHorarios.getInt("CD_TABELA_HORARIO"));
					register.put("CD_HORARIO", rsm.getInt("CD_HORARIO"));
					register.put("CD_VARIACAO", rsmHorarios.getInt("CD_VARIACAO"));
					register.put("HR_TRECHO_" + rsmHorarios.getInt("CD_TRECHO"), Util.formatDate(rsmHorarios.getGregorianCalendar("HR_PARTIDA"), "HH:mm"));
				}
				
				if (rsmHorarios.getLines().size()>0) {
					rsmHorariosOrdenados.addRegister(register);
				}
			}
			return rsmHorariosOrdenados;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaServices.getHorarios: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaServices.getHorarios: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTabelaLinha(int cdLinha, int cdTabelaHoraio) {
		return getAllByTabelaLinha(cdLinha, cdTabelaHoraio, null);
	}

	public static ResultSetMap getAllByTabelaLinha(int cdLinha, int cdTabelaHoraio, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
					pstmt = connect.prepareStatement("SELECT A.*, B.* FROM mob_tabela_horario_rota A"
													+ " JOIN mob_linha_rota B ON (A.cd_linha = B.cd_linha AND A.cd_rota = B.cd_rota)"
													+ " WHERE A.cd_linha = " + cdLinha + " AND A.cd_tabela_horario = " + cdTabelaHoraio);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaServices.getAllByTabelaLinha: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaServices.getAllByTabelaLinha: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_rota, B.tp_rota, C.nm_tabela_horario, C.tp_tabela_horario, D.nr_linha FROM mob_tabela_horario_rota A"
						+ "	JOIN mob_linha_rota B ON (A.cd_linha = B.cd_linha AND A.cd_rota = B.cd_rota)"
						+ " JOIN mob_tabela_horario C ON (A.cd_linha = C.cd_linha AND A.cd_tabela_horario = C.cd_tabela_horario)"
						+ " LEFT OUTER JOIN mob_linha D ON (A.cd_linha = D.cd_linha)", 
																					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result saveIdaEVolta(TabelaHorarioRota tabelaHorarioRotaIda, TabelaHorarioRota tabelaHorarioRotaVolta){
		return saveIdaEVolta(tabelaHorarioRotaIda,tabelaHorarioRotaVolta, null, null);
	}

	public static Result saveIdaEVolta(TabelaHorarioRota tabelaHorarioRotaIda, TabelaHorarioRota tabelaHorarioRotaVolta, AuthData authData){
		return saveIdaEVolta(tabelaHorarioRotaIda, tabelaHorarioRotaVolta, authData, null);
	}
	
	public static Result saveIdaEVolta(TabelaHorarioRota tabelaHorarioRotaIda, TabelaHorarioRota tabelaHorarioRotaVolta, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tabelaHorarioRotaIda==null || tabelaHorarioRotaVolta==null)
				return new Result(-1, "Erro ao salvar. TabelaHorarioRota é nulo");

			int retornoIda;
			int retornoVolta;
			
			TabelaHorarioRota thrI = TabelaHorarioRotaDAO.get(tabelaHorarioRotaIda.getCdLinha(), tabelaHorarioRotaIda.getCdTabelaHorario(), tabelaHorarioRotaIda.getCdRota(), connect);
			TabelaHorarioRota thrV = TabelaHorarioRotaDAO.get(tabelaHorarioRotaVolta.getCdLinha(), tabelaHorarioRotaVolta.getCdTabelaHorario(), tabelaHorarioRotaVolta.getCdRota(), connect);
			
			if(thrI==null){
				retornoIda = TabelaHorarioRotaDAO.insert(tabelaHorarioRotaIda, connect);
			}
			else {
				retornoIda = TabelaHorarioRotaDAO.update(tabelaHorarioRotaIda, connect);
			}
			
			if(thrV==null){
				retornoVolta = TabelaHorarioRotaDAO.insert(tabelaHorarioRotaVolta, connect);
			}
			else {
				retornoVolta = TabelaHorarioRotaDAO.update(tabelaHorarioRotaVolta, connect);
			}

			if(retornoVolta<=0 && retornoIda<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			ArrayList arr = new ArrayList();
			arr.add(thrI);
			arr.add(thrV);
			
			return new Result(retornoIda, (retornoVolta<=0 && retornoIda<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TABELAHORARIOROTA", arr);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveTabelaHorarioRotaIdaEVolta(TabelaHorarioRota tabelaHorarioRotaIda, TabelaHorarioRota tabelaHorarioRotaVolta){
		return saveTabelaHorarioRotaIdaEVolta(tabelaHorarioRotaIda, tabelaHorarioRotaVolta, null, null);
	}

	public static Result saveTabelaHorarioRotaIdaEVolta(TabelaHorarioRota tabelaHorarioRotaIda, TabelaHorarioRota tabelaHorarioRotaVolta,  AuthData authData){
		return saveTabelaHorarioRotaIdaEVolta(tabelaHorarioRotaIda, tabelaHorarioRotaVolta, authData, null);
	}
	
	public static Result saveTabelaHorarioRotaIdaEVolta(TabelaHorarioRota tabelaHorarioRotaIda, TabelaHorarioRota tabelaHorarioRotaVolta, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Criterios crt = new Criterios();
			
			crt.add("A.cd_tabela_horario", Integer.toString(tabelaHorarioRotaIda.getCdTabelaHorario()), ItemComparator.EQUAL, Types.INTEGER);	
			crt.add("A.cd_linha", Integer.toString(tabelaHorarioRotaIda.getCdLinha()), ItemComparator.EQUAL, Types.INTEGER);	
				
			ResultSetMap rsm =  Search.find("SELECT * FROM mob_tabela_horario_rota A "+
											"JOIN mob_linha_rota B ON A.cd_linha = B.cd_linha and A.cd_rota = B.cd_rota ", 
					crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			Result lgSave;
			
			rsm.beforeFirst();
			
			while(rsm.next()) {
				
				
				TabelaHorarioRota thr = new TabelaHorarioRota(rsm.getInt("CD_LINHA"),rsm.getInt("CD_TABELA_HORARIO"), rsm.getInt("CD_ROTA"), rsm.getInt("CD_VARIACAO"));
				
				if(rsm.getInt("TP_ROTA") == LinhaRotaServices.TP_IDA) {
					if(thr.getCdRota() != tabelaHorarioRotaIda.getCdRota()) {
						
						if(HorarioServices.validaExistenciaHorarios(rsm.getInt("CD_TABELA_HORARIO"), rsm.getInt("CD_LINHA"), rsm.getInt("CD_ROTA"))) {
							return new Result(-1, "Não é possível alterar as rotas com horários cadastrados");
						}
						
						Result resp = remove(thr);
						if(resp.getCode() < 0) 
							return resp;
						
						lgSave = save(tabelaHorarioRotaIda);
						if(lgSave.getCode() < 0)
							return lgSave;
					}
				}else if(rsm.getInt("TP_ROTA") == LinhaRotaServices.TP_VOLTA) {
					if(thr.getCdRota() != tabelaHorarioRotaVolta.getCdRota()) {
						
						if(HorarioServices.validaExistenciaHorarios(rsm.getInt("CD_TABELA_HORARIO"), rsm.getInt("CD_LINHA"), rsm.getInt("CD_ROTA"))) {
							return new Result(-1, "Não é possível alterar as rotas com horários cadastrados");
						}
						
						Result resp = remove(thr);
						if(resp.getCode() < 0) 
							return resp;
						
						lgSave = save(tabelaHorarioRotaVolta);
						if(lgSave.getCode() < 0)
							return lgSave;
					}
				}
									
			}

			return new Result(1, "Tabela Atualizada com Sucesso");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}