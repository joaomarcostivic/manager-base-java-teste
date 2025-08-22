package com.tivic.manager.mob.grafica;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LoteImpressaoAitDAO{

	public static int insert(LoteImpressaoAit objeto) {
		return insert(objeto, null);
	}

	public static int insert(LoteImpressaoAit objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_lote_impressao_ait (cd_lote_impressao,"+
			                                  "cd_ait,"+
			                                  "id_lote_impressao_ait,"+
			                                  "st_impressao,"+
			                                  "dt_inclusao,"+
			                                  "dt_impressao,"+
			                                  "dt_envio,"+
			                                  "txt_erro,"+
			                                  "cd_arquivo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdLoteImpressao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLoteImpressao());
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			pstmt.setString(3,objeto.getIdLoteImpressaoAit());
			pstmt.setInt(4,objeto.getStImpressao());
			if(objeto.getDtInclusao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInclusao().getTimeInMillis()));
			if(objeto.getDtImpressao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtImpressao().getTimeInMillis()));
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			if(objeto.getTxtErro()== null)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setString(8,objeto.getTxtErro());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LoteImpressaoAit objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(LoteImpressaoAit objeto, int cdLoteImpressaoOld, int cdAitOld) {
		return update(objeto, cdLoteImpressaoOld, cdAitOld, null);
	}

	public static int update(LoteImpressaoAit objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(LoteImpressaoAit objeto, int cdLoteImpressaoOld, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_lote_impressao_ait SET cd_lote_impressao=?,"+
												      		   "cd_ait=?,"+
												      		   "id_lote_impressao_ait=?,"+
												      		   "st_impressao=?,"+
												      		   "dt_inclusao=?,"+
												      		   "dt_impressao=?,"+
												      		   "dt_envio=?,"+
												      		   "txt_erro=?,"+
												      		   "cd_arquivo=? WHERE cd_lote_impressao=? AND cd_ait=?");
			pstmt.setInt(1,objeto.getCdLoteImpressao());
			pstmt.setInt(2,objeto.getCdAit());
			pstmt.setString(3,objeto.getIdLoteImpressaoAit());
			pstmt.setInt(4,objeto.getStImpressao());
			if(objeto.getDtInclusao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInclusao().getTimeInMillis()));
			if(objeto.getDtImpressao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtImpressao().getTimeInMillis()));
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			if(objeto.getTxtErro()== null)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setString(8,objeto.getTxtErro());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdArquivo());
			pstmt.setInt(10, cdLoteImpressaoOld!=0 ? cdLoteImpressaoOld : objeto.getCdLoteImpressao());
			pstmt.setInt(11, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLoteImpressao, int cdAit) {
		return delete(cdLoteImpressao, cdAit, null);
	}

	public static int delete(int cdLoteImpressao, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_lote_impressao_ait WHERE cd_lote_impressao=? AND cd_ait=?");
			pstmt.setInt(1, cdLoteImpressao);
			pstmt.setInt(2, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LoteImpressaoAit get(int cdLoteImpressao, int cdAit) {
		return get(cdLoteImpressao, cdAit, null);
	}

	public static LoteImpressaoAit get(int cdLoteImpressao, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_impressao_ait WHERE cd_lote_impressao=? AND cd_ait=?");
			pstmt.setInt(1, cdLoteImpressao);
			pstmt.setInt(2, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LoteImpressaoAit(rs.getInt("cd_lote_impressao"),
						rs.getInt("cd_ait"),
						rs.getString("id_lote_impressao_ait"),
						rs.getInt("st_impressao"),
						(rs.getTimestamp("dt_inclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inclusao").getTime()),
						(rs.getTimestamp("dt_impressao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_impressao").getTime()),
						(rs.getTimestamp("dt_envio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_envio").getTime()),
						rs.getInt("cd_arquivo"),
						rs.getString("txt_erro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_impressao_ait");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LoteImpressaoAit> getList() {
		return getList(null);
	}

	public static ArrayList<LoteImpressaoAit> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LoteImpressaoAit> list = new ArrayList<LoteImpressaoAit>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LoteImpressaoAit obj = LoteImpressaoAitDAO.get(rsm.getInt("cd_lote_impressao"), rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoAitDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_lote_impressao_ait A", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
