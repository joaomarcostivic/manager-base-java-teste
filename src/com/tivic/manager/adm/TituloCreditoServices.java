package com.tivic.manager.adm;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TituloCreditoServices {

	public static final int teAVISTA = 0;
	public static final int teAPRAZO = 1;

	public static final int tdocCNPJ	= 0;
	public static final int tdocCPF 	= 1;

	public static final int tcPORTADOR 	= 0;
	public static final int tcNOMINAL 	= 1;
	public static final int tcAORDEM 	= 2;

	public static final int stEM_ABERTO 	= 0;
	public static final int stDESCONTADO	= 1;//Compensado
	public static final int stTRANSFERIDO	= 2;
	public static final int stLIQUIDADO		= 3;//Recebido
	public static final int stCANCELADO		= 4;
	//Factoring
	public static final int stDEVOLVIDA_1X 	= 5;
	public static final int stDEVOLVIDA_2X 	= 6;
	public static final int stRESGATADO 	= 7;
	public static final int stPERDA			= 8;
	
	public static final String[] tipoEmissao     = {"A Vista", "A Prazo"};
	public static final String[] tipoCirculacao  = {"Título ao Portador", "Título Nominativo", "Título à Ordem"};
	public static final String[] situacao        = {"Em aberto", "Descontado", "Transferido", "Liquidado", "Cancelado", "Devolvido 1x", "Devolvido 2x", "Resgatado", "Perda"};
	public static final String[] tipoEmissor     = {"Jurídica", "Física"};


	
	public static Result save(TituloCredito tituloCredito){
		return save(tituloCredito, null);
	}

	public static Result save(TituloCredito tituloCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tituloCredito==null)
				return new Result(-1, "Erro ao salvar. TituloCredito é nulo");

			int retorno;
			if(tituloCredito.getCdTituloCredito()==0){
				retorno = TituloCreditoDAO.insert(tituloCredito, connect);
				tituloCredito.setCdTituloCredito(retorno);
			}
			else {
				retorno = TituloCreditoDAO.update(tituloCredito, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TITULOCREDITO", tituloCredito);
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
	public static Result remove(int cdTituloCredito){
		return remove(cdTituloCredito, false, null);
	}
	public static Result remove(int cdTituloCredito, boolean cascade){
		return remove(cdTituloCredito, cascade, null);
	}
	public static Result remove(int cdTituloCredito, boolean cascade, Connection connect){
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
			retorno = TituloCreditoDAO.delete(cdTituloCredito, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_titulo_credito");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int insert(TituloCredito objeto) {
		return insert(objeto, null);
	}

	public static int insert(TituloCredito objeto, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			// Buscando informações do emissor
			if(objeto.getCdContaReceber()>0 && objeto.getNmEmissor()==null || objeto.getNmEmissor().equals(""))	{
				ResultSet rs = connect.prepareStatement("SELECT nm_pessoa, nr_cpf, nr_cnpj, gn_pessoa " +
						                                "FROM adm_conta_receber A " +
						                                "JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
						                                "LEFT OUTER JOIN grl_pessoa_fisica   C ON (B.cd_pessoa = C.cd_pessoa) " +
						                                "LEFT OUTER JOIN grl_pessoa_juridica D ON (B.cd_pessoa = D.cd_pessoa) " +
						                                "WHERE A.cd_conta_receber = "+objeto.getCdContaReceber()).executeQuery();
				if(rs.next()){
					objeto.setNmEmissor(rs.getString("nm_pessoa"));
					objeto.setTpDocumentoEmissor(rs.getInt("gn_pessoa"));
					if(rs.getInt("gn_pessoa")==tdocCNPJ)
						objeto.setNrDocumentoEmissor(rs.getString("nr_cnpj"));
					else
						objeto.setNrDocumentoEmissor(rs.getString("nr_cpf"));
				}
			}

			return TituloCreditoDAO.insert(objeto, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * Encapsulamento do método setSituacaoTitulos para uso no Flex
	 * especificamente para cancelamento de títulos
	 * 
	 * @see #setSituacaoTitulos(int[], int, Connection)
	 * 
	 * @author Alvaro
	 * @param titulos
	 * @since 07/05/2015
	 * @return
	 */
	public static Result cancelarTitulos(int[] titulos){
		return cancelarTitulos(titulos, null);
	}
	public static Result cancelarTitulos(int[] titulos, Connection connection){
		Result result = new Result(-1, "Erro ao efetuar cancelamento!");
		int cdRetorno = setSituacaoTitulos(titulos, stCANCELADO, connection);
		result.setCode(cdRetorno);
		if( cdRetorno > 0)
			result.setMessage("Cancelamento efetuado com sucesso!");
		return result;
	}
	
	public static int setSituacaoTitulos(int[] titulos, int stTituloCredito) {
		return setSituacaoTitulos(titulos, stTituloCredito, null);
	}

	public static int setSituacaoTitulos(int[] titulos, int stTituloCredito, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			for (int i=0; titulos!=null && i<titulos.length; i++)
				if (setSituacaoTitulo(titulos[i], stTituloCredito, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

			if(isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoServices.setSituacaoTitulos: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setSituacaoContaByTitulo(int cdContaReceber, int stTituloCredito, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connection);

			switch(stTituloCredito) {
				case stEM_ABERTO:
					PreparedStatement pstmt = connection.prepareStatement(
							   "SELECT SUM(vl_recebido) AS vl_recebido, " +
							   "       SUM(vl_multa) AS vl_multa, " +
							   "       SUM(vl_juros) AS vl_juros, " +
							   "       SUM(vl_desconto) AS vl_desconto, " +
							   "       MAX(B.dt_movimento) AS dt_movimento " +
					           "FROM adm_movimento_conta_receber A " +
					           "JOIN adm_movimento_conta B ON (A.cd_conta = B.cd_conta " +
					           "                          AND A.cd_movimento_conta = B.cd_movimento_conta) " +
					           "JOIN adm_forma_pagamento C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
					           "WHERE A.cd_conta_receber = ?");
					pstmt.setInt(1, cdContaReceber);
					ResultSet rs = pstmt.executeQuery();
					boolean next = rs.next();

					float vlRecebido = !next ? 0 : rs.getFloat("vl_recebido") - rs.getFloat("vl_juros") -
										rs.getFloat("vl_multa") + rs.getFloat("vl_desconto");
					Double vlAReceber = contaReceber.getVlConta() + contaReceber.getVlAcrescimo() - contaReceber.getVlAbatimento() -
										vlRecebido;
					if (vlAReceber <= 0.01)
						return -1;
					contaReceber.setStConta(ContaReceberServices.ST_EM_ABERTO);
					break;
				case stDESCONTADO:
				case stTRANSFERIDO:
				case stCANCELADO:
					pstmt = connection.prepareStatement("SELECT * " +
							"FROM adm_movimento_conta_receber " +
							"WHERE cd_conta_receber = ?");
					pstmt.setInt(1, cdContaReceber);
					if (pstmt.executeQuery().next())
						return -1;
					else
						contaReceber.setStConta(ContaReceberServices.ST_CANCELADA);
					break;
				case stLIQUIDADO:
					contaReceber.setStConta(ContaReceberServices.ST_RECEBIDA);
					break;
			}

			return ContaReceberDAO.update(contaReceber, connection);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setSituacaoTitulo(int cdTituloCredito, int stTituloCredito){
		return setSituacaoTitulo(cdTituloCredito, stTituloCredito, null);
	}

	public static int setSituacaoTitulo(int cdTituloCredito, int stTituloCredito, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			TituloCredito titulo = TituloCreditoDAO.get(cdTituloCredito, connection);
			if (titulo.getCdContaReceber()>0 && setSituacaoContaByTitulo(titulo.getCdContaReceber(), stTituloCredito, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			titulo.setStTitulo(stTituloCredito);
			if (TituloCreditoDAO.update(titulo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if(isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * Encapsulamento do método compensarTitulos para uso no Flex
	 * 
	 * @see #compensarTitulos(int[], int, GregorianCalendar, int, Connection)
	 * 
	 * @author Alvaro
	 * @param titulos
	 * @param cdFormaPagamento
	 * @param dtCompensacao
	 * @param cdUsuario
	 * @since 06/05/2015
	 * @return
	 */
	public static Result compensarTitulosCredito(int[] titulos, int cdFormaPagamento, GregorianCalendar dtCompensacao, int cdUsuario){
		return compensarTitulosCredito(titulos, cdFormaPagamento, dtCompensacao, cdUsuario, null);
	}
	public static Result compensarTitulosCredito(int[] titulos, int cdFormaPagamento, GregorianCalendar dtCompensacao, int cdUsuario, Connection connection ){
		Result result = new Result(-1);
		int cdResult = compensarTitulos(titulos, cdFormaPagamento, dtCompensacao, cdUsuario, connection);
		result.setCode(cdResult);
		result.setMessage( (cdResult>0)?"Compensação efetuada com sucesso":"Erro efetuar compensação..." );
		return result;
	}
	
	public static int compensarTitulos(int[] titulos, int cdFormaPagamento, GregorianCalendar dtCompensacao, int cdUsuario){
		return compensarTitulos(titulos, cdFormaPagamento, dtCompensacao, cdUsuario, null);
	}

	public static int compensarTitulos(int[] titulos, int cdFormaPagamento, GregorianCalendar dtCompensacao,
			int cdUsuario, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			for (int i=0; i<titulos.length; i++)
				if (compensarTitulo(titulos[i], cdFormaPagamento, dtCompensacao, cdUsuario, connection) == null) {
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
			Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static HashMap<String, Object> compensarTitulo(int cdTituloCredito, int cdFormaPagamento, GregorianCalendar dtCompensacao,
			int cdUsuario){
		return compensarTitulo(cdTituloCredito, cdFormaPagamento, dtCompensacao, cdUsuario, null);
	}

	public static HashMap<String, Object> compensarTitulo(int cdTituloCredito, int cdFormaPagamento, GregorianCalendar dtCompensacao,
			int cdUsuario, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			TituloCredito titulo = TituloCreditoDAO.get(cdTituloCredito, connection);
			if (titulo==null || titulo.getCdConta()<=0 || ContaFinanceiraDAO.get(titulo.getCdConta()).getTpConta()!=ContaFinanceiraServices.TP_CONTA_BANCARIA) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			int cdContaReceber = titulo.getCdContaReceber();
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connection);
			if (conta.getStConta() != ContaReceberServices.ST_EM_ABERTO) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT B.cd_movimento_conta, B.cd_conta " +
					"FROM adm_movimento_titulo_credito A, adm_movimento_conta B " +
					"WHERE A.cd_movimento_conta = B.cd_movimento_conta " +
					"  AND A.cd_conta = B.cd_conta " +
					"  AND A.cd_titulo_credito = ? " +
					"ORDER BY B.dt_movimento DESC");
			pstmt.setInt(1, cdTituloCredito);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			int cdMovimentoOrigem = rs.getInt("cd_movimento_conta");
			int cdContaOrigem = rs.getInt("cd_conta");
			MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/,
					titulo.getCdConta(),
					cdContaOrigem,
					cdMovimentoOrigem,
					cdUsuario,
					0 /*cdCheque*/,
					0 /*cdViagem*/,
					dtCompensacao /*dtMovimento*/,
					titulo.getVlTitulo() /*vlMovimento*/,
					"" /*nrDocumento*/,
					MovimentoContaServices.CREDITO /*tpMovimento*/,
					MovimentoContaServices.toDEPOSITO /*tpOrigem*/,
					MovimentoContaServices.ST_COMPENSADO /*stMovimento*/,
					"Compensação de título" /*dsHistorico*/,
					null /*dtDeposito*/,
					"" /*idExtrato*/,
					cdFormaPagamento,
					0 /*cdFechamento*/,0/*cdTurno*/);

			ArrayList<MovimentoContaReceber> contas = new ArrayList<MovimentoContaReceber>();
			contas.add(new MovimentoContaReceber(titulo.getCdConta(),
					0 /*cdMovimentoConta*/,
					cdContaReceber,
					titulo.getVlTitulo() /*vlRecebido*/,
					0 /*vlJuros*/,
					0 /*vlMulta*/,
					0 /*vlDesconto*/,
					0 /*vlTarifaCobranca*/,
					0 /*cdArquivo*/,
					0 /*cdRegistro*/));

			ArrayList<MovimentoContaCategoria> categorias = new ArrayList<MovimentoContaCategoria>();
			ResultSetMap rsmCategorias = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connection);
			while (rsmCategorias.next())
				categorias.add(new MovimentoContaCategoria(titulo.getCdConta(),
						0 /*cdMovimentoConta*/,
						rsmCategorias.getInt("cd_categoria_economica"),
						rsmCategorias.getFloat("vl_conta_categoria"),
						0 /*cdMovimeentoContaCategoria*/,
						0 /*cdContaPagar*/,
						cdContaReceber,
						MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));

			int cdMovimentoConta = -1;

			if ((cdMovimentoConta = MovimentoContaServices.insert(movimento, contas, categorias, null /*titulos*/,
					TituloCreditoServices.stEM_ABERTO, connection).getCode()) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if(isConnectionNull)
				connection.commit();

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("cdMovimentoConta", cdMovimentoConta);
			hash.put("cdConta", movimento.getCdConta());

			return hash;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setSituacaoTitulosOfConta(int cdContaReceber, int stContaReceber, GregorianCalendar dtSituacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_titulo_credito " +
					"SET st_titulo = ?, " +
					"	 dt_credito = ? " +
					"WHERE cd_titulo_credito = ?");

			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * " +
					"FROM adm_titulo_credito " +
					"WHERE cd_conta_receber = "+cdContaReceber).executeQuery());
			while(rsm.next())	{
				int stTituloCredito = rsm.getInt("st_titulo");
				switch(stContaReceber){
					case ContaReceberServices.ST_EM_ABERTO:
						stTituloCredito = stEM_ABERTO;
						break;
					case ContaReceberServices.ST_RECEBIDA:
						stTituloCredito = stLIQUIDADO;
						break;
					case ContaReceberServices.ST_NEGOCIADA:
					case ContaReceberServices.ST_CANCELADA:
						stTituloCredito = stCANCELADO;
						break;
					case ContaReceberServices.ST_PERDA:
						stTituloCredito = stPERDA;
						break;
					case 7:
						stTituloCredito = stDEVOLVIDA_1X;
						break;
					case 8:
						stTituloCredito = stDEVOLVIDA_2X;
						break;
					case 9:
						stTituloCredito = stDESCONTADO;
						break;
					case 10:
						stTituloCredito = stRESGATADO;	
						break;
				}
				pstmt.setInt(1, stTituloCredito);
				pstmt.setTimestamp(2, stTituloCredito==stLIQUIDADO ? Util.convCalendarToTimestamp(dtSituacao) : null);
				pstmt.setInt(3, rsm.getInt("cd_titulo_credito"));
				pstmt.execute();
			}

			if(isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TituloCredito objeto) {
		return update(objeto, null);
	}

	public static int update(TituloCredito objeto, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			if (objeto.getCdContaReceber()>0 && setSituacaoContaByTitulo(objeto.getCdContaReceber(),
					objeto.getStTitulo(), connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -100;
			}

			// Buscando informações do emissor
			if(objeto.getCdContaReceber()>0 && objeto.getNmEmissor()==null || objeto.getNmEmissor().equals(""))	{
				ResultSet rs = connect.prepareStatement("SELECT nm_pessoa, nr_cpf, nr_cnpj, gn_pessoa " +
						                                "FROM adm_conta_receber A " +
						                                "JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
						                                "LEFT OUTER JOIN grl_pessoa_fisica   C ON (B.cd_pessoa = C.cd_pessoa) " +
						                                "LEFT OUTER JOIN grl_pessoa_juridica D ON (B.cd_pessoa = D.cd_pessoa) " +
						                                "WHERE A.cd_conta_receber = "+objeto.getCdContaReceber()).executeQuery();
				if(rs.next()){
					objeto.setNmEmissor(rs.getString("nm_pessoa"));
					objeto.setTpDocumentoEmissor(rs.getInt("gn_pessoa"));
					if(rs.getInt("gn_pessoa")==tdocCNPJ)
						objeto.setNrDocumentoEmissor(rs.getString("nr_cnpj"));
					else
						objeto.setNrDocumentoEmissor(rs.getString("nr_cpf"));
				}
			}
			int ret = TituloCreditoDAO.update(objeto, connect);
			if (ret <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAsResultSet(int cdTituloCredito) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_titulo_credito", Integer.toString(cdTituloCredito), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		int qtLimite = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtlimite")){
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
			}
		}
		
		String[] limitFirst = Util.getLimitAndSkip(qtLimite, 0);
		ResultSetMap rsm =  Search.find("SELECT "+limitFirst[0]+" A.*, B.cd_banco, B.nr_banco AS nr_instituicao_financeira, G.nm_conta, " +
				"B.nm_banco AS nm_instituicao_financeira, C.cd_tipo_documento, C.sg_tipo_documento, C.nm_tipo_documento, " +
				"E.nm_pessoa, E.gn_pessoa as tp_pessoa, D.dt_emissao, D.cd_conta AS cd_conta_previsao, D.cd_conta_carteira, F.nm_conta AS nm_conta_previsao " +
				"FROM adm_titulo_credito A " +
	            "LEFT OUTER JOIN grl_banco B ON (A.cd_instituicao_financeira = B.cd_banco) " +
	            "LEFT OUTER JOIN adm_tipo_documento C ON (A.cd_tipo_documento = C.cd_tipo_documento) " +
	            "LEFT OUTER JOIN adm_conta_receber D ON (A.cd_conta_receber = D.cd_conta_receber) " +
	            "LEFT OUTER JOIN grl_pessoa E ON (D.cd_pessoa = E.cd_pessoa) " +
	            "LEFT OUTER JOIN adm_conta_financeira F ON (D.cd_conta = F.cd_conta) " +
	            "JOIN adm_conta_financeira G ON (A.cd_conta = G.cd_conta) " +
	            "LEFT OUTER JOIN adm_alinea H ON (A.cd_alinea = H.cd_alinea) ",
	            ( qtLimite > 0 )?limitFirst[1]:"",
	            criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		return rsm;
	}

	public static float getSaldoOf(int cdConta, int cdTipoDocumento)	{
		return getSaldoOf(cdConta, cdTipoDocumento, null);
	}
	//
	public static float getSaldoOf(int cdConta, int cdTipoDocumento, GregorianCalendar dtSaldo)	{
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareCall(
					                           "SELECT SUM(vl_titulo) AS vl_saldo "+
											   "FROM adm_titulo_credito A "+
											   "WHERE st_titulo = "+stEM_ABERTO+
											   "  AND cd_conta  = "+cdConta+
											   "  AND cd_tipo_documento = "+cdTipoDocumento+
											   "  AND EXISTS (SELECT * FROM adm_movimento_titulo_credito MTC, adm_movimento_conta MC "+
											   "              WHERE MC.cd_conta           = MTC.cd_conta "+
											   "                AND MC.cd_movimento_conta = MTC.cd_movimento_conta " +
											   "                AND MC.cd_conta           = A.cd_conta " +
											   "                AND MTC.cd_titulo_credito = A.cd_titulo_credito "+
											   "                AND MC.tp_movimento       = "+MovimentoContaServices.CREDITO+
											   (dtSaldo!=null?" AND MC.dt_movimento <= ? " : "")+") ");
			if(dtSaldo!=null)	{
				dtSaldo.set(Calendar.HOUR_OF_DAY, 23);
				dtSaldo.set(Calendar.MINUTE, 59);
				dtSaldo.set(Calendar.SECOND, 59);
				pstmt.setTimestamp(1, new Timestamp(dtSaldo.getTimeInMillis()));
			}
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
				return rs.getFloat(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
		}
		finally{
			Conexao.desconectar(connect);
		}
		return 0;
	}

	public static ResultSetMap getSaldoEmTitulo(int cdConta)	{
		return getSaldoEmTitulo(cdConta, null, null);
	}

	public static ResultSetMap getSaldoEmTitulo(int cdConta, GregorianCalendar dtSaldo)	{
		return getSaldoEmTitulo(cdConta, dtSaldo, null);
	}

	public static ResultSetMap getSaldoEmTitulo(int cdConta, GregorianCalendar dtSaldo, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_tipo_documento, B.nm_tipo_documento, B.id_tipo_documento, B.sg_tipo_documento, " +
					                                      "       SUM(vl_titulo) AS vl_saldo, COUNT(*) AS qt_titulo "+
					                                      //"       SUM(C.vl_conta + C.vl_acrescimo - C.vl_abatimento) AS vl_saldo, COUNT(*) AS qt_titulo "+
														  "FROM adm_titulo_credito A "+					                                      
														  "LEFT OUTER JOIN adm_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento) "+
														  //"LEFT OUTER JOIN adm_conta_receber C ON (A.cd_conta_receber = C.cd_conta_receber)"+ 
														  "WHERE st_titulo = "+stEM_ABERTO+
														  "  AND (SELECT COUNT(*) FROM adm_movimento_titulo_credito MTC, adm_movimento_conta MC "+
														  "       WHERE MC.cd_conta           = "+cdConta+
														  "         AND MC.cd_movimento_conta = MTC.cd_movimento_conta " +
														  "         AND MTC.cd_conta          = "+cdConta+
														  "         AND MTC.cd_titulo_credito = A.cd_titulo_credito "+
														  "         AND MC.tp_movimento       = "+MovimentoContaServices.CREDITO+
														  (dtSaldo!=null?" AND MC.dt_movimento      <= ?) > "+
														                 "  (SELECT COUNT(*) FROM adm_movimento_titulo_credito MTC, adm_movimento_conta MC "+
														                 "                  WHERE MC.cd_conta           = "+cdConta+
														                 "                    AND MC.cd_movimento_conta = MTC.cd_movimento_conta " +
														                 "                    AND MTC.cd_conta          = "+cdConta+
														                 "                    AND MTC.cd_titulo_credito = A.cd_titulo_credito "+
														                 "                    AND MC.tp_movimento       = "+MovimentoContaServices.DEBITO+
														                 "                    AND MC.dt_movimento      <= ? ) " 
														  :
														  ") AND A.cd_conta = "+cdConta)+  
														  "GROUP BY A.cd_tipo_documento, B.nm_tipo_documento, B.id_tipo_documento, B.sg_tipo_documento");
			if(dtSaldo!=null)	{
				dtSaldo.set(Calendar.HOUR_OF_DAY, 23);
				dtSaldo.set(Calendar.MINUTE, 59);
				dtSaldo.set(Calendar.SECOND, 59);
				dtSaldo.set(Calendar.MILLISECOND, 59);
				pstmt.setTimestamp(1, new Timestamp(dtSaldo.getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(dtSaldo.getTimeInMillis()));
			}
			return new ResultSetMap(pstmt.executeQuery());											
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getTituloOfSaldo(int cdConta, int cdTipoDocumento)	{
		return getTituloOfSaldo(cdConta, cdTipoDocumento, null/*dtSaldo*/,0, null);
	}

	public static ResultSetMap getTituloOfSaldo(int cdConta, int cdTipoDocumento, GregorianCalendar dtSaldo, int cdTurno)	{
		return getTituloOfSaldo(cdConta, cdTipoDocumento, dtSaldo,cdTurno, null);
	}
	
	public static ResultSetMap getTituloOfSaldo(int cdConta, int cdTipoDocumento, GregorianCalendar dtSaldo)	{
		return getTituloOfSaldo(cdConta, cdTipoDocumento, dtSaldo,0, null);
	}
	
	public static ResultSetMap getTituloOfSaldo(int cdConta, int cdTipoDocumento, GregorianCalendar dtSaldo,int cdTurno, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar(): connect;
			String sql = "SELECT A.*, B.nr_banco AS nr_instituicao_financeira, " +
						 "       B.nm_banco AS nm_instituicao_financeira, C.sg_tipo_documento, C.nm_tipo_documento, " +
						"		E.nm_pessoa, D.dt_emissao, A.vl_titulo AS vl_titulo " +
						"FROM adm_titulo_credito A " +
			            "LEFT OUTER JOIN grl_banco           B ON (A.cd_instituicao_financeira = B.cd_banco) " +
			            "LEFT OUTER JOIN adm_tipo_documento  C ON (A.cd_tipo_documento = C.cd_tipo_documento) " +
			            "LEFT OUTER JOIN adm_conta_receber   D ON (A.cd_conta_receber = D.cd_conta_receber) " +
			            "LEFT OUTER JOIN grl_pessoa          E ON (D.cd_pessoa = E.cd_pessoa) " +
			            "LEFT OUTER JOIN alm_documento_saida F ON (D.cd_documento_saida = F.cd_documento_saida) " +
			            "WHERE A.st_titulo         = "+stEM_ABERTO+
						(cdTipoDocumento>0 ? "  AND A.cd_tipo_documento = "+cdTipoDocumento : "")+
						(cdTurno>0 ? "  AND F.cd_turno = "+cdTurno : "")+
						"  AND EXISTS (SELECT * FROM adm_movimento_titulo_credito MTC, adm_movimento_conta MC "+
						"              WHERE MC.cd_conta           = "+cdConta+
						"                AND MC.cd_movimento_conta = MTC.cd_movimento_conta " +
						"                AND MTC.cd_conta          = "+cdConta+
						"                AND MTC.cd_titulo_credito = A.cd_titulo_credito "+
						"                AND MC.tp_movimento       = "+MovimentoContaServices.CREDITO+
					   (dtSaldo!=null ? 
						  "                AND MC.dt_movimento      <= ? )"+
						  "  AND NOT EXISTS (SELECT * FROM adm_movimento_titulo_credito MTC, adm_movimento_conta MC "+
						  "                  WHERE MC.cd_conta           = "+cdConta+
						  "                    AND MC.cd_movimento_conta = MTC.cd_movimento_conta " +
						  "                    AND MTC.cd_conta          = "+cdConta+
						  "                    AND MTC.cd_titulo_credito = A.cd_titulo_credito "+
						  "                    AND MC.tp_movimento       = "+MovimentoContaServices.DEBITO+
						  "                    AND MC.dt_movimento      <= ? ) " 
						  :
						  ") AND A.cd_conta = "+cdConta)+
						" ORDER BY D.dt_emissao, nm_banco, nm_emissor ";
			PreparedStatement pstmt = connect.prepareCall(sql);
			if(dtSaldo!=null)	{
				dtSaldo.set(Calendar.HOUR_OF_DAY, 23);
				dtSaldo.set(Calendar.MINUTE, 59);
				dtSaldo.set(Calendar.SECOND, 59);
				dtSaldo.set(Calendar.MILLISECOND, 59);
				pstmt.setTimestamp(1, new Timestamp(dtSaldo.getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(dtSaldo.getTimeInMillis()));
			}
			return new ResultSetMap(pstmt.executeQuery());		
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteAllOfContaReceber(int cdContaReceber) {
		return deleteAllOfContaReceber(cdContaReceber, null);
	}

	public static int deleteAllOfContaReceber(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_titulo_credito WHERE cd_conta_receber = " + cdContaReceber).executeQuery());

			while (rsm != null && rsm.next())	{
				connect.prepareStatement("DELETE FROM adm_movimento_titulo_credito " +
						                 "WHERE cd_titulo_credito = "+rsm.getInt("cd_titulo_credito")).executeUpdate();
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_titulo_credito WHERE cd_conta_receber = "+cdContaReceber);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String, Object> toRegister(TituloCredito titulo) {
		try {
			HashMap<String, Object> register = new HashMap<String, Object>();
			String[] fields = {"cd_titulo_credito",
					"cd_instituicao_financeira",
					"cd_alinea",
					"nr_documento",
					"nr_documento_emissor",
					"tp_documento_emissor",
					"nm_emissor",
					"vl_titulo",
					"tp_emissao",
					"nr_agencia",
					"dt_vencimento",
					"dt_credito",
					"st_titulo",
					"ds_observacao",
					"cd_tipo_documento",
					"tp_circulacao",
					"cd_conta",
					"cd_conta_receber"};
			Class<?> objClass = TituloCredito.class;
			for (int i=0; i<fields.length; i++) {
				String methodName = "";
				String field = fields[i];
				StringTokenizer token = new StringTokenizer(field.toLowerCase(),"_");
				for(int j=0; token.hasMoreTokens(); j++){
					if(j==0)
						methodName+=token.nextToken();
					else
						methodName+=Util.capitular(token.nextToken());
				}
				methodName = "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
				Method method = objClass.getDeclaredMethod(methodName, new Class[] {});
				Object value = method.invoke(titulo, new Object[] {});
				register.put(field.toUpperCase(), value);
			}
			return register;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap getTituloCreditoOfTurno(int cdConta,int cdTurno, GregorianCalendar dtDocumentoSaida,String sgFormaPagamento){
		return getTituloCreditoOfTurno(cdConta,cdTurno,dtDocumentoSaida,sgFormaPagamento,null);
	}
	
	public static ResultSetMap getTituloCreditoOfTurno(int cdConta,int cdTurno, GregorianCalendar dtDocumentoSaida){
		return getTituloCreditoOfTurno(cdConta,cdTurno,dtDocumentoSaida,null,null);
	}
	
	public static ResultSetMap getTituloCreditoOfTurno(int cdConta,int cdTurno, GregorianCalendar dtDocumentoSaida, String sgFormaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			GregorianCalendar dtDocumentoSaidaFinal = (GregorianCalendar) dtDocumentoSaida.clone();
			dtDocumentoSaida.set(Calendar.HOUR, 0);
			dtDocumentoSaida.set(Calendar.MINUTE, 0);
			dtDocumentoSaida.set(Calendar.SECOND, 0);
			
			dtDocumentoSaidaFinal.set(Calendar.HOUR, 23);
			dtDocumentoSaidaFinal.set(Calendar.MINUTE, 59);
			dtDocumentoSaidaFinal.set(Calendar.SECOND, 59);
			String sql = "SELECT DISTINCT(TC.cd_titulo_credito),DS.nr_documento_saida,MCR.vl_desconto,TC.dt_vencimento,TC.nm_emissor," +
					 "FP.nm_forma_pagamento,CR.qt_parcelas, CR.txt_observacao,MC.dt_movimento, MC.vl_movimento,FP.tp_forma_pagamento," +
					 "TC.nr_documento,TC.vl_titulo,CR.vl_acrescimo,TD.nm_tipo_documento,DS.vl_total_documento,PC.nm_pessoa AS nm_cliente " +
					 "FROM adm_titulo_credito TC " +
					 "LEFT OUTER JOIN adm_movimento_titulo_credito MTC ON(TC.cd_titulo_credito = MTC.cd_titulo_credito AND TC.cd_conta = MTC.cd_conta) " +
					 "LEFT OUTER JOIN adm_movimento_conta          MC  ON(MTC.cd_movimento_conta = MC.cd_movimento_conta AND TC.cd_conta = MC.cd_conta) " +
					 "LEFT OUTER JOIN adm_tipo_documento  		   TD ON (TC.cd_tipo_documento = TD.cd_tipo_documento) " +
					 "LEFT OUTER JOIN adm_movimento_conta_receber  MCR ON(MTC.cd_movimento_conta = MCR.cd_movimento_conta AND MC.cd_conta = MCR.cd_conta AND TC.cd_conta_receber = MCR.cd_conta_receber) " +
					 "LEFT OUTER JOIN adm_conta_receber            CR  ON(TC.cd_conta_receber = CR.cd_conta_receber  ) " +
					 "LEFT OUTER JOIN alm_documento_saida          DS  ON(CR.cd_documento_saida = DS.cd_documento_saida) " +
					 "LEFT OUTER JOIN grl_pessoa                   PC  ON(CR.cd_pessoa = PC.cd_pessoa) " +
					 "LEFT OUTER JOIN adm_forma_pagamento          FP  ON(CR.cd_forma_pagamento = FP.cd_forma_pagamento) " +
					 " WHERE DS.cd_turno = ? AND DS.cd_conta = ? AND DS.dt_documento_saida BETWEEN ? AND ? " +
					 (sgFormaPagamento!=null?" AND FP.sg_forma_pagamento = '"+ sgFormaPagamento+"'":"");
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdTurno);
			pstmt.setInt(2, cdConta);
			pstmt.setTimestamp(3, new Timestamp(dtDocumentoSaida.getTimeInMillis()));
			pstmt.setTimestamp(4, new Timestamp(dtDocumentoSaidaFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static ResultSetMap getRelatorioConsolidacaoNotasPrazo( ResultSetMap rsmFechamentos ){
		return getRelatorioConsolidacaoNotasPrazo(rsmFechamentos, null);
	}
	
	public static ResultSetMap getRelatorioConsolidacaoNotasPrazo( ResultSetMap rsmFechamentos,  Connection connect ){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmNotasPrazo = new ResultSetMap();
		ResultSetMap rsmTmpNotasPrazo = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			while(rsmFechamentos.next()){
				rsmTmpNotasPrazo = getTituloCreditoOfTurno( rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getInt("CD_TURNO"), rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO"), "NP");
				rsmNotasPrazo.getLines().addAll( rsmTmpNotasPrazo.getLines()  );
			}
			ArrayList<String> orderBy =  new ArrayList<String>();
			orderBy.add("NM_EMISSOR");
			rsmNotasPrazo.orderBy(orderBy);
			return rsmNotasPrazo;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getCartoesCredito(int cdConta, int cdTurno, GregorianCalendar dtFechamentoInicial){
		return getCartoesCredito(cdConta, cdTurno, dtFechamentoInicial, null);
	}
	
	public static ResultSetMap getCartoesCredito(int cdConta, int cdTurno, GregorianCalendar dtFechamentoInicial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();		
				String sql = "SELECT B.cd_forma_pagamento, nm_forma_pagamento, tp_forma_pagamento, B.cd_plano_pagamento, nm_plano_pagamento, vl_pagamento, " +
					 	     "A.dt_documento_saida AS dt_recebimento " +
			                 "FROM alm_documento_saida A " +
							 "JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
							 "JOIN adm_forma_pagamento             C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
							 "JOIN adm_plano_pagamento             D ON (B.cd_plano_pagamento = D.cd_plano_pagamento) " +
							 "WHERE A.cd_conta                       = "+cdConta+
							 "  AND CAST(dt_documento_saida AS DATE) = ? " +
							 "  AND A.cd_turno                       = "+cdTurno+
							 "  AND C.tp_forma_pagamento             = "+FormaPagamentoServices.TEF+
							 "ORDER BY nm_forma_pagamento, nm_plano_pagamento DESC ";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(dtFechamentoInicial.getTimeInMillis()));
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}		
	}
	
	public static ResultSetMap getRelatorioConsolidacaoCartoes( ResultSetMap rsmFechamentos ){
		return getRelatorioConsolidacaoCartoes(rsmFechamentos, null);
	}
	public static ResultSetMap getRelatorioConsolidacaoCartoes( ResultSetMap rsmFechamentos,  Connection connect ){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmCartoes = new ResultSetMap();
		ResultSetMap rsmTmpCartoes = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			while(rsmFechamentos.next()){
				rsmTmpCartoes = getCartoesCredito( rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getInt("CD_TURNO"), rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO") );
				rsmCartoes.getLines().addAll( rsmTmpCartoes.getLines()  );
			}
			
			return rsmCartoes;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @author Alvaro
	 * @param rsmFechamentos ResultSetMap 
	 * @return ResultSetMap contendo todos os cheques à vista de cada fechamento incluído no rsmFechamentos
	 * 		( Cheques à vista foram identificados como tendo a data de vencimento igual à data do fechamento )
	 */
	public static ResultSetMap getRelatorioConsolidacaoChequeVista( ResultSetMap rsmFechamentos ){
		return getRelatorioConsolidacaoChequeVista(rsmFechamentos, null);
	}
	public static ResultSetMap getRelatorioConsolidacaoChequeVista( ResultSetMap rsmFechamentos,  Connection connect ){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmCheques = new ResultSetMap();
		ResultSetMap rsmTmpCheques = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			while(rsmFechamentos.next()){
				rsmTmpCheques = getTituloCreditoOfTurno( rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getInt("CD_TURNO"), rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO") );
				rsmTmpCheques.beforeFirst();
				while( rsmTmpCheques.next() ){
					GregorianCalendar dtVencimentoCheque = rsmTmpCheques.getGregorianCalendar("DT_VENCIMENTO");
					GregorianCalendar dtFechamento = rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO");
					
					dtVencimentoCheque.clear( Calendar.HOUR );dtVencimentoCheque.clear( Calendar.MINUTE );dtVencimentoCheque.clear( Calendar.SECOND );
					dtFechamento.clear( Calendar.HOUR );dtFechamento.clear( Calendar.MINUTE );dtFechamento.clear( Calendar.SECOND );
					
					if( rsmTmpCheques.getString("NM_TIPO_DOCUMENTO").indexOf("Cheque") >= 0 
						&&  dtVencimentoCheque.compareTo( dtFechamento ) == 0  ){
						rsmCheques.addRegister( rsmTmpCheques.getRegister() );
					}
				}
			}
			ArrayList<String> orderBy =  new ArrayList<String>();
			orderBy.add("NM_EMISSOR");
			rsmCheques.orderBy(orderBy);
			return rsmCheques;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * @author Alvaro
	 * @param rsmFechamentos ResultSetMap 
	 * @return ResultSetMap contendo todos os cheques a prazo de cada fechamento incluído no rsmFechamentos
	 */
	public static ResultSetMap getRelatorioConsolidacaoChequePrazo( ResultSetMap rsmFechamentos ){
		return getRelatorioConsolidacaoChequePrazo(rsmFechamentos, null);
	}
	public static ResultSetMap getRelatorioConsolidacaoChequePrazo( ResultSetMap rsmFechamentos,  Connection connect ){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmCheques = new ResultSetMap();
		ResultSetMap rsmTmpCheques = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			while(rsmFechamentos.next()){
				rsmTmpCheques = getTituloCreditoOfTurno( rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getInt("CD_TURNO"), rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO") );
				rsmTmpCheques.beforeFirst();
				while( rsmTmpCheques.next() ){
					GregorianCalendar dtVencimentoCheque = rsmTmpCheques.getGregorianCalendar("DT_VENCIMENTO");
					GregorianCalendar dtFechamento = rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO");
					
					dtVencimentoCheque.clear( Calendar.HOUR );dtVencimentoCheque.clear( Calendar.MINUTE );dtVencimentoCheque.clear( Calendar.SECOND );
					dtFechamento.clear( Calendar.HOUR );dtFechamento.clear( Calendar.MINUTE );dtFechamento.clear( Calendar.SECOND );
					
					if( rsmTmpCheques.getString("NM_TIPO_DOCUMENTO").indexOf("Cheque") >= 0 
							&&  dtVencimentoCheque.compareTo( dtFechamento ) != 0  ){
						rsmCheques.addRegister( rsmTmpCheques.getRegister() );
					}
				}
			}
			ArrayList<String> orderBy =  new ArrayList<String>();
			orderBy.add("NM_EMISSOR");
			rsmCheques.orderBy(orderBy);
			return rsmCheques;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * @author Alvaro
	 * @param int cdContaReceber
	 * @return TituloCredito
	 */
	public static TituloCredito getByContaReceber(int cdContaReceber) {
		return getByContaReceber(cdContaReceber, null);
	}
	
	/**
	 * @author Alvaro
	 * @param int cdContaReceber
	 * @return TituloCredito
	 */
	public static TituloCredito getByContaReceber(int cdContaReceber, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rs = new ResultSetMap( connect.prepareStatement("SELECT * " +
								"FROM adm_titulo_credito " +
								"WHERE cd_conta_receber = "+cdContaReceber ).executeQuery());
			rs.beforeFirst();
			if( rs.next() )
				return TituloCreditoDAO.get( rs.getInt("CD_TITULO_CREDITO") );
				
			return null;	
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoServices.getByContaReceber: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoServices.getByContaReceber: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}