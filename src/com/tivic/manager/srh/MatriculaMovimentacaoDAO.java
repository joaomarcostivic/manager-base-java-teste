package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import sol.dao.Util;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class MatriculaMovimentacaoDAO{

	public static int insert(MatriculaMovimentacao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(MatriculaMovimentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_matricula");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdMatricula()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_movimentacao");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("srh_matricula_movimentacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMovimentacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_matricula_movimentacao (cd_matricula,"+
			                                  "cd_movimentacao,"+
			                                  "cd_tipo_movimentacao,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "nr_documento,"+
			                                  "st_movimentacao) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2, code);
			if(objeto.getCdTipoMovimentacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoMovimentacao());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(6,objeto.getNrDocumento());
			pstmt.setInt(7,objeto.getStMovimentacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaMovimentacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MatriculaMovimentacao objeto, int cdMatriculaOld, int cdMovimentacaoOld) {
		return update(objeto, cdMatriculaOld, cdMovimentacaoOld, null);
	}

	public static int update(MatriculaMovimentacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MatriculaMovimentacao objeto, int cdMatriculaOld, int cdMovimentacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_matricula_movimentacao SET cd_matricula=?,"+
												      		   "cd_movimentacao=?,"+
												      		   "cd_tipo_movimentacao=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "nr_documento=?,"+
												      		   "st_movimentacao=? WHERE cd_matricula=? AND cd_movimentacao=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdMovimentacao());
			if(objeto.getCdTipoMovimentacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoMovimentacao());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(6,objeto.getNrDocumento());
			pstmt.setInt(7,objeto.getStMovimentacao());
			pstmt.setInt(8, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(9, cdMovimentacaoOld!=0 ? cdMovimentacaoOld : objeto.getCdMovimentacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdMovimentacao) {
		return delete(cdMatricula, cdMovimentacao, null);
	}

	public static int delete(int cdMatricula, int cdMovimentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_matricula_movimentacao WHERE cd_matricula=? AND cd_movimentacao=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdMovimentacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaMovimentacao get(int cdMatricula, int cdMovimentacao) {
		return get(cdMatricula, cdMovimentacao, null);
	}

	public static MatriculaMovimentacao get(int cdMatricula, int cdMovimentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_matricula_movimentacao WHERE cd_matricula=? AND cd_movimentacao=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdMovimentacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaMovimentacao(rs.getInt("cd_matricula"),
						rs.getInt("cd_movimentacao"),
						rs.getInt("cd_tipo_movimentacao"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getString("nr_documento"),
						rs.getInt("st_movimentacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_matricula_movimentacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaMovimentacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_matricula_movimentacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
