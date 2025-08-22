package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class EquipamentoPessoaServices {
	
	public static final int ST_EM_ANDAMENTO = 0;
	public static final int ST_CONCLUIDO    = 1;
	public static final String[] situacao = {"Em andamento", "Concluído"};

	public static Result save(EquipamentoPessoa equipamentoPessoa){
		return save(equipamentoPessoa, null, null);
	}

	public static Result save(EquipamentoPessoa equipamentoPessoa, AuthData authData){
		return save(equipamentoPessoa, authData, null);
	}

	public static Result save(EquipamentoPessoa equipamentoPessoa, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(equipamentoPessoa==null)
				return new Result(-1, "Erro ao salvar. EquipamentoPessoa é nulo");

			int retorno;
			if(equipamentoPessoa.getCdEquipamentoPessoa()==0){
				retorno = EquipamentoPessoaDAO.insert(equipamentoPessoa, connect);
				equipamentoPessoa.setCdEquipamentoPessoa(retorno);
			}
			else {
				retorno = EquipamentoPessoaDAO.update(equipamentoPessoa, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EQUIPAMENTOPESSOA", equipamentoPessoa);
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
	public static Result remove(EquipamentoPessoa equipamentoPessoa) {
		return remove(equipamentoPessoa.getCdEquipamentoPessoa(), equipamentoPessoa.getCdEquipamento(), equipamentoPessoa.getCdPessoa());
	}
	public static Result remove(int cdEquipamentoPessoa, int cdEquipamento, int cdPessoa){
		return remove(cdEquipamentoPessoa, cdEquipamento, cdPessoa, false, null, null);
	}
	public static Result remove(int cdEquipamentoPessoa, int cdEquipamento, int cdPessoa, boolean cascade){
		return remove(cdEquipamentoPessoa, cdEquipamento, cdPessoa, cascade, null, null);
	}
	public static Result remove(int cdEquipamentoPessoa, int cdEquipamento, int cdPessoa, boolean cascade, AuthData authData){
		return remove(cdEquipamentoPessoa, cdEquipamento, cdPessoa, cascade, authData, null);
	}
	public static Result remove(int cdEquipamentoPessoa, int cdEquipamento, int cdPessoa, boolean cascade, AuthData authData, Connection connect){
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
			retorno = EquipamentoPessoaDAO.delete(cdEquipamentoPessoa, cdEquipamento, cdPessoa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_equipamento_pessoa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoPessoaServices.getAll: " + e);
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
		return Search.find(
				  " SELECT A.*, B.*, "
			    + " C.nm_pessoa "
				+ " FROM grl_equipamento_pessoa A"
				+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
				+ " JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa)", 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getHistoricoEquipamento(int cdEquipamento) {
		return getHistoricoEquipamento(cdEquipamento, null);
	}
	
	public static ResultSetMap getHistoricoEquipamento(int cdEquipamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement ps = connection.prepareStatement(
					" SELECT A.*, "
				  + " B.*,"
				  + " C.nm_pessoa,"
				  + " D.*,"
				  + " D1.nm_tipo_agendamento "
				  + " FROM grl_equipamento_pessoa A"
				  + " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
				  + " JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa)"
				  + " LEFT OUTER JOIN agd_agendamento D ON (A.cd_agendamento = D.cd_agendamento)"
				  + " LEFT OUTER JOIN agd_tipo_agendamento D1 ON (D.cd_tipo_agendamento = D1.cd_tipo_agendamento)"
				  + " WHERE A.cd_equipamento = ? " //pIndex = 1
				  + " ORDER BY A.dt_final DESC");
			ps.setInt(1, cdEquipamento);
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			while(rsm.next()) {
				rsm.setValueToField("DS_DT_INICIAL", Util.formatDate(rsm.getGregorianCalendar("DT_INICIAL"), "dd/MM/yyyy"));
				rsm.setValueToField("DS_DT_FINAL", Util.formatDate(rsm.getGregorianCalendar("DT_FINAL"), "dd/MM/yyyy"));
				rsm.setValueToField("NM_ST_EMPRESTIMO", situacao[rsm.getInt("st_emprestimo")]);
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
