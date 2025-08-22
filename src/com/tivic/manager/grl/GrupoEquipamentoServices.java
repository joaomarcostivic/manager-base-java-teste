package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.seg.AuthData;

public class GrupoEquipamentoServices {

	public static Result save(GrupoEquipamento grupoEquipamento){
		return save(grupoEquipamento, null, null);
	}

	public static Result save(GrupoEquipamento grupoEquipamento, AuthData authData){
		return save(grupoEquipamento, authData, null);
	}

	public static Result save(GrupoEquipamento grupoEquipamento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(grupoEquipamento==null)
				return new Result(-1, "Erro ao salvar. GrupoEquipamento é nulo");

			int retorno;
			if(grupoEquipamento.getCdGrupo()==0){
				retorno = GrupoEquipamentoDAO.insert(grupoEquipamento, connect);
				grupoEquipamento.setCdGrupo(retorno);
			}
			else {
				retorno = GrupoEquipamentoDAO.update(grupoEquipamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "GRUPOEQUIPAMENTO", grupoEquipamento);
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
	public static Result remove(GrupoEquipamento grupoEquipamento) {
		return remove(grupoEquipamento.getCdGrupo());
	}
	public static Result remove(int cdGrupo){
		return remove(cdGrupo, false, null, null);
	}
	public static Result remove(int cdGrupo, boolean cascade){
		return remove(cdGrupo, cascade, null, null);
	}
	public static Result remove(int cdGrupo, boolean cascade, AuthData authData){
		return remove(cdGrupo, cascade, authData, null);
	}
	public static Result remove(int cdGrupo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = GrupoEquipamentoDAO.delete(cdGrupo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_grupo_equipamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllGrupoEquipamentosItem(int cdGrupo) {
		return getAllGrupoEquipamentosItem(cdGrupo, null);
	}
	
	public static ResultSetMap getAllGrupoEquipamentosItem(int cdGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
				"SELECT C.* FROM grl_grupo_equipamento A, grl_grupo_equipamento_item B, " + 
				"grl_equipamento C WHERE A.cd_grupo = B.cd_grupo AND B.cd_equipamento = C.cd_equipamento AND A.cd_grupo = ? AND A.tp_equipamento = ?"
			);
			
			pstmt.setInt(1, cdGrupo);
			pstmt.setInt(2, EquipamentoServices.CAMERA);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoEquipamentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_grupo_equipamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
