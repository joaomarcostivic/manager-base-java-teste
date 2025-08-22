package com.tivic.manager.srh;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class LotacaoDAO{

	public static int insert(Lotacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Lotacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_lotacao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_matricula");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMatricula()));
			int code = Conexao.getSequenceCode("srh_lotacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLotacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_lotacao (cd_lotacao,"+
			                                  "cd_matricula,"+
			                                  "cd_setor,"+
			                                  "cd_funcao,"+
			                                  "dt_lotacao,"+
			                                  "dt_remocao,"+
			                                  "vl_carga_horaria,"+
			                                  "id_lotacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatricula());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdFuncao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFuncao());
			if(objeto.getDtLotacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtLotacao().getTimeInMillis()));
			if(objeto.getDtRemocao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRemocao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getVlCargaHoraria());
			pstmt.setString(8,objeto.getIdLotacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Lotacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Lotacao objeto, int cdLotacaoOld, int cdMatriculaOld) {
		return update(objeto, cdLotacaoOld, cdMatriculaOld, null);
	}

	public static int update(Lotacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Lotacao objeto, int cdLotacaoOld, int cdMatriculaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_lotacao SET cd_lotacao=?,"+
												      		   "cd_matricula=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_funcao=?,"+
												      		   "dt_lotacao=?,"+
												      		   "dt_remocao=?,"+
												      		   "vl_carga_horaria=?,"+
												      		   "id_lotacao=? WHERE cd_lotacao=? AND cd_matricula=?");
			pstmt.setInt(1,objeto.getCdLotacao());
			pstmt.setInt(2,objeto.getCdMatricula());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdFuncao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFuncao());
			if(objeto.getDtLotacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtLotacao().getTimeInMillis()));
			if(objeto.getDtRemocao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtRemocao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getVlCargaHoraria());
			pstmt.setString(8,objeto.getIdLotacao());
			pstmt.setInt(9, cdLotacaoOld!=0 ? cdLotacaoOld : objeto.getCdLotacao());
			pstmt.setInt(10, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLotacao, int cdMatricula) {
		return delete(cdLotacao, cdMatricula, null);
	}

	public static int delete(int cdLotacao, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_lotacao WHERE cd_lotacao=? AND cd_matricula=?");
			pstmt.setInt(1, cdLotacao);
			pstmt.setInt(2, cdMatricula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Lotacao get(int cdLotacao, int cdMatricula) {
		return get(cdLotacao, cdMatricula, null);
	}

	public static Lotacao get(int cdLotacao, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_lotacao WHERE cd_lotacao=? AND cd_matricula=?");
			pstmt.setInt(1, cdLotacao);
			pstmt.setInt(2, cdMatricula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Lotacao(rs.getInt("cd_lotacao"),
						rs.getInt("cd_matricula"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_funcao"),
						(rs.getTimestamp("dt_lotacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lotacao").getTime()),
						(rs.getTimestamp("dt_remocao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_remocao").getTime()),
						rs.getInt("vl_carga_horaria"),
						rs.getString("id_lotacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_lotacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Lotacao> getList() {
		return getList(null);
	}

	public static ArrayList<Lotacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Lotacao> list = new ArrayList<Lotacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Lotacao obj = LotacaoDAO.get(rsm.getInt("cd_lotacao"), rsm.getInt("cd_matricula"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LotacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM srh_lotacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
