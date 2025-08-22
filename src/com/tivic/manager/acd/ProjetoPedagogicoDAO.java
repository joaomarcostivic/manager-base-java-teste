package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ProjetoPedagogicoDAO{

	public static int insert(ProjetoPedagogico objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProjetoPedagogico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_projeto_pedagogico");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_instituicao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdInstituicao()));
			int code = Conexao.getSequenceCode("acd_projeto_pedagogico", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProjetoPedagogico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_projeto_pedagogico (cd_projeto_pedagogico,"+
			                                  "cd_instituicao,"+
			                                  "dt_cadastro,"+
			                                  "dt_vigencia_inicial,"+
			                                  "dt_vigencia_final,"+
			                                  "txt_proposta,"+
			                                  "nm_projeto_pedagogico,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtVigenciaInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtVigenciaInicial().getTimeInMillis()));
			if(objeto.getDtVigenciaFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtVigenciaFinal().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtProposta());
			pstmt.setString(7,objeto.getNmProjetoPedagogico());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProjetoPedagogico objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProjetoPedagogico objeto, int cdProjetoPedagogicoOld, int cdInstituicaoOld) {
		return update(objeto, cdProjetoPedagogicoOld, cdInstituicaoOld, null);
	}

	public static int update(ProjetoPedagogico objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProjetoPedagogico objeto, int cdProjetoPedagogicoOld, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_projeto_pedagogico SET cd_projeto_pedagogico=?,"+
												      		   "cd_instituicao=?,"+
												      		   "dt_cadastro=?,"+
												      		   "dt_vigencia_inicial=?,"+
												      		   "dt_vigencia_final=?,"+
												      		   "txt_proposta=?,"+
												      		   "nm_projeto_pedagogico=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_projeto_pedagogico=? AND cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdProjetoPedagogico());
			pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtVigenciaInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtVigenciaInicial().getTimeInMillis()));
			if(objeto.getDtVigenciaFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtVigenciaFinal().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtProposta());
			pstmt.setString(7,objeto.getNmProjetoPedagogico());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPeriodoLetivo());
			pstmt.setInt(9, cdProjetoPedagogicoOld!=0 ? cdProjetoPedagogicoOld : objeto.getCdProjetoPedagogico());
			pstmt.setInt(10, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProjetoPedagogico, int cdInstituicao) {
		return delete(cdProjetoPedagogico, cdInstituicao, null);
	}

	public static int delete(int cdProjetoPedagogico, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_projeto_pedagogico WHERE cd_projeto_pedagogico=? AND cd_instituicao=?");
			pstmt.setInt(1, cdProjetoPedagogico);
			pstmt.setInt(2, cdInstituicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProjetoPedagogico get(int cdProjetoPedagogico, int cdInstituicao) {
		return get(cdProjetoPedagogico, cdInstituicao, null);
	}

	public static ProjetoPedagogico get(int cdProjetoPedagogico, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_projeto_pedagogico WHERE cd_projeto_pedagogico=? AND cd_instituicao=?");
			pstmt.setInt(1, cdProjetoPedagogico);
			pstmt.setInt(2, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProjetoPedagogico(rs.getInt("cd_projeto_pedagogico"),
						rs.getInt("cd_instituicao"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						(rs.getTimestamp("dt_vigencia_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vigencia_inicial").getTime()),
						(rs.getTimestamp("dt_vigencia_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vigencia_final").getTime()),
						rs.getString("txt_proposta"),
						rs.getString("nm_projeto_pedagogico"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_projeto_pedagogico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProjetoPedagogico> getList() {
		return getList(null);
	}

	public static ArrayList<ProjetoPedagogico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProjetoPedagogico> list = new ArrayList<ProjetoPedagogico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProjetoPedagogico obj = ProjetoPedagogicoDAO.get(rsm.getInt("cd_projeto_pedagogico"), rsm.getInt("cd_instituicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_projeto_pedagogico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}