package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoProcessoDAO{

	public static int insert(TipoProcesso objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoProcesso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_tipo_processo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdTipoProcesso()<=0)
				objeto.setCdTipoProcesso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_tipo_processo (cd_tipo_processo,"+
			                                  "cd_area_direito,"+
			                                  "nm_tipo_processo,"+
			                                  "nm_parte,"+
			                                  "tp_contra_parte,"+
			                                  "nm_contra_parte,"+
			                                  "nm_outro_interessado,"+
			                                  "lg_segredo_justica,"+
			                                  "tp_site_busca) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdTipoProcesso());
			if(objeto.getCdAreaDireito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAreaDireito());
			pstmt.setString(3,objeto.getNmTipoProcesso());
			pstmt.setString(4,objeto.getNmParte());
			pstmt.setInt(5,objeto.getTpContraParte());
			pstmt.setString(6,objeto.getNmContraParte());
			pstmt.setString(7,objeto.getNmOutroInteressado());
			pstmt.setInt(8,objeto.getLgSegredoJustica());
			pstmt.setInt(9,objeto.getTpSiteBusca());
			pstmt.executeUpdate();
			return objeto.getCdTipoProcesso();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoProcesso objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoProcesso objeto, int cdTipoProcessoOld) {
		return update(objeto, cdTipoProcessoOld, null);
	}

	public static int update(TipoProcesso objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoProcesso objeto, int cdTipoProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_tipo_processo SET cd_tipo_processo=?,"+
												      		   "cd_area_direito=?,"+
												      		   "nm_tipo_processo=?,"+
												      		   "nm_parte=?,"+
												      		   "tp_contra_parte=?,"+
												      		   "nm_contra_parte=?,"+
												      		   "nm_outro_interessado=?,"+
												      		   "lg_segredo_justica=?,"+
												      		   "tp_site_busca=? WHERE cd_tipo_processo=?");
			pstmt.setInt(1,objeto.getCdTipoProcesso());
			if(objeto.getCdAreaDireito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAreaDireito());
			pstmt.setString(3,objeto.getNmTipoProcesso());
			pstmt.setString(4,objeto.getNmParte());
			pstmt.setInt(5,objeto.getTpContraParte());
			pstmt.setString(6,objeto.getNmContraParte());
			pstmt.setString(7,objeto.getNmOutroInteressado());
			pstmt.setInt(8,objeto.getLgSegredoJustica());
			pstmt.setInt(9,objeto.getTpSiteBusca());
			pstmt.setInt(10, cdTipoProcessoOld!=0 ? cdTipoProcessoOld : objeto.getCdTipoProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoProcesso) {
		return delete(cdTipoProcesso, null);
	}

	public static int delete(int cdTipoProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_tipo_processo WHERE cd_tipo_processo=?");
			pstmt.setInt(1, cdTipoProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoProcesso get(int cdTipoProcesso) {
		return get(cdTipoProcesso, null);
	}

	public static TipoProcesso get(int cdTipoProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_processo WHERE cd_tipo_processo=?");
			pstmt.setInt(1, cdTipoProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoProcesso(rs.getInt("cd_tipo_processo"),
						rs.getInt("cd_area_direito"),
						rs.getString("nm_tipo_processo"),
						rs.getString("nm_parte"),
						rs.getInt("tp_contra_parte"),
						rs.getString("nm_contra_parte"),
						rs.getString("nm_outro_interessado"),
						rs.getInt("lg_segredo_justica"),
						rs.getInt("tp_site_busca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_processo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_tipo_processo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
