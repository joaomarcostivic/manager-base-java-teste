package com.tivic.manager.flp;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class MatriculaEventoFinanceiroDAO{

	public static int insert(MatriculaEventoFinanceiro objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaEventoFinanceiro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_matricula_evento_financeiro (cd_evento_financeiro,"+
			                                  "cd_matricula,"+
			                                  "dt_inicio,"+
			                                  "qt_repeticoes,"+
			                                  "qt_evento_financeiro,"+
			                                  "vl_evento_financeiro,"+
			                                  "qt_horas) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEventoFinanceiro());
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatricula());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setInt(4,objeto.getQtRepeticoes());
			pstmt.setFloat(5,objeto.getQtEventoFinanceiro());
			pstmt.setFloat(6,objeto.getVlEventoFinanceiro());
			pstmt.setFloat(7,objeto.getQtHoras());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaEventoFinanceiro objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MatriculaEventoFinanceiro objeto, int cdEventoFinanceiroOld, int cdMatriculaOld) {
		return update(objeto, cdEventoFinanceiroOld, cdMatriculaOld, null);
	}

	public static int update(MatriculaEventoFinanceiro objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MatriculaEventoFinanceiro objeto, int cdEventoFinanceiroOld, int cdMatriculaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_matricula_evento_financeiro SET cd_evento_financeiro=?,"+
												      		   "cd_matricula=?,"+
												      		   "dt_inicio=?,"+
												      		   "qt_repeticoes=?,"+
												      		   "qt_evento_financeiro=?,"+
												      		   "vl_evento_financeiro=?,"+
												      		   "qt_horas=? WHERE cd_evento_financeiro=? AND cd_matricula=?");
			pstmt.setInt(1,objeto.getCdEventoFinanceiro());
			pstmt.setInt(2,objeto.getCdMatricula());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setInt(4,objeto.getQtRepeticoes());
			pstmt.setFloat(5,objeto.getQtEventoFinanceiro());
			pstmt.setFloat(6,objeto.getVlEventoFinanceiro());
			pstmt.setFloat(7,objeto.getQtHoras());
			pstmt.setInt(8, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			pstmt.setInt(9, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEventoFinanceiro, int cdMatricula) {
		return delete(cdEventoFinanceiro, cdMatricula, null);
	}

	public static int delete(int cdEventoFinanceiro, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_matricula_evento_financeiro WHERE cd_evento_financeiro=? AND cd_matricula=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.setInt(2, cdMatricula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaEventoFinanceiro get(int cdEventoFinanceiro, int cdMatricula) {
		return get(cdEventoFinanceiro, cdMatricula, null);
	}

	public static MatriculaEventoFinanceiro get(int cdEventoFinanceiro, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_matricula_evento_financeiro WHERE cd_evento_financeiro=? AND cd_matricula=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.setInt(2, cdMatricula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaEventoFinanceiro(rs.getInt("cd_evento_financeiro"),
						rs.getInt("cd_matricula"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						rs.getInt("qt_repeticoes"),
						rs.getFloat("qt_evento_financeiro"),
						rs.getFloat("vl_evento_financeiro"),
						rs.getFloat("qt_horas"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_matricula_evento_financeiro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaEventoFinanceiroDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM flp_matricula_evento_financeiro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
