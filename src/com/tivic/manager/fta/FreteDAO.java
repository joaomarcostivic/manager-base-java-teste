package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class FreteDAO{

	public static int insert(Frete objeto) {
		return insert(objeto, null);
	}

	public static int insert(Frete objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_frete", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFrete(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_frete (cd_frete,"+
			                                  "cd_cliente,"+
			                                  "lg_frete_destinatario,"+
			                                  "dt_entrega,"+
			                                  "vl_total,"+
			                                  "cd_viagem) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCliente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCliente());
			pstmt.setInt(3,objeto.getLgFreteDestinatario());
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getVlTotal());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdViagem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Frete objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Frete objeto, int cdFreteOld) {
		return update(objeto, cdFreteOld, null);
	}

	public static int update(Frete objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Frete objeto, int cdFreteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_frete SET cd_frete=?,"+
												      		   "cd_cliente=?,"+
												      		   "lg_frete_destinatario=?,"+
												      		   "dt_entrega=?,"+
												      		   "vl_total=?,"+
												      		   "cd_viagem=? WHERE cd_frete=?");
			pstmt.setInt(1,objeto.getCdFrete());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCliente());
			pstmt.setInt(3,objeto.getLgFreteDestinatario());
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getVlTotal());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdViagem());
			pstmt.setInt(7, cdFreteOld!=0 ? cdFreteOld : objeto.getCdFrete());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFrete) {
		return delete(cdFrete, null);
	}

	public static int delete(int cdFrete, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_frete WHERE cd_frete=?");
			pstmt.setInt(1, cdFrete);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Frete get(int cdFrete) {
		return get(cdFrete, null);
	}

	public static Frete get(int cdFrete, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_frete WHERE cd_frete=?");
			pstmt.setInt(1, cdFrete);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Frete(rs.getInt("cd_frete"),
						rs.getInt("cd_cliente"),
						rs.getInt("lg_frete_destinatario"),
						(rs.getTimestamp("dt_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega").getTime()),
						rs.getFloat("vl_total"),
						rs.getInt("cd_viagem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_frete");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FreteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_frete", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
