package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class UnidadeExecutoraInstituicaoDAO{

	public static int insert(UnidadeExecutoraInstituicao objeto) {
		return insert(objeto, null);
	}

	public static int insert(UnidadeExecutoraInstituicao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_unidade_executora_instituicao (cd_instituicao,"+
			                                  "cd_unidade_executora,"+
			                                  "cd_exercicio,"+
			                                  "dt_vinculacao,"+
			                                  "st_vinculacao) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			if(objeto.getCdUnidadeExecutora()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUnidadeExecutora());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdExercicio());
			if(objeto.getDtVinculacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtVinculacao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStVinculacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UnidadeExecutoraInstituicao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(UnidadeExecutoraInstituicao objeto, int cdInstituicaoOld, int cdUnidadeExecutoraOld, int cdExercicioOld) {
		return update(objeto, cdInstituicaoOld, cdUnidadeExecutoraOld, cdExercicioOld, null);
	}

	public static int update(UnidadeExecutoraInstituicao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(UnidadeExecutoraInstituicao objeto, int cdInstituicaoOld, int cdUnidadeExecutoraOld, int cdExercicioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_unidade_executora_instituicao SET cd_instituicao=?,"+
												      		   "cd_unidade_executora=?,"+
												      		   "cd_exercicio=?,"+
												      		   "dt_vinculacao=?,"+
												      		   "st_vinculacao=? WHERE cd_instituicao=? AND cd_unidade_executora=? AND cd_exercicio=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdUnidadeExecutora());
			pstmt.setInt(3,objeto.getCdExercicio());
			if(objeto.getDtVinculacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtVinculacao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStVinculacao());
			pstmt.setInt(6, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(7, cdUnidadeExecutoraOld!=0 ? cdUnidadeExecutoraOld : objeto.getCdUnidadeExecutora());
			pstmt.setInt(8, cdExercicioOld!=0 ? cdExercicioOld : objeto.getCdExercicio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdUnidadeExecutora, int cdExercicio) {
		return delete(cdInstituicao, cdUnidadeExecutora, cdExercicio, null);
	}

	public static int delete(int cdInstituicao, int cdUnidadeExecutora, int cdExercicio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_unidade_executora_instituicao WHERE cd_instituicao=? AND cd_unidade_executora=? AND cd_exercicio=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdUnidadeExecutora);
			pstmt.setInt(3, cdExercicio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UnidadeExecutoraInstituicao get(int cdInstituicao, int cdUnidadeExecutora, int cdExercicio) {
		return get(cdInstituicao, cdUnidadeExecutora, cdExercicio, null);
	}

	public static UnidadeExecutoraInstituicao get(int cdInstituicao, int cdUnidadeExecutora, int cdExercicio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_unidade_executora_instituicao WHERE cd_instituicao=? AND cd_unidade_executora=? AND cd_exercicio=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdUnidadeExecutora);
			pstmt.setInt(3, cdExercicio);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UnidadeExecutoraInstituicao(rs.getInt("cd_instituicao"),
						rs.getInt("cd_unidade_executora"),
						rs.getInt("cd_exercicio"),
						(rs.getTimestamp("dt_vinculacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vinculacao").getTime()),
						rs.getInt("st_vinculacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_unidade_executora_instituicao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<UnidadeExecutoraInstituicao> getList() {
		return getList(null);
	}

	public static ArrayList<UnidadeExecutoraInstituicao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<UnidadeExecutoraInstituicao> list = new ArrayList<UnidadeExecutoraInstituicao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				UnidadeExecutoraInstituicao obj = UnidadeExecutoraInstituicaoDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_unidade_executora"), rsm.getInt("cd_exercicio"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraInstituicaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_unidade_executora_instituicao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
