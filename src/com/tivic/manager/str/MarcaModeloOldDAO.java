package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

public class MarcaModeloOldDAO {

	public static int insert(MarcaModelo objeto) {
		return insert(objeto, null);
	}

	public static int insert(MarcaModelo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("marca_modelo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdMarca() <= 0)
				objeto.setCdMarca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO marca_modelo (cod_marca,"+
			                                  "nm_marca,"+
			                                  "nm_modelo," +
			                                  "dt_atualizacao) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdMarca());
			pstmt.setString(2,objeto.getNmMarca());
			pstmt.setString(3,objeto.getNmModelo());
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MarcaModelo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MarcaModelo objeto, int cdMarcaOld) {
		return update(objeto, cdMarcaOld, null);
	}

	public static int update(MarcaModelo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MarcaModelo objeto, int cdMarcaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE marca_modelo SET cod_marca=?,"+
												      		   "nm_marca=?,"+
												      		   "nm_modelo=?, " +
												      		   "dt_atualizacao=? "+
												      		   " WHERE cod_marca=?");
			pstmt.setInt(1,objeto.getCdMarca());
			pstmt.setString(2,objeto.getNmMarca());
			pstmt.setString(3,objeto.getNmModelo());
			pstmt.setTimestamp(4, Util.convCalendarToTimestamp(objeto.getDtAtualizacao()));
			pstmt.setInt(5, cdMarcaOld!=0 ? cdMarcaOld : objeto.getCdMarca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MarcaModelo get(int cdMarca) {
		return get(cdMarca, null);
	}

	public static MarcaModelo get(int cdMarca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM marca_modelo WHERE cod_marca=?");
			pstmt.setInt(1, cdMarca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MarcaModelo(rs.getInt("cod_marca"),
						rs.getString("nm_marca"),
						rs.getString("nm_modelo"),
						(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()));
			} else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
