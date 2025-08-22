package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoDAO{

	public static int insert(Contrato objeto) {
		return insert(objeto, null);
	}

	public static int insert(Contrato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.adm.ContratoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContrato(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mcr_contrato (cd_contrato,"+
			                                  "dt_liberacao,"+
			                                  "tp_garantia,"+
			                                  "tp_emprestimo,"+
			                                  "cd_grupo_solidario,"+
			                                  "cd_membro_grupo) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getDtLiberacao()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLiberacao().getTimeInMillis()));
			pstmt.setInt(3,objeto.getTpGarantia());
			pstmt.setInt(4,objeto.getTpEmprestimo());
			if(objeto.getCdGrupoSolidario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdGrupoSolidario());
			if(objeto.getCdMembroGrupo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMembroGrupo());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Contrato objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Contrato objeto, int cdContratoOld) {
		return update(objeto, cdContratoOld, null);
	}

	public static int update(Contrato objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Contrato objeto, int cdContratoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Contrato objetoTemp = get(objeto.getCdContrato(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO mcr_contrato (cd_contrato,"+
			                                  "dt_liberacao,"+
			                                  "tp_garantia,"+
			                                  "tp_emprestimo,"+
			                                  "cd_grupo_solidario,"+
			                                  "cd_membro_grupo) VALUES (?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE mcr_contrato SET cd_contrato=?,"+
												      		   "dt_liberacao=?,"+
												      		   "tp_garantia=?,"+
												      		   "tp_emprestimo=?,"+
												      		   "cd_grupo_solidario=?,"+
												      		   "cd_membro_grupo=? WHERE cd_contrato=?");
			pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getDtLiberacao()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLiberacao().getTimeInMillis()));
			pstmt.setInt(3,objeto.getTpGarantia());
			pstmt.setInt(4,objeto.getTpEmprestimo());
			if(objeto.getCdGrupoSolidario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdGrupoSolidario());
			if(objeto.getCdMembroGrupo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMembroGrupo());
			if (objetoTemp != null) {
				pstmt.setInt(7, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.adm.ContratoDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato) {
		return delete(cdContrato, null);
	}

	public static int delete(int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mcr_contrato WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			pstmt.executeUpdate();
			if (com.tivic.manager.adm.ContratoDAO.delete(cdContrato, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Contrato get(int cdContrato) {
		return get(cdContrato, null);
	}

	public static Contrato get(int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_contrato A, adm_contrato B WHERE A.cd_contrato=B.cd_contrato AND A.cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Contrato(rs.getInt("cd_contrato"),
						rs.getInt("cd_convenio"),
						rs.getInt("cd_categoria_parcelas"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_modelo_contrato"),
						rs.getInt("cd_indicador"),
						(rs.getTimestamp("dt_assinatura")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_assinatura").getTime()),
						(rs.getTimestamp("dt_primeira_parcela")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeira_parcela").getTime()),
						rs.getInt("nr_dia_vencimento"),
						rs.getInt("nr_parcelas"),
						rs.getFloat("pr_juros_mora"),
						rs.getFloat("pr_multa_mora"),
						rs.getFloat("pr_desconto_adimplencia"),
						rs.getFloat("pr_desconto"),
						rs.getInt("tp_contrato"),
						rs.getFloat("vl_parcelas"),
						rs.getFloat("vl_adesao"),
						rs.getFloat("vl_contrato"),
						rs.getString("nr_contrato"),
						rs.getString("txt_contrato"),
						rs.getInt("st_contrato"),
						rs.getString("id_contrato"),
						(rs.getTimestamp("dt_inicio_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_vigencia").getTime()),
						(rs.getTimestamp("dt_final_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_vigencia").getTime()),
						rs.getInt("cd_agente"),
						rs.getInt("cd_conta_carteira"),
						rs.getInt("cd_conta"),
						rs.getInt("tp_amortizacao"),
						rs.getInt("gn_contrato"),
						rs.getFloat("pr_juros"),
						rs.getInt("cd_tipo_operacao"),
						rs.getInt("cd_documento"),
						rs.getInt("tp_desconto"),
						rs.getFloat("vl_desconto"),
						rs.getInt("cd_contrato_origem"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_categoria_adesao"),
						(rs.getTimestamp("dt_liberacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_liberacao").getTime()),
						rs.getInt("tp_garantia"),
						rs.getInt("tp_emprestimo"),
						rs.getInt("cd_grupo_solidario"),
						rs.getInt("cd_membro_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_contrato");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_contrato", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
