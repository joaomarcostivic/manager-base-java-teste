package com.tivic.manager.srh;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class HorarioServices {
	public static final int TP_NORMAL = 0;
	public static final int TP_RSR    = 1;
	//
	public static final int TP_NOTURNO = 0;
	public static final int TP_DIURNO  = 1;

	public static Result save(Horario horario){
		return save(horario, null, null);
	}

	public static Result save(Horario horario, AuthData authData){
		return save(horario, authData, null);
	}

	public static Result save(Horario horario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(horario==null)
				return new Result(-1, "Erro ao salvar. Horario é nulo");

			int retorno;
			if(horario.getCdHorario()==0){
				retorno = HorarioDAO.insert(horario, connect);
				horario.setCdHorario(retorno);
			}
			else {
				retorno = HorarioDAO.update(horario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "HORARIO", horario);
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
	public static Result remove(int cdHorario, int cdTabelaHorario){
		return remove(cdHorario, cdTabelaHorario, false, null, null);
	}
	public static Result remove(int cdHorario, int cdTabelaHorario, boolean cascade){
		return remove(cdHorario, cdTabelaHorario, cascade, null, null);
	}
	public static Result remove(int cdHorario, int cdTabelaHorario, boolean cascade, AuthData authData){
		return remove(cdHorario, cdTabelaHorario, cascade, authData, null);
	}
	public static Result remove(int cdHorario, int cdTabelaHorario, boolean cascade, AuthData authData, Connection connect){
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
			retorno = HorarioDAO.delete(cdHorario, cdTabelaHorario, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_horario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_horario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	/**
	 * Busca todos horários de uma determinada Tabela de Horários
	 * 
	 * @author Pádua
	 * @since 13/03/2017
	 */
	public static ResultSetMap getAllHorarioByTabelaHorario(int cdTabelaHorario) {
		return getAllHorarioByTabelaHorario(cdTabelaHorario, null);
	}

	public static ResultSetMap getAllHorarioByTabelaHorario(int cdTabelaHorario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
		
			pstmt = connect.prepareStatement(
					"SELECT A.*, B.nr_dia_semana, B.hr_entrada, B.hr_saida"
					+ " FROM srh_tabela_horario A"
					+ " JOIN srh_horario B ON (A.cd_tabela_horario = B.cd_tabela_horario and B.cd_tabela_horario = "+cdTabelaHorario+")"
			);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.getAllHorarioByTabelaHorario: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.getAllHorarioByTabelaHorario: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
