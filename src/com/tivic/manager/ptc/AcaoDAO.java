package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class AcaoDAO{

	public static int insert(Acao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Acao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_acao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_decisao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdDecisao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_fluxo");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdFluxo()));
			int code = Conexao.getSequenceCode("ptc_acao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAcao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_acao (cd_acao,"+
			                                  "tp_acao,"+
			                                  "lg_automatico,"+
			                                  "cd_setor,"+
			                                  "cd_decisao,"+
			                                  "cd_fluxo,"+
			                                  "cd_tipo_pendencia,"+
			                                  "cd_tipo_ocorrencia,"+
			                                  "cd_fase) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getTpAcao());
			pstmt.setInt(3,objeto.getLgAutomatico());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSetor());
			if(objeto.getCdDecisao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDecisao());
			if(objeto.getCdFluxo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFluxo());
			if(objeto.getCdTipoPendencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoPendencia());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipoOcorrencia());
			if(objeto.getCdFase()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdFase());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Acao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(Acao objeto, int cdAcaoOld, int cdDecisaoOld, int cdFluxoOld) {
		return update(objeto, cdAcaoOld, cdDecisaoOld, cdFluxoOld, null);
	}

	public static int update(Acao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(Acao objeto, int cdAcaoOld, int cdDecisaoOld, int cdFluxoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_acao SET cd_acao=?,"+
												      		   "tp_acao=?,"+
												      		   "lg_automatico=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_decisao=?,"+
												      		   "cd_fluxo=?,"+
												      		   "cd_tipo_pendencia=?,"+
												      		   "cd_tipo_ocorrencia=?,"+
												      		   "cd_fase=? WHERE cd_acao=? AND cd_decisao=? AND cd_fluxo=?");
			pstmt.setInt(1,objeto.getCdAcao());
			pstmt.setInt(2,objeto.getTpAcao());
			pstmt.setInt(3,objeto.getLgAutomatico());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSetor());
			pstmt.setInt(5,objeto.getCdDecisao());
			pstmt.setInt(6,objeto.getCdFluxo());
			if(objeto.getCdTipoPendencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoPendencia());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipoOcorrencia());
			if(objeto.getCdFase()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdFase());
			pstmt.setInt(10, cdAcaoOld!=0 ? cdAcaoOld : objeto.getCdAcao());
			pstmt.setInt(11, cdDecisaoOld!=0 ? cdDecisaoOld : objeto.getCdDecisao());
			pstmt.setInt(12, cdFluxoOld!=0 ? cdFluxoOld : objeto.getCdFluxo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAcao, int cdDecisao, int cdFluxo) {
		return delete(cdAcao, cdDecisao, cdFluxo, null);
	}

	public static int delete(int cdAcao, int cdDecisao, int cdFluxo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_acao WHERE cd_acao=? AND cd_decisao=? AND cd_fluxo=?");
			pstmt.setInt(1, cdAcao);
			pstmt.setInt(2, cdDecisao);
			pstmt.setInt(3, cdFluxo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Acao get(int cdAcao, int cdDecisao, int cdFluxo) {
		return get(cdAcao, cdDecisao, cdFluxo, null);
	}

	public static Acao get(int cdAcao, int cdDecisao, int cdFluxo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_acao WHERE cd_acao=? AND cd_decisao=? AND cd_fluxo=?");
			pstmt.setInt(1, cdAcao);
			pstmt.setInt(2, cdDecisao);
			pstmt.setInt(3, cdFluxo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Acao(rs.getInt("cd_acao"),
						rs.getInt("tp_acao"),
						rs.getInt("lg_automatico"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_decisao"),
						rs.getInt("cd_fluxo"),
						rs.getInt("cd_tipo_pendencia"),
						rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("cd_fase"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_acao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Acao> getList() {
		return getList(null);
	}

	public static ArrayList<Acao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Acao> list = new ArrayList<Acao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Acao obj = AcaoDAO.get(rsm.getInt("cd_acao"), rsm.getInt("cd_decisao"), rsm.getInt("cd_fluxo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_acao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
