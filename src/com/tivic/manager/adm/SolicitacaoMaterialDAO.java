package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SolicitacaoMaterialDAO{

	public static int insert(SolicitacaoMaterial objeto) {
		return insert(objeto, null);
	}

	public static int insert(SolicitacaoMaterial objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_solicitacao_material", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSolicitacaoMaterial(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_solicitacao_material (cd_solicitacao_material,"+
			                                  "cd_setor_solicitante,"+
			                                  "dt_solicitacao,"+
			                                  "st_solicitacao_material,"+
			                                  "id_solicitacao_material,"+
			                                  "cd_documento,"+
			                                  "txt_observacao,"+
			                                  "cd_solicitante,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdSetorSolicitante()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSetorSolicitante());
			if(objeto.getDtSolicitacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtSolicitacao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStSolicitacaoMaterial());
			pstmt.setString(5,objeto.getIdSolicitacaoMaterial());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumento());
			pstmt.setString(7,objeto.getTxtObservacao());
			if(objeto.getCdSolicitante()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSolicitante());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SolicitacaoMaterial objeto) {
		return update(objeto, 0, null);
	}

	public static int update(SolicitacaoMaterial objeto, int cdSolicitacaoMaterialOld) {
		return update(objeto, cdSolicitacaoMaterialOld, null);
	}

	public static int update(SolicitacaoMaterial objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(SolicitacaoMaterial objeto, int cdSolicitacaoMaterialOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_solicitacao_material SET cd_solicitacao_material=?,"+
												      		   "cd_setor_solicitante=?,"+
												      		   "dt_solicitacao=?,"+
												      		   "st_solicitacao_material=?,"+
												      		   "id_solicitacao_material=?,"+
												      		   "cd_documento=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_solicitante=?,"+
												      		   "cd_empresa=? WHERE cd_solicitacao_material=?");
			pstmt.setInt(1,objeto.getCdSolicitacaoMaterial());
			if(objeto.getCdSetorSolicitante()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSetorSolicitante());
			if(objeto.getDtSolicitacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtSolicitacao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStSolicitacaoMaterial());
			pstmt.setString(5,objeto.getIdSolicitacaoMaterial());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumento());
			pstmt.setString(7,objeto.getTxtObservacao());
			if(objeto.getCdSolicitante()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSolicitante());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdEmpresa());
			pstmt.setInt(10, cdSolicitacaoMaterialOld!=0 ? cdSolicitacaoMaterialOld : objeto.getCdSolicitacaoMaterial());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSolicitacaoMaterial) {
		return delete(cdSolicitacaoMaterial, null);
	}

	public static int delete(int cdSolicitacaoMaterial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_solicitacao_material WHERE cd_solicitacao_material=?");
			pstmt.setInt(1, cdSolicitacaoMaterial);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SolicitacaoMaterial get(int cdSolicitacaoMaterial) {
		return get(cdSolicitacaoMaterial, null);
	}

	public static SolicitacaoMaterial get(int cdSolicitacaoMaterial, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_solicitacao_material WHERE cd_solicitacao_material=?");
			pstmt.setInt(1, cdSolicitacaoMaterial);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SolicitacaoMaterial(rs.getInt("cd_solicitacao_material"),
						rs.getInt("cd_setor_solicitante"),
						(rs.getTimestamp("dt_solicitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_solicitacao").getTime()),
						rs.getInt("st_solicitacao_material"),
						rs.getString("id_solicitacao_material"),
						rs.getInt("cd_documento"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_solicitante"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_solicitacao_material");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_solicitacao_material", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
