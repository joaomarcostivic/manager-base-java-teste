package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PlanoContasDAO{

	public static int insert(PlanoContas objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoContas objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_plano_contas", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlanoContas(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_plano_contas (cd_plano_contas,"+
			                                  "cd_conta_lucro,"+
			                                  "cd_conta_prejuizo,"+
			                                  "cd_moeda,"+
			                                  "nm_plano_contas,"+
			                                  "ds_mascara_conta,"+
			                                  "dt_inativacao,"+
			                                  "id_plano_contas,"+
			                                  "cd_conta_ativo,"+
			                                  "cd_conta_passivo,"+
			                                  "cd_conta_receita,"+
			                                  "cd_conta_despesa,"+
			                                  "cd_conta_custo,"+
			                                  "cd_conta_disponivel,"+
			                                  "cd_conta_patrimonio_liquido,"+
			                                  "cd_conta_resultado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContaLucro()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaLucro());
			if(objeto.getCdContaPrejuizo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaPrejuizo());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMoeda());
			pstmt.setString(5,objeto.getNmPlanoContas());
			pstmt.setString(6,objeto.getDsMascaraConta());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setString(8,objeto.getIdPlanoContas());
			if(objeto.getCdContaAtivo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdContaAtivo());
			if(objeto.getCdContaPassivo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdContaPassivo());
			if(objeto.getCdContaReceita()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdContaReceita());
			if(objeto.getCdContaDespesa()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdContaDespesa());
			if(objeto.getCdContaCusto()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdContaCusto());
			if(objeto.getCdContaDisponivel()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdContaDisponivel());
			if(objeto.getCdContaPatrimonioLiquido()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdContaPatrimonioLiquido());
			if(objeto.getCdContaResultado()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdContaResultado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoContas objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlanoContas objeto, int cdPlanoContasOld) {
		return update(objeto, cdPlanoContasOld, null);
	}

	public static int update(PlanoContas objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlanoContas objeto, int cdPlanoContasOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_plano_contas SET cd_plano_contas=?,"+
												      		   "cd_conta_lucro=?,"+
												      		   "cd_conta_prejuizo=?,"+
												      		   "cd_moeda=?,"+
												      		   "nm_plano_contas=?,"+
												      		   "ds_mascara_conta=?,"+
												      		   "dt_inativacao=?,"+
												      		   "id_plano_contas=?,"+
												      		   "cd_conta_ativo=?,"+
												      		   "cd_conta_passivo=?,"+
												      		   "cd_conta_receita=?,"+
												      		   "cd_conta_despesa=?,"+
												      		   "cd_conta_custo=?,"+
												      		   "cd_conta_disponivel=?,"+
												      		   "cd_conta_patrimonio_liquido=?,"+
												      		   "cd_conta_resultado=? WHERE cd_plano_contas=?");
			pstmt.setInt(1,objeto.getCdPlanoContas());
			if(objeto.getCdContaLucro()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaLucro());
			if(objeto.getCdContaPrejuizo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaPrejuizo());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMoeda());
			pstmt.setString(5,objeto.getNmPlanoContas());
			pstmt.setString(6,objeto.getDsMascaraConta());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setString(8,objeto.getIdPlanoContas());
			if(objeto.getCdContaAtivo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdContaAtivo());
			if(objeto.getCdContaPassivo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdContaPassivo());
			if(objeto.getCdContaReceita()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdContaReceita());
			if(objeto.getCdContaDespesa()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdContaDespesa());
			if(objeto.getCdContaCusto()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdContaCusto());
			if(objeto.getCdContaDisponivel()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdContaDisponivel());
			if(objeto.getCdContaPatrimonioLiquido()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdContaPatrimonioLiquido());
			if(objeto.getCdContaResultado()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdContaResultado());
			pstmt.setInt(17, cdPlanoContasOld!=0 ? cdPlanoContasOld : objeto.getCdPlanoContas());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoContas) {
		return delete(cdPlanoContas, null);
	}

	public static int delete(int cdPlanoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_plano_contas WHERE cd_plano_contas=?");
			pstmt.setInt(1, cdPlanoContas);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoContas get(int cdPlanoContas) {
		return get(cdPlanoContas, null);
	}

	public static PlanoContas get(int cdPlanoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_plano_contas WHERE cd_plano_contas=?");
			pstmt.setInt(1, cdPlanoContas);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoContas(rs.getInt("cd_plano_contas"),
						rs.getInt("cd_conta_lucro"),
						rs.getInt("cd_conta_prejuizo"),
						rs.getInt("cd_moeda"),
						rs.getString("nm_plano_contas"),
						rs.getString("ds_mascara_conta"),
						(rs.getTimestamp("dt_inativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inativacao").getTime()),
						rs.getString("id_plano_contas"),
						rs.getInt("cd_conta_ativo"),
						rs.getInt("cd_conta_passivo"),
						rs.getInt("cd_conta_receita"),
						rs.getInt("cd_conta_despesa"),
						rs.getInt("cd_conta_custo"),
						rs.getInt("cd_conta_disponivel"),
						rs.getInt("cd_conta_patrimonio_liquido"),
						rs.getInt("cd_conta_resultado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_plano_contas");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoContasDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_plano_contas", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
