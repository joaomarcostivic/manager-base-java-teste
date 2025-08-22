package com.tivic.manager.cae;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CardapioServices {
	
	public static final String[] tiposModalidade = {"Pré-escola","Creche","Fundamental","Educação Especializada","Mais Educação","Mais Educação Quilombola","Quilombola","EJA"};
	public static final String[] idCardapioSemanal = {"1ª SEMANA","2ª SEMANA","3ª SEMANA","4ª SEMANA"};
	
	public static Result save(Cardapio cardapio){
		return save(cardapio, null);
	}

	public static Result save(Cardapio cardapio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cardapio==null)
				return new Result(-1, "Erro ao salvar. Cardapio é nulo");

			int retorno;
			if(cardapio.getCdCardapio()==0){
				retorno = CardapioDAO.insert(cardapio, connect);
				cardapio.setCdCardapio(retorno);
			}
			else {
				retorno = CardapioDAO.update(cardapio, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CARDAPIO", cardapio);
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

	public static Result remove(int cdCardapio){
		return remove(cdCardapio, false, null);
	}
	public static Result remove(int cdCardapio, boolean cascade){
		return remove(cdCardapio, cascade, null);
	}
	public static Result remove(int cdCardapio, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				connect.prepareStatement("DELETE FROM cae_refeicao WHERE cd_cardaprio = "+cdCardapio).execute();
				connect.prepareStatement("DELETE FROM cae_cardapio_nutricionista WHERE cd_cardapio = "+ cdCardapio).execute();
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = CardapioDAO.delete(cdCardapio, connect);
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
	 * Método para pegar todos os cardapios ativos
	 * @return cardapios ativos
	 */
	public static ResultSetMap getAllAtivos() {
		return getAllAtivos(null);
	}

	public static ResultSetMap getAllAtivos(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			GregorianCalendar hoje = new GregorianCalendar();
			
			pstmt = connect.prepareStatement("SELECT * FROM cae_cardapio WHERE dt_inicial_validade <= '" + Util.convCalendarStringSql(hoje) + 
																		"' AND dt_final_validade >= '" + Util.convCalendarStringSql(hoje)+"'");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioServices.getAllAtivos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioServices.getAllAtivos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para pegar todos as refeições de um cardapio
	 * @param codigo do cardapio
	 * @return refeições do cardapio
	 */
	public static ResultSetMap getAllRefeicoes(int cdCardapio) {
		return getAllRefeicoes(cdCardapio, null);
	}

	public static ResultSetMap getAllRefeicoes(int cdCardapio, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT B.tp_horario, B.nr_dia, C.nm_preparacao" + 
											" FROM cae_cardapio A" +
											" LEFT OUTER JOIN cae_refeicao B ON (A.cd_cardapio = B.cd_cardapio)" +
											" LEFT OUTER JOIN cae_preparacao C ON (B.cd_preparacao = C.cd_preparacao)" +
											" WHERE A.cd_cardapio = " + cdCardapio +
											" ORDER BY B.tp_horario, B.nr_dia");
			
			ResultSetMap rsmPreparacos = new ResultSetMap(pstmt.executeQuery());
			HashMap<Integer, HashMap<String, Object>> hashPrincipal = new HashMap<Integer, HashMap<String,Object>>();
			
			String nomePreparacaoSeg = "";
			String nomePreparacaoTer = "";
			String nomePreparacaoQua = "";
			String nomePreparacaoQui = "";
			String nomePreparacaoSex = "";
			
			while(rsmPreparacos.next()){
				
				//Faz a inserção inicial do tipo de horario no hashPrincipal
				if(!hashPrincipal.containsKey(rsmPreparacos.getInt("TP_HORARIO"))){
					HashMap<String, Object> registerPreparacao = new HashMap<String, Object>();
					registerPreparacao.put("TP_HORARIO", rsmPreparacos.getInt("TP_HORARIO"));
					
					nomePreparacaoSeg = "";
					nomePreparacaoTer = "";
					nomePreparacaoQua = "";
					nomePreparacaoQui = "";
					nomePreparacaoSex = "";
					
					hashPrincipal.put(rsmPreparacos.getInt("TP_HORARIO"), registerPreparacao);
				}
				
				//Utiliza o hash daquele tipo de horario (com o bloco anterior, é garantido que sempre haverá um hash correspondente)
				HashMap<String, Object> registerPreparacao = hashPrincipal.get(rsmPreparacos.getInt("TP_HORARIO"));
				
				registerPreparacao.put("NR_DIA", rsmPreparacos.getString("NR_DIA"));
				
				if (rsmPreparacos.getInt("NR_DIA") == 1) {
					nomePreparacaoSeg  = (nomePreparacaoSeg=="" ? rsmPreparacos.getString("NM_PREPARACAO") : nomePreparacaoSeg + ", " + rsmPreparacos.getString("NM_PREPARACAO"));
					registerPreparacao.put("NM_REFEICAO_SEG", nomePreparacaoSeg);
				} else if (rsmPreparacos.getInt("NR_DIA") == 2) {
					nomePreparacaoTer = (nomePreparacaoTer=="" ? rsmPreparacos.getString("NM_PREPARACAO") : nomePreparacaoTer + ", " + rsmPreparacos.getString("NM_PREPARACAO"));
					registerPreparacao.put("NM_REFEICAO_TER", nomePreparacaoTer);
				} else if (rsmPreparacos.getInt("NR_DIA") == 3) {
					nomePreparacaoQua = (nomePreparacaoQua=="" ? rsmPreparacos.getString("NM_PREPARACAO") : nomePreparacaoQua + ", " + rsmPreparacos.getString("NM_PREPARACAO"));
					registerPreparacao.put("NM_REFEICAO_QUA", nomePreparacaoQua);
				} else if (rsmPreparacos.getInt("NR_DIA") == 4) {
					nomePreparacaoQui = (nomePreparacaoQui=="" ? rsmPreparacos.getString("NM_PREPARACAO") : nomePreparacaoQui + ", " + rsmPreparacos.getString("NM_PREPARACAO"));
					registerPreparacao.put("NM_REFEICAO_QUI", nomePreparacaoQui);
				} else if (rsmPreparacos.getInt("NR_DIA") == 5) {
					nomePreparacaoSex = (nomePreparacaoSex=="" ? rsmPreparacos.getString("NM_PREPARACAO") : nomePreparacaoSex + ", " + rsmPreparacos.getString("NM_PREPARACAO"));
					registerPreparacao.put("NM_REFEICAO_SEX", nomePreparacaoSex);
				}
			}
			
			ResultSetMap rsm = new ResultSetMap();
			for(Integer tpHorarioHash : hashPrincipal.keySet()){
				rsm.addRegister(hashPrincipal.get(tpHorarioHash));
			}

			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioServices.getAllRefeicoes: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioServices.getAllRefeicoes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para pegar todos os valores nutricionais de um cardapio
	 * @param cdCardapio Codigo do cardapio
	 * @return register Com os valores nutricionais
	 */
	public static Object getValoresNutricionais(int cdCardapioGrupo) {
		return getValoresNutricionais(cdCardapioGrupo, null);
	}

	public static Object getValoresNutricionais(int cdCardapioGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String calculo = "/100"; //colocando em grama o valor nutricional
			
			// Busca os valores nutricionais dos ingredientes
			pstmt = connect.prepareStatement("SELECT E.cd_ingrediente, COUNT(E.cd_ingrediente) AS quantidade, E.nm_ingrediente," +
											" (E.vl_kcal "+ calculo +") as vl_kcal," +
											" (E.vl_cho "+ calculo +") as vl_cho," +
											" (E.vl_ptn "+ calculo +") as vl_ptn," +
											" (E.vl_lip "+ calculo +") as vl_lip," +
											" (E.vl_fibras "+ calculo +") as vl_fibras," +
											" (E.vl_vit_a "+ calculo +") as vl_vit_a," +
											" (E.vl_vit_c "+ calculo +") as vl_vit_c," +
											" (E.vl_ca "+ calculo +") as vl_ca," +
											" (E.vl_fe "+ calculo +") as vl_fe," +
											" (E.vl_mg "+ calculo +") as vl_mg," +
											" (E.vl_zn "+ calculo +") as vl_zn" +
											" FROM cae_cardapio A" +
											" LEFT OUTER JOIN cae_refeicao B ON (A.cd_cardapio = B.cd_cardapio)" +
											" LEFT OUTER JOIN cae_preparacao C ON (B.cd_preparacao = C.cd_preparacao)" +
											" LEFT OUTER JOIN cae_preparacao_ingrediente D ON (B.cd_preparacao = D.cd_preparacao)" +
											" LEFT OUTER JOIN cae_ingrediente E ON (D.cd_ingrediente = E.cd_ingrediente)" +
											" WHERE A.cd_cardapio in"
												+ " (select cd_cardapio from cae_cardapio_grupo A join cae_cardapio B on (A.cd_cardapio_grupo = B.cd_cardapio_grupo)"
												+ " where A.cd_cardapio_grupo = " + cdCardapioGrupo + ")" +
											" GROUP BY E.cd_ingrediente" +
											" ORDER BY E.cd_ingrediente");
			ResultSetMap ingredientes = new ResultSetMap(pstmt.executeQuery());
			
			int dias = 20;
			Ingrediente cardapio = new Ingrediente();
			
			cardapio.setVlKcal(0.0);
			cardapio.setVlCho(0.0);
			cardapio.setVlPtn(0.0);
			cardapio.setVlLip(0.0);
			cardapio.setVlFibras(0.0);
			cardapio.setVlVitA(0.0);
			cardapio.setVlVitC(0.0);
			cardapio.setVlCa(0.0);
			cardapio.setVlFe(0.0);
			cardapio.setVlMg(0.0);
			cardapio.setVlZn(0.0);
			
			while (ingredientes.next()) {
				
				if (ingredientes.getInt("CD_INGREDIENTE")==0)
					ingredientes.next();
					
				// Busca do per capta do ingrediente
				Double perCapta = IngredienteServices.getPerCapta(ingredientes.getInt("CD_INGREDIENTE"), 0, cdCardapioGrupo, connect);
				
				// SOMA = VALOR NUTRICIONAL x PER CAPTA x Nº DE VEZES do ingrediente no cardapio
				cardapio.setVlKcal(cardapio.getVlKcal() + (ingredientes.getDouble("vl_kcal")*ingredientes.getDouble("quantidade")*perCapta));
				
				cardapio.setVlCho(cardapio.getVlCho() + (ingredientes.getDouble("vl_cho")*ingredientes.getDouble("quantidade")*perCapta));
				cardapio.setVlPtn(cardapio.getVlPtn() + (ingredientes.getDouble("vl_ptn")*ingredientes.getDouble("quantidade")*perCapta));
				cardapio.setVlLip(cardapio.getVlLip() + (ingredientes.getDouble("vl_lip")*ingredientes.getDouble("quantidade")*perCapta));
				cardapio.setVlFibras(cardapio.getVlFibras() + (ingredientes.getDouble("vl_fibras")*ingredientes.getDouble("quantidade")*perCapta));
				cardapio.setVlVitA(cardapio.getVlVitA() + (ingredientes.getDouble("vl_vit_a")*ingredientes.getDouble("quantidade")*perCapta));
				cardapio.setVlVitC(cardapio.getVlVitC() + (ingredientes.getDouble("vl_vit_c")*ingredientes.getDouble("quantidade")*perCapta));
				cardapio.setVlCa(cardapio.getVlCa() + (ingredientes.getDouble("vl_ca")*ingredientes.getDouble("quantidade")*perCapta));
				cardapio.setVlFe(cardapio.getVlFe() + (ingredientes.getDouble("vl_fe")*ingredientes.getDouble("quantidade")*perCapta));
				cardapio.setVlMg(cardapio.getVlMg() + (ingredientes.getDouble("vl_mg")*ingredientes.getDouble("quantidade")*perCapta));
				cardapio.setVlZn(cardapio.getVlZn() + (ingredientes.getDouble("vl_zn")*ingredientes.getDouble("quantidade")*perCapta));
			}
			
			// Divide os valores nutricionais do cardapio pela quantidade de dias do mes
			cardapio.setVlKcal(cardapio.getVlKcal()/dias);
			cardapio.setVlCho(cardapio.getVlCho()/dias);
			cardapio.setVlPtn(cardapio.getVlPtn()/dias);
			cardapio.setVlLip(cardapio.getVlLip()/dias);
			cardapio.setVlFibras(cardapio.getVlFibras()/dias);
			cardapio.setVlVitA(cardapio.getVlVitA()/dias);
			cardapio.setVlVitC(cardapio.getVlVitC()/dias);
			cardapio.setVlCa(cardapio.getVlCa()/dias);
			cardapio.setVlFe(cardapio.getVlFe()/dias);
			cardapio.setVlMg(cardapio.getVlMg()/dias);
			cardapio.setVlZn(cardapio.getVlZn()/dias);
			
			
			// Busca dos valores de recomendacao nutricional do grupo do cardapio
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.* FROM cae_recomendacao_nutricional A" + 
											" JOIN cae_modalidade B ON (A.cd_recomendacao_nutricional = B.cd_recomendacao_nutricional) " +
											" JOIN cae_cardapio_grupo C ON (B.cd_modalidade = C.cd_modalidade) " + 
											" WHERE C.cd_cardapio_grupo = " + cdCardapioGrupo).executeQuery());
			
			/**
			 * Preenchimento do register para ser retornado do metodo com os valores nutricionais do cardapio.
			 * Atribuido: 0 - caso o valor esteja abaixo do minimo de recomendacao nutricional
			 * 			  1 - caso o valor esteja dentro do da recomendacao nutricional
			 * 			  2 - caso o valor esteja acima do maximo de recomendacao nutricional
			 */
			HashMap<String, Object> register = new HashMap<String, Object>();
			while(rsm.next()) {
				register.put("VL_KCAL", cardapio.getVlKcal());
				if(cardapio.getVlKcal() <= rsm.getDouble("VL_KCAL_MIN"))
					register.put("VL_KCAL_VALIDO", 0);
				else if (cardapio.getVlKcal() >= rsm.getDouble("VL_KCAL_MAX"))
					register.put("VL_KCAL_VALIDO", 2);
				else
					register.put("VL_KCAL_VALIDO", 1);
				
				register.put("VL_CHO", cardapio.getVlCho());
				if(cardapio.getVlCho() <= rsm.getDouble("VL_CHO_MIN"))
					register.put("VL_CHO_VALIDO", 0);
				else if (cardapio.getVlCho() >= rsm.getDouble("VL_CHO_MAX"))
					register.put("VL_CHO_VALIDO", 2);
				else
					register.put("VL_CHO_VALIDO", 1);
				
				register.put("VL_PTN", cardapio.getVlPtn());
				if(cardapio.getVlPtn() <= rsm.getDouble("VL_PTN_MIN"))
					register.put("VL_PTN_VALIDO", 0);
				else if (cardapio.getVlPtn() >= rsm.getDouble("VL_PTN_MAX"))
					register.put("VL_PTN_VALIDO", 2);
				else
					register.put("VL_PTN_VALIDO", 1);
				
				register.put("VL_LIP", cardapio.getVlLip());
				if(cardapio.getVlLip() <= rsm.getDouble("VL_LIP_MIN"))
					register.put("VL_LIP_VALIDO", 0);
				else if (cardapio.getVlLip() >= rsm.getDouble("VL_LIP_MAX"))
					register.put("VL_LIP_VALIDO", 2);
				else
					register.put("VL_LIP_VALIDO", 1);
				
				register.put("VL_FIBRAS", cardapio.getVlFibras());
				if(cardapio.getVlFibras() <= rsm.getDouble("VL_FIBRAS_MIN"))
					register.put("VL_FIBRAS_VALIDO", 0);
				else if (cardapio.getVlFibras() >= rsm.getDouble("VL_FIBRAS_MAX"))
					register.put("VL_FIBRAS_VALIDO", 2);
				else
					register.put("VL_FIBRAS_VALIDO", 1);
				
				register.put("VL_VIT_A", cardapio.getVlVitA());
				if(cardapio.getVlVitA() <= rsm.getDouble("VL_VIT_A_MIN"))
					register.put("VL_VIT_A_VALIDO", 0);
				else if (cardapio.getVlVitA() >= rsm.getDouble("VL_VIT_A_MAX"))
					register.put("VL_VIT_A_VALIDO", 2);
				else
					register.put("VL_VIT_A_VALIDO", 1);
				
				register.put("VL_VIT_C", cardapio.getVlVitC());
				if(cardapio.getVlVitC() <= rsm.getDouble("VL_VIT_C_MIN"))
					register.put("VL_VIT_C_VALIDO", 0);
				else if (cardapio.getVlVitC() >= rsm.getDouble("VL_VIT_C_MAX"))
					register.put("VL_VIT_C_VALIDO", 2);
				else
					register.put("VL_VIT_C_VALIDO", 1);
				
				register.put("VL_CA", cardapio.getVlCa());
				if(cardapio.getVlCa() <= rsm.getDouble("VL_CA_MIN"))
					register.put("VL_CA_VALIDO", 0);
				else if (cardapio.getVlCa() >= rsm.getDouble("VL_CA_MAX"))
					register.put("VL_CA_VALIDO", 2);
				else
					register.put("VL_CA_VALIDO", 1);

				register.put("VL_FE", cardapio.getVlFe());
				if(cardapio.getVlFe() <= rsm.getDouble("VL_FE_MIN"))
					register.put("VL_FE_VALIDO", 0);
				else if (cardapio.getVlFe() >= rsm.getDouble("VL_FE_MAX"))
					register.put("VL_FE_VALIDO", 2);
				else
					register.put("VL_FE_VALIDO", 1);

				register.put("VL_MG", cardapio.getVlMg());
				if(cardapio.getVlMg() <= rsm.getDouble("VL_MG_MIN"))
					register.put("VL_MG_VALIDO", 0);
				else if (cardapio.getVlMg() >= rsm.getDouble("VL_MG_MAX"))
					register.put("VL_MG_VALIDO", 2);
				else
					register.put("VL_MG_VALIDO", 1);
				
				register.put("VL_ZN", cardapio.getVlZn());
				if(cardapio.getVlZn() <= rsm.getDouble("VL_ZN_MIN"))
					register.put("VL_ZN_VALIDO", 0);
				else if (cardapio.getVlZn() >= rsm.getDouble("VL_ZN_MIN"))
					register.put("VL_ZN_VALIDO", 2);
				else
					register.put("VL_ZN_VALIDO", 1);
			}
			
			return register;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioServices.getValoresNutricionais: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioServices.getValoresNutricionais: " + e);
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
		return Search.find("SELECT * FROM cae_cardapio ORDER BY nm_cardapio", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
