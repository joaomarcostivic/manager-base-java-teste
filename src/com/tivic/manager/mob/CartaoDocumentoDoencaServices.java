package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CartaoDocumentoDoencaServices {

	public static Result save(CartaoDocumentoDoenca cartaoDocumentoDoenca){
		return save(cartaoDocumentoDoenca, null);
	}

	public static Result save(CartaoDocumentoDoenca cartaoDocumentoDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cartaoDocumentoDoenca==null)
				return new Result(-1, "Erro ao salvar. CartaoDocumentoDoenca é nulo");

			int retorno;
			if(cartaoDocumentoDoenca.getCdCartaoDocumentoDoenca()==0){
				retorno = CartaoDocumentoDoencaDAO.insert(cartaoDocumentoDoenca, connect);
				cartaoDocumentoDoenca.setCdCartaoDocumentoDoenca(retorno);
			}
			else {
				retorno = CartaoDocumentoDoencaDAO.update(cartaoDocumentoDoenca, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CARTAODOCUMENTODOENCA", cartaoDocumentoDoenca);
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
	
	public static Result remove(int cdCartaoDocumentoDoenca){
		return remove(cdCartaoDocumentoDoenca, false, null);
	}
	
	public static Result remove(int cdCartaoDocumentoDoenca, boolean cascade){
		return remove(cdCartaoDocumentoDoenca, cascade, null);
	}
	
	public static Result remove(int cdCartaoDocumentoDoenca, boolean cascade, Connection connect){
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
			retorno = CartaoDocumentoDoencaDAO.delete(cdCartaoDocumentoDoenca, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao_documento_doenca");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllDoencasByPessoa(int cdPessoa, int cdDocumento){
		return getAllDoencasByPessoa(cdPessoa, cdDocumento, null);
	}
	
	public static ResultSetMap getAllDoencasByPessoa(int cdPessoa, int cdDocumento,  Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("E.CD_PESSOA", String.valueOf(cdPessoa), ItemComparator.EQUAL, Types.VARCHAR));
		criterios.add(new ItemComparator("A1.CD_DOCUMENTO", String.valueOf(cdDocumento), ItemComparator.EQUAL, Types.VARCHAR));
		
		String sql = "SELECT B.*, A.txt_descricao FROM mob_cartao_documento_doenca A "+
					 "LEFT OUTER JOIN mob_cartao_documento A1 ON (A.cd_cartao_documento = A1.cd_cartao_documento) " +
					 "LEFT OUTER JOIN grl_doenca B ON (A.cd_doenca = B.cd_doenca) "+
					 "LEFT OUTER JOIN mob_cartao C ON (A1.cd_cartao = C.cd_cartao) "+
					 "LEFT OUTER JOIN ptc_documento D ON (A1.cd_documento = D.cd_documento) "+
					 "LEFT OUTER JOIN ptc_documento_pessoa E ON (D.cd_documento = E.cd_documento) ";
		
		return Search.findAndLog(sql, "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_cartao_documento_doenca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
