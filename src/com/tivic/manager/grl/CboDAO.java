package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CboDAO{

	public static int insert(Cbo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Cbo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_cbo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCbo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_cbo (cd_cbo,"+
			                                  "nm_cbo,"+
			                                  "sg_cbo,"+
			                                  "id_cbo,"+
			                                  "nr_nivel,"+
			                                  "cd_cbo_superior,"+
			                                  "nr_cbo,"+
			                                  "txt_ocupacao,"+
			                                  "txt_descricao_sumaria,"+
			                                  "txt_condicao_exercicio,"+
			                                  "txt_formacao,"+
			                                  "txt_excecao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCbo());
			pstmt.setString(3,objeto.getSgCbo());
			pstmt.setString(4,objeto.getIdCbo());
			pstmt.setInt(5,objeto.getNrNivel());
			if(objeto.getCdCboSuperior()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCboSuperior());
			pstmt.setString(7,objeto.getNrCbo());
			pstmt.setString(8,objeto.getTxtOcupacao());
			pstmt.setString(9,objeto.getTxtDescricaoSumaria());
			pstmt.setString(10,objeto.getTxtCondicaoExercicio());
			pstmt.setString(11,objeto.getTxtFormacao());
			pstmt.setString(12,objeto.getTxtExcecao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cbo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Cbo objeto, int cdCboOld) {
		return update(objeto, cdCboOld, null);
	}

	public static int update(Cbo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Cbo objeto, int cdCboOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_cbo SET cd_cbo=?,"+
												      		   "nm_cbo=?,"+
												      		   "sg_cbo=?,"+
												      		   "id_cbo=?,"+
												      		   "nr_nivel=?,"+
												      		   "cd_cbo_superior=?,"+
												      		   "nr_cbo=?,"+
												      		   "txt_ocupacao=?,"+
												      		   "txt_descricao_sumaria=?,"+
												      		   "txt_condicao_exercicio=?,"+
												      		   "txt_formacao=?,"+
												      		   "txt_excecao=? WHERE cd_cbo=?");
			pstmt.setInt(1,objeto.getCdCbo());
			pstmt.setString(2,objeto.getNmCbo());
			pstmt.setString(3,objeto.getSgCbo());
			pstmt.setString(4,objeto.getIdCbo());
			pstmt.setInt(5,objeto.getNrNivel());
			if(objeto.getCdCboSuperior()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCboSuperior());
			pstmt.setString(7,objeto.getNrCbo());
			pstmt.setString(8,objeto.getTxtOcupacao());
			pstmt.setString(9,objeto.getTxtDescricaoSumaria());
			pstmt.setString(10,objeto.getTxtCondicaoExercicio());
			pstmt.setString(11,objeto.getTxtFormacao());
			pstmt.setString(12,objeto.getTxtExcecao());
			pstmt.setInt(13, cdCboOld!=0 ? cdCboOld : objeto.getCdCbo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCbo) {
		return delete(cdCbo, null);
	}

	public static int delete(int cdCbo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_cbo WHERE cd_cbo=?");
			pstmt.setInt(1, cdCbo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Cbo get(int cdCbo) {
		return get(cdCbo, null);
	}

	public static Cbo get(int cdCbo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cbo WHERE cd_cbo=?");
			pstmt.setInt(1, cdCbo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Cbo(rs.getInt("cd_cbo"),
						rs.getString("nm_cbo"),
						rs.getString("sg_cbo"),
						rs.getString("id_cbo"),
						rs.getInt("nr_nivel"),
						rs.getInt("cd_cbo_superior"),
						rs.getString("nr_cbo"),
						rs.getString("txt_ocupacao"),
						rs.getString("txt_descricao_sumaria"),
						rs.getString("txt_condicao_exercicio"),
						rs.getString("txt_formacao"),
						rs.getString("txt_excecao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cbo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CboDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_cbo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
