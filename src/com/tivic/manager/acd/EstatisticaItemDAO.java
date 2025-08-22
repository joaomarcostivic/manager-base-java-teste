package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class EstatisticaItemDAO{

	public static int insert(EstatisticaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(EstatisticaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[4];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_item");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_apuracao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdApuracao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_periodo_letivo");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdPeriodoLetivo()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_instituicao");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdInstituicao()));
			int code = Conexao.getSequenceCode("acd_estatistica_item", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_estatistica_item (cd_item,"+
			                                  "cd_apuracao,"+
			                                  "cd_periodo_letivo,"+
			                                  "cd_instituicao,"+
			                                  "tp_item,"+
			                                  "tp_qualificacao,"+
			                                  "cd_curso,"+
			                                  "cd_tipo_necessidade_especial,"+
			                                  "qt_apuracao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdApuracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdApuracao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdInstituicao());
			pstmt.setInt(5,objeto.getTpItem());
			pstmt.setInt(6,objeto.getTpQualificacao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCurso());
			if(objeto.getCdTipoNecessidadeEspecial()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipoNecessidadeEspecial());
			pstmt.setInt(9,objeto.getQtApuracao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EstatisticaItem objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(EstatisticaItem objeto, int cdItemOld, int cdApuracaoOld, int cdPeriodoLetivoOld, int cdInstituicaoOld) {
		return update(objeto, cdItemOld, cdApuracaoOld, cdPeriodoLetivoOld, cdInstituicaoOld, null);
	}

	public static int update(EstatisticaItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(EstatisticaItem objeto, int cdItemOld, int cdApuracaoOld, int cdPeriodoLetivoOld, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_estatistica_item SET cd_item=?,"+
												      		   "cd_apuracao=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "cd_instituicao=?,"+
												      		   "tp_item=?,"+
												      		   "tp_qualificacao=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_tipo_necessidade_especial=?,"+
												      		   "qt_apuracao=? WHERE cd_item=? AND cd_apuracao=? AND cd_periodo_letivo=? AND cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdItem());
			pstmt.setInt(2,objeto.getCdApuracao());
			pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setInt(4,objeto.getCdInstituicao());
			pstmt.setInt(5,objeto.getTpItem());
			pstmt.setInt(6,objeto.getTpQualificacao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCurso());
			if(objeto.getCdTipoNecessidadeEspecial()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdTipoNecessidadeEspecial());
			pstmt.setInt(9,objeto.getQtApuracao());
			pstmt.setInt(10, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.setInt(11, cdApuracaoOld!=0 ? cdApuracaoOld : objeto.getCdApuracao());
			pstmt.setInt(12, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.setInt(13, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdItem, int cdApuracao, int cdPeriodoLetivo, int cdInstituicao) {
		return delete(cdItem, cdApuracao, cdPeriodoLetivo, cdInstituicao, null);
	}

	public static int delete(int cdItem, int cdApuracao, int cdPeriodoLetivo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_estatistica_item WHERE cd_item=? AND cd_apuracao=? AND cd_periodo_letivo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdItem);
			pstmt.setInt(2, cdApuracao);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.setInt(4, cdInstituicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EstatisticaItem get(int cdItem, int cdApuracao, int cdPeriodoLetivo, int cdInstituicao) {
		return get(cdItem, cdApuracao, cdPeriodoLetivo, cdInstituicao, null);
	}

	public static EstatisticaItem get(int cdItem, int cdApuracao, int cdPeriodoLetivo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_estatistica_item WHERE cd_item=? AND cd_apuracao=? AND cd_periodo_letivo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdItem);
			pstmt.setInt(2, cdApuracao);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.setInt(4, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EstatisticaItem(rs.getInt("cd_item"),
						rs.getInt("cd_apuracao"),
						rs.getInt("cd_periodo_letivo"),
						rs.getInt("cd_instituicao"),
						rs.getInt("tp_item"),
						rs.getInt("tp_qualificacao"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_tipo_necessidade_especial"),
						rs.getInt("qt_apuracao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_estatistica_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EstatisticaItem> getList() {
		return getList(null);
	}

	public static ArrayList<EstatisticaItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EstatisticaItem> list = new ArrayList<EstatisticaItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EstatisticaItem obj = EstatisticaItemDAO.get(rsm.getInt("cd_item"), rsm.getInt("cd_apuracao"), rsm.getInt("cd_periodo_letivo"), rsm.getInt("cd_instituicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_estatistica_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}