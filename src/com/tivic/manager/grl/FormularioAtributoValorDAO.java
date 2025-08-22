package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class FormularioAtributoValorDAO{

	public static int insert(FormularioAtributoValor objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormularioAtributoValor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_formulario_atributo");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdFormularioAtributo()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_formulario_atributo_valor");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("grl_formulario_atributo_valor", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormularioAtributoValor(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_formulario_atributo_valor (cd_formulario_atributo,"+
			                                  "cd_formulario_atributo_valor,"+
			                                  "cd_produto_servico,"+
			                                  "cd_opcao,"+
			                                  "cd_empresa,"+
			                                  "cd_documento,"+
			                                  "txt_atributo_valor,"+
			                                  "cd_pessoa,"+
			                                  "cd_pessoa_valor,"+
			                                  "cd_cliente,"+
			                                  "cd_arquivo,"+
			                                  "cd_arquivo_documento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdFormularioAtributo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdFormularioAtributo());
			pstmt.setInt(2, code);
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdOpcao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOpcao());
			pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumento());
			pstmt.setString(7,objeto.getTxtAtributoValor());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPessoa());
			if(objeto.getCdPessoaValor()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdPessoaValor());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCliente());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdArquivo());
			if(objeto.getCdArquivoDocumento()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdArquivoDocumento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormularioAtributoValor objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(FormularioAtributoValor objeto, int cdFormularioAtributoOld, int cdFormularioAtributoValorOld) {
		return update(objeto, cdFormularioAtributoOld, cdFormularioAtributoValorOld, null);
	}

	public static int update(FormularioAtributoValor objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(FormularioAtributoValor objeto, int cdFormularioAtributoOld, int cdFormularioAtributoValorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_formulario_atributo_valor SET cd_formulario_atributo=?,"+
												      		   "cd_formulario_atributo_valor=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_opcao=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_documento=?,"+
												      		   "txt_atributo_valor=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_pessoa_valor=?,"+
												      		   "cd_cliente=?,"+
												      		   "cd_arquivo=?,"+
												      		   "cd_arquivo_documento=? WHERE cd_formulario_atributo=? AND cd_formulario_atributo_valor=?");
			pstmt.setInt(1,objeto.getCdFormularioAtributo());
			pstmt.setInt(2,objeto.getCdFormularioAtributoValor());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdOpcao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOpcao());
			pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumento());
			pstmt.setString(7,objeto.getTxtAtributoValor());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPessoa());
			if(objeto.getCdPessoaValor()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdPessoaValor());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCliente());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdArquivo());
			if(objeto.getCdArquivoDocumento()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdArquivoDocumento());
			pstmt.setInt(13, cdFormularioAtributoOld!=0 ? cdFormularioAtributoOld : objeto.getCdFormularioAtributo());
			pstmt.setInt(14, cdFormularioAtributoValorOld!=0 ? cdFormularioAtributoValorOld : objeto.getCdFormularioAtributoValor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormularioAtributo, int cdFormularioAtributoValor) {
		return delete(cdFormularioAtributo, cdFormularioAtributoValor, null);
	}

	public static int delete(int cdFormularioAtributo, int cdFormularioAtributoValor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_formulario_atributo_valor WHERE cd_formulario_atributo=? AND cd_formulario_atributo_valor=?");
			pstmt.setInt(1, cdFormularioAtributo);
			pstmt.setInt(2, cdFormularioAtributoValor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormularioAtributoValor get(int cdFormularioAtributo, int cdFormularioAtributoValor) {
		return get(cdFormularioAtributo, cdFormularioAtributoValor, null);
	}

	public static FormularioAtributoValor get(int cdFormularioAtributo, int cdFormularioAtributoValor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo_valor WHERE cd_formulario_atributo=? AND cd_formulario_atributo_valor=?");
			pstmt.setInt(1, cdFormularioAtributo);
			pstmt.setInt(2, cdFormularioAtributoValor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormularioAtributoValor(rs.getInt("cd_formulario_atributo"),
						rs.getInt("cd_formulario_atributo_valor"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_opcao"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_documento"),
						rs.getString("txt_atributo_valor"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_pessoa_valor"),
						rs.getInt("cd_cliente"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_arquivo_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo_valor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FormularioAtributoValor> getList() {
		return getList(null);
	}

	public static ArrayList<FormularioAtributoValor> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FormularioAtributoValor> list = new ArrayList<FormularioAtributoValor>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FormularioAtributoValor obj = FormularioAtributoValorDAO.get(rsm.getInt("cd_formulario_atributo"), rsm.getInt("cd_formulario_atributo_valor"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_formulario_atributo_valor", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}