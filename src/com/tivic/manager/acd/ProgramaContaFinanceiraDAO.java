package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ProgramaContaFinanceiraDAO{

	public static int insert(ProgramaContaFinanceira objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProgramaContaFinanceira objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_programa_conta_financeira (cd_conta,"+
			                                  "cd_programa,"+
			                                  "cd_unidade_executora,"+
			                                  "dt_vinculacao,"+
			                                  "st_vinculacao) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdConta()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConta());
			if(objeto.getCdPrograma()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPrograma());
			if(objeto.getCdUnidadeExecutora()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUnidadeExecutora());
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
			System.err.println("Erro! ProgramaContaFinanceiraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProgramaContaFinanceira objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ProgramaContaFinanceira objeto, int cdContaOld, int cdProgramaOld, int cdUnidadeExecutoraOld) {
		return update(objeto, cdContaOld, cdProgramaOld, cdUnidadeExecutoraOld, null);
	}

	public static int update(ProgramaContaFinanceira objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ProgramaContaFinanceira objeto, int cdContaOld, int cdProgramaOld, int cdUnidadeExecutoraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_programa_conta_financeira SET cd_conta=?,"+
												      		   "cd_programa=?,"+
												      		   "cd_unidade_executora=?,"+
												      		   "dt_vinculacao=?,"+
												      		   "st_vinculacao=? WHERE cd_conta=? AND cd_programa=? AND cd_unidade_executora=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdPrograma());
			pstmt.setInt(3,objeto.getCdUnidadeExecutora());
			if(objeto.getDtVinculacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtVinculacao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStVinculacao());
			pstmt.setInt(6, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(7, cdProgramaOld!=0 ? cdProgramaOld : objeto.getCdPrograma());
			pstmt.setInt(8, cdUnidadeExecutoraOld!=0 ? cdUnidadeExecutoraOld : objeto.getCdUnidadeExecutora());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta, int cdPrograma, int cdUnidadeExecutora) {
		return delete(cdConta, cdPrograma, cdUnidadeExecutora, null);
	}

	public static int delete(int cdConta, int cdPrograma, int cdUnidadeExecutora, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_programa_conta_financeira WHERE cd_conta=? AND cd_programa=? AND cd_unidade_executora=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdPrograma);
			pstmt.setInt(3, cdUnidadeExecutora);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProgramaContaFinanceira get(int cdConta, int cdPrograma, int cdUnidadeExecutora) {
		return get(cdConta, cdPrograma, cdUnidadeExecutora, null);
	}

	public static ProgramaContaFinanceira get(int cdConta, int cdPrograma, int cdUnidadeExecutora, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa_conta_financeira WHERE cd_conta=? AND cd_programa=? AND cd_unidade_executora=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdPrograma);
			pstmt.setInt(3, cdUnidadeExecutora);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProgramaContaFinanceira(rs.getInt("cd_conta"),
						rs.getInt("cd_programa"),
						rs.getInt("cd_unidade_executora"),
						(rs.getTimestamp("dt_vinculacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vinculacao").getTime()),
						rs.getInt("st_vinculacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa_conta_financeira");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProgramaContaFinanceira> getList() {
		return getList(null);
	}

	public static ArrayList<ProgramaContaFinanceira> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProgramaContaFinanceira> list = new ArrayList<ProgramaContaFinanceira>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProgramaContaFinanceira obj = ProgramaContaFinanceiraDAO.get(rsm.getInt("cd_conta"), rsm.getInt("cd_programa"), rsm.getInt("cd_unidade_executora"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaContaFinanceiraDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_programa_conta_financeira", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
