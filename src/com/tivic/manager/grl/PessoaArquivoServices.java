package com.tivic.manager.grl;

import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import com.tivic.manager.util.ImagemServices;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;


public class PessoaArquivoServices {
	public static Result save(PessoaArquivo pessoaArquivo, Arquivo arquivo){
		return save(pessoaArquivo, arquivo, true, null);
	}
	
	public static Result save(PessoaArquivo pessoaArquivo, Arquivo arquivo, boolean updateBytes){
		return save(pessoaArquivo, arquivo, updateBytes, null);
	}
	
	public static Result save(PessoaArquivo pessoaArquivo, Arquivo arquivo, boolean updateBytes, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(arquivo==null)
				return new Result(-1, "Erro ao salvar. Arquivo é nulo");
			
			if(arquivo.getCdArquivo()==0){
				arquivo.setDtArquivamento(new GregorianCalendar());
				arquivo = insert(pessoaArquivo, arquivo, connect);
			}
			else {
				arquivo = update(pessoaArquivo, arquivo, !updateBytes, connect);
			}
			
			if(arquivo==null || arquivo.getCdArquivo()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result((arquivo==null || arquivo.getCdArquivo()<=0)?-1:arquivo.getCdArquivo(), 
							  (arquivo==null || arquivo.getCdArquivo()<=0)?"Erro ao salvar...":"Salvo com sucesso...", 
							  "ARQUIVO", arquivo);
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
	
	public static Arquivo insert(PessoaArquivo pessoaArquivo, Arquivo arquivo) {
		return insert(pessoaArquivo, arquivo, null);
	}

	public static Arquivo insert(PessoaArquivo pessoaArquivo, Arquivo arquivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			arquivo.setDtArquivamento(new GregorianCalendar());
			int cdArquivo = ArquivoDAO.insert(arquivo, connection);
			if (cdArquivo <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			arquivo.setCdArquivo(cdArquivo);
			pessoaArquivo.setCdArquivo(cdArquivo);
			if (PessoaArquivoDAO.insert(pessoaArquivo, connection)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (isConnectionNull)
				connection.commit();

			return arquivo;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Arquivo update(PessoaArquivo pessoaArquivo, Arquivo arquivo, boolean notUpdateFile) {
		return update(pessoaArquivo, arquivo, notUpdateFile, null);
	}

	public static Arquivo update(PessoaArquivo pessoaArquivo, Arquivo arquivo) {
		return update(pessoaArquivo, arquivo, false, null);
	}

	public static Arquivo update(PessoaArquivo pessoaArquivo, Arquivo arquivo, boolean notUpdateFile, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			if (notUpdateFile) {
				Arquivo arquivoTemp = ArquivoDAO.get(arquivo.getCdArquivo(), connection);
				if (arquivoTemp != null)
					arquivo.setBlbArquivo(arquivoTemp.getBlbArquivo());
			}
			int cdResultado = ArquivoDAO.update(arquivo, connection);
			if (cdResultado <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (PessoaArquivoDAO.update(pessoaArquivo, connection)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (isConnectionNull)
				connection.commit();

			return arquivo;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * @category JURIS
	 * 
	 * OBS: 'delete' é uma palavra reservada no AS3. 
	 * Para fins de chamada remota costumo usar remove ou deleteNomeDaEntidade
	 */
	public static Result remove(int cdArquivo, int cdPessoa) {
		int r = delete(cdArquivo, cdPessoa, null);
		
		return new Result(r, r >0 ? "Arquivo excluído com sucesso!" : "Erro ao excluir arquivo!");
	}
	
	public static int delete(int cdArquivo, int cdPessoa) {
		return delete(cdArquivo, cdPessoa, null);
	}

	public static int delete(int cdArquivo, int cdPessoa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if (PessoaArquivoDAO.delete(cdArquivo, cdPessoa, connection)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (ArquivoDAO.delete(cdArquivo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {		
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
		
			String sql = "SELECT A.*, B.*, C.nm_pessoa "+
				       "FROM grl_pessoa_arquivo A "+
					   "LEFT OUTER JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo) "+
					   "LEFT OUTER JOIN grl_pessoa  C ON (A.cd_pessoa = C.cd_pessoa) ";

		
			ResultSetMap rsm = Search.find(sql + " ", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm!=null && rsm.next() && rsm.getTimestamp("dt_final")!=null){
				rsm.setValueToField("ST_DT_VENCIMENTO", rsm.getTimestamp("dt_final").before(new Date()) ? 0 : 1);
			}
			
			
			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}		
	}
	
	public static Result save(FormDataBodyPart cdPessoa, FormDataBodyPart cdUsuario, FormDataBodyPart nmArquivo, FormDataBodyPart files) {
		return save(cdPessoa, cdUsuario, nmArquivo, files, null);
	}
	
	public static Result save(FormDataBodyPart cdPessoaBodyPart, FormDataBodyPart cdUsuarioBodyPart, FormDataBodyPart nmArquivoBodyPart, FormDataBodyPart filesBodyPart, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdPessoa = Integer.parseInt(cdPessoaBodyPart.getEntityAs(String.class));
			int cdUsuario = Integer.parseInt(cdUsuarioBodyPart.getEntityAs(String.class));
			String nmArquivo = nmArquivoBodyPart.getEntityAs(String.class);
			
			InputStream is = filesBodyPart.getEntityAs(InputStream.class);
			PessoaArquivo pessoaArquivo = new PessoaArquivo(0, cdPessoa, null, 0, 0); 
			Arquivo arquivo = new Arquivo(0, 
					nmArquivo, 
					new GregorianCalendar(), 
					nmArquivo, 
					ImagemServices.writeToByteArray(is).toByteArray(), 
					0, 
					null, 
					cdUsuario, 
					0, null, null, 0, null, 0, null, null);
			
			
			Result result = save(pessoaArquivo, arquivo, true, connect);
			if(result.getCode()<0) {
				if(isConnectionNull)
					connect.rollback();
				return result;
			}
			
			if(isConnectionNull)
				connect.commit();

			return new Result(1, "Salvo com sucesso...");
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
	
	
}
