package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class InstituicaoEducacensoArquivoRegistroDAO{

	public static int insert(InstituicaoEducacensoArquivoRegistro objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoEducacensoArquivoRegistro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_instituicao");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdInstituicao()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_periodo_letivo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPeriodoLetivo()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_registro");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("acd_instituicao_educacenso_arquivo_registro", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegistro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_educacenso_arquivo_registro (cd_instituicao,"+
			                                  "cd_periodo_letivo,"+
			                                  "cd_registro,"+
			                                  "tp_registro,"+
			                                  "txt_registro) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPeriodoLetivo());
			pstmt.setInt(3, code);
			pstmt.setString(4,objeto.getTpRegistro());
			pstmt.setString(5,objeto.getTxtRegistro());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoEducacensoArquivoRegistro objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoEducacensoArquivoRegistro objeto, int cdInstituicaoOld, int cdPeriodoLetivoOld, int cdRegistroOld) {
		return update(objeto, cdInstituicaoOld, cdPeriodoLetivoOld, cdRegistroOld, null);
	}

	public static int update(InstituicaoEducacensoArquivoRegistro objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoEducacensoArquivoRegistro objeto, int cdInstituicaoOld, int cdPeriodoLetivoOld, int cdRegistroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_educacenso_arquivo_registro SET cd_instituicao=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "cd_registro=?,"+
												      		   "tp_registro=?,"+
												      		   "txt_registro=? WHERE cd_instituicao=? AND cd_periodo_letivo=? AND cd_registro=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdPeriodoLetivo());
			pstmt.setInt(3,objeto.getCdRegistro());
			pstmt.setString(4,objeto.getTpRegistro());
			pstmt.setString(5,objeto.getTxtRegistro());
			pstmt.setInt(6, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(7, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.setInt(8, cdRegistroOld!=0 ? cdRegistroOld : objeto.getCdRegistro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdPeriodoLetivo, int cdRegistro) {
		return delete(cdInstituicao, cdPeriodoLetivo, cdRegistro, null);
	}

	public static int delete(int cdInstituicao, int cdPeriodoLetivo, int cdRegistro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_educacenso_arquivo_registro WHERE cd_instituicao=? AND cd_periodo_letivo=? AND cd_registro=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdPeriodoLetivo);
			pstmt.setInt(3, cdRegistro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoEducacensoArquivoRegistro get(int cdInstituicao, int cdPeriodoLetivo, int cdRegistro) {
		return get(cdInstituicao, cdPeriodoLetivo, cdRegistro, null);
	}

	public static InstituicaoEducacensoArquivoRegistro get(int cdInstituicao, int cdPeriodoLetivo, int cdRegistro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso_arquivo_registro WHERE cd_instituicao=? AND cd_periodo_letivo=? AND cd_registro=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdPeriodoLetivo);
			pstmt.setInt(3, cdRegistro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoEducacensoArquivoRegistro(rs.getInt("cd_instituicao"),
						rs.getInt("cd_periodo_letivo"),
						rs.getInt("cd_registro"),
						rs.getString("tp_registro"),
						rs.getString("txt_registro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso_arquivo_registro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoEducacensoArquivoRegistro> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoEducacensoArquivoRegistro> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoEducacensoArquivoRegistro> list = new ArrayList<InstituicaoEducacensoArquivoRegistro>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoEducacensoArquivoRegistro obj = InstituicaoEducacensoArquivoRegistroDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo"), rsm.getInt("cd_registro"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoRegistroDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_educacenso_arquivo_registro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}