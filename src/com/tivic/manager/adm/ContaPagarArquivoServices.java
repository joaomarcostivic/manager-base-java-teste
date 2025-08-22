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

public class ContaPagarArquivoServices {
	
	/**
	 * @author Alvaro/DDK
	 * @param contaPagarArquivo
	 * @since 30/03/2015
	 * @return
	 */
	public static Result save(ContaPagarArquivo contaPagarArquivo, Arquivo arquivo){
		return save(contaPagarArquivo, arquivo, null);
	}
	
	/**
	 * @author Alvaro/DDK
	 * @param contaPagarArquivo
	 * @param connect
	 * @since 30/03/2015
	 * @return
	 */
	public static Result save( ContaPagarArquivo contaPagarArquivo, Arquivo arquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if( contaPagarArquivo == null ||  arquivo==null)
				return new Result(-1, "Erro ao salvar. ContaPagarArquivo é nulo");

			int retorno = 1;
			
			Result resultArquivo = ArquivoServices.save(arquivo, null, connect);
			if( resultArquivo.getCode() <= 0 )
				return new Result(-1, "Erro ao salvar Arquivo");
			
			arquivo = (Arquivo) resultArquivo.getObjects().get("ARQUIVO");
			ContaPagarArquivo contaPagarArquivoTmp = ContaPagarArquivoDAO.get( contaPagarArquivo.getCdContaPagar(), arquivo.getCdArquivo(), connect);
			
			if( contaPagarArquivoTmp==null){
				contaPagarArquivo.setCdArquivo( arquivo.getCdArquivo() );
				retorno = ContaPagarArquivoDAO.insert(contaPagarArquivo, connect);
			}else{
				retorno = ContaPagarArquivoDAO.update(contaPagarArquivo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTAPAGARARQUIVO", contaPagarArquivo);
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
	
	/**
	 * @author Alvaro/DDK
	 * @param cdContaPagar
	 * @param cdArquivo
	 * @since 30/03/2015
	 * @return
	 */
	public static Result remove(int cdContaPagar, int cdArquivo){
		return remove(cdContaPagar, cdArquivo, false, null);
	}
	/**
	 * @author Alvaro/DDK
	 * @param cdContaPagar
	 * @param cdArquivo
	 * @param cascade
	 * @since 30/03/2015
	 * @return
	 */
	public static Result remove(int cdContaPagar, int cdArquivo, boolean cascade){
		return remove(cdContaPagar, cdArquivo, cascade, null);
	}
	/**
	 * @author Alvaro/DDk
	 * @param cdContaPagar
	 * @param cdArquivo
	 * @param cascade
	 * @param connect
	 * @since 30/03/2015
	 * @return
	 */
	public static Result remove(int cdContaPagar, int cdArquivo, boolean cascade, Connection connect){
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
				retorno = ContaPagarArquivoDAO.delete(cdContaPagar, cdArquivo, connect);
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
	
	public static Result removeAll(int cdContaPagar){
		return removeAll(cdContaPagar, null);
	}
	
	public static Result removeAll(int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_conta_pagar", Integer.toString(cdContaPagar), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmArquivos =  find(criterios);
			while( rsmArquivos.next() ){
				Result res = remove(cdContaPagar, rsmArquivos.getInt("CD_ARQUIVO"), true, connect);
				if( res.getCode() <=0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao excluir arquivo da Conta a Pagar!");
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
	
	/**
	 * @author Alvaro/DDK
	 * @since 30/03/2015
	 * @return
	 */
	public static ResultSetMap getAll() {
		return getAll(null);
	}
	
	/**
	 * @author Alvaro/DDK
	 * @param connect
	 * @since 30/03/2015
	 * @return
	 */
	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.cd_arquivo, B.nm_arquivo, B.dt_arquivamento, B.nm_documento, B.cd_tipo_arquivo, "+
											 "B.dt_criacao, B.cd_usuario, B.st_arquivo "+	
											 "FROM adm_conta_pagar_arquivo A "+
					   						 "JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo )");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @author Alvaro/DDK
	 * @param criterios
	 * @since 30/03/2015
	 * @return
	 */
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	/**
	 * @author Alvaro/DDK
	 * @param criterios
	 * @param connect
	 * @since 30/03/2015
	 * @return
	 */
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.cd_arquivo, B.nm_arquivo, B.dt_arquivamento, B.nm_documento, B.cd_tipo_arquivo, "+
						   "B.dt_criacao, B.cd_usuario, B.st_arquivo "+
						   "FROM adm_conta_pagar_arquivo A "+
						   "JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo )", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
