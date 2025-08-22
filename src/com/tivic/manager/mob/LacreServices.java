package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.Util;
import com.tivic.manager.mob.AfericaoCatracaServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LacreServices {
	
	public static final String[] situacoesLacre = {"Inativo", "Ativo", "Disponível", "Descartado", "Defeituoso"};
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;
	public static final int ST_DISPONIVEL = 2;
	public static final int ST_DESCARTADO = 3;
	public static final int ST_DEFEITUOSO = 4;

	
	private static final int ERRO_LACRE_JA_CADASTRADO = -2;
	
	public static Result saveLote(ArrayList<Lacre> lacre){
		int retorno = 0;
		String msgErro = "";		
		Result rsSave = new Result(0);

		for (Lacre saveLacre : lacre) {
			rsSave = save(saveLacre);
			if(rsSave.getCode()<=0){
				if (rsSave.getCode() == ERRO_LACRE_JA_CADASTRADO) // Apenas informa
					msgErro += (msgErro==""? "Código já cadastrado:" + "\n["+saveLacre.getIdSerie() + "-" +saveLacre.getIdLacre()+"]":"\n["+saveLacre.getIdSerie() + saveLacre.getIdLacre()+"]");
				else{
					msgErro = rsSave.getMessage();
				}
				 
			}
		}
		return new Result(retorno, (retorno > 0 ? msgErro : (msgErro!=""? msgErro : "Lote de Lacres salvo com sucesso!!")));
	}
	
	public static Result save(Lacre lacre){
		return save(lacre, null);
	}

	public static Result save(Lacre lacre, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(lacre==null)
				return new Result(-1, "Erro ao salvar! Lacre é nulo");
			
			if(lacre.getIdLacre()==null)
				return new Result(-1, "Erro ao salvar! ID do Lacre está nulo");
			
			if(lacre.getIdSerie()==null)
				return new Result(-1, "Erro ao salvar! ID da Série do Lacre está nulo");
			
			int retorno;
			if(lacre.getCdLacre()==0){
				if( connect.prepareStatement("SELECT * FROM mob_lacre " +
	                    					 " WHERE id_lacre = '"+lacre.getIdLacre()+"'" +
	                    					 "   AND id_serie = '"+lacre.getIdSerie()+"'" ).executeQuery().next())
					return new Result(ERRO_LACRE_JA_CADASTRADO, "Este lacre já está cadastrado!");
							
				retorno = LacreDAO.insert(lacre, connect);
//				System.out.println("retorno: " + retorno);
				lacre.setCdLacre(retorno);
			}
			else {
				retorno = LacreDAO.update(lacre, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LACRE", lacre);
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
	
	public static int atualizarSituacao(int cdLacre, Connection connection) throws ValidationException {
		
		Lacre lacre = LacreDAO.get(cdLacre);
		if (lacre == null)
			throw new ValidationException("Erro ao salvar! Lacre é nulo.");
		
		if (lacre.getStLacre()==ST_DISPONIVEL)
			lacre.setStLacre(ST_ATIVO);
		else if (lacre.getStLacre()==ST_ATIVO)
			lacre.setStLacre(ST_INATIVO);

		int cod = LacreDAO.update(lacre, connection);
		if (cod <= 0) {
			Conexao.rollback(connection);
			throw new ValidationException("Erro ao atualizar situacao do lacre.");
		}
		
		return cod;
	}
	
	public static int atualizarSituacao(int cdLacre, int stLacre, Connection connection) throws ValidationException {
		
		Lacre lacre = LacreDAO.get(cdLacre);
		if (lacre == null)
			throw new ValidationException("Erro ao salvar! Lacre é nulo.");
		
		lacre.setStLacre(stLacre);

		int cod = LacreDAO.update(lacre, connection);
		if (cod <= 0) {
			Conexao.rollback(connection);
			throw new ValidationException("Erro ao atualizar situacao do lacre.");
		}
		
		return cod;
	}
	
	/**
	 * Metodo para verificar se o lacre esta ATIVO.
	 * @param cdLacre
	 * @param connection
	 * @return true/ false 
	 * @throws ValidationException
	 */
	public static boolean isAtivo(int cdLacre, Connection connection) throws ValidationException {
		
		Lacre lacre = LacreDAO.get(cdLacre);
		if (lacre == null)
			throw new ValidationException("Erro ao buscar! Lacre é nulo.");
		
		if (lacre.getStLacre()==ST_ATIVO)
			return true;
		
		return false;
	}
	
	/**
	 * Metodo para verificar se o lacre esta DISPONIVEL.
	 * @param cdLacre
	 * @param connection
	 * @return true/ false
	 * @throws ValidationException
	 */
	public static boolean isDisponivel(int cdLacre, Connection connection) throws ValidationException {
		
		Lacre lacre = LacreDAO.get(cdLacre);
		if (lacre == null)
			throw new ValidationException("Erro ao buscar! Lacre é nulo.");
		
		if (lacre.getStLacre()==ST_DISPONIVEL)
			return true;
		
		return false;
	}
	
	
	public static Result remove(int cdLacre){
		return remove(cdLacre, false, null);
	}
	public static Result remove(int cdLacre, boolean cascade){
		return remove(cdLacre, cascade, null);
	}
	public static Result remove(int cdLacre, boolean cascade, Connection connect){
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
			retorno = LacreDAO.delete(cdLacre, connect);
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
		return getAll(null,null);
	}

	public static ResultSetMap getAllDisponiveis() {
		ArrayList<ItemComparator> criterios = new ArrayList<>();
		criterios.add(new ItemComparator("ST_LACRE", String.valueOf(ST_DISPONIVEL), ItemComparator.EQUAL, Types.INTEGER));
		
		return getAll(criterios, null);
	}

	public static ResultSetMap getAll(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			return Search.find("SELECT * FROM mob_lacre ", criterios,
					connect!=null ? connect : Conexao.conectar(), connect==null);
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreServices.getAll: " + e);
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
		
		Criterios    crt = new Criterios();
		int qtLimite = 0;
		int qtDeslocamento = 0;
		
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
			else if (criterios.get(i).getColumn().equalsIgnoreCase("qtDeslocamento"))
				qtDeslocamento = Integer.parseInt(criterios.get(i).getValue());
			else
				crt.add(criterios.get(i));
		}
		
		String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, qtDeslocamento);

		ResultSetMap rsm = Search.find("SELECT  " + sqlLimit[0] + " A.* FROM mob_lacre A",
				   " " + sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		return rsm;
	}
	
	public static ResultSetMap findVeiculosLacrados(ArrayList<ItemComparator> criterios) {
		return findVeiculosLacrados(criterios, null);
	}

	public static ResultSetMap findVeiculosLacrados(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			return Search.find("SELECT A.*, B.*, "+
								" C.nm_pessoa AS nm_proprietario, E.nm_pessoa AS nm_concessionario, F.*, G.*, G.cd_lacre, "+
								" H.txt_observacao as txt_observacao_aplicacao, H.dt_afericao as dt_aplicacao, H.qt_aferido as qt_catraca_inicial, "+
								" I.txt_observacao as txt_observacao_remocao, I.dt_afericao as dt_remocao, I.qt_aferido as qt_catraca_final "+
								" FROM mob_concessao_veiculo A "+
								" JOIN fta_veiculo   	 B ON ( A.cd_veiculo = B.cd_veiculo ) "+
								" JOIN grl_pessoa    	 C ON ( B.cd_proprietario = C.cd_pessoa ) "+
								" JOIN mob_concessao 	 D ON ( A.cd_concessao = D.cd_concessao ) "+
								" JOIN grl_pessoa    	 E ON ( D.cd_concessionario = E.cd_pessoa ) "+
								" JOIN mob_lacre_catraca F ON ( F.cd_concessao_veiculo = A.cd_concessao_veiculo  ) "+
								" JOIN mob_lacre     	 G ON ( G.cd_lacre = F.cd_lacre ) "+
								" LEFT OUTER JOIN mob_afericao_catraca H ON ( F.cd_afericao_aplicacao = H.cd_afericao_catraca ) "+
								" LEFT OUTER JOIN mob_afericao_catraca I ON ( F.cd_afericao_remocao = I.cd_afericao_catraca ) "
								, criterios,connect!=null ? connect : Conexao.conectar(), connect==null);
		}catch(Exception e) {
			if( connect!=null )
				Conexao.desconectar(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreServices.findVeiculosLacrados: " + e);
			return null;
		}
	}
	
	public static int update(Lacre lacre){
		return LacreDAO.update(lacre);
	}
	
	public static HashMap<String, Object> getSyncData() {
		return getSyncData(null);
	}

	public static HashMap<String, Object> getSyncData(ArrayList<Lacre> lacre) {
		return getSyncData(lacre, null);
	}

	public static HashMap<String, Object> getSyncData(ArrayList<Lacre> lacre, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
						
			
			//COMENTADA POR N�O TER NECESSIDADE DE ATUALIZAR O LACRE NA SINCRONIZA��O
			//if(lacre != null && lacre.size() > 0) {
			//	for(Lacre l : lacre) {
			//		Result save = save(l, connect);
			//		
			//		if(save.getCode() <= 0) {
			//			throw new Exception("Não foi possível completar a sincronização");
			//		}
			//	}
			//}
			
			connect.commit();

			String sql = "SELECT * FROM mob_lacre ORDER BY cd_lacre";
			
			pstmt = connect.prepareStatement(sql);
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("Lacre", Util.resultSetToArrayList(pstmt.executeQuery()));
						
			return register;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Lacre getLastLacre(int nrPrefixo) {
		return getLastLacre(nrPrefixo, null);
	}
	
	public static Lacre getLastLacre(int nrPrefixo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;		
		try {

			String sql = "SELECT A.cd_afericao_remocao, C.* FROM mob_lacre_catraca A " + 
					"JOIN mob_concessao_veiculo B ON (A.cd_concessao_veiculo = B.cd_concessao_veiculo) " + 
					"JOIN mob_lacre C on (A.cd_lacre = C.cd_lacre) " +
					"WHERE B.nr_prefixo = ? " +  
					"ORDER BY A.cd_lacre_catraca DESC LIMIT 1";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, nrPrefixo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			Lacre lacre = new Lacre();
			if(rsm.next()) {
				if(rsm.getInt("cd_afericao_remocao") == 0) {
					lacre.setCdLacre(rsm.getInt("cd_lacre"));
					lacre.setIdLacre(rsm.getString("id_lacre"));
					lacre.setIdSerie(rsm.getString("id_serie"));
					lacre.setStLacre(rsm.getInt("st_lacre"));
					return lacre;
				}
			}
				return null;
			
		}catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}	
	}

}
