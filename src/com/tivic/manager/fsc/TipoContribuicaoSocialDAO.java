package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoContribuicaoSocialDAO{

	public static int insert(TipoContribuicaoSocial objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoContribuicaoSocial objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_tipo_contribuicao_social", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoContribuicaoSocial(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_tipo_contribuicao_social (cd_tipo_contribuicao_social,"+
			                                  "nm_tipo_contribuicao_social,"+
			                                  "nr_tipo_contribuicao_social) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoContribuicaoSocial());
			pstmt.setString(3,objeto.getNrTipoContribuicaoSocial());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoContribuicaoSocial objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoContribuicaoSocial objeto, int cdTipoContribuicaoSocialOld) {
		return update(objeto, cdTipoContribuicaoSocialOld, null);
	}

	public static int update(TipoContribuicaoSocial objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoContribuicaoSocial objeto, int cdTipoContribuicaoSocialOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_tipo_contribuicao_social SET cd_tipo_contribuicao_social=?,"+
												      		   "nm_tipo_contribuicao_social=?,"+
												      		   "nr_tipo_contribuicao_social=? WHERE cd_tipo_contribuicao_social=?");
			pstmt.setInt(1,objeto.getCdTipoContribuicaoSocial());
			pstmt.setString(2,objeto.getNmTipoContribuicaoSocial());
			pstmt.setString(3,objeto.getNrTipoContribuicaoSocial());
			pstmt.setInt(4, cdTipoContribuicaoSocialOld!=0 ? cdTipoContribuicaoSocialOld : objeto.getCdTipoContribuicaoSocial());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoContribuicaoSocial) {
		return delete(cdTipoContribuicaoSocial, null);
	}

	public static int delete(int cdTipoContribuicaoSocial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_tipo_contribuicao_social WHERE cd_tipo_contribuicao_social=?");
			pstmt.setInt(1, cdTipoContribuicaoSocial);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoContribuicaoSocial get(int cdTipoContribuicaoSocial) {
		return get(cdTipoContribuicaoSocial, null);
	}

	public static TipoContribuicaoSocial get(int cdTipoContribuicaoSocial, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_contribuicao_social WHERE cd_tipo_contribuicao_social=?");
			pstmt.setInt(1, cdTipoContribuicaoSocial);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoContribuicaoSocial(rs.getInt("cd_tipo_contribuicao_social"),
						rs.getString("nm_tipo_contribuicao_social"),
						rs.getString("nr_tipo_contribuicao_social"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_contribuicao_social ORDER BY CAST(nr_tipo_contribuicao_social AS INTEGER)");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoContribuicaoSocialServices.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_tipo_contribuicao_social", " ORDER BY CAST(nr_tipo_contribuicao_social AS INTEGER) ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
