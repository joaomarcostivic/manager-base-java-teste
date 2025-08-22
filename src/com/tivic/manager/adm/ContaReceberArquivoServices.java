package com.tivic.manager.adm;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoServices;

public class ContaReceberArquivoServices {
	
	public static Result save(ContaReceberArquivo contaReceberArquivo, Arquivo arquivo){
		return save(contaReceberArquivo, arquivo, null);
	}
	
	public static Result save(ContaReceberArquivo contaReceberArquivo, Arquivo arquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if( contaReceberArquivo == null ||  arquivo==null)
				return new Result(-1, "Erro ao salvar. ContaReceberArquivo é nulo");

			int retorno = 1;
			
			Result resultArquivo = ArquivoServices.save(arquivo);
			if( resultArquivo.getCode() <= 0 )
				return new Result(-1, "Erro ao salvar Arquivo");
			
			arquivo = (Arquivo) resultArquivo.getObjects().get("ARQUIVO");
			ContaReceberArquivo contaReceberArquivoTmp = ContaReceberArquivoDAO.get( contaReceberArquivo.getCdContaReceber(), arquivo.getCdArquivo(), connect);
			
			if( contaReceberArquivoTmp==null){
				contaReceberArquivo.setCdArquivo( arquivo.getCdArquivo() );
				retorno = ContaReceberArquivoDAO.insert(contaReceberArquivo, connect);
			}else{
				retorno = ContaReceberArquivoDAO.update(contaReceberArquivo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTAPAGARARQUIVO", contaReceberArquivo);
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
	
	public static Result save(ContaReceberArquivo contaReceberArquivo){
		return save(contaReceberArquivo, null, null);
	}

	public static Result save(ContaReceberArquivo contaReceberArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(contaReceberArquivo==null)
				return new Result(-1, "Erro ao salvar. ContaReceberArquivo é nulo");

			int retorno;
			ContaReceberArquivo contaReceberArquivoTmp = ContaReceberArquivoDAO.get(contaReceberArquivo.getCdContaReceber(), contaReceberArquivo.getCdArquivo(), connect);
			if( contaReceberArquivoTmp==null){
				retorno = ContaReceberArquivoDAO.insert(contaReceberArquivo, connect);
			}
			else {
				retorno = ContaReceberArquivoDAO.update(contaReceberArquivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTARECEBERARQUIVO", contaReceberArquivo);
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
	public static Result remove(int cdContaReceber, int cdArquivo){
		return remove(cdContaReceber, cdArquivo, false, null);
	}
	public static Result remove(int cdContaReceber, int cdArquivo, boolean cascade){
		return remove(cdContaReceber, cdArquivo, cascade, null);
	}
	public static Result remove(int cdContaReceber, int cdArquivo, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			int retornoArquivo = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0){
				retorno = ContaReceberArquivoDAO.delete(cdContaReceber, cdArquivo, connect);
				retornoArquivo = ArquivoDAO.delete(cdArquivo, connect);
			}
			if(retorno<=0 || retornoArquivo<=0 ){
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
	
	public static Result removeAll(int cdContaReceber){
		return removeAll(cdContaReceber, null);
	}
	
	public static Result removeAll(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_conta_receber", Integer.toString(cdContaReceber), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmArquivos =  find(criterios);
			while( rsmArquivos.next() ){
				Result res = remove(cdContaReceber, rsmArquivos.getInt("CD_ARQUIVO"), true, connect);
				if( res.getCode() <=0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao excluir arquivo da Conta a Receber!");
				}
				res = ArquivoServices.remove( rsmArquivos.getInt("CD_ARQUIVO"), true, connect);
				if( res.getCode() <=0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao excluir arquivo!");
				}
			}
			retorno = 1;
			
			if(retorno<=0){
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
			pstmt = connect.prepareStatement("SELECT A.*, B.cd_arquivo, B.nm_arquivo, B.dt_arquivamento, B.nm_documento, B.cd_tipo_arquivo, "+
										     "B.dt_criacao, B.cd_usuario, B.st_arquivo "+
										     "FROM adm_conta_receber_arquivo A "+
										     "JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo )");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoServices.getAll: " + e);
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
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return Search.find("SELECT A.*, B.cd_arquivo, B.nm_arquivo, B.dt_arquivamento, B.nm_documento, B.cd_tipo_arquivo, "+
						   "B.dt_criacao, B.cd_usuario, B.st_arquivo "+
						   "FROM adm_conta_receber_arquivo A "+
						   "JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo )", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByCliente(int cdEmpresa, int cdCliente) {
		return getAllByCliente(cdEmpresa, cdCliente, null);
	}

	public static ResultSetMap getAllByCliente(int cdEmpresa, int cdCliente, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT B.*, C.*, D.nm_nivel_cobranca, E.nm_cobranca FROM adm_conta_receber_arquivo A "
					+ "							JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo) "
					+ "							JOIN adm_conta_receber C ON (A.cd_conta_receber = C.cd_conta_receber) "
					+ "							JOIN adm_nivel_cobranca D ON (A.cd_nivel_cobranca = D.cd_nivel_cobranca) "
					+ "							JOIN adm_cobranca E ON (A.cd_cobranca = E.cd_cobranca) "
					+ "						WHERE C.cd_pessoa = " + cdCliente 
					+ "                       AND C.cd_empresa = " + cdEmpresa 
					+ "                     ORDER BY B.dt_criacao DESC");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
