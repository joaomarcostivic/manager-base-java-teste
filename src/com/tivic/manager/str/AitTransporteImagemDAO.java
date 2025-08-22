package com.tivic.manager.str;

import java.sql.*;

import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AitTransporteImagemDAO{

	public static int insert(AitTransporteImagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitTransporteImagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("STR_AIT_TRANSPORTE_IMAGEM", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdImagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO STR_AIT_TRANSPORTE_IMAGEM (CD_IMAGEM,"+
			                                  "BLB_IMAGEM,"+
			                                  "CD_AIT) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(2, Types.BINARY);
			else
				pstmt.setBytes(2,objeto.getBlbImagem());
			
			if(objeto.getCdAit()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAit());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitTransporteImagem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AitTransporteImagem objeto, int cdImagemOld) {
		return update(objeto, cdImagemOld, null);
	}

	public static int update(AitTransporteImagem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AitTransporteImagem objeto, int cdImagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE STR_AIT_TRANSPORTE_IMAGEM SET CD_IMAGEM=?,"+
												      		   "BLB_IMAGEM=?,"+
												      		   "CD_AIT=? WHERE CD_IMAGEM=?");
			pstmt.setInt(1,objeto.getCdImagem());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(2, Types.BINARY);
			else
				pstmt.setBytes(2,objeto.getBlbImagem());
			if(objeto.getCdAit()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAit());
			pstmt.setInt(4, cdImagemOld!=0 ? cdImagemOld : objeto.getCdImagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdImagem) {
		return delete(cdImagem, null);
	}

	public static int delete(int cdImagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM STR_AIT_TRANSPORTE_IMAGEM WHERE CD_IMAGEM=?");
			pstmt.setInt(1, cdImagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitTransporteImagem get(int cdImagem) {
		return get(cdImagem, null);
	}

	public static AitTransporteImagem get(int cdImagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM STR_AIT_TRANSPORTE_IMAGEM WHERE CD_IMAGEM=?");
			pstmt.setInt(1, cdImagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitTransporteImagem(rs.getInt("CD_IMAGEM"),
						rs.getBytes("BLB_IMAGEM"),
						rs.getInt("CD_AIT"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM STR_AIT_TRANSPORTE_IMAGEM");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitTransporteImagem> getList() {
		return getList(null);
	}

	public static ArrayList<AitTransporteImagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitTransporteImagem> list = new ArrayList<AitTransporteImagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitTransporteImagem obj = AitTransporteImagemDAO.get(rsm.getInt("CD_IMAGEM"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteImagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM STR_AIT_TRANSPORTE_IMAGEM", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
