package com.tivic.manager.cae;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class CardapioGrupoServices {
	
	public static final int TP_MENSAL  = 0;
	public static final int TP_SEMANAL = 1;
	
	public static final String[] tipoCardapioGrupo = {"Mensal","Semanal"};
	
	public static Result save(CardapioGrupo cardapioGrupo){
		return save(cardapioGrupo, false, false, null);
	}
	
	public static Result save(CardapioGrupo cardapioGrupo, boolean lgAlterarCardapios, boolean lgAlterarTipo){
		return save(cardapioGrupo, lgAlterarCardapios, lgAlterarTipo, null);
	}

	public static Result save(CardapioGrupo cardapioGrupo, boolean lgAlterarCardapios, boolean lgAlterarTipo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cardapioGrupo==null)
				return new Result(-1, "Erro ao salvar. CardapioGrupo é nulo");

			int retorno;
			if(cardapioGrupo.getCdCardapioGrupo()==0){
				retorno = CardapioGrupoDAO.insert(cardapioGrupo, connect);
				cardapioGrupo.setCdCardapioGrupo(retorno);
				
				// Cadastrar cardapios associados ao grupo
				if (lgAlterarCardapios) {
					if (cardapioGrupo.getTpCardapioGrupo()==TP_MENSAL){
						Cardapio cardapio = new Cardapio(0, cardapioGrupo.getNmCardapioGrupo(), cardapioGrupo.getDtInicialGrupo(), cardapioGrupo.getDtFinalGrupo(), 0, 0, 
								cardapioGrupo.getCdModalidade(), cardapioGrupo.getCdCardapioGrupo(), 0+"");
						CardapioServices.save(cardapio, connect);
					} else {
						int i = 0;
						while (i < CardapioServices.idCardapioSemanal.length) {
							Cardapio cardapio = new Cardapio(0, cardapioGrupo.getNmCardapioGrupo(), cardapioGrupo.getDtInicialGrupo(), cardapioGrupo.getDtFinalGrupo(), 0, 0, 
									cardapioGrupo.getCdModalidade(), cardapioGrupo.getCdCardapioGrupo(), i+"");
							CardapioServices.save(cardapio, connect);
							i++;
						}
					}
				}
			}
			else {
				retorno = CardapioGrupoDAO.update(cardapioGrupo, connect);
				
				// Atualizar os cardapios associados ao grupo
				if (lgAlterarCardapios){
					ResultSetMap rsmCardapios = getCardapios(cardapioGrupo.getCdCardapioGrupo(), connect);

					while (rsmCardapios.next()) {
						
						Cardapio cardapio = new Cardapio(rsmCardapios.getInt("CD_CARDAPIO"), cardapioGrupo.getNmCardapioGrupo(), cardapioGrupo.getDtInicialGrupo(), 
								cardapioGrupo.getDtFinalGrupo(), 0, 0, cardapioGrupo.getCdModalidade(), cardapioGrupo.getCdCardapioGrupo(), rsmCardapios.getString("ID_CARDAPIO"));
						CardapioServices.save(cardapio, connect);
						
						if (lgAlterarTipo && rsmCardapios.getString("ID_CARDAPIO")!="0") {
							retorno = CardapioServices.remove(rsmCardapios.getInt("CD_CARDAPIO"), true, connect).getCode();
							if (retorno <= 0)
								break;
						}
					}
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CARDAPIOGRUPO", cardapioGrupo);
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

	public static Result remove(int cdCardapioGrupo){
		return remove(cdCardapioGrupo, false, null);
	}
	public static Result remove(int cdCardapioGrupo, boolean cascade){
		return remove(cdCardapioGrupo, cascade, null);
	}
	public static Result remove(int cdCardapioGrupo, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				connect.prepareStatement("DELETE FROM cae_refeicao WHERE cd_cardapio IN (SELECT cd_cardapio FROM cae_cardapio WHERE cd_cardapio_grupo = "+ cdCardapioGrupo +")").execute();
				connect.prepareStatement("DELETE FROM cae_cardapio_nutricionista WHERE cd_cardapio IN (SELECT cd_cardapio FROM cae_cardapio WHERE cd_cardapio_grupo = "+ cdCardapioGrupo +")").execute();
				connect.prepareStatement("DELETE FROM cae_cardapio WHERE cd_cardapio_grupo = "+cdCardapioGrupo).execute();
				retorno = 1;
			}
			
			if(!cascade || retorno>0)
			retorno = CardapioGrupoDAO.delete(cdCardapioGrupo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_cardapio_grupo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para retornar a quantidade de vezez que tem um ingrediente em um grupo de cardapio de acordo a modalidadee o mês de referencia
	 * @param cdModalidade
	 * @param mes
	 * @return
	 */
	public static ResultSetMap getIngredientesByModalidade(int cdModalidade, int mes) {
		return getIngredientesByModalidade(cdModalidade, mes, 0, null);
	}
	
	public static ResultSetMap getIngredientesByModalidade(int cdModalidade, int mes, int cdIngrediente) {
		return getIngredientesByModalidade(cdModalidade, mes, cdIngrediente, null);
	}

	public static ResultSetMap getIngredientesByModalidade(int cdModalidade, int mes, int cdIngrediente, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			GregorianCalendar dtInicial = new GregorianCalendar(new GregorianCalendar().get(Calendar.YEAR), mes, 1);
			GregorianCalendar dtFinal = Util.getUltimoDiaMes(mes, new GregorianCalendar().get(Calendar.YEAR));
			
			pstmt = connect.prepareStatement("SELECT E.cd_ingrediente, COUNT(E.cd_ingrediente) AS qtdCardapio" +
											" FROM cae_cardapio A" +
											" LEFT OUTER JOIN cae_refeicao B ON (A.cd_cardapio = B.cd_cardapio)" +
											" LEFT OUTER JOIN cae_preparacao C ON (B.cd_preparacao = C.cd_preparacao)" +
											" LEFT OUTER JOIN cae_preparacao_ingrediente D ON (B.cd_preparacao = D.cd_preparacao)" +
											" LEFT OUTER JOIN cae_ingrediente E ON (D.cd_ingrediente = E.cd_ingrediente)" +
											" WHERE A.cd_cardapio IN" +
												" (SELECT cd_cardapio FROM cae_cardapio_grupo F join cae_cardapio G ON (F.cd_cardapio_grupo = G.cd_cardapio_grupo)" +
												" WHERE F.dt_inicial_grupo <= ? " + 
												" AND F.dt_final_grupo >= ? " + 
												" AND A.cd_modalidade = ?)" +
												(cdIngrediente > 0 ? " AND E.cd_ingrediente = ? " : "") +
											" GROUP BY E.cd_ingrediente ORDER BY E.cd_ingrediente ASC");
			
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdModalidade);			
			if(cdIngrediente > 0)
				pstmt.setInt(4, cdIngrediente);
			
			ResultSetMap ingredientes = new ResultSetMap(pstmt.executeQuery());

			while(ingredientes.next()){
				ingredientes.setValueToField("VL_PER_CAPTA", IngredienteServices.getPerCapta(ingredientes.getInt("CD_INGREDIENTE"), cdModalidade, connect));
			}
			
			return ingredientes;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para retornar todos os cardapios do grupo
	 * @param cdCardapioGrupo
	 * @return cardapios
	 */
	public static ResultSetMap getCardapios(int cdCardapioGrupo) {
		return getCardapios(cdCardapioGrupo, null);
	}

	public static ResultSetMap getCardapios(int cdCardapioGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT B.cd_cardapio, B.id_cardapio FROM cae_cardapio_grupo A" +
											" LEFT OUTER JOIN cae_cardapio B ON (A.cd_cardapio_grupo = B.cd_cardapio_grupo)" +
											" WHERE A.cd_cardapio_grupo = " + cdCardapioGrupo);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para duplicar o cardapioGrupo e seus derivados
	 * @param cdCardapioGrupo
	 * @return novo cardapioGrupo
	 */
	public static Result duplicar(int cdCardapioGrupo) {
		return duplicar(cdCardapioGrupo, null);
	}
	
	public static Result duplicar(int cdCardapioGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			CardapioGrupo cardapioGrupo = CardapioGrupoDAO.get(cdCardapioGrupo, connect);
			ResultSetMap rsmCardapios = getCardapios(cdCardapioGrupo, connect);
			
			// Cria novo cardapioGrupo
			cardapioGrupo.setCdCardapioGrupo(0);
			cardapioGrupo.setNmCardapioGrupo(cardapioGrupo.getNmCardapioGrupo() + " - CÓPIA");
			
			int retorno = CardapioGrupoDAO.insert(cardapioGrupo, connect);
			cardapioGrupo.setCdCardapioGrupo(retorno);
			
			// Cria os novos cardapios
			while (rsmCardapios.next() && retorno>=0) {
					Cardapio cardapio = CardapioDAO.get(rsmCardapios.getInt("CD_CARDAPIO"), connect);
					cardapio.setCdCardapio(0);
					cardapio.setNmCardapio(cardapioGrupo.getNmCardapioGrupo());
					cardapio.setCdCardapioGrupo(cardapioGrupo.getCdCardapioGrupo());
					
					retorno = CardapioDAO.insert(cardapio, connect);
					cardapio.setCdCardapio(retorno);
					
					// Cria as refeicoes para os novos cardapios
					ResultSetMap rsmRefeicoes = RefeicaoServices.getByCardapio(rsmCardapios.getInt("CD_CARDAPIO"), connect);
					while(rsmRefeicoes.next() && retorno>=0) {
						Refeicao refeicao = RefeicaoDAO.get(rsmRefeicoes.getInt("CD_CARDAPIO"), rsmRefeicoes.getInt("CD_PREPARACAO"), rsmRefeicoes.getInt("CD_REFEICAO"), connect);
						refeicao.setCdCardapio(cardapio.getCdCardapio());
						refeicao.setCdRefeicao(0);
						
						retorno = RefeicaoDAO.insert(refeicao, connect);
					}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(cardapioGrupo.getCdCardapioGrupo(), (retorno<=0)?"Erro ao duplicar...":"Duplicado com sucesso...", "CARDAPIOGRUPO", cardapioGrupo);
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
	 * Método para buscar todas as refeicoes do cardápio, com suas preparacoes e ingredientes
	 * Usado no diário da merendeira
	 * @param cdCardapioGrupo
	 * @return rsm com as refeicoes
	 */
	public static ResultSetMap getRefeicoesCompleto(int cdCardapioGrupo, GregorianCalendar data) {
		return getRefeicoesCompleto(cdCardapioGrupo, data, null);
	}
	
	public static ResultSetMap getRefeicoesCompleto(int cdCardapioGrupo, GregorianCalendar data, Connection connect){
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if (data==null)
				data = new GregorianCalendar();
			
			int semana = data.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			if (semana==5) // o cardapio eh montado para 4 semanas, se tiver na quinta, sera considerada a primeira do mes seguinte 
				semana = 1;
			
			pstmt = connect.prepareStatement( "SELECT * FROM cae_cardapio A"
					+ " JOIN cae_refeicao B ON (A.cd_cardapio = B.cd_cardapio)"
					+ " WHERE A.cd_cardapio_grupo = ? AND A.id_cardapio = ? AND A.nr_dia = ?"
					+ " ORDER BY B.tp_horario" );
			
			pstmt.setInt(1, cdCardapioGrupo);
			pstmt.setString(2, Integer.toString(semana-1));
			pstmt.setInt(3, data.get(Calendar.DAY_OF_MONTH));

			ResultSetMap rsmRefeicoes = new ResultSetMap(pstmt.executeQuery());
			while (rsmRefeicoes.next()) {
				
				ResultSetMap rsmIngredientes = PreparacaoServices.getIngredientes(rsmRefeicoes.getInt("CD_PREPARACAO"), connect);
				rsmRefeicoes.setValueToField("RSM_INGREDIENTES", rsmIngredientes);
			}
			
			return rsmRefeicoes;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoServices.getRefeicoesCompleto: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoServices.getRefeicoesCompleto: " + e);
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
		return Search.find("SELECT * FROM cae_cardapio_grupo ORDER BY nm_cardapio_grupo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
