package com.tivic.manager.adm;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoServices;

public class ContaFechamentoArquivoServices {

	public static Result save(ContaFechamentoArquivo contaFechamentoArquivoo, Arquivo arquivo){
		return save(contaFechamentoArquivoo, arquivo, null);
	}
	
	/**
	 * @author Alvaro/DDK
	 * @param contaPagarArquivo
	 * @param connect
	 * @since 30/03/2015
	 * @return
	 */
	public static Result save( ContaFechamentoArquivo contaFechamentoArquivo, Arquivo arquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if( contaFechamentoArquivo == null ||  arquivo==null)
				return new Result(-1, "Erro ao salvar. ContaFechamentoArquivo é nulo");

			int retorno = 1;
			
			Result resultArquivo = ArquivoServices.save(arquivo, null, connect);
			if( resultArquivo.getCode() <= 0 )
				return new Result(-1, "Erro ao salvar Arquivo");
			
			arquivo = (Arquivo) resultArquivo.getObjects().get("ARQUIVO");
			ContaFechamentoArquivo fechamentoArquivoTmp = ContaFechamentoArquivoDAO.get(
																				contaFechamentoArquivo.getCdConta(),
																				contaFechamentoArquivo.getCdFechamento(),
																				contaFechamentoArquivo.getCdArquivo(),
																				connect);
			
			if( fechamentoArquivoTmp==null){
				contaFechamentoArquivo.setCdArquivo( arquivo.getCdArquivo() );
				retorno = ContaFechamentoArquivoDAO.insert(contaFechamentoArquivo, connect);
			}else{
				retorno = ContaFechamentoArquivoDAO.update(contaFechamentoArquivo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTAFECHAMENTOARQUIVO", contaFechamentoArquivo);
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
	
	public static Result remove(int cdConta, int cdFechamento, int cdArquivo){
		return remove(cdConta, cdFechamento, cdArquivo, false, null);
	}
	public static Result remove(int cdConta, int cdFechamento, int cdArquivo, boolean cascade){
		return remove(cdConta, cdFechamento, cdArquivo, cascade, null);
	}
	public static Result remove(int cdConta, int cdFechamento, int cdArquivo, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = ContaFechamentoArquivoDAO.delete(cdConta, cdFechamento, cdArquivo, connect);
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
	
	public static Result removeAll(int cdConta, int cdFechamento){
		return removeAll(cdConta, cdFechamento, null);
	} 
	
	public static Result removeAll(int cdConta, int cdFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta", Integer.toString(cdConta), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_fechamento", Integer.toString(cdFechamento), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmArquivos =  find(criterios);
			if( rsmArquivos != null ){
				while( rsmArquivos.next() ){
					Result res = remove(cdConta, cdFechamento, rsmArquivos.getInt("CD_ARQUIVO"), true, connect);
					if( res.getCode() <=0 ){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-2, "Erro ao excluir arquivo do Fechamento!");
					}
					res = ArquivoServices.remove( rsmArquivos.getInt("CD_ARQUIVO"), true, connect);
					if( res.getCode() <=0 ){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-2, "Erro ao excluir arquivo!");
					}
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento_arquivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByFechamento(int cdConta, int cdFechamento) {
		return getAllByFechamento(cdConta, cdFechamento, null);
	}
	
	public static ResultSetMap getAllByFechamento(int cdConta, int cdFechamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento_arquivo A "+
												"  JOIN GRL_ARQUIVO B ON ( A.cd_arquivo = B.cd_arquivo )"+
												"  WHERE A.cd_conta =  "+cdConta+
												"  AND   A.cd_fechamento =  "+cdFechamento
											);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoServices.getAllByFechamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoServices.getAllByFechamento: " + e);
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
		return Search.find("SELECT * FROM adm_conta_fechamento_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
