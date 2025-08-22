package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoPrdSrvDepDAO{

	public static int insert(ContratoPrdSrvDep objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContratoPrdSrvDep objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_prd_srv_dep (cd_contrato,"+
			                                  "cd_produto_servico,"+
			                                  "cd_dependente,"+
			                                  "cd_dependencia,"+
			                                  "dt_contratacao,"+
			                                  "dt_primeira_parcela) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdDependente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDependente());
			if(objeto.getCdDependencia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDependencia());
			if(objeto.getDtContratacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtContratacao().getTimeInMillis()));
			if(objeto.getDtPrimeiraParcela()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPrimeiraParcela().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoPrdSrvDep objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(ContratoPrdSrvDep objeto, int cdContratoOld, int cdProdutoServicoOld, int cdDependenteOld, int cdDependenciaOld) {
		return update(objeto, cdContratoOld, cdProdutoServicoOld, cdDependenteOld, cdDependenciaOld, null);
	}

	public static int update(ContratoPrdSrvDep objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(ContratoPrdSrvDep objeto, int cdContratoOld, int cdProdutoServicoOld, int cdDependenteOld, int cdDependenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_prd_srv_dep SET cd_contrato=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_dependente=?,"+
												      		   "cd_dependencia=?,"+
												      		   "dt_contratacao=?,"+
												      		   "dt_primeira_parcela=? WHERE cd_contrato=? AND cd_produto_servico=? AND cd_dependente=? AND cd_dependencia=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdDependente());
			pstmt.setInt(4,objeto.getCdDependencia());
			if(objeto.getDtContratacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtContratacao().getTimeInMillis()));
			if(objeto.getDtPrimeiraParcela()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPrimeiraParcela().getTimeInMillis()));
			pstmt.setInt(7, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(8, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(9, cdDependenteOld!=0 ? cdDependenteOld : objeto.getCdDependente());
			pstmt.setInt(10, cdDependenciaOld!=0 ? cdDependenciaOld : objeto.getCdDependencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdProdutoServico, int cdDependente, int cdDependencia) {
		return delete(cdContrato, cdProdutoServico, cdDependente, cdDependencia, null);
	}

	public static int delete(int cdContrato, int cdProdutoServico, int cdDependente, int cdDependencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_prd_srv_dep WHERE cd_contrato=? AND cd_produto_servico=? AND cd_dependente=? AND cd_dependencia=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdDependente);
			pstmt.setInt(4, cdDependencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoPrdSrvDep get(int cdContrato, int cdProdutoServico, int cdDependente, int cdDependencia) {
		return get(cdContrato, cdProdutoServico, cdDependente, cdDependencia, null);
	}

	public static ContratoPrdSrvDep get(int cdContrato, int cdProdutoServico, int cdDependente, int cdDependencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_prd_srv_dep WHERE cd_contrato=? AND cd_produto_servico=? AND cd_dependente=? AND cd_dependencia=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdDependente);
			pstmt.setInt(4, cdDependencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoPrdSrvDep(rs.getInt("cd_contrato"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_dependente"),
						rs.getInt("cd_dependencia"),
						(rs.getTimestamp("dt_contratacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_contratacao").getTime()),
						(rs.getTimestamp("dt_primeira_parcela")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeira_parcela").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_prd_srv_dep");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoPrdSrvDepDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_prd_srv_dep", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
