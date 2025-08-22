package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class AtaDAO{

	public static int insert(Ata objeto) {
		return insert(objeto, null);
	}

	public static int insert(Ata objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ptc_ata", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_ata (cd_ata,"+
			                                  "cd_usuario,"+
			                                  "dt_cadastro,"+
			                                  "dt_ata,"+
			                                  "id_ata) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getCdUsuario());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtAta()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAta().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdAta());
			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Ata objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Ata objeto, int cdAtaOld) {
		return update(objeto, cdAtaOld, null);
	}

	public static int update(Ata objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Ata objeto, int cdAtaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_ata SET cd_ata=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_cadastro=?,"+
												      		   "dt_ata=?,"+
												      		   "id_ata=? WHERE cd_ata=?");
			pstmt.setInt(1,objeto.getCdAta());
			pstmt.setInt(2,objeto.getCdUsuario());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtAta()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAta().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdAta());
			
			pstmt.setInt(7, cdAtaOld!=0 ? cdAtaOld : objeto.getCdAta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAta) {
		return delete(cdAta, null);
	}

	public static int delete(int cdAta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_ata WHERE cd_ata=?");
			pstmt.setInt(1, cdAta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Ata get(int cdAta) {
		return get(cdAta, null);
	}

	public static Ata get(int cdAta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_ata WHERE cd_ata=?");
			pstmt.setInt(1, cdAta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Ata(rs.getInt("cd_ata"),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						(rs.getTimestamp("dt_ata")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ata").getTime()),
						rs.getString("id_ata"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_ata");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Ata> getList() {
		return getList(null);
	}

	public static ArrayList<Ata> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Ata> list = new ArrayList<Ata>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Ata obj = AtaDAO.get(rsm.getInt("cd_ata"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_ata", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

