package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class CartaoServices {
		
	public static String[] stCartao = {"Inativo", "Ativo"};
	public static String[] tpCartao = {"Idoso", "Estudante", "PNE"};

	public static Result save(Cartao cartao){
		return save(cartao, null);
	}

	public static Result save(Cartao cartao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cartao==null)
				return new Result(-1, "Erro ao salvar. Cartao é nulo");

			int retorno;
			if(cartao.getCdCartao()==0){
				retorno = CartaoDAO.insert(cartao, connect);
				cartao.setCdCartao(retorno);
			}
			else {
				retorno = CartaoDAO.update(cartao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CARTAO", cartao);
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
	public static Result remove(int cdCartao){
		return remove(cdCartao, false, null);
	}
	public static Result remove(int cdCartao, boolean cascade){
		return remove(cdCartao, cascade, null);
	}
	public static Result remove(int cdCartao, boolean cascade, Connection connect){
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
			retorno = CartaoDAO.delete(cdCartao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getCartoesByPessoa(int cdPessoa) {
		System.out.println(cdPessoa);
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_PESSOA", String.valueOf(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = find(criterios);
		//System.out.println(rsm);
		return rsm;
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		//System.out.println(criterios);
		ResultSetMap rsm = Search.find("SELECT * FROM mob_cartao A LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		return rsm;
	}
	
	public static ResultSetMap findRelatorio(ArrayList<ItemComparator> criterios) {
		return findRelatorio(criterios, null);
	}

	public static ResultSetMap findRelatorio(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			String orderBy = "";			
			int qtRegistros = 0;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("LIMIT")) {
					qtRegistros = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
					criterios.remove(i);
					i--;
				}
			}
			
			String sql = "  SELECT A.*, E.nr_documento, F.nm_situacao_documento, G.nr_rg, G.nr_cpf" + 
						 "  B.nm_pessoa AS nm_solicitante, H.id_doenca AS nm_cid," + 
						 "	J.nm_pessoa AS nm_atendente, K.txt_tramitacao AS txt_ultima_ocorrencia" + 
						 "	FROM mob_cartao								A" + 
						 "	left JOIN grl_pessoa 						B ON ( A.cd_pessoa = B.cd_pessoa)" + 
						 "	right JOIN  mob_cartao_documento 			C ON ( C.cd_cartao = A.cd_cartao)" + 
						 "	right JOIN mob_cartao_documento_doenca 		D ON ( D.cd_cartao_documento = C.cd_cartao_documento )" + 
						 "	JOIN ptc_documento 							E ON ( E.cd_documento = C.cd_documento )" + 
						 "	JOIN ptc_situacao_documento 				F ON ( F.cd_situacao_documento = E.cd_situacao_documento)" + 
						 "	JOIN grl_pessoa_fisica 						G ON ( G.cd_pessoa = B.cd_pessoa )" + 
						 "	JOIN grl_doenca 							H ON ( H.cd_doenca = D.cd_doenca )" + 
						 "	JOIN seg_usuario 							I ON ( I.cd_usuario = E.cd_usuario )" + 
						 "	JOIN grl_pessoa 							J ON ( J.cd_pessoa = I.cd_pessoa )" + 
						 "	JOIN ptc_documento_tramitacao 				K ON ( K.cd_documento = C.cd_documento )" +
						 " WHERE 1=1";
						
			ResultSetMap rsm = Search.find(sql, ( orderBy != "" ? orderBy + " DESC " : " ORDER BY A.CD_PESSOA ASC ") + (qtRegistros > 0? " LIMIT " + qtRegistros: ""),
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			 
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.findRelatorio: " + e);
			return null;
		}
	}

}