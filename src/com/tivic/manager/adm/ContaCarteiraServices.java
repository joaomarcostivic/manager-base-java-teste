package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ContaCarteiraServices {

	public static final int _NR_AGENCIA   = 0;
	public static final int _NR_CARTEIRA  = 1;
	public static final int _NR_CEDENTE   = 2;
	public static final int _NOSSO_NUMERO = 3;
	public static final int _FIXO         = 4;
	public static final int _NR_CONTA  	  = 5;
	public static final int _ANO_EMISSAO  = 6;
	public static final int _NR_SERVICO   = 7;
	public static final int _NR_CONVENIO  = 8;
	public static final int _DV_CAMPO_LIVRE = 9;

	public static final int _MOD10 	  = 0;
	public static final int _MOD11_V1 = 1;
	public static final int _MOD11_V2 = 2;
	public static final int _DV_NONE  = 5;

	public static final int _CNAB240 = 0;
	public static final int _CNAB400 = 1;

	public static String[] tipoCarteira = {"Não Informada","Simples","Vinculada","Caucionada","Descontada","Vendor"};

	public static String[] tipoCampo = {"Nº Agência","Nº Carteira","Nº Cedente","Nosso Nº","Fixo","Nº da Conta","Ano Emissão",
		                                "Nº Serviço", "Nº Convênio","DV CL [MOD11]"};

	
	/**
	 * @see this{@link #save(ContaCarteira, Connection)}
	 */
	public static Result save(ContaCarteira contaCarteira){
		return save(contaCarteira, null);
	}
	
	/**
	 * Método para encapsular processo de inserir ou atualizar
	 * 
	 * @author Luiz Romario Filho
	 * @param contaCarteira entidade a ser persistida
	 * @param conexão caso precise executar uma transação
	 * @since 28/08/2014
	 * @return result com código e mensagem da transação, code >= 0 ? sucesso : erro 
	 */
	public static Result save(ContaCarteira contaCarteira, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(contaCarteira==null)
				return new Result(-1, "Erro ao salvar. Conta Carteira é nulo");
			
			int retorno;
			if(contaCarteira.getCdContaCarteira()==0){
				retorno = ContaCarteiraDAO.insert(contaCarteira, connect);
				contaCarteira.setCdConta(retorno);
			}
			else {
				retorno = ContaCarteiraDAO.update(contaCarteira, connect);
			}
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTACARTEIRA", contaCarteira);
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
	
	public static byte[] getBytesImage(int cdConta, int cdContaCarteira) {
		return getBytesImage(cdConta, cdContaCarteira, null);
	}

	public static byte[] getBytesImage(int cdConta, int cdContaCarteira, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			ContaCarteira carteira = ContaCarteiraDAO.get(cdContaCarteira, cdConta, connection);
			return carteira==null ? null : carteira.getImgLogotipo();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getCarteirasOfConta(int cdConta) {
		return getCarteiraOfConta(cdConta, null);
	}

	public static ResultSetMap getCarteiraOfConta(int cdConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_conta_carteira " +
					                                                     "WHERE cd_conta = "+cdConta).executeQuery());
			while (rsm != null && rsm.next()) {
				ResultSetMap rsmCampoLivre = getCampoLivre(rsm.getString("txt_campo_livre"));
				while(rsmCampoLivre.next())	{
					rsm.setValueToField("TP_CAMPO"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("TP_CAMPO"));
					rsm.setValueToField("NR_INICIO_CAMPO"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("NR_INICIO_CAMPO"));
					rsm.setValueToField("QT_DIGITOS"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("QT_DIGITOS"));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraServices.getCarteiraOfConta: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("CD_CONTA_FINANCEIRA_CARTEIRA")) {
				ItemComparator criterio = criterios.remove(i);
				if (criterio.getValue().indexOf("-") != -1) {
					int cdConta = Integer.parseInt(criterio.getValue().substring(0, criterio.getValue().indexOf("-")));
					int cdCarteira = Integer.parseInt(criterio.getValue().substring(criterio.getValue().indexOf("-") + 1));
					criterios.add(new ItemComparator("A.cd_conta", cdConta + "", ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.cd_conta_carteira", cdCarteira + "", ItemComparator.EQUAL, Types.INTEGER));
				}
				i += 1;
			}
		}
		ResultSetMap rsm = Search.find("SELECT A.*, " +
									   "	   B.nm_conta, B.nr_conta, B.nr_dv, B.vl_limite, " +
									   "	   C.nr_agencia, D.nr_banco, D.nm_banco, D.id_banco " +
							           "FROM adm_conta_carteira A " +
							           "JOIN adm_conta_financeira   B ON (A.cd_conta = B.cd_conta) " +
							           "LEFT OUTER JOIN grl_agencia C ON (B.cd_agencia = C.cd_agencia) " +
							           "LEFT OUTER JOIN grl_banco   D ON (C.cd_banco = D.cd_banco)", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		while (rsm != null && rsm.next()) {
			rsm.getRegister().put("CD_CONTA_FINANCEIRA_CARTEIRA", rsm.getInt("cd_conta") + "-" + rsm.getInt("cd_conta_carteira"));
			rsm.getRegister().put("NM_CONTA_FINANCEIRA_CARTEIRA", rsm.getString("nm_conta") + " - " + rsm.getString("nm_carteira"));
			ResultSetMap rsmCampoLivre = getCampoLivre(rsm.getString("txt_campo_livre"));
			while(rsmCampoLivre.next())	{
				rsm.setValueToField("TP_CAMPO"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("TP_CAMPO"));
				rsm.setValueToField("NR_INICIO_CAMPO"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("NR_INICIO_CAMPO"));
				rsm.setValueToField("QT_DIGITOS"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("QT_DIGITOS"));
			}
		}
		rsm.beforeFirst();
		return rsm;
	}
	
	public static ResultSetMap getCampoLivre(String txtCampoLivre) {
		ResultSetMap rsm = new ResultSetMap();
		try	{
			if (txtCampoLivre==null)
				return rsm;
			StringTokenizer campos = new StringTokenizer(txtCampoLivre, ",", false);
			while(campos.hasMoreTokens())	{
				StringTokenizer valores = new StringTokenizer(campos.nextToken(), ":", false);
				HashMap<String,Object> reg = new HashMap<String,Object>();
				// Tipo de Campo
				try	{
					if (valores.hasMoreTokens())
						reg.put("TP_CAMPO", new Integer(valores.nextToken()));
				}catch(Exception e){};
				// Início do Campo
				try	{
					if (valores.hasMoreTokens())
						reg.put("NR_INICIO_CAMPO", new Integer(valores.nextToken()));
				}catch(Exception e){};
				// Quantidade
				try	{
					if (valores.hasMoreTokens())
						reg.put("QT_DIGITOS", new Integer(valores.nextToken()));
				}catch(Exception e){};
				rsm.addRegister(reg);
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		rsm.beforeFirst();
		return rsm;
	}
	
	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}
	
	public static ResultSetMap getAll(int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, " +
								   "	   B.nm_conta, B.nr_conta, B.nr_dv, B.vl_limite, " +
								   "	   C.nr_agencia, D.nr_banco, D.nm_banco, D.id_banco " +
						           "FROM adm_conta_carteira A " +
						           "JOIN adm_conta_financeira   B ON (A.cd_conta = B.cd_conta AND B.cd_empresa = "+cdEmpresa+") " +
						           "LEFT OUTER JOIN grl_agencia C ON (B.cd_agencia = C.cd_agencia) " +
						           "LEFT OUTER JOIN grl_banco   D ON (C.cd_banco = D.cd_banco)");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm != null && rsm.next()) {
				rsm.getRegister().put("CD_CONTA_FINANCEIRA_CARTEIRA", rsm.getInt("cd_conta") + "-" + rsm.getInt("cd_conta_carteira"));
				rsm.getRegister().put("NM_CONTA_FINANCEIRA_CARTEIRA", rsm.getString("nm_conta") + " - " + rsm.getString("nm_carteira"));
				ResultSetMap rsmCampoLivre = getCampoLivre(rsm.getString("txt_campo_livre"));
				while(rsmCampoLivre.next())	{
					rsm.setValueToField("TP_CAMPO"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("TP_CAMPO"));
					rsm.setValueToField("NR_INICIO_CAMPO"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("NR_INICIO_CAMPO"));
					rsm.setValueToField("QT_DIGITOS"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("QT_DIGITOS"));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getAllByUsuario(int cdEmpresa, int cdUsuario) {
		return getAllByUsuario(cdEmpresa, cdUsuario, null);
	}
	
	public static ResultSetMap getAllByUsuario(int cdEmpresa, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, " +
					"	   B.nm_conta, B.nr_conta, B.nr_dv, B.vl_limite, " +
					"	   C.nr_agencia, D.nr_banco, D.nm_banco, D.id_banco " +
					"FROM adm_conta_carteira A " +
					"JOIN adm_conta_financeira   B ON (A.cd_conta = B.cd_conta AND B.cd_empresa = "+cdEmpresa+") " +
					"LEFT JOIN  seg_usuario_conta_financeira  B2 ON (B2.cd_conta = B.cd_conta"+
					"                                                AND B2.cd_usuario = "+cdUsuario+") "+
					"LEFT OUTER JOIN grl_agencia C ON (B.cd_agencia = C.cd_agencia) " +
					"LEFT OUTER JOIN grl_banco   D ON (C.cd_banco = D.cd_banco)");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm != null && rsm.next()) {
				rsm.getRegister().put("CD_CONTA_FINANCEIRA_CARTEIRA", rsm.getInt("cd_conta") + "-" + rsm.getInt("cd_conta_carteira"));
				rsm.getRegister().put("NM_CONTA_FINANCEIRA_CARTEIRA", rsm.getString("nm_conta") + " - " + rsm.getString("nm_carteira"));
				ResultSetMap rsmCampoLivre = getCampoLivre(rsm.getString("txt_campo_livre"));
				while(rsmCampoLivre.next())	{
					rsm.setValueToField("TP_CAMPO"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("TP_CAMPO"));
					rsm.setValueToField("NR_INICIO_CAMPO"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("NR_INICIO_CAMPO"));
					rsm.setValueToField("QT_DIGITOS"+(rsmCampoLivre.getPosition()+1), rsmCampoLivre.getObject("QT_DIGITOS"));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @see #removeByConta(int)
	 */
	public static Result removeByConta(int cdConta) {
		return removeByConta(cdConta, null);
	}

	/**
	 * Método para remover uma carteira a partir de uma conta financeira
	 * 
	 * @author Luiz Romario Filho
	 * @since 28/08/2014
	 * @param cdConta id da conta financeira
	 * @return result com código e mensagem da transação, code >= 0 ? sucesso : erro 
	 */
	public static Result removeByConta(int cdConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(connect.prepareStatement("DELETE FROM adm_conta_carteira WHERE cd_conta="+cdConta).execute()) {
				retorno = 1;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta conta carteira está vinculada a outros registros e não pode ser excluída!");
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Conta carteira excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir conta carteira!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeContaCarteira(int cdContaCarteira, int cdConta) {
		return removeContaCarteira(cdContaCarteira, cdConta, null);
	}
	
	public static Result removeContaCarteira(int cdContaCarteira, int cdConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			int code = ContaCarteiraDAO.delete(cdContaCarteira, cdConta);
			if( code > 0) {
				retorno = code;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta conta carteira está vinculada a outros registros e não pode ser excluída!");
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Conta carteira excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir conta carteira!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
}