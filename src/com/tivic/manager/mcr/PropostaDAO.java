package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PropostaDAO{

	public static int insert(Proposta objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Proposta objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_proposta");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_ata_comite");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAtaComite()));
			int code = Conexao.getSequenceCode("mcr_proposta", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProposta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mcr_proposta (cd_proposta,"+
			                                  "cd_ata_comite,"+
			                                  "dt_liberacao,"+
			                                  "nr_parcelas,"+
			                                  "vl_parcelas) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAtaComite()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAtaComite());
			if(objeto.getDtLiberacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtLiberacao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getNrParcelas());
			pstmt.setFloat(5,objeto.getVlParcelas());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Proposta objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Proposta objeto, int cdPropostaOld, int cdAtaComiteOld) {
		return update(objeto, cdPropostaOld, cdAtaComiteOld, null);
	}

	public static int update(Proposta objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Proposta objeto, int cdPropostaOld, int cdAtaComiteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mcr_proposta SET cd_proposta=?,"+
												      		   "cd_ata_comite=?,"+
												      		   "dt_liberacao=?,"+
												      		   "nr_parcelas=?,"+
												      		   "vl_parcelas=? WHERE cd_proposta=? AND cd_ata_comite=?");
			pstmt.setInt(1,objeto.getCdProposta());
			pstmt.setInt(2,objeto.getCdAtaComite());
			if(objeto.getDtLiberacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtLiberacao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getNrParcelas());
			pstmt.setFloat(5,objeto.getVlParcelas());
			pstmt.setInt(6, cdPropostaOld!=0 ? cdPropostaOld : objeto.getCdProposta());
			pstmt.setInt(7, cdAtaComiteOld!=0 ? cdAtaComiteOld : objeto.getCdAtaComite());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProposta, int cdAtaComite) {
		return delete(cdProposta, cdAtaComite, null);
	}

	public static int delete(int cdProposta, int cdAtaComite, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mcr_proposta WHERE cd_proposta=? AND cd_ata_comite=?");
			pstmt.setInt(1, cdProposta);
			pstmt.setInt(2, cdAtaComite);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Proposta get(int cdProposta, int cdAtaComite) {
		return get(cdProposta, cdAtaComite, null);
	}

	public static Proposta get(int cdProposta, int cdAtaComite, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_proposta WHERE cd_proposta=? AND cd_ata_comite=?");
			pstmt.setInt(1, cdProposta);
			pstmt.setInt(2, cdAtaComite);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Proposta(rs.getInt("cd_proposta"),
						rs.getInt("cd_ata_comite"),
						(rs.getTimestamp("dt_liberacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_liberacao").getTime()),
						rs.getInt("nr_parcelas"),
						rs.getFloat("vl_parcelas"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_proposta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PropostaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_proposta", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
