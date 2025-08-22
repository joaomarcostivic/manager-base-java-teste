package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PlanoTopicoDAO{

	public static int insert(PlanoTopico objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoTopico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int code = Conexao.getSequenceCode("acd_plano_topico", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTopico(code);
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_plano_topico (cd_plano,"+
			                                  "cd_secao,"+
			                                  "cd_topico,"+
			                                  "cd_topico_superior,"+
			                                  "txt_topico,"+
			                                  "nr_ordem,"+
			                                  "nm_titulo) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPlano()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPlano());
			if(objeto.getCdSecao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSecao());
			if(objeto.getCdTopico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTopico());
			if(objeto.getCdTopicoSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTopicoSuperior());
			pstmt.setString(5,objeto.getTxtTopico());
			pstmt.setInt(6,objeto.getNrOrdem());
			pstmt.setString(7,objeto.getNmTitulo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoTopico objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(PlanoTopico objeto, int cdPlanoOld, int cdSecaoOld, int cdTopicoOld) {
		return update(objeto, cdPlanoOld, cdSecaoOld, cdTopicoOld, null);
	}

	public static int update(PlanoTopico objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(PlanoTopico objeto, int cdPlanoOld, int cdSecaoOld, int cdTopicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_plano_topico SET cd_plano=?,"+
												      		   "cd_secao=?,"+
												      		   "cd_topico=?,"+
												      		   "cd_topico_superior=?,"+
												      		   "txt_topico=?,"+
												      		   "nr_ordem=?,"+
												      		   "nm_titulo=? WHERE cd_plano=? AND cd_secao=? AND cd_topico=?");
			pstmt.setInt(1,objeto.getCdPlano());
			pstmt.setInt(2,objeto.getCdSecao());
			pstmt.setInt(3,objeto.getCdTopico());
			if(objeto.getCdTopicoSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTopicoSuperior());
			pstmt.setString(5,objeto.getTxtTopico());
			pstmt.setInt(6,objeto.getNrOrdem());
			pstmt.setString(7,objeto.getNmTitulo());
			pstmt.setInt(8, cdPlanoOld!=0 ? cdPlanoOld : objeto.getCdPlano());
			pstmt.setInt(9, cdSecaoOld!=0 ? cdSecaoOld : objeto.getCdSecao());
			pstmt.setInt(10, cdTopicoOld!=0 ? cdTopicoOld : objeto.getCdTopico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlano, int cdSecao, int cdTopico) {
		return delete(cdPlano, cdSecao, cdTopico, null);
	}

	public static int delete(int cdPlano, int cdSecao, int cdTopico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_plano_topico WHERE cd_plano=? AND cd_secao=? AND cd_topico=?");
			pstmt.setInt(1, cdPlano);
			pstmt.setInt(2, cdSecao);
			pstmt.setInt(3, cdTopico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoTopico get(int cdPlano, int cdSecao, int cdTopico) {
		return get(cdPlano, cdSecao, cdTopico, null);
	}

	public static PlanoTopico get(int cdPlano, int cdSecao, int cdTopico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano_topico WHERE cd_plano=? AND cd_secao=? AND cd_topico=?");
			pstmt.setInt(1, cdPlano);
			pstmt.setInt(2, cdSecao);
			pstmt.setInt(3, cdTopico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoTopico(rs.getInt("cd_plano"),
						rs.getInt("cd_secao"),
						rs.getInt("cd_topico"),
						rs.getInt("cd_topico_superior"),
						rs.getString("txt_topico"),
						rs.getInt("nr_ordem"),
						rs.getString("nm_titulo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano_topico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoTopico> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoTopico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoTopico> list = new ArrayList<PlanoTopico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoTopico obj = PlanoTopicoDAO.get(rsm.getInt("cd_plano"), rsm.getInt("cd_secao"), rsm.getInt("cd_topico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTopicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_plano_topico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
