package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class ArquivoMovimentoDAO{

	public static int insert(ArquivoMovimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(ArquivoMovimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_arquivo_movimento");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_movimento");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMovimento()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_ait");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdAit()));
			int code = Conexao.getSequenceCode("mob_arquivo_movimento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdArquivoMovimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_arquivo_movimento (cd_arquivo_movimento,"+
											  "cd_movimento,"+
			                                  "cd_ait,"+
			                                  "tp_arquivo,"+
			                                  "nr_remessa,"+
			                                  "nr_sequencial,"+
			                                  "tp_status,"+
			                                  "nr_erro,"+
			                                  "ds_entrada,"+
			                                  "ds_saida,"+
			                                  "tp_origem,"+
			                                  "dt_arquivo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::json, ?::json, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMovimento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimento());
			if(objeto.getCdAit()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAit());
			pstmt.setInt(4,objeto.getTpArquivo());
			pstmt.setInt(5,objeto.getNrRemessa());
			pstmt.setInt(6,objeto.getNrSequencial());
			pstmt.setInt(7,objeto.getTpStatus());
			pstmt.setString(8,objeto.getNrErro());
			System.out.println("Entrada = " + objeto.getDsEntrada());
			pstmt.setString(9,objeto.getDsEntrada());
			System.out.println("Saida = " + objeto.getDsSaida());
			pstmt.setString(10,objeto.getDsSaida());
			pstmt.setInt(11,objeto.getTpOrigem());
			if(objeto.getDtArquivo()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtArquivo().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ArquivoMovimento objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ArquivoMovimento objeto, int cdArquivoMovimentoOld, int cdMovimentoOld, int cdAitOld) {
		return update(objeto, cdArquivoMovimentoOld, cdMovimentoOld, cdAitOld, null);
	}

	public static int update(ArquivoMovimento objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ArquivoMovimento objeto, int cdArquivoMovimentoOld, int cdMovimentoOld, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_arquivo_movimento SET cd_arquivo_movimento=?,"+
															   "cd_movimento=?,"+
												      		   "cd_ait=?,"+
												      		   "tp_arquivo=?,"+
							                                   "nr_remessa=?,"+
							                                   "nr_sequencial=?,"+
							                                   "tp_status=?,"+
							                                   "nr_erro=?,"+
							                                   "ds_entrada=?::json,"+
							                                   "ds_saida=?::json,"+
							                                   "tp_origem=?,"+
												      		   "dt_arquivo=? WHERE cd_arquivo_movimento=? AND cd_movimento=? AND cd_ait=?");
			pstmt.setInt(1,objeto.getCdArquivoMovimento());
			pstmt.setInt(2,objeto.getCdMovimento());
			pstmt.setInt(3,objeto.getCdAit());
			pstmt.setInt(4,objeto.getTpArquivo());
			pstmt.setInt(5,objeto.getNrRemessa());
			pstmt.setInt(6,objeto.getNrSequencial());
			pstmt.setInt(7,objeto.getTpStatus());
			pstmt.setString(8,objeto.getNrErro());
			pstmt.setString(9,objeto.getDsEntrada());
			pstmt.setString(10,objeto.getDsSaida());
			pstmt.setInt(11,objeto.getTpOrigem());
			if(objeto.getDtArquivo()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtArquivo().getTimeInMillis()));
			pstmt.setInt(13, cdArquivoMovimentoOld!=0 ? cdArquivoMovimentoOld : objeto.getCdArquivoMovimento());
			pstmt.setInt(14, cdMovimentoOld!=0 ? cdMovimentoOld : objeto.getCdMovimento());
			pstmt.setInt(15, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdArquivoMovimento, int cdMovimento, int cdAit) {
		return delete(cdArquivoMovimento, cdMovimento, cdAit, null);
	}

	public static int delete(int cdArquivoMovimento, int cdMovimento, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_arquivo_movimento WHERE cd_arquivo_movimento=? AND cd_movimento=? AND cd_ait=?");
			pstmt.setInt(1, cdArquivoMovimento);
			pstmt.setInt(2, cdMovimento);
			pstmt.setInt(3, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArquivoMovimento get(int cdArquivoMovimento, int cdMovimento, int cdAit) {
		return get(cdArquivoMovimento, cdMovimento, cdAit, null);
	}

	public static ArquivoMovimento get(int cdArquivoMovimento, int cdMovimento, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_arquivo_movimento WHERE cd_movimento=? AND cd_ait=? AND cd_arquivo_movimento = ?");
			pstmt.setInt(1, cdMovimento);
			pstmt.setInt(2, cdAit);
			pstmt.setInt(3, cdArquivoMovimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ArquivoMovimento(rs.getInt("cd_arquivo_movimento"),
						rs.getInt("cd_movimento"),
						rs.getInt("cd_ait"),
						rs.getInt("tp_arquivo"),
						rs.getInt("nr_remessa"),
						rs.getInt("nr_sequencial"),
						rs.getInt("tp_status"),
						rs.getString("nr_erro"),
						rs.getString("ds_entrada"),
						rs.getString("ds_saida"),
						rs.getInt("tp_origem"),
						(rs.getTimestamp("dt_arquivo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_arquivo").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_arquivo_movimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ArquivoMovimento> getList() {
		return getList(null);
	}

	public static ArrayList<ArquivoMovimento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ArquivoMovimento> list = new ArrayList<ArquivoMovimento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ArquivoMovimento obj = ArquivoMovimentoDAO.get(rsm.getInt("cd_arquivo_movimento"), rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoMovimentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_arquivo_movimento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
