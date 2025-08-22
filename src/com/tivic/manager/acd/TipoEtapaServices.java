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

public class TipoEtapaServices {
	
	public static Result save(TipoEtapa tipoEtapa){
		return save(tipoEtapa, null);
	}
	
	public static Result save(TipoEtapa tipoEtapa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoEtapa==null)
				return new Result(-1, "Erro ao salvar. Tipo de etapa é nulo");
			
			int retorno;
			if(tipoEtapa.getCdEtapa()==0){
				retorno = TipoEtapaDAO.insert(tipoEtapa, connect);
				tipoEtapa.setCdEtapa(retorno);
			}
			else {
				retorno = TipoEtapaDAO.update(tipoEtapa, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOETAPA", tipoEtapa);
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
	
	public static Result remove(int cdTipoEtapa){
		return remove(cdTipoEtapa, false, null);
	}
	
	public static Result remove(int cdTipoEtapa, boolean cascade){
		return remove(cdTipoEtapa, cascade, null);
	}
	
	public static Result remove(int cdTipoEtapa, boolean cascade, Connection connect){
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
				retorno = TipoEtapaDAO.delete(cdTipoEtapa, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de etapa está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de etapa excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de etapa!");
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_etapa AS nm_etapa_posterior "
											+ " FROM acd_tipo_etapa A"
											+ " LEFT OUTER JOIN acd_tipo_etapa B ON (A.cd_etapa_posterior = B.cd_etapa)"
											+ " ORDER BY cd_etapa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaServices.getAll: " + e);
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
		ResultSetMap  _rsm = Search.find("SELECT * FROM acd_tipo_etapa", "ORDER BY nm_etapa" +limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	    return _rsm;
	}
	
	public static TipoEtapa getById(String idEtapa) {
		return getById(idEtapa, null);
	}

	public static TipoEtapa getById(String idEtapa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_etapa WHERE id_etapa=?");
			pstmt.setString(1, idEtapa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoEtapa(rs.getInt("cd_etapa"),
						rs.getString("nm_etapa"),
						rs.getString("id_etapa"),
						rs.getInt("lg_regular"),
						rs.getInt("lg_especial"),
						rs.getInt("lg_eja"),
						rs.getInt("cd_etapa_posterior"),
						rs.getString("sg_tipo_etapa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEtapaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
