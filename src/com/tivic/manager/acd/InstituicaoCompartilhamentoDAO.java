package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class InstituicaoCompartilhamentoDAO{

	public static int insert(InstituicaoCompartilhamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoCompartilhamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_compartilhamento (cd_instituicao,"+
			                                  "nr_instituicao_compartilhamento) VALUES (?, ?)");
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setString(2, objeto.getNrInstituicaoCompartilha());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoCompartilhamento objeto) {
		return update(objeto, 0, null, null);
	}

	public static int update(InstituicaoCompartilhamento objeto, int cdInstituicaoOld, String nrInstituicaoCompartilhaOld) {
		return update(objeto, cdInstituicaoOld, nrInstituicaoCompartilhaOld, null);
	}

	public static int update(InstituicaoCompartilhamento objeto, Connection connect) {
		return update(objeto, 0, null, connect);
	}

	public static int update(InstituicaoCompartilhamento objeto, int cdInstituicaoOld, String nrInstituicaoCompartilhaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_compartilhamento SET cd_instituicao=?,"+
												      		   "nr_instituicao_compartilhamento=? WHERE cd_instituicao=? AND nr_instituicao_compartilhamento=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setString(2,objeto.getNrInstituicaoCompartilha());
			pstmt.setInt(3, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setString(4, nrInstituicaoCompartilhaOld!=null ? nrInstituicaoCompartilhaOld : objeto.getNrInstituicaoCompartilha());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, String nrInstituicaoCompartilha) {
		return delete(cdInstituicao, nrInstituicaoCompartilha, null);
	}

	public static int delete(int cdInstituicao, String nrInstituicaoCompartilha, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_compartilhamento WHERE cd_instituicao=? AND nr_instituicao_compartilhamento=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setString(2, nrInstituicaoCompartilha);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoCompartilhamento get(int cdInstituicao, String nrInstituicaoCompartilha) {
		return get(cdInstituicao, nrInstituicaoCompartilha, null);
	}

	public static InstituicaoCompartilhamento get(int cdInstituicao, String nrInstituicaoCompartilha, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_compartilhamento WHERE cd_instituicao=? AND nr_instituicao_compartilhamento=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setString(2, nrInstituicaoCompartilha);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoCompartilhamento(rs.getInt("cd_instituicao"),
						rs.getString("nr_instituicao_compartilhamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_compartilhamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoCompartilhamento> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoCompartilhamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoCompartilhamento> list = new ArrayList<InstituicaoCompartilhamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoCompartilhamento obj = InstituicaoCompartilhamentoDAO.get(rsm.getInt("cd_instituicao"), rsm.getString("nr_instituicao_compartilhamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCompartilhamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_compartilhamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
