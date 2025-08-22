package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoEquipamentoServices {
	
	public static final int ST_DESATIVADO   = 0;
	public static final int ST_ATIVADO 		= 1;
	
	public static Result save(TipoEquipamento tipoEquipamento){
		return save(tipoEquipamento, null);
	}
	
	public static Result save(TipoEquipamento tipoEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoEquipamento==null)
				return new Result(-1, "Erro ao salvar. Tipo de equipamento é nulo");
			
			int retorno;
			if(tipoEquipamento.getCdTipoEquipamento()==0){
				retorno = TipoEquipamentoDAO.insert(tipoEquipamento, connect);
				tipoEquipamento.setCdTipoEquipamento(retorno);
			}
			else {
				retorno = TipoEquipamentoDAO.update(tipoEquipamento, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOEQUIPAMENTO", tipoEquipamento);
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
	
	public static Result remove(int cdTipoEquipamento){
		return remove(cdTipoEquipamento, false, null);
	}
	
	public static Result remove(int cdTipoEquipamento, boolean cascade){
		return remove(cdTipoEquipamento, cascade, null);
	}
	
	public static Result remove(int cdTipoEquipamento, boolean cascade, Connection connect){
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
				retorno = TipoEquipamentoDAO.delete(cdTipoEquipamento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de equipamento está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de equipamento excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de equipamento!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_equipamento ORDER BY nm_tipo_equipamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoDAO.getAll: " + e);
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
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		ResultSetMap  _rsm = Search.find("SELECT * FROM acd_tipo_equipamento", "ORDER BY nm_tipo_equipamento" +limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	    return _rsm;
	}
	
	public static TipoEquipamento getById(String idTipoEquipamento) {
		return getById(idTipoEquipamento, null);
	}

	public static TipoEquipamento getById(String idTipoEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_equipamento WHERE id_tipo_equipamento=?");
			pstmt.setString(1, idTipoEquipamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoEquipamento(rs.getInt("cd_tipo_equipamento"),
						rs.getString("nm_tipo_equipamento"),
						rs.getString("id_tipo_equipamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoServices.getById: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoServices.getById: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
