package com.tivic.manager.sinc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

public class LoteServices {

	public static final int TP_IDA 	 = 0;
	public static final int TP_VOLTA = 1;
	public static final int TP_DUPLA = 2;
	
	public static Result save(Lote lote){
		return save(lote, null);
	}

	public static Result save(Lote lote, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(lote==null)
				return new Result(-1, "Erro ao salvar. Lote é nulo");

			int retorno;
			if(lote.getCdLote()==0){
				retorno = LoteDAO.insert(lote, connect);
				lote.setCdLote(retorno);
			}
			else {
				retorno = LoteDAO.update(lote, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOTE", lote);
		}
		catch(Exception e){
			Util.registerLog(e);
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
	public static Result remove(int cdLote){
		return remove(cdLote, false, null);
	}
	public static Result remove(int cdLote, boolean cascade){
		return remove(cdLote, cascade, null);
	}
	public static Result remove(int cdLote, boolean cascade, Connection connect){
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
			retorno = LoteDAO.delete(cdLote, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			Util.registerLog(e);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_lote");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			Util.registerLog(sqlExpt);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM sinc_lote", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Metodo que recebe um objeto JSON e faz os critérios de um SELECT com os nomes dos campos e seus valores
	 * @param campos Campos que seráo passados para incluir nos critérios
	 * @return Retorna uma String no formato de critérios de um SELECT
	 * @throws JSONException Caso ocorra exceção por JSON mal formada, o erro será jogado para o método chamador
	 */
	public static String getCriterios(JSONObject campos) throws JSONException{
		if(campos == null)
			return null;
		//Array que guarará as chaves vindas dos campos
		ArrayList<String> chaves  = new ArrayList<String>();
		//Array que guardará os valores das chaves vindas dos campos
		ArrayList<Object> valores = new ArrayList<Object>();
		//Iterador sobre o JSON
		Iterator<?> keys = campos.keys();
		while(keys.hasNext()){
			//Adiciona a chave e o valor nos arrays
			String chave = (String)keys.next();
			chaves.add(chave);
			//Trata caso seja um campo inteiro ou um campo String
			valores.add((campos.get(chave) instanceof Integer ? (Integer)campos.get(chave) : campos.get(chave).toString()));
		}
		//Monta o critério com as chaves e valores
		String criteriosWhere = "";
		for(int i = 0; i < chaves.size(); i++){
			if(i == 0){
				criteriosWhere += chaves.get(i) + " = '" + valores.get(i) + "'";
			}
			else{
				criteriosWhere += " AND " + chaves.get(i) + " = '" + valores.get(i) + "'";
			}
		}
		//Retorna a String em forma de criterios WHERE
		return criteriosWhere;
	}
	
	/**
	 * Metodo principal, fará toda a rotina de sincronização LOCAL para SERVIDOR, e SERVIDOR para LOCAL, sendo chamado pelo LOCAL apenas
	 * @param cdLocalServidor Código do servidor cadastrado no LOCAL
	 * @return
	 */
	public static Result chamadaSincronizacao(int cdLocalServidor){
		return chamadaSincronizacao(cdLocalServidor, TP_DUPLA);
	}
	public static Result chamadaSincronizacao(int cdLocalServidor, int tpSentido){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			//Busca o SERVIDOR para esse LOCAL
			
			Local localServidor = LocalDAO.get(cdLocalServidor, connectLocal);
			
			
			//Abre a conexão com o SERVIDOR
			Connection connectServer = Conexao.conectar(localServidor.getNmUrlDatabase(), localServidor.getNmLoginDatabase(), localServidor.getNmSenhaDatabase());
			
			try{
				
				connectServer.setAutoCommit(false);
				
				//BUSCAR NO SERVER QUAL O LOCAL QUE TEM O MESMO ID DO LOCAL
				PreparedStatement pstmtBuscaIdLocal = connectServer.prepareStatement("SELECT * FROM sinc_local WHERE id_local = '" +ParametroServices.getValorOfParametro("NR_ID_CLIENTE", connectLocal)+ "'");
				ResultSetMap rsmBuscaIdLocal = new ResultSetMap(pstmtBuscaIdLocal.executeQuery());
				int cdLocal = 0;
				if(rsmBuscaIdLocal.next())
					cdLocal = rsmBuscaIdLocal.getInt("cd_local");
				//Array que guarda os Lotes de Registro já analisados no LOCAL para SERVIDOR para que eles não sejam passados novamente na volta (SERVIDOR para LOCAL)
				//(já que no Servidor irá criar novos lote registro durante a sincronização)
				ArrayList<Integer> cdCodigoLoteRegistroJaAnalisados = new ArrayList<Integer>();
				System.out.println("---------------------------------------------------------------------Antes da primeira---------------------------------------------------------------------");
				//Realiza a primeira sincronização, do LOCAL para o SERVIDOR
				Result resultado = sincronizacao(tpSentido, connectLocal, connectServer, false, cdCodigoLoteRegistroJaAnalisados, cdLocal, new ArrayList<LoteRegistro>());
				//Busca os registros que precisarão ser colocados de novo no LOCAL, pois não poderiam ser deletados e foram
				ArrayList<LoteRegistro> registroReparador = (ArrayList<LoteRegistro>) resultado.getObjects().get("registroReparador");
				System.out.println("---------------------------------------------------------------------Antes da segunda---------------------------------------------------------------------");
				//Faz a segunda sincronização, do SERVIDOR para o LOCAL
				sincronizacao(tpSentido, connectServer, connectLocal, true, cdCodigoLoteRegistroJaAnalisados, cdLocal, registroReparador);
				System.out.println("---------------------------------------------------------------------Depois da segunda---------------------------------------------------------------------");
				
				//Criar chamada para fechar em lotes os registros
				//Cliente
				Lote loteLocal  = new Lote(0, Util.getDataAtual());
				save(loteLocal, connectLocal);
				connectLocal.prepareStatement("UPDATE sinc_lote_registro SET cd_lote = " + loteLocal.getCdLote() + " WHERE cd_lote IS NULL").executeUpdate();
//				Servidor
				Lote loteServer = new Lote(0, Util.getDataAtual());
				save(loteServer, connectServer);
				connectServer.prepareStatement("UPDATE sinc_lote_registro SET cd_lote = " + loteServer.getCdLote() + " WHERE cd_lote IS NULL").executeUpdate();
				
				//Da commit em ambos caso a sincronização tenha sido feita sem erros
				connectLocal.commit();
				connectServer.commit();
			
				return new Result(1, "Sincronização realizada");
			}
			catch(Exception e){
				Conexao.rollback(connectLocal);
				Conexao.rollback(connectServer);
				//registra log de erro quando a classe é utilizad pelo pdv
				Util.registerLog(e);
				e.printStackTrace();
				return new Result(-1, "Erro na sincronização");
			}
			finally{
				Conexao.desconectar(connectServer);
			}
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronização");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}
	
	/**
	 * Metodo que sincronização dois bancos de dados, fazendo a analise de INSERT, UPDATE e DELETE dos Lotes de Registro do primeiro banco que não tiverem Lotes associados
	 * @param connectLocal Conexão do banco que irá passar as informações para adiante
	 * @param connectServer Conexão do banco que irá receber as informações passadas
	 * @param isServer Booleano que indica se a rodada é do Servidor ou não
	 * @param cdCodigoLoteRegistroJaAnalisados Array que guarda os código já analisados, para que na volta (SERVIDOR para LOCAL) eles não sejam analisados novamente (já que no Servidor irá criar novos lote registro durante a sincronização)
	 * @param registrosRecentesLocal Guarda os registros que seráo analisados pelo LOCAL, para que na vez do Servidor eles sejam analisados nos UPDATE (caso fosse feita uma busca comum na hora, esses registros já teriam Lote associados, tornando-se indissociaveis dos registros antigos)
	 * @param cdLocal Código do LOCAL (no Servidor) que está fazendo a sincronização com o SERVIDOR
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static Result sincronizacao(int tpSentido, Connection connectLocal, Connection connectServer, boolean isServer, ArrayList<Integer> cdCodigoLoteRegistroJaAnalisados, int cdLocal, ArrayList<LoteRegistro> registroReparador) throws JSONException, SQLException{
		
		//Busca todo o LOTE_REGISTRO do LOCAL que não esteja vinculado a nenhum LOTE
		//Ou busca todo Lote Registro do Servidor que não esteja ligado a esse Local
		ResultSetMap rsmLocalLoteRegistro = new ResultSetMap();
		
		String dtInicioSincronizacao = ParametroServices.getValorOfParametro("DT_INICIO_SINCRONIZACAO", connectServer);
		
		
		if(isServer){
			
			PreparedStatement pstmt = connectLocal.prepareStatement("SELECT * FROM sinc_lote_registro A "
					+ "															   WHERE NOT EXISTS (SELECT * FROM sinc_lote_registro_local B "
					+ "																						WHERE A.cd_lote_registro = B.cd_lote_registro "
					+ "																						  AND B.cd_local="+cdLocal+") "
					+ "    														     AND A.dt_atualizacao > '" + dtInicioSincronizacao + "'"
					+ "															   ORDER BY cd_lote_registro");
			rsmLocalLoteRegistro = new ResultSetMap(pstmt.executeQuery());
		}
		else{
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_lote", "", ItemComparator.ISNULL, Types.INTEGER));
			
			rsmLocalLoteRegistro = LoteRegistroServices.find(criterios, connectLocal);
		}
		
		for(int i = 0;i < registroReparador.size();){
			
			HashMap<String, Object> registro = LoteRegistroServices.getRegister(registroReparador.get(i));
			
			manipularRegistro(registro, connectLocal, connectServer, isServer, tpSentido, cdLocal, dtInicioSincronizacao, cdCodigoLoteRegistroJaAnalisados, registroReparador);
			
			registroReparador.remove(i);
		}
		
		//Itera sobre os lote registro encontrados
		while(rsmLocalLoteRegistro.next()){
			manipularRegistro(rsmLocalLoteRegistro.getRegister(), connectLocal, connectServer, isServer, tpSentido, cdLocal, dtInicioSincronizacao, cdCodigoLoteRegistroJaAnalisados, registroReparador);
		}
		
		Result resultado = new Result(1);
		resultado.addObject("registroReparador", registroReparador);
		return resultado;
			
	}						

	
	public static void manipularRegistro(HashMap<String, Object> registerLocal, Connection connectLocal, Connection connectServer, Boolean isServer, int tpSentido, int cdLocal, String dtInicioSincronizacao, ArrayList<Integer> cdCodigoLoteRegistroJaAnalisados, ArrayList<LoteRegistro> registroReparador) throws SQLException, JSONException{
		//Registro de Lote Registro que está sendo iterado
		LoteRegistro registro = LoteRegistroDAO.get(Integer.parseInt(String.valueOf(registerLocal.get("CD_LOTE_REGISTRO"))), connectLocal);
		
		//Tabela que está sendo alterada
		Tabela tabela = TabelaDAO.get(registro.getCdTabela(), connectLocal);
		
		//Verificação se a tabela esta ativa para fazer sincronização
		if(tabela.getStSincronizacao() == TabelaServices.ST_DESATIVADO){
			return;
		}
		
		//Verificação do sentido de sincronização da tabela
		if(!isServer){
			if(tpSentido == TP_DUPLA)
				if(tabela.getTpSincronizacao() == TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL)
					return;
			if(tpSentido == TP_VOLTA)
				return;
		}
		else{
			if(tpSentido == TP_DUPLA)
				if(tabela.getTpSincronizacao() == TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR)
					return;
			if(tpSentido == TP_IDA)
				return;
		}
		
		//Variavel que guarda as chaves primarias do Local
		JSONObject regChaveLocal = new JSONObject(registro.getNmValorChaves());
		//Verifica se o lote registro é de atualização, caso seja, verifica se houve alteração na chave primaria
		//Colocando o campo novo no registro da chave (pois no UPDATE o campo a ficar em valor_chaves, é o antigo)
		if(registro.getTpAtualizacao() == LoteRegistroServices.TP_UPDATE){
			JSONObject regCamposAlterados = new JSONObject(registro.getNmCampoAlterado());
			if(regCamposAlterados != null && regCamposAlterados.length() > 0){
				Iterator<?> keysCamposAlterados = regCamposAlterados.keys();
				while(keysCamposAlterados.hasNext()){
					String chaveCamposAlterados = (String)keysCamposAlterados.next();
					if(regChaveLocal.has(chaveCamposAlterados)){
						regChaveLocal.put(chaveCamposAlterados, regCamposAlterados.get(chaveCamposAlterados).toString().split("\\|")[1]);
					}
				}
			}
		}
		//Variavel que guarda as chaves estrangeiras do Local
		JSONObject regChaveEstrangeiraLocal = getChaveEstrangeira(tabela, connectLocal);
		//Variavel que guarda as chaves primarias referentes do Servidor
		JSONObject regChaveServidor = getValorChaveServidor(tabela, regChaveLocal, connectLocal, connectServer, isServer, cdLocal, registro.getCdLoteRegistro());
		//Se o registro for um UPDATE ou DELETE, e a chave do Servidor não for encontrada (significando que houve um DELETE da chave no Servidor), então o lote_registro
		//É dado como analisado, sem ser feito nada
		if(registro.getTpAtualizacao() == LoteRegistroServices.TP_UPDATE || registro.getTpAtualizacao() == LoteRegistroServices.TP_DELETE){
			if(regChaveServidor == null){
				return;
			}
	    }
		
		//Variavel que guarda as chaves por meio de criterios WHERE
		String chavesServer = getCriterios(regChaveServidor);
		
		//Variavel que guardará a chave que corresponde ao lote registro atual (caso seja um INSERT)
		JSONObject regChaveReferencia = new JSONObject();
		
		//Verifica se o registro foi deletado do Local
		ResultSetMap rsmRegistroNoLocalDeletado = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM sinc_lote_registro A WHERE A.tp_atualizacao = " + LoteRegistroServices.TP_DELETE
																							   + " AND A.cd_tabela = " + registro.getCdTabela() +
				 (isServer ?  "															   AND NOT EXISTS (SELECT * FROM sinc_lote_registro_local B "
							+ "																						WHERE A.cd_lote_registro = B.cd_lote_registro "
							+ "																						  AND B.cd_local="+cdLocal+") "
							+ "    														     AND A.dt_atualizacao > '" + dtInicioSincronizacao + "'" : " AND A.cd_lote IS NULL")).executeQuery());
		boolean registroDeletado = false;
		//Busca o registro dos campos da tabela que está sendo analisada
		JSONObject registroTabelaLocal = getRegistroTabela(tabela, regChaveLocal, connectLocal);
		//Caso o registro tenha sido deletado, o registro da tabela será apenas as chaves (pois o registro logo será deletado, assim os lote registro terão com o que trabalhar, até o registro ser deletado)
		while(rsmRegistroNoLocalDeletado.next()){
			JSONObject regRegistroDeletado = new JSONObject(rsmRegistroNoLocalDeletado.getString("nm_valor_chaves"));
			Iterator<?> chaves = regChaveLocal.keys();
			boolean valorIgual = true;
			while(chaves.hasNext()){
				String chave = (String) chaves.next();
				Iterator<?> chavesDeletado = regRegistroDeletado.keys();
				
				boolean chaveAchada = false;
				while(chavesDeletado.hasNext()){
					String chaveDeletado = (String) chavesDeletado.next();
					if(chave.equals(chaveDeletado)){
						chaveAchada = true;
						if(!regChaveLocal.get(chave).toString().equals(regRegistroDeletado.get(chaveDeletado).toString())){
							registroDeletado = false;
							valorIgual = false;
						}
						break;
					}
					
				}
				
				if(!chaveAchada)
					valorIgual = false;
				
				if(!valorIgual)
					break;
				
			}
			
			if(valorIgual){
				registroDeletado = true;
				break;
			}
			
		}
		
		//Caso o registro já tenha sido deletado pelo próprio chamador, ele não será trabalhado
		if(registroDeletado)
			return;
		
		//Verifica se a tabela tem campos que não podem ser nulos
		JSONObject regCamposNaoNulos = new JSONObject(tabela.getNmCampoNaoNulo());
		
		//Converte o registro encontrado para um HashMap
		HashMap<String, Object> regRegistroLocal = Util.convertJSONtoHashMap(registroTabelaLocal);
		//Verifica se no banco de dados do Servidor tem um registro com o mesmo CAMPO UNICO, ou com a mesma chave (não havendo na chave um campo gerador)TODO: Verificar em relação ao CD_ITEM que é 1
		JSONObject regChaveServerCampoUnico = null;
		if(!registroDeletado)
			regChaveServerCampoUnico = hasCampoUnicoOuMesmaChave(registerLocal, tabela, regChaveLocal, regRegistroLocal, regChaveEstrangeiraLocal, connectServer, connectLocal, isServer, cdLocal, registro, regCamposNaoNulos);
		
		//Verifica se houve alguma alteração no registro
		boolean registroAlterado = false;
		
		//Entra no bloco se for originalmente LOCAL
		//Ou se for um SERVIDOR, se o Lote Registro não tiver sido inserido durante a rotina de sincronização (não precisando obviamente de analise no Local, já que foi o proprio que gerou o lote registro)
		if(!isServer || !cdCodigoLoteRegistroJaAnalisados.contains(registro.getCdLoteRegistro())){
			
			switch(registro.getTpAtualizacao()){
				case LoteRegistroServices.TP_INSERT:
					//Faz a inserção do registro no banco de dados do Servidor
					Result resultadoInserir = insertRegistro(tabela, regChaveLocal, regChaveEstrangeiraLocal, regChaveReferencia, registro, registroTabelaLocal, regRegistroLocal, regChaveServerCampoUnico, connectServer, connectLocal, isServer, cdLocal, cdCodigoLoteRegistroJaAnalisados);
					//Busca a chave primária que foi colocada no Servidor
					regChaveReferencia = (JSONObject) resultadoInserir.getObjects().get("regChaveReferencia");
					//Verifica se houve realmente uma alteração no banco de dados do Servidor
					registroAlterado = (Boolean) resultadoInserir.getObjects().get("registroInserido");
				break;
				case LoteRegistroServices.TP_UPDATE:
					//Busca a chave primária desse registro no Banco do Servidor
					regChaveServidor = getValorChaveServidor(tabela, regChaveLocal, connectLocal, connectServer, isServer, cdLocal, registro.getCdLoteRegistro());
					//Atualiza o registro
					Result resultadoAtualizar = updateRegistro(tabela, regChaveLocal, regChaveServidor, regChaveEstrangeiraLocal, regRegistroLocal, registro, connectLocal, connectServer, isServer, cdLocal, cdCodigoLoteRegistroJaAnalisados);
					//Verifica se houve realmente uma alteração no banco de dados do Servidor
					registroAlterado = (Boolean) resultadoAtualizar.getObjects().get("registroAtualizado");
				break;
					
				case LoteRegistroServices.TP_DELETE:
					//Busca a chave primária desse registro no Banco do Servidor
					regChaveServidor = getValorChaveServidor(tabela, regChaveLocal, connectLocal, connectServer, isServer, cdLocal, registro.getCdLoteRegistro());
					//Transforma a chave do servidor para coloca-la em forma de criterio
					chavesServer = getCriterios(regChaveServidor);
					//Usa um save point para caso haja um problema na hora de tentar deletar o registro, a conexão não seja perdida, apenas restaurada
					Savepoint savePoint = connectServer.setSavepoint("1");
					
					int ret = 1;
					try{
						//Tenta realizar a deleção apenas se a chave foi buscada corretamente
						if(chavesServer != null && !chavesServer.equals("") && !chavesServer.equals("null")){
							System.out.println(registro.getCdLoteRegistro() + " - DELETE FROM " + tabela.getNmTabela() + " WHERE " + chavesServer);
							ret = connectServer.prepareStatement("DELETE FROM " + tabela.getNmTabela() + " WHERE " + chavesServer).executeUpdate();
						}	
					}
					catch(Exception e){
						Util.registerLog(e);
						connectServer.rollback(savePoint);
						e.printStackTrace();
						ret = -1;
					}
					//Caso não tenha sido feito o DELETE no Servidor, significando que esse registro não pode ser deletado, será feita uma inserção no Local
					if(ret < 0){
						ResultSetMap rsmRegistroNoServer = new ResultSetMap(connectServer.prepareStatement("SELECT * FROM sinc_lote_registro WHERE tp_atualizacao = " + LoteRegistroServices.TP_DELETE + " AND nm_valor_chaves = '" + regChaveServidor.toString() + "'" ).executeQuery());
						if(!rsmRegistroNoServer.next()){
							registroReparador.add(new LoteRegistro(0, 0, tabela.getCdTabela(), regChaveServidor.toString(), ParametroServices.getValorOfParametro("NR_ID_CLIENTE", 0), LoteRegistroServices.TP_INSERT, Util.getDataAtual(), null, null));
						}
						
					}
					else{
						registroAlterado = true;
					}
					
				break;
			}
		}
		
		//Busca-se o ultimo lote registro e o ultimo lote do Servidor
		LoteRegistro ultimoLoteRegistro = null;
		ResultSetMap rsmUltimoLoteRegistro = new ResultSetMap(connectServer.prepareStatement("SELECT MAX(cd_lote_registro) AS cd_lote_registro FROM sinc_lote_registro").executeQuery());
		if(rsmUltimoLoteRegistro.next())
			ultimoLoteRegistro = LoteRegistroDAO.get(rsmUltimoLoteRegistro.getInt("cd_lote_registro"), connectServer);
		
		//Caso seja um SERVIDOR
		if(isServer){
			
			//Fazer um dos Result devolver o valor das chaves no LOCAL
			String nmChaveLocal = (regChaveReferencia == null || regChaveReferencia.toString().equals("{}") ? (regChaveServidor == null ? "{}" : regChaveServidor.toString()) : regChaveReferencia.toString());
			
			//O Servidor controla as referencias aos locais a partir da tabela LoteRegistroLocal, então é acrescentado esse registro aqui
			LoteRegistroLocal loteRegistroLocal = new LoteRegistroLocal(0, cdLocal, Util.getDataAtual(), nmChaveLocal);
			loteRegistroLocal.setCdLoteRegistro(registro.getCdLoteRegistro());
			LoteRegistroDAO.update(registro, connectLocal);
			LoteRegistroLocalDAO.insert(loteRegistroLocal, connectLocal);
			
			//Caso tenha sido uma inserção e não tenha sido do array de codigos ja analisados do Local, irá ser acrescentada a chave de referencia no ultimo lote registro
			if(registro.getTpAtualizacao() == LoteRegistroServices.TP_INSERT && !cdCodigoLoteRegistroJaAnalisados.contains(registro.getCdLoteRegistro())){
				if(ultimoLoteRegistro != null){
					ultimoLoteRegistro.setNmChaveReferencia(registro.getNmValorChaves());
					LoteRegistroDAO.update(ultimoLoteRegistro, connectServer);
				}
			}
			
		}
		//Caso seja um LOCAL
		else{
			//E o registro tiver sido alterado (ter acrescentado algo no SERVIDOR), o registro acrescentado irá para o array, não sendo analisado na volta (SERVIDOR para LOCAL)
			if(registroAlterado && ultimoLoteRegistro != null){
				cdCodigoLoteRegistroJaAnalisados.add(ultimoLoteRegistro.getCdLoteRegistro());
			}
			
		}
	}
	
	
	
	/**
	 * Metodo que irá inserir um Registro vindo do Local para o Servidor
	 * @param tabela Tabela que o registro irá inserir
	 * @param regChaveLocal Chave primária do registro no Local
	 * @param regChaveEstrangeiraLocal Chaves estrangeiras do Local
	 * @param regChaveReferencia JSON que irá guardar a chave que irá ser inserida
	 * @param registro Registro de LoteRegistro que está sendo analisado
	 * @param registroTabela Campos da tabela que foi inserida no Local, e será inserido no Servidor
	 * @param regRegistroLocal Contem os registros dos campos que estáo sendo inseridos
	 * @param regChaveServerCampoUnico Verifica se no registro foi identificado campo unico ou campo de mesma chave
	 * @param connectServer Conexão para o banco que irá receber o registro
	 * @param connectLocal Conexão para o banco que está passando o registro
	 * @param isServer Verifica se o primeiro banco é servidor ou não
	 * @param cdLocal Passa o código do LOCAL que está gravado no SERVIDOR
	 * @param registrosRecentesLocal Passa os codigos dos registros que seráo analisados nessa sincronização pelo LOCAL (usado para quando a inserção virar uma atualização)
	 * @param cdCodigoLoteRegistroJaAnalisados Passa os códigos que não precisarão ser analisados pelo SERVIDOR (pois são originais do LOCAL)
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static Result insertRegistro(Tabela tabela, JSONObject regChaveLocal, JSONObject regChaveEstrangeiraLocal, JSONObject regChaveReferencia, LoteRegistro registro, JSONObject registroTabela, HashMap<String, Object> regRegistroLocal, JSONObject regChaveServerCampoUnico, Connection connectServer, Connection connectLocal, boolean isServer, int cdLocal, ArrayList<Integer> cdCodigoLoteRegistroJaAnalisados) throws JSONException, SQLException{
		//Verifica se o registro foi inserido ou não
		boolean registroInserido = false;
	
		//Verifica se a tabela tem campos que não podem ser nulos
		JSONObject regCamposNaoNulos = new JSONObject(tabela.getNmCampoNaoNulo());
		
		//Busca o valor das chaves de acordo a tabela
		JSONObject regCamposChaveTabela = new JSONObject(tabela.getNmCampoChave());
		
		//Verifica se foi encontrado campo unico ou mesma chave (sem campo gerador) no Servidor
		if(regChaveServerCampoUnico == null){
			String campos  = "";
			String valores = "";
			
			Iterator<?> chaves = registroTabela.keys();
			int i = 0;
			//Itera sobre os campos do Local para que sejam inseridos no Servidor
			while(chaves.hasNext()){
				String chave = (String) chaves.next();
				//Caso seja um campo gerador, e seja parte da chave primária (não fazendo parte de uma chave estrangeira)
				if(regCamposChaveTabela.has(chave) && !regChaveEstrangeiraLocal.has(chave)){
					Iterator<?> chavePrimaria = regCamposChaveTabela.keys();
					HashMap<String,Object>[] keys = new HashMap[regCamposChaveTabela.length()];
					int countKeysValidos = 0;
					for(int j = 0; chavePrimaria.hasNext(); j++){
						String chavePK = (String)chavePrimaria.next();
						String valuePK = regCamposChaveTabela.get(chavePK).toString();
						if(valuePK.equals("0") || valuePK.equals("1")){
							countKeysValidos++;
							keys[j] = new HashMap<String,Object>();
							keys[j].put("FIELD_NAME", chavePK.toLowerCase());
							keys[j].put("IS_KEY_NATIVE", (valuePK.equals("0") ? "NO" : "YES"));
							//Busca o valor da chave estrangeira para inserir no keys
							if(regChaveEstrangeiraLocal.has(chavePK)){
								//Busca-se as chaves estrangeiras a partir das suas tabelas de origem
								HashMap<Tabela, JSONObject> chavesEstrangeiras = getTabelasChaveEstrangeira(tabela, regRegistroLocal, false, connectLocal);
								for(Tabela tabelaProvedora : chavesEstrangeiras.keySet()){
									JSONObject chavesEstrangeirasPrimaria = chavesEstrangeiras.get(tabelaProvedora);
									//Caso a chave analisada fará parte dessa tabela
									if(chavesEstrangeirasPrimaria.has(chavePK)){
										//Busca-se os nomes originais da chave primária
										Result resultado = getChavePrimariaOriginal(chavePK, tabela, chavesEstrangeirasPrimaria, connectLocal);
										chavesEstrangeirasPrimaria = (JSONObject) resultado.getObjects().get("chavesEstrangeirasPrimariaServidor");
										String chaveEstrangeira = (String) resultado.getObjects().get("chave");
										//Verifica-se o valor da chave servidor dessa chave estrangeira, caminhando-se pelos Lote Registro
										JSONObject chavesEstrangeirasPrimariaServidor = getValorChaveServidor(tabelaProvedora, chavesEstrangeirasPrimaria, connectLocal, connectServer, isServer, cdLocal, registro.getCdLoteRegistro());
										keys[j].put("FIELD_VALUE", new Integer(chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString()));
									}
								}
							}
						}
					}
					HashMap<String,Object>[] keysFinal = new HashMap[countKeysValidos];
					for(int j = 0, z = 0; j < keys.length; j++){
						if(keys[j] != null){
							keysFinal[z++] = keys[j];
						}
					}
					//Faz a geração da chave no banco Servidor
					int code = Conexao.getSequenceCodeSync(tabela.getNmTabela(), keysFinal, connectServer);
					if(i==0){
						campos  = chave;
						valores = "'" + code + "'";
					}
					else{
						campos  += ", " + chave;
						valores += ", '" + code + "'";
					}
					//Coloca no registro de referencia o novo codigo
					regChaveReferencia.put(chave, code);
					i++;
				}
				//Caso seja uma chave estrangeira
				else if(regChaveEstrangeiraLocal.has(chave)){
					//Busca-se as chaves estrangeiras a partir das suas tabelas de origem
					HashMap<Tabela, JSONObject> chavesEstrangeiras = getTabelasChaveEstrangeira(tabela, regRegistroLocal, false, connectLocal);
					for(Tabela tabelaProvedora : chavesEstrangeiras.keySet()){
						JSONObject chavesEstrangeirasPrimaria = chavesEstrangeiras.get(tabelaProvedora);
						//Caso a chave analisada fará parte dessa tabela
						if(chavesEstrangeirasPrimaria.has(chave)){
							//Busca-se os nomes originais da chave primária
							Result resultado = getChavePrimariaOriginal(chave, tabela, chavesEstrangeirasPrimaria, connectLocal);
							chavesEstrangeirasPrimaria = (JSONObject) resultado.getObjects().get("chavesEstrangeirasPrimariaServidor");
							String chaveEstrangeira = (String) resultado.getObjects().get("chave");
							//Verifica-se o valor da chave servidor dessa chave estrangeira, caminhando-se pelos Lote Registro
							JSONObject chavesEstrangeirasPrimariaServidor = getValorChaveServidor(tabelaProvedora, chavesEstrangeirasPrimaria, connectLocal, connectServer, isServer, cdLocal, registro.getCdLoteRegistro());
							if(i==0){
								campos  = chave;
								valores = (!chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && !chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("null") ? "'" + chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira) + "'" : (chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && regCamposNaoNulos.has(chaveEstrangeira.toUpperCase()) ? "0" : "NULL"));
							}
							else{
								campos  += ", " + chave;
								valores += "," + (!chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && !chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("null") ? "'" + chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira) + "'" : (chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && regCamposNaoNulos.has(chaveEstrangeira.toUpperCase()) ? "0" : "NULL"));
							}
							//Se fizer também parte da chave primária, é incluida na chave de referencia
							if(regChaveLocal.has(chave))
								regChaveReferencia.put(chave, (!chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && !chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("null") ? chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira) : "NULL"));
							i++;
							break;
						}
					}
					
				}
				//Se for um campo comum, ou uma chave não geradora (a exemplo de cd_item em documento_saida_item), é simplesmente incluida exatamente como veio do registro original
				else{
					if(i==0){
						campos  = chave;
						valores = (registroTabela.get(chave) == null || (registroTabela.get(chave).toString()).equals("null") || (registroTabela.get(chave).toString()).equals("0") ? ((registroTabela.get(chave).toString()).equals("0") && regCamposNaoNulos.has(chave.toUpperCase()) ? "0" : "NULL") : "'" + registroTabela.get(chave) + "'");
					}
					else{
						campos  += ", " + chave;
						valores += ", " + (registroTabela.get(chave) == null || (registroTabela.get(chave).toString()).equals("null") || (registroTabela.get(chave).toString()).equals("0") ? ((registroTabela.get(chave).toString()).equals("0") && regCamposNaoNulos.has(chave.toUpperCase()) ? "0" : "NULL") : "'" + registroTabela.get(chave) + "'");
					}
					i++;
				}
			}
			if(campos != null && !campos.equals("") && valores != null && !valores.equals("")){
				System.out.println(registro.getCdLoteRegistro() + " - INSERT INTO " + tabela.getNmTabela() + "(" + campos+ ") VALUES (" + valores +")");
				registroInserido = true;
				connectServer.prepareStatement("INSERT INTO " + tabela.getNmTabela() + "(" + campos+ ") VALUES (" + valores +")").executeUpdate();
				
				if(!isServer){
					registro.setNmChaveReferencia(regChaveReferencia.toString());
					LoteRegistroDAO.update(registro, connectLocal);
				}
			}
		}
		//Caso haja campo unico ou mesma chave, haverá não uma inserção e sim uma atualização
		else{
			Iterator<?> chaves = registroTabela.keys();
			int i = 0;
			String camposAlterados = "{";
			//Reune os campos que deverão ser mandados para tentar serem alterados
			while(chaves.hasNext()){
				String chave = (String) chaves.next();
				if(!regChaveLocal.has(chave.toUpperCase()) && !regChaveEstrangeiraLocal.has(chave.toUpperCase())){
					if(i==0)
						camposAlterados += "\"" + chave + "\":\" |" + registroTabela.get(chave).toString() + "\"";
					else
						camposAlterados += ", \"" + chave + "\":\" |" + registroTabela.get(chave).toString() + "\"";
					i++;
				}
				if(regChaveEstrangeiraLocal.has(chave.toUpperCase()) && !registroTabela.get(chave.toUpperCase()).toString().equals("0")){
					if(i==0)
						camposAlterados += "\"" + chave + "\":\"" + (regChaveLocal.has(chave) ? registroTabela.get(chave).toString() : " ") +"|" + registroTabela.get(chave).toString() + "\"";
					else
						camposAlterados += ", \"" + chave + "\":\"" + (regChaveLocal.has(chave) ? registroTabela.get(chave).toString() : " ") +"|" + registroTabela.get(chave).toString() + "\"";
					i++;
				}
			}
			camposAlterados += "}";
			//inclui o registro de campo alterado
			registro.setNmCampoAlterado(camposAlterados);
			LoteRegistroDAO.update(registro, connectLocal);
			//Chama o metodo de atualização geral
			updateRegistro(tabela, regChaveLocal, regChaveServerCampoUnico, regChaveEstrangeiraLocal, regRegistroLocal, registro, connectLocal, connectServer, isServer, cdLocal, cdCodigoLoteRegistroJaAnalisados);
		
			//Se for um LOCAL inclui o campo de chave de referencia
			if(!isServer){
				registro.setNmChaveReferencia(regChaveServerCampoUnico.toString());
				LoteRegistroDAO.update(registro, connectLocal);
			}
		}
		
		Result resultado = new Result(1);
		resultado.addObject("regChaveReferencia", regChaveReferencia);
		resultado.addObject("registroInserido", registroInserido);
		return resultado;
		
	}
	
	/**
	 * Metodo que fará o UPDATE na tabela do Servidor
	 * @param tabela Tabela que o registro irá inserir
	 * @param regChaveLocal Chave primária do registro no Local
	 * @param regChaveServidor Chave primária do registro no Servidor
	 * @param regChaveEstrangeiraLocal Chaves estrangeiras do Local
	 * @param regRegistroLocal Contem os registros dos campos que estáo sendo inseridos
	 * @param registro Registro de LoteRegistro que está sendo analisado
	 * @param connectLocal Conexão para o banco que está passando o registro
	 * @param connectServer Conexão para o banco que irá receber o registro
	 * @param isServer Verifica se o primeiro banco é servidor ou não
	 * @param cdLocal Passa o código do LOCAL que está gravado no SERVIDOR
	 * @param registrosRecentesLocal Passa os codigos dos registros que seráo analisados nessa sincronização pelo LOCAL (usado para quando a inserção virar uma atualização)
	 * @param cdCodigoLoteRegistroJaAnalisados Passa os códigos que não precisarão ser analisados pelo SERVIDOR (pois são originais do LOCAL)
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	public static Result updateRegistro(Tabela tabela, JSONObject regChaveLocal, JSONObject regChaveServidor, JSONObject regChaveEstrangeiraLocal, HashMap<String, Object> regRegistroLocal, LoteRegistro registro, Connection connectLocal, Connection connectServer, boolean isServer, int cdLocal, ArrayList<Integer> cdCodigoLoteRegistroJaAnalisados) throws JSONException, SQLException{
		//Verifica se haverá uma alteração ou não
		boolean registroAtualizado = false;
		
		//Transforma a chave primária do Servidor na forma de String criterios
		String chavesServer = getCriterios(regChaveServidor);
		
		//Busca se há campos que não podem ser nulos
		JSONObject regCamposNaoNulos = new JSONObject(tabela.getNmCampoNaoNulo());
		
		//Rsm que irá buscar as alterações recentes da sincronização ou do LOCAL ou do SERVIDOR
		ResultSetMap rsmServidorMaisRecenteLoteRegistro;
		
		if(!isServer){
			String dtInicioSincronizacao = ParametroServices.getValorOfParametro("DT_INICIO_SINCRONIZACAO", connectLocal);
			
			//String que tratará o sql que será usado caso não seja um SERVIDOR
			String sql = " SELECT * FROM sinc_lote_registro A "
				+ "  WHERE NOT EXISTS (SELECT * FROM sinc_lote_registro_local B WHERE A.cd_lote_registro = B.cd_lote_registro AND B.cd_local = "+cdLocal+")"
				+ "    AND A.dt_atualizacao > '" + dtInicioSincronizacao + "'"
				+ "  ORDER BY A.cd_lote_registro";
			
			rsmServidorMaisRecenteLoteRegistro = new ResultSetMap(connectServer.prepareStatement(sql).executeQuery());
		}
		else{
			
			//String que tratará o sql que será usado caso não seja um SERVIDOR
			String sql = " SELECT * FROM sinc_lote_registro WHERE cd_lote IS NULL ORDER BY cd_lote_registro";
			
			rsmServidorMaisRecenteLoteRegistro = new ResultSetMap(connectServer.prepareStatement(sql).executeQuery());
		}
		
		//Buscar atualizações mais recentes do servidor do que as atualizações do registro
		JSONObject camposAlterados = new JSONObject(registro.getNmCampoAlterado());
		Iterator<?> chavesCamposAlterados = camposAlterados.keys();
		int j = 0;
		String camposAtualizados = "";
		//Itera apenas sobre os campos que foram alterados pelo UPDATE
		while(camposAlterados != null && chavesCamposAlterados.hasNext()){
			//Pega a chave e valor alterados
			String chaveCamposAlterados = chavesCamposAlterados.next().toString();
			String valorCamposAlterados = camposAlterados.get(chaveCamposAlterados).toString();
			//Variavel que verifica se essa é a alteração mais recente (comparado com a alteração do Servidor, se tiver havido alteração nesse registro nesse campo)
			boolean alteracaoMaisRecente = true;
			//Itera sobre as alterações feitas pelo Servidor, para a comparação
			while(rsmServidorMaisRecenteLoteRegistro.next()){
				//Se a atualização do registro atual for mais recente, então é passado para o proximo do rsm
				if(rsmServidorMaisRecenteLoteRegistro.getGregorianCalendar("dt_atualizacao").before(registro.getDtAtualizacao()))
					continue;
				//Se for a vez do LOCAL na rodada e o registro estiver entre os já analisados (passados para o SERVIDOR a partir do LOCAL), então é passado para o proximo do rsm 
				if(!isServer && cdCodigoLoteRegistroJaAnalisados.contains(rsmServidorMaisRecenteLoteRegistro.getInt("cd_lote_registro")))
					continue;
				//Se não for um UPDATE, então é passado para o proximo do rsm
				if(rsmServidorMaisRecenteLoteRegistro.getInt("tp_atualizacao") != LoteRegistroServices.TP_UPDATE)
					continue;
				//OBS.: Essas três verificações acima, são feitas dessa forma por conta de não haver um Sql montado quando isServer estiver ativo
				//Assim não é possível filtrar essas três verificações na consulta
				
				//É buscado o objeto do Lote Registro
				LoteRegistro registroServidorMaisRecente = LoteRegistroDAO.get(rsmServidorMaisRecenteLoteRegistro.getInt("CD_LOTE_REGISTRO"), connectServer);
				//É buscado os campos alterados desse lote registro em JSON
				JSONObject camposAlteradosServidorMaisRecente = new JSONObject(registroServidorMaisRecente.getNmCampoAlterado());
				if(camposAlteradosServidorMaisRecente != null){
					Iterator<?> chavesCamposAlteradosServidorMaisRecente = camposAlteradosServidorMaisRecente.keys();
					//Itera-se sobre os campos alterados
					while(chavesCamposAlteradosServidorMaisRecente.hasNext()){
						String chaveCamposAlteradosServidorMaisRecente = (String) chavesCamposAlteradosServidorMaisRecente.next();
						//Caso o campo alterado seja o mesmo que o registro atual
						if(chaveCamposAlterados.equals(chaveCamposAlteradosServidorMaisRecente)){
							//Variavel para se verificar se o registro que o lote registro do Servidor modificou, é o mesmo que o atual esta tentando modificar
							boolean mesmaChave = true;
							
							JSONObject chavesServidorMaisRecente = new JSONObject(registroServidorMaisRecente.getNmValorChaves());
							Iterator<?> keysServidorMaisRecente = chavesServidorMaisRecente.keys();
							//Itera-se sobre a chave primária para saber se ela é igual
							while(keysServidorMaisRecente.hasNext()){
								String chaveServidorMaisRecente = (String) keysServidorMaisRecente.next();
								String valorServidorMaisRecente = chavesServidorMaisRecente.get(chaveServidorMaisRecente).toString();
								if(!regChaveServidor.has(chaveServidorMaisRecente) || !regChaveServidor.get(chaveServidorMaisRecente).toString().equals(valorServidorMaisRecente)){
									mesmaChave = false;
								}
							}
							//Caso a chave seja a mesma, então a alteração mais recente foi feita pelo Servidor, logo não se alterará esse campo
							if(mesmaChave)
								alteracaoMaisRecente = false;
						}
					}
				}
			}
			rsmServidorMaisRecenteLoteRegistro.beforeFirst();
			if(alteracaoMaisRecente){
				//Se o campo alterado for uma chave estrangeira, irá se buscar o verdadeiro valor dela no Servidor a partir dos Lote Registro anteriores
				if(regChaveEstrangeiraLocal.has(chaveCamposAlterados.toUpperCase())){
					HashMap<Tabela, JSONObject> chavesEstrangeiras = getTabelasChaveEstrangeira(tabela, regRegistroLocal, false, connectLocal);
					for(Tabela tabelaProvedora : chavesEstrangeiras.keySet()){
						JSONObject chavesEstrangeirasPrimaria = chavesEstrangeiras.get(tabelaProvedora);
						if(chavesEstrangeirasPrimaria.has(chaveCamposAlterados.toUpperCase())){
							
							Result resultado = getChavePrimariaOriginal(chaveCamposAlterados, tabela, chavesEstrangeirasPrimaria, connectLocal);
							chavesEstrangeirasPrimaria = (JSONObject) resultado.getObjects().get("chavesEstrangeirasPrimariaServidor");
							
							String chaveEstrangeira = (String) resultado.getObjects().get("chave");
							
							JSONObject chavesEstrangeirasPrimariaServidor = getValorChaveServidor(tabelaProvedora, chavesEstrangeirasPrimaria, connectLocal, connectServer, isServer, cdLocal, registro.getCdLoteRegistro());
							
							if(j==0){
								camposAtualizados  = chaveCamposAlterados + " = " + (!chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && !chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("null") ? "'" + chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira) + "'" : (chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && regCamposNaoNulos.has(chaveEstrangeira.toUpperCase()) ? "0" : "NULL"));
							}
							else{
								camposAtualizados  += ", " + chaveCamposAlterados + " = " + (!chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && !chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("null") ? "'" + chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira) + "'" : (chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && regCamposNaoNulos.has(chaveEstrangeira.toUpperCase()) ? "0" : "NULL"));
							}
							j++;
							break;
						}
					}
				}
				//Se for qualquer outro campo (campos comuns e campo de chave primária não geradora) irá apenas colocar o valor que o Local pôs
				else{
					if(j==0){
						camposAtualizados = chaveCamposAlterados + " = " + (valorCamposAlterados.split("\\|").length > 1 && valorCamposAlterados.split("\\|")[1] != null && !valorCamposAlterados.split("\\|")[1].equals("null") && !valorCamposAlterados.split("\\|")[1].equals("0") && !valorCamposAlterados.split("\\|")[1].equals("") ? "'" + valorCamposAlterados.split("\\|")[1] + "'" : "null");
					}
					else{
						camposAtualizados += ", " + chaveCamposAlterados + " = " + (valorCamposAlterados.split("\\|").length > 1 && valorCamposAlterados.split("\\|")[1] != null && !valorCamposAlterados.split("\\|")[1].equals("null") && !valorCamposAlterados.split("\\|")[1].equals("") && !valorCamposAlterados.split("\\|")[1].equals("0") ? "'" + valorCamposAlterados.split("\\|")[1] + "'" : "null");
					}
					j++;
				}
			}
		}
		
		//atualizar os campos com atualizações mais recentes
		if(!camposAtualizados.equals("") && !camposAtualizados.equals("{}")){
			registroAtualizado = true;
			System.out.println(registro.getCdLoteRegistro() + " - UPDATE " + tabela.getNmTabela() + " SET " + camposAtualizados + " WHERE " + chavesServer);
			connectServer.prepareStatement("UPDATE " + tabela.getNmTabela() + " SET " + camposAtualizados + " WHERE " + chavesServer).executeUpdate();								
		}
		
		Result resultado = new Result(1);
		resultado.addObject("registroAtualizado", registroAtualizado);
		return resultado;
		
	}
	
	/**
	 * Metodo para buscar as chaves estrangeiras de uma tabela cadastrada em SINC_TABELA
	 * @param tabela Tabela que se deseja buscar as chaves estrangeiras
	 * @param connect
	 * @return Retorna um JSONObject com todas as chaves estrangeiras com valor 0
	 * @throws SQLException 
	 * @throws JSONException 
	 */
	public static JSONObject getChaveEstrangeira(Tabela tabela, Connection connect) throws SQLException, JSONException{
		//Variavel para não deixar a mesma chave ser incluida duas vezes no JSON final
		ArrayList<String> chavesMarcadas = new ArrayList<String>();
		//Faz uma busca na tabela SINC_TABELA_DEPENDENCIA para saber todas as tabelas provedoras da tabela passada
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_dependente", "" + tabela.getCdTabela(), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmTabelasProvedoras = TabelaDependenciaDAO.find(criterios, connect);
		//Variavel que guardará por meio do JSON, as chaves primarias das tabelas provedoras da tabela passada
		String chavesEstrangeiras = "{";
		//Itera sobre as tabelas provedoras
		for(int i = 0; rsmTabelasProvedoras.next();){
			//Busca a tabela dependencia
			TabelaDependencia tabelaDependencia = TabelaDependenciaDAO.get(rsmTabelasProvedoras.getInt("cd_dependente"), rsmTabelasProvedoras.getInt("cd_provedor"), connect);
			
			//Busca as chaves estrangeiras que fazem parte dessa relação
			JSONObject chaveEstrangeira = new JSONObject(tabelaDependencia.getNmChaves());
			
			Iterator<?> keysChaveEstrangeira = chaveEstrangeira.keys();
			//Itera sobre as chaves buscadas
			while(keysChaveEstrangeira.hasNext()){
				String chaveTabela = (String) keysChaveEstrangeira.next();
				if(!chavesMarcadas.contains(chaveTabela)){
					
					//Inclui a chave na String que fará o novo JSONObject
					if(i == 0)
						chavesEstrangeiras += chaveTabela + ":0";
					else
						chavesEstrangeiras += ", " + chaveTabela + ":0";
					
					chavesMarcadas.add(chaveTabela);
					
					i++;
				}
			}
			
		}
		//Põe-se  o fechamento de chaves ao final
		chavesEstrangeiras += "}";
		return new JSONObject(chavesEstrangeiras);
	}
	
	/**
	 * Metodo que retorna um HashMap que liga uma tabela provedora as suas chaves com os valores das mesmas, a partir de uma tabela dependente
	 * @param tabela Tabela dependente que se deseja busca as relações com a tabela provedora
	 * @param registroLocal Registro que se deseja busca as relações de chave estrangeira
	 * @param usarChaveOriginal Campo que indica qual o nome as chaves terão no JSONObject, o nome delas como esta na tabela provedora (true), ou na tabela dependente (false)
	 * @param connect
	 * @return Retorna um HashMap que associa as tabelas provedoras com os campos e valores das chaves
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static HashMap<Tabela, JSONObject> getTabelasChaveEstrangeira(Tabela tabela, HashMap<String, Object> registroLocal, boolean usarChaveOriginal, Connection connect) throws SQLException, JSONException{
		HashMap<Tabela, JSONObject> tabelaChaves = new HashMap<Tabela, JSONObject>();
		
		//Faz uma busca na tabela SINC_TABELA_DEPENDENCIA para saber todas as tabelas provedoras da tabela passada
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_dependente", "" + tabela.getCdTabela(), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmTabelasProvedoras = TabelaDependenciaDAO.find(criterios, connect);
		//Itera sobre as tabelas provedoras
		while(rsmTabelasProvedoras.next()){
			TabelaDependencia tabelaDependente = TabelaDependenciaDAO.get(rsmTabelasProvedoras.getInt("cd_dependente"), rsmTabelasProvedoras.getInt("cd_provedor"), connect);
			Tabela tabelaProvedora = TabelaDAO.get(rsmTabelasProvedoras.getInt("cd_provedor"), connect);
			//Busca as chaves da tabela
			JSONObject campoChave = new JSONObject(tabelaDependente.getNmChaves());
			Iterator<?> keys = campoChave.keys();
			String camposJson = "{";
			int j = 0;
			while(keys.hasNext()){
				String chave = keys.next().toString();
		
				//Coloca o nome da chave estrangeira (ou da chave original) com o valor pego com o nome da chave original
				if(j==0)
					camposJson += chave + " : " + registroLocal.get(chave);
				else
					camposJson += ", " + chave + " : " + registroLocal.get(chave);
				j++;
			}
			camposJson += "}";
			//Poe no HashMap a tabela com as suas chaves e valores
			tabelaChaves.put(tabelaProvedora, new JSONObject(camposJson));
		}
		
		return tabelaChaves;
	}
	
	/**
	 * Metodo que retorna o valor da chave estrangeira de uma tabela dependente, com os nomes originais, vindos da tabela provedora
	 * @param chaveOriginal
	 * @param tabela
	 * @param chavesNoLocal
	 * @param connect
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static Result getChavePrimariaOriginal(String chaveOriginal, Tabela tabela, JSONObject chavesNoLocal, Connection connect) throws SQLException, JSONException{
		String chaveOriginalRetorno = "";
		
		PreparedStatement pstmt = connect.prepareStatement("SELECT pgcon.* FROM pg_constraint pgcon " + 
															"WHERE pgcon.conrelid = CAST(? AS REGCLASS) " +
															"AND contype = 'f'");
		pstmt.setString(1, tabela.getNmTabela());
		
		PreparedStatement pstmt2 = connect.prepareStatement("SELECT attname FROM pg_attribute WHERE attrelid = ? AND attnum = ?");
		
		Iterator<?> keys = chavesNoLocal.keys();
		JSONObject chavesNoLocalOriginal = new JSONObject();
		while(keys.hasNext()){
			String chave = (String)keys.next();
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				Integer[] chaves = (Integer[])((java.sql.Array)rsm.getObject("conkey")).getArray();
				Integer[] chaves2 = (Integer[])((java.sql.Array)rsm.getObject("confkey")).getArray();
				for(int i = 0; i < chaves.length; i++){
					pstmt2.setInt(1, rsm.getInt("conrelid"));
					pstmt2.setInt(2, chaves[i]);
					ResultSetMap rsm2 = new ResultSetMap(pstmt2.executeQuery());
					rsm2.next();
					
					pstmt2.setInt(1, rsm.getInt("confrelid"));
					pstmt2.setInt(2, chaves2[i]);
					ResultSetMap rsm3 = new ResultSetMap(pstmt2.executeQuery());
					rsm3.next();
					
					if(rsm2.getString("attname").toUpperCase().equals(chave)){
						if(chave.equals(chaveOriginal))
							chaveOriginalRetorno = rsm3.getString("attname").toUpperCase();
						String novaChave = rsm3.getString("attname").toUpperCase();
						chavesNoLocalOriginal.put(novaChave, chavesNoLocal.get(chave));
					}
				}
			}
		}
		Result resultado = new Result(1);
		resultado.addObject("chavesEstrangeirasPrimariaServidor", chavesNoLocalOriginal);
		resultado.addObject("chave", chaveOriginalRetorno);
		return resultado;
	}
	
	/**
	 * Verifica se determinada tabela possui campo unico, para que este seja analisado na hora de um INSERT ou UPDATE
	 * @param tabela
	 * @param regRegistroLocal
	 * @param connectServer
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	private static JSONObject hasCampoUnicoOuMesmaChave(HashMap<String, Object> registro, Tabela tabela, JSONObject regChaveLocal, HashMap<String, Object> regRegistroLocal, JSONObject regChaveEstrangeiraLocal, Connection connectServer, Connection connectLocal, boolean isServer, int cdLocal, LoteRegistro loteRegistro, JSONObject regCamposNaoNulos) throws JSONException, SQLException {
		
		//Variavel que detecta se a chave primária é composta apenas de chave estrangeira
		boolean apenasChaveEstrangeira = true;
		
		Iterator<?> keysChaveLocal = regChaveLocal.keys();
		while(keysChaveLocal.hasNext()){
			String chave = (String) keysChaveLocal.next();
			if(!regChaveEstrangeiraLocal.has(chave))
				apenasChaveEstrangeira = false;
		}
		if(tabela.getNmCampoUnico() == null || tabela.getNmCampoUnico().equals(""))
			return (apenasChaveEstrangeira ? hasMesmaChave(tabela, regChaveLocal, connectServer, connectLocal, regRegistroLocal, isServer, cdLocal, loteRegistro, regCamposNaoNulos) : null);
		JSONObject regCampoUnicoLocal = new JSONObject(tabela.getNmCampoUnico());
		Iterator<?> keys = regCampoUnicoLocal.keys();
		while(keys.hasNext()){
			String chave = (String)keys.next();
			regCampoUnicoLocal.put(chave, regRegistroLocal.get(chave));
		}
		String criterioCampoUnico = getCriterios(regCampoUnicoLocal);
		if(criterioCampoUnico == null || criterioCampoUnico.equals(""))
			return (apenasChaveEstrangeira ? hasMesmaChave(tabela, regChaveLocal, connectServer, connectLocal, regRegistroLocal, isServer, cdLocal, loteRegistro, regCamposNaoNulos) : null);
		ResultSetMap rsm = new ResultSetMap(connectServer.prepareStatement("SELECT * FROM " + tabela.getNmTabela() + " WHERE " + criterioCampoUnico).executeQuery());
		ResultSetMap rsm2 = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM " + tabela.getNmTabela() + " WHERE " + criterioCampoUnico).executeQuery());
		if(rsm != null && rsm.next() && rsm2.size() < 2){
			JSONObject regCampoChaveServer = new JSONObject(tabela.getNmCampoChave());
			keys = regCampoChaveServer.keys();
			while(keys.hasNext()){
				String chave = (String)keys.next();
				regCampoChaveServer.put(chave, rsm.getString(chave));
			}
			
			return regCampoChaveServer;
		}
		else{
			return (apenasChaveEstrangeira ? hasMesmaChave(tabela, regChaveLocal, connectServer, connectLocal, regRegistroLocal, isServer, cdLocal, loteRegistro, regCamposNaoNulos) : null);
		}
		
	}
	
	/**
	 * Metodo que verifica se há a mesma chave primaria entre o Local e o Servidor
	 * @param tabela
	 * @param regChaveLocal
	 * @param connectServer
	 * @param connectLocal
	 * @param regRegistroLocal
	 * @param isServer
	 * @param cdLocal
	 * @param registro
	 * @param regCamposNaoNulos
	 * @return
	 * @throws JSONException
	 * @throws SQLException
	 */
	private static JSONObject hasMesmaChave(Tabela tabela, JSONObject regChaveLocal, Connection connectServer, Connection connectLocal, HashMap<String, Object> regRegistroLocal, boolean isServer, int cdLocal, LoteRegistro registro, JSONObject regCamposNaoNulos) throws JSONException, SQLException {
		
		JSONObject regChaveLocalReferencia = new JSONObject(regChaveLocal.toString());
		Iterator<?> iterador = regChaveLocalReferencia.keys();
		while(iterador.hasNext()){
			String chave = (String)iterador.next();
			
			//Busca-se as chaves estrangeiras a partir das suas tabelas de origem
			HashMap<Tabela, JSONObject> chavesEstrangeiras = getTabelasChaveEstrangeira(tabela, regRegistroLocal, false, connectLocal);
			for(Tabela tabelaProvedora : chavesEstrangeiras.keySet()){
				JSONObject chavesEstrangeirasPrimaria = chavesEstrangeiras.get(tabelaProvedora);
				//Caso a chave analisada fará parte dessa tabela
				if(chavesEstrangeirasPrimaria.has(chave)){
					//Busca-se os nomes originais da chave primária
					Result resultado = getChavePrimariaOriginal(chave, tabela, chavesEstrangeirasPrimaria, connectLocal);
					chavesEstrangeirasPrimaria = (JSONObject) resultado.getObjects().get("chavesEstrangeirasPrimariaServidor");
					String chaveEstrangeira = (String) resultado.getObjects().get("chave");
					//Verifica-se o valor da chave servidor dessa chave estrangeira, caminhando-se pelos Lote Registro
					JSONObject chavesEstrangeirasPrimariaServidor = getValorChaveServidor(tabelaProvedora, chavesEstrangeirasPrimaria, connectLocal, connectServer, isServer, cdLocal, registro.getCdLoteRegistro());
					regChaveLocalReferencia.put(chave, (!chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && !chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("null") ? chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira) : (chavesEstrangeirasPrimariaServidor.get(chaveEstrangeira).toString().equals("0") && regCamposNaoNulos.has(chaveEstrangeira.toUpperCase()) ? "0" : "NULL")));
				}
			}
		
		}
		String criteriosChave = getCriterios(regChaveLocalReferencia);
		ResultSetMap rsm = new ResultSetMap(connectServer.prepareStatement("SELECT * FROM " + tabela.getNmTabela() + " WHERE " + criteriosChave).executeQuery());
		if(rsm.next()){
			return regChaveLocalReferencia;
		}
		
		return null;
	}

	private static JSONObject getRegistroTabela(Tabela tabela,
		JSONObject regChaveServidor, Connection connect) throws JSONException, SQLException {
		String criterios = getCriterios(regChaveServidor);
		ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM " + tabela.getNmTabela() + " WHERE " + criterios).executeQuery());
		String json = "{";
		if(rsm.next()){
			
			int i = 0;
			HashMap<String, Object> register = rsm.getRegister();
			for(String chave : register.keySet()){
				if(i==0){
					json += chave + " : " + (register.get(chave) == null || (register.get(chave).toString()).equals("null") ? "null" : "'" + register.get(chave) + "'"); 
				}
				else{
					json += ", " + chave + " : " + (register.get(chave) == null || (register.get(chave).toString()).equals("null") ? "null" : "'" + register.get(chave) + "'");
				}
				i++;
			}
		}
		json += "}";
		json = json.replaceAll("\\n", "");
		json = json.replaceAll("\\r", "");
		json = json.replaceAll("\\t", "");
		return new JSONObject(json);
	}

	/**
	 * Metodo que verifica o valor da chave no Banco de Dados oposto, fazendo uma passagem de todos os seus Lote Registro, do mais recente ao mais antigo, para saber
	 * se houve INSERCAO, UPDATE ou DELETE dessa chave, fazendo as modificações necessárias caso tal tenha acontecido, e depois buscando dos Lote Registro do Banco
	 * Oposto do mais antigo para o mais recente, se houve INSERCAO, UPDATE ou DELETE, fazendo também as modificações necessárias.
	 * @param tabela
	 * @param regChaveLocal
	 * @param connectLocal
	 * @param connectServer
	 * @param isServer
	 * @param cdLocal
	 * @param cdRegistro
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 */
	private static JSONObject getValorChaveServidor(Tabela tabela, JSONObject regChaveLocal, Connection connectLocal, Connection connectServer, boolean isServer, int cdLocal, int cdRegistro) throws SQLException, JSONException {
		String dtInicioSincronizacao = ParametroServices.getValorOfParametro("DT_INICIO_SINCRONIZACAO", connectServer);
		
		JSONObject regChaveServidor = new JSONObject(regChaveLocal.toString());
		String sql = "SELECT * FROM sinc_lote_registro A "
				+(isServer ? "      LEFT OUTER JOIN sinc_lote_registro_local B ON (B.cd_lote_registro = A.cd_lote_registro AND B.cd_local = "+cdLocal+")" : "")
				+"      WHERE A.cd_lote_registro <= " + cdRegistro
				+"        AND cd_tabela = " + tabela.getCdTabela()
		   +(isServer ? " AND A.dt_atualizacao > '" + dtInicioSincronizacao + "'" : "")
				+"		ORDER BY A.cd_lote_registro DESC";
		
		PreparedStatement pstmt = connectLocal.prepareStatement(sql);
		ResultSetMap rsmRegistroLocal = new ResultSetMap(pstmt.executeQuery());
		
		boolean insertJaRealizado = false;
		
		//Primeira iteração: Registros do Banco atual, do mais recente para o mais antigo
		while(rsmRegistroLocal.next()){
			JSONObject regValorChave = new JSONObject(rsmRegistroLocal.getString("NM_VALOR_CHAVES"));
			
			Iterator<?> keysChaveLocal = regChaveServidor.keys();
			
			boolean isEqual = true;
			
			//Verifica-se se o registro do rsm tem a mesma chave daquele que se está buscando
			while(keysChaveLocal.hasNext()){
				String chave = (String) keysChaveLocal.next();
				JSONObject regCampoAlterado = new JSONObject(rsmRegistroLocal.getString("NM_CAMPO_ALTERADO"));
				Iterator<?> keysRegChaveAlterada = regCampoAlterado.keys();
				while(keysRegChaveAlterada.hasNext()){
					String chaveAlterada = (String)keysRegChaveAlterada.next();
					//Caso tenha havido alteração na chave, busca-se o valor original
					if(chave.equals(chaveAlterada) && regChaveServidor.get(chave).toString().equals(regCampoAlterado.get(chaveAlterada).toString().split("\\|")[1])){
						regChaveServidor.put(chaveAlterada, (regCampoAlterado.get(chaveAlterada).toString().split("\\|")[0]));
					}
				}
				if(!regValorChave.has(chave) || !regValorChave.get(chave).toString().equals(regChaveServidor.get(chave).toString())){
					isEqual = false;
					break;
				}
			}
			//Caso a chave seja a mesma
			if(isEqual){
				//Se tiver havido uma inserção, então descobre-se a chave que foi originalmente para o Servidor, podendo se passar para a próxima verificação
				if(rsmRegistroLocal.getInt("TP_ATUALIZACAO") == LoteRegistroServices.TP_INSERT){
					if(rsmRegistroLocal.getString((isServer ? "NM_CHAVE_LOCAL" : "NM_CHAVE_REFERENCIA")) != null){
						insertJaRealizado = true;
						regChaveServidor = new JSONObject(rsmRegistroLocal.getString((isServer ? "NM_CHAVE_LOCAL" : "NM_CHAVE_REFERENCIA")));
					}
					break;
					
				}
					
			}
		}
		
		dtInicioSincronizacao = ParametroServices.getValorOfParametro("DT_INICIO_SINCRONIZACAO", connectLocal);
		
		sql = "SELECT * FROM sinc_lote_registro A "
			+(!isServer ? "      LEFT OUTER JOIN sinc_lote_registro_local B ON (B.cd_lote_registro = A.cd_lote_registro AND B.cd_local = "+cdLocal+")" : "")
			+"  WHERE cd_tabela = " + tabela.getCdTabela()
			+(!isServer ? " AND A.dt_atualizacao > '" + dtInicioSincronizacao + "'" : "")
			+ " ORDER BY A.cd_lote_registro";
		pstmt = connectServer.prepareStatement(sql);
		rsmRegistroLocal = new ResultSetMap(pstmt.executeQuery());
		//Variavel que idenfica se houve um DELETE desse registro com a chave atual
		boolean identificadoDelete = false;
		//Variavel que idenfica se houve um INSERT desse registro com a chave atual
		boolean identificadoInsert = false;
		//OBS.:Essas variaveis acima procuram identificar ciclos de INSERT, UPDATE e DELETE, da mesma chave
		//Segunda iteração: Registros do Banco oposto, do mais antigo para o mais recente
		while(rsmRegistroLocal.next()){
			
			JSONObject regValorChave = new JSONObject(rsmRegistroLocal.getString("NM_VALOR_CHAVES"));
			
			Iterator<?> keysChaveLocal = regChaveServidor.keys();
			
			boolean isEqual = true;
			
			//Iteração para saber se houve modificação na chave, colocando o valor mais atual
			while(keysChaveLocal.hasNext()){
				String chave = (String) keysChaveLocal.next();
				JSONObject regCampoAlterado = new JSONObject(rsmRegistroLocal.getString("NM_CAMPO_ALTERADO"));
				Iterator<?> keysRegChaveAlterada = regCampoAlterado.keys();
				while(keysRegChaveAlterada.hasNext()){
					String chaveAlterada = (String)keysRegChaveAlterada.next();
					if(chave.equals(chaveAlterada) && regChaveServidor.get(chave).toString().equals(regCampoAlterado.get(chaveAlterada).toString().split("\\|")[0])){
						regChaveServidor.put(chaveAlterada, (regCampoAlterado.get(chaveAlterada).toString().split("\\|")[1]));
					}
				}
				
				if(!regValorChave.has(chave) || !regValorChave.get(chave).toString().equals(regChaveServidor.get(chave).toString())){
					isEqual = false;
					break;
				}
			}
			
			//Identifica se houve INSERT dessa chave
			if(!insertJaRealizado && !identificadoInsert && rsmRegistroLocal.getInt("TP_ATUALIZACAO") == LoteRegistroServices.TP_INSERT){
				String chaveLocalReferenciada = rsmRegistroLocal.getString((!isServer ? "NM_CHAVE_LOCAL" : "NM_CHAVE_REFERENCIA"));
				if(chaveLocalReferenciada != null && !chaveLocalReferenciada.equals("") && !chaveLocalReferenciada.equals("{}")){
					JSONObject regReferencia = new JSONObject(chaveLocalReferenciada);
					
					keysChaveLocal = regChaveServidor.keys();
					
					isEqual = true;
					
					while(keysChaveLocal.hasNext()){
						String chave = (String) keysChaveLocal.next();
						if(!regReferencia.has(chave) || !regReferencia.get(chave).toString().equals(regChaveServidor.get(chave).toString())){
							isEqual = false;
							break;
						}
					}
					//Se a chave for a mesma, buscar-se-a a chave do banco
					if(isEqual){
						
						regValorChave = new JSONObject(rsmRegistroLocal.getString("NM_VALOR_CHAVES"));
						Iterator<?> keysRegChaveServidor = regChaveServidor.keys();
						while(keysRegChaveServidor.hasNext()){
							String chaveRegChaveServidor = (String) keysRegChaveServidor.next();
							Iterator<?> keysRegValorChave = regValorChave.keys();
							while(keysRegValorChave.hasNext()){
								String chaveRegValorChave = (String) keysRegValorChave.next();
								if(chaveRegChaveServidor.equals(chaveRegValorChave)){
									regChaveServidor.put(chaveRegValorChave, regValorChave.get(chaveRegValorChave));
								}
							}
						}
						//Se houve um DELETE anteriormente dessa chave, então ele é desconsiderado, pois o registro é dito como semanticamente o mesmo
						if(identificadoDelete)
							identificadoDelete = false;
						
						identificadoInsert = true;
					}
				}
			}
			//Caso um DELETE seja identificado
			if(rsmRegistroLocal.getInt("TP_ATUALIZACAO") == LoteRegistroServices.TP_DELETE &&
			   rsmRegistroLocal.getInt("CD_LOTE") == 0){
				regValorChave = new JSONObject(rsmRegistroLocal.getString("NM_VALOR_CHAVES"));
				
				keysChaveLocal = regChaveServidor.keys();
				
				isEqual = true;
				
				while(keysChaveLocal.hasNext()){
					String chave = (String) keysChaveLocal.next();
					if(!regValorChave.has(chave) || !regValorChave.get(chave).toString().equals(regChaveServidor.get(chave).toString())){
						isEqual = false;
						break;
					}
				}
				//Se as chaves forem iguais, identificação que houve um DELETE
				if(isEqual){
					identificadoDelete = true;
					identificadoInsert = false;
					insertJaRealizado = false;
				}
			}
		}
		//Se o DELETE foi identificado e nenhum INSERT posterior, significa que o registro foi apagado do SERVIDOR, e não se devolverá nada adiante
		//Haverá um erro na Sincronização
		if(identificadoDelete){
			return null;
		}
		
		return regChaveServidor;		
	}
	
	/**
	 * Inicializa o Grupo de Pessoa para a sincronização
	 */
	public static void initSincPessoa(){
		
		Connection connect = Conexao.conectar();
		
		try{
			System.out.println("initPessoa");
			connect.setAutoCommit(false);

			//Criação do registro de Pessoa
			Grupo grupo = GrupoServices.getByName("Pessoa", connect);
			if(grupo == null)
				grupo = new Grupo(0, "Pessoa", GrupoServices.ST_INATIVO);
			if(GrupoServices.save(grupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			//Tabela Empresa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlEmpresa = TabelaServices.getByName("grl_empresa", connect);
			if(tabelaGrlEmpresa == null)
				tabelaGrlEmpresa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_empresa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_EMPRESA\":1}", "{\"LG_EMPRESA\":0}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL)).getCode(), connect);
			else{
				tabelaGrlEmpresa.setDtInicio(Util.getDataAtual());
				tabelaGrlEmpresa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlEmpresa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL);
				if(TabelaServices.update(tabelaGrlEmpresa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			TabelaGrupo tabelaGrupo = new TabelaGrupo(tabelaGrlEmpresa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Vinculo
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlVinculo = TabelaServices.getByName("grl_vinculo", connect);
			if(tabelaGrlVinculo == null)
				tabelaGrlVinculo = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_vinculo", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_VINCULO\":1}", "{\"LG_ESTATICO\":0, \"LG_FUNCAO\":0, \"LG_CADASTRO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlVinculo.setDtInicio(Util.getDataAtual());
				tabelaGrlVinculo.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlVinculo.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlVinculo, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			tabelaGrupo = new TabelaGrupo(tabelaGrlVinculo.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Pessoa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlPessoa = TabelaServices.getByName("grl_pessoa", connect);
			if(tabelaGrlPessoa == null)
				tabelaGrlPessoa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_pessoa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PESSOA\":1}", "{\"GN_PESSOA\":0, \"ST_CADASTRO\":0, \"LG_NOTIFICACAO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlPessoa.setDtInicio(Util.getDataAtual());
				tabelaGrlPessoa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlPessoa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlPessoa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			tabelaGrupo = new TabelaGrupo(tabelaGrlPessoa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela PessoaEmpresa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlPessoaEmpresa = TabelaServices.getByName("grl_pessoa_empresa", connect);
			if(tabelaGrlPessoaEmpresa == null)
				tabelaGrlPessoaEmpresa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_pessoa_empresa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_EMPRESA\":0, \"CD_PESSOA\":0, \"CD_VINCULO\":0}", "{\"ST_VINCULO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlPessoaEmpresa.setDtInicio(Util.getDataAtual());
				tabelaGrlPessoaEmpresa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlPessoaEmpresa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlPessoaEmpresa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}		
			//Relaciona a tabela com o grupo de PessoaEmpresa
			tabelaGrupo = new TabelaGrupo(tabelaGrlPessoaEmpresa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela PessoaEndereco
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlPessoaEndereco = TabelaServices.getByName("grl_pessoa_endereco", connect);
			if(tabelaGrlPessoaEndereco == null)
				tabelaGrlPessoaEndereco = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_pessoa_endereco", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_ENDERECO\":1, \"CD_PESSOA\":0}", "{\"LG_COBRANCA\":0, \"LG_PRINCIPAL\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlPessoaEndereco.setDtInicio(Util.getDataAtual());
				tabelaGrlPessoaEndereco.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlPessoaEndereco.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlPessoaEndereco, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de PessoaEndereco
			tabelaGrupo = new TabelaGrupo(tabelaGrlPessoaEndereco.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela PessoaFisica
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlPessoaFisica = TabelaServices.getByName("grl_pessoa_fisica", connect);
			if(tabelaGrlPessoaFisica == null)
				tabelaGrlPessoaFisica = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_pessoa_fisica", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{\"NR_CPF\":0}", "{\"CD_PESSOA\":0}", "{\"TP_SEXO\":0, \"ST_ESTADO_CIVIL\":0, \"TP_CATEGORIA_HABILITACAO\":0, \"TP_RACA\":0, \"LG_DEFICIENTE_FISICO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlPessoaFisica.setDtInicio(Util.getDataAtual());
				tabelaGrlPessoaFisica.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlPessoaFisica.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlPessoaFisica, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de PessoaFisica
			tabelaGrupo = new TabelaGrupo(tabelaGrlPessoaFisica.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela PessoaJuridica
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlPessoaJuridica = TabelaServices.getByName("grl_pessoa_juridica", connect);
			if(tabelaGrlPessoaJuridica == null)
				tabelaGrlPessoaJuridica = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_pessoa_juridica", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{\"NR_CNPJ\":0}", "{\"CD_PESSOA\":0}", "{\"NR_FUNCIONARIOS\":0, \"TP_EMPRESA\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlPessoaJuridica.setDtInicio(Util.getDataAtual());
				tabelaGrlPessoaJuridica.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlPessoaJuridica.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlPessoaJuridica, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de PessoaJuridica
			tabelaGrupo = new TabelaGrupo(tabelaGrlPessoaJuridica.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Cliente
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmCliente = TabelaServices.getByName("adm_cliente", connect);
			if(tabelaAdmCliente == null)
				tabelaAdmCliente = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_cliente", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_EMPRESA\":0, \"CD_PESSOA\":0}", "{\"LG_CONVENIO\":0, \"LG_ECOMMERCE\":0, \"LG_LIMITE_CREDITO\":0, \"LG_AGENDA\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaAdmCliente.setDtInicio(Util.getDataAtual());
				tabelaAdmCliente.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmCliente.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaAdmCliente, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Cliente
			tabelaGrupo = new TabelaGrupo(tabelaAdmCliente.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
			//Registra dependencias do grupo Pessoa
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlPessoaEndereco.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_PESSOA\":\"CD_PESSOA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlPessoaFisica.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_PESSOA\":\"CD_PESSOA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlPessoaJuridica.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_PESSOA\":\"CD_PESSOA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlPessoaEmpresa.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_PESSOA\":\"CD_PESSOA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlPessoaEmpresa.getCdTabela(), tabelaGrlVinculo.getCdTabela(), "{\"CD_VINCULO\":\"CD_VINCULO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlPessoaEmpresa.getCdTabela(), tabelaGrlEmpresa.getCdTabela(), "{\"CD_EMPRESA\":\"CD_EMPRESA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmCliente.getCdTabela(), tabelaGrlEmpresa.getCdTabela(), "{\"CD_EMPRESA\":\"CD_EMPRESA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmCliente.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_PESSOA\":\"CD_PESSOA\"}"), connect);
			
			connect.commit();
			
			System.out.println("fim initPessoa");
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connect);
		}finally{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Inicializa o grupo de Vendas para a sincronização
	 */
	public static void initSincVenda(){
		Connection connect = Conexao.conectar();
		try{
			
			connect.setAutoCommit(false);
			System.out.println("initVenda");
			
			//Criação do registro de Venda
			Grupo grupo = GrupoServices.getByName("Venda", connect);
			if(grupo == null)
				grupo = new Grupo(0, "Venda", GrupoServices.ST_INATIVO);
			if(GrupoServices.save(grupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			//Tabela DocumentoSaida
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAlmDocumentoSaida = TabelaServices.getByName("alm_documento_saida", connect);
			if(tabelaAlmDocumentoSaida == null)
				tabelaAlmDocumentoSaida = TabelaDAO.get(TabelaServices.save(new Tabela(0, "alm_documento_saida", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_DOCUMENTO_SAIDA\":1}", "{\"ST_DOCUMENTO_SAIDA\":0, \"TP_DOCUMENTO_SAIDA\":0, \"TP_SAIDA\":0, \"TP_FRETE\":0, \"CD_EMPRESA\":0}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAlmDocumentoSaida.setDtInicio(Util.getDataAtual());
				tabelaAlmDocumentoSaida.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAlmDocumentoSaida.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAlmDocumentoSaida, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			TabelaGrupo tabelaGrupo = new TabelaGrupo(tabelaAlmDocumentoSaida.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Pessoa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlPessoa = TabelaServices.getByName("grl_pessoa", connect);
			if(tabelaGrlPessoa == null)
				tabelaGrlPessoa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_pessoa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PESSOA\":1}", "{\"GN_PESSOA\":0, \"ST_CADASTRO\":0, \"LG_NOTIFICACAO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlPessoa.setDtInicio(Util.getDataAtual());
				tabelaGrlPessoa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlPessoa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlPessoa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			tabelaGrupo = new TabelaGrupo(tabelaGrlPessoa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela ProdutoServico
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlProdutoServico = TabelaServices.getByName("grl_produto_servico", connect);
			if(tabelaGrlProdutoServico == null)
				tabelaGrlProdutoServico = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_produto_servico", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PRODUTO_SERVICO\":1}", "{\"TP_PRODUTO_SERVICO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX), connect).getCode(), connect);
			else{
				tabelaGrlProdutoServico.setDtInicio(Util.getDataAtual());
				tabelaGrlProdutoServico.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlProdutoServico.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlProdutoServico, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de ProdutoServico
			tabelaGrupo = new TabelaGrupo(tabelaGrlProdutoServico.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela DocumentoSaidaItem
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAlmDocumentoSaidaItem = TabelaServices.getByName("alm_documento_saida_item", connect);
			if(tabelaAlmDocumentoSaidaItem == null)
				tabelaAlmDocumentoSaidaItem = TabelaDAO.get(TabelaServices.save(new Tabela(0, "alm_documento_saida_item", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_DOCUMENTO_SAIDA\":0, \"CD_PRODUTO_SERVICO\":2, \"CD_EMPRESA\":2, \"CD_ITEM\":1}", "{}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAlmDocumentoSaidaItem.setDtInicio(Util.getDataAtual());
				tabelaAlmDocumentoSaidaItem.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAlmDocumentoSaidaItem.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAlmDocumentoSaidaItem, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de DocumentoSaidaItem
			tabelaGrupo = new TabelaGrupo(tabelaAlmDocumentoSaidaItem.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Empresa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlEmpresa = TabelaServices.getByName("grl_empresa", connect);
			if(tabelaGrlEmpresa == null)
				tabelaGrlEmpresa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_empresa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_EMPRESA\":1}", "{\"LG_EMPRESA\":0}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL)).getCode(), connect);
			else{
				tabelaGrlEmpresa.setDtInicio(Util.getDataAtual());
				tabelaGrlEmpresa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlEmpresa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL);
				if(TabelaServices.update(tabelaGrlEmpresa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Empresa
			tabelaGrupo = new TabelaGrupo(tabelaGrlEmpresa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela LocalArmazenamento
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAlmLocalArmazenamento = TabelaServices.getByName("alm_local_armazenamento", connect);
			if(tabelaAlmLocalArmazenamento == null)
				tabelaAlmLocalArmazenamento = TabelaDAO.get(TabelaServices.save(new Tabela(0, "alm_local_armazenamento", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_LOCAL_ARMAZENAMENTO\":1}", "{}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX), connect).getCode(), connect);
			else{
				tabelaAlmLocalArmazenamento.setDtInicio(Util.getDataAtual());
				tabelaAlmLocalArmazenamento.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAlmLocalArmazenamento.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaAlmLocalArmazenamento, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de LocalArmazenamento
			tabelaGrupo = new TabelaGrupo(tabelaAlmLocalArmazenamento.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela SaidaLocalItem
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAlmSaidaLocalItem = TabelaServices.getByName("alm_saida_local_item", connect);
			if(tabelaAlmSaidaLocalItem == null)
				tabelaAlmSaidaLocalItem = TabelaDAO.get(TabelaServices.save(new Tabela(0, "alm_saida_local_item", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_SAIDA\":1, \"CD_PRODUTO_SERVICO\":0, \"CD_DOCUMENTO_SAIDA\":0, \"CD_EMPRESA\":0, \"CD_LOCAL_ARMAZENAMENTO\":0, \"CD_ITEM\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL), connect).getCode(), connect);
			else{
				tabelaAlmSaidaLocalItem.setDtInicio(Util.getDataAtual());
				tabelaAlmSaidaLocalItem.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAlmSaidaLocalItem.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaAlmSaidaLocalItem, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de SaidaLocalItem
			tabelaGrupo = new TabelaGrupo(tabelaAlmSaidaLocalItem.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Tributo
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmTributo = TabelaServices.getByName("adm_tributo", connect);
			if(tabelaAdmTributo == null)
				tabelaAdmTributo = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_tributo", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_TRIBUTO\":0}", "{\"TP_TRIBUTO\":0, \"LG_ALIQUOTA_PROGRESSIVA\":0, \"TP_ESFERA_APLICACAO\":0, \"TP_OPERACAO\":0, \"TP_COBRANCA\":0}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL), connect).getCode(), connect);
			else{
				tabelaAdmTributo.setDtInicio(Util.getDataAtual());
				tabelaAdmTributo.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmTributo.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL);
				if(TabelaServices.update(tabelaAdmTributo, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Tributo
			tabelaGrupo = new TabelaGrupo(tabelaAdmTributo.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela TributoAliquota
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmTributoAliquota = TabelaServices.getByName("adm_tributo_aliquota", connect);
			if(tabelaAdmTributoAliquota == null)
				tabelaAdmTributoAliquota = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_tributo_aliquota", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_TRIBUTO_ALIQUOTA\":1, \"CD_TRIBUTO\":0}", "{\"ST_TRIBUTARIA\":0, \"TP_OPERACAO\":0, \"TP_BASE_CALCULO\":0, \"TP_MOTIVO_DESONERACAO\":0, \"TP_BASE_CALCULO_SUBSTITUICAO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX), connect).getCode(), connect);
			else{
				tabelaAdmTributoAliquota.setDtInicio(Util.getDataAtual());
				tabelaAdmTributoAliquota.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmTributoAliquota.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaAdmTributoAliquota, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de TributoAliquota
			tabelaGrupo = new TabelaGrupo(tabelaAdmTributoAliquota.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela SaidaItemAliquota
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmSaidaItemAliquota = TabelaServices.getByName("adm_saida_item_aliquota", connect);
			if(tabelaAdmSaidaItemAliquota == null)
				tabelaAdmSaidaItemAliquota = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_saida_item_aliquota", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PRODUTO_SERVICO\":0, \"CD_DOCUMENTO_SAIDA\":0, \"CD_EMPRESA\":0, \"CD_TRIBUTO_ALIQUOTA\":0, \"CD_TRIBUTO\":0, \"CD_ITEM\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmSaidaItemAliquota.setDtInicio(Util.getDataAtual());
				tabelaAdmSaidaItemAliquota.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmSaidaItemAliquota.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmSaidaItemAliquota, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de SaidaItemAliquota
			tabelaGrupo = new TabelaGrupo(tabelaAdmSaidaItemAliquota.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela SaidaTributo
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmSaidaTributo = TabelaServices.getByName("adm_saida_tributo", connect);
			if(tabelaAdmSaidaTributo == null)
				tabelaAdmSaidaTributo = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_saida_tributo", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_DOCUMENTO_SAIDA\":0, \"CD_TRIBUTO\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmSaidaTributo.setDtInicio(Util.getDataAtual());
				tabelaAdmSaidaTributo.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmSaidaTributo.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmSaidaTributo, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de SaidaTributo
			tabelaGrupo = new TabelaGrupo(tabelaAdmSaidaTributo.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			//Registra dependencias do grupo Venda
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAlmDocumentoSaida.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_CLIENTE\":\"CD_PESSOA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAlmDocumentoSaidaItem.getCdTabela(), tabelaAlmDocumentoSaida.getCdTabela(), "{\"CD_DOCUMENTO_SAIDA\":\"CD_DOCUMENTO_SAIDA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAlmDocumentoSaidaItem.getCdTabela(), tabelaGrlProdutoServico.getCdTabela(), "{\"CD_PRODUTO_SERVICO\":\"CD_PRODUTO_SERVICO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAlmDocumentoSaidaItem.getCdTabela(), tabelaGrlEmpresa.getCdTabela(), "{\"CD_EMPRESA\":\"CD_EMPRESA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAlmSaidaLocalItem.getCdTabela(), tabelaAlmDocumentoSaidaItem.getCdTabela(), "{\"CD_DOCUMENTO_SAIDA\":\"CD_DOCUMENTO_SAIDA\",\"CD_PRODUTO_SERVICO\":\"CD_PRODUTO_SERVICO\",\"CD_EMPRESA\":\"CD_EMPRESA\",\"CD_ITEM\":\"CD_ITEM\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAlmSaidaLocalItem.getCdTabela(), tabelaAlmLocalArmazenamento.getCdTabela(), "{\"CD_LOCAL_ARMAZENAMENTO\":\"CD_LOCAL_ARMAZENAMENTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmTributoAliquota.getCdTabela(), tabelaAdmTributo.getCdTabela(), "{\"CD_TRIBUTO\":\"CD_TRIBUTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmSaidaItemAliquota.getCdTabela(), tabelaAlmDocumentoSaidaItem.getCdTabela(), "{\"CD_DOCUMENTO_SAIDA\":\"CD_DOCUMENTO_SAIDA\", \"CD_PRODUTO_SERVICO\":\"CD_PRODUTO_SERVICO\", \"CD_EMPRESA\":\"CD_EMPRESA\", \"CD_ITEM\":\"CD_ITEM\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmSaidaItemAliquota.getCdTabela(), tabelaAdmTributoAliquota.getCdTabela(), "{\"CD_TRIBUTO_ALIQUOTA\":\"CD_TRIBUTO_ALIQUOTA\", \"CD_TRIBUTO\":\"CD_TRIBUTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmSaidaTributo.getCdTabela(), tabelaAlmDocumentoSaida.getCdTabela(), "{\"CD_DOCUMENTO_SAIDA\":\"CD_DOCUMENTO_SAIDA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmSaidaTributo.getCdTabela(), tabelaAdmTributo.getCdTabela(), "{\"CD_TRIBUTO\":\"CD_TRIBUTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlProdutoServico.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_FABRICANTE\":\"CD_PESSOA\"}"), connect);
			connect.commit();
			System.out.println("fim initVenda");
		
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connect);
		}finally{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Inicializa o grupo de Pagamento para a sincronização
	 */
	public static void initSincPagamento(){
		Connection connect = Conexao.conectar();
		try{
			
			connect.setAutoCommit(false);
			System.out.println("initPagamento");
			
			//Criação do registro de Pagamento
			Grupo grupo = GrupoServices.getByName("Pagamento", connect);
			if(grupo == null)
				grupo = new Grupo(0, "Pagamento", GrupoServices.ST_INATIVO);
			if(GrupoServices.save(grupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			//Tabela PlanoPagamento
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmPlanoPagamento = TabelaServices.getByName("adm_plano_pagamento", connect);
			if(tabelaAdmPlanoPagamento == null)
				tabelaAdmPlanoPagamento = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_plano_pagamento", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PLANO_PAGAMENTO\":1}", "{}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL), connect).getCode(), connect);
			else{
				tabelaAdmPlanoPagamento.setDtInicio(Util.getDataAtual());
				tabelaAdmPlanoPagamento.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmPlanoPagamento.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL);
				if(TabelaServices.update(tabelaAdmPlanoPagamento, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			TabelaGrupo tabelaGrupo = new TabelaGrupo(tabelaAdmPlanoPagamento.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela FormaPagamento
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmFormaPagamento = TabelaServices.getByName("adm_forma_pagamento", connect);
			if(tabelaAdmFormaPagamento == null)
				tabelaAdmFormaPagamento = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_forma_pagamento", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_FORMA_PAGAMENTO\":1}", "{\"TP_FORMA_PAGAMENTO\":0, \"LG_TRANSFERENCIA\":0}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL), connect).getCode(), connect);
			else{
				tabelaAdmFormaPagamento.setDtInicio(Util.getDataAtual());
				tabelaAdmFormaPagamento.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmFormaPagamento.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL);
				if(TabelaServices.update(tabelaAdmFormaPagamento, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			tabelaGrupo = new TabelaGrupo(tabelaAdmFormaPagamento.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela PlanoPagtoDocumentoSaida
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmPlanoPagtoDocumentoSaida = TabelaServices.getByName("adm_plano_pagto_documento_saida", connect);
			if(tabelaAdmPlanoPagtoDocumentoSaida == null)
				tabelaAdmPlanoPagtoDocumentoSaida = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_plano_pagto_documento_saida", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PLANO_PAGAMENTO\":0, \"CD_DOCUMENTO_SAIDA\":0, \"CD_FORMA_PAGAMENTO\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmPlanoPagtoDocumentoSaida.setDtInicio(Util.getDataAtual());
				tabelaAdmPlanoPagtoDocumentoSaida.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmPlanoPagtoDocumentoSaida.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmPlanoPagtoDocumentoSaida, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			tabelaGrupo = new TabelaGrupo(tabelaAdmPlanoPagtoDocumentoSaida.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela DocumentoSaida
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAlmDocumentoSaida = TabelaServices.getByName("alm_documento_saida", connect);
			if(tabelaAlmDocumentoSaida == null)
				tabelaAlmDocumentoSaida = TabelaDAO.get(TabelaServices.save(new Tabela(0, "alm_documento_saida", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_DOCUMENTO_SAIDA\":1}", "{\"ST_DOCUMENTO_SAIDA\":0, \"TP_DOCUMENTO_SAIDA\":0, \"TP_SAIDA\":0, \"TP_FRETE\":0}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAlmDocumentoSaida.setDtInicio(Util.getDataAtual());
				tabelaAlmDocumentoSaida.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAlmDocumentoSaida.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAlmDocumentoSaida, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			tabelaGrupo = new TabelaGrupo(tabelaAlmDocumentoSaida.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela ContaFinanceira
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmContaFinanceira = TabelaServices.getByName("adm_conta_financeira", connect);
			if(tabelaAdmContaFinanceira == null)
				tabelaAdmContaFinanceira = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_conta_financeira", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CONTA\":1}", "{\"TP_CONTA\":0, \"TP_OPERACAO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX), connect).getCode(), connect);
			else{
				tabelaAdmContaFinanceira.setDtInicio(Util.getDataAtual());
				tabelaAdmContaFinanceira.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmContaFinanceira.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaAdmContaFinanceira, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			tabelaGrupo = new TabelaGrupo(tabelaAdmContaFinanceira.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Empresa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlEmpresa = TabelaServices.getByName("grl_empresa", connect);
			if(tabelaGrlEmpresa == null)
				tabelaGrlEmpresa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_empresa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_EMPRESA\":1}", "{\"LG_EMPRESA\":0}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL)).getCode(), connect);
			else{
				tabelaGrlEmpresa.setDtInicio(Util.getDataAtual());
				tabelaGrlEmpresa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlEmpresa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL);
				if(TabelaServices.update(tabelaGrlEmpresa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Empresa
			tabelaGrupo = new TabelaGrupo(tabelaGrlEmpresa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Pessoa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlPessoa = TabelaServices.getByName("grl_pessoa", connect);
			if(tabelaGrlPessoa == null)
				tabelaGrlPessoa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_pessoa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PESSOA\":1}", "{\"GN_PESSOA\":0, \"ST_CADASTRO\":0, \"LG_NOTIFICACAO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlPessoa.setDtInicio(Util.getDataAtual());
				tabelaGrlPessoa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlPessoa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlPessoa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			tabelaGrupo = new TabelaGrupo(tabelaGrlPessoa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela ContaCarteira
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmContaCarteira = TabelaServices.getByName("adm_conta_carteira", connect);
			if(tabelaAdmContaCarteira == null)
				tabelaAdmContaCarteira = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_conta_carteira", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CONTA_CARTEIRA\":1, \"CD_CONTA\":0}", "{\"TP_DIGITO\":0, \"TP_COBRANCA\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX), connect).getCode(), connect);
			else{
				tabelaAdmContaCarteira.setDtInicio(Util.getDataAtual());
				tabelaAdmContaCarteira.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmContaCarteira.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaAdmContaCarteira, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de ContaCarteira
			tabelaGrupo = new TabelaGrupo(tabelaAdmContaCarteira.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela TituloCredito
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmTituloCredito = TabelaServices.getByName("adm_titulo_credito", connect);
			if(tabelaAdmTituloCredito == null)
				tabelaAdmTituloCredito = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_titulo_credito", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_TITULO_CREDITO\":1}", "{\"TP_DOCUMENTO_EMISSOR\":0, \"TP_EMISSAO\":0, \"ST_TITULO\":0, \"TP_CIRCULACAO\":0}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmTituloCredito.setDtInicio(Util.getDataAtual());
				tabelaAdmTituloCredito.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmTituloCredito.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmTituloCredito, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de TituloCredito
			tabelaGrupo = new TabelaGrupo(tabelaAdmTituloCredito.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela ContaReceber
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmContaReceber = TabelaServices.getByName("adm_conta_receber", connect);
			if(tabelaAdmContaReceber == null)
				tabelaAdmContaReceber = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_conta_receber", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CONTA_RECEBER\":1}", "{\"ST_CONTA\":0}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmContaReceber.setDtInicio(Util.getDataAtual());
				tabelaAdmContaReceber.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmContaReceber.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmContaReceber, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de ContaReceber
			tabelaGrupo = new TabelaGrupo(tabelaAdmContaReceber.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela CategoriaEconomica
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmCategoriaEconomica = TabelaServices.getByName("adm_categoria_economica", connect);
			if(tabelaAdmCategoriaEconomica == null)
				tabelaAdmCategoriaEconomica = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_categoria_economica", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CATEGORIA_ECONOMICA\":1}", "{\"TP_CATEGORIA_ECONOMICA\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX), connect).getCode(), connect);
			else{
				tabelaAdmCategoriaEconomica.setDtInicio(Util.getDataAtual());
				tabelaAdmCategoriaEconomica.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmCategoriaEconomica.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaAdmCategoriaEconomica, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de CategoriaEconomica
			tabelaGrupo = new TabelaGrupo(tabelaAdmCategoriaEconomica.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela CentroCusto
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaCtbCentroCusto = TabelaServices.getByName("ctb_centro_custo", connect);
			if(tabelaCtbCentroCusto == null)
				tabelaCtbCentroCusto = TabelaDAO.get(TabelaServices.save(new Tabela(0, "ctb_centro_custo", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CENTRO_CUSTO\":1}", "{}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX), connect).getCode(), connect);
			else{
				tabelaCtbCentroCusto.setDtInicio(Util.getDataAtual());
				tabelaCtbCentroCusto.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaCtbCentroCusto.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaCtbCentroCusto, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de CentroCusto
			tabelaGrupo = new TabelaGrupo(tabelaCtbCentroCusto.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela ContaReceberCategoria
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmContaReceberCategoria = TabelaServices.getByName("adm_conta_receber_categoria", connect);
			if(tabelaAdmContaReceberCategoria == null)
				tabelaAdmContaReceberCategoria = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_conta_receber_categoria", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CONTA_RECEBER_CATEGORIA\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmContaReceberCategoria.setDtInicio(Util.getDataAtual());
				tabelaAdmContaReceberCategoria.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmContaReceberCategoria.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmContaReceberCategoria, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de ContaReceberCategoria
			tabelaGrupo = new TabelaGrupo(tabelaAdmContaReceberCategoria.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Cheque
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmCheque = TabelaServices.getByName("adm_cheque", connect);
			if(tabelaAdmCheque == null)
				tabelaAdmCheque = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_cheque", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CHEQUE\":1}", "{\"ST_CHEQUE\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX), connect).getCode(), connect);
			else{
				tabelaAdmCheque.setDtInicio(Util.getDataAtual());
				tabelaAdmCheque.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmCheque.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaAdmCheque, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Cheque
			tabelaGrupo = new TabelaGrupo(tabelaAdmCheque.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela MovimentoConta
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmMovimentoConta = TabelaServices.getByName("adm_movimento_conta", connect);
			if(tabelaAdmMovimentoConta == null)
				tabelaAdmMovimentoConta = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_movimento_conta", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_MOVIMENTO_CONTA\":1, \"CD_CONTA\":0}", "{\"TP_MOVIMENTO\":0, \"TP_ORIGEM\":0, \"ST_MOVIMENTO\":0}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmMovimentoConta.setDtInicio(Util.getDataAtual());
				tabelaAdmMovimentoConta.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmMovimentoConta.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmMovimentoConta, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de MovimentoConta
			tabelaGrupo = new TabelaGrupo(tabelaAdmMovimentoConta.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela MovimentoContaCategoria
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmMovimentoContaCategoria = TabelaServices.getByName("adm_movimento_conta_categoria", connect);
			if(tabelaAdmMovimentoContaCategoria == null)
				tabelaAdmMovimentoContaCategoria = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_movimento_conta_categoria", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CONTA\":0, \"CD_MOVIMENTO_CONTA\":0, \"CD_CATEGORIA_ECONOMICA\":0, \"CD_MOVIMENTO_CONTA_CATEGORIA\":1}", "{\"TP_MOVIMENTO\":0}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmMovimentoContaCategoria.setDtInicio(Util.getDataAtual());
				tabelaAdmMovimentoContaCategoria.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmMovimentoContaCategoria.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmMovimentoContaCategoria, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de MovimentoContaCategoria
			tabelaGrupo = new TabelaGrupo(tabelaAdmMovimentoContaCategoria.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela MovimentoContaReceber
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmMovimentoContaReceber = TabelaServices.getByName("adm_movimento_conta_receber", connect);
			if(tabelaAdmMovimentoContaReceber == null)
				tabelaAdmMovimentoContaReceber = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_movimento_conta_receber", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CONTA\":0, \"CD_MOVIMENTO_CONTA\":0, \"CD_CONTA_RECEBER\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmMovimentoContaReceber.setDtInicio(Util.getDataAtual());
				tabelaAdmMovimentoContaReceber.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmMovimentoContaReceber.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmMovimentoContaReceber, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de MovimentoContaReceber
			tabelaGrupo = new TabelaGrupo(tabelaAdmMovimentoContaReceber.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela MovimentoTituloCredito
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmMovimentoTituloCredito = TabelaServices.getByName("adm_movimento_titulo_credito", connect);
			if(tabelaAdmMovimentoTituloCredito == null)
				tabelaAdmMovimentoTituloCredito = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_movimento_titulo_credito", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_TITULO_CREDITO\":0, \"CD_MOVIMENTO_CONTA\":0, \"CD_CONTA\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR), connect).getCode(), connect);
			else{
				tabelaAdmMovimentoTituloCredito.setDtInicio(Util.getDataAtual());
				tabelaAdmMovimentoTituloCredito.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmMovimentoTituloCredito.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_LOCAL_SERVIDOR);
				if(TabelaServices.update(tabelaAdmMovimentoTituloCredito, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de MovimentoTituloCredito
			tabelaGrupo = new TabelaGrupo(tabelaAdmMovimentoTituloCredito.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			//Registra dependencias do grupo Pagamento
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmPlanoPagtoDocumentoSaida.getCdTabela(), tabelaAlmDocumentoSaida.getCdTabela(), "{\"CD_DOCUMENTO_SAIDA\":\"CD_DOCUMENTO_SAIDA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmPlanoPagtoDocumentoSaida.getCdTabela(), tabelaAdmPlanoPagamento.getCdTabela(), "{\"CD_PLANO_PAGAMENTO\":\"CD_PLANO_PAGAMENTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmPlanoPagtoDocumentoSaida.getCdTabela(), tabelaAdmFormaPagamento.getCdTabela(), "{\"CD_FORMA_PAGAMENTO\":\"CD_FORMA_PAGAMENTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaFinanceira.getCdTabela(), tabelaGrlEmpresa.getCdTabela(), "{\"CD_EMPRESA\":\"CD_EMPRESA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaFinanceira.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_RESPONSAVEL\":\"CD_PESSOA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaCarteira.getCdTabela(), tabelaAdmContaFinanceira.getCdTabela(), "{\"CD_CONTA\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmTituloCredito.getCdTabela(), tabelaAdmContaFinanceira.getCdTabela(), "{\"CD_CONTA\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceber.getCdTabela(), tabelaAdmContaCarteira.getCdTabela(), "{\"CD_CONTA_CARTEIRA\":\"CD_CONTA_CARTEIRA\", \"CD_CONTA\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceber.getCdTabela(), tabelaAdmContaFinanceira.getCdTabela(), "{\"CD_CONTA\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceber.getCdTabela(), tabelaAdmContaReceber.getCdTabela(), "{\"CD_CONTA_ORIGEM\":\"CD_CONTA_RECEBER\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceber.getCdTabela(), tabelaAlmDocumentoSaida.getCdTabela(), "{\"CD_DOCUMENTO_SAIDA\":\"CD_DOCUMENTO_SAIDA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceber.getCdTabela(), tabelaGrlEmpresa.getCdTabela(), "{\"CD_EMPRESA\":\"CD_EMPRESA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceber.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_PESSOA\":\"CD_PESSOA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceber.getCdTabela(), tabelaAdmPlanoPagtoDocumentoSaida.getCdTabela(), "{\"CD_PLANO_PAGAMENTO\":\"CD_PLANO_PAGAMENTO\", \"CD_DOCUMENTO_SAIDA\":\"CD_DOCUMENTO_SAIDA\", \"CD_FORMA_PAGAMENTO\":\"CD_FORMA_PAGAMENTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmTituloCredito.getCdTabela(), tabelaAdmContaReceber.getCdTabela(), "{\"CD_CONTA_RECEBER\":\"CD_CONTA_RECEBER\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceber.getCdTabela(), tabelaAdmTituloCredito.getCdTabela(), "{\"CD_TITULO_CREDITO\":\"CD_TITULO_CREDITO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmCategoriaEconomica.getCdTabela(), tabelaAdmCategoriaEconomica.getCdTabela(), "{\"CD_CATEGORIA_SUPERIOR\":\"CD_CATEGORIA_ECONOMICA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaCtbCentroCusto.getCdTabela(), tabelaCtbCentroCusto.getCdTabela(), "{\"CD_CENTRO_CUSTO_SUPERIOR\":\"CD_CENTRO_CUSTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceberCategoria.getCdTabela(), tabelaAdmCategoriaEconomica.getCdTabela(), "{\"CD_CATEGORIA_ECONOMICA\":\"CD_CATEGORIA_ECONOMICA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceberCategoria.getCdTabela(), tabelaCtbCentroCusto.getCdTabela(), "{\"CD_CENTRO_CUSTO\":\"CD_CENTRO_CUSTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmContaReceberCategoria.getCdTabela(), tabelaAdmContaReceber.getCdTabela(), "{\"CD_CONTA_RECEBER\":\"CD_CONTA_RECEBER\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmCheque.getCdTabela(), tabelaAdmContaFinanceira.getCdTabela(), "{\"CD_CONTA\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoConta.getCdTabela(), tabelaAdmContaFinanceira.getCdTabela(), "{\"CD_CONTA\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoConta.getCdTabela(), tabelaAdmCheque.getCdTabela(), "{\"CD_CHEQUE\":\"CD_CHEQUE\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoConta.getCdTabela(), tabelaAdmMovimentoConta.getCdTabela(), "{\"CD_MOVIMENTO_ORIGEM\":\"CD_MOVIMENTO_CONTA\", \"CD_CONTA_ORIGEM\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoContaCategoria.getCdTabela(), tabelaAdmCategoriaEconomica.getCdTabela(), "{\"CD_CATEGORIA_ECONOMICA\":\"CD_CATEGORIA_ECONOMICA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoContaCategoria.getCdTabela(), tabelaCtbCentroCusto.getCdTabela(), "{\"CD_CENTRO_CUSTO\":\"CD_CENTRO_CUSTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoContaCategoria.getCdTabela(), tabelaAdmContaReceber.getCdTabela(), "{\"CD_CONTA_RECEBER\":\"CD_CONTA_RECEBER\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoContaCategoria.getCdTabela(), tabelaAdmMovimentoConta.getCdTabela(), "{\"CD_MOVIMENTO_CONTA\":\"CD_MOVIMENTO_CONTA\", \"CD_CONTA\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoContaReceber.getCdTabela(), tabelaAdmContaReceber.getCdTabela(), "{\"CD_CONTA_RECEBER\":\"CD_CONTA_RECEBER\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoContaReceber.getCdTabela(), tabelaAdmMovimentoConta.getCdTabela(), "{\"CD_MOVIMENTO_CONTA\":\"CD_MOVIMENTO_CONTA\", \"CD_CONTA\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoTituloCredito.getCdTabela(), tabelaAdmMovimentoConta.getCdTabela(), "{\"CD_MOVIMENTO_CONTA\":\"CD_MOVIMENTO_CONTA\", \"CD_CONTA\":\"CD_CONTA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmMovimentoTituloCredito.getCdTabela(), tabelaAdmTituloCredito.getCdTabela(), "{\"CD_TITULO_CREDITO\":\"CD_TITULO_CREDITO\"}"), connect);
			
			connect.commit();
			System.out.println("fim initPagamento");	
		
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connect);
		}finally{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Inicializa o grupo de Produto para a sincronização
	 */
	public static void initSincProduto(){
		
		Connection connect = Conexao.conectar();
		
		try{
			connect.setAutoCommit(false);
			
			//Criação do registro de Produto
			Grupo grupo = GrupoServices.getByName("Produto", connect);
			if(grupo == null)
				grupo = new Grupo(0, "Produto", GrupoServices.ST_INATIVO);
			if(GrupoServices.save(grupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			//Tabela ProdutoServico
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlProdutoServico = TabelaServices.getByName("grl_produto_servico", connect);
			if(tabelaGrlProdutoServico == null)
				tabelaGrlProdutoServico = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_produto_servico", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PRODUTO_SERVICO\":1}", "{}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlProdutoServico.setDtInicio(Util.getDataAtual());
				tabelaGrlProdutoServico.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlProdutoServico.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlProdutoServico, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			TabelaGrupo tabelaGrupo = new TabelaGrupo(tabelaGrlProdutoServico.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Produto
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlProduto = TabelaServices.getByName("grl_produto", connect);
			if(tabelaGrlProduto == null)
				tabelaGrlProduto = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_produto", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PRODUTO_SERVICO\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlProduto.setDtInicio(Util.getDataAtual());
				tabelaGrlProduto.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlProduto.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlProduto, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Produto
			tabelaGrupo = new TabelaGrupo(tabelaGrlProduto.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela ProdutoServicoEmpresa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlProdutoServicoEmpresa = TabelaServices.getByName("grl_produto_servico_empresa", connect);
			if(tabelaGrlProdutoServicoEmpresa == null)
				tabelaGrlProdutoServicoEmpresa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_produto_servico_empresa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_EMPRESA\":0, \"CD_PRODUTO_SERVICO\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlProdutoServicoEmpresa.setDtInicio(Util.getDataAtual());
				tabelaGrlProdutoServicoEmpresa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlProdutoServicoEmpresa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlProdutoServicoEmpresa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de ProdutoServicoEmpresa
			tabelaGrupo = new TabelaGrupo(tabelaGrlProdutoServicoEmpresa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Empresa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlEmpresa = TabelaServices.getByName("grl_empresa", connect);
			if(tabelaGrlEmpresa == null)
				tabelaGrlEmpresa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_empresa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_EMPRESA\":1}", "{\"LG_EMPRESA\":0}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL)).getCode(), connect);
			else{
				tabelaGrlEmpresa.setDtInicio(Util.getDataAtual());
				tabelaGrlEmpresa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlEmpresa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL);
				if(TabelaServices.update(tabelaGrlEmpresa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Empresa
			tabelaGrupo = new TabelaGrupo(tabelaGrlEmpresa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela LocalArmazenamento
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAlmLocalArmazenamento = TabelaServices.getByName("alm_local_armazenamento", connect);
			if(tabelaAlmLocalArmazenamento == null)
				tabelaAlmLocalArmazenamento = TabelaDAO.get(TabelaServices.save(new Tabela(0, "alm_local_armazenamento", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_LOCAL_ARMAZENAMENTO\":1}", "{}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX), connect).getCode(), connect);
			else{
				tabelaAlmLocalArmazenamento.setDtInicio(Util.getDataAtual());
				tabelaAlmLocalArmazenamento.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAlmLocalArmazenamento.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaAlmLocalArmazenamento, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de LocalArmazenamento
			tabelaGrupo = new TabelaGrupo(tabelaAlmLocalArmazenamento.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			
			
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela TabelaPreco
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmTabelaPreco = TabelaServices.getByName("adm_tabela_preco", connect);
			if(tabelaAdmTabelaPreco == null)
				tabelaAdmTabelaPreco = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_tabela_preco", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_TABELA_PRECO\":1}", "{}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL), connect).getCode(), connect);
			else{
				tabelaAdmTabelaPreco.setDtInicio(Util.getDataAtual());
				tabelaAdmTabelaPreco.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmTabelaPreco.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL);
				if(TabelaServices.update(tabelaAdmTabelaPreco, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			
			//Relaciona a tabela com o grupo de Tabela de PreÃ§o
			tabelaGrupo = new TabelaGrupo(tabelaAdmTabelaPreco.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela ProdutoServicoPreco
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaAdmProdutoServicoPreco = TabelaServices.getByName("adm_produto_servico_preco", connect);
			if(tabelaAdmProdutoServicoPreco == null)
				tabelaAdmProdutoServicoPreco = TabelaDAO.get(TabelaServices.save(new Tabela(0, "adm_produto_servico_preco", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_TABELA_PRECO\":0, \"CD_PRODUTO_SERVICO\":0, \"CD_PRODUTO_SERVICO_PRECO\":1}", "{}", TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL), connect).getCode(), connect);
			else{
				tabelaAdmProdutoServicoPreco.setDtInicio(Util.getDataAtual());
				tabelaAdmProdutoServicoPreco.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaAdmProdutoServicoPreco.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_SERVIDOR_LOCAL);
				if(TabelaServices.update(tabelaAdmProdutoServicoPreco, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			
			//Relaciona a tabela com o grupo de Produto Servico PreÃ§o
			tabelaGrupo = new TabelaGrupo(tabelaAdmProdutoServicoPreco.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			
			//Registra dependencias do grupo Produto
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlProduto.getCdTabela(), tabelaGrlProdutoServico.getCdTabela(), "{\"CD_PRODUTO_SERVICO\":\"CD_PRODUTO_SERVICO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlProdutoServicoEmpresa.getCdTabela(), tabelaGrlProdutoServico.getCdTabela(), "{\"CD_PRODUTO_SERVICO\":\"CD_PRODUTO_SERVICO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlProdutoServicoEmpresa.getCdTabela(), tabelaGrlEmpresa.getCdTabela(), "{\"CD_EMPRESA\":\"CD_EMPRESA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlProdutoServicoEmpresa.getCdTabela(), tabelaAlmLocalArmazenamento.getCdTabela(), "{\"CD_LOCAL_ARMAZENAMENTO\":\"CD_LOCAL_ARMAZENAMENTO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmProdutoServicoPreco.getCdTabela(), tabelaAdmTabelaPreco.getCdTabela(), "{\"CD_TABELA_PRECO\":\"CD_TABELA_PRECO\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaAdmProdutoServicoPreco.getCdTabela(), tabelaGrlProdutoServico.getCdTabela(), "{\"CD_PRODUTO_SERVICO\":\"CD_PRODUTO_SERVICO\"}"), connect);
			
			connect.commit();
			
		
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connect);
		}finally{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Inicializa o grupo de Endereco para a sincronização
	 */
	public static void initSincEndereco(){
		
		Connection connect = Conexao.conectar();
		
		try{
			connect.setAutoCommit(false);
			
			//Criação do registro de Produto
			Grupo grupo = GrupoServices.getByName("Endereco", connect);
			if(grupo == null)
				grupo = new Grupo(0, "Endereco", GrupoServices.ST_INATIVO);
			if(GrupoServices.save(grupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			//Tabela PessoaEndereco
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlPessoaEndereco = TabelaServices.getByName("grl_pessoa_endereco", connect);
			if(tabelaGrlPessoaEndereco == null)
				tabelaGrlPessoaEndereco = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_pessoa_endereco", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_ENDERECO\":1, \"CD_PESSOA\":0}", "{}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlPessoaEndereco.setDtInicio(Util.getDataAtual());
				tabelaGrlPessoaEndereco.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlPessoaEndereco.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlPessoaEndereco, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			TabelaGrupo tabelaGrupo = new TabelaGrupo(tabelaGrlPessoaEndereco.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Pessoa
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlPessoa = TabelaServices.getByName("grl_pessoa", connect);
			if(tabelaGrlPessoa == null)
				tabelaGrlPessoa = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_pessoa", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_PESSOA\":1}", "{\"GN_PESSOA\":0, \"ST_CADASTRO\":0, \"LG_NOTIFICACAO\":0}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlPessoa.setDtInicio(Util.getDataAtual());
				tabelaGrlPessoa.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlPessoa.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlPessoa, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Pessoa
			tabelaGrupo = new TabelaGrupo(tabelaGrlPessoa.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Tabela Cidade
			//Verifica se a tabela existe, caso não exista ela é criada
			Tabela tabelaGrlCidade = TabelaServices.getByName("grl_cidade", connect);
			if(tabelaGrlCidade == null)
				tabelaGrlCidade = TabelaDAO.get(TabelaServices.save(new Tabela(0, "grl_cidade", Util.getDataAtual(), TabelaServices.ST_DESATIVADO, "{}", "{\"CD_CIDADE\":1}", "{}", TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX)).getCode(), connect);
			else{
				tabelaGrlCidade.setDtInicio(Util.getDataAtual());
				tabelaGrlCidade.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				tabelaGrlCidade.setTpSincronizacao(TabelaServices.TP_SINCRONIZACAO_FULL_DUPLEX);
				if(TabelaServices.update(tabelaGrlCidade, connect) < 0){
					Conexao.rollback(connect);
					return;
				}
			}
			//Relaciona a tabela com o grupo de Cidade
			tabelaGrupo = new TabelaGrupo(tabelaGrlCidade.getCdTabela(), grupo.getCdGrupo());
			if(TabelaGrupoServices.save(tabelaGrupo, connect).getCode() < 0){
				Conexao.rollback(connect);
				return;
			}
			
			//Registra dependencias do grupo Endereco
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlPessoaEndereco.getCdTabela(), tabelaGrlPessoa.getCdTabela(), "{\"CD_PESSOA\":\"CD_PESSOA\"}"), connect);
			TabelaDependenciaServices.save(new TabelaDependencia(tabelaGrlPessoaEndereco.getCdTabela(), tabelaGrlCidade.getCdTabela(), "{\"CD_CIDADE\":\"CD_CIDADE\"}"), connect);
			
			connect.commit();
			
		
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connect);
		}finally{
			Conexao.desconectar(connect);
		}
	}
	
}