package com.tivic.manager.crt;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class EmpreendimentoDAO{

	public static int insert(Empreendimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Empreendimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crt_empreendimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEmpreendimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crt_empreendimento (cd_empreendimento,"+
			                                  "nm_empreendimento,"+
			                                  "txt_empreendimento,"+
			                                  "blb_logo,"+
			                                  "txt_memorial,"+
			                                  "blb_geodados,"+
			                                  "blb_capa,"+
			                                  "cd_local_armazenamento,"+
			                                  "cd_construtora) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmEmpreendimento());
			pstmt.setString(3,objeto.getTxtEmpreendimento());
			if(objeto.getBlbLogo()==null)
				pstmt.setNull(4, Types.BINARY);
			else
				pstmt.setBytes(4,objeto.getBlbLogo());
			pstmt.setString(5,objeto.getTxtMemorial());
			if(objeto.getBlbGeodados()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getBlbGeodados());
			if(objeto.getBlbCapa()==null)
				pstmt.setNull(7, Types.BINARY);
			else
				pstmt.setBytes(7,objeto.getBlbCapa());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdLocalArmazenamento());
			if(objeto.getCdConstrutora()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdConstrutora());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Empreendimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Empreendimento objeto, int cdEmpreendimentoOld) {
		return update(objeto, cdEmpreendimentoOld, null);
	}

	public static int update(Empreendimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Empreendimento objeto, int cdEmpreendimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crt_empreendimento SET cd_empreendimento=?,"+
												      		   "nm_empreendimento=?,"+
												      		   "txt_empreendimento=?,"+
												      		   "blb_logo=?,"+
												      		   "txt_memorial=?,"+
												      		   "blb_geodados=?,"+
												      		   "blb_capa=?,"+
												      		   "cd_local_armazenamento=?,"+
												      		   "cd_construtora=? WHERE cd_empreendimento=?");
			pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setString(2,objeto.getNmEmpreendimento());
			pstmt.setString(3,objeto.getTxtEmpreendimento());
			if(objeto.getBlbLogo()==null)
				pstmt.setNull(4, Types.BINARY);
			else
				pstmt.setBytes(4,objeto.getBlbLogo());
			pstmt.setString(5,objeto.getTxtMemorial());
			if(objeto.getBlbGeodados()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getBlbGeodados());
			if(objeto.getBlbCapa()==null)
				pstmt.setNull(7, Types.BINARY);
			else
				pstmt.setBytes(7,objeto.getBlbCapa());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdLocalArmazenamento());
			if(objeto.getCdConstrutora()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdConstrutora());
			pstmt.setInt(10, cdEmpreendimentoOld!=0 ? cdEmpreendimentoOld : objeto.getCdEmpreendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento) {
		return delete(cdEmpreendimento, null);
	}

	public static int delete(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crt_empreendimento WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Empreendimento get(int cdEmpreendimento) {
		return get(cdEmpreendimento, null);
	}

	public static Empreendimento get(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crt_empreendimento WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Empreendimento(rs.getInt("cd_empreendimento"),
						rs.getString("nm_empreendimento"),
						rs.getString("txt_empreendimento"),
						rs.getBytes("blb_logo")==null?null:rs.getBytes("blb_logo"),
						rs.getString("txt_memorial"),
						rs.getBytes("blb_geodados")==null?null:rs.getBytes("blb_geodados"),
						rs.getBytes("blb_capa")==null?null:rs.getBytes("blb_capa"),
						rs.getInt("cd_local_armazenamento"),
						rs.getInt("cd_construtora"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crt_empreendimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Empreendimento> getList() {
		return getList(null);
	}

	public static ArrayList<Empreendimento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Empreendimento> list = new ArrayList<Empreendimento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Empreendimento obj = EmpreendimentoDAO.get(rsm.getInt("cd_empreendimento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM crt_empreendimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
