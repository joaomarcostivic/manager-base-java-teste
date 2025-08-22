package com.tivic.manager.adapter.base.antiga.aitImagem;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AitImagemOldDAO{

	public static int insert(AitImagemOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitImagemOld objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("str_ait_imagem", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdImagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO str_ait_imagem (cd_imagem,"+
			                                  "cd_ait,"+
			                                  "blb_imagem,"+
			                                  "tp_imagem,"+
			                                  "lg_impressao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setInt(4,objeto.getTpImagem());
			pstmt.setInt(5,objeto.getLgImpressao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitImagemOld objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AitImagemOld objeto, int cdImagemOld) {
		return update(objeto, cdImagemOld, null);
	}

	public static int update(AitImagemOld objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AitImagemOld objeto, int cdImagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE str_ait_imagem SET cd_imagem=?,"+
												      		   "cd_ait=?,"+
												      		   "blb_imagem=?,"+
												      		   "tp_imagem=?,"+
												      		   "lg_impressao=? WHERE cd_imagem=?");
			pstmt.setInt(1,objeto.getCdImagem());
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setInt(4,objeto.getTpImagem());
			pstmt.setInt(5,objeto.getLgImpressao());
			pstmt.setInt(6, cdImagemOld!=0 ? cdImagemOld : objeto.getCdImagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM str_ait_imagem WHERE cd_imagem=?");
			pstmt.setInt(1, cdImagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitImagemOld get(int cdImagem) {
		return get(cdImagem, null);
	}

	public static AitImagemOld get(int cdImagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_ait_imagem WHERE cd_imagem=?");
			pstmt.setInt(1, cdImagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitImagemOld(rs.getInt("cd_imagem"),
						rs.getInt("cd_ait"),
						rs.getBytes("blb_imagem")==null?null:rs.getBytes("blb_imagem"),
						rs.getInt("tp_imagem"),
						rs.getInt("lg_impressao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM str_ait_imagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitImagemOld> getList() {
		return getList(null);
	}

	public static ArrayList<AitImagemOld> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitImagemOld> list = new ArrayList<AitImagemOld>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitImagemOld obj = get(rsm.getInt("cd_imagem"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM str_ait_imagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
