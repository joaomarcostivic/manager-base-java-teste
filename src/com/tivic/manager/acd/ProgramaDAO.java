package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ProgramaDAO{

	public static int insert(Programa objeto) {
		return insert(objeto, null);
	}

	public static int insert(Programa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_programa", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPrograma(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_programa (cd_programa,"+
			                                  "nm_programa,"+
			                                  "tp_esfera,"+
			                                  "txt_programa,"+
			                                  "dt_registro,"+
			                                  "tp_programa,"+
			                                  "st_programa,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "id_programa,"+
			                                  "lg_gerenciado_financeiramente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmPrograma());
			pstmt.setInt(3,objeto.getTpEsfera());
			pstmt.setString(4,objeto.getTxtPrograma());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpPrograma());
			pstmt.setInt(7,objeto.getStPrograma());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(10,objeto.getIdPrograma());
			pstmt.setInt(11,objeto.getLgGerenciadoFinanceiramente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Programa objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Programa objeto, int cdProgramaOld) {
		return update(objeto, cdProgramaOld, null);
	}

	public static int update(Programa objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Programa objeto, int cdProgramaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_programa SET cd_programa=?,"+
												      		   "nm_programa=?,"+
												      		   "tp_esfera=?,"+
												      		   "txt_programa=?,"+
												      		   "dt_registro=?,"+
												      		   "tp_programa=?,"+
												      		   "st_programa=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "id_programa=?,"+
												      		   "lg_gerenciado_financeiramente=? WHERE cd_programa=?");
			pstmt.setInt(1,objeto.getCdPrograma());
			pstmt.setString(2,objeto.getNmPrograma());
			pstmt.setInt(3,objeto.getTpEsfera());
			pstmt.setString(4,objeto.getTxtPrograma());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpPrograma());
			pstmt.setInt(7,objeto.getStPrograma());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(10,objeto.getIdPrograma());
			pstmt.setInt(11,objeto.getLgGerenciadoFinanceiramente());
			pstmt.setInt(12, cdProgramaOld!=0 ? cdProgramaOld : objeto.getCdPrograma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPrograma) {
		return delete(cdPrograma, null);
	}

	public static int delete(int cdPrograma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_programa WHERE cd_programa=?");
			pstmt.setInt(1, cdPrograma);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Programa get(int cdPrograma) {
		return get(cdPrograma, null);
	}

	public static Programa get(int cdPrograma, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa WHERE cd_programa=?");
			pstmt.setInt(1, cdPrograma);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Programa(rs.getInt("cd_programa"),
						rs.getString("nm_programa"),
						rs.getInt("tp_esfera"),
						rs.getString("txt_programa"),
						(rs.getTimestamp("dt_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro").getTime()),
						rs.getInt("tp_programa"),
						rs.getInt("st_programa"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getString("id_programa"),
						rs.getInt("lg_gerenciado_financeiramente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Programa> getList() {
		return getList(null);
	}

	public static ArrayList<Programa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Programa> list = new ArrayList<Programa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Programa obj = ProgramaDAO.get(rsm.getInt("cd_programa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_programa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}