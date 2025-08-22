package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class QuadroVagasDAO{

	public static int insert(QuadroVagas objeto) {
		return insert(objeto, null);
	}

	public static int insert(QuadroVagas objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_quadro_vagas");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_instituicao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdInstituicao()));
			int code = Conexao.getSequenceCode("acd_quadro_vagas", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdQuadroVagas(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_quadro_vagas (cd_quadro_vagas,"+
			                                  "cd_instituicao,"+
			                                  "cd_periodo_letivo,"+
			                                  "dt_cadastro,"+
			                                  "st_quadro_vagas,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStQuadroVagas());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(QuadroVagas objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(QuadroVagas objeto, int cdQuadroVagasOld, int cdInstituicaoOld) {
		return update(objeto, cdQuadroVagasOld, cdInstituicaoOld, null);
	}

	public static int update(QuadroVagas objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(QuadroVagas objeto, int cdQuadroVagasOld, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_quadro_vagas SET cd_quadro_vagas=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "dt_cadastro=?,"+
												      		   "st_quadro_vagas=?,"+
												      		   "txt_observacao=? WHERE cd_quadro_vagas=? AND cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdQuadroVagas());
			pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStQuadroVagas());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7, cdQuadroVagasOld!=0 ? cdQuadroVagasOld : objeto.getCdQuadroVagas());
			pstmt.setInt(8, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdQuadroVagas, int cdInstituicao) {
		return delete(cdQuadroVagas, cdInstituicao, null);
	}

	public static int delete(int cdQuadroVagas, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_quadro_vagas WHERE cd_quadro_vagas=? AND cd_instituicao=?");
			pstmt.setInt(1, cdQuadroVagas);
			pstmt.setInt(2, cdInstituicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static QuadroVagas get(int cdQuadroVagas, int cdInstituicao) {
		return get(cdQuadroVagas, cdInstituicao, null);
	}

	public static QuadroVagas get(int cdQuadroVagas, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas WHERE cd_quadro_vagas=? AND cd_instituicao=?");
			pstmt.setInt(1, cdQuadroVagas);
			pstmt.setInt(2, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new QuadroVagas(rs.getInt("cd_quadro_vagas"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_periodo_letivo"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getInt("st_quadro_vagas"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<QuadroVagas> getList() {
		return getList(null);
	}

	public static ArrayList<QuadroVagas> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<QuadroVagas> list = new ArrayList<QuadroVagas>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				QuadroVagas obj = QuadroVagasDAO.get(rsm.getInt("cd_quadro_vagas"), rsm.getInt("cd_instituicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuadroVagasDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_quadro_vagas", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
