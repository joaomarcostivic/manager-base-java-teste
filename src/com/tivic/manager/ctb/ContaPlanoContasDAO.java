package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaPlanoContasDAO{

	public static int insert(ContaPlanoContas objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaPlanoContas objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_conta_plano_contas", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaPlanoContas(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_conta_plano_contas (cd_conta_plano_contas,"+
			                                  "cd_conta,"+
			                                  "cd_plano_contas,"+
			                                  "cd_conta_superior,"+
			                                  "nr_conta,"+
			                                  "nr_digito,"+
			                                  "tp_conta,"+
			                                  "tp_natureza,"+
			                                  "dt_inativacao,"+
			                                  "txt_observacao,"+
			                                  "nr_conta_externa,"+
			                                  "dt_cadastro,"+
			                                  "pr_depreciacao,"+
			                                  "lg_orcamentaria,"+
			                                  "id_conta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConta());
			if(objeto.getCdPlanoContas()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPlanoContas());
			if(objeto.getCdContaSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContaSuperior());
			pstmt.setString(5,objeto.getNrConta());
			pstmt.setInt(6,objeto.getNrDigito());
			pstmt.setInt(7,objeto.getTpConta());
			pstmt.setInt(8,objeto.getTpNatureza());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setString(10,objeto.getTxtObservacao());
			pstmt.setString(11,objeto.getNrContaExterna());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setFloat(13,objeto.getPrDepreciacao());
			pstmt.setInt(14,objeto.getLgOrcamentaria());
			pstmt.setString(15,objeto.getIdConta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaPlanoContas objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaPlanoContas objeto, int cdContaPlanoContasOld) {
		return update(objeto, cdContaPlanoContasOld, null);
	}

	public static int update(ContaPlanoContas objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaPlanoContas objeto, int cdContaPlanoContasOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_conta_plano_contas SET cd_conta_plano_contas=?,"+
												      		   "cd_conta=?,"+
												      		   "cd_plano_contas=?,"+
												      		   "cd_conta_superior=?,"+
												      		   "nr_conta=?,"+
												      		   "nr_digito=?,"+
												      		   "tp_conta=?,"+
												      		   "tp_natureza=?,"+
												      		   "dt_inativacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "nr_conta_externa=?,"+
												      		   "dt_cadastro=?,"+
												      		   "pr_depreciacao=?,"+
												      		   "lg_orcamentaria=?,"+
												      		   "id_conta=? WHERE cd_conta_plano_contas=?");
			pstmt.setInt(1,objeto.getCdContaPlanoContas());
			if(objeto.getCdConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConta());
			if(objeto.getCdPlanoContas()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPlanoContas());
			if(objeto.getCdContaSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContaSuperior());
			pstmt.setString(5,objeto.getNrConta());
			pstmt.setInt(6,objeto.getNrDigito());
			pstmt.setInt(7,objeto.getTpConta());
			pstmt.setInt(8,objeto.getTpNatureza());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setString(10,objeto.getTxtObservacao());
			pstmt.setString(11,objeto.getNrContaExterna());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setFloat(13,objeto.getPrDepreciacao());
			pstmt.setInt(14,objeto.getLgOrcamentaria());
			pstmt.setString(15,objeto.getIdConta());
			pstmt.setInt(16, cdContaPlanoContasOld!=0 ? cdContaPlanoContasOld : objeto.getCdContaPlanoContas());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaPlanoContas) {
		return delete(cdContaPlanoContas, null);
	}

	public static int delete(int cdContaPlanoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_conta_plano_contas WHERE cd_conta_plano_contas=?");
			pstmt.setInt(1, cdContaPlanoContas);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaPlanoContas get(int cdContaPlanoContas) {
		return get(cdContaPlanoContas, null);
	}

	public static ContaPlanoContas get(int cdContaPlanoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_conta_plano_contas WHERE cd_conta_plano_contas=?");
			pstmt.setInt(1, cdContaPlanoContas);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaPlanoContas(rs.getInt("cd_conta_plano_contas"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_plano_contas"),
						rs.getInt("cd_conta_superior"),
						rs.getString("nr_conta"),
						rs.getInt("nr_digito"),
						rs.getInt("tp_conta"),
						rs.getInt("tp_natureza"),
						(rs.getTimestamp("dt_inativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inativacao").getTime()),
						rs.getString("txt_observacao"),
						rs.getString("nr_conta_externa"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getFloat("pr_depreciacao"),
						rs.getInt("lg_orcamentaria"),
						rs.getString("id_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_conta_plano_contas");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_conta_plano_contas", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
