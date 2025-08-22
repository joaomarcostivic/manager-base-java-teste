package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AtendimentoNecessidadeDAO{

	public static int insert(AtendimentoNecessidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(AtendimentoNecessidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_atendimento_necessidade (cd_atendimento,"+
			                                  "cd_tipo_necessidade,"+
			                                  "vl_necessidade) VALUES (?, ?, ?)");
			if(objeto.getCdAtendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAtendimento());
			if(objeto.getCdTipoNecessidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoNecessidade());
			pstmt.setString(3,objeto.getVlNecessidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AtendimentoNecessidade objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AtendimentoNecessidade objeto, int cdAtendimentoOld, int cdTipoNecessidadeOld) {
		return update(objeto, cdAtendimentoOld, cdTipoNecessidadeOld, null);
	}

	public static int update(AtendimentoNecessidade objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AtendimentoNecessidade objeto, int cdAtendimentoOld, int cdTipoNecessidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_atendimento_necessidade SET cd_atendimento=?,"+
												      		   "cd_tipo_necessidade=?,"+
												      		   "vl_necessidade=? WHERE cd_atendimento=? AND cd_tipo_necessidade=?");
			pstmt.setInt(1,objeto.getCdAtendimento());
			pstmt.setInt(2,objeto.getCdTipoNecessidade());
			pstmt.setString(3,objeto.getVlNecessidade());
			pstmt.setInt(4, cdAtendimentoOld!=0 ? cdAtendimentoOld : objeto.getCdAtendimento());
			pstmt.setInt(5, cdTipoNecessidadeOld!=0 ? cdTipoNecessidadeOld : objeto.getCdTipoNecessidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtendimento, int cdTipoNecessidade) {
		return delete(cdAtendimento, cdTipoNecessidade, null);
	}

	public static int delete(int cdAtendimento, int cdTipoNecessidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_atendimento_necessidade WHERE cd_atendimento=? AND cd_tipo_necessidade=?");
			pstmt.setInt(1, cdAtendimento);
			pstmt.setInt(2, cdTipoNecessidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AtendimentoNecessidade get(int cdAtendimento, int cdTipoNecessidade) {
		return get(cdAtendimento, cdTipoNecessidade, null);
	}

	public static AtendimentoNecessidade get(int cdAtendimento, int cdTipoNecessidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_atendimento_necessidade WHERE cd_atendimento=? AND cd_tipo_necessidade=?");
			pstmt.setInt(1, cdAtendimento);
			pstmt.setInt(2, cdTipoNecessidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AtendimentoNecessidade(rs.getInt("cd_atendimento"),
						rs.getInt("cd_tipo_necessidade"),
						rs.getString("vl_necessidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_atendimento_necessidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoNecessidadeDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_atendimento_necessidade", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
