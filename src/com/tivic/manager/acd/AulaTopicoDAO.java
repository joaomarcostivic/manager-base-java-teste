package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AulaTopicoDAO{

	public static int insert(AulaTopico objeto) {
		return insert(objeto, null);
	}

	public static int insert(AulaTopico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_aula_topico (cd_plano,"+
			                                  "cd_secao,"+
			                                  "cd_topico,"+
			                                  "cd_aula,"+
			                                  "lg_executado,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?)");
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
			if(objeto.getCdAula()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAula());
			pstmt.setInt(5,objeto.getLgExecutado());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AulaTopico objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(AulaTopico objeto, int cdPlanoOld, int cdSecaoOld, int cdTopicoOld, int cdAulaOld) {
		return update(objeto, cdPlanoOld, cdSecaoOld, cdTopicoOld, cdAulaOld, null);
	}

	public static int update(AulaTopico objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(AulaTopico objeto, int cdPlanoOld, int cdSecaoOld, int cdTopicoOld, int cdAulaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_aula_topico SET cd_plano=?,"+
												      		   "cd_secao=?,"+
												      		   "cd_topico=?,"+
												      		   "cd_aula=?,"+
												      		   "lg_executado=?,"+
												      		   "txt_observacao=? WHERE cd_plano=? AND cd_secao=? AND cd_topico=? AND cd_aula=?");
			pstmt.setInt(1,objeto.getCdPlano());
			pstmt.setInt(2,objeto.getCdSecao());
			pstmt.setInt(3,objeto.getCdTopico());
			pstmt.setInt(4,objeto.getCdAula());
			pstmt.setInt(5,objeto.getLgExecutado());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7, cdPlanoOld!=0 ? cdPlanoOld : objeto.getCdPlano());
			pstmt.setInt(8, cdSecaoOld!=0 ? cdSecaoOld : objeto.getCdSecao());
			pstmt.setInt(9, cdTopicoOld!=0 ? cdTopicoOld : objeto.getCdTopico());
			pstmt.setInt(10, cdAulaOld!=0 ? cdAulaOld : objeto.getCdAula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlano, int cdSecao, int cdTopico, int cdAula) {
		return delete(cdPlano, cdSecao, cdTopico, cdAula, null);
	}

	public static int delete(int cdPlano, int cdSecao, int cdTopico, int cdAula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_aula_topico WHERE cd_plano=? AND cd_secao=? AND cd_topico=? AND cd_aula=?");
			pstmt.setInt(1, cdPlano);
			pstmt.setInt(2, cdSecao);
			pstmt.setInt(3, cdTopico);
			pstmt.setInt(4, cdAula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AulaTopico get(int cdPlano, int cdSecao, int cdTopico, int cdAula) {
		return get(cdPlano, cdSecao, cdTopico, cdAula, null);
	}

	public static AulaTopico get(int cdPlano, int cdSecao, int cdTopico, int cdAula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_topico WHERE cd_plano=? AND cd_secao=? AND cd_topico=? AND cd_aula=?");
			pstmt.setInt(1, cdPlano);
			pstmt.setInt(2, cdSecao);
			pstmt.setInt(3, cdTopico);
			pstmt.setInt(4, cdAula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AulaTopico(rs.getInt("cd_plano"),
						rs.getInt("cd_secao"),
						rs.getInt("cd_topico"),
						rs.getInt("cd_aula"),
						rs.getInt("lg_executado"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_topico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AulaTopico> getList() {
		return getList(null);
	}

	public static ArrayList<AulaTopico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AulaTopico> list = new ArrayList<AulaTopico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AulaTopico obj = AulaTopicoDAO.get(rsm.getInt("cd_plano"), rsm.getInt("cd_secao"), rsm.getInt("cd_topico"), rsm.getInt("cd_aula"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaTopicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_aula_topico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}