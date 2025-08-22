package com.tivic.manager.fta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tivic.manager.bpm.BemManutencao;
import com.tivic.manager.bpm.BemManutencaoDAO;
import com.tivic.manager.bpm.ComponenteReferencia;
import com.tivic.manager.bpm.ComponenteReferenciaDAO;
import com.tivic.manager.bpm.ComponenteReferenciaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class VeiculoServices {

	public static final String[] tipoCombustivel = {"Gasolina", "Alcool", "Diesel", "Gás Natural", "Gasolina/álcool", "Gasolina/Gás", "Alcool/Gás", "Biodiesel", "S500", "S10"};
	public static final String[] tipoCombustivelAbastecimento = {"Gasolina", "Alcóol", "Diesel", "Gás Natural", "Biodiesel", "Diesel S10", "Diesel S500"};
	public static final String[] tipoEixo = {"Simples", "Duplo"};
	public static final String[] tipoReboque = {"Nenhum", "Madeira/Aberta", "Madeira/Fechada", "Baú", "Tanque", "Cegonha", "Outro"};
	public static final String[] tipoCarga = {"Nenhuma", "Comum", "Explosivos", "Gases Inflamáveis", "Liquidos Inflamáveis", "Sólidos Inflamáveis", "Radioativos", "Corrosivos", "Substâncias diversas"};
	public static final String[] coresVeiculo = {"AMARELA", "AZUL", "BEGE ", "BRANCA", "CINZA", "DOURADA", "GRENA", "LARANJA", "MARROM", "PRATA", "PRETA", "ROSA", "ROXA", "VERDE", "VERMELHA", "FANTASIA"};
	public static final String[] coresVeiculoRBG = {"#FFFF00", "#0066FF", "#FFFF99 ", "#FFFFFF", "#CCCCCC", "#FFCC00", "", "#FF9900", "#996600", "#CCCCCC", "#000000", "#FF99CC", "#6633CC", "#009900", "#FF0000", ""};

	//pneu
	public static final String[] tipoMovimentacaoPneu = {"Novo", "Em Atividade", "Em manutenção", "Baixa"};
	public static final String[] tipoLocalPneu = {"Dianteiro", "Traseiro", "No veiculo"};
	public static final String[] tipoLadoPneu = {"Direito", "Esquerdo", "No veiculo"};
	public static final String[] tipoDisposicaoPneu = {"Interno", "Externo", "No veiculo"};
	public static final String[] tipoAquisicaoPneu = {"Novo", "Semi-novo", "Recapado", "Remoldado", "Carcaça"};

	/* Para o campo st_cadastro*/
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;

	public static String[] situacaoVeiculo = {"Inativo","Ativo"};
	
	public static final int TP_NAO_ADAPTADO       = 0;
	public static final int TP_ADAPTADO           = 1;
	public static final String[] tipoAdaptado = {"Não Adaptado", "Adaptado"};
	

	public static Result save(Veiculo veiculo){
		return save(veiculo, null, true, null);
	}

	public static Result save(Veiculo veiculo, Reboque reboque){
		return save(veiculo, reboque, true, null);
	}
	
	public static Result save(Veiculo veiculo, Reboque reboque, boolean lgBpm){
		return save(veiculo, reboque, lgBpm, null);
	}
	
	public static Result save(Veiculo veiculo, Reboque reboque, Connection connect){
		return save(veiculo, reboque, false, connect);
	}

	public static Result save(Veiculo veiculo, Reboque reboque, boolean lgBpm, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 1;
			if(reboque!=null){
				if(reboque.getCdReboque()==0){
					retorno = ReboqueDAO.insert(reboque, connect);
					veiculo.setCdReboque(retorno);
				}
				else
					retorno = ReboqueDAO.update(reboque, connect);
			}

			/**
			 * Valida a existencia de outro veiculo com a placa informada
			 */
			if( connect.prepareStatement("SELECT * FROM fta_veiculo " +
										 " WHERE nr_placa = '"+veiculo.getNrPlaca()+"' "+
										 "   AND cd_veiculo <> "+veiculo.getCdVeiculo() ).executeQuery().next()){

				return new Result(-2, "A placa "+veiculo.getNrPlaca()+" está cadastrada para outro veículo! Pesquise pela placa e veja se encontra outro registro");
			}

			if(veiculo.getCdVeiculo()==0){
//				if(!Util.isStrBaseAntiga() && lgBpm) {
//					//Bem bem = new Bem(0, 0, veiculo.getNmModelo(), "0", "0", "0", "0", 0, "0", "0", 0, 0, veiculo.getCdMarca(), veiculo.getNmModelo(), 0, veiculo.getNrTabelaReferencia(), 0, new Float(0.0));
//					Bem bem = new Bem();
//					bem.setCdBem(0);
//					bem.setNmProdutoServico(veiculo.getNmModelo());
//					bem.setCdBem(veiculo.getCdMarca());
//					bem.setNmModelo(veiculo.getNmModelo());
//					bem.setNrReferencia(veiculo.getNrTabelaReferencia());
//
//					retorno = BemDAO.insert(bem, connect);
//					veiculo.setCdBem(retorno);
//				}
				
				retorno = VeiculoDAO.insert(veiculo, connect);
				if(retorno>0){
					veiculo.setCdVeiculo(retorno);

					/*
					for(int i=0; retorno>0 && i<componentes.length; i++){
						componentes[i].setCdReferencia(veiculo.getCdVeiculo());
						retorno = ComponenteReferenciaDAO.insert(componentes[i], connect);
					}*/
				}
			}else{
				if(retorno>0 && veiculo.getCdReboque()!=0 && reboque==null){
					retorno = ReboqueServices.delete(veiculo.getCdReboque(), connect);
					veiculo.setCdReboque(0);
				}

				if(retorno>0)
					retorno = VeiculoDAO.update(veiculo, connect);
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VEICULO", veiculo);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}
	
	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM fta_veiculo A " +
						 "RIGHT JOIN mob_concessao_veiculo B on (A.cd_veiculo = B.cd_veiculo) " +
			             "LEFT JOIN mob_concessao C ON (B.cd_concessao = C.cd_concessao) " +
			             "WHERE C.tp_concessao = 0;";
			
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT * FROM fta_veiculo A " +
						 "RIGHT JOIN mob_concessao_veiculo B on (A.cd_veiculo = B.cd_veiculo) " +
			             "LEFT JOIN mob_concessao C ON (B.cd_concessao = C.cd_concessao) " +
			             "WHERE C.tp_concessao = 0;";
			
			pstmt = connect.prepareStatement(sql);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static int delete(int cdVeiculo) {
		return delete(cdVeiculo, null);
	}

	public static int delete(int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Veiculo veiculo = VeiculoDAO.get(cdVeiculo, connect);
			if(veiculo==null)
				return -1;

			//deletando manutencoes
			//			int retorno = ManutencaoBemServices.deleteByReferencia(cdVeiculo, connect);

			int retorno = 1;
			//deletando pneus
			if(retorno>0)
				retorno = PneuServices.deleteByVeiculo(cdVeiculo, connect);

			//deletando componentes
			if(retorno>0)
				retorno = ComponenteReferenciaServices.deleteByReferencia(cdVeiculo, connect);

			//deletando abastecimentos
			if(retorno>0)
				retorno = AbastecimentoServices.deleteByVeiculo(cdVeiculo, connect);

			//deletando veiculo
			if(retorno>0)
				retorno = VeiculoDAO.delete(cdVeiculo, connect);

			//deletando reboque
			if(veiculo.getCdReboque()>0 && retorno>0){
				retorno = ReboqueServices.delete(veiculo.getCdReboque(), connect);
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result remove(int cdVeiculo){
		return remove(cdVeiculo, null);
	}

	public static Result remove(int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Veiculo veiculo = VeiculoDAO.get(cdVeiculo, connect);
			if(veiculo==null)
				return new Result(-1, "Não foi encontrado nenhum veículo com o código informado!");

			//deletando manutencoes
			//			int retorno = ManutencaoBemServices.deleteByReferencia(cdVeiculo, connect);

			int retorno = 1;
			//deletando pneus
			if(retorno>0)
				retorno = PneuServices.deleteByVeiculo(cdVeiculo, connect);

			//deletando componentes
			if(retorno>0)
				retorno = ComponenteReferenciaServices.deleteByReferencia(cdVeiculo, connect);

			//deletando abastecimentos
			if(retorno>0)
				retorno = AbastecimentoServices.deleteByVeiculo(cdVeiculo, connect);

			//deletando veiculo
			if(retorno>0)
				retorno = VeiculoDAO.delete(cdVeiculo, connect);

			//deletando reboque
			if(veiculo.getCdReboque()>0 && retorno>0){
				retorno = ReboqueServices.delete(veiculo.getCdReboque(), connect);
			}

			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluí­do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluí­do com sucesso!");
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

	public static ResultSetMap findAllVeiculosByFrota(int cdFrota){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_FROTA", String.valueOf(cdFrota), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap findVeiculoAtivosMobilidade(ArrayList<ItemComparator> criterios) {
		return findVeiculoAtivosMobilidade(criterios, null);
	}

	public static ResultSetMap findAllVeiculosSemFrota(ArrayList<ItemComparator> criterios){
		criterios.add(new ItemComparator("A.CD_FROTA", "-1", ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}

	public static ResultSetMap findVeiculoAtivosMobilidade(ArrayList<ItemComparator> criterios, Connection connect) {

		String fieldsMobilidade = ""; 
		String queryMobilidade  = ""; 
		Boolean pesquisaPonto   = false;
		
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_GRUPO_PARADA")) {
				pesquisaPonto = true;
			}else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_PONTO")) {
				pesquisaPonto = true;
			}else if (criterios.get(i).getColumn().equalsIgnoreCase("DS_REFERENCIA")) {
				pesquisaPonto = true;
			}else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_ORDEM")) {
				pesquisaPonto = true;
			}else if (criterios.get(i).getColumn().equalsIgnoreCase("pesquisaPonto")) {
				pesquisaPonto = true;
				criterios.remove(i);
				i--;
			}
		}
		
		criterios.add(new ItemComparator("A.st_veiculo", "" + ST_ATIVO , ItemComparator.EQUAL, Types.INTEGER));

		queryMobilidade  = " LEFT OUTER JOIN mob_concessao_veiculo D ON (A.cd_veiculo = D.cd_veiculo ) " +
						   " LEFT OUTER JOIN mob_concessao         I ON (D.cd_concessao = I.cd_concessao ) " +
						   " LEFT OUTER JOIN grl_pessoa            J ON (I.cd_concessionario = J.cd_pessoa ) "+
						   (pesquisaPonto ? 
						   "  LEFT OUTER JOIN mob_parada           M ON (D.cd_concessao = M.cd_concessao) " +
						   "  LEFT OUTER JOIN mob_grupo_parada     N ON (M.cd_grupo_parada = N.cd_grupo_parada) " : "" );
		
		fieldsMobilidade = " , D.*, E.*, J.NM_PESSOA as NM_CONCESSIONARIO "+
				          (pesquisaPonto ? 
				           ", N.nm_grupo_parada AS NR_PONTO, M.ds_referencia AS NR_ORDEM " : "");


		return Search.find(" SELECT A.*, A.st_veiculo, A2.*, A3.*, B.*, C.*, C.NM_PESSOA AS NM_PROPRIETARIO, E.nm_frota, " +
				"E.id_frota, F.NM_PESSOA AS NM_RESPONSAVEL, G.NM_CIDADE, H.SG_ESTADO, B1.nm_marca AS NM_MARCA_CARROCERIA, " +
				"B1.nm_modelo AS NM_MODELO_CARROCERIA, K.cd_plano_vistoria, K.nm_plano_vistoria "+
				fieldsMobilidade+
				" FROM fta_veiculo A " +
//				" JOIN bpm_referencia 			     A2 ON (A.cd_veiculo      = A2.cd_referencia) " +
//				" JOIN bpm_bem        			     A3 ON (A2.cd_bem         = A3.cd_bem) " +
				" LEFT OUTER JOIN fta_marca_modelo   B  ON (A.cd_marca        = B.cd_marca) " +
				" LEFT OUTER JOIN fta_marca_modelo   B1 ON (A.cd_marca_carroceria = B1.cd_marca) " +
				" LEFT OUTER JOIN grl_pessoa         C  ON (A.cd_proprietario = C.cd_pessoa) " +
				" LEFT OUTER JOIN fta_frota          E  ON (A.cd_frota        = E.cd_frota) " +
				" LEFT OUTER JOIN grl_pessoa         F  ON (E.cd_responsavel  = F.cd_pessoa) " +
				" LEFT OUTER JOIN grl_cidade         G  ON (A.cd_cidade       = G.cd_cidade) " +
				" LEFT OUTER JOIN grl_estado         H  ON (G.cd_estado       = H.cd_estado) " +
				" LEFT OUTER JOIN mob_plano_vistoria K  ON (A.cd_plano_vistoria = K.cd_plano_vistoria) " +
				queryMobilidade+" ",
				" ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);

	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {

		LogUtils.debug("VeiculoServices.find");
		LogUtils.createTimer("VEICULO_FIND_TIMER");
		String fieldsMobilidade = ""; 
		String queryMobilidade = ""; 	
		boolean pesquisaPonto = false;
		ResultSetMap rsmVeiculos;
		
		if(Util.isStrBaseAntiga()) { 
			rsmVeiculos = Search.find("SELECT * FROM fta_veiculo A", " ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		} else {
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
	
				if (criterios.get(i).getColumn().equalsIgnoreCase("lgMobilidade")) {
					queryMobilidade  = " LEFT OUTER JOIN mob_concessao_veiculo D ON (A.cd_veiculo = D.cd_veiculo ) " +
									   " LEFT OUTER JOIN mob_concessao 		   I ON (D.cd_concessao = I.cd_concessao ) " +
									   " LEFT OUTER JOIN grl_pessoa            J ON (I.cd_concessionario = J.cd_pessoa ) ";
					fieldsMobilidade = " , D.*, E.*, I.DT_INICIO_CONCESSAO, I.DT_FINAL_CONCESSAO, J.NM_PESSOA as NM_CONCESSIONARIO ";
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("NM_GRUPO_PARADA")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_PONTO")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("DS_REFERENCIA")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_ORDEM")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("pesquisaPonto")) {
					pesquisaPonto = true;
					criterios.remove(i);
					i--;
				}
			}	
			
			if (pesquisaPonto){
				queryMobilidade += " LEFT OUTER JOIN mob_parada			   K ON (I.cd_concessao = K.cd_concessao) "+
						   		   " LEFT OUTER JOIN mob_grupo_parada      L ON (K.cd_grupo_parada = L.cd_grupo_parada) ";
				
				fieldsMobilidade += ", K.CD_PARADA, L.CD_GRUPO_PARADA, K.ds_referencia AS NR_ORDEM, L.nm_grupo_parada AS NR_PONTO ";
			}
			
			String sql = " SELECT A.*, A.st_veiculo, B.*, C.*, C.NM_PESSOA AS NM_PROPRIETARIO, E.nm_frota, " +
					"E.id_frota, F.NM_PESSOA AS NM_RESPONSAVEL, G.NM_CIDADE, H.SG_ESTADO, B1.nm_marca AS NM_MARCA_CARROCERIA, " +
					"B1.nm_modelo AS NM_MODELO_CARROCERIA, M.cd_categoria, M.nm_categoria, N.CD_PLANO_VISTORIA, N.NM_PLANO_VISTORIA, "+
					"Q.cd_cor, Q.nm_cor as nm_cor_veiculo, O.*, P.* " +
					fieldsMobilidade+
					" FROM fta_veiculo A " +
//					" JOIN bpm_referencia 			        A2 ON (A.cd_veiculo      = A2.cd_referencia) " +
//					" JOIN bpm_bem        			        A3 ON (A2.cd_bem         = A3.cd_bem) " +
					" LEFT OUTER JOIN fta_marca_modelo      B  ON (A.cd_marca        = B.cd_marca) " +
					" LEFT OUTER JOIN fta_marca_modelo      B1 ON (A.cd_marca_carroceria = B1.cd_marca) " +
					" LEFT OUTER JOIN grl_pessoa            C  ON (A.cd_proprietario = C.cd_pessoa) " +
					" LEFT OUTER JOIN fta_frota             E  ON (A.cd_frota        = E.cd_frota) " +
					" LEFT OUTER JOIN grl_pessoa            F  ON (E.cd_responsavel  = F.cd_pessoa) " +
					" LEFT OUTER JOIN grl_cidade            G  ON (A.cd_cidade       = G.cd_cidade) " +
					" LEFT OUTER JOIN grl_estado            H  ON (G.cd_estado       = H.cd_estado) " +
					" LEFT OUTER JOIN fta_categoria_veiculo M  ON (A.cd_categoria    = M.cd_categoria) "+
					" LEFT OUTER JOIN mob_plano_vistoria    N  ON (A.cd_plano_vistoria = N.cd_plano_vistoria) " +
					" LEFT OUTER JOIN fta_cor               Q  ON (A.cd_cor          = Q.cd_cor) " +
					" LEFT OUTER JOIN fta_tipo_veiculo      O  ON (A.cd_tipo_veiculo = O.cd_tipo_veiculo) " +
					" LEFT OUTER JOIN fta_especie_veiculo   P  ON (A.cd_especie = P.cd_especie) " +
					queryMobilidade+" ";

			//System.out.println(Search.getStatementSQL(sql, " ", criterios, true));
			//LogUtils.debug("SQL:\n"+Search.getStatementSQL(sql, " ", criterios, true));
			
			rsmVeiculos = Search.find(sql, " ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		
		//LogUtils.debug(rsmVeiculos.size() + " registro(s)");
		//LogUtils.logTimer("VEICULO_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
		//LogUtils.destroyTimer("VEICULO_FIND_TIMER");
		
		return rsmVeiculos;

	}	

	public static ResultSetMap findVeiculosByCodigo(int cdVeiculo) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_VEICULO", String.valueOf(cdVeiculo), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return find(criterios, null);
	}

	public static ResultSetMap findVeiculoModeloMarca(ArrayList<ItemComparator> criterios) {
		return findVeiculoModeloMarca(criterios, null);
	}

	public static ResultSetMap findVeiculoModeloMarca(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find(" SELECT * " +
						   " FROM fta_veiculo A, fta_modelo_veiculo B, bpm_marca C " +
						   " WHERE A.cd_modelo = B.cd_modelo " +
						   "   AND B.cd_marca  = C.cd_marca ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap findAllVeiculos() {
		return findCompleto(new ArrayList<ItemComparator>(), null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find(" SELECT A.*, B.tp_reboque, B.tp_carga, B.nr_capacidade as nr_capacidade_reboque, " +
				"        B.tp_eixo_dianteiro as tp_eixo_dianteiro_reboque, " +
				"        B.tp_eixo_traseiro as tp_eixo_traseiro_reboque, " +
				"        B.qt_eixos_dianteiros as qt_eixos_dianteiros_reboque, " +
				"        B.qt_eixos_traseiros as qt_eixos_traseiros_reboque, " +
				"        C.nm_modelo, D.cd_marca, D.nm_marca," +
				"        E.*, F.*, F.nm_pessoa as nm_proprietario, " +
				"		 M.cd_categoria, M.nm_categoria, N.cd_plano_vistoria, N.nm_plano_vistoria "+	
				" FROM fta_veiculo A " +
				" LEFT OUTER JOIN fta_reboque 		B ON (A.cd_reboque=B.cd_reboque) " +
				" LEFT OUTER JOIN fta_modelo_veiculo C ON (A.cd_modelo=C.cd_modelo) " +
				" LEFT OUTER JOIN bpm_marca 			D ON (A.cd_marca=D.cd_marca) "+
				" LEFT OUTER JOIN fta_tipo_veiculo 	E ON (A.cd_tipo_veiculo=E.cd_tipo_veiculo) "+
				" LEFT OUTER JOIN grl_pessoa 		F ON (A.cd_proprietario = F.cd_pessoa) "+
				" LEFT OUTER JOIN grl_cidade            G  ON (A.cd_cidade       = G.cd_cidade) " +
				" LEFT OUTER JOIN grl_estado            H  ON (G.cd_estado       = H.cd_estado) " +
				" LEFT OUTER JOIN fta_categoria_veiculo M  ON (A.cd_categoria    = M.cd_categoria) "+
				" LEFT OUTER JOIN mob_plano_vistoria    N  ON (A.cd_plano_vistoria = N.cd_plano_vistoria) ",
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios) {
		return Search.find(" SELECT *, B.nm_pessoa as nm_proprietario  FROM fta_veiculo A, grl_pessoa B WHERE A.cd_proprietario = B.cd_pessoa ", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap getCompleto(int cdVeiculo) {
		return getCompleto(cdVeiculo, null);
	}

	public static ResultSetMap getCompleto(int cdVeiculo, Connection connect) {
		try {
			connect = (connect == null ? Conexao.conectar() : connect);

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_VEICULO", String.valueOf(cdVeiculo), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			ResultSetMap rsm = findCompleto(criterios, connect);
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.getCompleto: " +  e);
			Conexao.rollback(connect);
			return null;
		}
	}

	public static ResultSetMap getAllByProprietario(int cdProprietario) {
		return getAllByProprietario(cdProprietario, null);
	}

	public static ResultSetMap getAllByProprietario(int cdProprietario, Connection connect) {
		try {
			connect = (connect == null ? Conexao.conectar() : connect);
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM fta_veiculo A "
					+" LEFT JOIN fta_marca_modelo C on ( A.cd_marca = C.cd_marca) "
					+" JOIN grl_pessoa B on (A.cd_proprietario = B.cd_pessoa) "
					+" WHERE cd_proprietario = " + cdProprietario).executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.getAllByProprietario: " + sqlExpt);
			Conexao.rollback(connect);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.getAllByProprietario: " +  e);
			Conexao.rollback(connect);
			return null;
		}
	}

	public static ResultSetMap getAllByConcessao(int cdConcessao) {
		return getAllByConcessao(cdConcessao, null);
	}

	public static ResultSetMap getAllByConcessao(int cdConcessao, Connection connect) {
		try {
			connect = (connect == null ? Conexao.conectar() : connect);

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fta_veiculo A "
																+ "JOIN mob_concessao_veiculo B ON (A.cd_veiculo = B.cd_veiculo)"
																+ "WHERE cd_concessao = ?"
																+ "ORDER BY nr_prefixo");
			pstmt.setInt(1, cdConcessao);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			return rsm;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.getAllByConcessao: " + sqlExpt);
			Conexao.rollback(connect);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.getAllByConcessao: " +  e);
			Conexao.rollback(connect);
			return null;
		}
	}

	public static int insertPneuVeiculo(ComponenteReferencia componente, Pneu pneu){
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);

			/*
			 * 1. inserir componente
			 * 2. inserir pneu
			 * 3. inserir manutencao preventiva a cada Xkm
			 */
			int cdComponente = ComponenteReferenciaDAO.insert(componente, connect);
			pneu.setCdComponentePneu(cdComponente);
			PneuDAO.insert(pneu, connect);

			connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.insertPneuVeiculo: " + sqlExpt);
			Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.insertPneuVeiculo: " +  e);
			Conexao.rollback(connect);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int deletePneuVeiculo(int cdComponentePneu) {
		return deletePneuVeiculo(cdComponentePneu, null);
	}

	public static int deletePneuVeiculo(int cdComponentePneu, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(PneuDAO.delete(cdComponentePneu, connect)==1)
				ComponenteReferenciaDAO.delete(cdComponentePneu, connect);
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.deletePneuVeiculo: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findPneusVeiculoReboque(int cdVeiculo, int cdReboque) {
		return findPneusVeiculoReboque(cdVeiculo, cdReboque, null);
	}

	public static ResultSetMap findPneusVeiculoReboque(int cdVeiculo, int cdReboque, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(" SELECT * " +
					" FROM bpm_componente_referencia A " +
					" INNER JOIN fta_pneu B ON (A.cd_componente=B.cd_componente_pneu) " +
					" INNER JOIN fta_posicao_pneu C ON (C.cd_posicao=B.cd_posicao) " +
					" INNER JOIN fta_modelo_pneu D ON (D.cd_modelo=B.cd_modelo) " +
					" INNER JOIN bpm_marca E ON (E.cd_marca=B.cd_marca) " +
					" WHERE A.cd_referencia in (?, ?)");
			pstmt.setInt(1,cdVeiculo);
			pstmt.setInt(2,cdReboque);

			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.findPneusVeiculoReboque: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.findPneusVeiculoReboque: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static boolean hasVeiculoByPlaca(String nrPlaca) {
		return hasVeiculoByPlaca(nrPlaca, null);
	}

	public static boolean hasVeiculoByPlaca(String nrPlaca, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(" SELECT * " +
										 	 " FROM fta_veiculo  " +
										 	 " WHERE nr_placa = ?");
			pstmt.setString(1,nrPlaca);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.findPneusVeiculoReboque: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.findPneusVeiculoReboque: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Veiculo getVeiculoByPlaca(String nrPlaca) {
		return getVeiculoByPlaca(nrPlaca, null);
	}

	public static Veiculo getVeiculoByPlaca(String nrPlaca, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(" SELECT * " +
										 	 " FROM fta_veiculo A  " +
											 " WHERE A.nr_placa = ?");
			pstmt.setString(1, nrPlaca);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return VeiculoDAO.get(rs.getInt("cd_veiculo"), connect);

			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.getVeiculoByPlaca: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.getVeiculoByPlaca: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int addComponentes(int cdVeiculo, ArrayList<Integer> componentes){
		return addComponentes(cdVeiculo, componentes, null);
	}
	public static int addComponentes(int cdVeiculo, ArrayList<Integer> componentes, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 0;

			for(int i=0; i<componentes.size(); i++){
				TipoComponente tipoComponente = TipoComponenteDAO.get(componentes.get(i), connect);

				ComponenteVeiculo componente = new ComponenteVeiculo(0, //cdComponente
						cdVeiculo, //cdReferencia
						tipoComponente.getNmTipoComponente(), //nmComponente
						null, //dtGarantia
						null, //dtValidade
						null, //dtAquisicao
						null, //dtBaixa
						"", //nrSerie
						ComponenteReferenciaServices.ST_EM_USO, //stComponente
						tipoComponente.getCdTipoComponente(), //cdTipoComponente
						0, //cdMarca
						0, //qtHodometroUltimaManutencao
						tipoComponente.getQtHodometroValidade(), //qtHodometroValidade
						tipoComponente.getQtHodometroManutencao(), //qtHodometroManutencao
						tipoComponente.getTpRecorrenciaManutencao(), //tpRecorrenciaManutencao
						tipoComponente.getQtIntervaloRecorrencia(), //qtIntervaloRecorrencia
						null, //dtInstalacao
						tipoComponente.getTxtTipoComponente(), //txtObservacao
						new GregorianCalendar()); //dtInicioRecorrencia

				retorno = ComponenteVeiculoServices.save(componente, connect);
				if(retorno<0)
					break;
			}
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.addComponentes: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.addComponentes: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findComponentesByTipo(int cdVeiculo, int cdTipoComponente) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_REFERENCIA", String.valueOf(cdVeiculo), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		criterios.add(new ItemComparator("B.CD_TIPO_COMPONENTE", String.valueOf(cdTipoComponente), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return findComponentes(criterios, null);
	}

	public static ResultSetMap findComponentes(int cdVeiculo) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_REFERENCIA", String.valueOf(cdVeiculo), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return findComponentes(criterios, null);
	}

	public static ResultSetMap findComponentes(ArrayList<ItemComparator> criterios) {
		return findComponentes(criterios, null);
	}

	public static ResultSetMap findComponentes(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * " +
				" FROM bpm_componente_referencia A " +
				" JOIN fta_componente_veiculo B ON (A.cd_componente = B.cd_componente)", "ORDER BY nm_componente", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap findOcorrencias(int cdVeiculo) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_VEICULO", String.valueOf(cdVeiculo), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return findOcorrencias(criterios, null);
	}

	public static ResultSetMap findOcorrencias(ArrayList<ItemComparator> criterios) {
		return findOcorrencias(criterios, null);
	}

	public static ResultSetMap findOcorrencias(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * " +
				" FROM fta_ocorrencia A " +
				" JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) " +
				" LEFT OUTER JOIN fta_componente_veiculo C ON (A.cd_componente = C.cd_componente) " +
				" LEFT OUTER JOIN bpm_componente_referencia D ON (A.cd_componente = D.cd_componente) " +
				" LEFT OUTER JOIN grl_pessoa E ON (B.cd_pessoa = E.cd_pessoa) ", "ORDER BY st_ocorrencia, dt_ocorrencia DESC", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap findCheckups(int cdVeiculo) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_VEICULO", String.valueOf(cdVeiculo), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return findCheckups(criterios, null);
	}

	public static ResultSetMap findCheckups(ArrayList<ItemComparator> criterios) {
		return findCheckups(criterios, null);
	}

	public static ResultSetMap findCheckups(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.*, D.nm_pessoa as nm_usuario, F.nm_pessoa as nm_usuario_responsavel, " +
				" (SELECT COUNT(G.cd_checkup_item) FROM fta_veiculo_checkup_item G WHERE G.cd_checkup = A.cd_checkup ) as qt_itens " +
				" FROM fta_veiculo_checkup A " +
				" LEFT OUTER JOIN agd_agendamento B ON (A.cd_agendamento = B.cd_agendamento) " +
				" LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario = C.cd_usuario) " +
				" LEFT OUTER JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa) "+
				" LEFT OUTER JOIN seg_usuario E ON (A.cd_usuario_responsavel = E.cd_usuario) " +
				" LEFT OUTER JOIN grl_pessoa F ON (E.cd_pessoa = F.cd_pessoa) ", "ORDER BY st_checkup, dt_checkup DESC", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static int insertManutencaoComponenteVeiculo(int cdComponente, int qtComponente, BemManutencao manutencao){
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);

			/*
			 * 1. inserir manutencao
			 * 2. inserir manutencao_componente
			 */
			int cdManutencao = BemManutencaoDAO.insert(manutencao, connect);
			if(cdManutencao>0){
				PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bpm_manutencao_componente (cd_manutencao,"+
						"cd_componente,"+
						"qt_componente) VALUES (?, ?, ?)");
				pstmt.setInt(1, cdManutencao);
				pstmt.setInt(2, cdComponente);
				if(qtComponente==0)
					pstmt.setNull(3, Types.INTEGER);
				else
					pstmt.setInt(3,qtComponente);
				pstmt.executeUpdate();
			}

			connect.commit();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.insertManutencaoComponenteVeiculo: " +  e);
			Conexao.rollback(connect);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}



	public static int realizarManutencao(int cdManutencao, GregorianCalendar dtAtendimento, int qtHodometroAtual,
			String txtRelatorioTecnico, String txtAvaliacao, float vlTotal){
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bpm_manutencao_bem SET dt_manutencao=?,"+
					"qt_hodometro_atual=?,"+
					"st_manutencao=?,"+
					"dt_atendimento=?,"+
					"txt_relatorio_tecnico=?,"+
					"txt_avaliacao=?,"+
					"vl_total=? WHERE cd_manutencao=?");
			pstmt.setTimestamp(1,new Timestamp((new GregorianCalendar()).getTimeInMillis()));
			if(qtHodometroAtual==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,qtHodometroAtual);
			pstmt.setInt(3,1);
			if(dtAtendimento==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(dtAtendimento.getTimeInMillis()));
			pstmt.setString(5,txtRelatorioTecnico);
			pstmt.setString(6,txtAvaliacao);
			if(vlTotal==0)
				pstmt.setNull(7, Types.FLOAT);
			else
				pstmt.setFloat(7,vlTotal);
			pstmt.setInt(8,cdManutencao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.realizarManutencao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}


	public static ResultSetMap findManutencaoAgendada(int cdVeiculo) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_REFERENCIA", String.valueOf(cdVeiculo), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		criterios.add(new ItemComparator("A.ST_MANUTENCAO", "0", ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return findManutencao(criterios);
	}

	public static ResultSetMap findManutencaoRealizada(int cdVeiculo) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_REFERENCIA", String.valueOf(cdVeiculo), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		criterios.add(new ItemComparator("A.ST_MANUTENCAO", "0", ItemComparator.DIFFERENT, java.sql.Types.INTEGER));
		return findManutencao(criterios);
	}

	public static ResultSetMap getManutencao(int cdManutencao) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_MANUTENCAO", String.valueOf(cdManutencao), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return findManutencao(criterios);
	}
	public static ResultSetMap findManutencao(ArrayList<ItemComparator> criterios) {
		return findManutencao(criterios, null);
	}

	public static ResultSetMap findManutencao(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.qt_componente, C.*, D.cd_pessoa, D.nm_pessoa " +
				" FROM bpm_manutencao_bem A" +
				" LEFT OUTER JOIN bpm_manutencao_componente B ON (A.cd_manutencao = B.cd_manutencao) "+
				" LEFT OUTER JOIN bpm_componente_referencia C ON (B.cd_componente = C.cd_componente)"+
				" LEFT OUTER JOIN grl_pessoa D ON (A.cd_pessoa_manutencao = D.cd_pessoa) ", criterios, connect!=null ? connect : Conexao.conectar());
	}

	public static ResultSetMap findAbastecimentos(int cdVeiculo) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("CD_VEICULO", String.valueOf(cdVeiculo), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return findAbastecimentos(criterios, null);
	}

	public static ResultSetMap findAbastecimentos(ArrayList<ItemComparator> criterios) {
		return findAbastecimentos(criterios, null);
	}

	public static ResultSetMap findAbastecimentos(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM fta_abastecimento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static Result addVeiculoFrota(int cdVeiculo, int cdFrota){
		return addVeiculoFrota(cdVeiculo, cdFrota, null);
	}

	public static Result addVeiculoFrota(int cdVeiculo, int cdFrota, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 1;
			// verifica se este veículo já está cadastra a este frota
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_veiculo, cd_frota FROM fta_veiculo "+
					"WHERE cd_veiculo = " + cdVeiculo +
					"  AND cd_frota   = " + cdFrota);			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				ResultSetMap rsmVeiculoFrota = FrotaServices.get(cdFrota);
				String nmFrota = "";
				if (rsmVeiculoFrota.next())
					nmFrota = rsmVeiculoFrota.getString("nm_frota");
				if (isConnectionNull)
					Conexao.rollback(connect);

				return new Result(-1, "Este veículo já se encontra cadastrado para para a " + nmFrota + ".");
			}
			//
			Veiculo veiculo = VeiculoDAO.get(cdVeiculo, connect);
			veiculo.setCdFrota(cdFrota);
			//			veiculo.setCdVeiculo( veiculo.getCdReferencia() );
			if(retorno == 1)
				retorno = VeiculoDAO.update(veiculo, connect);

			connect.commit();
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VEICULO", veiculo);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.addVeiculoFrota: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result delVeiculoFrota(int cdVeiculo, int cdFrota){
		return delVeiculoFrota(cdVeiculo, cdFrota, null);
	}

	public static Result delVeiculoFrota(int cdVeiculo, int cdFrota, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 1;
			Veiculo veiculo = VeiculoDAO.get(cdVeiculo, connect);
			//atualizarão a tabela setando NULL no lugar do cd_frota
			veiculo.setCdFrota(0);

			if(retorno == 1)
				retorno = VeiculoDAO.update(veiculo, connect);

			connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Este veículo foi removido desta frota...", "VEICULO", veiculo);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.delVeiculoFrota: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findProprietarios(ArrayList<ItemComparator> criterios) {
		return findProprietarios(criterios, null);
	}

	public static ResultSetMap findProprietarios(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
			connect = (connect == null ? Conexao.conectar() : connect);

			String orderBy = " ORDER BY ";
			String groupBy = " GROUP BY ";			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy += criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("GROUPBY")) {
					groupBy += criterios.get(i).getValue().toString().trim();					
					criterios.remove(i);
					i--;
				}
			}

			return Search.find("SELECT A.cd_pessoa, A.nm_pessoa,C.cd_concessao, E.nm_pessoa FROM grl_pessoa A " +
					"           JOIN fta_veiculo            B ON (B.cd_proprietario = A.cd_pessoa) "+
					"LEFT OUTER JOIN mob_concessao_veiculo  C ON (B.cd_veiculo = C.cd_veiculo) "+
					"LEFT OUTER JOIN mob_concessao          D ON (D.cd_concessao = C.cd_concessao) "+
					"LEFT OUTER JOIN grl_pessoa             E ON (E.cd_pessoa = D.cd_concessionario) ",
					groupBy + orderBy, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.findProprietarios: " +  e);
			Conexao.rollback(connect);
			return null;
		}
	}

	public static void setValueToFieldEmVeiculo(ResultSetMap rsm, ArrayList<ItemComparator> criterios) {
		boolean verAnoFabrica = false;
		boolean verCRLVEmDia  = false;

		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("verAnoFabrica")) {
				verAnoFabrica = true;
				criterios.remove(i);
				i--;
			}else if (criterios.get(i).getColumn().equalsIgnoreCase("verCRLVEmDia")) {
				verCRLVEmDia = true;
				criterios.remove(i);
				i--;
			}
		}

		while(rsm.next()){
			int qtAnosCRLV = 0;
			int nrFinalPlaca = 0;

			GregorianCalendar dataAtual = Util.getDataAtual();
			GregorianCalendar dataComparacao;

			if(verAnoFabrica){			
				if(rsm.getString("nr_ano_fabricacao") != null && Integer.valueOf(rsm.getString("nr_ano_fabricacao").trim()) > 0)
					rsm.setValueToField("QT_IDADE_VEICULO", (Integer.valueOf(Util.formatDate(dataAtual, "yyyy")) - Integer.valueOf(rsm.getString("nr_ano_fabricacao").trim())));
				else{
					rsm.setValueToField("QT_IDADE_VEICULO", 0);
					rsm.setValueToField("ST_ERRO_SEM_DT_FABRICACAO", -100);
					rsm.setValueToField("DS_ERRO_SEM_DT_FABRICACAO_MSG", "Verifique se a data de Fabricação está correta e atualize!");
				}
			}
			if(verCRLVEmDia){				
				qtAnosCRLV = Integer.valueOf(Util.formatDate(dataAtual, "yyyy")) - Integer.parseInt((rsm.getString("nr_ano_licenciamento")==null || rsm.getString("nr_ano_licenciamento").equals("") ? 
																									"0" : rsm.getString("nr_ano_licenciamento").trim()));

				if(rsm.getString("nr_ano_licenciamento") != null){
					// se a diferença for maior que um ano, então está irregular
					if (qtAnosCRLV > 1){
						setMsgErroCRLV(rsm, "O CRLV do veículo está vencido há " + qtAnosCRLV + " anos");

					}//Faz a verificação das datas de acordo com o final da placa do veiculo
					else if (qtAnosCRLV == 1){
						//TODO validar placa
						nrFinalPlaca = Integer.valueOf(rsm.getString("nr_placa").substring(6, 7));

						switch (nrFinalPlaca) {
						case 1:		
							dataComparacao = Util.stringToCalendar("29/04/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						case 2:
							dataComparacao = Util.stringToCalendar("31/05/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						case 3:
							dataComparacao = Util.stringToCalendar("29/06/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						case 4:
							dataComparacao = Util.stringToCalendar("30/06/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						case 5:
							dataComparacao = Util.stringToCalendar("29/07/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						case 6:
							dataComparacao = Util.stringToCalendar("31/08/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						case 7:
							dataComparacao = Util.stringToCalendar("29/09/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						case 8:
							dataComparacao = Util.stringToCalendar("30/09/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						case 9:
							dataComparacao = Util.stringToCalendar("31/10/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						case 0:
							dataComparacao = Util.stringToCalendar("30/11/"+Util.formatDate(dataAtual, "yyyy"));
							if(Util.compareDates(dataAtual, dataComparacao) > 0){
								setMsgErroCRLV(rsm,"O CRLV do veículo venceu aproximadamente no dia " + Util.formatDateTime(dataComparacao, "dd/MM"));
							}
							break;

						}
					}
				}else{
					setMsgErroCRLV(rsm, "O CRLV do veículo não está registrado no cadastro. Atualize!");
				}
			}
		}
	}

	public static void setMsgErroCRLV(ResultSetMap rsm, String msg) {
		rsm.setValueToField("ST_ERRO_CRLV_IRREGULAR", -100);
		rsm.setValueToField("DS_ERRO_CRLV_IRREGULAR_MSG", msg);
	}

	public static ResultSetMap findAllCidadesCRLV(){
		return findAllCidadesCRLV(null, null);
	}

	public static ResultSetMap findAllCidadesCRLV(ArrayList<ItemComparator> criterios, Connection connection){
		try {
			connection = (connection == null ? Conexao.conectar() : connection);


			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_cidade, B.nm_cidade, C.sg_estado FROM fta_veiculo A "+
																  "LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) "+
																  "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado) "+
																  "GROUP BY A.cd_cidade, B.nm_cidade, C.sg_estado ");			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			while (rsm.next())
				rsm.setValueToField("DS_CIDADE_SG_UF", (rsm.getString("nm_cidade")== null ? "CIDADE NÃO INFORMADA." : rsm.getString("nm_cidade") + "-" + rsm.getString("sg_estado")));
			rsm.beforeFirst();

			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.findAllCidadesCRLV: " +  e);
			Conexao.rollback(connection);
			return null;
		}

	}

	public static HashMap<String, Object> getVeiculoDetran(String placa) {
		try {
			String nrPlaca 	= placa.replaceAll("-", "");
			String urlCons	= "http://54.207.43.29:8080/etransito/detrancon?p="+nrPlaca+"&u=tivic&s=t1v1k!";
			URL url = new URL(urlCons);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 
			
			return toMap(new JSONObject(buffer.toString()));
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	private static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
	    HashMap<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}
	
	public static List<Object> toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}

}