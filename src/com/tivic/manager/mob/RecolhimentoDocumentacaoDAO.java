package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class RecolhimentoDocumentacaoDAO{

	public static int insert(RecolhimentoDocumentacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(RecolhimentoDocumentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_recolhimento_documentacao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_tipo_documentacao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdTipoDocumentacao()));
			int code = Conexao.getSequenceCode("mob_recolhimento_documentacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRecolhimentoDocumentacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_recolhimento_documentacao (cd_recolhimento_documentacao,"+
			                                  "cd_rrd,"+
			                                  "cd_tipo_documentacao,"+
			                                  "dt_devolucao,"+
			                                  "cd_usuario,"+
			                                  "cd_agente,"+
			                                  "nr_documento,"+
			                                  "cd_trrav) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdRrd()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRrd());
			if(objeto.getCdTipoDocumentacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoDocumentacao());
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getCdUsuario());
			pstmt.setInt(6,objeto.getCdAgente());
			pstmt.setString(7,objeto.getNrDocumento());
			if(objeto.getCdTrrav()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTrrav());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RecolhimentoDocumentacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RecolhimentoDocumentacao objeto, int cdRecolhimentoDocumentacaoOld, int cdTipoDocumentacaoOld) {
		return update(objeto, cdRecolhimentoDocumentacaoOld, cdTipoDocumentacaoOld, null);
	}

	public static int update(RecolhimentoDocumentacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RecolhimentoDocumentacao objeto, int cdRecolhimentoDocumentacaoOld, int cdTipoDocumentacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_recolhimento_documentacao SET cd_recolhimento_documentacao=?,"+
												      		   "cd_rrd=?,"+
												      		   "cd_tipo_documentacao=?,"+
												      		   "dt_devolucao=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_agente=?,"+
												      		   "nr_documento=?,"+
												      		   "cd_trrav=? WHERE cd_recolhimento_documentacao=? AND cd_tipo_documentacao=?");
			pstmt.setInt(1,objeto.getCdRecolhimentoDocumentacao());
			if(objeto.getCdRrd()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRrd());
			pstmt.setInt(3,objeto.getCdTipoDocumentacao());
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getCdUsuario());
			pstmt.setInt(6,objeto.getCdAgente());
			pstmt.setString(7,objeto.getNrDocumento());
			if(objeto.getCdTrrav()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTrrav());
			pstmt.setInt(9, cdRecolhimentoDocumentacaoOld!=0 ? cdRecolhimentoDocumentacaoOld : objeto.getCdRecolhimentoDocumentacao());
			pstmt.setInt(10, cdTipoDocumentacaoOld!=0 ? cdTipoDocumentacaoOld : objeto.getCdTipoDocumentacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRecolhimentoDocumentacao, int cdTipoDocumentacao) {
		return delete(cdRecolhimentoDocumentacao, cdTipoDocumentacao, null);
	}

	public static int delete(int cdRecolhimentoDocumentacao, int cdTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_recolhimento_documentacao WHERE cd_recolhimento_documentacao=? AND cd_tipo_documentacao=?");
			pstmt.setInt(1, cdRecolhimentoDocumentacao);
			pstmt.setInt(2, cdTipoDocumentacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RecolhimentoDocumentacao get(int cdRecolhimentoDocumentacao, int cdTipoDocumentacao) {
		return get(cdRecolhimentoDocumentacao, cdTipoDocumentacao, null);
	}

	public static RecolhimentoDocumentacao get(int cdRecolhimentoDocumentacao, int cdTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_recolhimento_documentacao WHERE cd_recolhimento_documentacao=? AND cd_tipo_documentacao=?");
			pstmt.setInt(1, cdRecolhimentoDocumentacao);
			pstmt.setInt(2, cdTipoDocumentacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RecolhimentoDocumentacao(rs.getInt("cd_recolhimento_documentacao"),
						rs.getInt("cd_rrd"),
						rs.getInt("cd_tipo_documentacao"),
						(rs.getTimestamp("dt_devolucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_devolucao").getTime()),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_agente"),
						rs.getString("nr_documento"),
						rs.getInt("cd_trrav"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_recolhimento_documentacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RecolhimentoDocumentacao> getList() {
		return getList(null);
	}

	public static ArrayList<RecolhimentoDocumentacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RecolhimentoDocumentacao> list = new ArrayList<RecolhimentoDocumentacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RecolhimentoDocumentacao obj = RecolhimentoDocumentacaoDAO.get(rsm.getInt("cd_recolhimento_documentacao"), rsm.getInt("cd_tipo_documentacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecolhimentoDocumentacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_recolhimento_documentacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}