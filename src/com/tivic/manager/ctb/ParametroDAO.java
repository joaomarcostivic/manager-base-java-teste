package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ParametroDAO{

	public static int insert(Parametro objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Parametro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_parametro");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_empresa");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEmpresa()));
			int code = Conexao.getSequenceCode("ctb_parametro", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdParametro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_parametro (cd_parametro,"+
			                                  "cd_empresa,"+
			                                  "cd_conta,"+
			                                  "cd_conta_financeira,"+
			                                  "cd_contrato,"+
			                                  "cd_convenio,"+
			                                  "cd_cliente,"+
			                                  "cd_fornecedor,"+
			                                  "cd_evento_financeiro,"+
			                                  "cd_setor,"+
			                                  "cd_lancamento_auto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdConta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConta());
			if(objeto.getCdContaFinanceira()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContaFinanceira());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdContrato());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdConvenio());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCliente());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdFornecedor());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdEventoFinanceiro());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdSetor());
			if(objeto.getCdLancamentoAuto()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdLancamentoAuto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Parametro objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Parametro objeto, int cdParametroOld, int cdEmpresaOld) {
		return update(objeto, cdParametroOld, cdEmpresaOld, null);
	}

	public static int update(Parametro objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Parametro objeto, int cdParametroOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_parametro SET cd_parametro=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_conta=?,"+
												      		   "cd_conta_financeira=?,"+
												      		   "cd_contrato=?,"+
												      		   "cd_convenio=?,"+
												      		   "cd_cliente=?,"+
												      		   "cd_fornecedor=?,"+
												      		   "cd_evento_financeiro=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_lancamento_auto=? WHERE cd_parametro=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdParametro());
			pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdConta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConta());
			if(objeto.getCdContaFinanceira()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContaFinanceira());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdContrato());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdConvenio());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCliente());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdFornecedor());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdEventoFinanceiro());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdSetor());
			if(objeto.getCdLancamentoAuto()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdLancamentoAuto());
			pstmt.setInt(12, cdParametroOld!=0 ? cdParametroOld : objeto.getCdParametro());
			pstmt.setInt(13, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdParametro, int cdEmpresa) {
		return delete(cdParametro, cdEmpresa, null);
	}

	public static int delete(int cdParametro, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_parametro WHERE cd_parametro=? AND cd_empresa=?");
			pstmt.setInt(1, cdParametro);
			pstmt.setInt(2, cdEmpresa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Parametro get(int cdParametro, int cdEmpresa) {
		return get(cdParametro, cdEmpresa, null);
	}

	public static Parametro get(int cdParametro, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_parametro WHERE cd_parametro=? AND cd_empresa=?");
			pstmt.setInt(1, cdParametro);
			pstmt.setInt(2, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Parametro(rs.getInt("cd_parametro"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_conta_financeira"),
						rs.getInt("cd_contrato"),
						rs.getInt("cd_convenio"),
						rs.getInt("cd_cliente"),
						rs.getInt("cd_fornecedor"),
						rs.getInt("cd_evento_financeiro"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_lancamento_auto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_parametro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_parametro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
