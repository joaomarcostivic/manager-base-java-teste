package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class MarcaModeloDAO{

	public static int insert(MarcaModelo objeto) {
		return insert(objeto, null);
	}

	public static int insert(MarcaModelo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_marca_modelo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if(objeto.getCdMarca() <= 0)
				objeto.setCdMarca(code);
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_marca_modelo (cd_marca,"+
															                                 "nm_marca,"+
															                                 "nm_modelo,"+
															                                 "tp_marca,"+
															                                 "dt_atualizacao,"+
															                                 "nr_marca) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdMarca());
			pstmt.setString(2,objeto.getNmMarca());
			pstmt.setString(3,objeto.getNmModelo());
			pstmt.setInt(4,objeto.getTpMarca());
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setString(6,objeto.getNrMarca());
			pstmt.executeUpdate();
			return objeto.getCdMarca();
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
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_marca_modelo SET cd_marca=?,"+
												      		   "nm_marca=?,"+
												      		   "nm_modelo=?,"+
																"tp_marca=?,"+
																"dt_atualizacao=?,"+
																"nr_marca=? "+
												      		   " WHERE cd_marca=?");
			pstmt.setInt(1,objeto.getCdMarca());
			pstmt.setString(2,objeto.getNmMarca());
			pstmt.setString(3,objeto.getNmModelo());
			pstmt.setInt(4, objeto.getTpMarca());
			pstmt.setTimestamp(5, Util.convCalendarToTimestamp(objeto.getDtAtualizacao()));
			pstmt.setString(6,objeto.getNrMarca());
			pstmt.setInt(7, cdMarcaOld!=0 ? cdMarcaOld : objeto.getCdMarca());
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

	public static int delete(int cdMarca) {
		return delete(cdMarca, null);
	}

	public static int delete(int cdMarca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_marca_modelo WHERE cd_marca=?");
			pstmt.setInt(1, cdMarca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.delete: " +  e);
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
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_marca_modelo WHERE cd_marca=?");
				pstmt.setInt(1, cdMarca);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new MarcaModelo(rs.getInt("cd_marca"),
							rs.getString("nm_marca"),
							rs.getString("nm_modelo"),
							rs.getInt("tp_marca"),
							Util.convTimestampToCalendar(rs.getTimestamp("dt_atualizacao")),
							rs.getString("nr_marca"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM marca_modelo WHERE cod_marca=?");
				pstmt.setInt(1, cdMarca);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new MarcaModelo(rs.getInt("cod_marca"),
							rs.getString("nm_marca"),
							rs.getString("nm_modelo"),
							0,
							Util.convTimestampToCalendar(rs.getTimestamp("dt_atualizacao")),
							null);
				}
			}
			
			return null;
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
	
	public static MarcaModelo getBaseNova(int cdMarca) {
		return get(cdMarca, null);
	}

	public static MarcaModelo getBaseNova(int cdMarca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_marca_modelo WHERE cd_marca=?");
			pstmt.setInt(1, cdMarca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MarcaModelo(rs.getInt("cd_marca"),
						rs.getString("nm_marca"),
						rs.getString("nm_modelo"),
						rs.getInt("tp_marca"),
						Util.convTimestampToCalendar(rs.getTimestamp("dt_atualizacao")),
						rs.getString("nr_marca"));
			} else {
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_marca_modelo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MarcaModelo> getList() {
		return getList(null);
	}

	public static ArrayList<MarcaModelo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MarcaModelo> list = new ArrayList<MarcaModelo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MarcaModelo obj = MarcaModeloDAO.get(rsm.getInt("cd_marca"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fta_marca_modelo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static int directInsert(MarcaModelo objeto) {
		return directInsert(objeto, null);
	}

	public static int directInsert(MarcaModelo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			if(objeto.getCdMarca() <= 0)
				return -1;
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_marca_modelo (cd_marca,"+
															                                 "nm_marca,"+
															                                 "nm_modelo,"+
																							 "tp_marca,"+
															                                 "dt_atualizacao,"+
																							 "nr_marca) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdMarca());
			pstmt.setString(2,objeto.getNmMarca());
			pstmt.setString(3,objeto.getNmModelo());
			pstmt.setInt(4,objeto.getTpMarca());
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setString(6,objeto.getNrMarca());
			pstmt.executeUpdate();
			
			return objeto.getCdMarca();
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

}
