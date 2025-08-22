package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProcessoAtividadeDAO{

	public static int insert(ProcessoAtividade objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ProcessoAtividade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_atividade");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_processo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProcesso()));
			int code = Conexao.getSequenceCode("grl_processo_atividade", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAtividade(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_processo_atividade (cd_atividade,"+
			                                  "cd_processo,"+
			                                  "cd_atividade_requisito,"+
			                                  "nm_atividade,"+
			                                  "txt_atividade,"+
			                                  "cd_setor,"+
			                                  "tp_unidade_tempo,"+
			                                  "qt_duracao_maxima,"+
			                                  "lg_obrigatorio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getCdAtividadeRequisito()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAtividadeRequisito());
			pstmt.setString(4,objeto.getNmAtividade());
			pstmt.setString(5,objeto.getTxtAtividade());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdSetor());
			pstmt.setInt(7,objeto.getTpUnidadeTempo());
			pstmt.setInt(8,objeto.getQtDuracaoMaxima());
			pstmt.setInt(9,objeto.getLgObrigatorio());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoAtividade objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProcessoAtividade objeto, int cdAtividadeOld, int cdProcessoOld) {
		return update(objeto, cdAtividadeOld, cdProcessoOld, null);
	}

	public static int update(ProcessoAtividade objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProcessoAtividade objeto, int cdAtividadeOld, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_processo_atividade SET cd_atividade=?,"+
												      		   "cd_processo=?,"+
												      		   "cd_atividade_requisito=?,"+
												      		   "nm_atividade=?,"+
												      		   "txt_atividade=?,"+
												      		   "cd_setor=?,"+
												      		   "tp_unidade_tempo=?,"+
												      		   "qt_duracao_maxima=?,"+
												      		   "lg_obrigatorio=? WHERE cd_atividade=? AND cd_processo=?");
			pstmt.setInt(1,objeto.getCdAtividade());
			pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getCdAtividadeRequisito()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAtividadeRequisito());
			pstmt.setString(4,objeto.getNmAtividade());
			pstmt.setString(5,objeto.getTxtAtividade());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdSetor());
			pstmt.setInt(7,objeto.getTpUnidadeTempo());
			pstmt.setInt(8,objeto.getQtDuracaoMaxima());
			pstmt.setInt(9,objeto.getLgObrigatorio());
			pstmt.setInt(10, cdAtividadeOld!=0 ? cdAtividadeOld : objeto.getCdAtividade());
			pstmt.setInt(11, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtividade, int cdProcesso) {
		return delete(cdAtividade, cdProcesso, null);
	}

	public static int delete(int cdAtividade, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_processo_atividade WHERE cd_atividade=? AND cd_processo=?");
			pstmt.setInt(1, cdAtividade);
			pstmt.setInt(2, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoAtividade get(int cdAtividade, int cdProcesso) {
		return get(cdAtividade, cdProcesso, null);
	}

	public static ProcessoAtividade get(int cdAtividade, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_processo_atividade WHERE cd_atividade=? AND cd_processo=?");
			pstmt.setInt(1, cdAtividade);
			pstmt.setInt(2, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoAtividade(rs.getInt("cd_atividade"),
						rs.getInt("cd_processo"),
						rs.getInt("cd_atividade_requisito"),
						rs.getString("nm_atividade"),
						rs.getString("txt_atividade"),
						rs.getInt("cd_setor"),
						rs.getInt("tp_unidade_tempo"),
						rs.getInt("qt_duracao_maxima"),
						rs.getInt("lg_obrigatorio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_processo_atividade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAtividadeDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_processo_atividade", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
