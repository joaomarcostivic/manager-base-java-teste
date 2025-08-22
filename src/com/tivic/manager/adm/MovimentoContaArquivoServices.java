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

public class MovimentoContaArquivoServices {

	public static Result save(MovimentoContaArquivo movimentoContaArquivo,  Arquivo arquivo){
		return save(movimentoContaArquivo, arquivo, null);
	}

	public static Result save(MovimentoContaArquivo movimentoContaArquivo, Arquivo arquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(movimentoContaArquivo==null ||  arquivo==null)
				return new Result(-1, "Erro ao salvar. MovimentoContaArquivo é nulo");

			int retorno;
			
			Result resultArquivo = ArquivoServices.save(arquivo, null, connect);
			if( resultArquivo.getCode() <= 0 )
				return new Result(-1, "Erro ao salvar Arquivo");
			
			arquivo = (Arquivo) resultArquivo.getObjects().get("ARQUIVO");
			MovimentoContaArquivo movContaArquivoTmp = MovimentoContaArquivoDAO.get( movimentoContaArquivo.getCdMovimentoConta(),
																					movimentoContaArquivo.getCdConta(),
																					arquivo.getCdArquivo(), connect);
			
			if( movContaArquivoTmp==null){
				movimentoContaArquivo.setCdArquivo( arquivo.getCdArquivo() );
				retorno = MovimentoContaArquivoDAO.insert(movimentoContaArquivo, connect);
			}else{
				retorno = MovimentoContaArquivoDAO.update(movimentoContaArquivo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MOVIMENTOCONTAARQUIVO", movimentoContaArquivo);
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
	public static Result remove(int cdMovimentoConta, int cdConta, int cdArquivo){
		return remove(cdMovimentoConta, cdConta, cdArquivo, false, null);
	}
	public static Result remove(int cdMovimentoConta, int cdConta, int cdArquivo, boolean cascade){
		return remove(cdMovimentoConta, cdConta, cdArquivo, cascade, null);
	}
	public static Result remove(int cdMovimentoConta, int cdConta, int cdArquivo, boolean cascade, Connection connect){
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
			retorno = MovimentoContaArquivoDAO.delete(cdMovimentoConta, cdConta, cdArquivo, connect);
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
	
	public static Result removeAll(int cdMovimentoConta, int cdConta){
		return removeAll(cdMovimentoConta, cdConta, null);
	}
	public static Result removeAll(int cdMovimentoConta, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			
			ResultSetMap rsmArquivos = getAllByMovimento(cdMovimentoConta, cdConta, connect);
			while( rsmArquivos.next() ){
				Result res = remove(cdMovimentoConta, cdConta, rsmArquivos.getInt("CD_ARQUIVO"), true, connect);
				if( res.getCode() <=0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao excluir arquivo!");
				}
				res = ArquivoServices.remove( rsmArquivos.getInt("CD_ARQUIVO"), true, connect);
				if( res.getCode() <=0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao excluir arquivo do movimento!");
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_arquivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByMovimento( int cdMovimentoConta, int cdConta) {
		return getAllByMovimento(cdMovimentoConta, cdConta, null);
	}
	
	public static ResultSetMap getAllByMovimento( int cdMovimentoConta, int cdConta,  Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_arquivo A "+
											 " LEFT JOIN grl_arquivo B on ( A.cd_arquivo = B.cd_arquivo ) "+
											 " WHERE A.cd_movimento_conta = "+cdMovimentoConta+
											 "       and A.cd_conta = "+cdConta);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.getAllByMovimento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.getAllByMovimento: " + e);
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
		return Search.find("SELECT * FROM adm_movimento_conta_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
