package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class InstituicaoEducacensoArquivoDAO{

	public static int insert(InstituicaoEducacensoArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoEducacensoArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_educacenso_arquivo (cd_instituicao,"+
			                                  "cd_periodo_letivo,"+
			                                  "pr_registro,"+
			                                  "pr_campos,"+
			                                  "dt_atualizacao,"+
			                                  "lg_execucao) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPeriodoLetivo());
			pstmt.setInt(3,objeto.getPrRegistro());
			pstmt.setFloat(4,objeto.getPrCampos());
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setInt(6,objeto.getLgExecucao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoEducacensoArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(InstituicaoEducacensoArquivo objeto, int cdInstituicaoOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoEducacensoArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(InstituicaoEducacensoArquivo objeto, int cdInstituicaoOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_educacenso_arquivo SET cd_instituicao=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "pr_registro=?,"+
												      		   "pr_campos=?,"+
												      		   "dt_atualizacao=?,"+
												      		   "lg_execucao=? WHERE cd_instituicao=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdPeriodoLetivo());
			pstmt.setInt(3,objeto.getPrRegistro());
			pstmt.setFloat(4,objeto.getPrCampos());
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setInt(6,objeto.getLgExecucao());
			pstmt.setInt(7, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(8, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_educacenso_arquivo WHERE cd_instituicao=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoEducacensoArquivo get(int cdInstituicao, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdPeriodoLetivo, null);
	}

	public static InstituicaoEducacensoArquivo get(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso_arquivo WHERE cd_instituicao=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoEducacensoArquivo(rs.getInt("cd_instituicao"),
						rs.getInt("cd_periodo_letivo"),
						rs.getInt("pr_registro"),
						rs.getInt("pr_campos"),
						(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()),
						rs.getInt("lg_execucao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoEducacensoArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoEducacensoArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoEducacensoArquivo> list = new ArrayList<InstituicaoEducacensoArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoEducacensoArquivo obj = InstituicaoEducacensoArquivoDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_educacenso_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}