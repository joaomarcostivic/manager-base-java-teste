package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AgenciaServices {
	
	
	public static Result save(Agencia agencia, PessoaEndereco endereco){
		return save(agencia, endereco, null);
	}

	public static Result save(Agencia agencia, PessoaEndereco endereco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(agencia==null)
				return new Result(-1, "Erro ao salvar. Agencia é nulo");
			if(endereco==null)
				return new Result(-1, "Erro ao salvar. Endereço é nulo");

			int retorno;
			if(agencia.getCdAgencia()==0){
				retorno = AgenciaDAO.insert(agencia, connect);
				agencia.setCdAgencia(retorno);
				endereco.setCdPessoa( retorno );
				PessoaEnderecoServices.save(endereco, connect);
			}
			else {
				retorno = AgenciaDAO.update(agencia, connect);
				retorno = PessoaEnderecoDAO.update(endereco, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AGENCIA", agencia);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdAgencia){
		return remove(cdAgencia, false, null);
	}
	public static Result remove(int cdAgencia, boolean cascade){
		return remove(cdAgencia, cascade, null);
	}
	public static Result remove(int cdAgencia, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				connect.prepareStatement("DELETE FROM grl_pessoa_endereco WHERE cd_pessoa = "+cdAgencia).executeUpdate();
				retorno = 1;
			}
			if(!cascade || retorno>0){
				retorno = AgenciaDAO.delete(cdAgencia, connect);
			}
			if(retorno<=0 ){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_agencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		String nmBanco = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("B.NM_BANCO")) {
				nmBanco =	Util.limparTexto(criterios.get(i).getValue());
				nmBanco = nmBanco.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT (nr_agencia || \' - \' ||  B.nm_banco) AS cl_agencia, A.*, B.*, C.*, C.id_pessoa AS id_agencia, " +
						   "D.nr_cnpj, E.*, F.nm_cidade, F2.*  " +
				           "FROM grl_agencia A " +
				           "JOIN grl_banco  B ON (A.cd_banco = B.cd_banco) " +
				           "JOIN grl_pessoa C ON (A.cd_agencia = C.cd_pessoa) " +
				           "JOIN grl_pessoa_juridica D ON (A.cd_agencia = D.cd_pessoa) "+
				           "LEFT OUTER JOIN grl_pessoa_endereco E ON (A.cd_agencia = E.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_cidade F ON (F.cd_cidade = E.cd_cidade) "+
				           "LEFT OUTER JOIN grl_estado F2 ON (F.cd_estado = F2.cd_estado) "+
				           (!nmBanco.equals("") ?
									" AND TRANSLATE (b.nm_banco, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', "+
									"					'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmBanco)+"%' "
									: ""),
				           criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * @see #find(ArrayList)
	 */
	public static ResultSetMap find() {
		return find(new ArrayList<sol.dao.ItemComparator>(), null);
	}

	public static int insert(Agencia objeto, PessoaEndereco endereco)	{
		Connection connect = Conexao.conectar();
		try	{
			int cdAgencia = AgenciaDAO.insert(objeto, connect);
			if(cdAgencia > 0) {
				endereco.setCdPessoa(cdAgencia);
				PessoaEnderecoDAO.insert(endereco, connect);
			}
			return cdAgencia;
		}
		catch(Exception e){
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int update(Agencia objeto, PessoaEndereco endereco)	{
		Connection connect = Conexao.conectar();
		try	{
			int ret = AgenciaDAO.update(objeto, connect);
			if(ret > 0)	{
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE cd_pessoa = "+objeto.getCdAgencia()).executeQuery();
				if(rs.next())
					PessoaEnderecoDAO.update(endereco, connect);
				else
					PessoaEnderecoDAO.insert(endereco, connect);
			}
			return ret;
		}
		catch(Exception e){
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgencia) {
		return delete(cdAgencia, null);
	}

	public static int delete(int cdAgencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			// Excluindo endereços
			connect.prepareStatement("DELETE FROM grl_pessoa_endereco WHERE cd_pessoa = "+cdAgencia).executeUpdate();

			if (AgenciaDAO.delete(cdAgencia, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenciaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}