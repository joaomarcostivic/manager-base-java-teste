package com.tivic.manager.grl;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.tivic.manager.acd.InstituicaoEducacensoServices;
import com.tivic.manager.acd.OcorrenciaParametro;
import com.tivic.manager.acd.OcorrenciaParametroServices;
import com.tivic.manager.adm.ContaPagarServices;
import com.tivic.manager.adm.ContaReceberServices;
import com.tivic.manager.adm.FormaPagamentoServices;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.prc.ProcessoServices;
import com.tivic.manager.seg.AcaoServices;
import com.tivic.manager.seg.Modulo;
import com.tivic.manager.seg.ModuloServices;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.RequestUtilities;
import sol.util.Result;

public class ParametroServices {

	public static final int TURNO_MATUTINO   = 1;
	public static final int TURNO_VESPERTINO = 2;
	public static final int TURNO_NOTURNO    = 3;

	/* tipos de dado dos parametros */
	public static final int NUMERICO = 0;
	public static final int STRING   = 1;
	public static final int DATA     = 2;
	public static final int LOGICO   = 3;
	public static final int IMAGEM   = 4;
	public static final int FILE     = 5;

	public static String[] tiposDados = {"Numérico","String","Data/Horário","Lógico (Sim/não)","Binário (Imagens, Arquivos, etc.)"};

	/* tipos de apresentacao dos parametros */
	public static final int SINGLE_TEXT 				= 0;
	public static final int MULTI_TEXT					= 1;
	public static final int CHECK_BOX					= 2;
	public static final int COMBO_BOX					= 3;
	public static final int LIST_BOX					= 4;
	public static final int LIST_BOX_MULTI_SELECTION 	= 5;
	public static final int RADIO_BOX					= 6;
	public static final int FILTER						= 7;
	public static final int DUAL_TEXT					= 8;
	public static final int PASSWORD					= 9;

	public static final String[] tiposApresentacao = {"Texto (singleline)", "Texto (multiline)", "Check Box", "Combo Box", "Lista (seleção simples)",
			"Lista (multipla seleção)", "Radio Box", "Filtro", "Dual Text", "Texto (password)"};

	/* tipos de parametros */
	public static final int TP_GERAL   = 0;
	public static final int TP_EMPRESA = 1;
	public static final int TP_PESSOA  = 2;

	public static final String[] tiposParametro = {"Geral", "Aplicável á Empresa", "Aplicável á Pessoa"};

	/* tipos de niveis de acesso dos parametros */
	public static final int NIVEL_ACESSO_OPERADOR      = 0;
	public static final int NIVEL_ACESSO_ADMINISTRADOR = 1;
	public static final int NIVEL_ACESSO_SISTEMA       = 2;

	public static final String[] niveisAcesso = {"Operador", "Administrador", "Sistema"};
	
    /* Grupos de Parâmetro (MOB) */
    public static final int NAO_CATEGORIZADO		= 0;
    public static final int DADOS_ORGAO				= 1;
    public static final int OUTRAS_INFORMACOES		= 2;
    public static final int MARCAS_CONFIG			= 3;
	public static final int MOBILIDADE				= 4;
    public static final int GERADORES_SEQUENCIAIS	= 5;
    public static final int PROTOCOLOS				= 6;
	public static final int INCONSISTENCIAS			= 7;
    public static final int NAI						= 8;
    public static final int NIP						= 9;
    public static final int FINANCEIRO				= 10;
    public static final int PORTAL_CIDADAO			= 11;
    public static final int TALONARIO_ELETRONICO    = 12;
    
    public static final String[] gruposParametro = {"Dados do Órgão", "Outras Informações", "Marcas e Config. Impressos", "Mobilidade", "Cobrança/RENAINF", "Protocolos", "Inconsistências", "NAI", "NIP", "Portal", "Financeiro", "Talonário Eletrônico"};
    
	
	private static ParametroOpcaoServices _service = new ParametroOpcaoServices();

	public static int init()	{
		try{
			initAcd();
			initAlm();
			initAdm();
			initBlb();
			initCae();
			initFac();
			initVendas();
			initAgenda();
			initGrl();
			initMcr();
			initMob();
			initSrh();
			initFta();
			initCrm();
			initJur();
			initPtc();
			initCrt();
			initOrd();
			initEgov();
			initPcb();
			initFsc();
			initSeg();
			initGed();
			initMsg();
			
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}
	
	public static List<Parametro> getNaoCategorizados(String[] categorizados) {
		return getNaoCategorizados(categorizados, null);
	}
	
	public static List<Parametro> getNaoCategorizados(String[] categorizados, Connection connect) {
		boolean isConnNull = (connect == null);
		
		if(isConnNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_PARAMETRO WHERE CD_MODULO = 27 AND NM_PARAMETRO NOT IN (" + mountStringFromArray(categorizados) + ")");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			List<Parametro> params = new ResultSetMapper<Parametro>(rsm, Parametro.class).toList();
			
			for(Parametro param : params) {
				
				List<ParametroValor> valores = new ResultSetMapper<ParametroValor>(getValoresOfParametro(param.getCdParametro()), ParametroValor.class).toList();
				
				if(valores.size() > 0) {
					ParametroValor[] valor = new ParametroValor[valores.size()];
					
					for(int i = 0; i < valores.size() ; i++) {
						valor[i] = valores.get(i);
					}
				
					param.setValores(valor);
				}
			}
			
			return params;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static List<Parametro> getParametrosFromCods(int[] cods) {
		return getParametrosFromCods(cods, null);
	}
	
	public static List<Parametro> getParametrosFromCods(int[] cods, Connection connect) {
		boolean isConnNull = (connect == null);
		
		if(isConnNull)
			connect = Conexao.conectar();
		
		try {
			List<Parametro> params = new ArrayList<>();
			
			params.addAll(getParametrosFromCodsByTpDado(cods, IMAGEM, connect));
			params.addAll(getParametrosFromCodsByApresentacao(cods, SINGLE_TEXT, connect));
			params.addAll(getParametrosFromCodsByApresentacao(cods, LIST_BOX, connect));
			params.addAll(getParametrosFromCodsByApresentacao(cods, CHECK_BOX, connect));
			params.addAll(getParametrosFromCodsByApresentacao(cods, MULTI_TEXT, connect));
			
			return params;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static List<Parametro> getParametrosFromCodsByApresentacao(int[] cods, int tpApresentacao, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_PARAMETRO WHERE CD_MODULO = 27 AND CD_PARAMETRO IN (" + mountStringFromIntArray(cods) + ")"
					+ "AND TP_APRESENTACAO = ? AND TP_DADO <> ? ORDER BY NM_PARAMETRO ASC");
			pstmt.setInt(1, tpApresentacao);
			pstmt.setInt(2, IMAGEM);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			List<Parametro> params = new ResultSetMapper<Parametro>(rsm, Parametro.class).toList();
			
			for(Parametro param : params) {				
				List<ParametroValor> valores = new ResultSetMapper<ParametroValor>(getValoresOfParametro(param.getCdParametro()), ParametroValor.class).toList();
				
				if(valores.size() > 0) {
					ParametroValor[] valor = new ParametroValor[valores.size()];
					
					for(int i = 0; i < valores.size() ; i++) {
						valor[i] = valores.get(i);
					}
				
					param.setValores(valor);
				}
			}
			
			return params;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	private static List<Parametro> getParametrosFromCodsByTpDado(int[] cods, int tpDado, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_PARAMETRO WHERE CD_MODULO = 27 AND CD_PARAMETRO IN (" + mountStringFromIntArray(cods) + ")"
					+ "AND TP_DADO = ? ORDER BY NM_PARAMETRO ASC");
			pstmt.setInt(1, tpDado);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			List<Parametro> params = new ResultSetMapper<Parametro>(rsm, Parametro.class).toList();
			
			for(Parametro param : params) {
				
				List<ParametroValor> valores = new ResultSetMapper<ParametroValor>(getValoresOfParametro(param.getCdParametro()), ParametroValor.class).toList();
				
				if(valores.size() > 0) {
					ParametroValor[] valor = new ParametroValor[valores.size()];
					
					for(int i = 0; i < valores.size() ; i++) {
						valor[i] = valores.get(i);
					}
				
					param.setValores(valor);
				}
			}
			
			return params;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	private static String mountStringFromArray(String[] array) {
		String finalValue = "";
		for(String value: array) {
			finalValue += ("\'" + value + "\'" + ",");
		}
		return finalValue.substring(0, finalValue.length() - 1);
	}
	
	private static String mountStringFromIntArray(int[] array) {
		String finalValue = "";
		for(int value: array) {
			finalValue += ("\'" + value + "\'" + ",");
		}
		return finalValue.substring(0, finalValue.length() - 1);
	}

	public static int initFlex()	{
		try{
			if(Util.getConfManager().getIdOfDbUsed().equals("FB"))
				return init();
			else
				return Util.init();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}


	public static float getValorOfParametroAsFloat(String key, float defaultValue) {
		return getValorOfParametroAsFloat(key, defaultValue, 0, null);
	}

	public static float getValorOfParametroAsFloat(String key, float defaultValue, int cdEmpresa) {
		return getValorOfParametroAsFloat(key, defaultValue, cdEmpresa, null);
	}

	public static float getValorOfParametroAsFloat(String key, float defaultValue, int cdEmpresa, Connection con) {
		try {
			String value = getValorOfParametro(key, cdEmpresa, con);
			value = value.replaceAll(",", ".");
			return Float.parseFloat(value);
		}
		catch(Exception e) {
			return defaultValue;
		}
	}

	public static int getValorOfParametroAsInteger(String key, int defaultValue) {
		return getValorOfParametroAsInteger(key, defaultValue, 0, null);
	}

	public static int getValorOfParametroAsInteger(String key, int defaultValue, int cdEmpresa) {
		return getValorOfParametroAsInteger(key, defaultValue, cdEmpresa, null);
	}

	public static int getValorOfParametroAsInteger(String key, int defaultValue, int cdEmpresa, Connection con) {
		try {
			//o metodo abaixo nao passa valor default, logo, retorna uma string 'null' quando a consulta nao traz resultado
			//e o Interger.parseInt nao consegue tratar.
			String vlParametro = getValorOfParametro(key, cdEmpresa, con);
			if (vlParametro==null)
				return defaultValue;
			else
				return Integer.parseInt(vlParametro);
		}
		catch(Exception e) {
			Util.registerLog(e);
			return defaultValue;
		}
	}

	public static int getValorOfParametroAsIntegerByPessoa(String key, int defaultValue, int cdPessoa){
		return Integer.parseInt(getValorOfParametro(key, String.valueOf(defaultValue), 0, cdPessoa, null));
	}

	public static byte[] getValorOfParametroAsBytes(String key) {
		return getValorOfParametroAsBytes(key, null);
	}

	public static byte[] getValorOfParametroAsBytes(String key, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			Parametro parametro = getByName(key, connection);
			if (parametro==null)
				return null;
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_parametro_valor " +
					"WHERE cd_parametro = ? " +
					"  AND cd_empresa IS NULL " +
					"  AND cd_pessoa IS NULL");
			pstmt.setInt(1, parametro.getCdParametro());
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next())
				return null;
			else if (parametro.getTpDado()!=IMAGEM && parametro.getTpDado()!=FILE)
				return rs.getString("vl_inicial")==null ? null : rs.getString("vl_inicial").getBytes();
			else
				return rs.getBytes("blb_valor");
		}
		catch(Exception e) {
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static byte[] getValorOfParametroAsBytes(int cdParametro) {
		return getValorOfParametroAsBytes(cdParametro, null);
	}

	public static byte[] getValorOfParametroAsBytes(int cdParametro, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			Parametro parametro = ParametroDAO.get(cdParametro, connection);
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_parametro_valor " +
					"WHERE cd_parametro = ? " +
					"  AND cd_empresa IS NULL " +
					"  AND cd_pessoa IS NULL");
			pstmt.setInt(1, cdParametro);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next())
				return null;
			else if (parametro.getTpDado()!=IMAGEM && parametro.getTpDado()!=FILE)
				return rs.getString("vl_inicial")==null ? null : rs.getString("vl_inicial").getBytes();
			else
				return rs.getBytes("blb_valor");
		}
		catch(Exception e) {
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static String getTypeOfImage(String key) {
		return getTypeOfImage(key, null);
	}

	public static String getTypeOfImage(String key, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			Parametro parametro = getByName(key, connection);
			if (parametro==null)
				return null;
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM grl_parametro_valor A" +
					"WHERE cd_parametro = ? " +
					"  AND cd_empresa IS NULL " +
					"  AND cd_pessoa IS NULL");
			pstmt.setInt(1, parametro.getCdParametro());
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next())
				return null;
			else if (parametro.getTpDado()!=IMAGEM)
				return null;
			else
				return rs.getString("vl_inicial");
		}
		catch(Exception e) {
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
	 * Método definitivo para busca de valores de parametros por vários IDs. 
	 * Todos os outros serão depreciados ou farão chamadas e este método.
	 * @param ids
	 * @return
	 */
	public static HashMap<String, Object> getParamsValues(String[] ids){
		return getParamsValues(ids, 0, null);
	}

	/**
	 * Método definitivo para busca de valores de parametros por vários IDs. 
	 * Todos os outros serão depreciados ou farão chamadas e este método.
	 * @param ids
	 * @param cdEmpresa
	 * @return
	 */
	public static HashMap<String, Object> getParamsValues(String[] ids, int cdEmpresa){
		return getParamsValues(ids, cdEmpresa, null);
	}

	/**
	 * Método definitivo para busca de valores de parametros por vários IDs. 
	 * Todos os outros serão depreciados ou farão chamadas e este método.
	 * @param ids
	 * @param cdEmpresa
	 * @param connect
	 * @return um mapa de objetos com os tipos e valores dos parametros
	 */
	public static HashMap<String, Object> getParamsValues(String[] ids, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			HashMap<String, Object> values = new HashMap<String, Object>();
			if(ids!=null) {
				for (int i = 0; i < ids.length; i++) {

					Parametro p = ParametroServices.getByName(ids[i], connect);

					if(p==null) //parametro nao existe
						continue;

					int cdEmpresaParam = p.getTpParametro() == TP_EMPRESA ? cdEmpresa : 0;

					if(p.getTpApresentacao() != LIST_BOX_MULTI_SELECTION) {
						switch(p.getTpDado()) {
						case NUMERICO:
							values.put(ids[i], getValorOfParametroAsInteger(ids[i], 0, cdEmpresaParam, connect));
							break;
						case STRING:
							values.put(ids[i], getValorOfParametro(ids[i], "", cdEmpresaParam, connect));
							break;
						case LOGICO:
							values.put(ids[i], getValorOfParametroAsInteger(ids[i], 0, cdEmpresaParam, connect) == 1);
							break;
						case IMAGEM:
							values.put(ids[i], getValorOfParametroAsBytes(ids[i], connect));
							break;
						case FILE:
							values.put(ids[i], getValorOfParametroAsBytes(ids[i], connect));
							break;
						default:
							values.put(ids[i], getValorOfParametro(ids[i], "", cdEmpresaParam, connect));
						}
					}
					else {
						ArrayList<Object> v = getValoresOfParametroAsArrayList(ids[i], cdEmpresaParam, connect);
						if(v!=null)
							values.put(ids[i], v.toArray());
					}
				}
			}

			return values;

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

	public static ArrayList<Object> getValoresOfParametroAsArrayList(String nmParametro) {
		return getValoresOfParametroAsArrayList(nmParametro, 0, null);
	}

	public static ArrayList<Object> getValoresOfParametroAsArrayList(String nmParametro, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			Parametro p = ParametroServices.getByName(nmParametro, connect);

			if(p==null)
				return null;

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_PARAMETRO_VALOR WHERE CD_PARAMETRO = ? "+
					(cdEmpresa > 0 ? "AND CD_EMPRESA = "+cdEmpresa : ""));

			pstmt.setInt(1, p.getCdParametro());

			ResultSet rs = pstmt.executeQuery();

			ArrayList<Object> values = new ArrayList<>();
			while(rs.next()) {
				switch(p.getTpDado()) {
				case NUMERICO:
					values.add(Integer.parseInt(rs.getString("vl_inicial")));
					break;
				case STRING:
					values.add(rs.getString("vl_inicial"));
					break;
				case LOGICO:
					values.add(Integer.parseInt(rs.getString("vl_inicial")) == 1);
					break;
				default:
					values.add(rs.getString("vl_inicial"));
				}
			}
			return values;

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

	public static HashMap<String, String> getValuesEmpresa(String[] keysEmpresa, int cdEmpresa) {
		return getValues(null, keysEmpresa, cdEmpresa, null);
	}

	public static HashMap<String, String> getValues(String[] keys) {
		return getValues(keys, null, 0, null);
	}

	public static HashMap<String, String> getValues(String[] keysGeral, String[] keysEmpresa, int cdEmpresa) {
		return getValues(keysGeral, keysEmpresa, cdEmpresa, null);
	}

	public static HashMap<String, String> getValues(String[] keys, Connection connect){
		return getValues(keys, null);
	}

	public static HashMap<String, String> getValues(String[] keysGeral, String[] keysEmpresa, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			HashMap<String, String> values = new HashMap<String, String>();
			if(keysGeral!=null) {
				for (int i = 0; i < keysGeral.length; i++) {
					values.put(keysGeral[i], getValorOfParametro(keysGeral[i], connect));
				}
			}

			if(keysEmpresa!=null && cdEmpresa>0) {
				for (int i = 0; i < keysEmpresa.length; i++) {
					values.put(keysEmpresa[i], getValorOfParametro(keysEmpresa[i], cdEmpresa, connect));
				}	
			}

			return values;

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
	
	public static Result getValorParametro(int cdParametro, String vlInicial) {
		return getValorParametro(cdParametro, vlInicial, null);
	}
	
	public static Result getValorParametro(int cdParametro, String vlInicial, Connection connect) {
		try {
			if(connect == null)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_PARAMETRO_VALOR WHERE CD_PARAMETRO = ? AND VL_INICIAL = ? OR VL_INICIAL = ''");
			pstmt.setInt(1, cdParametro);
			pstmt.setString(2, vlInicial);
			
			ResultSet rs = pstmt.executeQuery();
			ResultSetMap rsm = new ResultSetMap(rs);
			
			if(!rsm.next())
				return new Result(0, "Não foi encontrado um valor.");
			
			return new Result(1, "Valor já inserido");
		}catch(Exception e) {
			return new Result(-1, e.getMessage());
		}
	}

	public static String getValorOfParametroAsString(String key, String defaultValue) {
		return getValorOfParametro(key, defaultValue, null);
	}

	public static String getValorOfParametro(String key) {
		return getValorOfParametro(key, null);
	}

	public static String getValorOfParametro(String key, Connection connect){
		return getValorOfParametro(key, null, connect);
	}

	public static String getValorOfParametro(String key, String defaultValue, Connection connect){
		return getValorOfParametro(key, defaultValue, 0, connect);
	}

	public static String getValorOfParametro(String key, int cdEmpresa, Connection connect){
		return getValorOfParametro(key, null, cdEmpresa, connect);
	}

	public static String getValorOfParametro(String key, String defaultValue, int cdEmpresa){
		return getValorOfParametro(key, defaultValue, cdEmpresa, null);
	}

	public static String getValorOfParametro(String key, int cdEmpresa){
		return getValorOfParametro(key, null, cdEmpresa, null);
	}

	public static String getValorOfParametro(String key, String defaultValue, int cdEmpresa, Connection connect){
		return getValorOfParametro(key, defaultValue, cdEmpresa, 0, connect);
	}
	public static String getValorOfParametro(String key, String defaultValue, int cdEmpresa, int cdPessoa){
		return getValorOfParametro(key, defaultValue, cdEmpresa, 0, null);
	}

	public static String getValorOfParametro(String key, String defaultValue, int cdEmpresa, int cdPessoa, Connection connect){
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); //Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		boolean isConnectionNull = connect==null;

		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			if (lgBaseAntiga)
			{
				PreparedStatement pstmt = connect.prepareStatement("SELECT "+  key + " FROM parametro");
				ResultSet rs = pstmt.executeQuery();
				
				String dsValue = "";
				byte[] blbValor = null;
				
				if (rs.next())
				{
					if (key.equals("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS") || 
							key.equals("img_logo_orgao") ||
							key.equals("img_logo_departamento"))
					{
						blbValor = rs.getBytes(key);
						dsValue = new String(blbValor, StandardCharsets.ISO_8859_1);
					}
					else
					{
						dsValue = rs.getString(key);
					}
				}
				
				if(dsValue==null || dsValue.trim().equals(""))
					return defaultValue;
				else
					return dsValue;
			}
			else
			{
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.tp_apresentacao, C.vl_real " +
							"FROM GRL_PARAMETRO_VALOR A " +
							"JOIN GRL_PARAMETRO B ON (A.cd_parametro = B.cd_parametro) " +
							"LEFT OUTER JOIN grl_parametro_opcao C ON (A.cd_parametro = C.cd_parametro AND A.cd_opcao = C.cd_opcao) " +
							"WHERE A.CD_PARAMETRO = B.CD_PARAMETRO " +
							"  AND B.NM_PARAMETRO=? " +
							(cdEmpresa > 0 ? " AND A.CD_EMPRESA = "+cdEmpresa : " AND A.CD_EMPRESA IS NULL") + 
							(cdPessoa > 0 ? " AND A.CD_PESSOA = "+cdPessoa : " AND A.CD_PESSOA IS NULL"));
			pstmt.setString(1, key);
			ResultSet rs = pstmt.executeQuery();
			String dsValue = "";
			byte[] blbValor = null;
			
			if(rs.next()) {
				
				blbValor = rs.getBytes("blb_valor");
				dsValue = rs.getInt("tp_apresentacao")==COMBO_BOX ? rs.getString("vl_real") : rs.getString("VL_INICIAL");
				
				if(rs.getInt("tp_apresentacao")==MULTI_TEXT && blbValor!=null) 
					dsValue = new String(blbValor, StandardCharsets.ISO_8859_1);
			}
			
			if(dsValue==null || dsValue.trim().equals(""))
				return defaultValue;
			else
				return dsValue;
			}
		}
		catch(Exception e) {
			Util.registerLog(e);
			// CODATO			
			//e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getNextValorOfParametro(String key) {
		return getNextValorOfParametro(key, null);
	}

	public static String getNextValorOfParametro(String key, Connection connect){
		return getNextValorOfParametro(key, null, connect);
	}

	public static String getNextValorOfParametro(String key, String defaultValue, Connection connect){
		return getNextValorOfParametro(key, defaultValue, 0, connect);
	}

	public static String getNextValorOfParametro(String key, int cdEmpresa, Connection connect){
		return getNextValorOfParametro(key, null, cdEmpresa, connect);
	}

	public static String getNextValorOfParametro(String key, String defaultValue, int cdEmpresa){
		return getNextValorOfParametro(key, defaultValue, cdEmpresa, null);
	}

	public static String getNextValorOfParametro(String key, int cdEmpresa){
		return getNextValorOfParametro(key, null, cdEmpresa, null);
	}

	public static String getNextValorOfParametro(String key, String defaultValue, int cdEmpresa, Connection connect){
		return getNextValorOfParametro(key, defaultValue, cdEmpresa, 0, connect);
	}
	public static String getNextValorOfParametro(String key, String defaultValue, int cdEmpresa, int cdPessoa){
		return getNextValorOfParametro(key, defaultValue, cdEmpresa, 0, null);
	}

	public static String getNextValorOfParametro(String key, String defaultValue, int cdEmpresa, int cdPessoa, Connection connect){
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); //Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		boolean isConnectionNull = connect==null;

		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			if (lgBaseAntiga)
			{
				PreparedStatement pstmt = connect.prepareStatement("SELECT "+  key + " FROM parametro");
				ResultSet rs = pstmt.executeQuery();
				
				String dsValue = "";
				byte[] blbValor = null;
				
				if (rs.next())
				{
					if (key.equals("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS") || 
							key.equals("img_logo_orgao") ||
							key.equals("img_logo_departamento"))
					{
						blbValor = rs.getBytes(key);
						dsValue = new String(blbValor, StandardCharsets.ISO_8859_1);
					}
					else
					{
						dsValue = rs.getString(key);
					}
				}
				
				if(dsValue==null || dsValue.trim().equals(""))
					return defaultValue;
				else 
					return dsValue;
			}
			else
			{
				PreparedStatement pstmt = connect.prepareStatement(
						"SELECT A.*, B.tp_apresentacao, C.vl_real " +
								"FROM GRL_PARAMETRO_VALOR A " +
								"JOIN GRL_PARAMETRO B ON (A.cd_parametro = B.cd_parametro) " +
								"LEFT OUTER JOIN grl_parametro_opcao C ON (A.cd_parametro = C.cd_parametro AND A.cd_opcao = C.cd_opcao) " +
								"WHERE A.CD_PARAMETRO = B.CD_PARAMETRO " +
								"  AND B.NM_PARAMETRO=? " +
								(cdEmpresa > 0 ? " AND A.CD_EMPRESA = "+cdEmpresa : " AND A.CD_EMPRESA IS NULL") + 
								(cdPessoa > 0 ? " AND A.CD_PESSOA = "+cdPessoa : " AND A.CD_PESSOA IS NULL"));
				pstmt.setString(1, key);
				ResultSet rs = pstmt.executeQuery();
				String dsValue = "";
				byte[] blbValor = null;
				
				if(rs.next()) {
					
					blbValor = rs.getBytes("blb_valor");
					dsValue = rs.getInt("tp_apresentacao")==COMBO_BOX ? rs.getString("vl_real") : rs.getString("VL_INICIAL");
					
					if(rs.getInt("tp_apresentacao")==MULTI_TEXT && blbValor!=null) 
						dsValue = new String(blbValor, StandardCharsets.ISO_8859_1);
				}
				
				if(dsValue==null || dsValue.trim().equals("")) {
					ArrayList<ParametroValor> params = new ArrayList<ParametroValor>();
					ParametroValor valor = ParametroValorDAO.get(rs.getInt("cd_parametro"), rs.getInt("cd_valor"));
					valor.setVlInicial("1");
					params.add(valor);
					return valor.getVlInicial();
				} else {
					String novoValor = String.valueOf(Integer.parseInt(dsValue) + 1);
					ArrayList<ParametroValor> params = new ArrayList<ParametroValor>();
					ParametroValor valor = ParametroValorDAO.get(rs.getInt("cd_parametro"), rs.getInt("cd_valor"));
					valor.setVlInicial(novoValor);
					params.add(valor);
					setValoresOfParametro(valor.getCdParametro(), 0, 0, params);
					return String.valueOf(valor.getVlInicial());
				}
			}
		}
		catch(Exception e) {
			Util.registerLog(e);
			// CODATO			
			//e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setValoresOfParametros(int[] cdsParametros, Object[] valuesOfParametros) {
		return setValoresOfParametros(cdsParametros, valuesOfParametros, null);
	}

	public static int setValoresOfParametros(int[] cdsParametros, Object[] valuesOfParametros, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			for (int i=0; i<cdsParametros.length; i++) {
				int cdParametro = cdsParametros[i];
				Object[] values = (Object[])valuesOfParametros[i];
				if (setValoresOfParametro(cdParametro, values, connection)<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.setValoresOfParametros: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setValoresOfParametros(ServletRequest request) {
		return setValoresOfParametros(request, 0, 0, null);
	}

	public static int setValoresOfParametros(ServletRequest request, int cdEmpresa, int cdPessoa) {
		return setValoresOfParametros(request, cdEmpresa, cdPessoa, null);
	}

	public static int setValoresOfParametros(ServletRequest request, int cdEmpresa, int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int countParametros = RequestUtilities.getAsInteger(request, "countParams", 0);
			for (int i=0; i<countParametros; i++) {
				int cdParametro = RequestUtilities.getAsInteger(request, "cdParametro_" + i, 0);
				if (cdParametro > 0) {
					Parametro parametro = ParametroDAO.get(cdParametro, connection);
					if (parametro.getTpParametro() == TP_EMPRESA && cdEmpresa <= 0)
						continue;
					int cdEmpresaTemp = parametro.getTpParametro()==TP_EMPRESA ? cdEmpresa : 0;
					if (parametro.getTpParametro() == TP_PESSOA && cdPessoa <= 0)
						continue;
					int cdPessoaTemp = parametro.getTpParametro()==TP_PESSOA ? cdPessoa : 0;
					ArrayList<ParametroValor> valores = new ArrayList<ParametroValor>();
					if (parametro.getTpApresentacao()==LIST_BOX_MULTI_SELECTION) {
						for (int j=0; request.getParameter("cdParametroOpcao_" + i + "_" + j)!=null; j++) {
							valores.add(new ParametroValor(cdParametro,
									0 /*cdValor*/,
									RequestUtilities.getAsInteger(request, "cdParametroOpcao_" + i + "_" + j, 0) /*cdOpcao*/,
									cdEmpresaTemp,
									cdPessoaTemp,
									null /*blbValor*/,
									"" /*vlInicial*/,
									"" /*vlFinal*/));
						}
					}
					else
						for (int j=0; request.getParameter("cdParametroValor_" + i + "_" + j)!=null; j++) {
							int cdOpcao = parametro.getTpApresentacao()==COMBO_BOX && RequestUtilities.getAsInteger(request, "cdParametroValor_" + i + "_" + j, 0) > 0 ?
									RequestUtilities.getAsInteger(request, "cdParametroValor_" + i + "_" + j, 0) : 0;
									String vlInicial = parametro.getTpApresentacao()!=COMBO_BOX && parametro.getTpApresentacao()!=MULTI_TEXT &&
											parametro.getTpDado()!=IMAGEM && parametro.getTpDado()!=FILE ?
													(parametro.getTpApresentacao()==DUAL_TEXT ? RequestUtilities.getAsString(request, "cdParametroValor_" + i + "_" + j + "_0", "") :
														RequestUtilities.getAsString(request, "cdParametroValor_" + i + "_" + j, "")) : null;
													String vlFinal = parametro.getTpApresentacao()==DUAL_TEXT ? RequestUtilities.getAsString(request, "cdParametroValor_" + i + "_" + j + "_1", "") : null;
													byte[] blbValor = parametro.getTpDado()==IMAGEM || parametro.getTpDado()==FILE && request instanceof HttpServletRequest &&
															((HttpServletRequest)request).getSession().getAttribute(RequestUtilities.getAsString(request, "cdParametroValor_" + i + "_" + j, "")) instanceof byte[] ?
																	(byte[])((HttpServletRequest)request).getSession().getAttribute(RequestUtilities.getAsString(request, "cdParametroValor_" + i + "_" + j, "")) :
																		parametro.getTpDado()==STRING && parametro.getTpApresentacao()==MULTI_TEXT ?
																				RequestUtilities.getAsString(request, "cdParametroValor_" + i + "_" + j, "").getBytes() : null;
																				valores.add(new ParametroValor(cdParametro, 0, cdOpcao, cdEmpresaTemp, cdPessoaTemp, blbValor, vlInicial, vlFinal));
						}
					if (setValoresOfParametro(cdParametro,cdEmpresaTemp, cdPessoaTemp, valores, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.setValoresOfParametros: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setValoresOfParametro(int cdParametro, Object[] values) {
		return setValoresOfParametro(cdParametro, values, null);
	}

	public static int setValoresOfParametro(int cdParametro, Object[] values, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			Parametro parametro = ParametroDAO.get(cdParametro, connection);
			if (parametro==null)
				return -1;

			PreparedStatement pstmt = connection.prepareStatement("DELETE " +
					"FROM grl_parametro_valor " +
					"WHERE cd_parametro = ? " +
					"  AND cd_empresa IS NULL " +
					"  AND cd_pessoa IS NULL");
			pstmt.setInt(1, cdParametro);
			pstmt.execute();

			for (int i=0; values!=null && i<values.length; i++) {
				if (values[i] == null)
					continue;
				int cdOpcao = parametro.getTpApresentacao()==COMBO_BOX && ((Integer)values[i]).intValue() > 0 ? ((Integer)values[i]).intValue() : 0;
				String vlInicial = parametro.getTpApresentacao()!=COMBO_BOX && parametro.getTpDado()!=IMAGEM && parametro.getTpDado()!=FILE ?
						(parametro.getTpApresentacao()==DUAL_TEXT ? (((Object[])values[i])[0]).toString() : values[i].toString()) : null;
						String vlFinal = parametro.getTpApresentacao()==DUAL_TEXT ? (((Object[])values[i])[1]).toString() : null;
						ParametroValor valor = new ParametroValor(cdParametro, 0, cdOpcao, 0, 0,
								parametro.getTpDado()==IMAGEM || parametro.getTpDado()==FILE ? (byte[])(values[i]) : null, vlInicial, vlFinal);
						if (ParametroValorDAO.insert(valor, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.setValoresOfParametro: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setValoresOfParametro(int cdParametro, int cdEmpresa, int cdPessoa, ArrayList<ParametroValor> values) {
		return setValoresOfParametro(cdParametro, cdEmpresa, cdPessoa, (ArrayList<ParametroValor>)values, null);
	}

	public static int setValoresOfParametro(int cdParametro,int cdEmpresa, int cdPessoa, ArrayList<ParametroValor> values, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			System.out.println(cdParametro);

			PreparedStatement pstmt = connection.prepareStatement("DELETE " +
					"FROM grl_parametro_valor " +
					"WHERE cd_parametro = ? " +
					(cdEmpresa > 0 ? " AND cd_empresa = " + cdEmpresa : " AND cd_empresa IS NULL ") +
					(cdPessoa > 0 ? " AND cd_pessoa = " + cdPessoa : " AND cd_pessoa IS NULL"));
			pstmt.setInt(1, cdParametro);
			pstmt.execute();

			for (int i=0; values!=null && i<values.size(); i++) {
				if (values.get(i) == null)
					continue;
				if (ParametroValorDAO.insert(values.get(i), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getValoresOfParametro(String key) {
		ResultSetMap rsm = getValoresOfParametro(key, null);
		return rsm;
	}

	public static ResultSetMap getValoresOfParametro(String key, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_parametro", "nm_parametro", key, ItemComparator.EQUAL, Types.VARCHAR, true));
			ResultSetMap rsmParametros = ParametroDAO.find(criterios, connection);
			int cdParametro = rsmParametros==null || !rsmParametros.next() ? 0 : rsmParametros.getInt("cd_parametro");
			return cdParametro<=0 ? null : getValoresOfParametro(cdParametro, 0 /*cdEmpresa*/, 0 /*cdPessoa*/, connection);
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

	public static ResultSetMap getValoresOfParametro(int cdParametro){
		return getValoresOfParametro(cdParametro, 0, 0, null);
	}

	public static ResultSetMap getValoresOfParametro(int cdParametro, int cdEmpresa, int cdPessoa){
		return getValoresOfParametro(cdParametro, cdEmpresa, cdPessoa, null);
	}

	public static ResultSetMap getValoresOfParametro(int cdParametro, int cdEmpresa, int cdPessoa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			//System.out.println("cdParametro: "+ cdParametro+"\ncdEmpresa: "+ cdEmpresa+"\ncdPessoa: "+ cdPessoa);

			Parametro parametro = ParametroDAO.get(cdParametro, connection);
			if(parametro==null)
				return null;

			int tpParametro = parametro.getTpParametro();

			String sql = "SELECT A.*, B.TP_APRESENTACAO, B.txt_url_filtro, C.vl_real, C.vl_apresentacao " +
					"FROM GRL_PARAMETRO B, GRL_PARAMETRO_VALOR A " +
					"LEFT OUTER JOIN grl_parametro_opcao C ON (A.cd_parametro = C.cd_parametro AND " +
					"										   A.cd_opcao = C.cd_opcao) " +
					"WHERE A.CD_PARAMETRO = B.CD_PARAMETRO " +
					"  AND A.CD_PARAMETRO = ? " +
					(tpParametro == TP_PESSOA && cdPessoa>0 ? " AND A.CD_PESSOA = ? " : "  AND A.CD_PESSOA IS NULL ") +
					(tpParametro == TP_EMPRESA && cdEmpresa>0 ? " AND A.CD_EMPRESA = ? " : "  AND A.CD_EMPRESA IS NULL ");

			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, cdParametro);
			int i = 2;
			if (tpParametro == TP_PESSOA && cdPessoa > 0)
				pstmt.setInt(i++, cdPessoa);
			if (tpParametro == TP_EMPRESA && cdEmpresa > 0)
				pstmt.setInt(i++, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			String nmClassDAO = null;
			String dsField = null;
			String cdField = null;
			Class<?> classDAO = null;
			Method method = null;
			String methodStr = null;
			while (rsm!=null && rsm.next() && rsm.getInt("TP_APRESENTACAO")==FILTER) {
				if (rsm.getPointer() == 0) {
					String txtUrlFiltro = rsm.getString("txt_url_filtro");
					if (txtUrlFiltro != null && txtUrlFiltro.length()>=2)
						txtUrlFiltro = txtUrlFiltro.substring(1, txtUrlFiltro.length() - 1);
					String[] tokens = Util.getTokens(txtUrlFiltro, ',', false);
					for (i=0; tokens!=null && i<tokens.length; i++){
						while (tokens[i].length()>0 && tokens[i].charAt(0) == '\n')
							tokens[i] = tokens[i].length()==1 ? "" : tokens[i].substring(1);
						tokens[i] = tokens[i].trim();
						if (tokens[i].length()>=10 && tokens[i].substring(0, 10).equalsIgnoreCase("className:")) {
							nmClassDAO = tokens[i].length()==10 ? "" : tokens[i].substring(10);
							nmClassDAO = nmClassDAO.replaceFirst("dotManager", "com.tivic.manager");
							nmClassDAO = nmClassDAO.trim().replaceAll("[\"]", "");
						}
						else if (tokens[i].length()>=8 && tokens[i].substring(0, 8).equalsIgnoreCase("cdField:")) {
							cdField = tokens[i].length()==8 ? "" : tokens[i].substring(8);
							cdField = cdField.trim().replaceAll("[\"]", "");
						}
						else if (tokens[i].length()>=8 && tokens[i].substring(0, 8).equalsIgnoreCase("dsField:")) {
							dsField = tokens[i].length()==8 ? "" : tokens[i].substring(8);
							dsField = dsField.trim().replaceAll("[\"]", "");
						}
						else if (tokens[i].length()>=7 && tokens[i].substring(0, 7).equalsIgnoreCase("method:")) {
							methodStr = tokens[i].length()==7 ? "" : tokens[i].substring(7);
							methodStr = methodStr.trim().replaceAll("[\"]", "");
						}
					}
					classDAO = nmClassDAO==null ? null : Class.forName(nmClassDAO);
					method 	 = classDAO==null ? null : classDAO.getMethod((methodStr != null ? methodStr : "find"), new Class[] {ArrayList.class, Connection.class});
				}

				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				if (cdField != null && rsm.getString("VL_INICIAL") != null && !rsm.getString("VL_INICIAL").trim().equalsIgnoreCase("")) {


					criterios.add(new ItemComparator(cdField, rsm.getString("VL_INICIAL"), ItemComparator.EQUAL, Types.INTEGER));
					Object obj = method.invoke(null, new Object[] {criterios, connection});
					if (obj instanceof ResultSetMap) {
						ResultSetMap rsmTemp = (ResultSetMap)obj;
						if (rsmTemp.next()) {
							HashMap<String,Object> register = rsm.getRegister();
							register.put(dsField.toUpperCase(), rsmTemp.getString(dsField));
							register.put(cdField.toUpperCase(), rsmTemp.getString(cdField));
							register.put("CD_FIELD", cdField);
							register.put("DS_FIELD", dsField);
							rsm.updateRegister(register);
						}
					}
				}
			}

			if (parametro.getTpApresentacao() == MULTI_TEXT) {
				rsm.beforeFirst();
				while (rsm.next()) {
					if (rsm.getObject("BLB_VALOR") instanceof byte[])
						rsm.getRegister().put("BLB_VALOR", new String((byte[])rsm.getObject("BLB_VALOR")));
				}
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.getValoresOfParametro: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int updateImageOfParametro(int cdParametro, byte[] bytesOfImage, String tpImage) {
		return updateImageOfParametro(cdParametro, bytesOfImage, tpImage, null);
	}

	public static int updateImageOfParametro(int cdParametro, byte[] bytesOfImage, String tpImage, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			Parametro parametro = ParametroDAO.get(cdParametro, connection);
			if (parametro==null)
				return -1;

			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_parametro_valor " +
					"WHERE cd_parametro = ? " +
					"  AND cd_empresa IS NULL " +
					"  AND cd_pessoa IS NULL");
			pstmt.setInt(1, cdParametro);
			pstmt.execute();

			ParametroValor valor = new ParametroValor(cdParametro, 0, 0, 0, 0, bytesOfImage, tpImage, null);
			if (ParametroValorDAO.insert(valor, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.updateImageOfParametro: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int updateValueOfParametro(int cdParametro, String vlInicial, String vlFinal) {
		return updateValueOfParametro(cdParametro, vlInicial, vlFinal, null);
	}

	public static int updateValueOfParametro(int cdParametro, String vlInicial, String vlFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			Parametro parametro = ParametroDAO.get(cdParametro, connection);
			if (parametro==null)
				return -1;

			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_parametro_valor " +
					"WHERE cd_parametro = ? " +
					"  AND cd_empresa IS NULL " +
					"  AND cd_pessoa IS NULL");
			pstmt.setInt(1, cdParametro);
			pstmt.execute();

			ParametroValor valor = new ParametroValor(cdParametro, 0, 0, 0, 0, null, vlInicial, vlFinal);
			if (ParametroValorDAO.insert(valor, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.updateValueOfParametro: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int insertValueOfParametro(int cdParametro, String vlInicial, byte[] blbValor) {
		return insertValueOfParametro(cdParametro, vlInicial, blbValor, null);
	}
	
	public static int insertValueOfParametro(int cdParametro, String vlInicial, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ParametroValor pValor = new ParametroValor();
			pValor.setCdParametro(cdParametro);
			pValor.setVlInicial(vlInicial);
			
			int res = ParametroValorDAO.insert(pValor);
			
			Conexao.desconectar(connect);
			
			return res;
						
		} catch(Exception e) {
			return 0;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insertValueOfParametro(int cdParametro, String vlInicial, byte[] blbValor, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ParametroValor pValor = new ParametroValor();
			pValor.setCdParametro(cdParametro);
			pValor.setVlInicial(vlInicial);
			pValor.setBlbValor(blbValor);
			
			int res = ParametroValorDAO.insert(pValor);
			
			Conexao.desconectar(connect);
			
			return res;
						
		} catch(Exception e) {
			return 0;
		}
	}

	public static int updateValueOfParametro(int cdParametro, String vlInicial) {
		return updateValueOfParametro(cdParametro, vlInicial,(Connection)null);
	}
	public static int updateValueOfParametro(int cdParametro, String vlInicial, Connection connection) {
		return updateValueOfParametro(cdParametro, vlInicial, 0, connection);
	}

	public static int updateValueOfParametro(int cdParametro, String vlInicial, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			PreparedStatement pstmt = connection.prepareStatement("UPDATE grl_parametro_valor SET vl_inicial=?"+
					" WHERE cd_parametro=?" +
					((cdEmpresa > 0) ? " AND cd_empresa="+cdEmpresa : ""));
			pstmt.setString(1, vlInicial);
			pstmt.setInt(2, cdParametro);
			if(pstmt.executeUpdate() <= 0){
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			Conexao.desconectar(connection);
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.updateValueOfParametro: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int updateValueOfParametro(int cdParametro, String vlInicial, byte[] blbValor) {
		return updateValueOfParametro(cdParametro, vlInicial, blbValor, 0, null);
	}
	
	public static int updateValueOfParametro(int cdParametro, String vlInicial, byte[] blbValor, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			PreparedStatement pstmt = connection.prepareStatement("UPDATE grl_parametro_valor SET blb_valor=?"+
					" WHERE cd_parametro=?" +
					((cdEmpresa > 0) ? " AND cd_empresa="+cdEmpresa : ""));
			pstmt.setBytes(1, blbValor);
			pstmt.setInt(2, cdParametro);
			if(pstmt.executeUpdate() <= 0){
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			Conexao.desconectar(connection);
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.updateValueOfParametro: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result updateValueOfParametroByName(String nmParametro, String vlInicial, int cdUsuario) {
		return updateValueOfParametroByName(nmParametro, vlInicial, cdUsuario,(Connection)null);
	}
	public static Result updateValueOfParametroByName(String nmParametro, String vlInicial, int cdUsuario, Connection connection) {
		return updateValueOfParametroByName(nmParametro, vlInicial, -1, 0, cdUsuario, connection);
	}
	
	public static Result updateValueOfParametroByName(String nmParametro, int cdOpcao, int cdUsuario) {
		return updateValueOfParametroByName(nmParametro, cdOpcao, cdUsuario,(Connection)null);
	}
	public static Result updateValueOfParametroByName(String nmParametro, int cdOpcao, int cdUsuario, Connection connection) {
		return updateValueOfParametroByName(nmParametro, null, cdOpcao, 0, cdUsuario, connection);
	}

	private static Result updateValueOfParametroByName(String nmParametro, String vlInicial, int cdOpcao, int cdEmpresa, int cdUsuario, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			//Hash que poderá mudar de lugar
			HashMap<String, String> conjuntoNomeParametroControle = new HashMap<String, String>();
			conjuntoNomeParametroControle.put("ST_FAZER_MATRICULA_AEE", "Adicionar alunos nas turmas de AEE");
			conjuntoNomeParametroControle.put("ST_FAZER_MATRICULA_MAIS_EDUCACAO", "Adicionar alunos nas turmas de Mais Educação");
			conjuntoNomeParametroControle.put("ST_FAZER_CONSERVACAO_MATRICULA", "Conservação de matrícula");
			conjuntoNomeParametroControle.put("ST_FAZER_PROGRESSAO_MATRICULA_9_ANO_REPETENTES", "Progressão de repetentes do 9 ano");
			conjuntoNomeParametroControle.put("ST_APENAS_MATRICULAS_EM_CURSO", "Matrículas em curso");
			conjuntoNomeParametroControle.put("ST_FAZER_REMANEJAMENTOS", "Remanejamentos");
			conjuntoNomeParametroControle.put("ST_FAZER_TRANSFERENCIA", "Solicitação e Conclusão de Transferência");
			conjuntoNomeParametroControle.put("ST_FAZER_ALTERACOES_IDENTIFICACAO_CADASTRO_ALUNO", "Nome do aluno, Nome da mãe do aluno e Data de Nascimento");
			conjuntoNomeParametroControle.put("ST_FAZER_ALTERACOES_NUMERO_MATRICULA", "Número de Matrícula");
			conjuntoNomeParametroControle.put("ST_FAZER_ALTERACOES_STATUS_MATRICULA", "Status de matrícula");
			conjuntoNomeParametroControle.put("ST_ABRIR_MOVIMENTACAO", "Movimentação Escolar");
			conjuntoNomeParametroControle.put("ST_ABRIR_MATRICULA_COMPLETA", "Matrícula Passo a Passo");
			conjuntoNomeParametroControle.put("ST_ABRIR_MOVIMENTACAO_PROFESSORES", "Movimentação de Professores");
			conjuntoNomeParametroControle.put("ST_ABRIR_SITUACAO_ALUNO_CENSO", "Situação do aluno - Educacenso");
			conjuntoNomeParametroControle.put("ST_ABRIR_TURMA_MODALIDADE_CURSO", "Turmas-Modalidade/Curso");
			conjuntoNomeParametroControle.put("ST_ALTERACAO_MODALIDADE_MATRICULA", "Modalidade das matrículas");
			
			Parametro parametro = getByName(nmParametro, connection);
			ParametroValor parametroValor = ParametroValorDAO.get(parametro.getCdParametro(), 1, connection);
			
			int cdOpcaoAnterior = (parametroValor != null ? parametroValor.getCdOpcao() : 0);
			
			ParametroOpcao parametroOpcaoAnterior = ParametroOpcaoDAO.get(parametro.getCdParametro(), cdOpcaoAnterior, connection);
			ParametroOpcao parametroOpcaoPosterior = ParametroOpcaoDAO.get(parametro.getCdParametro(), cdOpcao, connection);
			
			PreparedStatement pstmt = null;
			if(vlInicial != null){
				pstmt = connection.prepareStatement("UPDATE grl_parametro_valor SET vl_inicial=?"+
						" WHERE cd_parametro=?" +
						((cdEmpresa > 0) ? " AND cd_empresa="+cdEmpresa : ""));
				pstmt.setString(1, vlInicial);
				pstmt.setInt(2, parametro.getCdParametro());
			}
			else if(cdOpcao >= 0){
				pstmt = connection.prepareStatement("UPDATE grl_parametro_valor SET cd_opcao=?"+
						" WHERE cd_parametro=?" +
						((cdEmpresa > 0) ? " AND cd_empresa="+cdEmpresa : ""));
				pstmt.setInt(1, cdOpcao);
				pstmt.setInt(2, parametro.getCdParametro());
			}
			int ret = pstmt.executeUpdate();
			if(ret == 0){
				if(vlInicial != null){
					pstmt = connection.prepareStatement("INSERT INTO grl_parametro_valor (cd_parametro, cd_valor, vl_inicial) VALUES ("+parametro.getCdParametro()+", 1, '"+vlInicial+"')");
					ret = pstmt.executeUpdate();
					if(ret <= 0 ){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao atualizar");
					}
				}
				else if(cdOpcao >= 0){
					pstmt = connection.prepareStatement("INSERT INTO grl_parametro_valor (cd_parametro, cd_valor, cd_opcao) VALUES ("+parametro.getCdParametro()+", 1, "+cdOpcao+")");
					ret = pstmt.executeUpdate();
					if(ret <= 0 ){
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao atualizar");
					}
				} 
			}
			else if(ret < 0){
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar");
			}
			
			if(cdUsuario > 0){
				int cdTipoOcorrenciaAlteracaoParametro = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERAR_PARAMETRO_CONTROLE_EDF, connection).getCdTipoOcorrencia();
				
				String txtOcorrencia = "";
				
				if(parametroOpcaoAnterior != null){
					txtOcorrencia = "Parâmetro " + conjuntoNomeParametroControle.get(parametro.getNmParametro()) + " alterado de '" + parametroOpcaoAnterior.getVlApresentacao() + "' para '" + parametroOpcaoPosterior.getVlApresentacao() + "'";
				}
				else{
					txtOcorrencia = "Parâmetro " + conjuntoNomeParametroControle.get(parametro.getNmParametro()) + " iniciado com '" + parametroOpcaoPosterior.getVlApresentacao() + "'";
				}
				
				OcorrenciaParametro ocorrencia = new OcorrenciaParametro(0, 0, txtOcorrencia, Util.getDataAtual(), cdTipoOcorrenciaAlteracaoParametro, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, parametro.getCdParametro(), cdOpcaoAnterior, cdOpcao);
				Result result = OcorrenciaParametroServices.save(ocorrencia, connection);
				if(result.getCode() < 0){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao gerar ocorrencia");
				}
			}
			
			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Parâmetro atualizado com sucesso");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.updateValueOfParametro: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao atualizar");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	

	public static ArrayList<?> getOpcoesInArrayOfParametro(int cdParametro) {
		return getOpcoesInArrayOfParametro(cdParametro,null);
	}

	public static ArrayList<?> getOpcoesInArrayOfParametro(int cdParametro, Connection connect) {
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			pstmt = connect.prepareStatement("SELECT * FROM GRL_PARAMETRO_OPCAO WHERE CD_PARAMETRO = ?");
			pstmt.setInt(1, cdParametro);
			rs = pstmt.executeQuery();
			ArrayList<HashMap<String,Object>> valoresParametro = new ArrayList<HashMap<String,Object>>();
			while(rs.next()){
				HashMap<String,Object> hash = new HashMap<String,Object>();
				hash.put("CD_OPCAO", new Integer(rs.getInt("CD_OPCAO")));
				hash.put("CD_PARAMETRO", new Integer(rs.getInt("cd_parametro")));
				hash.put("VL_APRESENTACAO", rs.getString("vl_apresentacao"));
				hash.put("VL_REAL", rs.getString("vl_real"));
				valoresParametro.add(hash);
			}
			rs.close();
			return valoresParametro;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.getOpcoesInArrayOfParametro: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.getOpcoesInArrayOfParametro: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteByNmParametro(String nmParametro) {
		return deleteByNmParametro(nmParametro, null);
	}

	public static int deleteByNmParametro(String nmParametro, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			Parametro p = ParametroServices.getByName(nmParametro, connection);

			return delete(p.getCdParametro(), connection);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdParametro) {
		return delete(cdParametro, null);
	}

	public static int delete(int cdParametro, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_parametro_valor " +
					"WHERE cd_parametro = ?");
			pstmt.setInt(1, cdParametro);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE FROM grl_parametro_opcao " +
					"WHERE cd_parametro = ?");
			pstmt.setInt(1, cdParametro);
			pstmt.execute();

			if (ParametroDAO.delete(cdParametro, connection)<=0) {
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
			System.err.println("Erro! ParametroServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int insert(Parametro objeto, ParametroValor[] opcoesParametro) {
		return insert(objeto, opcoesParametro, null);
	}

	public static int insert(Parametro objeto, ParametroValor[] opcoesParametro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int code = ParametroDAO.insert(objeto, connect);
			if (code==-1){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			for(int i=0; opcoesParametro!=null && i<opcoesParametro.length; i++){
				opcoesParametro[i].setCdParametro(code);
				int codeValor = ParametroValorDAO.insert(opcoesParametro[i], connect);
				if (codeValor<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}

			if (isConnectionNull)
				connect.commit();

			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Parametro objeto) {
		return update(objeto, (Connection)null);
	}

	public static int update(Parametro objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if (ParametroDAO.update(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			/* exclusão de configurações do parï¿½metro para empresas e pessoas */
			if (objeto.getTpParametro() == TP_GERAL) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE " +
						"FROM grl_parametro_valor " +
						"WHERE cd_parametro = ? " +
						"  AND ((NOT cd_empresa IS NULL) OR " +
						"		(NOT cd_pessoa IS NULL))");
				pstmt.setInt(1, objeto.getCdParametro());
				pstmt.execute();
			}
			/* exclusão de configurações do parï¿½metro para pessoas ou tipo genï¿½rico; caso o
			 * parï¿½metro esteja vinculado ï¿½ uma empresa especï¿½fica, tambem as configurações jï¿½ existentes
			 * do parï¿½metros para as demais empresas tambï¿½m serão removidas
			 */
			else if (objeto.getTpParametro() == TP_EMPRESA) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE " +
						"FROM grl_parametro_valor " +
						"WHERE cd_parametro = ? " +
						"  AND ((NOT cd_pessoa IS NULL) OR " +
						"		(cd_empresa IS NULL) " +
						"		" + (objeto.getCdEmpresa()>0 ? " OR (cd_empresa <> ?)" : "") + ")");
				pstmt.setInt(1, objeto.getCdParametro());
				if (objeto.getCdEmpresa() > 0)
					pstmt.setInt(2, objeto.getCdEmpresa());
				pstmt.execute();
			}
			else if (objeto.getTpParametro() == TP_PESSOA) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE " +
						"FROM grl_parametro_valor " +
						"WHERE cd_parametro = ? " +
						"  AND ((NOT cd_empresa IS NULL) OR " +
						"		(cd_pessoa IS NULL) " +
						"		" + (objeto.getCdPessoa()>0 ? " OR (cd_pessoa <> ?)" : "") + ")");
				pstmt.setInt(1, objeto.getCdParametro());
				if (objeto.getCdPessoa() > 0)
					pstmt.setInt(2, objeto.getCdPessoa());
				pstmt.execute();
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(Parametro objeto, ParametroValor[] opcoesParametro){
		return update(objeto, opcoesParametro, false);
	}

	public static int update(Parametro objeto, ParametroValor[] opcoesParametro, boolean parameterOnly) {
		return update(objeto, opcoesParametro, parameterOnly, null);
	}

	public static int update(Parametro objeto, ParametroValor[] opcoesParametro, boolean parameterOnly, Connection connect){
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int code = ParametroDAO.update(objeto, connect);
			if (code<=0){
				Conexao.rollback(connect);
				return -1;
			}

			if(!parameterOnly){
				//remover o parametros
				pstmt = connect.prepareStatement("DELETE FROM GRL_PARAMETRO_VALOR WHERE CD_PARAMETRO=?");
				pstmt.setInt(1, objeto.getCdParametro());
				pstmt.execute();
				for(int i=0; opcoesParametro!=null && i<opcoesParametro.length; i++){
					opcoesParametro[i].setCdParametro(objeto.getCdParametro());
					int codeValor = ParametroValorDAO.insert(opcoesParametro[i], connect);
					if (codeValor<=0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return -1;
					}
				}
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Parametro getByName(String nmParametro) {
		return getByName(nmParametro, null);
	}

	public static Parametro getByName(String nmParametro, Connection connect){
		boolean isConnectNull = connect==null;
		try {
			if (isConnectNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT * FROM GRL_PARAMETRO WHERE NM_PARAMETRO=\'"+nmParametro+"\'").executeQuery();
			if(rs.next()){
				return new Parametro(rs.getInt("cd_parametro"),
						rs.getInt("cd_empresa"),
						rs.getString("nm_parametro"),
						rs.getInt("tp_dado"),
						rs.getInt("tp_apresentacao"),
						rs.getString("nm_rotulo"),
						rs.getString("txt_url_filtro"),
						rs.getInt("cd_pessoa"),
						rs.getInt("tp_parametro"),
						rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getInt("tp_nivel_acesso"));
			}
			else{
				return null;
			}
			//REMOVIDO POR OBRIGAR A IR AO BANCO 2x BUSCAR DADOS QUE Jï¿½ ESTï¿½O NO RESULTSET
			//return rs.next() ? ParametroDAO.get(rs.getInt("CD_PARAMETRO"), connect) : null;
		}
		catch(Exception e) {
			System.out.println("Erro ao tentar buscar parametro: " + nmParametro);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectNull)
				Conexao.desconectar(connect);
		}
	}

	public static Parametro getByNameCdPessoa(String nmParametro, int cdPessoa) {
		return getByNameCdPessoa(nmParametro, cdPessoa, null);
	}

	public static Parametro getByNameCdPessoa(String nmParametro, int cdPessoa, Connection connect){
		boolean isConnectNull = connect==null;
		try {
			if (isConnectNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT * FROM GRL_PARAMETRO WHERE NM_PARAMETRO=\'"+nmParametro+"\' AND CD_PESSOA = " + cdPessoa).executeQuery();
			return rs.next() ? ParametroDAO.get(rs.getInt("CD_PARAMETRO"), connect) : null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getOpcoesOfParametro(int cdParametro) {
		return getOpcoesOfParametro(cdParametro, false, 0, 0, null);
	}

	public static ResultSetMap getOpcoesOfParametro(int cdParametro, boolean multList, int cdEmpresa, int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.cd_valor AS lg_opcao " +
					"FROM GRL_PARAMETRO_OPCAO A " +
					"LEFT OUTER JOIN grl_parametro_valor B ON (A.cd_parametro = B.cd_parametro " +
					"									   AND A.cd_opcao = B.cd_opcao " +
					(cdEmpresa>0 ? "					   AND A.cd_empresa = ? " : "") +
					(cdPessoa>0 ? "						   AND A.cd_pessoa = ? " : "") + ") " +
					"WHERE A.CD_PARAMETRO = ?");
			pstmt.setInt(1, cdParametro);
			int i = 2;
			if (cdEmpresa>0)
				pstmt.setInt(i++, cdEmpresa);
			if (cdPessoa>0)
				pstmt.setInt(i++, cdPessoa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroValor.getOpcoesOfParametro: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getOpcoesParametroByNome(String nmParametro) {
		return getOpcoesParametroByNome(nmParametro, null);
	}
	
	public static ResultSetMap getOpcoesParametroByNome(String nmParametro, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* FROM GRL_PARAMETRO_OPCAO A INNER JOIN GRL_PARAMETRO B ON (A.CD_PARAMETRO = B.CD_PARAMETRO) " + 
																"WHERE B.NM_PARAMETRO = ? ");
			pstmt.setString(1, nmParametro);
			ResultSet result = pstmt.executeQuery();
			
			ResultSetMap rsm = new ResultSetMap(result);
			
			return rsm;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllParametros(int cdEmpresa) {
		return getAllParametros(cdEmpresa, null);
	}

	public static ResultSetMap getAllParametros(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM GRL_PARAMETRO A " +
					"WHERE CD_EMPRESA " + (cdEmpresa==0 ? "IS NULL" : "= ?"));
			if (cdEmpresa!=0)
				pstmt.setInt(1, cdEmpresa);
			return new ResultSetMap(pstmt.executeQuery());
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

	/************************************************************************************************************
	 * MÓDULO GERAL
	 ************************************************************************************************************/
	public static void initGrl() {
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "grl");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesGrl(cdSistema, cdModulo, connect);

			// Tipo de Endereï¿½amento
			insertUpdate(new Parametro(0, 0, "TP_ENDERECAMENTO", NUMERICO, COMBO_BOX, "Tipo de endereï¿½amento", "",
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vínculo - Clientes
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_CLIENTE", NUMERICO, FILTER,
					"Vínculo - Clientes",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Cidade Default - Endereï¿½amento
			insertUpdate(new Parametro(0, 0, "CD_CIDADE_END_DEFAULT", NUMERICO, FILTER,
					"Cidade Default (Endereï¿½aamento)",
					"{caption:\"Selecionando a Cidade\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.CidadeServices\",method:\"find\"," +
							" cdField: \"CD_CIDADE\", dsField: \"NM_CIDADE\"," +
							" filterFields: [[{label:\"Cidade\", reference:\"NM_CIDADE\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:90}]],"+
							" gridOptions: {columns:[{label:\"Cidade\",reference:\"NM_CIDADE\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Pais Default
			insertUpdate(new Parametro(0, 0, "CD_PAIS_END_DEFAULT", NUMERICO, FILTER,
					"Pais Default",
					"{caption:\"Selecionando o pais\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.PaisServices\",method:\"find\"," +
							" cdField: \"CD_PAIS\", dsField: \"NM_PAIS\"," +
							" filterFields: [[{label:\"Pais\", reference:\"NM_PAIS\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:90}]],"+
							" gridOptions: {columns:[{label:\"Pais\",reference:\"NM_PAIS\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vínculo - Fornecedor
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_FORNECEDOR", NUMERICO, FILTER,
					"Vínculo - Fornecedor",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vínculo - Fornecedor
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_PRESTADOR", NUMERICO, FILTER,
					"Vínculo - Prestador",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vínculo - Desenvolvedor
			insertUpdate(new Parametro(0, 0, "CD_DESENVOLVEDOR", NUMERICO, FILTER,
					"Empresa Desenvolvedora",
					"{caption:\"Selecionando o Desenvolvedor do Sitema\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.grl.PessoaServices\",method:\"find\"," +
							" cdField: \"A.CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Desenvolvedor\",reference:\"A.nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Desenvolvedor\",reference:\"nm_pessoa\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Arquivo Desenvolvedor
			insertUpdate(new Parametro(0, 0, "NM_ARQUIVO_DESENVOLVEDOR", STRING, SINGLE_TEXT,
					"Arquivo do Desenvolvedor", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Vínculo - Colaborador
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_COLABORADOR", NUMERICO, FILTER,
					"Vínculo - Colaborador",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vínculo - Vendedor
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_VENDEDOR", NUMERICO, FILTER,
					"Vínculo - Vendedor",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vínculo - Autor
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_AUTOR", NUMERICO, FILTER,
					"Vínculo - Autor",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vínculo - Emitentes
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_EMITENTE", NUMERICO, FILTER,
					"Vínculo - Emitente",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//Logomarca padrão
			insertUpdate(new Parametro(0, 0, "BLB_LOGO_PADRAO", IMAGEM, SINGLE_TEXT,
					"Logo Padrão",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Link com previsï¿½o do tempo
			connect.setAutoCommit(false);
			int cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "PREVISAO_TEMPO", NUMERICO, COMBO_BOX, "Previsao do Tempo (Tela Inicial)", "",
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] cidades = {"Vitoria da Conquista", "Salvador"};
				String[] urls 	 = {"http://g1.globo.com/Portal/G1V2/prevtempo/glb_g1v2_clima_previsao_cidade_content/0,,0-0-290,00.html",
				"http://g1.globo.com/Portal/G1V2/prevtempo/glb_g1v2_clima_previsao_cidade_content/0,,0-0-285,00.html"};
				for (int i=0; i<cidades.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, cidades[i] /*vlApresentacao*/, urls[i] /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			connect.commit();
			connect.setAutoCommit(true);

			// Cidade onde irï¿½ buscar a previsï¿½o do tempo
			insertUpdate(new Parametro(0, 0, "NM_CIDADE_PREVISAO_TEMPO", STRING, SINGLE_TEXT, "Cidade e Estado para a previsao do tempo. Ex: Sao Paulo, SP",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);	

			// Exibir apenas endereï¿½o principal
			insertUpdate(new Parametro(0, 0, "LG_ENDERECO_PRINCIPAL", LOGICO, CHECK_BOX,
					"Exibir apenas endereço principal", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Exibir aba "Arquivos" no cadastro de pessoas
			insertUpdate(new Parametro(0, 0, "LG_PESSOA_ARQUIVOS", LOGICO, CHECK_BOX,
					"Exibir grade de arquivos no cadastro de pessoas", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Exibir aba "Fotos" no cadastro de produtos
			insertUpdate(new Parametro(0, 0, "LG_FOTO_PRODUTO", LOGICO, CHECK_BOX,
					"Exibir grade de fotos no cadastro de produtos", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			/**
			 * USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O
			 */
			// Servidor SMP usado para envio de e-mails (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			String nmParametro = "NM_SERVIDOR_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Email: Endereco Servidor SMTP",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Login de Servidor SMP usado para envio de e-mails (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "NM_LOGIN_SERVIDOR_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Email: Login de acesso ao Servidor SMTP",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Senha de acesso ao Login de Servidor SMTP usado para envio de e-mails (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "NM_SENHA_SERVIDOR_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, PASSWORD, "Email: Senha de acesso ao Servidor SMTP",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Email remetente usado para envio de e-mails (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "NM_EMAIL_REMETENTE_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Email: Endereco remetente",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Email remetente usado para envio de e-mails (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "LG_AUTENTICACAO_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX, "Email: Requisicao de autenticacao",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Email remetente usado para envio de e-mails (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "LG_SSL_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX, "Email: Conexao criptografada SSL",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema/*cdSistema*/, NIVEL_ACESSO_OPERADOR), connect);
			// Porta SMTP para envio de email (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "NR_PORTA_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "Email: Porta SMTP",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Permitir que haja debug ao enviar email (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "LG_DEBUG_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Email: Permitir que haja log de debug ao enviar email?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Nome do transporte para SMPT (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "NM_TRANSPORT_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Email: Nome do transporte para SMTP",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Servidor SMP usado para envio de e-mails (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "DS_EMAIL_ASSINATURA";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, MULTI_TEXT, "Email: Assinatura do email de notificacao",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Email usado para resposta de e-mails (USO GERAL NA PLATAFORMA, NAO ALTERAR SEM PONDERAï¿½ï¿½O)
			nmParametro = "NM_EMAIL_RESPOSTA_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Email: Endereco para resposta",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Ativar Mensagem/Notificação
			insertUpdate(new Parametro(0, 0, "LG_ALERTA", LOGICO, CHECK_BOX,
					"Alertas: Ativar alertas do sistema", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			
			nmParametro = "TP_GRUPO_USUARIO_ALERTA";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Alertas: Grupos notificáveis" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			if(cdParametro >= 0)	{
				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtVinculos = connect.prepareStatement("SELECT * FROM seg_grupo");
				ResultSetMap rsmVinculos = new ResultSetMap(pstmtVinculos.executeQuery());
				while(rsmVinculos.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmVinculos.getString("CD_GRUPO"));

					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmVinculos.getString("NM_GRUPO") /*vlApresentacao*/, 
								rsmVinculos.getString("CD_GRUPO") /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}
			}
			
			//Num de meses para verifica processos sem pagamento
			nmParametro = "NR_MESES_PROCESSO_SEM_PAGAMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "Alertas: Quantidade de meses para verificar processos sem pagamento",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Data da Sicronizacao
			insertUpdate(new Parametro(0, 0, "DT_SINCRONIZACAO", STRING, SINGLE_TEXT,
					"Data da ultima sincronizacao", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// ID do cliente na rede
			nmParametro = "NR_ID_CLIENTE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "ID do cliente (para sincronizacao) ",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Grupo de Combustï¿½vel
			nmParametro	= "CD_GRUPO_USUARIO_SUPERVISOR";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER, "Grupo de Supervisores",
					"{caption:\"Grupo de supervisores\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.seg.GrupoDAO\", method:\"find\"," +
							" cdField: \"CD_GRUPO\", dsField: \"NM_GRUPO\"," +
							" filterFields: [[{label:\"Grupo de Supervisores\", reference:\"nm_grupo\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo de Supervisores\", reference:\"NM_GRUPO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Data do inicio da sincronizaï¿½ï¿½o para esse cliente
			nmParametro = "DT_INICIO_SINCRONIZACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "Data de inicio para sincronizaï¿½ï¿½o ",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Vínculo - Emitentes
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_EMITENTE", NUMERICO, FILTER,
					"Vínculo - Emitente",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Documentaï¿½ï¿½o para Certidï¿½o de Nascimento
			insertUpdate(new Parametro(0, 0, "CD_TIPO_DOCUMENTACAO_NASCIMENTO", NUMERICO, FILTER,
					"Tipo Documentaï¿½ï¿½o - Nascimento",
					"{caption:\"Selecionando o tipo de documentação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoDocumentacaoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTACAO\", dsField: \"NM_TIPO_DOCUMENTACAO\"," +
							" filterFields: [[{label:\"Tipo de Documentaï¿½ï¿½o\", reference:\"nm_tipo_documentacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documentaï¿½ï¿½o\",reference:\"NM_TIPO_DOCUMENTACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Documentaï¿½ï¿½o para Certidï¿½o de Casamento
			insertUpdate(new Parametro(0, 0, "CD_TIPO_DOCUMENTACAO_CASAMENTO", NUMERICO, FILTER,
					"Tipo Documentaï¿½ï¿½o - Casamento",
					"{caption:\"Selecionando o tipo de documentação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoDocumentacaoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTACAO\", dsField: \"NM_TIPO_DOCUMENTACAO\"," +
							" filterFields: [[{label:\"Tipo de Documentaï¿½ï¿½o\", reference:\"nm_tipo_documentacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documentaï¿½ï¿½o\",reference:\"NM_TIPO_DOCUMENTACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "NM_PORTA_SERIAL";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Endereï¿½o da porta serial (Com) para acesso bluetooth",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Inicializar o sistema de educaï¿½ï¿½o juntamente com os outros
			insertUpdate(new Parametro(0, 0, "LG_INIT_EDUCACAO", LOGICO, CHECK_BOX,
					"Inicializar o sistema de educaï¿½ï¿½o", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de endereco para correspondï¿½ncia 
			insertUpdate(new Parametro(0, 0, "CD_TIPO_ENDERECO_CORRESPONDENCIA", NUMERICO, FILTER,
					"Tipo Endereï¿½o Correspondï¿½ncia",
					"{caption:\"Selecionando o tipo de endereï¿½o para correspondï¿½ncia\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoEnderecoServices\",method:\"find\"," +
							" cdField: \"CD_TIPO_ENDERECO\", dsField: \"NM_TIPO_ENDERECO\"," +
							" filterFields: [[{label:\"Tipo de Endereï¿½o Correspondï¿½ncia\", reference:\"NM_TIPO_ENDERECO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documentaï¿½ï¿½o\",reference:\"NM_TIPO_ENDERECO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Vínculo - Cartï¿½rio
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_CARTORIO", NUMERICO, FILTER,
					"Vínculo - Cartï¿½rio",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Caminho do Sistema
			nmParametro = "NM_PATH_PRINCIPAL"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Caminho do Sistema", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// CNPJ obrigatï¿½rio no Formulário de Pessoa
			insertUpdate(new Parametro(0, 0, "LG_CNPJ_OBRIGATORIO_PESSOA_FORM", LOGICO, CHECK_BOX,
					"CNPJ obrigatï¿½rio no Formulário de Pessoa", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// CPF obrigatï¿½rio no Formulário de Pessoa
			insertUpdate(new Parametro(0, 0, "LG_CPF_OBRIGATORIO_PESSOA_FORM", LOGICO, CHECK_BOX,
					"CPF obrigatï¿½rio no Formulário de Pessoa", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);


			//COMPLIANCE
			insertUpdate(new Parametro(0, 0, "LG_COMPLIANCE", LOGICO, CHECK_BOX,
					"Ativar Compliance", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Banco de Dados para Compliance
			nmParametro = "DS_COMPLIANCE_DATABASE";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Banco de dados para Compliance",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Usuário BD Compliance
			nmParametro = "DS_COMPLIANCE_USER";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Usuário BD Compliance",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Senha BD Compliance
			nmParametro = "DS_COMPLIANCE_PASS";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Senha BD Compliance",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Exibir abas de Dados Funcionais no cadastro de pessoa (PessoaForm2.mxml)
			nmParametro = "LG_SHOW_DADOS_FUNCIONAIS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX, "Exibir abas de Dados Funcionais no cadastro de pessoa",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);


			//Exibir preview de relatï¿½rios
			insertUpdate(new Parametro(0, 0, "LG_SHOW_PREVIEW_REPORT", LOGICO, CHECK_BOX,
					"Exibir preview de relatorios", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//Exibir pod de mensagens
			insertUpdate(new Parametro(0, 0, "LG_SHOW_MENSAGEM_POD", LOGICO, CHECK_BOX,
					"Exibir Pod de mensagens", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//Exibir pod de mensagens
			insertUpdate(new Parametro(0, 0, "LG_CADASTRO_UPPER_CASE_SEM_ACENTO", LOGICO, CHECK_BOX,
					"Cadastros em caixa alta e sem acento", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			
			/*
			 * LOG CONFIGURATIONS
			 */
			//nao mais utilizado, apagar onte tiver
			deleteByNmParametro("DEBUG_VERBOSE_LEVEL");
			
			//NIVEL DE VERBOSIDADEW
			nmParametro	= "LOG_VERBOSE_LEVEL";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Log: Nível de verbosidade", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<LogUtils.verboseLevels.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, LogUtils.verboseLevels[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			//GRAVAR ARQUIVO DE LOG
			insertUpdate(new Parametro(0, 0, "LG_LOG_FILE", LOGICO, CHECK_BOX,
					"Log: Gravar em arquivo?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//PATH TOMCAT (USO SUPORTE/PAINEL DE CONTROLE)
			insertUpdate(new Parametro(0, 0, "PATH_TOMCAT", STRING, SINGLE_TEXT, "Log: Caminho de log do Tomcat",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
		
			//EMPRESA DEFAULT
			insertUpdate(new Parametro(0, 0, "CD_EMPRESA_DEFAULT", NUMERICO, FILTER,
					"Geral: Empresa Default",
					"{caption:\"Selecione a Empresa\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.EmpresaServices\",method: \"find\"," +
							" cdField: \"CD_EMPRESA\", dsField: \"NM_EMPRESA\"," +
							" filterFields: [[{label:\"Empresa\",reference:\"nm_empresa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_EMPRESA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			/*
			 * FIM LOG CONFIGURATIONS
			 */
			
			//COMPRIMIR ARQUIVOS
			insertUpdate(new Parametro(0, 0, "LG_COMPRESS_FILE", LOGICO, CHECK_BOX,
					"Config: Compactar arquivos do sistema", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MODULO RECURSOS HUMANOS
	 ************************************************************************************************************/
	public static void initSrh()	{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "srh");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			// Parametro para pai o vinculo de Funcionário
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_FUNCIONARIO", NUMERICO, FILTER,
					"Vínculo: Funcionários",
					"{caption:\"Localize e selecione vinculo referente a Funcionários\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Indicador Salário Mï¿½nimo
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_INDICADOR_SALARIO_MINIMO", NUMERICO, FILTER,
					"Indicador Salário Mï¿½nimo",
					"{caption:\"Selecionando Indicador\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.IndicadorDAO\",method: \"find\"," +
							" cdField: \"CD_INDICADOR\", dsField: \"NM_INDICADOR\"," +
							" filterFields: [[{label:\'Indicador\',reference:\'nm_indicador\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Indicador\",reference:\"NM_INDICADOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Indicador Salário Família
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_INDICADOR_SALARIO_FAMILIA", NUMERICO, FILTER,
					"Indicador Salário Família",
					"{caption:\"Selecionando Indicador\",width:680,height:350,modal:true," +
							" cdField: \"CD_INDICADOR\", dsField: \"NM_INDICADOR\"," +
							" className:\"com.tivic.manager.grl.IndicadorDAO\",method: \"find\"," +
							" filterFields:[[{label:\"Indicador\",reference:\"nm_indicador\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions:{columns:[{label:\"Indicador\",reference:\"NM_INDICADOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Indicador IRRF
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_INDICADOR_IRRF", NUMERICO, FILTER,
					"Indicador IRRF",
					"{caption:\"Selecionando Indicador\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.IndicadorDAO\",method: \"find\"," +
							" cdField: \"CD_INDICADOR\", dsField: \"NM_INDICADOR\"," +
							" filterFields: [[{label:\'Indicador\',reference:\'nm_indicador\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Indicador\",reference:\"NM_INDICADOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Indicador INSS
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_INDICADOR_INSS", NUMERICO, FILTER,
					"Indicador INSS",
					"{caption:\"Selecionando Indicador\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.IndicadorDAO\",method: \"find\"," +
							" cdField: \"CD_INDICADOR\", dsField: \"NM_INDICADOR\"," +
							" filterFields: [[{label:\'Indicador\',reference:\'nm_indicador\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Indicador\",reference:\"NM_INDICADOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Indicador Salário Família
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_INDICADOR_SAL_FAM", NUMERICO, FILTER,
					"Indicador Salário Mï¿½nimo",
					"{caption:\"Selecionando Indicador\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.IndicadorDAO\",method: \"find\"," +
							" cdField: \"CD_INDICADOR\", dsField: \"NM_INDICADOR\"," +
							" filterFields: [[{label:\'Indicador\',reference:\'nm_indicador\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Indicador\",reference:\"NM_INDICADOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para INSS
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_INSS", NUMERICO, FILTER,
					"Tritubo INSS",
					"{caption:\"Selecionando Tributo\",width:680,height:350,modal:true,noDrag:true," +
							" cdField: \"CD_TRIBUTO\", dsField: \"NM_TRIBUTO\"," +
							" className: \"com.tivic.manager.adm.TributoDAO\",method: \"find\"," +
							" filterFields: [[{label:\"Tributo\",reference:\"nm_tributo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tributo\",reference:\"NM_TRIBUTO\"}," +
							"                       {label:\"Esfera Governamental\",reference:\"CL_TIPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para IRRF
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_IRRF", NUMERICO, FILTER,
					"Tritubo Imposto de Renda Pes. Fásica",
					"{caption:\"Selecionando Tributo\",width:680,height:350,modal:true,noDrag:true," +
							"cdField: \"CD_TRIBUTO\", dsField: \"NM_TRIBUTO\"," +
							"className: \"com.tivic.manager.adm.TributoDAO\",method: \"find\"," +
							"filterFields: [[{label:\"Tributo\",reference:\"nm_tributo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							"gridOptions: {columns:[{label:\"Tributo\",reference:\"NM_TRIBUTO\"}," +
							"                       {label:\"Esfera Governamental\",reference:\"CL_TIPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO ACADï¿½MICO
	 ************************************************************************************************************/
	public static void initAcd() {
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "acd");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesAcd(cdSistema, cdModulo, connect);

			insertUpdate(new Parametro(0, 0, "CD_VINCULO_INSTITUICAO_ACADEMICA", NUMERICO, FILTER,
					"Vínculo: Instituicoes Academicas",
					"{caption:\"Localize e selecione vinculo referente a Instituicoes Academicas\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_AGENCIA_FINANCIADORA", NUMERICO, FILTER,
					"Vínculo: Agencia Financiadoras",
					"{caption:\"Localize e selecione vinculo referente a Agencias Financiadoras\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_PROFESSOR", NUMERICO, FILTER,
					"Vínculo: Professores",
					"{caption:\"Localize e selecione vinculo referente a Professores\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_VINCULO_CONDUTOR", NUMERICO, FILTER,
					"Vínculo: Condutor",
					"{caption:\"Localize e selecione vinculo referente a Condutores\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_VINCULO_ALUNO", NUMERICO, FILTER,
					"Vínculo: Aluno",
					"{caption:\"Localize e selecione vinculo referente a Alunos\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_VINCULO_DIRETOR_INSTITUICAO", NUMERICO, FILTER,
					"Vínculo: Diretor(a) Escolar",
					"{caption:\"Localize e selecione vinculo referente a Diretores Escolares\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_VINCULO_VICE_DIRETOR_INSTITUICAO", NUMERICO, FILTER,
					"Vínculo: Vice-Diretor(a) Escolar",
					"{caption:\"Localize e selecione vinculo referente a Vice-Diretores Escolares\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_VINCULO_SECRETARIO_INSTITUICAO", NUMERICO, FILTER,
					"Vínculo: Secretario(a) Escolar",
					"{caption:\"Localize e selecione vinculo referente a Secretarios Escolares\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_VINCULO_TESOUREIRO_INSTITUICAO", NUMERICO, FILTER,
					"Vínculo: Tesoureiro(a) Escolar",
					"{caption:\"Localize e selecione vinculo referente a Tesoureiros Escolares\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_VINCULO_COORDENADOR_PEDAGOGICO", NUMERICO, FILTER,
					"Vínculo: Coordenador(a) Pedagogico(a)",
					"{caption:\"Localize e selecione vinculo referente ao(a) Coordenador(a) Pedagogico(a)\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_VINCULO_ADMINISTRADOR_INSTITUICAO", NUMERICO, FILTER,
					"Vínculo: Administrador(a) da Instituicao",
					"{caption:\"Localize e selecione vinculo referente ao(a) Administrador(a) da Instituicao\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_TIPO_DOC_SOLIC_RESCISAO", NUMERICO, FILTER,
					"Protocolo: Tipo de Documento referente a rescisoes",
					"{caption:\"Localize tipo de documento referente a solicitacao de rescisoes\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			insertUpdate(new Parametro(0, 0, "FORMALUNO.PREFERENCES", STRING, MULTI_TEXT, "Cadastro e Manutenï¿½ï¿½o de Alunos: Preferï¿½ncias",
					"", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);
			// Categoria econômica default para faturamento de taxas de matriculas
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_TAXA_MATRICULA", NUMERICO, FILTER,
					"Financeiro: Categoria para faturamento de taxas de matriculas",
					"{caption:\"Selecione categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para classificaï¿½ï¿½o de receitas
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_MENSALIDADE", NUMERICO, FILTER,
					"Financeiro: Categoria para faturamento de mensalidades e parcelas",
					"{caption:\"Selecione categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Georeferenciamento
			insertUpdate(new Parametro(0, 0, "CD_CAMADA_ESCOLAS_MUNICIPAIS", NUMERICO, FILTER,
					"Geo: Camada das escolas municipais",
					"{caption:\"Selecione a camada\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.geo.CamadaServices\",method: \"find\"," +
							" cdField: \"CD_CAMADA\", dsField: \"NM_CAMADA\"," +
							" filterFields: [[{label:\"Camada\",reference:\"nm_camada\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_CAMADA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			insertUpdate(new Parametro(0, 0, "CD_CAMADA_ESCOLAS_ESTADUAIS", NUMERICO, FILTER,
					"Geo: Camada das escolas estaduais",
					"{caption:\"Selecione a camada\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.geo.CamadaServices\",method: \"find\"," +
							" cdField: \"CD_CAMADA\", dsField: \"NM_CAMADA\"," +
							" filterFields: [[{label:\"Camada\",reference:\"nm_camada\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_CAMADA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			insertUpdate(new Parametro(0, 0, "CD_CAMADA_ESCOLAS_FEDERAIS", NUMERICO, FILTER,
					"Geo: Camada das escolas federais",
					"{caption:\"Selecione a camada\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.geo.CamadaServices\",method: \"find\"," +
							" cdField: \"CD_CAMADA\", dsField: \"NM_CAMADA\"," +
							" filterFields: [[{label:\"Camada\",reference:\"nm_camada\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_CAMADA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			insertUpdate(new Parametro(0, 0, "CD_CAMADA_UNIVERSIDADES", NUMERICO, FILTER,
					"Geo: Camada das universidades",
					"{caption:\"Selecione a camada\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.geo.CamadaServices\",method: \"find\"," +
							" cdField: \"CD_CAMADA\", dsField: \"NM_CAMADA\"," +
							" filterFields: [[{label:\"Camada\",reference:\"nm_camada\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_CAMADA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_INSTITUICAO_SECRETARIA_MUNICIPAL", NUMERICO, FILTER,
					"Acadêmico: Instituição da Secretaria de Educação do Município",
					"{caption:\"Selecione a instituição\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.InstituicaoDAO\",method: \"find\"," +
							" cdField: \"CD_INSTITUICAO\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Instituição\",reference:\"nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_PESSOA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Setor CAE/SMED
			insertUpdate(new Parametro(0, 0, "CD_SETOR_CAE", NUMERICO, FILTER,
					"Setor do CAE na SMED",
					"{caption:\"Localize o setor \", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.SetorDAO\",method:\"find\"," +
							" cdField: \"CD_SETOR\", dsField: \"NM_SETOR\"," +
							" filterFields: [[{label:\"Setor\", reference:\"nm_setor\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Setor\",reference:\"NM_SETOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			insertUpdate(new Parametro(0, 0, "CD_TIPO_PERIODO_LETIVO", NUMERICO, FILTER,
					"Acadêmico: Tipo de Período para Períodos Letivos",
					"{caption:\"Selecione o tipo de perï¿½odo\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.TipoPeriodoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_PERIODO\", dsField: \"NM_TIPO_PERIODO\"," +
							" filterFields: [[{label:\"Perï¿½odo\",reference:\"nm_tipo_periodo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_TIPO_PERIODO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_TIPO_DEPENDENCIA_SALA", NUMERICO, FILTER,
					"Acadêmico: Tipo de Dependência para sala de aula",
					"{caption:\"Selecione o tipo de dependência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.TipoDependenciaServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_DEPENDENCIA\", dsField: \"NM_TIPO_DEPENDENCIA\"," +
							" filterFields: [[{label:\"Tipo de Dependência\",reference:\"nm_tipo_dependencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_TIPO_DEPENDENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_TIPO_DEPENDENCIA_BIBLIOTECA", NUMERICO, FILTER,
					"Acadêmico: Tipo de Dependência para biblioteca",
					"{caption:\"Selecione o tipo de dependência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.TipoDependenciaServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_DEPENDENCIA\", dsField: \"NM_TIPO_DEPENDENCIA\"," +
							" filterFields: [[{label:\"Tipo de Dependência\",reference:\"nm_tipo_dependencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_TIPO_DEPENDENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_TIPO_AVALIACAO_FINAL", NUMERICO, FILTER,
					"Acadêmico: Tipo de avaliaï¿½ï¿½o para nota final de unidade",
					"{caption:\"Selecione o tipo de avaliaï¿½ï¿½o\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.TipoAvaliacaoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_AVALIACAO\", dsField: \"NM_TIPO_AVALIACAO\"," +
							" filterFields: [[{label:\"Tipo de Avaliaï¿½ï¿½o\",reference:\"nm_tipo_avaliacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"NM_TIPO_AVALIACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_PESSOA_PREFEITURA_MUNICIPAL", NUMERICO, FILTER,
					"Geral: Pessoa Jurï¿½dica da Prefeitura do Municï¿½pio",
					"{caption:\"Selecionando Pessoa\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.grl.PessoaDAO\",method:\"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Nome\",reference:\"nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome da Pessoa\",reference:\"NM_PESSOA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_PESSOA_SUPORTE", NUMERICO, FILTER,
					"Geral: Pessoa do Suporte da Empresa de Desenvolvimento",
					"{caption:\"Selecionando Pessoa\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.grl.PessoaServices\",method:\"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Nome\",reference:\"A.nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome da Pessoa\",reference:\"NM_PESSOA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			insertUpdate(new Parametro(0, 0, "CD_FUNCAO_PROFESSOR", NUMERICO, FILTER,
					"Recursos Humanos: Funï¿½ï¿½o para Professor",
					"{caption:\"Selecionando Funï¿½ï¿½o\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.srh.FuncaoDAO\",method:\"find\"," +
							" cdField: \"CD_FUNCAO\", dsField: \"NM_FUNCAO\"," +
							" filterFields: [[{label:\"Nome\",reference:\"nm_funcao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome da Funï¿½ï¿½o\",reference:\"NM_FUNCAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_SECAO_CONTEUDO_PROGRAMATICO_AULA", NUMERICO, FILTER,
					"Pedagï¿½gico: Seï¿½ï¿½es do Plano de Aula",
					"{caption:\"Selecione a seï¿½ï¿½o do plano de aula\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.acd.PlanoSecaoServices\",method:\"findSecaoAula\"," +
							" cdField: \"CD_SECAO\", dsField: \"NM_SECAO\"," +
							" filterFields: [[{label:\"Nome\",reference:\"nm_secao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome da Seï¿½ï¿½o\",reference:\"NM_SECAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "CD_SECAO_CONTEUDO_PROGRAMATICO_CURSO", NUMERICO, FILTER,
					"Pedagï¿½gico: Seï¿½ï¿½es do Plano de Curso",
					"{caption:\"Selecione a seï¿½ï¿½o do plano de curso\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.acd.PlanoSecaoServices\",method:\"findSecaoCurso\"," +
							" cdField: \"CD_SECAO\", dsField: \"NM_SECAO\"," +
							" filterFields: [[{label:\"Nome\",reference:\"nm_secao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome da Seï¿½ï¿½o\",reference:\"NM_SECAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			String nmParametro	= "CD_TIPO_OCORRENCIA_SOLICITACAO_TRANSFERENCIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Tipo de Ocorrï¿½ncia de Solicitaï¿½ï¿½o de Transferï¿½ncia",
					"{caption:\"Selecionar Tipo de Ocorrï¿½ncia\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_TIPO_OCORRENCIA_CONCLUSAO_TRANSFERENCIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Tipo de Ocorrï¿½ncia de Conclusï¿½o de Transferï¿½ncia",
					"{caption:\"Selecionar Tipo de Ocorrï¿½ncia\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_TIPO_OCORRENCIA_REMANEJAMENTO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Tipo de Ocorrï¿½ncia de Remanejamento",
					"{caption:\"Selecionar Tipo de Ocorrï¿½ncia\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_SECAO_HABILIDADE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Seï¿½ï¿½o para Habilidade",
					"{caption:\"Selecionar seï¿½ï¿½o para habilidade\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.PlanoSecaoDAO\",method:\"find\"," +
							" cdField: \"CD_SECAO\", dsField: \"NM_SECAO\"," +
							" filterFields: [[{label:\"Seï¿½ï¿½o\", reference:\"NM_SECAO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Seï¿½ï¿½o\",reference:\"NM_SECAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_DOENCA_DIABETE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Ficha Mï¿½dica: Código de Doenï¿½a para Diabetes",
					"{caption:\"Selecionar Doenï¿½a\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.DoencaDAO\",method:\"find\"," +
							" cdField: \"CD_DOENCA\", dsField: \"NM_DOENCA\"," +
							" filterFields: [[{label:\"Doenï¿½a\", reference:\"NM_DOENCA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Doenï¿½a\",reference:\"NM_DOENCA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Habilitar limite maximo de vagas
			nmParametro = "LG_LIMITE_VAGAS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Acadêmico: Habilitar limite maximo de vagas por turma", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Valor para máximo de vagas
			nmParametro = "VL_LIMITE_VAGAS";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Acadêmico: Valor limite máximo de vagas (%)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);


			nmParametro	= "CD_PROGRAMA_BOLSA_FAMILIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Ficha Social: Programa - Bolsa Família",
					"{caption:\"Selecionar Programa\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.ProgramaDAO\",method:\"find\"," +
							" cdField: \"CD_PROGRAMA\", dsField: \"NM_PROGRAMA\"," +
							" filterFields: [[{label:\"Programa\", reference:\"NM_PROGRAMA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Programa\",reference:\"NM_PROGRAMA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_PROGRAMA_MAIS_EDUCACAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Ficha Social: Programa - Mais Educaï¿½ï¿½o",
					"{caption:\"Selecionar Programa\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.ProgramaDAO\",method:\"find\"," +
							" cdField: \"CD_PROGRAMA\", dsField: \"NM_PROGRAMA\"," +
							" filterFields: [[{label:\"Programa\", reference:\"NM_PROGRAMA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Programa\",reference:\"NM_PROGRAMA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_TIPO_OCORRENCIA_CONFIRMACAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Tipo de Ocorrï¿½ncia de Confirmaï¿½ï¿½o de Matrï¿½cula",
					"{caption:\"Selecionar Tipo de Ocorrï¿½ncia\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			nmParametro	= "CD_CURSO_MULTI_CRECHE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Curso Multi - Creche",
					"{caption:\"Selecionar Curso\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.CursoServices\",method:\"findSimplificado\"," +
							" cdField: \"CD_CURSO\", dsField: \"NM_PRODUTO_SERVICO\"," +
							" filterFields: [[{label:\"Curso\", reference:\"B.NM_PRODUTO_SERVICO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Curso\",reference:\"NM_CURSO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_CURSO_MULTI_PRE_ESCOLA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Curso Multi - Prï¿½-escola",
					"{caption:\"Selecionar Curso\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.CursoServices\",method:\"findSimplificado\"," +
							" cdField: \"CD_CURSO\", dsField: \"NM_PRODUTO_SERVICO\"," +
							" filterFields: [[{label:\"Curso\", reference:\"B.NM_PRODUTO_SERVICO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Curso\",reference:\"NM_CURSO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_CURSO_MULTI_REGULAR";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Curso Multi - Regular",
					"{caption:\"Selecionar Curso\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.CursoServices\",method:\"findSimplificado\"," +
							" cdField: \"CD_CURSO\", dsField: \"NM_PRODUTO_SERVICO\"," +
							" filterFields: [[{label:\"Curso\", reference:\"B.NM_PRODUTO_SERVICO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Curso\",reference:\"NM_CURSO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_CURSO_MULTI_ANOS_INICIAIS";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Curso Multi - EJA - Anos Iniciais",
					"{caption:\"Selecionar Curso\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.CursoServices\",method:\"findSimplificado\"," +
							" cdField: \"CD_CURSO\", dsField: \"NM_PRODUTO_SERVICO\"," +
							" filterFields: [[{label:\"Curso\", reference:\"B.NM_PRODUTO_SERVICO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Curso\",reference:\"NM_CURSO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_CURSO_MULTI_ANOS_FINAIS";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Curso Multi - EJA - Anos Finais",
					"{caption:\"Selecionar Curso\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.CursoServices\",method:\"findSimplificado\"," +
							" cdField: \"CD_CURSO\", dsField: \"NM_PRODUTO_SERVICO\"," +
							" filterFields: [[{label:\"Curso\", reference:\"B.NM_PRODUTO_SERVICO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Curso\",reference:\"NM_CURSO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_CURSO_PROJOVEM";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Acadêmico: Curso Projovem urbano",
					"{caption:\"Selecionar Curso\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.CursoServices\",method:\"findSimplificado\"," +
							" cdField: \"CD_CURSO\", dsField: \"NM_PRODUTO_SERVICO\"," +
							" filterFields: [[{label:\"Curso\", reference:\"B.NM_PRODUTO_SERVICO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Curso\",reference:\"NM_CURSO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);


			// Habilitar limite maximo de vagas
			nmParametro = "LG_PERMISSAO_MATRICULA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Acadêmico: Habilitar cadastro de matricula para o módulo de escola", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);



			nmParametro	= "CD_CURSO_MAIS_EDUCACAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Academico: Curso - Mais Educaï¿½ï¿½o",
					"{caption:\"Selecionar Curso\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.CursoServices\",method:\"findSimplificado\"," +
							" cdField: \"CD_CURSO\", dsField: \"NM_PRODUTO_SERVICO\"," +
							" filterFields: [[{label:\"Curso\", reference:\"nm_produto_servico\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Curso\",reference:\"NM_PRODUTO_SERVICO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_CURSO_ATENDIMENTO_ESPECIALIZADO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Academico: Curso - Atendimento Especializado",
					"{caption:\"Selecionar Curso\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.acd.CursoServices\",method:\"findSimplificado\"," +
							" cdField: \"CD_CURSO\", dsField: \"NM_PRODUTO_SERVICO\"," +
							" filterFields: [[{label:\"Curso\", reference:\"nm_produto_servico\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Curso\",reference:\"NM_PRODUTO_SERVICO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//Cursos do fundamental 1
			nmParametro = "CD_CURSO_FUNDAMENTAL_1";
			int cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Cursos do Fundamental 1" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);
			
			if(cdParametro > 0)	{
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtCursos = connect.prepareStatement("SELECT A.cd_curso, B.cd_produto_servico, B.nm_produto_servico FROM acd_curso A " +
						"						JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) " +
						"			 		  ORDER BY B.nm_produto_servico");
				ResultSetMap rsmCurso = new ResultSetMap(pstmtCursos.executeQuery());
				while(rsmCurso.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, String.valueOf(rsmCurso.getInt("CD_CURSO")));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmCurso.getString("NM_PRODUTO_SERVICO")/*vlApresentacao*/, rsmCurso.getString("CD_CURSO") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}

			//Cursos do fundamental 2
			nmParametro = "CD_CURSO_FUNDAMENTAL_2";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Cursos do Fundamental 2" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);
			if(cdParametro > 0)	{
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtCursos = connect.prepareStatement("SELECT A.cd_curso, B.cd_produto_servico, B.nm_produto_servico FROM acd_curso A " +
						"						JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) " +
						"			 		  ORDER BY B.nm_produto_servico");
				ResultSetMap rsmCurso = new ResultSetMap(pstmtCursos.executeQuery());
				while(rsmCurso.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, String.valueOf(rsmCurso.getInt("CD_CURSO")));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmCurso.getString("NM_PRODUTO_SERVICO")/*vlApresentacao*/, rsmCurso.getString("CD_CURSO") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}

			//Cursos do Educação Infantil
			nmParametro = "CD_CURSO_EDUCACAO_INFANTIL";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Cursos de Educação Infantil" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);
			
			if(cdParametro > 0)	{
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtCursos = connect.prepareStatement("SELECT A.cd_curso, B.cd_produto_servico, B.nm_produto_servico FROM acd_curso A " +
						"						JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) " +
						"			 		  ORDER BY B.nm_produto_servico");
				ResultSetMap rsmCurso = new ResultSetMap(pstmtCursos.executeQuery());
				while(rsmCurso.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, String.valueOf(rsmCurso.getInt("CD_CURSO")));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmCurso.getString("NM_PRODUTO_SERVICO")/*vlApresentacao*/, rsmCurso.getString("CD_CURSO") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}

			// Data de referencia do Censo
			insertUpdate(new Parametro(0, 0, "DT_REFERENCIA_CENSO", STRING, SINGLE_TEXT,
					"Data de Referencia do Censo", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Vínculo de Fornecedor Escolas
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_FORNECEDOR_ESCOLA", NUMERICO, FILTER,
			"Vínculo: Fornecedores das Escolas",
			"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
					" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
					" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
					" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
					" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			
			String[] tiposPermissaoParametros = {"Desabilitado", "Habilitado apenas para a SMED", "Habilitado apenas para as escolas", "Habilitado para todos"};
			
			// Habilitar tela de Matrícula Passo a Passo para as escolas
			nmParametro	= "ST_ABRIR_MATRICULA_COMPLETA";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita a tela de Matrícula Passo a Passo para as escolas", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar as escolas a fazerem solicitação e conclusão de transferência
			nmParametro	= "ST_FAZER_TRANSFERENCIA";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazerem solicitação e conclusão de transferência", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar as escolas a fazerem remanejamentos
			nmParametro	= "ST_FAZER_REMANEJAMENTOS";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazerem remanejamentos", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar as escolas a alterarem o nome do aluno, nome da mãe e data de nascimento
			nmParametro	= "ST_FAZER_ALTERACOES_IDENTIFICACAO_CADASTRO_ALUNO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazer alterações no nome do aluno, nome da mãe e data de nascimento", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar as escolas a alterarem o número de matrícula do aluno
			nmParametro	= "ST_FAZER_ALTERACOES_NUMERO_MATRICULA";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazer alterações no número de matrícula do aluno", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar as escolas a alterarem o status de matrícula do aluno
			nmParametro	= "ST_FAZER_ALTERACOES_STATUS_MATRICULA";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazer alterações no status de matrícula do aluno", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar as escolas a fazer conservação de matrícula
			nmParametro	= "ST_FAZER_CONSERVACAO_MATRICULA";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazer conservação de matrícula do aluno", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar as escolas a fazer progressão de matrícula do 9 ano repetentes
			nmParametro	= "ST_FAZER_PROGRESSAO_MATRICULA_9_ANO_REPETENTES";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazer progressão de matrícula dos alunos de 9 ano repetentes", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar tela de Movimentação para as escolas
			nmParametro	= "ST_ABRIR_MOVIMENTACAO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita a tela de Movimentação para as escolas", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar tela de Turmas - Modalidade/Curso para as escolas
			nmParametro	= "ST_ABRIR_TURMA_MODALIDADE_CURSO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita a tela de Turmas-Modalidade/Curso para as escolas", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar tela de Situacao do aluno para o censo para as escolas
			nmParametro	= "ST_ABRIR_SITUACAO_ALUNO_CENSO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita a tela de Situação do aluno para o censo para as escolas", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar as escolas a fazer matrícula dos alunos de AEE
			nmParametro	= "ST_FAZER_MATRICULA_AEE";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazer matrícula dos alunos de AEE", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar as escolas a fazer matrícula dos alunos de Mais Educação
			nmParametro	= "ST_FAZER_MATRICULA_MAIS_EDUCACAO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazer matrícula dos alunos de Mais Educação", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Habilitar tela de Movimentação dos professores para as escolas
			nmParametro	= "ST_ABRIR_MOVIMENTACAO_PROFESSORES";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita a tela de Movimentação de Professores para as escolas", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Controle para que sejam feitas apenas matrículas em curso
			nmParametro	= "ST_APENAS_MATRICULAS_EM_CURSO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazerem apenas matrículas em curso", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Controle para que sejam feitas apenas matrículas em curso
			nmParametro	= "ST_APENAS_MATRICULAS_EM_CURSO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazerem apenas matrículas em curso", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Controle para que sejam feitas alterações de modalidade de alunos
			nmParametro	= "ST_ALTERACAO_MODALIDADE_MATRICULA";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Acadêmico: Habilita as escolas a fazerem alteração na modalidade das matrículas", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<tiposPermissaoParametros.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposPermissaoParametros[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Usuario especial para realizar determinadas operações no sistema
			insertUpdate(new Parametro(0, 0, "CD_USUARIO_PERMISSAO_ESPECIAL", NUMERICO, FILTER,
					"Acadêmico: Usuário padrão para realizar determinadas permissões no sistema",
					"{caption:\"Selecionando Usuário\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.seg.UsuarioServices\",method:\"find\"," +
							" cdField: \"CD_USUARIO\", dsField: \"NM_USUARIO\"," +
							" filterFields: [[{label:\"Nome do Usuário\",reference:\"NM_PESSOA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome do Usuario\",reference:\"NM_USUARIO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_INSTRUTOR_EDUCARTE", NUMERICO, FILTER,
					"Acadêmico: Vínculo - Instrutor Educarte",
					"{caption:\"Localize e selecione vinculo referente a instrutores do educarte\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}
		finally {
			Conexao.desconectar(connect);
		}

	}

	/************************************************************************************************************
	 * MODULO CONTROLE DE ALIMENTACAO ESCOLAR
	 ************************************************************************************************************/
	public static void initCae() {
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "cae");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;
			
			System.out.println("cdSistema: "+cdSistema + ", cdModulo: "+cdModulo);
			
			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesCae(cdSistema, cdModulo, connect);
			
			// Vinculo - Nutricionista
			String nmParametro = "CD_VINCULO_NUTRICIONISTA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Vinculos: Nutricionista",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vinculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vï¿½nculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vinculo - Merendeira
			nmParametro = "CD_VINCULO_MERENDEIRA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Vinculos: Merendeira",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vinculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vinculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vinculo - Supervisor Cae
			nmParametro = "CD_VINCULO_SUPERVISOR_CAE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Vinculos: Supervisor Cae",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vinculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vinculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			//Local de Armazenamento padrao para o CAE
			nmParametro	= "CD_LOCAL_ARMAZENAMENTO_DEFAULT_CAE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Alimentação: Local de Armazenamento Padrão",
					"{caption:\"Selecione Local de Armazenamento Default\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.alm.LocalArmazenamentoServices\",method: \"findCompleto\"," +
							" cdField: \"CD_LOCAL_ARMAZENAMENTO\", dsField: \"NM_LOCAL_ARMAZENAMENTO\"," +
							" filterFields: [[{label:\"Local de Armazenamento\",reference:\"nm_local_armazenamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Local de Armazenamento\",reference:\"NM_LOCAL_ARMAZENAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//Local de Armazenamento padrao para o CAE
			nmParametro	= "CD_TIPO_OPERACAO_VAREJO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de Operação - Varejo",
					"{caption: \"Selecionando Tipo de Operação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.TipoOperacaoDAO\", method: \"find\"," +
							" cdField: \"CD_TIPO_OPERACAO\", dsField: \"NM_TIPO_OPERACAO\"," +
							" filterFields: [[{label: \"Tipo de Operação\", reference: \"nm_tipo_operacao\", datatype: _VARCHAR, comparator: _LIKE_ANY, width: 100}]]," +
							" gridOptions: {columns: [{label: \"Tipo de Operação\", reference: \"NM_TIPO_OPERACAO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}
		finally {
			Conexao.desconectar(connect);
		}

	}

	/************************************************************************************************************
	 * MÓDULO ADMINISTRADOR
	 ************************************************************************************************************/
	public static void initAdm()		{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo  = ? ");
			pstmt.setString(1, "fnc");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo  = isNext ? rs.getInt("cd_modulo") : 0;

			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesAdm(cdSistema, cdModulo, connect);

			// Ignorar dias não ï¿½teis em geraï¿½ï¿½o de contas
			insertUpdate(new Parametro(0, 0, "LG_IGNORAR_DIA_NAO_UTIL", LOGICO, CHECK_BOX,
					"Financeiro: Ignorar dias não uteis ao gerar contas", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categorias Economicas Por Empresa
			insertUpdate(new Parametro(0, 0, "LG_CATEGORIAS_ECONOMICAS_POR_EMPRESA", LOGICO, CHECK_BOX,
					"Financeiro: Categorias Econômicas por Empresa", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Moeda Padrão
			insertUpdate(new Parametro(0, 0, "CD_MOEDA_DEFAULT", NUMERICO, FILTER,
					"Financeiro: Moeda padrão",
					"{caption:\"Selecionando Moeda Padrão\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.MoedaDAO\",method:\"find\"," +
							" cdField: \"cd_moeda\", dsField: \"nm_moeda\"," +
							" filterFields: [[{label:\"Nome\", reference:\"nm_moeda\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome\",reference:\"nm_moeda\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para ISS
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_ISS", NUMERICO, FILTER,
					"Financeiro: Tritubo ISS",
					"{caption:\"Selecionando Tributo\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TributoDAO\",method: \"find\"," +
							" cdField: \"CD_TRIBUTO\", dsField: \"NM_TRIBUTO\"," +
							" filterFields: [[{label:\"Tributo\",reference:\"nm_tributo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tributo\",reference:\"NM_TRIBUTO\"}," +
							"                        {label:\"Esfera Governamental\",reference:\"CL_TIPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para classificaï¿½ï¿½o de despesas
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_DESPESAS_DEFAULT", NUMERICO, FILTER,
					"Financeiro: Categoria econômica padrão - Despesas",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para classificaï¿½ï¿½o de receitas
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_RECEITAS_DEFAULT", NUMERICO, FILTER,
					"Financeiro: Categoria econômica padrão - Receitas",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para multa recebida
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_MULTA_RECEBIDA", NUMERICO, FILTER,
					"Financeiro: Categoria econômica de multa recebida - Receita",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para juros recebido
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_JUROS_RECEBIDO", NUMERICO, FILTER,
					"Financeiro: Categoria econômica de juros recebido - Receita",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria econômica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para acráscimos recebido/acrescentados a contas a receber
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_ACRESCIMO_RECEBIDO", NUMERICO, FILTER,
					"Financeiro: Categoria econômica de acrescimo recebido - Receita",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria econômica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para descontos concedidos
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_DESCONTO_CONCEDIDO", NUMERICO, FILTER,
					"Financeiro: Categoria econômica para desconto concedido - Reduï¿½ï¿½o da Receita",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica para descontos de TEF
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_DESCONTO_TEF", NUMERICO, FILTER,
					"Financeiro: Categoria econômica para desconto das Adm. de Cartï¿½es de Crï¿½dito/Dï¿½bito",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"findCategoriaReducaoReceita\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para multa paga
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_MULTA_PAGA", NUMERICO, FILTER,
					"Financeiro: Categoria econômica de multa paga - Despesa",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para juros recebido
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_JUROS_PAGO", NUMERICO, FILTER,
					"Financeiro: Categoria econômica de juros pagos - Receita",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para descontos concedidos
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_DESCONTO_RECEBIDO", NUMERICO, FILTER,
					"Financeiro: Categoria econômica para desconto recebido - Reduï¿½ï¿½o da Despesa",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Configuração de Vencimentos de Parcelas - Contratos
			String nmParametro	= "TP_VENCIMENTO_PARCELAS_CONTRATO";
			int cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Financeiro: Configuração de Vencimento de Parcelas - Contratos", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tiposConfiguracao = {"Taxa de Adesï¿½o e 1ï¿½ Parcela em venc. distintos",
				"Taxa de Adesï¿½o e 1ï¿½ Parcela no mesmo vencimento"};
				for (int i=0; i<tiposConfiguracao.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposConfiguracao[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			// Configuração de Tipo de Documento para geraï¿½ï¿½o de parcelas de contrato
			nmParametro	= "CD_TIPO_DOCUMENTO_CONTRATO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Tipo de Documento para parcelas de contrato",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Configuração de Tipo de Documento para geraï¿½ï¿½o de parcelas de contrato
			nmParametro	= "CD_TIPO_DOCUMENTO_PAGAR_CONTRATO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Tipo de Documento para parcela(s) de liberaï¿½ï¿½o de contrato",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Indica se o parametro de verificaï¿½ï¿½o de autorização de pagamento deverï¿½ ser seguido ou não
			nmParametro	= "LG_PAGAMENTO_SEM_AUTORIZACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Ignorar autorização de pagamento",
					"",0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tipo de documento nota fiscal
			nmParametro	= "CD_TIPO_DOCUMENTO_CONTA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Tipo de Documento - Lançar tributos automáticos",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro = "NM_USUARIOS_TETO_AUTORIZACAO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, STRING /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Usuarios liberados para informar teto de autorização" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);
			if(cdParametro > 0)	{
				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtUsers = connect.prepareStatement(" SELECT * FROM seg_usuario a "+
																		" JOIN grl_pessoa b on ( a.cd_pessoa = b.cd_pessoa ) "+
																		" WHERE st_usuario = 1 "+
																		" ORDER BY NM_PESSOA ");
				ResultSetMap rsmUsers = new ResultSetMap(pstmtUsers.executeQuery());
				while(rsmUsers.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmUsers.getString("NM_LOGIN"));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmUsers.getString("NM_PESSOA") /*vlApresentacao*/, rsmUsers.getString("NM_LOGIN") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}
			
			// Configuração de Tipo de Documento para permuta
			nmParametro	= "CD_TIPO_DOCUMENTO_PERMUTA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Tipo de Documento Permuta",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_FORMA_PAGAMENTO_PERMUTA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Forma de Pagamento Permuta",
					"{caption:\"Selecionando Forma de Pagamento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.FormaPagamentoDAO\",method:\"find\"," +
							" cdField: \"CD_FORMA_PAGAMENTO\", dsField: \"NM_FORMA_PAGAMENTO\"," +
							" filterFields: [[{label:\"Forma de Pagamento\", reference:\"nm_forma_pagamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			nmParametro	= "CD_FORMA_PAGAMENTO_DINHEIRO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Forma de Pagamento Dinheiro",
					"{caption:\"Selecionando Forma de Pagamento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.FormaPagamentoDAO\",method:\"find\"," +
							" cdField: \"CD_FORMA_PAGAMENTO\", dsField: \"NM_FORMA_PAGAMENTO\"," +
							" filterFields: [[{label:\"Forma de Pagamento\", reference:\"nm_forma_pagamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			nmParametro	= "CD_FORMA_PAGAMENTO_BOLETO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Forma de Pagamento Boleto",
					"{caption:\"Selecionando Forma de Pagamento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.FormaPagamentoDAO\",method:\"find\"," +
							" cdField: \"CD_FORMA_PAGAMENTO\", dsField: \"NM_FORMA_PAGAMENTO\"," +
							" filterFields: [[{label:\"Forma de Pagamento\", reference:\"nm_forma_pagamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_FORMA_PAGAMENTO_CHEQUE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Forma de pagamento tipo cheque",
					"{caption:\"Selecionando Forma de Pagamento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.FormaPagamentoDAO\",method:\"find\"," +
							" cdField: \"CD_FORMA_PAGAMENTO\", dsField: \"NM_FORMA_PAGAMENTO\"," +
							" filterFields: [[{label:\"Forma de Pagamento\", reference:\"nm_forma_pagamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_TIPO_OCORRENCIA_CANCEL_DEPENDENTE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Contratos - Ocorrï¿½ncia de Cancelamento de Dependentes",
					"{caption:\"Selecionar Tipo de Ocorrï¿½ncia\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Gerar conta a pagar quando gera parcelas do contrato
			nmParametro = "LG_GERAR_CONTA_PAGAR_CONTRATO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Gerar conta a pagar na geraï¿½ï¿½o de parcelas do contrato", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Gerar conta a pagar quando gera parcelas do contrato
			nmParametro = "LG_ATUALIZAR_PROXIMA_CONTA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Atualizar também contas geradas via repetição (parcelas)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Gerar conta a pagar quando gera parcelas do contrato
			nmParametro = "LG_IMPRESSAO_CHEQUE";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Envia cheques para fila de impressão?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Estoque mï¿½nimo de cheque, por talï¿½o
			nmParametro = "QT_ESTOQUE_MINIMO_CHEQUE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Estoque mï¿½nimo de cheque, por talï¿½o", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Indica a carteira padrão
			nmParametro	= "CD_CONTA_PADRAO_RECEBIMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Financeiro: Conta para Recebimentos",
					"{caption:\"Selecionando conta\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.ContaFinanceiraDAO\",method: \"find\"," +
							" cdField: \"CD_CONTA\", dsField: \"NM_CONTA\"," +
							" filterFields: [[{label:\"Conta\",reference:\"nm_conta\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nï¿½mero\",reference:\"NR_CONTA\"}," +
							"                        {label:\"Conta\",reference:\"NM_CONTA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Indica a forma da recebimento padrão
			nmParametro	= "CD_FORMA_PAGAMENTO_CONTA_RECEBER_PADRAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Financeiro: Forma de pagamento para recebimentos",
					"{caption:\"Selecionando forma de pagamento\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.FormaPagamentoDAO\",method: \"find\"," +
							" cdField: \"CD_FORMA_PAGAMENTO\", dsField: \"NM_FORMA_PAGAMENTO\"," +
							" filterFields: [[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Configuração de Tipo de Documento para geraï¿½ï¿½o de parcelas de contrato
			nmParametro	= "CD_TIPO_DOCUMENTO_CHEQUE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Tipo de Documento para cheques",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "TP_DOCUMENTO_CHEQUE";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Tipos de documento - Cheque" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);

			if(cdParametro > 0)	{
				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtTpDocumento = connect.prepareStatement("SELECT * FROM adm_tipo_documento");
				ResultSetMap rsmTpDocumento = new ResultSetMap(pstmtTpDocumento.executeQuery());
				while(rsmTpDocumento.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmTpDocumento.getString("CD_TIPO_DOCUMENTO"));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmTpDocumento.getString("NM_TIPO_DOCUMENTO") /*vlApresentacao*/, rsmTpDocumento.getString("CD_TIPO_DOCUMENTO") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}

			insertUpdate(new Parametro(0, 0, "CD_USUARIO_PADRAO_BAIXA_AUTOMATICA", NUMERICO, FILTER,
					"Financeiro: Usuário padrão para baixa automática de cheques",
					"{caption:\"Selecionando Usuário\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.seg.UsuarioServices\",method:\"find\"," +
							" cdField: \"CD_USUARIO\", dsField: \"NM_USUARIO\"," +
							" filterFields: [[{label:\"Nome do Usuário\",reference:\"NM_PESSOA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Nome do Usuario\",reference:\"NM_USUARIO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Gerar conta a pagar quando gera parcelas do contrato
			nmParametro = "LG_BAIXA_AUTOMATICA_CHEQUES";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Gerar baixa automática de cheques", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Parametro logico para saber se a condiï¿½ï¿½o de pagamento ï¿½ obrigatoria
			nmParametro = "LG_CONDICAO_PAGAMENTO_OBRIGATORIA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: A Condiï¿½ï¿½o de Pagamento ï¿½ obrigatï¿½ria", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_FORMA_PAGAMENTO_PROGRAMA_FATURA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Forma de Pagamento para Programa de Fatura",
					"{caption:\"Selecionando Forma de Pagamento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.FormaPagamentoDAO\",method:\"find\"," +
							" cdField: \"CD_FORMA_PAGAMENTO\", dsField: \"NM_FORMA_PAGAMENTO\"," +
							" hiddenFields: [{reference:\"TP_FORMA_PAGAMENTO\",value:\""+FormaPagamentoServices.TITULO_CREDITO+"\",comparator:_EQUAL,datatype:_INTEGER}], " +
							" filterFields: [[{label:\"Forma de Pagamento\", reference:\"nm_forma_pagamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Categoria econômica para receita de estorno (receita)
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_RECEITA_ESTORNO", NUMERICO, FILTER,
					"Financeiro: Categoria econômica para receita de estorno",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"findCategoriaReceita\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Categoria econômica para despesa de estorno (despesa)
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_DESPESA_ESTORNO", NUMERICO, FILTER,
					"Financeiro: Categoria econômica para despesa de estorno",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"findCategoriaDespesa\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Categoria econômica para descontos de Programa de Fatura
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_DESCONTO_PROGRAMA_FATURA", NUMERICO, FILTER,
					"Financeiro: Categoria econômica para desconto do Programa de Fatura",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Categoria econômica para juros de Programa de Fatura
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_JUROS_PROGRAMA_FATURA", NUMERICO, FILTER,
					"Financeiro: Categoria econômica para juros do Programa de Fatura",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Gerar conta a pagar quando gera parcelas do contrato
			nmParametro = "LG_COBRANCA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Fazer cobranï¿½a de contas automática", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Gerar conta a pagar quando gera parcelas do contrato
			nmParametro = "LG_EMAIL_COBRANCA_DETALHADO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Enviar no email de cobranï¿½a detalhes sobre multa, juros e suspensï¿½o ou cancelamento de crédito", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Documento para geraï¿½ï¿½o de faturas
			nmParametro	= "CD_TIPO_DOCUMENTO_FATURA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro - Tipo de documento para geraï¿½ï¿½o de fatura",
					"{caption:\"Selecionando tipo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\", method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de documento\", reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Arquivo
			nmParametro	= "CD_TIPO_ARQUIVO_EMAIL";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Tipo de Arquivo para Email",
					"{caption:\"Selecionando tipo de arquivo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoArquivoDAO\", method:\"find\"," +
							" cdField: \"CD_TIPO_ARQUIVO\", dsField: \"NM_TIPO_ARQUIVO\"," +
							" filterFields: [[{label:\"Tipo de arquivo\", reference:\"nm_tipo_arquivo\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de arquivo\", reference:\"NM_TIPO_ARQUIVO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Substituir os juros que estï¿½o no programa de fatura pelos juros de cobranï¿½a
			nmParametro = "LG_SUBTITUIR_JUROS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Substituir juros do programa de fatura pela cobranï¿½a", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Nível da Categoria Econômica Despesa para o Dashboard Financeiro
			insertUpdate(new Parametro(0, 0, "NR_NIVEL_CATEGORIA_DESPESA_DASHBOARD", NUMERICO, FILTER,
					"Financeiro: Nível da Categoria Econômica Despesa para o Dashboard Financeiro",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"findCategoriaDespesa\"," +
							" cdField: \"NR_NIVEL\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Nível da Categoria Econômica Receita para o Dashboard Financeiro

			insertUpdate(new Parametro(0, 0, "NR_NIVEL_CATEGORIA_RECEITA_DASHBOARD", NUMERICO, FILTER,
					"Financeiro: Nível da Categoria Econômica Receita para o Dashboard Financeiro",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"findCategoriaReceita\"," +
							" cdField: \"NR_NIVEL\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Definir a exibição ou não dos campos de turno
			nmParametro = "LG_TURNO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Exibir Campo Turno nos Formulários", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Controle de Crï¿½dito do Cliente
			nmParametro = "LG_CONTROLE_CREDITO_CLIENTE";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Controlar o crédito do cliente", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Configuração de Tipo de Documento para discriminaï¿½ï¿½o do faturamento das notas fiscais
			nmParametro = "CD_TIPO_DOCUMENTO_CARTAO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Financeiro: Tipo de Documento para cartões" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);
			if(cdParametro > 0)	{
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtTpDocumento = connect.prepareStatement("SELECT * FROM adm_tipo_documento");
				ResultSetMap rsmTpDocumento = new ResultSetMap(pstmtTpDocumento.executeQuery());
				while(rsmTpDocumento.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmTpDocumento.getString("CD_TIPO_DOCUMENTO"));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmTpDocumento.getString("NM_TIPO_DOCUMENTO") /*vlApresentacao*/, rsmTpDocumento.getString("CD_TIPO_DOCUMENTO") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}

			// Configuração de Tipo de Documento para atualizar o número dos documentos nas notas fiscais
			nmParametro = "CD_TIPO_DOCUMENTO_BOLETO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Financeiro: Tipo de Documento para Boletos" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);
			if(cdParametro > 0)	{
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtTpDocumento = connect.prepareStatement("SELECT * FROM adm_tipo_documento");
				ResultSetMap rsmTpDocumento = new ResultSetMap(pstmtTpDocumento.executeQuery());
				while(rsmTpDocumento.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmTpDocumento.getString("CD_TIPO_DOCUMENTO"));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmTpDocumento.getString("NM_TIPO_DOCUMENTO") /*vlApresentacao*/, rsmTpDocumento.getString("CD_TIPO_DOCUMENTO") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}

			//Habilitar cï¿½lculo automï¿½tico de juros e multa
			insertUpdate(new Parametro(0, 0, "LG_CALCULO_JUROS_MULTA", LOGICO, CHECK_BOX,
					"Financeiro: Habilitar Cálculo Automático de Juros e Multa", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			//TIPO DE ANDAMENTO PARA GERAï¿½ï¿½O AUTOMï¿½TICA DO ANDAMENTO DE PROCESSO FINANCEIRO A PARTIR DO MÓDULO FINANCEIRO
			nmParametro	= "CD_TIPO_ANDAMENTO_PROCESSO_FINANCEIRO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Tipo de Andamento gerado para Processo Financeiro a partir de Contas a Pagar",
					"{caption:\"Selecionando andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "TP_ORDENACAO_PESQUISA_CONTA_RECEBER";
			cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, STRING, COMBO_BOX,
					"Financeiro: Tipo de ordenaï¿½ï¿½o na pesquisa de contas a receber", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] labels = {"Crescente","Decrescente"};
				String[] values = {"ASC", "DESC"};
				for (int i=0; i<labels.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, labels[i] /*vlApresentacao*/, values[i] /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			nmParametro = "TP_ORDENACAO_PESQUISA_CONTA_PAGAR";
			cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, STRING, COMBO_BOX,
					"Financeiro: Tipo de ordenaï¿½ï¿½o na pesquisa de contas a pagar", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] labels = {"Crescente","Decrescente"};
				String[] values = {"ASC", "DESC"};
				for (int i=0; i<labels.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, labels[i] /*vlApresentacao*/, values[i] /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			// Quantidade de registros a serem buscados
			nmParametro = "QT_LIMITE_BUSCA_CONTA_PAGAR";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Financeiro: Limite de registros na pesquisa de contas a pagar", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Quantidade de registros a serem buscados
			nmParametro = "QT_LIMITE_BUSCA_CONTA_RECEBER";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Financeiro: Limite de registros na pesquisa de contas a receber", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Permitir que nota possa ser criada com documento em conferencia
			nmParametro = "LG_NOVA_TELA_TABELA_PRECO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Ativar nova tela de tabela de preï¿½o?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// FAVORECIDO OBRIGATï¿½RIO EM CONTAS A PAGAR 
			nmParametro = "LG_FAVORECIDO_OBRIGATORIO_CONTA_PAGAR";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Tornar Favorecido obrigatï¿½rio em contas a pagar?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// HISTï¿½RICO OBRIGATï¿½RIO EM CONTAS 
			nmParametro = "LG_HISTORICO_CONTA_OBRIGATORIO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Tornar campo histï¿½rico obrigatï¿½rio nas contas?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// FAVORECIDO OBRIGATï¿½RIO EM CONTAS A PAGAR 
			nmParametro = "LG_BUSCAR_DOCUMENTOS_ATIVOS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Buscar somente os Tipos de Documentos que Estï¿½o ativos?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);			
			// CENTRO DE CUSTO OBRIGATï¿½RIO
			nmParametro = "LG_CENTRO_CUSTO_OBRIGATORIO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Tornar Centro de Custo obrigatï¿½rio?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Conta Bancï¿½rio para o filtro do relatï¿½rio de Contas a Receber
			nmParametro	= "CD_CONTA_REL_CONTA_RECEBER";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Conta (Caixa/Bancï¿½rio) para o filtro do relatï¿½rio de contas a receber",
					"{caption:\"Selecionando conta\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.ContaFinanceiraDAO\",method: \"find\"," +
							" cdField: \"CD_CONTA\", dsField: \"NM_CONTA\"," +
							" filterFields: [[{label:\"Conta\",reference:\"nm_conta\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nï¿½mero\",reference:\"NR_CONTA\"}," +
							"                        {label:\"Conta\",reference:\"NM_CONTA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Tipo de Documento para o filtro do relatï¿½rio de Contas a Receber
			nmParametro	= "CD_TIPO_DOCUMENTO_REL_CONTA_RECEBER";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Tipo de Documento para o filtro do relatï¿½rio de contas a receber",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Situação de conta a receber para o filtro do relatï¿½rio de Contas a Receber
			nmParametro	= "ST_CONTA_RECEBER_REL_CONTA_RECEBER";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, COMBO_BOX /*tpApresentacao*/,
					"Financeiro: Situação da conta para o filtro do relatï¿½rio de contas a receber" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_EMPRESA /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);
			if (cdParametro>0) {
				String[] options = ContaReceberServices.situacaoContaReceber;
				ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, "Todas" /*vlApresentacao*/, "0" /*vlReal*/,
						0 /*cdPessoa*/, 0 /*cdEmpresa*/));
				for (int i=0; i<options.length; i++){
					ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, options[i] /*vlApresentacao*/, Integer.toString(i+1) /*vlReal*/,
							0 /*cdPessoa*/, 0 /*cdEmpresa*/));
				}
			}

			// Situação de conta a receber para o filtro de busca em contas a receber
			nmParametro	= "ST_FILTRO_CONTA_CONTA_RECEBER";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, COMBO_BOX /*tpApresentacao*/,
					"Financeiro: Situação inicial para o filtro de busca em contas a receber" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);
			if (cdParametro>0) {
				String[] options = ContaReceberServices.situacaoContaReceber;
				ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, "Todas" /*vlApresentacao*/, "-1" /*vlReal*/,
						0 /*cdPessoa*/, 0 /*cdEmpresa*/));
				for (int i=0; i<options.length; i++){
					ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, options[i]/*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
				}
			}
			// Situação de conta a pagar para o filtro de busca em contas a pagar
			nmParametro	= "ST_FILTRO_CONTA_CONTA_PAGAR";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, COMBO_BOX /*tpApresentacao*/,
					"Financeiro: Situação inicial para o filtro de busca em contas a pagar" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);
			if (cdParametro>0) {
				String[] options = ContaPagarServices.situacaoContaPagar;
				ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, "Todas" /*vlApresentacao*/, "-1" /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
				for (int i=0; i<options.length; i++){
					ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, options[i]/*vlApresentacao*/, Integer.toString(i) /*vlReal*/,
							0 /*cdPessoa*/, 0 /*cdEmpresa*/));
				}
			}

			// Exibir informaï¿½ï¿½es da conta bancï¿½ria do favorecido na tela de contas a pagar
			nmParametro = "LG_SHOW_DADOS_CONTA_FAVORECIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Exibir informaï¿½ï¿½es da conta bancï¿½ria do favorecido na tela de contas a pagar?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// BUSCAR CENTRO DE CUSTO VERIFICANDO CADASTRO EM PessoaCentroCusto
			nmParametro = "LG_VERIFICAR_PESSOA_CENTRO_CUSTO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: Verificar restriï¿½ï¿½o de acesso ao centro de custo", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CDS_FORMAS_PAGAMENTO_FECHAMENTO_CAIXA";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Formas de Pagamento disponï¿½veis para movimentaï¿½ï¿½es de fechamento de caixa" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);
			if(cdParametro > 0)	{
				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtFormaPag = connect.prepareStatement("SELECT * FROM adm_forma_pagamento");
				ResultSetMap rsmFormaPag = new ResultSetMap(pstmtFormaPag.executeQuery());
				while(rsmFormaPag.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmFormaPag.getString("CD_FORMA_PAGAMENTO"));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmFormaPag.getString("NM_FORMA_PAGAMENTO") /*vlApresentacao*/, rsmFormaPag.getString("CD_FORMA_PAGAMENTO") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}

			// Tipo de Documento para o filtro do relatï¿½rio de Contas a Receber
			nmParametro	= "CD_TIPO_DOCUMENTO_FECHAMENTO_CAIXA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro: Tipo de Documento para fechamento de Caixa",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_TIPO_DOCUMENTO_CARTAO_CREDITO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro - Tipo de documento para Cartï¿½es de Crï¿½dito",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			insertUpdate(new Parametro(0, 0, "LG_REL_CONTA_PAGAR_MULTI_EMPRESA", LOGICO, CHECK_BOX, "Permitir alterar a empresa no relatório de contas a pagar?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			insertUpdate(new Parametro(0, 0, "LG_REL_PAGAMENTOS_MULTI_EMPRESA", LOGICO, CHECK_BOX, "Permitir alterar a empresa no relatório de pagamentos?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			insertUpdate(new Parametro(0, 0, "LG_REL_CONTA_RECEBER_MULTI_EMPRESA", LOGICO, CHECK_BOX, "Permitir alterar a empresa no relatório de contas a receber?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			insertUpdate(new Parametro(0, 0, "LG_REL_RECEBIMENTOS_MULTI_EMPRESA", LOGICO, CHECK_BOX, "Permitir alterar a empresa no relatório de recebimentos?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// PAX - Código do turno de fechamento de caixa padrão
			nmParametro	= "CD_TURNO_FECHAMENTO_DEFAULT";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro PAX: Código do turno de fechamento de caixa padrão",
					"{caption:\"Selecione o turno\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TurnoServices\",method:\"find\"," +
							" cdField: \"CD_TURNO\", dsField: \"NM_TURNO\"," +
							" filterFields: [[{label:\"Turno\", reference:\"nm_turno\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Turno\",reference:\"NM_TURNO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// PAX - Código do turno de fechamento de caixa cob
			nmParametro	= "CD_TURNO_FECHAMENTO_COB";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Financeiro PAX: Código do turno de fechamento de caixa COB",
					"{caption:\"Selecione o turno\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TurnoServices\",method:\"find\"," +
							" cdField: \"CD_TURNO\", dsField: \"NM_TURNO\"," +
							" filterFields: [[{label:\"Turno\", reference:\"nm_turno\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Turno\",reference:\"NM_TURNO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			//PAX - Usuários bloqueados para fechamento
			nmParametro = "CD_USUARIOS_BLOQUEADOS_FECHAMENTO_CAIXA";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
						                                 "Financeiro: Usuários bloqueados para operar fechamento de caixa" /*nmRotulo*/, 
						                                 null /*txtUrlFiltro*/, 0 /*cdPessoa*/, TP_GERAL /*tpParametro*/, cdModulo, cdSistema,
						                                 NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);
			if(cdParametro > 0)	{
				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtUser = connect.prepareStatement("SELECT * FROM seg_usuario A JOIN GRL_PESSOA "+
																		" B ON ( A.CD_PESSOA = B.CD_PESSOA )");
				ResultSetMap rsmUser = new ResultSetMap(pstmtUser.executeQuery());
				while(rsmUser.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmUser.getString("CD_USUARIO"));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmUser.getString("NM_PESSOA") /*vlApresentacao*/, rsmUser.getString("CD_USUARIO") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}
				
			}
			
			// Gerar número de documento para conta a pagar automatico
			nmParametro = "LG_NUM_DOC_CONTA_PAGAR_AUTOMATICO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: número de documento automatico para conta pagar", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			
			// Gerar número de conta a receber automatico
			nmParametro = "LG_NUM_DOC_CONTA_RECEBER_AUTOMATICO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Financeiro: número de documento automatico para conta receber", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			
			// Desabilitar seleao de categorias raiz para classificaçã de contas e movimentaçoes
			insertUpdate(new Parametro(0, 0, "LG_DESABILITAR_SELECAO_CATEGORIA_RAIZ", LOGICO, CHECK_BOX,
					"Financeiro: Desabilitar seleção de categorias raiz para classificação de contas e movimentações", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Categoria econômica default para classificaï¿½ï¿½o de despesas
			insertUpdate(new Parametro(0, 0, "CD_CATEGORIA_CUSTAS_REEMBOLSAVEIS", NUMERICO, FILTER,
					"Jurídico-Financeiro: Categoria de custas reembolsáveis",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		} 
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO FACTORING
	 ************************************************************************************************************/
	public static void initFac()		{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo  = ? ");
			pstmt.setString(1, "fac");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo  = isNext ? rs.getInt("cd_modulo") : 0;

			// Indica a carteira padrão
			String nmParametro	= "CD_CARTEIRA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Carteira",
					"{caption:\"Selecionando carteira\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.ContaCarteiraServices\",method: \"find\"," +
							" cdField: \"CD_CONTA_CARTEIRA\", dsField: \"NM_CARTEIRA\"," +
							" filterFields: [[{label:\"Carteira\",reference:\"nm_carteira\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Conta\",reference:\"NR_CONTA\"}," +
							"                        {label:\"Carteira\",reference:\"NM_CARTEIRA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Indica a carteira padrão
			nmParametro	= "CD_CONTA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Carteira - Conta",
					"{caption:\"Selecionando conta\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.ContaFinanceiraDAO\",method: \"find\"," +
							" cdField: \"CD_CONTA\", dsField: \"NM_CONTA\"," +
							" filterFields: [[{label:\"Conta\",reference:\"nm_conta\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nï¿½mero\",reference:\"NR_CONTA\"}," +
							"                        {label:\"Conta\",reference:\"NM_CONTA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Valor do limite por cliente
			nmParametro = "VL_LIMITE_FACTORING";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Valor do limite por cliente (R$)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Valor do limite por emitente
			nmParametro = "VL_LIMITE_FACTORING_EMISSOR";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Valor do limite por emitente (R$)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Valor do limite por cheque
			nmParametro = "VL_LIMITE_FACTORING_UNITARIO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Valor do limite por cheque (R$)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);			
			// Valor da taxa de devolução
			nmParametro = "VL_TAXA_DEVOLUCAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Taxa de devolução (R$)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Valor da taxa padrão
			nmParametro = "VL_TAXA_PADRAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Taxa padrão (%)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Valor da taxa juros
			nmParametro = "VL_TAXA_JUROS";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Taxa juros (%)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Valor da taxa prorrogaï¿½ï¿½o
			nmParametro = "VL_TAXA_PRORROGACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Taxa prorrogaï¿½ï¿½o (%)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Valor de ganho minimo
			nmParametro = "VL_GANHO_MINIMO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Valor de ganho mï¿½nimo (R$)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Valor da taxa minima
			nmParametro = "VL_TAXA_MINIMA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Taxa mï¿½nima (%)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Idade minima da conta
			nmParametro = "QT_IDADE_MINIMA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Idade mï¿½nima da conta (meses)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Prazo máximo
			nmParametro = "QT_PRAZO_MAXIMO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Prazo máximo (meses)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Prazo mï¿½nimo
			nmParametro = "QT_PRAZO_MINIMO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Prazo mï¿½nimo (meses)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Categoria econômica default para cheques a vista - Despesa
			nmParametro	= "CD_CATEGORIA_ECONOMICA_CHEQUE_VISTA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Cheque a vista (Despesa)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Categoria econômica default para o desconto no cheques a vista - Receita
			nmParametro	= "CD_CATEGORIA_ECONOMICA_DESCONTO_CHEQUE_VISTA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Desconto no cheque a vista (Receita)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Categoria econômica default para cheques a vista - Receita
			nmParametro	= "CD_CATEGORIA_ECONOMICA_CHEQUE_A_PRAZO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Cheque a prazo (Receita)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Categoria econômica default para juros por atraso - Receita
			nmParametro	= "CD_CATEGORIA_ECONOMICA_JUROS_ATRASO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Juros por atraso (Receita)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Categoria econômica default para multa por devolucao - Receita
			nmParametro	= "CD_CATEGORIA_ECONOMICA_MULTA_DEVOLUCAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Multa por devolução (Receita)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Categoria econômica default para multa por devolucao - Receita
			nmParametro	= "CD_CATEGORIA_ECONOMICA_MULTA_PRORROGACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Multa por prorrogaï¿½ï¿½o (Receita)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Categoria econômica default da soma liquida dos cheques a receber que deseja abater para um novo cheque a pagar - Receita
			nmParametro	= "CD_CATEGORIA_ECONOMICA_DESCONTO_COMPENSACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Desconto na compensaï¿½ï¿½o (Receita)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Categoria econômica default para o balanco ao se descontar cheques a receber de um cheque a pagar - DESPESA
			nmParametro	= "CD_CATEGORIA_ECONOMICA_DESCONTO_BALANCO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Desconto na compensaï¿½ï¿½o (Despesa)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Permitir que o sistema os valores caso ultrapasse ou não chegue aos valores estabelecidos na configuracao
			insertUpdate(new Parametro(0, 0, "LG_PERMITIR_SUBSTITUICAO", LOGICO, CHECK_BOX, "Permitir valores serem substituidos automaticamente caso não estejam na faixa de configuraï¿½ï¿½o?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Decidir se os parametros gerais de valores sao obrigatorios (caso nao haja configuracao para o cliente)
			insertUpdate(new Parametro(0, 0, "LG_PARAMETROS_OBRIGATORIOS", LOGICO, CHECK_BOX, "Parametros terï¿½o que ser configurados obrigatoriamente?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Decidir se bloqueia o cliente apás 2 devoluï¿½ï¿½es
			insertUpdate(new Parametro(0, 0, "LG_BLOQUEAR_CLIENTE_DEVOLUCAO", LOGICO, CHECK_BOX, "Bloquear cliente apás 2 devoluï¿½ï¿½es?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Decidir se bloqueia o emitente apás 2 devoluï¿½ï¿½es
			insertUpdate(new Parametro(0, 0, "LG_BLOQUEAR_EMITENTE_DEVOLUCAO", LOGICO, CHECK_BOX, "Bloquear emitente apás 2 devoluï¿½ï¿½es?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Pedir autorização para emitir cheque de cliente e emitente bloqueado
			insertUpdate(new Parametro(0, 0, "LG_AUTORIZACAO_CHEQUE_PESSOA_BLOQUEADA", LOGICO, CHECK_BOX, "Pedir autorização para emitir cheque de cliente/emitente bloqueado?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Nï¿½ Agï¿½ncia Padrão da Empresa
			nmParametro = "NR_AGENCIA_FACTORING";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Nï¿½ Agï¿½ncia Padrão da Empresa", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Forma pagamento para compensaï¿½ï¿½o
			nmParametro	= "CD_FORMA_PAGAMENTO_COMPENSACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Forma de pagamento para compensaï¿½ï¿½o de cheques",
					"{caption:\"Selecionando forma de pagamento\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.FormaPagamentoDAO\",method: \"find\"," +
							" cdField: \"CD_FORMA_PAGAMENTO\", dsField: \"NM_FORMA_PAGAMENTO\"," +
							" filterFields: [[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Configuração de Tipo de Documento para custï¿½dia
			nmParametro	= "CD_TIPO_DOCUMENTO_CUSTODIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de documento para custï¿½dia",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tipo de Conta a Receber da conta a receber da custï¿½dia
			nmParametro = "TP_CONTA_RECEBER_CUSTODIA";
			int cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, COMBO_BOX,
					"Tipo de Conta a Receber - Custï¿½dia", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] labels = ContaReceberServices.tiposContaReceber;
				String[] values = {"" + ContaReceberServices.TP_TAXA_ADESAO, "" + ContaReceberServices.TP_PARCELA, "" + ContaReceberServices.TP_MANUTENCAO, "" + ContaReceberServices.TP_RESCISAO_CONTRATUAL, "" + ContaReceberServices.TP_NEGOCIACAO, "" + ContaReceberServices.TP_OUTRO};
				for (int i=0; i<labels.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, labels[i] /*vlApresentacao*/, values[i] /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			// Categoria econômica default da soma liquida dos cheques a receber que deseja abater para um novo cheque a pagar - Receita
			nmParametro	= "CD_CATEGORIA_ECONOMICA_DESCONTO_CUSTODIA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Taxa da custodia (Despesa)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Categoria econômica default para cheques a vista - Despesa
			nmParametro	= "CD_CATEGORIA_ECONOMICA_CUSTODIA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Custï¿½dia (Receita)",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			

		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}	

	/************************************************************************************************************
	 * MÓDULO POSTOS DE COMBUSTï¿½VEIS
	 ************************************************************************************************************/
	public static void initPcb()		{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo  = ? ");
			pstmt.setString(1, "pcb");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo  = isNext ? rs.getInt("cd_modulo") : 0;
			// Natureza da Operação [CFOP] - Venda Combustï¿½vel
			insertUpdate(new Parametro(0, 0, "CD_NATUREZA_OPERACAO_COMBUSTIVEL", NUMERICO, FILTER,
					"Natureza da Operação - Venda de Combustï¿½vel",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Nï¿½ LMC (Livro)
			String nmParametro = "NR_LMC";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Nï¿½ Livro LMC", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Nï¿½ Folha LMC
			nmParametro = "NR_FOLHA_LMC";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Nï¿½ Folha LMC", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Grupo de Combustï¿½vel
			nmParametro	= "CD_GRUPO_COMBUSTIVEL";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER, "Grupo de Combustï¿½veis",
					"{caption:\"Grupo de combustï¿½veis\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.alm.GrupoDAO\", method:\"find\"," +
							" cdField: \"CD_GRUPO\", dsField: \"NM_GRUPO\"," +
							" filterFields: [[{label:\"Grupo de Combustivel\", reference:\"nm_grupo\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo de Combustivel\", reference:\"NM_GRUPO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Grupo de Combustï¿½vel
			nmParametro	= "CD_GRUPO_LUBRIFICANTE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER, "Grupo de Lubrificantes",
					"{caption:\"Grupo de Lubrificantes\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.alm.GrupoDAO\", method:\"find\"," +
							" cdField: \"CD_GRUPO\", dsField: \"NM_GRUPO\"," +
							" filterFields: [[{label:\"Grupo de Combustivel\", reference:\"nm_grupo\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo de Combustivel\", reference:\"NM_GRUPO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tabela Preï¿½o Padrão ï¿½ Vista
			nmParametro	= "CD_TABELA_PRECO_AVISTA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER, "Tabela de Preï¿½o a Vista",
					"{caption:\"Tabela de Preï¿½o a Vista\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.TabelaPrecoDAO\", method:\"find\"," +
							" cdField: \"CD_TABELA_PRECO\", dsField: \"NM_TABELA_PRECO\"," +
							" filterFields: [[{label:\"Tabela Preï¿½o\", reference:\"nm_tabela_preco\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Tabela Preï¿½o\", reference:\"NM_TABELA_PRECO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			connect.setAutoCommit(true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO VENDAS
	 ************************************************************************************************************/
	public static void initVendas()		{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo  = ? ");
			pstmt.setString(1, "vnd");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo  = isNext ? rs.getInt("cd_modulo") : 0;
			// Vendedor Obrigatï¿½rio (PDV)
			insertUpdate(new Parametro(0, 0, "LG_RELATORIO_ORDENADO_GRUPO", LOGICO, CHECK_BOX, "Relatï¿½rio ordenado por grupo no PDV", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vendedor Obrigatï¿½rio (PDV)
			insertUpdate(new Parametro(0, 0, "LG_VENDEDOR_OBRIGATORIO_PDV", LOGICO, CHECK_BOX, "Vendedor obrigatï¿½rio no PDV", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Cliente Obrigatï¿½rio (PDV)
			insertUpdate(new Parametro(0, 0, "LG_CLIENTE_OBRIGATORIO_PDV", LOGICO, CHECK_BOX, "Cliente obrigatï¿½rio no PDV", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Fechamento de Caixa Supervisionado
			insertUpdate(new Parametro(0, 0, "LG_FECHAMENTO_CAIXA_SUPERVISOR", LOGICO, CHECK_BOX,
					"Exige supervisï¿½o no fechamento do caixa", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Ignorar situação do caixa
			insertUpdate(new Parametro(0, 0, "LG_IGNORAR_SITUACAO_CAIXA", LOGICO, CHECK_BOX,
					"Financeiro: Ignorar situação do caixa para alterações nas movimentações", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Natureza da Operação - Varejo (PDV)
			insertUpdate(new Parametro(0, 0, "CD_NATUREZA_OPERACAO_VAREJO", NUMERICO, FILTER,
					"Natureza da Operação - Varejo",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Modelo de Documento Padrão para impressão de pedidos de vendas
			insertUpdate(new Parametro(0, 0, "CD_MODELO_DOC_PEDIDO_VENDA", NUMERICO, FILTER,
					"Modelo Documento Padrão para impressão de Pedidos de Vendas",
					"{caption:\"Selecionando Modelo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.ModeloDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_MODELO\", dsField: \"NM_MODELO\"," +
							" filterFields: [[{label:\"Modelo de Documento\", reference:\"NM_MODELO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Modelo de Documento\",reference:\"NM_MODELO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Natureza da Operação - Atacado (PDV)
			insertUpdate(new Parametro(0, 0, "CD_NATUREZA_OPERACAO_ATACADO", NUMERICO, FILTER,
					"Natureza da Operação - Atacado",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tipo de Operação - Varejo (PDV)
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TIPO_OPERACAO_VAREJO", NUMERICO, FILTER,
					"Tipo de Operação - Varejo",
					"{caption: \"Selecionando Tipo de Operação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.TipoOperacaoDAO\", method: \"find\"," +
							" cdField: \"CD_TIPO_OPERACAO\", dsField: \"NM_TIPO_OPERACAO\"," +
							" filterFields: [[{label: \"Tipo de Operação\", reference: \"nm_tipo_operacao\", datatype: _VARCHAR, comparator: _LIKE_ANY, width: 100}]]," +
							" gridOptions: {columns: [{label: \"Tipo de Operação\", reference: \"NM_TIPO_OPERACAO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tipo de Operação - Atacado (PDV)
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TIPO_OPERACAO_ATACADO", NUMERICO, FILTER,
					"Tipo de Operação - Atacado",
					"{caption:\"Selecionando Tipo de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OPERACAO\", dsField: \"NM_TIPO_OPERACAO\"," +
							" filterFields: [[{label:\"Tipo de Operação\", reference:\"nm_tipo_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Operação\",reference:\"NM_TIPO_OPERACAO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Indica a carteira padrão para lanï¿½amento de conta a receber geradas no PDV
			insertUpdate(new Parametro(0, 0, "CD_CARTEIRA_PDV", NUMERICO, FILTER,
					"Carteira PDV",
					"{caption:\"Selecionando carteira\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.ContaCarteiraServices\",method: \"find\"," +
							" cdField: \"CD_CONTA_CARTEIRA\", dsField: \"NM_CARTEIRA\"," +
							" filterFields: [[{label:\"Carteira\",reference:\"nm_carteira\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Conta\",reference:\"NR_CONTA\"}," +
							"                        {label:\"Carteira\",reference:\"NM_CARTEIRA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Indica local de armazenamento do PDV
			insertUpdate(new Parametro(0, 0, "CD_LOCAL_ARMAZENAMENTO_PDV", NUMERICO, FILTER,
					"Local de Armazenamento PDV",
					"{caption:\"Selecionando o local de armazenamento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.alm.LocalArmazenamentoServices\",method: \"findCompleto\"," +
							" cdField: \"CD_LOCAL_ARMAZENAMENTO\", dsField: \"NM_LOCAL_ARMAZENAMENTO\"," +
							" filterFields: [[{label:\"Local de Armazenamento\",reference:\"nm_local_armazenamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Local de Armazenamento\",reference:\"NM_LOCAL_ARMAZENAMENTO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tipo de Operação - Pedido de Vendas
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TIPO_OPERACAO_PEDIDO_VENDA", NUMERICO, FILTER,
					"Tipo de Operação - Pedido de Venda",
					"{caption:\"Selecionando Tipo de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OPERACAO\", dsField: \"NM_TIPO_OPERACAO\"," +
							" filterFields: [[{label:\"Tipo de Operação\", reference:\"nm_tipo_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Operação\",reference:\"NM_TIPO_OPERACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Natureza da Operação - Pedido de Vendas
			String nmParametro	= "CD_NATUREZA_OPERACAO_PEDIDO_VENDA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Natureza da Operação - Pedido de Venda",
					"{caption:\"Selecionando Natureza da Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Percentual de desconto geral
			nmParametro = "PR_DESCONTO_MAXIMO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"% Desconto Geral (Max. Permitido)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Valor máximo para cupons
			nmParametro = "VL_MAXIMO_CUPOM";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Valor máximo para cupom", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Configuração de PRï¿½-VENDA (ECF)
			nmParametro = "TP_EMISSAO_DAV";
			connect.setAutoCommit(false);
			int cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, COMBO_BOX,
					"Emissï¿½o de Documento Auxiliar de Venda - DAV", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			if (cdParametro > 0) {
				String[] labels = {"não Emite DAV", "DAV por ECF", "Impressora não Fiscal"};
				String[] values = {"0", "1", "2"};
				for (int i=0; i<labels.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, labels[i] /*vlApresentacao*/, values[i] /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			/*
			 *  Configurações de Balanï¿½a
			 */
			// Dï¿½gito de (2 a 9) que identifique que o cï¿½digo de barras ï¿½ de balanï¿½a
			insertUpdate(new Parametro(0, 0, "NR_IDENTIFICADOR_CB_BALANCA", NUMERICO, SINGLE_TEXT, "BALANï¿½A: Dï¿½gito inicial do Cï¿½d. de Barras", "2",
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tamanho do Cï¿½d. do produto no Cï¿½d. de Barras da Balanï¿½a
			insertUpdate(new Parametro(0, 0, "QT_CODIGO_PRODUTO_BALANCA", NUMERICO, SINGLE_TEXT, "BALANï¿½A: Tamanho do Cï¿½d. do Produto no Cï¿½d. de Barras (4 a 6)", "4",
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Dados da Segunda parte do Cï¿½d. de Barras
			nmParametro = "TP_VALOR_BALANCA";
			connect.setAutoCommit(false);
			cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, COMBO_BOX,
					"BALANï¿½A: Dado contigo na 2ï¿½ parte do Cï¿½d. de Barras", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			if (cdParametro > 0) {
				String[] labels = {"Total a Pagar", "Quantidade / Peso"};
				String[] values = {"0", "1"};
				for (int i=0; i<labels.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, labels[i] /*vlApresentacao*/, values[i] /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			// Configuração de PRï¿½-VENDA (ECF)
			nmParametro = "TP_APLICACAO_TABELA_PRECO";
			cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, COMBO_BOX,
					"Como aplicar tabela de preï¿½o difenciada/promocional", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			if (cdParametro > 0) {
				String[] labels = {"Substituindo a Original", "Como Desconto ou Acráscimo"};
				String[] values = {"0", "1"};
				for (int i=0; i<labels.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, labels[i] /*vlApresentacao*/, values[i] /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			//
			connect.commit();
			connect.setAutoCommit(true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO MICROCRï¿½DITO
	 ************************************************************************************************************/
	public static void initMcr()		{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "mcr");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			// Vínculo - Agente de crédito
			String nmParametro = "CD_VINCULO_AGENTE_CREDITO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Vínculo - Agente de Crï¿½dito",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Vínculo - Avalista
			nmParametro = "CD_VINCULO_AVALISTA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Vínculo - Avalista",
					"{caption:\"Selecionando o vinculo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_EVENTO_FINANCEIRO_CAPITAL";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Evento Financeiro - Capital",
					"{caption:\"Selecionando evento financeiro\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.EventoFinanceiroDAO\",method:\"find\"," +
							" cdField: \"CD_EVENTO_FINANCEIRO\", dsField: \"NM_EVENTO_FINANCEIRO\"," +
							" filterFields: [[{label:\"Evento Financeiro\", reference:\"nm_evento_financeiro\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Evento Financeiro\",reference:\"NM_EVENTO_FINANCEIRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_EVENTO_FINANCEIRO_JUROS";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Evento Financeiro - Juros",
					"{caption:\"Selecionando evento financeiro\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.EventoFinanceiroDAO\",method:\"find\"," +
							" cdField: \"CD_EVENTO_FINANCEIRO\", dsField: \"NM_EVENTO_FINANCEIRO\"," +
							" filterFields: [[{label:\"Evento Financeiro\", reference:\"nm_evento_financeiro\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Evento Financeiro\",reference:\"NM_EVENTO_FINANCEIRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_EVENTO_FINANCEIRO_IOF";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Evento Financeiro - IOF",
					"{caption:\"Selecionando evento financeiro\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.EventoFinanceiroDAO\",method:\"find\"," +
							" cdField: \"CD_EVENTO_FINANCEIRO\", dsField: \"NM_EVENTO_FINANCEIRO\"," +
							" filterFields: [[{label:\"Evento Financeiro\", reference:\"nm_evento_financeiro\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Evento Financeiro\",reference:\"NM_EVENTO_FINANCEIRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	/************************************************************************************************************
	 * MÓDULO MOBILIDADE URBANA
	 ************************************************************************************************************/
	public static void initMob(){
		Connection connect = Conexao.conectar();
		
		GrupoParametroServices _gpService = new GrupoParametroServices();
		ParametroGrupoServices _pgService = new ParametroGrupoServices();
		_pgService.resetParametrosGrupo(connect);
		_gpService.resetGruposParametro(connect);
		
		_gpService.initGruposMob();		
		initPermissoesMob(connect);
		
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "mcr");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			
			int grupo = -1;
			int cdParametro = -1;
			
			// DADOS ORGAO
			grupo = DADOS_ORGAO;
			insertUpdateMob(createParametroMob("NM_ORGAO_AUTUADOR", "Órgão autuador", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("SG_ORGAO", "Sigla do órgão autuador", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_SUBORGAO", "Nome do Subórgão autuador", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_DEPARTAMENTO", "Nome do Departamento", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("SG_DEPARTAMENTO", "Sigla do Departamento", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_CARGO", "Cargo do responsável pelo órgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_DIRETOR_JARI", "Nome do diretor da JARI", STRING, SINGLE_TEXT, connect), grupo, connect);
			// insertUpdateMob(createParametroMob("VL_UFIR", "Valor da UFIR", NUMERICO, SINGLE_TEXT), DADOS_ORGAO, connect);
			insertUpdateMob(createParametroMob("MOB_CD_ORGAO_AUTUADOR", "Código do órgão autuador", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_LOGRADOURO", "Rua do endereço do órgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NR_ENDERECO", "Número do endereço do órgão", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_COMPLEMENTO", "Complemento do endereço do órgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_PONTO_REFERENCIA", "Ponto de referência do órgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_BAIRRO", "Bairro do órgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NR_CEP", "CEP do órgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_CIDADE", "Cidade do órgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NR_TELEFONE", "Telefone Principal", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NR_TELEFONE2", "Telefone secundário", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_EMAIL", "Email do órgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("DS_ENDERECO", "Endereço completo do órgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_CD_ORGAO_SERVICO_CHAVE", "Nome do serviço de envio de AITs", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("URL_SICRONIZACAO_MARCA_MODELO_PRODEMGE", "URL de sincronização de Marca/Modelo PRODEMGE", STRING, SINGLE_TEXT, connect), grupo, connect);
			
			insertUpdateMob(createParametroMob("NM_DISPOSITIVO", "Nome do Dispositivo", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("TP_IMPRESSORA", "Tipo de Impressora", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_TITULO_IMPRESSAO_1", "Título da Impressão 01", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_TITULO_IMPRESSAO_2", "Título da Impressão 02", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_TITULO_IMPRESSAO_3", "Título da Impressão 03", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NR_VERSAO", "Número da Versão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_IMPRESSORA", "Nome da Impressora", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_ORGAO_AUTUADOR", "Código do Orgão Autuador", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_MUNICIPIO_AUTUADOR", "Nome do Município Autuador", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_CADASTRO_HORA_INFRACAO_ALTERAVEL", "Booleano Cadastro da Hora de Infração Alteravel", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NR_AIT", "Número do AIT", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NR_BOAT", "Número do Boat", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NR_RRD", "Número do RRD", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NR_TRRAV", "Número do TRRAV", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_BOAT", "Booleano do Boat", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_RRD", "Booleano do RRD", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_TRRAV", "Booleano do TRRAV", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_ORGAO", "Nome do Orgão", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_MUNICIPIO", "eTransporte - Nome do munícipio", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_CLIENTE_ECARTAS", "Cliente eCartas", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("PTC_NM_EMISSOR_RESPONSAVEL", "Nome do responsável por assinar credenciais de estacionamento", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("PTC_TEXT_SOLICITACAO_CREDENCIAL_IDOSO", "Texto com informações para solicitação de credencial de estacionamento - Idoso", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("PTC_TEXT_SOLICITACAO_CREDENCIAL_DEFICIENTE", "Texto com informações para solicitação de credencial de estacionamento - Deficiênte", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("PTC_TEMPLATE_COMPROVANTE_CREDENCIAL_DEFICIENTE", "Mensagem de comprovação da solicitação de credencial de estacionamento", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("PTC_TEMPLATE_DEFERIMENTO_CREDENCIAL_DEFICIENTE", "Mensagem de deferimento da solicitação de credencial de estacionamento", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("PTC_TEMPLATE_INDEFERINEMTO_CREDENCIAL_DEFICIENTE", "Mensagem de indeferimento da solicitação de credencial de estacionamento", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("FOOTER_EMAIL", "Rodapé do Email", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("HEADER_EMAIL", "Cabeçalho do Email", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_CEP_FIXO", "Cep fixo para solicitação de credencial - Idoso/PCD", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("TP_CONVENIO_PADRAO", "Convênio padrão para o Talonário Eletrônico (Ref: mob.AitServices.TP_CONVENIO_*)", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			
			// OUTRAS INFORMACOES
			grupo = OUTRAS_INFORMACOES;
			insertUpdateMob(createParametroMob("CD_OCORRENCIA_CANCELAMENTO_NIP", "Código: ocorrência de cancelamento de nip ", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_OCORRENCIA_CANCELAMENTO", "Código: ocorrência de cancelamento ", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_CORREIOS_NR_CONTRATO", "N° Contrato Correios", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_CORREIOS_SG_CHANCELA", "Descrição Chancela", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_CORREIOS_NR_CODIGO_CLIENTE", "Código do cliente", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_CORREIOS_SG_CLIENTE", "Sigla do cliente nos Correios", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_URL_FTP_DETRAN", "Endereço IP FTP DETRAN", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_NM_USUARIO_FTP_DETRAN", "Usuário FTP DETRAN", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_NM_SENHA_FTP_DETRAN", "Senha FTP DETRAN", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_CORREIOS_NR_CARTAO_POSTAGEM", "Número do cartão postagem", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PRAZOS_NR_DEFESA_PREVIA", "Prazo para envio de Defesa Prévia", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PRAZOS_NR_RECURSO_JARI", "Prazo para envio de recruso na JARI", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NR_BANCO_CREDITO", "Banco para recebimento de pagamentos", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NR_AGENCIA_CREDITO", "Banco para recebimento de pagamentos", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NR_CONTA_CREDITO", "Conta para recebimento de pagamentos", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NR_CD_FEBRABAN", "Código FEBRABAN", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_CORREIOS_MATRIZ_NAI", "Código Matriz NAI", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_CORREIOS_MATRIZ_NIP", "Código Matriz NIP", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_ECARTA_ENVIO", "Tipo de envio eCarta", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_AITS_NAO_ENTREGUES", "Publicar AITs não entregues", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("mob_tipo_arquivo_publicacao_nai", "Identifica qual o código do tipo de arquivo para NAI", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("mob_tipo_arquivo_publicacao_nip", "Identifica qual o código do tipo de arquivo para NIP", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_ENVIO_CORREIOS", "Identifica qual o tipo de envio dos Correios", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_AVISO_RECEBIMENTO_CORREIOS", "Identifica Tipos de Aviso de Recebimento para envio de cartas", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_LOGO_RECEBIMENTO_AR_DIGITAL", "Imagem para o aviso de recebimento digital", IMAGEM, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_LOGO_CORREIOS_CHANCELA", "Imagem com a logo dos correios e nome 'Correios'", IMAGEM, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_ARQUIVO_PUBLICACAO_PROCESSOS", "Identifica qual o código do tipo de arquivo para processos", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_ARQUIVO_PUBLICACAO_NOTIFICACOES", "Identifica qual o código do tipo de arquivo para publicação de notificações", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_OFICIO_DOCUMENTO_EXTERNO", "Identifica qual o código do tipo de arquivo para oficio documento externo", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_OCORRENCIA_DEFERIDA_DOCUMENTO", "Código da Ocorrencia Deferida do Documento", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_OCORRENCIA_INDEFERIDA_DOCUMENTO", "Código da Ocorrencia Indeferida do Documento", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_SITUACAO_DOCUMENTO_JULGADO", "Código da Situação do Documento Julgado", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_FASE_DEFERIDA", "Código da Fase Deferida", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_FASE_INDEFERIDA", "Código da Fase Indeferida", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PRAZO_DEFESA", "Prazo extra para defesa", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_USER_TIVIC", "Identifica qual o código do usuário TIVIC no cliente", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_USER_EDAT", "Identifica qual o código do usuário EDAT no cliente", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NM_SISTEMA_ETRANSITO", "Identifica qual o nome do sistema que está sendo utilizado para impressão de NAI/NIP", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_ENVIO_CORREIOS_NAI", "Identifica qual o tipo de envio dos Correios para NAI", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_ENVIO_CORREIOS_NIP", "Identifica qual o tipo de envio dos Correios para NIP", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_SITUACAO_DOCUMENTO_CANCELADO", "Código da Situação do Documento Cancelado", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", "Mensagem para Defesa Indeferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_BLB_DEFESA_PREVIA_DEFERIDA", "Mensagem para Defesa Deferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_BLB_JARI_SEM_PROVIMENTO", "Mensagem para JARI Indeferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_BLB_JARI_COM_PROVIMENTO", "Mensagem para JARI Deferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("mob_apresentar_observacao", "Apresentar mensagem de Observação", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_ARQUIVO_PREVISAO_POSTAGEM_CORREIOS", "Identifica qual o código do tipo de arquivo para previsão de postagem nos correios", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_ARQUIVO_LISTA_POSTAGEM_CORREIOS", "Identifica qual o código do tipo de arquivo para lista de postagem nos correios", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_HABILITACAO_EX", "Código do Estado da Habilitação Estrangeira", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICACAO_NAI_NAO_ENTREGUE", "Diário Oficial NAI - Selecionar opção não entregue?", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICACAO_NIP_NAO_ENTREGUE", "Diário Oficial NIP - Selecionar opção não entregue?", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICACAO_NIP_TODAS_SITUACOES", "Diário Oficial NIP - Habilitar publicação para todas situações", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_ARQUIVO_BAIXA_BANCARIA", "Código do Tipo de Arquivo de Pagamento.", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SNE_ADESAO_ORGAO_AUTUADOR", "Optante ao SNE", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_NIPS", "Publicação edital NIP", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_NAIS", "Publicação edital NAI", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_DEFESA_DEFERIDA", "Texto publicação Defesa deferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_DEFESA_INDEFERIDA", "Texto publicação Defesa indeferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_JARI_DEFERIDA", "Texto publicação JARI deferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_JARI_INDEFERIDA", "Texto publicação JARI indeferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_DEFESA_ADVERTENCIA_DEFERIDA", "Texto publicação Defesa Advertência deferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_DEFESA_ADVERTENCIA_INDEFERIDA", "Texto publicação Defesa Advertência indeferida", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_EQUIPAMENTO_VIDEOMONITORAMENTO", "Código do equipamento videomonitoramento", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NR_CODIGO_AVANCO_SINAL_VERMELHO", "Código do Detran para avanço de sinal vermelho", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NR_CODIGO_PARADA_SOBRE_FAIXA", "Código do Detran para parada sobre a faixa", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("mob_impressao_tp_modelo_nai", "Tipo de Modelo de Envio NAI", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("mob_impressao_tp_modelo_nip", "Tipo de Modelo de Envio NIP", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_ARQUIVO_ENVIO_DETRAN", "Identifica qual o código do tipo de arquivo para envio detran", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_EVENTO_ESTACIONAMENTO_DIGITAL", "Código do tipo evento estacionamento digital", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TP_ZONA_AZUL", "Código para identificar a zona azul", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_EVENTO_ESTACIONAMENTO_LIMIT_SYNC", "Limite de quntidade de eventos por syncronização com APP", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_EVENTO_ESTACIONAMENTO_TEMPO_LIMIT", "Tempo limite para o agente verificar a notificação.", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_IMPRESSAO_RODAPE_APP_AIT", "Mensagem exibida no rodapé do AIT impresso", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("GRL_TIPO_ARQUIVO_CANCELAMENTO", "Tipo de arquivo para cancelamento do AIT", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_CONVENIO", "Habilita ou desabilita a listagem de convênios no app", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_OBRIGAR_CONSISTENCIA_AIT", "Habilita ou desabilita a confirmação de consistencia dos autos", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_OBRIGAR_CONSISTENCIA_RADAR", "Determina a confirmação de consistencia dos autos de radar", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_OBRIGAR_CONSISTENCIA_VM", "Determina a confirmação de consistencia dos autos do VM", LOGICO, CHECK_BOX, connect), grupo, connect);

			cdParametro = insertUpdateMob(createParametroMob("MOB_CORREIOS_TP_ENVIO_NAI", "Envio NAI", STRING, LIST_BOX, connect),grupo, connect);
			if (cdParametro > 0) {
				String[] options = { "AR Digital", "Rem. Econômica", "Carta Registrada", "Carta Simples", "Outros" };
				_service.saveOpcoesLimpoArray(cdParametro, options);
			}			
			cdParametro = insertUpdateMob(createParametroMob("MOB_CORREIOS_TP_ENVIO_NIP", "Envio NIP", STRING, LIST_BOX, connect),grupo, connect);
			if (cdParametro > 0) {
				String[] options = { "AR Digital", "Rem. Econômica", "Carta Registrada", "Carta Simples", "Outros" };
				_service.saveOpcoesLimpoArray(cdParametro, options);
			}			
			cdParametro = insertUpdateMob(createParametroMob("MOB_CORREIOS_TP_ENVIO_RESULTADOS", "Envio dos Resultados", STRING, LIST_BOX, connect),grupo, connect);
			if (cdParametro > 0) {
				String[] options = { "AR Digital", "Rem. Econômica", "Carta Registrada", "Carta Simples", "Outros" };
				_service.saveOpcoesLimpoArray(cdParametro, options);
			}			
			cdParametro = insertUpdateMob(createParametroMob("LG_DESCONTO_BOLETO", "Desconto Boleto", STRING, LIST_BOX, connect),grupo, connect);
			if (cdParametro >= 0) {
				String[] options = { "Sem desconto no valor do boleto", "20% de desconto no valor do boleto" };
				_service.saveOpcoesLimpoArray(cdParametro, options);
			}
			
			// MARCAS E CONFIG.
			grupo = MARCAS_CONFIG;			
			insertUpdateMob(createParametroMob("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", "Logo da prefeitura", IMAGEM, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", "Logo do departamento", IMAGEM, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", "Chancela correios", IMAGEM, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_IMPRESSOS_BLB_LOGO_CETRAN", "Logo CETRAN", IMAGEM, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_IMPRESSOS_DS_TEXTO_NAI", "Texto impresso em NAI", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_IMPRESSOS_DS_TEXTO_NIP", "Texto impresso em NIP", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_IMPRESSOS_DS_TEXTO_NOTIFICACAO_ADVERTENCIA", "Texto impresso em Notificação de Advertência", STRING, MULTI_TEXT, connect),grupo, connect);
			cdParametro = insertUpdateMob(createParametroMob("MOB_IMPRESSOS_TP_MODELO_NAI", "Tipo Modelo NAI", NUMERICO, LIST_BOX, connect),grupo, connect);
			if (cdParametro > 0) {
				String[] options = { "Modelo com AR", "Modelo sem AR" };
				_service.saveOpcoesLimpoArray(cdParametro, options);
			}			
			cdParametro = insertUpdateMob(createParametroMob("MOB_IMPRESSOS_TP_MODELO_NIP", "Tipo Modelo NIP", NUMERICO, LIST_BOX, connect),grupo, connect);
			if (cdParametro > 0) {
				String[] options = { "Modelo com AR", "Modelo sem AR" };
				_service.saveOpcoesLimpoArray(cdParametro, options);
			}
			insertUpdateMob(createParametroMob("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", "Texto de mensagem educativa em NAI", STRING, MULTI_TEXT, connect),grupo, connect);
			cdParametro = insertUpdateMob(createParametroMob("MOB_LOCAL_IMPRESSAO", "Local Impressão", NUMERICO, LIST_BOX, connect),grupo, connect);
			if (cdParametro > 0) {
				String[] options = { "Impressão Local", "Impressão Externa", "eCartas" };
				_service.saveOpcoesLimpoArray(cdParametro, options);
			}
			
			// MOBILIDADE
			grupo = MOBILIDADE;
			insertUpdateMob(createParametroMob("DS_TITULO_1", "Linha 1", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("DS_TITULO_2", "Linha 2", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("DS_TITULO_3", "Linha 3", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NM_SENHA_ADMINISTRATIVA", "Senha Administrativa", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_IMPRESSOS_NM_MUNICIPIO_AUTUADOR", "Município do órgão autuador", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_IMPRESSOS_NM_ORGAO_AUTUADOR", "Nome do órgão autuador", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("LG_PREENCHIMENTO_AUTOMATICO", "Preenchimento Automático", LOGICO, CHECK_BOX, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_AITS_NAI", "Texto de publicação no DO NAIs", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PUBLICAR_AITS_NIP", "Texto de publicação no DO NIPs", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TXT_CONDUTOR", "Texto de tempo limite de apresentação de condutor na NAI, localizado a baixo do FICI", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_CNH", "CNH", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_RG", "RG", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_PERMISSAO", "Permissão para Dirigir", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_CPF", "CPF", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_COMPROVANTE_RESIDENCIA", "Comprovante de residência", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_CRLV", "CRLV", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_NAI", "Notificação de Autuação de Infração", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_FORMULARIO_APRESENTACAO", "Formulário de Apresentação do Condutor", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_CARTAO", "Cartão de banco", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_COMPROVANTE_MULTA", "Comprovante da multa", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_RESULTADO_JULGAMENTO", "Resultado do julgamento", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("ECT_LG_AUTORIZACAO_CEP_INVALIDO", "Autorização cep inválido e-cartas correios", LOGICO, CHECK_BOX, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_ARQUIVO_DE_SERVICO", "eCarta arquivo de serviço", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_RECIBO", "eCarta arquivo de recibo", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_INCONSISTENCIA", "eCarta arquivo de inconsistência", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_RESPOSTA", "eCarta arquivo de resposta", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("BOAT_SITUACAO_SYNC", "Situação a ser setada ao sincroanizar BOAR com talonario", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PRAZO_EMISSAO_NIC", "Prazo limite para geração da NIC", NUMERICO, SINGLE_TEXT, connect),grupo, connect);	
			insertUpdateMob(createParametroMob("LINK_FTPS_CORREIOS_EDI", "Endereço para conexão com o sistema EDI", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("USUARIO_FTPS_CORREIOS_EDI", "Usuário do sistema EDI", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("PASSWORD_CORREIOS_EDI", "Senha do sistema EDI", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PERIODICIDADE_JULGAMENTO_JARI", "Período (anos) para cálculo da media julgamento JARI ", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PERIODICIDADE_JULGAMENTO_DEFESA", "Período (anos) para cálculo da media julgamento Defesa ", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PERIODICIDADE_ESPERA_JARI", "Período (anos) para cálculo tempo de espera JARI ", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PERIODICIDADE_ESPERA_DEFESA", "Período (anos) para cálculo tempo de espera Defesa ", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("LG_PLACA_ZONA_AZUL_ALTERAVEL", "Permite a alteração da placa do evento de zona azul", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_ENDERECO_ZONA_AZUL_ALTERAVEL", "Permite a alteração do endereço do evento de zona azul", LOGICO, CHECK_BOX, connect), grupo, connect);
			
			// Portal do Cidadão
			grupo = PORTAL_CIDADAO;
			insertUpdateMob(createParametroMob("LG_FICI_FORA_DO_PRAZO", "Permitir apresentação de condutor fora do prazo - Portal", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_PORTAL_HOME_BLB_LOGO_PREFEITURA", "Logo da prefeitura para home - Portal", IMAGEM, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("PORTAL_LINK_SITE_ORGAO", "Link do site do órgão - Portal", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("PORTAL_LINK_EDAT", "Link do E-DAT - Portal", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("TXT_INSTRUCOES_FICI_PORTAL", "Instruções Apresentação de Condutor - Portal", STRING, MULTI_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("TXT_INSTRUCOES_DEFESA_PREVIA_PORTAL", "Instruções Defesa Prévia - Portal", STRING, MULTI_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("TXT_INSTRUCOES_JARI_PORTAL", "Instruções Recurso à JARI - Portal", STRING, MULTI_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("TXT_INSTRUCOES_CETRAN_PORTAL", "Instruções Recurso ao Cetran - Portal", STRING, MULTI_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("TXT_INSTRUCOES_CARTAO_IDOSO_PORTAL", "Instruções para cartão do idoso - Portal", STRING, MULTI_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("TXT_INSTRUCOES_CARTAO_PCD_PORTAL", "Instruções para cartão PcD - Portal", STRING, MULTI_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("TXT_EMAIL_PROTOCOLO_PORTAL", "Corpo do e-mail do envio de protocolo - Portal", STRING, MULTI_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_USER_PORTAL", "Identifica qual o código do usuário PORTAL no cliente", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NM_SISTEMA_PORTAL", "Identifica qual o nome do sistema que está sendo utilizado para impressão de NAI/NIP", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("DS_MENSAGEM_RECURSO_PORTAL", "Mensagem de prazo parar lançamento do recurso - Portal", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("LG_PORTAL_IMPRESSAO_NAI_NIP", "Identifica se existe notificação no AIT - Portal", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_PROCURACAO", "Procuração", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_CONTRATO_SOCIAL", "Contrato Social", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_ARQUIVO_EDITAL_PUBLICACAO", "Código do tipo de arquivo de Edital de Publicação", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_EDITAL_PUBLICACAO_PORTAL", "Ativação de publicação de edital no Portal", LOGICO, CHECK_BOX, connect),grupo, connect);
			insertUpdateMob(createParametroMob("LG_ENVIAR_MOTIVO_SOLICITACAO_EMAIL", "Habilita o envio do motivo de indeferimento", LOGICO, CHECK_BOX, connect),grupo, connect);
			insertUpdateMob(createParametroMob("QTD_DIAS_NOVA_SOLICITACAO", "Dias para solicitar cartão de PcD/idoso antes do vencimento", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_CTPS", "CTPS", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_ID_PROFISSIONAL", "Identidade profissional", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_FORMULARIO_VAGA_ESPECIAL", "Formulário de vaga especial", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_RELATORIO_MEDICO", "Relatório médico", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("TP_ARQUIVO_FORMULARIO_DEFESA", "Formulário de defesa", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			
			// GERADORES E SEQUENCIAIS
			grupo = GERADORES_SEQUENCIAIS;
			insertUpdateMob(createParametroMob("MOB_SIGLA_FICI", "Sigla FICI", STRING, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_SIGLA_DEFESA_PREVIA", "Sigla Defesa Prévia", STRING, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_SIGLA_DEFESA_ADVERTENCIA", "Sigla Defesa c/ Advertência", STRING, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_SIGLA_JARI", "Sigla Recurso JARI", STRING, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_SIGLA_CETRAN", "Sigla Recurso CETRAN", STRING, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_APRESENTACAO_CONDUTOR", "Sequencial para FICI", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_DEFESA_PREVIA", "Sequencial para Defesa Prévia", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_DEFESA_ADVERTENCIA", "Sequencial para Defesa c/ Advertência", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_JARI", "Sequencial para Recurso JARI", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_CETRAN", "Sequencial para Recurso CETRAN", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_NAI", "Sequencial para NAI", STRING, SINGLE_TEXT, connect),grupo, connect);
            insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_NIP", "Sequencial para NIP", STRING, SINGLE_TEXT, connect),grupo, connect);
			
			
			// PROTOCOLOS
		    grupo = PROTOCOLOS;
		    insertUpdateMob(createParametroMob("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", "Código FICI (Apresentação Condutor)", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_CD_TIPO_DOCUMENTO_DEFESA_PREVIA", "Código Defesa Prévia", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_CD_TIPO_DOCUMENTO_ADVERTENCIA_DEFESA", "Código Defesa (Advertência)", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_CD_TIPO_DOCUMENTO_JARI", "Código Recurso JARI", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_CD_TIPO_DOCUMENTO_CETRAN", "Código Recurso CETRAN", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_CD_TIPO_DOCUMENTO_ATA", "Código ATA", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_CD_TIPO_DOCUMENTO_CARTAO_IDOSO", "Código Cartão de Estacionamento - Idoso", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_CD_TIPO_DOCUMENTO_CARTAO_PCD", "Código Cartão de Estacionamento - PcD", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_CD_TIPO_INFRACAO_NIC", "Código de infração NIC", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_LG_IMPRIMIR_PROTOCOLO_GERAL", "Imprimir resultados de protocolos", LOGICO, CHECK_BOX, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("LG_LANCAR_MOVIMENTO_DOCUMENTO", "Envio automático ao DETRAN", LOGICO, CHECK_BOX, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_FASE_PENDENTE", "Código Fase: Pendente", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_FASE_DEFERIDO", "Código Fase: Deferido", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_FASE_INDEFERIDO", "Código Fase: Indeferido", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_APRESENTACAO_CONDUTOR", "Mensagem FICI Personalizada", STRING, MULTI_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_PUBLICAR_AITS_NAO_ENTREGUES", "Mensagem para publicação de AITs não entregues", STRING, MULTI_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("OCORRENCIA_LOTE_NAI", "Ocorrência de erro em lote NAI", STRING, MULTI_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("OCORRENCIA_LOTE_NIP", "Ocorrência de erro em lote NIP", STRING, MULTI_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("OCORRENCIA_LOTE_CANCELADO", "Ocorrência de erro em lote envio eCartas", STRING, MULTI_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("OCORRENCIA_ERRO_DETRAN", "Ocorrência de erro em lote envio DETRAN", STRING, MULTI_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("LG_GERAR_NR_DOCUMENTO", "Gerar número do documento de entrada", LOGICO, CHECK_BOX, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("LG_IMPRIMIR_CETRAN_DETALHADO", "Imprimir protocolo CETRAN detalhado", LOGICO, CHECK_BOX, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_SITUACAO_DOCUMENTO_DEFERIDO", "Código da Situação do Documento: Deferido", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_SITUACAO_DOCUMENTO_INDEFERIDO", "Código da Situação do Documento: Indeferido", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_FASE_JULGADO", "Código da Fase Julgado.", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_FASE_CANCELADO", "Código da Fase Cancelada.", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_TIPO_OCORRENCIA_DEFERIDA", "Código Tipo Ocorrencia Deferida.", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_TIPO_OCORRENCIA_INDEFERIDA", "Código Tipo Ocorrencia Ineferida.", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_OCORRENCIA_APRESENTACAO_CONDUTOR_ACEITO", "Código Apresentação Condutor Aceito", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_TIPO_OCORRENCIA_ENVIADO", "Código Tipo Ocorrencia Enviado.", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_TIPO_OCORRENCIA_AGUARDANDO_CONFIRMACAO", "Código Tipo Ocorrencia Aguardando confirmação.", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_TIPO_OCORRENCIA_RECEBIDO", "Código Tipo Ocorrencia Recebido.", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_TIPO_OCORRENCIA_PROTOCOLO_CANCELADO", "Código da Ocorrencia Protocolo Cancelado", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_OCORRENCIA_JULGAMENTO_CANCELADO", "Código da Ocorrencia Julgamento Cancelado", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_SITUACAO_PENDENTE", "Código da Situação do Documento Pendente", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("LG_GERAR_PROTOCOLO_REGRAS_ORGAO", "Gerar número de protocolos com regras", LOGICO, CHECK_BOX, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_GERADO", "Código da Ocorrencia Arquivo de Publicação Gerado.", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_ENVIADO", "Código da Ocorrencia Arquivo de Publicação Enviado.", STRING, SINGLE_TEXT, connect), grupo, connect);		
			insertUpdateMob(createParametroMob("MOB_TIPO_ARQUIVO_PUBLICACAO_PROTOCOLOS", "Código do tipo de Arquivo de Publicação Protocolos.", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_CD_TIPO_DOCUMENTO_OFICIO_EXTERNO", "Código do ofício externo", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_PUBLICACAO_NA", "Código para gerar o número de edital publicação NA.", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_PUBLICACAO_NP", "Código para gerar o número de edital publicação NP", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_PUBLICACAO_JARI_SEM_PROVIMENTO", "Código para gerar o número de edital publicação JARI sem provimento.", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_PUBLICACAO_JARI_COM_PROVIMENTO", "Código para gerar o número de edital publicação JARI com provimento.", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_PUBLICACAO_DEFESA_DEFERIDA", "Código para gerar o número de edital publicação DEFESA deferida.", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_PUBLICACAO_DEFESA_INDEFERIDA", "Código para gerar o número de edital publicação DEFESA indeferida.", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_PUBLICACAO_ADVERTENCIA_DEFERIDA", "Código para gerar o número de edital publicação ADVERTENCIA deferida.", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_PUBLICACAO_ADVERTENCIA_INDEFERIDA", "Código para gerar o número de edital publicação ADVERTENCIA indeferida.", NUMERICO, SINGLE_TEXT, connect), grupo, connect);
		    insertUpdateMob(createParametroMob("CD_VINCULO_AGENTE", "Vínculo Agente", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_VINCULO_RELATOR", "Vínculo Relator", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_VINCULO_PRESIDENTE_JARI", "Vínculo Presidente Jari", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("CD_VINCULO_SECRETARIO_JARI", "Vínculo Secretario Jari", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("LG_HABILITAR_LISTA_MEMBROS_JARI", "Habilita lista de membros da JARI", LOGICO, CHECK_BOX, connect),grupo, connect);
			insertUpdateMob(createParametroMob("LG_HABILITAR_IMPRESSAO_ORGAO", "Habilita impressão inserida pelo orgão", LOGICO, CHECK_BOX, connect),grupo, connect);
			
			// INCONSISTÊNCIAS
		    grupo = INCONSISTENCIAS;
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_SEM_NOME_PROPRIETARIO", "Sem nome do proprietário", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_SITUACAO_AIT_NAO_CONFIRMADA", "Situação do AIT não confirmada", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_SEM_NUMERO_CONTROLE", "AIT não possui número de controle", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_SEM_CPF_CNPJ_PROPRIETARIO", "AIT não possui o CPF/CNPJ do proprietário", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_SEM_MODELO_VEICULO", "Marca do veículo não encontrada", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_SEM_ESPECIE_VEICULO", "Espécie do veículo não encontrada", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_SEM_NR_RENAVAN", "Número do RENAVAM não encontrado", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_NAI_INTEMPESTIVO", "NAI intempestiva", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_NIP_INTEMPESTIVO", "NIP intempestiva", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_ERRO_AO_CONSULTAR_DADOS_DETRAN", "ERRO AO CONSULTAR DADOS DETRAN", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_PRAZOS_ENVIO_NAI", "Prazo para envio de recurso NAI", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_ENDERECO_NOTIFICACAO", "Sem endereço do Proprietário", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_INFRACAO_DE_COMPETENCIA_ESTADUAL", "INFRAÇÃO DE COMPETÊNCIA ESTADUAL", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    insertUpdateMob(createParametroMob("MOB_INCONSISTENCIA_PROPRIEDADE_VEICULAR_NIC", "PROPRIEDADE VEICULAR NIC", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
		    
		    
		    // NAI
		    grupo = NAI;
			insertUpdateMob(createParametroMob("MOB_INFORMACOES_ADICIONAIS_NAI", "Informações adicionais no verso da NAI", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_INFORMACOES_ADICIONAIS_RESPONSABILIDADE_PROPRIETARIO", "Informações adicionais de responsabilidade do proprietário", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TEXTO_IMFORMATIVO_CONDUTOR", "Informativo apresentação condutor", STRING, MULTI_TEXT, connect),grupo, connect);
			
			// NIP
		    grupo = NIP;
			insertUpdateMob(createParametroMob("MOB_INFORMACOES_ADICIONAIS_NIP", "Informações adicionais no verso da NIP", STRING, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_INFORMACOES_ADICIONAIS_DATA_RECALCULO_JUROS_NP", "Data permitida para recalculo de juros conforme a LEI Nº 13.281", DATA, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_INFORMACOES_ADICIONAIS_DATA_INICIO_EMISSAO_NP", "Data inicio prazo para emissão e expedição de NP, conforme Lei Nº 14.229 de 2021, artigo 282, § 6º", DATA, SINGLE_TEXT, connect),grupo, connect);

			
			// INFRAÇÕES RADAR
			insertUpdateMob(createParametroMob("MOB_NR_INFRACAO_VELOCIDADE_20", "Código de infração de velocidade acima do limite até 20%", NUMERICO, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_NR_INFRACAO_VELOCIDADE_20_50", "Código de infração de velocidade acima do limite de 20% até 50%", NUMERICO, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_NR_INFRACAO_VELOCIDADE_50", "Código de infração de velocidade acima do limite acima de 50%", NUMERICO, MULTI_TEXT, connect),grupo, connect);
			
			// TIPOS DE EVENTOS RADAR
			insertUpdateMob(createParametroMob("MOB_EVENTO_VELOCIDADE_ACIMA_LIMITE", "Velocidade Acima Limite", NUMERICO, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_EVENTO_PARADA_SOBRE_FAIXA", "Parada Sobre Faixa", NUMERICO, MULTI_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_EVENTO_AVANCO_SINAL", "Avanço de Sinal", NUMERICO, MULTI_TEXT, connect),grupo, connect);
			
			// NP ADVERTÊNCIA
			insertUpdateMob(createParametroMob("MOB_OPTANTE_NP_ADVERTENCIA", "Optante por gerar Notificação de Penalidade por Advertência automática", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_TEXTO_LEI_NP_ADVERTENCIA", "Lei para aplicação de Penalidade por Advertência", NUMERICO, MULTI_TEXT, connect),grupo, connect);	
			
			// PIX
			insertUpdateMob(createParametroMob("LG_PIX", "Optante por receber pagamento via PIX", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_CHAVE_PIX", "Chave PIX do Órgão", STRING, SINGLE_TEXT, connect),grupo, connect);		

			// Financeiro
			grupo = FINANCEIRO;
			insertUpdateMob(createParametroMob("NR_DIAS_VENCIMENTO_DIVIDA_ATIVA", "Dias de multa vencida para envio na dívida ativa", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_ARQUIVO_REMESSA_DIVIDA_ATIVA", "Tipo de arquivo para remessa de envio da dívida ativa", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_REMESSA_DIVIDA_ATIVA", "Tipo de documento para remessa de envio da dívida ativa ", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("LG_ENVIO_AUTOMATIDO_MOVIMENTO_DIVIDA_ATIVA", "Envio automático dos movimentos de dívida ativa para o DETRAN", LOGICO, CHECK_BOX, connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_TIPO_ARQUIVO_RETORNO_DIVIDA_ATIVA", "Tipo de arquivo para retorno da dívida ativa", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NR_PARCELAS_DIVIDA_ATIVA", "Número de parcelas para pagamento da dívida ativa", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("NR_CONTA_DIVIDA_ATIVA", "Sigla do tributo da Receita Federal da dívida ativa", STRING, SINGLE_TEXT, connect),grupo, connect);
			
			// Talonário Eletrônico
			grupo = TALONARIO_ELETRONICO;
			insertUpdateMob(createParametroMob("LG_API_SSL", "Ativa/desativa o uso do Protocolo de Segurança SSL para a API", LOGICO, CHECK_BOX, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_API_HOST", "Endereço do servidor da API para envio de dados do APP", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_API_PORT", "Porta do servidor da API para comunicação de dados do APP", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_API_CONTEXT", "Contexto da API para integração com o APP", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_API_APIROOT", "Caminho base para os endpoints da API do APP", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_EQUIPAMENTO_PREFIXO", "Prefixo de nome de equipamento", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("URL_DETRAN", "URL de consulta ao DETRAN", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_USUARIO_CONSULTA_DETRAN", "Usuário de auteticação da consulta ao DETRAN", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("NM_SENHA_CONSULTA_DETRAN", "Senha de auteticação da consulta ao DETRAN", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("CD_USUARIO_MASTER", "Código do Usuário Master", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("LG_WIFI_SYNC", "Sincronização por WI-FI", LOGICO, CHECK_BOX, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_OSM_VIEWBOX", "Limitação viewbox OSM", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("API_URL_NOMINATIM", "Rota de acesso para o serviço Nominatim", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_OSM_ESTADO", "Estado padrão para o mapa OSM", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("MOB_OSM_CIDADE", "Cidade padrão para o mapa OSM", STRING, SINGLE_TEXT, connect), grupo, connect);
			insertUpdateMob(createParametroMob("URL_DADOS_SYNC", "URL de sincronização de dados do aplicativo eTransito", STRING, SINGLE_TEXT, connect), grupo, connect);
			
			//Tipo de ocorrencia de insercao de agenda
			String nmParametro	= "PNE_DOCUMENTO_DEFERIDO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"PNE - Situacao: Deferido",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			 nmParametro	= "PNE_DOCUMENTO_INDEFERIDO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"PNE - Situacao: Indeferido",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro	= "PNE_DOCUMENTO_EM_ANDAMENTO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"PNE - Situacao: Em Andamento",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			 nmParametro	= "PNE_DOCUMENTO_CANCELADO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"PNE - Situacao: Cancelado",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
				nmParametro	= "PNE_DOCUMENTO_ANULACAO_CI";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"PNE - Ocorrencia Anulacao Documento",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro	= "PNE_DOCUMENTO_SALVAR";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"PNE - Salvar Ocorrencia",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro	= "PNE_DOCUMENTO_IMPRESSAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"PNE - Imprimir Ocorrencia",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro	= "PNE_DOCUMENTO_ENVIADO_PERICIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"PNE - Enviado Para Perícia",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
		    
			// Situação do Documento Deferido
			nmParametro = "PNE_SITUACAO_DOCUMENTO_DEFERIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Situação do Documento Deferido",
					"{caption:\"Situação do documento - Deferido\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.SituacaoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_DOCUMENTO\", dsField: \"NM_SITUACAO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro = "PNE_SITUACAO_DOCUMENTO_INDEFERIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Situação do Documento Indeferido",
					"{caption:\"Situação do documento - Indeferido\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.SituacaoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_DOCUMENTO\", dsField: \"NM_SITUACAO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro = "PNE_SITUACAO_DOCUMENTO_CANCELADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Situação do Documento Cancelado",
					"{caption:\"Situação do documento - Cancelado\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.SituacaoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_DOCUMENTO\", dsField: \"NM_SITUACAO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro = "PNE_SITUACAO_DOCUMENTO_EM_ANDAMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Situação do Documento Em Andamento",
					"{caption:\"Situação do documento - Em andamento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.SituacaoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_DOCUMENTO\", dsField: \"NM_SITUACAO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro = "GPN_TIPO_DOCUMENTO_COMUNICACAO_INTERNA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Tipo do Documento comunicação interna",
					"{caption:\"Tipo do documento - Comunicação interna\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.gpn.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			

			//Lista de Relatores
			nmParametro = "LISTA_RELATORES";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Lista de Relatores com vículo da JARI" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);
			if(cdParametro > 0)	{
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtRelatores = connect.prepareStatement("SELECT A.cd_pessoa, B.nm_pessoa FROM grl_pessoa_empresa A, grl_pessoa B, seg_usuario C "
						+ "WHERE cd_vinculo = " + ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_RELATOR", 0)
						+ "AND (A.cd_pessoa=B.cd_pessoa) " 
						+ "AND (A.cd_pessoa=C.cd_pessoa) "
						+ "AND st_usuario = " + UsuarioServices.ST_ATIVO);
				ResultSetMap rsmRelatores = new ResultSetMap(pstmtRelatores.executeQuery());
				while(rsmRelatores.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, String.valueOf(rsmRelatores.getInt("CD_PESSOA")));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmRelatores.getString("NM_PESSOA")/*vlApresentacao*/, rsmRelatores.getString("CD_PESSOA") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}
			
			nmParametro = "CD_RELATOR";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"CD especifico de Relatores para ser atribuída a Lista de Relatores",
					"{caption:\"Vinculo de pessoa\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Tipo de vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro = "CD_SECRETARIO_GERAL";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de CD para atribuição de relator como Secretário Geral da JARI",
					"{caption:\"Selecionando pessoa\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.PessoaDAO\",method: \"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Nome da pessoa\",reference:\"A.nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome da pessoa\",reference:\"NM_PESSOA\"},{label:\"St. Cadastro\",reference:\"ST_CADASTRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
						
			nmParametro = "CD_DIRETOR_JARI";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de CD para atribuição de relator como Diretor da JARI",
					"{caption:\"Selecionando pessoa\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.PessoaDAO\",method: \"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Nome da pessoa\",reference:\"A.nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome da pessoa\",reference:\"NM_PESSOA\"},{label:\"St. Cadastro\",reference:\"ST_CADASTRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			
			// VÍNCULOS
		    /*
			grupo = VINCULOS;
			insertUpdateMob(createParametroMob("CD_VINCULO_CONCESSIONARIO", "V�nculo: Concession�rio", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_CONCESSIONARIO_SECRETARIA", "V�nculo: Concession�rio Secretaria", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_CONCESSIONARIO_COLETIVO_RURAL","V�nculo: Concession�rio Coletivo Rural", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_MOTORISTA_AUXILIAR_COLETIVO_RURAL", "V�nculo: Motorista Auxiliar Coletivo Rural", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_CONCESSIONARIO_CONTRATO", "V�nculo: Concession�rio Contratado", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_MOTORISTA_AUXILIAR_CONTRATO", "V�nculo: Motorista Auxiliar Contrato", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_CONCESSIONARIO_ESCOLAR", "V�nculo - Concession�rio Escolar", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_CONCESSIONARIO_ESCOLAR_URBANO", "V�nculo - Concession�rio Escolar Urbano", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_CONCESSIONARIO_ESCOLAR_RURAL", "V�nculo - Concession�rio Escolar Rural", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_AUXILIAR_ESCOLAR", "V�nculo - Auxiliar do Tranporte Escolar", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_VISTORIADOR", "V�nculo - Vistoriador", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_CONDUTOR_MOB", "V�nculo - Condutor (MOB)", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_PNE_SOLICITANTE", "V�nculo - Solicitante PNE", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_VINCULO_MEDICO", "V�nculo - M�dico", connect),grupo, connect);
			*/
			
			//SEQUENCIAIS
			/*
			grupo = SEQUENCIAIS;
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_COBRANCA", "Cobranca", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_REMESSA_ENDERECAMENTO", "Remessa Endere�amento", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_REMESSA_MULTA", "Remessa Multa", NUMERICO, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_APRESENTACAO_CONDUTOR", "Apresenta��o de Condutor", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_DEFESA_PREVIA", "Defesa Pr�via", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_JARI", "Recurso JARI", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_CETRAN", "Recurso CETRAN", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_DEVOLUCAO_PAGAMENTO", "Devolu��o Pagamento", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_CANCELAMENTOS", "Cancelamento", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_NAI", "N�mero NAI", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_NR_NIP", "N�mero NIP", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_LG_NIP_AR", "N�mero NIP (AR)", STRING, SINGLE_TEXT, connect),grupo, connect);
			insertUpdateMob(createParametroMob("MOB_SEQUENCIA_LG_CONF_RESULTADOS", "N�mero Recebimento Resultados", STRING, SINGLE_TEXT, connect),grupo, connect);
			*/
			
			// NÃO CATEGORIZADOS
			/*
			grupo = NAO_CATEGORIZADO;
			insertUpdateMob(createParametroMob("CD_TIPO_DOCUMENTO_BEM_ES", "Tipo de Documento para Solicita��o de Cart�o BEM-ES", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_SETOR_SEMOB_ATUV", "Setor da SEMOB alocado na ATUV", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_SETOR_CEMAE", "Setor CEMAE", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_SETOR_ATUV_SIT", "Setor ATUV/SIT", connect),grupo, connect);
			insertUpdateMob(createParametroMob("CD_CIDADE_PNE_DEFAULT", "Cidade Padr�o (Endere�amento)", connect),grupo, connect);
			insertUpdateMob(createParametroMob("PNE_DOCUMENTO_DEFERIDO", "PNE - Situa��o: Deferido", connect),grupo, connect);
			insertUpdateMob(createParametroMob("PNE_DOCUMENTO_INDEFERIDO", "PNE - Situa��o: Indeferido", connect),grupo, connect);
			insertUpdateMob(createParametroMob("PNE_DOCUMENTO_EM_ANDAMENTO", "PNE - Situa��o: Em Andamento", connect),grupo, connect);
			insertUpdateMob(createParametroMob("PNE_DOCUMENTO_CANCELADO", "PNE - Situa��o: Cancelado", connect),grupo, connect);	
			insertUpdateMob(createParametroMob("PNE_OCORRENCIA_ANULACAO_CI", "PNE - Ocorr�ncia Anula��o Documento", connect),grupo, connect);
			*/
		    
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static void initPermissoesMob(Connection connect) {
		Modulo _modulo = ModuloServices.getModuloById("mob");
		int cdModulo = _modulo.getCdModulo();
		int cdSistema = _modulo.getCdSistema();
		
				
		
		File transito = new File(ContextManager.getRealPath() + "/xml/permissoes-tra.xml");
		File mobilidade = new File(ContextManager.getRealPath() + "/xml/permissoes-mob.xml");
		
		int cdModuloTra = AcaoServices.initTra(cdSistema);
		
		AcaoServices.initPermissoesTra(cdSistema, cdModuloTra, (transito.exists() ? transito : null), connect);
		AcaoServices.initPermissoesMob(cdSistema, cdModulo, (mobilidade.exists() ? mobilidade : null), connect);
		
	}
	
	private static Parametro createParametroMob(String nmParametro, String nmRotulo, Connection connect) {
		return createParametroMob(nmParametro, nmRotulo, NUMERICO, connect);
	}
	
	private static Parametro createParametroMob(String nmParametro, String nmRotulo, int tpDado, Connection connect) {
		return createParametroMob(nmParametro, nmRotulo, tpDado, FILTER, connect);
	}
	
	private static Parametro createParametroMob(String nmParametro, String nmRotulo, int tpDado, int tpApresentacao, Connection connect) {
		return createParametroMob(nmParametro, nmRotulo, tpDado, tpApresentacao, NIVEL_ACESSO_OPERADOR, connect);
	}
	
	private static Parametro createParametroMob(String nmParametro, String nmRotulo, int tpDado, int tpApresentacao, int nivelAcesso, Connection connect) {
		Modulo _modulo = ModuloServices.getModuloById("mob", connect);
		int cdModulo = _modulo.getCdModulo();
		int cdSistema = _modulo.getCdSistema();
		
		return new Parametro(0, 0, nmParametro, tpDado, tpApresentacao, nmRotulo, null, 0, TP_GERAL, cdModulo, cdSistema, nivelAcesso);
	}

	/************************************************************************************************************
	 * MÓDULO FROTA
	 ************************************************************************************************************/
	public static void initFta()		{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "fta");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			String nmParametro = "CD_AGENDA_MANUTENCAO_VEICULOS";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Frota - Agenda de manutenção de veículos",
					"{caption:\"Selecionando a agenda\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.agd.AgendaDAO\",method:\"find\"," +
							" cdField: \"CD_AGENDA\", dsField: \"NM_AGENDA\"," +
							" filterFields: [[{label:\"Agenda\", reference:\"nm_agenda\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_AGENDA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_TIPO_CHECKUP_VIAGEM";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Frota - Tipo de check-up para viagem",
					"{caption:\"Selecionando modelo de check-up\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.fta.TipoCheckupDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_CHECKUP\", dsField: \"NM_TIPO_CHECKUP\"," +
							" filterFields: [[{label:\"Tipo de Check-up\", reference:\"nm_tipo_checkup\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de check-up\",reference:\"NM_TIPO_CHECKUP\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_TIPO_AGENDAMENTO_CHECKUP";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Frota - Tipo de agendamento para check-up",
					"{caption:\"Selecionando tipo de agendamento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.agd.TipoAgendamentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_AGENDAMENTO\", dsField: \"NM_TIPO_AGENDAMENTO\"," +
							" filterFields: [[{label:\"Tipo de Agendamento\", reference:\"nm_tipo_agendamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Agendamento\",reference:\"NM_TIPO_AGENDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Configuração de Tipo de Documento para abastecimento
			nmParametro	= "CD_TIPO_DOCUMENTO_ABASTECIMENTO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Frota - Tipo de documento para conta a pagar de abastecimentos",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para multa de trï¿½nsito
			nmParametro	= "CD_CATEGORIA_ECONOMICA_MULTA_TRANSITO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Multa de Trï¿½nsito",
					"{caption:\"Selecionando categoria econômica\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria econômica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}]}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_GRUPO_MARCA_PNEU";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Grupo de marca de pneus",
					"{caption:\"Selecionando grupo de bens\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.bpm.GrupoMarcaDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO\", dsField: \"NM_GRUPO\"," +
							" filterFields: [[{label:\"Grupo de bens\", reference:\"nm_grupo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo de bens\",reference:\"NM_GRUPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_GRUPO_MARCA_VEICULO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Grupo de marca de veículos",
					"{caption:\"Selecionando grupo de bens\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.bpm.GrupoMarcaDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO\", dsField: \"NM_GRUPO\"," +
							" filterFields: [[{label:\"Grupo de bens\", reference:\"nm_grupo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo de bens\",reference:\"NM_GRUPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tipo de Rota para Venda Externa
			nmParametro	= "CD_TIPO_ROTA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Frota - Tipo de rota para Venda Externa",
					"{caption:\"Selecionando Tipo de Rota\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.fta.TipoRotaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_ROTA\", dsField: \"NM_TIPO\"," +
							" filterFields: [[{label:\"Tipo de Rota\", reference:\"nm_tipo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Rota\",reference:\"NM_TIPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Parametro para Vínculo de Representante do Concessionï¿½rio
			nmParametro = "CD_VINCULO_REPRESENTANTE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Representante",
					"{caption:\"Selecionando vinculo de Representante\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo de Representante\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo de Representante\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);
			// Parametro para Vínculo de Representante do Escolar Urbano
			nmParametro = "CD_VINCULO_REPRESENTANTE_ESCOLAR_URBANO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Representante Escolar Urbano",
					"{caption:\"Selecionando vinculo de Representante\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo de Representante\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo de Representante\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);
			// Parametro para Vínculo de Permissionïário
			nmParametro = "CD_VINCULO_PERMISSIONARIO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Permissionário",
					"{caption:\"Selecionando vinculo de Permissionï¿½rio\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo de Permissionï¿½rio\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo de Permissionï¿½rio\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Parametro para Vínculo de Motorista Designado
			nmParametro = "CD_VINCULO_MOTORISTA_DESIGNADO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Motorista Designado",
					"{caption:\"Selecionando vinculo de Motorista Designado\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo de Motorista Designado\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo de Motorista Designado\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);

			// Parametro para Vínculo de Motorista Auxï¿½liar
			nmParametro = "CD_VINCULO_MOTORISTA_AUXILIAR";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Motorista Auxiliar Taxi",
					"{caption:\"Selecionando vinculo de Motorista Auxï¿½liar\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo de Motorista Auxï¿½liar\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo de Motorista Auxï¿½liar\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO AGENDA
	 ************************************************************************************************************/
	public static void initAgenda()		{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "agd");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesAgd(cdSistema, cdModulo, connect);

			// Servidor SMP usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			String nmParametro = "NM_SERVIDOR_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Endereï¿½o Servidor SMTP",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "BLB_LOGO_AGENDA";
			insertUpdate(new Parametro(0, 0, nmParametro, IMAGEM, SINGLE_TEXT,
					"Logo Agenda (impressão de anexos)",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Login de Servidor SMP usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			nmParametro = "NM_LOGIN_SERVIDOR_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Login de acesso ao Servidor SMTP",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Senha de acesso ao Login de Servidor SMP usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			nmParametro = "NM_SENHA_SERVIDOR_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Senha de acesso ao Servidor SMTP",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Email remetente usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			nmParametro = "NM_EMAIL_REMETENTE_SMTP";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Endereï¿½o remetente (para envio de Emails) ",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Local Default de Agendamento
			nmParametro = "NM_LOCAL_DEFAULT";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Local Default",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "NM_CATEGORIA_AGENDA";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Categoria",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);

			nmParametro = "NR_VERSAO_AGENDA";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Versï¿½o da agenda",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);

			nmParametro = "LG_ENVIO_EMAIL";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Enviar Email quando incluir ocorrências", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro = "NM_SENHA_ADMINISTRATIVA";
			insertUpdate(new Parametro(
					0, 0, nmParametro, STRING, SINGLE_TEXT, "MOB - Senha Administrativa",  null, 0, TP_GERAL, 
					cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);

			nmParametro = "TP_MODALIDADE_AGENDA";
			int cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, COMBO_BOX, "Modalidade Agenda",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);
			if(cdParametro > 0)	{
				String[] options = {"Gratuita", "Profissional"};
				for (int i=0; i<options.length; i++)
					ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, options[i] /*vlApresentacao*/, Integer.toString(i+1) /*vlReal*/,
							0 /*cdPessoa*/, 0 /*cdEmpresa*/));
			}
			// Agenda (submï¿½d. officePronto)
			nmParametro = "QT_USUARIOS_AGENDA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "Quantidade de Usuários",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);
			// Agenda (submï¿½d. officePronto)
			nmParametro = "DS_URL_REPOSITORIO_BANNERS";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, MULTI_TEXT, "URL Repositï¿½rio Banners",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);
			// Agenda (submï¿½d. officePronto)
			nmParametro = "NM_EMPRESA_LICENCA";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, MULTI_TEXT, "Licenciado para",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "LG_ALTER_DADOS_BASICOS_AGEND_RESP";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpParametro*/, CHECK_BOX /*tpApresentacao*/,
					"Alteraï¿½ï¿½o de dados básicos de agendamentos por responsï¿½vel" /*nmRotulo*/, "" /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo /*cdModulo*/, cdSistema /*cdSistema*/, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			nmParametro = "CD_TIPO_AGENDAMENTO_DEFAULT";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de Agendamento Default",
					"{caption:\"Selecionando agendamento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.agd.AgendamentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_AGENDAMENTO\", dsField: \"NM_TIPO_AGENDAMENTO\"," +
							" filterFields: [[{label:\"Tipo de agendamento\", reference:\"nm_tipo_agendamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de agendamento\",reference:\"NM_TIPO_AGENDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_TIPO_AGENDAMENTO_COMP_DEFAULT";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de Agendamento Default para compromissos",
					"{caption:\"Selecionando agendamento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.agd.AgendamentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_AGENDAMENTO\", dsField: \"NM_TIPO_AGENDAMENTO\"," +
							" filterFields: [[{label:\"Tipo de agendamento\", reference:\"nm_tipo_agendamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de agendamento\",reference:\"NM_TIPO_AGENDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_TIPO_AGENDAMENTO_REUNIAO_ORD";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de Agendamento para reuniï¿½es ordinï¿½rias",
					"{caption:\"Selecionar tipo de agendamento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.agd.TipoAgendamentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_AGENDAMENTO\", dsField: \"NM_TIPO_AGENDAMENTO\"," +
							" filterFields: [[{label:\"Tipo de agendamento\", reference:\"nm_tipo_agendamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de agendamento\",reference:\"NM_TIPO_AGENDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_TIPO_AGENDAMENTO_REUNIAO_EXTRAORD";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de Agendamento para reuniï¿½es extraordinï¿½rias",
					"{caption:\"Selecionar tipo de agendamento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.agd.TipoAgendamentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_AGENDAMENTO\", dsField: \"NM_TIPO_AGENDAMENTO\"," +
							" filterFields: [[{label:\"Tipo de agendamento\", reference:\"nm_tipo_agendamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de agendamento\",reference:\"NM_TIPO_AGENDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_TIPO_DOCUMENTO_ATA_REUNIAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de Documento para registro de atas de reuniï¿½es",
					"{caption:\"Selecionar tipo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"cd_tipo_documento\", dsField: \"nm_tipo_documento\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de documento\",reference:\"nm_tipo_documento\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_MODELO_DOCUMENTO_CONVITE_REUNIAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Modelo de Documento para impressão/envio de convites de reuniï¿½es",
					"{caption:\"Selecionar modelo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.ModeloDocumentoDAO\",method:\"find\"," +
							" cdField: \"cd_modelo\", dsField: \"nm_modelo\"," +
							" filterFields: [[{label:\"Modelo de documento\", reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Modelo de documento\",reference:\"nm_modelo\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_MODELO_DOCUMENTO_ATA_REUNIAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Modelo de Documento para impressão/envio de atas de reuniï¿½es",
					"{caption:\"Selecionar modelo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.ModeloDocumentoDAO\",method:\"find\"," +
							" cdField: \"cd_modelo\", dsField: \"nm_modelo\"," +
							" filterFields: [[{label:\"Modelo de documento\", reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Modelo de documento\",reference:\"nm_modelo\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_MODELO_DOCUMENTO_HIST_ASSUNTO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Modelo de Documento - Histï¿½rico de Assunto/Tema",
					"{caption:\"Selecionar modelo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.ModeloDocumentoDAO\",method:\"find\"," +
							" cdField: \"cd_modelo\", dsField: \"nm_modelo\"," +
							" filterFields: [[{label:\"Modelo de documento\", reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Modelo de documento\",reference:\"nm_modelo\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_MAILING_CONTA_ENVIO_CONVITE_REUNIAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Conta de Mailing para envio de convites de reuniï¿½es",
					"{caption:\"Selecionar Conta de Mailing\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.crm.MailingContaDAO\",method:\"find\"," +
							" cdField: \"cd_conta\", dsField: \"nm_conta\"," +
							" filterFields: [[{label:\"Conta de Mailing\", reference:\"nm_conta\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Conta\",reference:\"nm_conta\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "CD_MAILING_CONTA_ENVIO_ATA_REUNIAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Conta de Mailing para envio de atas de reuniï¿½es",
					"{caption:\"Selecionar Conta de Mailing\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.crm.MailingContaDAO\",method:\"find\"," +
							" cdField: \"cd_conta\", dsField: \"nm_conta\"," +
							" filterFields: [[{label:\"Conta de Mailing\", reference:\"nm_conta\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Conta\",reference:\"nm_conta\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Ativa o registro de ocorrências em agenda_item
			nmParametro = "LG_OCORRENCIA_AGENDA";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpParametro*/, CHECK_BOX /*tpApresentacao*/,
					"Agenda: Ativar ocorrências em agendas" /*nmRotulo*/, "" /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo /*cdModulo*/, cdSistema /*cdSistema*/, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			// Ativa o registro de ocorrências automáticas em agenda_item
			nmParametro = "LG_OCORRENCIA_AUTOMATICA_AGENDA";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpParametro*/, CHECK_BOX /*tpApresentacao*/,
					"Agenda: Ativar ocorrências automáticas em agendas" /*nmRotulo*/, "" /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo /*cdModulo*/, cdSistema /*cdSistema*/, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			//Tipo de ocorrencia de insercao de agenda
			nmParametro	= "CD_TIPO_OCORRENCIA_INSERT_AGENDA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Agenda: Tipo de Ocorrência de inserção de agenda",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//Tipo de ocorrencia de atualizacao de agenda
			nmParametro	= "CD_TIPO_OCORRENCIA_UPDATE_AGENDA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Agenda: Tipo de Ocorrência de atualização de agenda",
					"{caption:\"Selecionar Tipo de Ocorrência\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//RESPONSï¿½VEIS
			nmParametro = "LG_RESPONSAVEIS_VINCULO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Agenda: Responsáveis limitados por vinculo", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);


			nmParametro = "TP_RESPONSAVEIS_VINCULO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Agenda: Responsáveis com os vinculos" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			if(cdParametro >= 0)	{

				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtVinculos = connect.prepareStatement("SELECT * FROM grl_vinculo");
				ResultSetMap rsmVinculos = new ResultSetMap(pstmtVinculos.executeQuery());
				while(rsmVinculos.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmVinculos.getString("CD_VINCULO"));

					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmVinculos.getString("NM_VINCULO") /*vlApresentacao*/, 
								rsmVinculos.getString("CD_VINCULO") /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}
			}

			// LANï¿½AR PARECER DA AGENDA AO CUMPRIR
			nmParametro = "LG_PARECER_CUMPRIR";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Agenda: Lançar parecer ao cumprir.", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			
			
			// Cliente que não pode ter preposto na diligência
			nmParametro = "CD_CLIENTE_PREPOSTO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Agenda: Cliente que não deve ter preposto na agenda",
					"{caption:\"Selecionando pessoa\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.PessoaDAO\",method: \"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Nome da pessoa\",reference:\"A.nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome da pessoa\",reference:\"NM_PESSOA\"},{label:\"St. Cadastro\",reference:\"ST_CADASTRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Cidade que pode exigir preposto na diligência
			nmParametro = "CD_CIDADE_PREPOSTO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Agenda: Comarca que pode ter preposto na agenda",
					"{caption:\"Selecionando cidade\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.CidadeDAO\",method: \"find\"," +
							" cdField: \"CD_CIDADE\", dsField: \"NM_CIDADE\"," +
							" filterFields: [[{label:\"Nome da cidade\",reference:\"nm_cidade\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome da cidade\",reference:\"NM_CIDADE\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			nmParametro = "CD_CIDADES_PREPOSTO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Agenda: Comarcas que podem ter preposto na agenda" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			if(cdParametro >= 0)	{

				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtCidades = connect.prepareStatement("SELECT * FROM grl_cidade ORDER BY nm_cidade");
				ResultSetMap rsmCidades = new ResultSetMap(pstmtCidades.executeQuery());
				while(rsmCidades.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmCidades.getString("CD_CIDADE"));

					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmCidades.getString("NM_CIDADE") /*vlApresentacao*/, 
								rsmCidades.getString("CD_CIDADE") /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}
			}
			
			// Tipos de Prazo de CONTESTACAO
			nmParametro = "TP_PRAZO_CONTESTACAO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Agenda: Prazos de Contestação" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			if(cdParametro >= 0)	{

				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtPrazosContestacao = connect.prepareStatement("SELECT * FROM prc_tipo_prazo ORDER BY nm_tipo_prazo");
				ResultSetMap rsmPrazosContestacao = new ResultSetMap(pstmtPrazosContestacao.executeQuery());
				while(rsmPrazosContestacao.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmPrazosContestacao.getString("CD_TIPO_PRAZO"));

					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmPrazosContestacao.getString("NM_TIPO_PRAZO") /*vlApresentacao*/, 
								rsmPrazosContestacao.getString("CD_TIPO_PRAZO") /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}
			}
						
			// Tipos de Prazo de RECURSO
			nmParametro = "TP_PRAZO_RECURSO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Agenda: Prazos de Recurso" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			if(cdParametro >= 0)	{

				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtPrazosRecurso = connect.prepareStatement("SELECT * FROM prc_tipo_prazo ORDER BY nm_tipo_prazo");
				ResultSetMap rsmPrazosRecurso = new ResultSetMap(pstmtPrazosRecurso.executeQuery());
				while(rsmPrazosRecurso.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmPrazosRecurso.getString("CD_TIPO_PRAZO"));

					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmPrazosRecurso.getString("NM_TIPO_PRAZO") /*vlApresentacao*/, 
								rsmPrazosRecurso.getString("CD_TIPO_PRAZO") /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}
			}
			
			// Tipos de Prazo que podem salvar agenda com data retroativa
			nmParametro = "CD_TIPO_PRAZO_RETROATIVO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Agenda: Prazos que podem ser salvos com data retroativa" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			if(cdParametro >= 0)	{

				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtPrazosRetroativos = connect.prepareStatement("SELECT * FROM prc_tipo_prazo ORDER BY nm_tipo_prazo");
				ResultSetMap rsmPrazosRetroativos = new ResultSetMap(pstmtPrazosRetroativos.executeQuery());
				while(rsmPrazosRetroativos.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmPrazosRetroativos.getString("CD_TIPO_PRAZO"));

					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmPrazosRetroativos.getString("NM_TIPO_PRAZO") /*vlApresentacao*/, 
								rsmPrazosRetroativos.getString("CD_TIPO_PRAZO") /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}
			}
			
			//Obrigatoriedade de vínculo a uma agenda
			nmParametro = "LG_PROCESSO_OBRIGATORIO";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpParametro*/, CHECK_BOX /*tpApresentacao*/,
					"Agenda: Vinculação obrigatória com processo" /*nmRotulo*/, "" /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo /*cdModulo*/, cdSistema /*cdSistema*/, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);
			
			// Cliente que não pode ter preposto na diligência
			nmParametro = "CD_CORRESPONDENTE_DILIGENCIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Agenda: Correspondente padrão para diligências",
					"{caption:\"Selecionando pessoa\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.PessoaDAO\",method: \"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Nome da pessoa\",reference:\"A.nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome da pessoa\",reference:\"NM_PESSOA\"},{label:\"St. Cadastro\",reference:\"ST_CADASTRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			
			//Tipo de prazo PROVISIONAR CUSTAS
			nmParametro	= "CD_PRAZO_PROVISIONAR_CUSTAS";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Agenda: Tipo de prazo \"Provisionar Custas\"",
					"{caption:\"Selecionar Tipo de Prazo\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.prc.TipoPrazoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_PRAZO\", dsField: \"NM_TIPO_PRAZO\"," +
							" filterFields: [[{label:\"Tipo de Prazo\", reference:\"NM_TIPO_PRAZO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Prazo\",reference:\"NM_TIPO_PRAZO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//Tipo de prazo COMPARECIMENTO EM AUDIENCIA
			nmParametro	= "CD_PRAZO_COMPARECIMENTO_AUDIENCIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Agenda: Tipo de prazo \"Comparecimento em Audiência\"",
					"{caption:\"Selecionar Tipo de Prazo\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.prc.TipoPrazoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_PRAZO\", dsField: \"NM_TIPO_PRAZO\"," +
							" filterFields: [[{label:\"Tipo de Prazo\", reference:\"NM_TIPO_PRAZO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Prazo\",reference:\"NM_TIPO_PRAZO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO ESTOQUE
	 ************************************************************************************************************/
	public static void initAlm()		{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo  = ? ");
			pstmt.setString(1, "alm");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;
			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesAlm(cdSistema, cdModulo, connect);

			String nmParametro	= "CD_LOCAL_ARMAZENAMENTO_DEFAULT";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Local de Armazenamento Default",
					"{caption:\"Selecione Local de Armazenamento Default\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.alm.LocalArmazenamentoServices\",method: \"findCompleto\"," +
							" cdField: \"CD_LOCAL_ARMAZENAMENTO\", dsField: \"NM_LOCAL_ARMAZENAMENTO\"," +
							" filterFields: [[{label:\"Local de Armazenamento\",reference:\"nm_local_armazenamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Local de Armazenamento\",reference:\"NM_LOCAL_ARMAZENAMENTO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Rateia Desconto
			insertUpdate(new Parametro(0, 0, "LG_RATEIA_DESCONTO_ENTRADA", LOGICO, CHECK_BOX,
					"Rateia o desconto na entrada", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Estrutura dos Grupos
			insertUpdate(new Parametro(0, 0, "LG_GRUPO_HIERARQUICO", LOGICO, CHECK_BOX, "Usar grupo em hierarquia?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Categoria econômica default para despesas relacionadas a fretes
			nmParametro	= "CD_CATEGORIA_DESPESAS_FRETES";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica para Despesas de Fretes",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para classificaï¿½ï¿½o de entradas
			nmParametro	= "CD_CATEGORIA_ENTRADAS_DEFAULT";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Entradas",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Configuração de Tipo de Documento para Faturamento de Fretes de Transportadoras
			nmParametro	= "CD_TIPO_DOCUMENTO_FRETE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de Documento para Faturamento de Fretes",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Categoria econômica default para classificaï¿½ï¿½o de saï¿½das
			nmParametro	= "CD_CATEGORIA_SAIDAS_DEFAULT";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Categoria econômica padrão - Saï¿½das",
					"{caption:\"Selecionando categoria econômica\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method: \"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria Economica\",reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Forma pagamento crédito de devolução
			nmParametro	= "CD_FORMA_PAGAMENTO_CREDITO_DEVOLUCAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Forma de pagamento para crédito de devolução do cliente",
					"{caption:\"Selecionando forma de pagamento\",width:800,height:400,modal:true," +
							" className: \"com.tivic.manager.adm.FormaPagamentoDAO\",method: \"find\"," +
							" cdField: \"CD_FORMA_PAGAMENTO\", dsField: \"NM_FORMA_PAGAMENTO\"," +
							" filterFields: [[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Forma de Pagamento\",reference:\"NM_FORMA_PAGAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Contabilização automática de estoque
			nmParametro = "LG_CONTABILIZACAO_ESTOQUE";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Contabilizar estoque automaticamente", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Campos de valores financeiros
			nmParametro = "LG_HABILITA_ITENS_REFERENCIA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Habilita itens com referï¿½ncia", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro	= "CD_NATUREZA_OPERACAO_ENTRADA_DEFAULT";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza de Operação padrão - Entradas",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Documento para devolução do cliente
			nmParametro	= "CD_TIPO_DOCUMENTO_DEVOLUCAO_CLIENTE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"devolução do Cliente: Tipo de Documento Padrão",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Documento para devolução do fornecedor
			nmParametro	= "CD_TIPO_DOCUMENTO_DEVOLUCAO_FORNECEDOR";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"devolução do Fornecedor: Tipo de Documento Padrão",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);


			// Categoria para devolução do cliente
			nmParametro	= "CD_CATEGORIA_DEVOLUCAO_CLIENTE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"devolução do Cliente: Categoria Econômica Padrão",
					"{caption:\"Selecionando a Categoria\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method:\"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria\", reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Categoria para devolução do fornecedor
			nmParametro	= "CD_CATEGORIA_DEVOLUCAO_FORNECEDOR";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"devolução do Fornecedor: Categoria Econômica Padrão",
					"{caption:\"Selecionando a Categoria\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.CategoriaEconomicaServices\",method:\"find\"," +
							" cdField: \"CD_CATEGORIA_ECONOMICA\", dsField: \"DS_CATEGORIA_ECONOMICA\"," +
							" filterFields: [[{label:\"Categoria\", reference:\"nm_categoria_economica\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Categoria econômica\",reference:\"DS_CATEGORIA_ECONOMICA\"}, " +
							"			               {label:\"Nï¿½ Completo\",reference:\"NR_CATEGORIA_ECONOMICA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"devolução do Cliente: Natureza de Operação Padrão",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_NATUREZA_OPERACAO_SAIDA_DEFAULT";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza de Operação padrão - Saï¿½das",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"devolução para o Fornecedor: Natureza de Operação Padrão",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Máscara do ID Reduzido
			nmParametro = "DS_ID_REDUZIDO_MASK";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Máscara do ID REDUZIDO", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tamanhos
			nmParametro = "DS_GRADE";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Grade (tamanhos separados por vï¿½rgula)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Etiquetas
			nmParametro = "ETQ_MARGEM_SUPERIOR";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "ETIQUETA: Margem Superior (cm)", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Etiquetas
			nmParametro = "ETQ_MARGEM_ESQUERDA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "ETIQUETA: Margem Esquerda (cm)", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Etiquetas
			nmParametro = "ETQ_ESPACO_HORIZONTAL";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "ETIQUETA: Espaï¿½o horizontal entre etiquetas (cm)", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Etiquetas
			nmParametro = "ETQ_ALTURA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "ETIQUETA: Altura da etiqueta (cm)", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Documento para geraï¿½ï¿½o de faturas de entrada
			nmParametro	= "CD_TIPO_DOCUMENTO_FATURA_ENTRADA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tipo de documento para faturamento de entradas",
					"{caption:\"Selecionando tipo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\", method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de documento\", reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Campos de valores financeiros
			nmParametro = "LG_ID_REDUZIDO_DIFERENTE_ORIGINAL";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"ID Reduzido deve ser gerado diferente do original na grade?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Classificaï¿½ï¿½o Fiscal - SUbstituiï¿½ï¿½o Tributï¿½ria
			nmParametro	= "CD_SUBSTITUICAO_TRIBUTARIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Substituição Tributï¿½ria",
					"{caption:\"Selecionando classificaï¿½ï¿½o fiscal\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.ClassificacaoFiscalDAO\", method:\"find\"," +
							" cdField: \"CD_CLASSIFICACAO_FISCAL\", dsField: \"NM_CLASSIFICACAO_FISCAL\"," +
							" filterFields: [[{label:\"Classificaï¿½ï¿½o FIscal\", reference:\"nm_classificacao_fiscal\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Classificaï¿½ï¿½o Fiscal\", reference:\"NM_CLASSIFICACAO_FISCAL\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Classificaï¿½ï¿½o Fiscal - SUbstituiï¿½ï¿½o Tributï¿½ria
			nmParametro	= "CD_TRIBUTADO_INTEGRALMENTE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Tributado Integralmente",
					"{caption:\"Selecionando classificaï¿½ï¿½o fiscal\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.ClassificacaoFiscalDAO\", method:\"find\"," +
							" cdField: \"CD_CLASSIFICACAO_FISCAL\", dsField: \"NM_CLASSIFICACAO_FISCAL\"," +
							" filterFields: [[{label:\"Classificaï¿½ï¿½o FIscal\", reference:\"nm_classificacao_fiscal\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Classificaï¿½ï¿½o Fiscal\", reference:\"NM_CLASSIFICACAO_FISCAL\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Aplicaï¿½ï¿½o de estilos ao Relatï¿½rio
			nmParametro	= "LG_RELATORIO_ESTILIZADO";
			int cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, COMBO_BOX /*tpApresentacao*/,
					"Relatório Estilizado" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);
			if (cdParametro>0) {
				String[] options = {"Sem Estilo", "Com Estilo"};
				for (int i=0; i<=options.length; i++)
					ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, options[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/,
							0 /*cdPessoa*/, 0 /*cdEmpresa*/));
			}

			// Tipo de ID utilizado para os produtos
			nmParametro	= "TP_ID_PRODUTO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, COMBO_BOX /*tpApresentacao*/,
					"Tipo de ID" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);
			if (cdParametro>0) {
				String[] options = {"ID", "ID-IDGRUPO"};
				for (int i=0; i<=options.length; i++)
					ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, options[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/,
							0 /*cdPessoa*/, 0 /*cdEmpresa*/));
			}

			//Tributos que sï¿½o isentos de Tributaï¿½ï¿½o
			nmParametro = "TP_SITUACAO_SEM_TRIBUTACAO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Situaï¿½ï¿½es que sï¿½o isentas de Tributaï¿½ï¿½o" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connect);

			if(cdParametro > 0)	{
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtSituacaoTributaria = connect.prepareStatement("SELECT *, B.nm_tributo FROM fsc_situacao_tributaria A " +
						"						JOIN adm_tributo B ON (A.cd_tributo = B.cd_tributo) " +
						"			 		  ORDER BY B.nm_tributo");
				ResultSetMap rsmSituacaoTributaria = new ResultSetMap(pstmtSituacaoTributaria.executeQuery());
				while(rsmSituacaoTributaria.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmSituacaoTributaria.getString("CD_SITUACAO_TRIBUTARIA"));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmSituacaoTributaria.getString("NM_TRIBUTO") + " - " + rsmSituacaoTributaria.getString("NM_SITUACAO_TRIBUTARIA")/*vlApresentacao*/, rsmSituacaoTributaria.getString("CD_TRIBUTO") + "-"+rsmSituacaoTributaria.getString("CD_SITUACAO_TRIBUTARIA") /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}

			}


			// Tamanho máximo do nome do produto (Quando não setado o tamanho ï¿½ virtualmente infinito)
			nmParametro = "DS_NOME_PRODUTO_TAMANHO_MAXIMO";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Tamanho máximo do nome do produto", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Natureza da Operação - Venda Externa - Nota de Retorno Normal (ENTRADA)
			insertUpdate(new Parametro(0, 0, "CD_NATUREZA_OPERACAO_VENDA_EXTERNA_RETORNO_NORMAL", NUMERICO, FILTER,
					"Natureza da Operação - Venda Externa - Nota de Retorno Normal (ENTRADA)",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Natureza da Operação - Venda Externa - Nota de Retorno Substituição (ENTRADA)
			insertUpdate(new Parametro(0, 0, "CD_NATUREZA_OPERACAO_VENDA_EXTERNA_RETORNO_SUBSTITUICAO", NUMERICO, FILTER,
					"Natureza da Operação - Venda Externa - Nota de Retorno Substituição (ENTRADA)",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Natureza da Operação - Venda Externa - Nota de Retorno (SAIDA)
			insertUpdate(new Parametro(0, 0, "CD_NATUREZA_OPERACAO_REMESSA", NUMERICO, FILTER,
					"Natureza da Operação - Nota de Remessa (SAIDA)",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Campos de valores financeiros
			nmParametro = "LG_PRECO_ITEM_REMESSA_SINCRONIZADO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Sincroniza os preï¿½os das vendas com a remessa especï¿½fica", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// O sistema deverï¿½ ou não puxar as informaï¿½ï¿½es da viagem ao gerar a nota fiscal eletrúnica
			nmParametro = "LG_BUSCAR_INFORMACOES_VIAGEM";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"O sistema deverï¿½ puxar as informaï¿½ï¿½es da viagem ao gerar a Nota Fiscal eletrúnica", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);			

			nmParametro	= "CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza de Operação de Importaï¿½ï¿½o",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_EVENTO_FINANCEIRO_CAPATAZIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Evento Financeiro - Capatazia",
					"{caption:\"Selecionando evento financeiro\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.EventoFinanceiroDAO\",method:\"find\"," +
							" cdField: \"CD_EVENTO_FINANCEIRO\", dsField: \"NM_EVENTO_FINANCEIRO\"," +
							" filterFields: [[{label:\"Evento Financeiro\", reference:\"nm_evento_financeiro\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Evento Financeiro\",reference:\"NM_EVENTO_FINANCEIRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_EVENTO_FINANCEIRO_TAXA_SISCOMEX";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Evento Financeiro - Taxa SISCOMEX",
					"{caption:\"Selecionando evento financeiro\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.EventoFinanceiroDAO\",method:\"find\"," +
							" cdField: \"CD_EVENTO_FINANCEIRO\", dsField: \"NM_EVENTO_FINANCEIRO\"," +
							" filterFields: [[{label:\"Evento Financeiro\", reference:\"nm_evento_financeiro\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Evento Financeiro\",reference:\"NM_EVENTO_FINANCEIRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_EVENTO_FINANCEIRO_ARMAZENAGEM";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Evento Financeiro - Armazenagem",
					"{caption:\"Selecionando evento financeiro\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.EventoFinanceiroDAO\",method:\"find\"," +
							" cdField: \"CD_EVENTO_FINANCEIRO\", dsField: \"NM_EVENTO_FINANCEIRO\"," +
							" filterFields: [[{label:\"Evento Financeiro\", reference:\"nm_evento_financeiro\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Evento Financeiro\",reference:\"NM_EVENTO_FINANCEIRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_EVENTO_FINANCEIRO_AFRMM";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Evento Financeiro - AFRMM",
					"{caption:\"Selecionando evento financeiro\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.adm.EventoFinanceiroDAO\",method:\"find\"," +
							" cdField: \"CD_EVENTO_FINANCEIRO\", dsField: \"NM_EVENTO_FINANCEIRO\"," +
							" filterFields: [[{label:\"Evento Financeiro\", reference:\"nm_evento_financeiro\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Evento Financeiro\",reference:\"NM_EVENTO_FINANCEIRO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_SITUACAO_TRIBUTARIA_IMPORTACAO_ICMS";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Situação Tributï¿½ria para Importaï¿½ï¿½o - ICMS",
					"{caption:\"Selecionando situação tributï¿½ria\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.fsc.SituacaoTributariaDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_TRIBUTARIA\", dsField: \"NM_SITUACAO_TRIBUTARIA\"," +
							" hiddenFields: [{reference:\"CD_TRIBUTO\",value:\"" + getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0) + "\", comparator:_EQUAL,datatype:_INTEGER}],"+
							" filterFields: [[{label:\"Situação Tributï¿½ria\", reference:\"nm_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70},"+ 
							"					{label:\"CST\", reference:\"nr_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:30}]],"+
							" gridOptions: {columns:[{label:\"Situação Tributï¿½ria\",reference:\"NM_SITUACAO_TRIBUTARIA\"}," + 
							"						   {label:\"CST\",reference:\"NR_SITUACAO_TRIBUTARIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			nmParametro = "CD_SITUACAO_TRIBUTARIA_IMPORTACAO_IPI";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Situação Tributï¿½ria para Importaï¿½ï¿½o - IPI",
					"{caption:\"Selecionando situação tributï¿½ria\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.fsc.SituacaoTributariaDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_TRIBUTARIA\", dsField: \"NM_SITUACAO_TRIBUTARIA\"," +
							" hiddenFields: [{reference:\"CD_TRIBUTO\",value:\"" + getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0) + "\", comparator:_EQUAL,datatype:_INTEGER}],"+
							" filterFields: [[{label:\"Situação Tributï¿½ria\", reference:\"nm_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70},"+ 
							"					{label:\"CST\", reference:\"nr_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:30}]],"+
							" gridOptions: {columns:[{label:\"Situação Tributï¿½ria\",reference:\"NM_SITUACAO_TRIBUTARIA\"}," + 
							"						   {label:\"CST\",reference:\"NR_SITUACAO_TRIBUTARIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			nmParametro = "CD_SITUACAO_TRIBUTARIA_IMPORTACAO_PIS";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Situação Tributï¿½ria para Importaï¿½ï¿½o - PIS",
					"{caption:\"Selecionando situação tributï¿½ria\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.fsc.SituacaoTributariaDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_TRIBUTARIA\", dsField: \"NM_SITUACAO_TRIBUTARIA\"," +
							" hiddenFields: [{reference:\"CD_TRIBUTO\",value:\"" + getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0) + "\", comparator:_EQUAL,datatype:_INTEGER}],"+
							" filterFields: [[{label:\"Situação Tributï¿½ria\", reference:\"nm_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70},"+ 
							"					{label:\"CST\", reference:\"nr_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:30}]],"+
							" gridOptions: {columns:[{label:\"Situação Tributï¿½ria\",reference:\"NM_SITUACAO_TRIBUTARIA\"}," + 
							"						   {label:\"CST\",reference:\"NR_SITUACAO_TRIBUTARIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			nmParametro = "CD_SITUACAO_TRIBUTARIA_IMPORTACAO_COFINS";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Situação Tributï¿½ria para Importaï¿½ï¿½o - COFINS",
					"{caption:\"Selecionando situação tributï¿½ria\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.fsc.SituacaoTributariaDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_TRIBUTARIA\", dsField: \"NM_SITUACAO_TRIBUTARIA\"," +
							" hiddenFields: [{reference:\"CD_TRIBUTO\",value:\"" + getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0) + "\", comparator:_EQUAL,datatype:_INTEGER}],"+
							" filterFields: [[{label:\"Situação Tributï¿½ria\", reference:\"nm_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70},"+ 
							"					{label:\"CST\", reference:\"nr_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:30}]],"+
							" gridOptions: {columns:[{label:\"Situação Tributï¿½ria\",reference:\"NM_SITUACAO_TRIBUTARIA\"}," + 
							"						   {label:\"CST\",reference:\"NR_SITUACAO_TRIBUTARIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			nmParametro = "CD_SITUACAO_TRIBUTARIA_IMPORTACAO_II";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Situação Tributï¿½ria para Importaï¿½ï¿½o - II",
					"{caption:\"Selecionando situação tributï¿½ria\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.fsc.SituacaoTributariaDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_TRIBUTARIA\", dsField: \"NM_SITUACAO_TRIBUTARIA\"," +
							" hiddenFields: [{reference:\"CD_TRIBUTO\",value:\"" + getValorOfParametroAsInteger("CD_TRIBUTO_II", 0) + "\", comparator:_EQUAL,datatype:_INTEGER}],"+
							" filterFields: [[{label:\"Situação Tributï¿½ria\", reference:\"nm_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70},"+ 
							"					{label:\"CST\", reference:\"nr_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:30}]],"+
							" gridOptions: {columns:[{label:\"Situação Tributï¿½ria\",reference:\"NM_SITUACAO_TRIBUTARIA\"}," + 
							"						   {label:\"CST\",reference:\"NR_SITUACAO_TRIBUTARIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			nmParametro = "CD_SITUACAO_TRIBUTARIA_IMPORTACAO_ANTI_DUMPING";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Situação Tributï¿½ria para Importaï¿½ï¿½o - Anti Dumping",
					"{caption:\"Selecionando situação tributï¿½ria\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.fsc.SituacaoTributariaDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_TRIBUTARIA\", dsField: \"NM_SITUACAO_TRIBUTARIA\"," +
							" hiddenFields: [{reference:\"CD_TRIBUTO\",value:\"" + getValorOfParametroAsInteger("CD_TRIBUTO_ANTI_DUMPING", 0) + "\", comparator:_EQUAL,datatype:_INTEGER}],"+
							" filterFields: [[{label:\"Situação Tributï¿½ria\", reference:\"nm_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70},"+ 
							"					{label:\"CST\", reference:\"nr_situacao_tributaria\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:30}]],"+
							" gridOptions: {columns:[{label:\"Situação Tributï¿½ria\",reference:\"NM_SITUACAO_TRIBUTARIA\"}," + 
							"						   {label:\"CST\",reference:\"NR_SITUACAO_TRIBUTARIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Contabilização automática de estoque
			nmParametro = "LG_CONTABILIZACAO_ESTOQUE_VENDA_EXTERNA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Contabilizar estoque de vendas externas", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_NATUREZA_OPERACAO_ENTRADA_TRANSFERENCIA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza de Operação para transferï¿½ncia - Entradas",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_NATUREZA_OPERACAO_SAIDA_TRANSFERENCIA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza de Operação para transferï¿½ncia - Saidas",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Torna obrigatï¿½rio os documentos na hora do cadastro de pessoa
			nmParametro = "LG_DOCUMENTOS_OBRIGATORIOS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Tornar obrigatï¿½rio os documentos cpf/cnpj", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Torna obrigatï¿½rio os documentos na hora do cadastro de pessoa
			nmParametro = "LG_NCM_OBRIGATORIO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Tornar obrigatï¿½rio o campo NCM do produto no momento de cadastrar", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Torna obrigatï¿½rio a classificaï¿½ï¿½o fiscal do produto no momento de cadastrar
			nmParametro = "LG_CLASSIFICACAO_FISCAL";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Tornar obrigatï¿½rio a classificaï¿½ï¿½o fiscal do produto no momento de cadastrar", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);


			//Natureza de Operacao para devolucao de Venda substituto
			nmParametro	= "CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE_SUBSTITUTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"devolução do Cliente: Natureza de Operação Substituto",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//Natureza de Operacao para devolucao de Compra substituto
			nmParametro	= "CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"devolução para o Fornecedor: Natureza de Operação Substituto",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			//Natureza de Operacao para devolucao de Venda fora do estado
			nmParametro	= "CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE_OUTRO_ESTADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"devolução do Cliente: Natureza de Operação Fora do Estado",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//Natureza de Operacao para devolucao de Compra fora do estado
			nmParametro	= "CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_OUTRO_ESTADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"devolução para o Fornecedor: Natureza de Operação Fora do Estado",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			//Natureza de Operacao para devolucao de Venda fora do estado substituto
			nmParametro	= "CD_NATUREZA_OPERACAO_DEVOLUCAO_CLIENTE_SUBSTITUTO_OUTRO_ESTADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"devolução do Cliente: Natureza de Operação Substituto e Fora do Estado",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//Natureza de Operacao para devolucao de Compra fora do estado substituto
			nmParametro	= "CD_NATUREZA_OPERACAO_DEVOLUCAO_FORNECEDOR_SUBSTITUTO_OUTRO_ESTADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"devolução para o Fornecedor: Natureza de Operação Substituto e Fora do Estado",
					"{caption:\"Selecionar Natureza de Operação\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method: \"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"nm_natureza_operacao\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"					{label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			//Limita a quantidade de produtos que serï¿½ exibida na pesquisa de produtos do sistema.
			nmParametro	= "LG_QUANTIDADE_LIMITE_PRODUTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Quantidade de produtos na listagem da pesquisa", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);


			nmParametro	= "CD_TIPO_OCORRENCIA_LIBERACAO_CONTRATO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Merenda Escolar: Tipo de Ocorrï¿½ncia de Liberaï¿½ï¿½o de Contrato",
					"{caption:\"Selecionar Tipo de Ocorrï¿½ncia\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro	= "CD_TIPO_OCORRENCIA_RENOVACAO_CONTRATO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Merenda Escolar: Tipo de Ocorrï¿½ncia de Renovaï¿½ï¿½o de Contrato",
					"{caption:\"Selecionar Tipo de Ocorrï¿½ncia\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"NM_TIPO_OCORRENCIA\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);



		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO Biblioteca
	 ************************************************************************************************************/
	public static void initBlb()		{
		Connection connection = null;
		try	{
			connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "blb");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			//Usuário da Biblioteca (não vinculado a uma instituiï¿½ï¿½o)
			insertUpdate(new Parametro(0, 0, "CD_VINCULO_CIDADAO", NUMERICO, FILTER,
					"Vínculo - Usuário da Biblioteca",
					"{caption:\"Localize e selecione vinculo referente aos Usuários da Biblioteca\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connection);

			insertUpdate(new Parametro(0, 0, "QT_DIAS_EMPRESTIMO", NUMERICO, SINGLE_TEXT, "Quantidade de dias do emprástimo", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connection);

			insertUpdate(new Parametro(0, 0, "QT_MAX_RENOVACAO", NUMERICO, SINGLE_TEXT, "Quantidade de renovaï¿½ï¿½o de renovaï¿½ï¿½o do emprástimo", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connection);

			insertUpdate(new Parametro(0, 0, "QT_MAX_EMPRESTIMO", NUMERICO, SINGLE_TEXT, "Quantidade mï¿½xima de livros por usuï¿½rio", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connection);


		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connection);
		}
	}	

	/************************************************************************************************************
	 * MÓDULO Contratos
	 ************************************************************************************************************/
	public static void initCrt()		{
		Connection connection = null;
		try	{
			connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "ctr");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			String nmParametro = "TP_CONTRATO_DISPONIVEL";
			int cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Tipos de contratos disponï¿½veis" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connection);

			if(cdParametro > 0)	{
				PreparedStatement pstmtOpcoes = connection.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				String[] opcoesHints = {"Serviços", "Convï¿½nios", "Microcrédito", "Compras e Vendas", "Prestaï¿½ï¿½o de Serviços Educacionais",
						"Seguros", "Consï¿½rcios", "Planos de Saï¿½de"};
				for (int i=0; i<=7; i++) {
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, Integer.toString(i));
					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, opcoesHints[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/,
								0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					}
				}
			}

			nmParametro = "TP_VENCIMENTO_ADESAO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, COMBO_BOX /*tpApresentacao*/,
					"Vencimento Taxa Adesão Contratos" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR /*tpNivelAcesso*/), connection);
			if (cdParametro>0) {
				String[] options = {"Data primeira", "Data assinatura", "Início vigência"};
				for (int i=0; i<options.length; i++)
					ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, options[i] /*vlApresentacao*/, Integer.toString(i+1) /*vlReal*/,
							0 /*cdPessoa*/, 0 /*cdEmpresa*/));
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connection);
		}
	}

	/************************************************************************************************************
	 * MÓDULO CRM
	 ************************************************************************************************************/
	public static void initCrm()		{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "crm");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			// Servidor SMP usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			String nmParametro = "NM_SERVIDOR_SMTP_CRM";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Endereï¿½o Servidor SMTP (CRM)",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Login de Servidor SMP usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			nmParametro = "NM_LOGIN_SERVIDOR_SMTP_CRM";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Login de acesso ao Servidor SMTP (CRM)",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Senha de acesso ao Login de Servidor SMP usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			nmParametro = "NM_SENHA_SERVIDOR_SMTP_CRM";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, PASSWORD, "Senha de acesso ao Servidor SMTP (CRM)",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Email remetente usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			nmParametro = "NM_EMAIL_REMETENTE_SMTP_CRM";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Endereï¿½o remetente (para envio de Emails - CRM) ",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Email remetente usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			nmParametro = "LG_AUTENTICACAO_SMTP_CRM";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX, "Requisiï¿½ï¿½o de autenticaï¿½ï¿½o (para envio de Emails - CRM) ",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Email remetente usado para envio de e-mails aos participantes de agendamento quando da inclusï¿½o de uma ocorrï¿½ncia
			nmParametro = "LG_SSL_SMTP_CRM";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX, "Conexï¿½o criptografada SSL (para envio de Emails - CRM) ",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema/*cdSistema*/, NIVEL_ACESSO_OPERADOR), connect);
			// Lanï¿½amento de Atendimentos :: Preferï¿½ncias
			nmParametro = "FORMATENDIMENTO.PREFERENCES";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, MULTI_TEXT, "Cadastro e Manutenï¿½ï¿½o de Atendimento: Preferï¿½ncias",
					"", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_SISTEMA), connect);
			// Servidor XMPP de acesso interno ï¿½ rede
			nmParametro = "NM_SERVIDOR_XMPP_INTERNO";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Servidor XMPP de acesso interno ï¿½ rede",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Servidor XMPP de acesso externo ï¿½ rede
			nmParametro = "NM_SERVIDOR_XMPP_EXTERNO";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Servidor XMPP de acesso externo ï¿½ rede",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Central de atendimento padrão
			nmParametro = "CD_CENTRAL_ATENDIMENTO_PADRAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Central de atendimento padrão",
					"{caption:\"Selecionando central de atendimento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.crm.CentralAtendimentoDAO\",method: \"find\"," +
							" cdField: \"CD_CENTRAL\", dsField: \"NM_CENTRAL\"," +
							" filterFields: [[{label:\"Central de atendimento\",reference:\"nm_central\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Central de atendimento\",reference:\"NM_CENTRAL\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO JURIDICO
	 ************************************************************************************************************/
	public static void initJur()	{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "jur");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = ?");

			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesJur(cdSistema, cdModulo, connect);

			// Parametro para Vínculo de Advogado
			String nmParametro = "CD_VINCULO_ADVOGADO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Advogado",
					"{caption:\"Selecionando vinculo de advogado\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo de Advogado\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo de Advogado\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Advogado do Adverso
			nmParametro = "CD_VINCULO_ADVOGADO_ADVERSO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Advogado do Adverso",
					"{caption:\"Vínculo Advogado do Adverso\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo Advogado do Adverso\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo de Advogado do Adverso\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Vínculo de Adverso
			nmParametro = "CD_VINCULO_ADVERSO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Adverso",
					"{caption:\"Vínculo Adverso\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo Adverso\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo Adverso\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Vínculo de Correspondente
			nmParametro = "CD_VINCULO_CORRESPONDENTE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Correspondente",
					"{caption:\"Vínculo Correspondente\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo Correspondente\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo Correspondente\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Vínculo de Oficial de Justiï¿½a
			nmParametro = "CD_VINCULO_OFICIAL_JUSTICA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Oficial de Justiï¿½a",
					"{caption:\"Vínculo Oficial de Justiï¿½a\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo Oficial de Justiï¿½a\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo Oficial de Justiï¿½a\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			//Parametro para vinculo de Juiz/Relator
			nmParametro = "CD_VINCULO_JUIZ";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Vínculo: Juiz/Relator",
					"{caption:\"Vínculo Juiz/Relator\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.VinculoDAO\",method: \"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo Juiz/Relator\",reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo Juiz/Relator\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Habilitar acesso ás opções de protocolo
			nmParametro = "LG_PROTOCOLO_PROCESSOS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Protocolo: Exibir opções de protocolo de processos", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Habilitar acesso aos recortes
			nmParametro = "LG_RECORTES";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Recortes: Exibir recortes", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// ID usuario recortes 
			nmParametro = "ID_USUARIO_RECORTES";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Recortes: ID do usuario do servico de recortes.",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Marcar como Processado
			nmParametro = "LG_MARCAR_PROCESSADO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Recortes: Marcar como \"Processado\" ao lançar andamento.", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Serviço padrão de recortes
			nmParametro = "CD_SERVICO_PADRAO_RECORTES";
			int cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, COMBO_BOX,
					"Recortes: Serviço padrão de recortes", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tiposConfiguracao = {"T-Legal",
						"Sercortes",
				"Alerte"};
				for (int i=0; i<tiposConfiguracao.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposConfiguracao[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			// Habilitar acesso as opções de faturamento
			nmParametro = "LG_FATURAMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Faturamento: Exibir opções de faturamento", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Configurações para setor público ou privado
			nmParametro = "LG_SETOR_PUBLICO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Outros: Exibir configurações de setor público", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Habilitar painel de correspondente
			nmParametro = "LG_CORRESPONDENTE";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Correspondente: Exibir painel de correspondente", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Tipo de documento ao protocolar processo
			nmParametro = "CD_TIPO_DOCUMENTO_PROTOCOLO_PROCESSOS";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Documento de Protocolo de Processos",
					"{caption:\"Selecionando tipo de documento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.ptc.TipoDocumentoDAO\",method: \"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de documento\",reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// URL do portal 
			nmParametro = "NM_URL_PORTAL";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Outros: Endereï¿½o do Portal do Sistema.",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Notificação automática via email
			nmParametro = "LG_NOTIFICACAO_AUTOMATICA_EMAIL";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Notificação: Notificar automaticamente via email?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Destinatarios para notificação de agenda via email
			nmParametro	= "TP_DESTINATARIO_NOTIFICACAO_EMAIL_AGENDA";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Notificação: Destinatario para notificação de agenda", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tipos = {"Responsï¿½vel pela Agenda",
						"Responsavel pelo Processo",
				"Responsavel pelo Grupo de Trabalho"};
				for (int i=0; i<tipos.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tipos[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			// Destinatarios para notificação de andamentos processo via email
			nmParametro	= "TP_DESTINATARIO_NOTIFICACAO_EMAIL_ANDAMENTO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Notificação: Destinatario para notificação de andamentos de processo", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tipos = {"Responsavel pelo Processo",
				"Responsï¿½vel pelo Grupo de Trabalho"};
				for (int i=0; i<tipos.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tipos[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			// Modelo de emails (autotexto) para diligencias
			nmParametro = "CD_MODELO_EMAIL_PROCESSO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Notificação: Modelo de email de notificação de processo",
					"{caption:\"Selecionando modelo de documento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.ModeloDocumentoDAO\",method: \"find\"," +
							" cdField: \"CD_MODELO\", dsField: \"NM_MODELO\"," +
							" filterFields: [[{label:\"Nome do modelo\",reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do Modelo\",reference:\"NM_MODELO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Modelo de emails (autotexto) para diligencias
			nmParametro = "CD_MODELO_EMAIL_DILIGENCIA_CORRESPONDENTE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Notificação: Modelo de email para encaminhamento de diligï¿½ncias ao correspondente",
					"{caption:\"Selecionando modelo de documento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.ModeloDocumentoDAO\",method: \"find\"," +
							" cdField: \"CD_MODELO\", dsField: \"NM_MODELO\"," +
							" filterFields: [[{label:\"Nome do modelo\",reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do Modelo\",reference:\"NM_MODELO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Modelo de emails (autotexto) para agendas
			nmParametro = "CD_MODELO_EMAIL_AGENDA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Notificação: Modelo de email de notificação de agenda",
					"{caption:\"Selecionando modelo de documento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.ModeloDocumentoDAO\",method: \"find\"," +
							" cdField: \"CD_MODELO\", dsField: \"NM_MODELO\"," +
							" filterFields: [[{label:\"Nome do modelo\",reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do Modelo\",reference:\"NM_MODELO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Modelo de emails (autotexto) para andamentos de processo
			nmParametro = "CD_MODELO_EMAIL_ANDAMENTO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Notificação: Modelo de email de notificação de andamentos de processo",
					"{caption:\"Selecionando modelo de documento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.ModeloDocumentoDAO\",method: \"find\"," +
							" cdField: \"CD_MODELO\", dsField: \"NM_MODELO\"," +
							" filterFields: [[{label:\"Nome do modelo\",reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do Modelo\",reference:\"NM_MODELO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Parametro para tipo de documento para modelo de email
			nmParametro = "CD_TIPO_DOCUMENTO_EMAIL";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Notificação: Tipo de Documento para criaï¿½ï¿½o de modelos de email",
					"{caption:\"Selecionando tipo de documento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoDocumentoDAO\",method: \"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de documento\",reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Configuração de tipo de modelo para email
			nmParametro	= "TP_MODELO_EMAIL";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Notificação: Tipo de modelo para email", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<ModeloDocumentoServices.tipoModelo.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, ModeloDocumentoServices.tipoModelo[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			// Access Token (Hash) do repositorio OneDrive usado para armazenar modelos e documentos do GED
			nmParametro = "GED_ONEDRIVE_ACCESS_TOKEN";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, MULTI_TEXT, "GED: Token de acesso do repositï¿½rio OneDrive para o GED",
					"", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Refresh Token (Hash) do repositorio OneDrive usado para armazenar modelos e documentos do GED
			nmParametro = "GED_ONEDRIVE_REFRESH_TOKEN";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, MULTI_TEXT, "GED: Token de atualizaï¿½ï¿½o do repositï¿½rio OneDrive para o GED",
					"", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Client ID da aplicacao que acessa o repositï¿½rio OneDrive
			nmParametro = "GED_ONEDRIVE_CLIENT_ID";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "GED: ID do Cliente para o OneDrive (Client ID).",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Client Secret da aplicacao que acessa o repositï¿½rio OneDrive
			nmParametro = "GED_ONEDRIVE_CLIENT_SECRET";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "GED: Segredo do Cliente para o OneDrive (Client Secret).",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Client Secret da aplicacao que acessa o repositï¿½rio OneDrive
			nmParametro = "GED_ONEDRIVE_AUTHORIZATION_CODE";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "GED: Código de autorização para o OneDrive (Authorization Code).",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Andamento financeiro padrão (custas)
			nmParametro = "CD_ANDAMENTO_FINANCEIRO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Faturamento: Andamento financeiro padrão",
					"{caption:\"Selecionando andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Andamento para despesas
			nmParametro = "CD_TIPO_ANDAMENTO_DESPESA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Faturamento: Tipo de Andamento para despesa",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Andamento para receitas
			nmParametro = "CD_TIPO_ANDAMENTO_RECEITA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Faturamento: Tipo de Andamento para receitas",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Andamento para distribuicao
			nmParametro = "CD_TIPO_ANDAMENTO_DISTRIBUICAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Workflow: Andamento Automatico para Distribuicao",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Andamento para encerramento
			nmParametro = "CD_TIPO_ANDAMENTO_SENTENCA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Workflow: Andamento Automatico para Encerramento",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Tipo de Andamento para reativação
			nmParametro = "CD_TIPO_ANDAMENTO_REATIVACAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Workflow: Andamento Automatico para Reativação",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Andamento para acordo
			nmParametro = "CD_TIPO_ANDAMENTO_ACORDO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Workflow: Andamento Automatico para Acordo",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Andamento para inativaï¿½ï¿½o
			nmParametro = "CD_TIPO_ANDAMENTO_INATIVACAO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Workflow: Andamento Automatico para Inativacao",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Tipo de Andamento para inativaï¿½ï¿½o
						nmParametro = "CD_TIPO_ANDAMENTO_DESCONTRATACAO";
						insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
								"Workflow: Andamento Automatico para Descontratação",
								"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
										" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
										" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
										" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
										" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
										0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Tipo de Andamento para audiencia
			nmParametro = "CD_TIPO_ANDAMENTO_AUDIENCIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Workflow: Andamento Automatico para Audiencias",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Andamento para repasse
			nmParametro = "CD_TIPO_ANDAMENTO_REPASSE";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Workflow: Andamento Automatico para Repasses",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Andamento para mensagem
			nmParametro = "CD_TIPO_ANDAMENTO_MENSAGEM";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Workflow: Andamento Automatico para mensagem",
					"{caption:\"Selecionando tipo de andamento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.prc.TipoAndamentoServices\",method: \"find\"," +
							" cdField: \"CD_TIPO_ANDAMENTO\", dsField: \"NM_TIPO_ANDAMENTO\"," +
							" filterFields: [[{label:\"Nome do tipo de andamento\",reference:\"nm_tipo_andamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do tipo de Andamento\",reference:\"NM_TIPO_ANDAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Revisar custas
			nmParametro = "LG_REVISAR_CUSTAS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Faturamento: Revisao automatica de custas", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Revisar custas reembolsï¿½veis
			nmParametro = "LG_REVISAR_CUSTAS_REEMBOLSAVEIS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Faturamento: Revisao automatica de custas reembolsaveis", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Revisar custas não reembolsï¿½veis
			nmParametro = "LG_REVISAR_CUSTAS_NAO_REEMBOLSAVEIS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Faturamento: Revisao automatica de custas não reembolsaveis", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Configuração de Prazo Secundï¿½rio
			nmParametro	= "TP_PRAZO_SECUNDARIO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Workflow: Configuração de prazo secundário", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tiposConfiguracao = {"Workflow",
				"Sugestão"};
				for (int i=0; i<tiposConfiguracao.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposConfiguracao[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			
			//Executar os modelos a partir do web
			nmParametro = "LG_GED_WEB";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"GED: Executar Modelos via WEB", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Configuração do tipo de contagem dos prazos do workflow
			nmParametro	= "TP_CONTAGEM_PRAZO_DEFAULT";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Workflow: Contagem padrão dos dias dos prazos", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tiposConfiguracao = {"Dias corridos",
				"Dias úteis"};
				for (int i=0; i<tiposConfiguracao.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposConfiguracao[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			// URL do portal 
			nmParametro = "QT_JUIZOS";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, 
					"Processo: Quantidade de Juízos/Varas.",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Conta para débito DAJ
			nmParametro	= "CD_CONTA_PADRAO_DAJ";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"DAJ: Conta para débito",
					"{caption:\"Selecionando conta\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.ContaFinanceiraDAO\",method: \"find\"," +
							" cdField: \"CD_CONTA\", dsField: \"NM_CONTA\"," +
							" filterFields: [[{label:\"Conta\",reference:\"nm_conta\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nï¿½mero\",reference:\"NR_CONTA\"}," +
							"                        {label:\"Conta\",reference:\"NM_CONTA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Documento para DAJ
			nmParametro	= "CD_TIPO_DOCUMENTO_DAJ";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"DAJ: Tipo de Documento (Administrativo)",
					"{caption:\"Selecionando Tipo de Documento\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de Documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Arquivo para DAJ
			nmParametro	= "CD_TIPO_ARQUIVO_DAJ";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"DAJ: Tipo de Arquivo",
					"{caption:\"Selecionando tipo de arquivo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoArquivoDAO\", method:\"find\"," +
							" cdField: \"CD_TIPO_ARQUIVO\", dsField: \"NM_TIPO_ARQUIVO\"," +
							" filterFields: [[{label:\"Tipo de arquivo\", reference:\"nm_tipo_arquivo\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de arquivo\", reference:\"NM_TIPO_ARQUIVO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Servico para DAJ
			nmParametro	= "CD_SERVICO_PADRAO_DAJ";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"DAJ: Tipo de Serviço Padrão",
					"{caption:\"Selecionando o Serviço\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.ProdutoServicoDAO\", method:\"find\"," +
							" cdField: \"CD_PRODUTO_SERVICO\", dsField: \"NM_PRODUTO_SERVICO\"," +
							" filterFields: [[{label:\"Nome do Serviço\", reference:\"NM_PRODUTO_SERVICO\", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do serviï¿½o\", reference:\"NM_PRODUTO_SERVICO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);		

			//Tipo de Instância em um novo processo
			nmParametro	= "TP_INSTANCIA_PROCESSO_NOVO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Processo: Instância (Novo Processo)", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<ProcessoServices.tipoInstancia.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, ProcessoServices.tipoInstancia[i] /*vlApresentacao*/, Integer.toString(i+1) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			//Parte Cliente em um novo processo
			nmParametro	= "TP_PARTE_CLIENTE_PROCESSO_NOVO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Processo: Parte Cliente (Novo Processo)", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tiposCliente = {"Requerido", "Autor"};
				for (int i=0; i<tiposCliente.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposCliente[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			//Probabilidade de Perda um novo processo
			nmParametro	= "TP_PERDA_PROCESSO_NOVO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Processo: Probabilidade de Perda (Novo Processo)", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				for (int i=0; i<ProcessoServices.tipoPerda.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, ProcessoServices.tipoPerda[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			//Visualizacao do Orgao Judicial
			nmParametro	= "TP_VISUAL_ORGAO_JUDICIAL";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Processo: Visualizacao do Orgao Judicial", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tipos = {"Órgão: Sub-órgão", "Órgão: Comarca", "Sub-órgão: Comarca"};
				for (int i=0; i<tipos.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tipos[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			//Visualizacao do Orgao Judicial
			nmParametro	= "TP_OBJETO_ACAO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Processo: Objeto da Acao", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tipos = {"Preenchimento Livre", "Usar tabela de tipo"};
				for (int i=0; i<tipos.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tipos[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			// Revisar custas não reembolsaveis
			nmParametro = "LG_SHOW_PROCESSO_ANDAMENTO_POD";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Processo: Exibir Pod de últimos andamentos de processos", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Nome do orgao Judicial
			nmParametro = "NM_ORGAO_JUDICIAL";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, 
					"Nomenclatura Padrão: Nome do órgão Judicial",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Nome do Sub-orgao Judicial
			nmParametro = "NM_SUBORGAO_JUDICIAL";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, 
					"Nomenclatura Padrão: Nome do Sub-órgão Judicial",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Nome da Parte Contraria
			nmParametro = "NM_PARTE_CONTRARIA";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, 
					"Nomenclatura Padrão: Nome da Parte Contrária",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Nome da ï¿½rgï¿½o Instituicional
			nmParametro = "NM_ORGAO_INSTITUCIONAL";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, 
					"Nomenclatura Padrão: Nome do órgão Institucioanal",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// EDI de banco
			nmParametro = "LG_EDI_BANCO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Outros: EDI de Bancos", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Registro de Atendimento
			nmParametro = "LG_REGISTRO_ATENDIMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Outros: Exibe Opção de Registro de Atendimento", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Revisar custas
			nmParametro = "LG_SHOW_CALCULOS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Processo: Exibir cálculos de processos", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			
			// Habilitar acesso aos recortes
			nmParametro = "LG_RELATORIO_DINAMICO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Processo: Exibir relatórios configuráveis", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			
			// Revisar custas não reembolsaveis
			nmParametro = "LG_SALVAR_PROCESSO_KURIER";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Recortes: Incluir novos processos na base da Kurier", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			
			/**
			 * MOBILE
			 */
			nmParametro = "LG_ATIVAR_EQUIPAMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Mobile: Ativar equipamento no primeiro login", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);


		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO PROTOCOLO
	 ************************************************************************************************************/
	public static void initPtc()	{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo  = ? ");
			pstmt.setString(1, "ptc");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema  = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo   = isNext ? rs.getInt("cd_modulo")  : 0;

			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesPtc(cdSistema, cdModulo, connect);

			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = ?");
			// Tipo de documento para COMUNICAï¿½ï¿½O INTERNA
			String nmParametro = "CD_TIPO_DOCUMENTO_CI";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Documento CI",
					"{caption:\"Localize tipo de documento - CI\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tipo de documento padrão para contratos
			nmParametro = "CD_TIPO_DOCUMENTO_PTC_CONTRATO";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo: Tipo de Documento para Contrato" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			// Tipo de documento padrão para recurso
			nmParametro = "CD_TIPO_DOCUMENTO_RECURSO";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo: Tipo de Documento para Recurso" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// MOB - Tipo de documento padrão para apresentação de condutor
			nmParametro = "MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo - MOB: Tipo de Documento para Apresentação de Condutor" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// MOB - Tipo de documento padrão para defesa
			nmParametro = "MOB_CD_TIPO_DOCUMENTO_DEFESA";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo - MOB: Tipo de Documento para Defesa" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// MOB - Tipo de documento padrão para recurso
			nmParametro = "MOB_CD_TIPO_DOCUMENTO_JARI";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo - MOB: Tipo de Documento para JARI" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// MOB - Tipo de documento padrão para recurso
			nmParametro = "MOB_CD_TIPO_DOCUMENTO_CETRAN";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo - MOB: Tipo de Documento para JARI" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// MOB - Tipo de documento padrão para recurso
			nmParametro = "MOB_CD_TIPO_DOCUMENTO_RESTITUICAO";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo - MOB: Tipo de Documento para JARI" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// Fase Inicial
			nmParametro = "CD_FASE_INICIAL";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo: Fase Inicial" /*nmRotulo*/,
					"{caption:\"Localize tipo de documento - CI\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.FaseDAO\",method:\"find\"," +
							" cdField: \"CD_FASE\", dsField: \"NM_FASE\"," +
							" filterFields: [[{label:\"Fase\", reference:\"nm_fase\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Fase\",reference:\"NM_FASE\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// Fase Deferido
			nmParametro = "CD_FASE_DEFERIDO";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo: Deferido" /*nmRotulo*/,
					"{caption:\"Localize a fase\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.FaseDAO\",method:\"find\"," +
							" cdField: \"CD_FASE\", dsField: \"NM_FASE\"," +
							" filterFields: [[{label:\"Fase\", reference:\"nm_fase\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Fase\",reference:\"NM_FASE\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);

			// Fase FINALIZADO
			nmParametro = "CD_FASE_INDEFERIDO";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo: Indeferido" /*nmRotulo*/,
					"{caption:\"Localize fase de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.FaseDAO\",method:\"find\"," +
							" cdField: \"CD_FASE\", dsField: \"NM_FASE\"," +
							" filterFields: [[{label:\"Fase\", reference:\"nm_fase\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Fase\",reference:\"NM_FASE\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// Fase PENDENTE
			nmParametro = "CD_FASE_PENDENTE";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"Protocolo: Pendente" /*nmRotulo*/,
					"{caption:\"Localize fase de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.FaseDAO\",method:\"find\"," +
							" cdField: \"CD_FASE\", dsField: \"NM_FASE\"," +
							" filterFields: [[{label:\"Fase\", reference:\"nm_fase\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Fase\",reference:\"NM_FASE\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);

			// Ocorrï¿½ncia para mudanï¿½a de fase
			nmParametro = "CD_TIPO_OCORRENCIA_FASE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Ocorrï¿½ncia para mudanï¿½a de fase",
					"{caption:\"Tipo de ocorrï¿½ncia para mudanï¿½a de fase\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Ocorrï¿½ncia para sincronizaï¿½ï¿½o de documento
			nmParametro = "CD_TIPO_OCORRENCIA_SINC";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Ocorrï¿½ncia para sincronizaï¿½ï¿½o de documento",
					"{caption:\"Tipo de ocorrï¿½ncia para sincronizaï¿½ï¿½o de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Ocorrencia para DAM gerado
			nmParametro = "CD_TIPO_OCORRENCIA_DAM";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Ocorrï¿½ncia para DAM gerado",
					"{caption:\"Tipo de ocorrï¿½ncia para DAM gerado\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrï¿½ncia\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrï¿½ncia\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Ocorrencia para Carga
			nmParametro = "CD_TIPO_OCORRENCIA_CARGA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Ocorrência para Carga",
					"{caption:\"Tipo de ocorrência para Carga\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Ocorrencia para Devolucao de Carga
			nmParametro = "CD_TIPO_OCORRENCIA_DEV_CARGA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Ocorrência para Devolução de Carga",
					"{caption:\"Tipo de ocorrência para Devolução de Carga\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Tipo de Ocorrência\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Ocorrência\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Situação do Documento Em Aberto - Tramitando
			nmParametro = "CD_SIT_DOC_EM_ABERTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Situação do Documento Recebido/Parado",
					"{caption:\"Situação do documento - Recebido/Parado\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.SituacaoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_DOCUMENTO\", dsField: \"NM_SITUACAO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Situação do Documento Arquivado
			nmParametro = "CD_SIT_DOC_ARQUIVADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Situação do Documento Arquivado",
					"{caption:\"Situação do documento - Arquivado\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.SituacaoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_DOCUMENTO\", dsField: \"NM_SITUACAO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Situação do Documento enviado pr0a Setor Externo
			nmParametro = "CD_SIT_DOC_ENVIO_EXTERNO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Situação do Documento em Setor Externo",
					"{caption:\"Situação do documento - Externo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.SituacaoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_DOCUMENTO\", dsField: \"NM_SITUACAO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Situação do Documento Em Aberto - Tramitando
			nmParametro = "CD_SIT_DOC_TRAMITANDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Situação do Documento Tramitando",
					"{caption:\"Situação do documento - em Tramite\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.SituacaoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_DOCUMENTO\", dsField: \"NM_SITUACAO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Situação do documento EM ANDAMENTO
			nmParametro = "CD_SIT_DOC_EM_ANDAMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Situação do documento em andamento",
					"{caption:\"Localize situação do documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.SituacaoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_DOCUMENTO\", dsField: \"NM_SITUACAO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Situacao do documento\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situacao do Documento\",reference:\"NM_SITUACAO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// ALTERAï¿½ï¿½O
			nmParametro = "NR_SEQUENCIAL_GERAL";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, "Sequencial Geral dos Documentos", "",
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Ocorrï¿½ncia inicial
			nmParametro = "CD_OCORRENCIA_INICIAL";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de ocorrencia inicial do documento",
					" {caption:\"Tipo de Ocorrencia inicial do documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Ocorrï¿½ncia\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Ocorrï¿½ncia inicial
			nmParametro = "CD_OCORRENCIA_INDEXACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de ocorrencia de indexacao automática de documento",
					" {caption:\"Tipo de Ocorrencia de indexacao automática de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Ocorrï¿½ncia\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Ocorrï¿½ncia protocolo de processo
			nmParametro = "CD_OCORRENCIA_PROTOCOLO_PROCESSO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de ocorrencia de protocolo de processo",
					" {caption:\"Tipo de Ocorrencia de Protocolo de Processo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Ocorrï¿½ncia\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Ocorrï¿½ncia protocolo de processo
			nmParametro = "CD_OCORRENCIA_MUDANCA_TIPO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de ocorrencia de mudanca de tipo de documento",
					" {caption:\"Tipo de Ocorrencia de mudanca de tipo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Ocorrï¿½ncia\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Ocorrï¿½ncia de envio de documento
			nmParametro = "CD_OCORRENCIA_ENVIO_DOCUMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de ocorrencia de envio de documento",
					" {caption:\"Tipo de Ocorrencia de envio de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Ocorrï¿½ncia\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Ocorrï¿½ncia de arquivamento de documento
			nmParametro = "CD_OCORRENCIA_ARQUIVAMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de ocorrencia de arquivamento de documento",
					" {caption:\"Tipo de Ocorrencia de arquivamento de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoOcorrenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_OCORRENCIA\", dsField: \"NM_TIPO_OCORRENCIA\"," +
							" filterFields: [[{label:\"Ocorrï¿½ncia\", reference:\"nm_tipo_ocorrencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_TIPO_OCORRENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de documento para REGISTRO DE RECLAMACAO
			nmParametro = "CD_TIPO_DOCUMENTO_RECLAMACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Documento de Registro de Reclamacao",
					"{caption:\"Localize tipo de documento - Reclamacao\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de documento para ATENDIMENTO POR TELEFONE
			nmParametro = "CD_TIPO_DOCUMENTO_ATEND_TELEFONE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Documento para Atendimento por telefone",
					"{caption:\"Localize tipo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Tipo de documento para SERVICOS DO PORTAL
						nmParametro = "CD_TIPO_DOCUMENTO_PORTAL";
						insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
								"Protocolo: Tipo de Documento para Servicos do Portal",
								"{caption:\"Localize tipo de documento - Reclamacao\", width:680, height:350, modal:true," +
										" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
										" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
										" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
										" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
										0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de documento para INFORMACAO
			nmParametro = "CD_TIPO_DOCUMENTO_INFORMACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Documento para Informacao",
					"{caption:\"Localize tipo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de documento para PROTOCOLO DE PROCESSO
			nmParametro = "CD_TIPO_DOCUMENTO_PROCESSO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Documento para Processo",
					"{caption:\"Localize tipo de documento - Protocolo de Processo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de documento para PROTOCOLO DE PROCESSO
			nmParametro = "CD_TIPO_DOCUMENTO_PROCESSO_ATENDIMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Documento do Processo derivado de Atendimento",
					"{caption:\"Localize tipo de documento - Processo derivado de Atendimento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de documento não CLASSIFICADO/ENCONTRADO
			nmParametro = "CD_TIPO_DOCUMENTO_NAO_CLASSIFICADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Tipo de Documento não encontrado/classificado",
					"{caption:\"Localize tipo de documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Tipos de documento que permitirao documentos com numeracao iguais
			nmParametro = "CD_TIPO_DOCUMENTO_NUMERACAO_ANALOGA";
			int cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Protocolo: Tipos de documento que permitem mesma numeração" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			if(cdParametro >= 0)	{
				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtTipoDocumento = connect.prepareStatement("SELECT * FROM gpn_tipo_documento ORDER BY nm_tipo_documento");
				ResultSetMap rsmTiposDocumento = new ResultSetMap(pstmtTipoDocumento.executeQuery());
				while(rsmTiposDocumento.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmTiposDocumento.getString("CD_TIPO_DOCUMENTO"));

					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmTiposDocumento.getString("NM_TIPO_DOCUMENTO") /*vlApresentacao*/, 
								rsmTiposDocumento.getString("CD_TIPO_DOCUMENTO") /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}
			}

			// Tipo de documentação para PROTOCOLO DE LIGACAO
			nmParametro = "CD_DOCUMENTACAO_PROTOCOLO_LIGACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Atendimento: Tipo de Documentacao para protocolo de ligacao",
					"{caption:\"Localize tipo de documentação\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.TipoDocumentacaoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTACAO\", dsField: \"NM_TIPO_DOCUMENTACAO\"," +
							" filterFields: [[{label:\"Tipo de documentação\", reference:\"nm_tipo_documentacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documentaï¿½ï¿½o\",reference:\"NM_TIPO_DOCUMENTACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo de Local para SALAS DE AUDIENCIA
			nmParametro = "CD_TIPO_LOCAL_AUDIENCIAS";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Atendimento: Tipo de Local para as salas de audiencia",
					"{caption:\"Localize o tipo de local para as audiencias\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.agd.TipoLocalDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_LOCAL\", dsField: \"NM_TIPO_LOCAL\"," +
							" filterFields: [[{label:\"Tipo de Local\", reference:\"nm_tipo_local\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Local\",reference:\"NM_TIPO_LOCAL\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Setor destino ATENDIMENTO
			nmParametro = "CD_SETOR_DESTINO_TRAMITACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Atendimento: Setor de destino do Atendimento",
					"{caption:\"Localize o setor de destino do atendimento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.SetorDAO\",method:\"find\"," +
							" cdField: \"CD_SETOR\", dsField: \"NM_SETOR\"," +
							" filterFields: [[{label:\"Setor\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Setor\",reference:\"NM_SETOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Setor FISCALIZACAO
			nmParametro = "CD_SETOR_FISCALIZACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Atendimento: Setor de Fiscalizacao",
					"{caption:\"Localize o setor de fiscalizacao\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.SetorDAO\",method:\"find\"," +
							" cdField: \"CD_SETOR\", dsField: \"NM_SETOR\"," +
							" filterFields: [[{label:\"Setor\", reference:\"nm_situacao_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Setor\",reference:\"NM_SETOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Setor FINANCEIRO
			nmParametro = "CD_SETOR_FINANCEIRO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Setor Financeiro",
					"{caption:\"Localize o setor financeiro\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.SetorDAO\",method:\"find\"," +
							" cdField: \"CD_SETOR\", dsField: \"NM_SETOR\"," +
							" filterFields: [[{label:\"Setor\", reference:\"nm_setor\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Setor\",reference:\"NM_SETOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Setor PROTOCOLO
			nmParametro = "CD_SETOR_PROTOCOLO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Setor Protocolo",
					"{caption:\"Localize o setor protocolo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.SetorDAO\",method:\"find\"," +
							" cdField: \"CD_SETOR\", dsField: \"NM_SETOR\"," +
							" filterFields: [[{label:\"Setor\", reference:\"nm_setor\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Setor\",reference:\"NM_SETOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Setor ATENDIMENTO
			nmParametro = "CD_SETOR_ATENDIMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Atendimento: Setor Atendimento",
					"{caption:\"Localize o setor atendimento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.SetorDAO\",method:\"find\"," +
							" cdField: \"CD_SETOR\", dsField: \"NM_SETOR\"," +
							" filterFields: [[{label:\"Setor\", reference:\"nm_setor\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Setor\",reference:\"NM_SETOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Setor não INFORMADO
			nmParametro = "CD_SETOR_DESCONHECIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Setor não informado",
					"{caption:\"Localize o setor padrão\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.SetorDAO\",method:\"find\"," +
							" cdField: \"CD_SETOR\", dsField: \"NM_SETOR\"," +
							" filterFields: [[{label:\"Setor\", reference:\"nm_setor\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Setor\",reference:\"NM_SETOR\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo pendência impressão DE NOTIFICACAO
			nmParametro = "CD_TP_PENDENCIA_IMPRESSAO_NOTIFICACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Atendimento: Tipo de pendência da impressão de notificação",
					"{caption:\"Localize o tipo de pendência\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoPendenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_PENDENCIA\", dsField: \"NM_TIPO_PENDENCIA\"," +
							" filterFields: [[{label:\"Pendï¿½ncia\", reference:\"nm_tipo_pendencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Pendï¿½ncia\",reference:\"NM_TIPO_PENDENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Tipo pendência ENTREGA DE NOTIFICACAO
			nmParametro = "CD_TP_PENDENCIA_ENTREGA_NOTIFICACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Atendimento: Tipo de pendência da entrega de notificação",
					"{caption:\"Localize o tipo de pendência\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoPendenciaDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_PENDENCIA\", dsField: \"NM_TIPO_PENDENCIA\"," +
							" filterFields: [[{label:\"Pendï¿½ncia\", reference:\"nm_tipo_pendencia\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Pendï¿½ncia\",reference:\"NM_TIPO_PENDENCIA\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Destinatarios para notificação via email de ocorrencias em documentos 
			nmParametro	= "TP_DESTINATARIO_NOTIFICACAO_EMAIL_OCORRENCIA";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Notificação: Destinatario para notificação de ocorrencias de documentos", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tipos = {"Todas as partes envolvidas", 
				"Solicitantes do Documento"};
				for (int i=0; i<tipos.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tipos[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			// Modelo de emails (autotexto) para ocorrencias de documentos
			nmParametro = "CD_MODELO_EMAIL_OCORRENCIA";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, FILTER,
					"Notificação: Modelo de email de notificação de ocorrências em documentos",
					"{caption:\"Selecionando modelo de documento\",width:680,height:350,modal:true," +
							" className:\"com.tivic.manager.grl.ModeloDocumentoDAO\",method: \"find\"," +
							" cdField: \"CD_MODELO\", dsField: \"NM_MODELO\"," +
							" filterFields: [[{label:\"Nome do modelo\",reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome do Modelo\",reference:\"NM_MODELO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Habilitar acesso ás opções de protocolo
			nmParametro = "LG_MODULO_ATENDIMENTO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Atendimento: Exibir módulo de atendimento", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Habilitar validação de horario na agenda de acordo o tipo de empresa
			nmParametro = "LG_VALIDAR_HORARIO";
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, LOGICO, CHECK_BOX,
					"Atendimento: Validar Horário", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Habilitar integração com a E&L
			nmParametro = "LG_PROTOCOLO_EEL";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Protocolo: Habilitar integração com o protocolo da E&L", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Usuário externo do módulo
			nmParametro = "CD_USUARIO_EXTERNO_PTC";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Protocolo: Usuário externo do módulo PTC",
					"{caption:\"Localize o usuario\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.seg.UsuarioDAO\",method:\"find\"," +
							" cdField: \"CD_USUARIO\", dsField: \"NM_LOGIN\"," +
							" filterFields: [[{label:\"Usuário\", reference:\"nm_login\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Usuário\",reference:\"NM_USUARIO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Usuário externo do módulo
			nmParametro = "CD_MODELO_NOTIFICACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"GED: a notificação",
					"{caption:\"Localize um modelo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.ModeloDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_MODELO\", dsField: \"NM_MODELO\"," +
							" filterFields: [[{label:\"Modelo\", reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Modelo\",reference:\"NM_MODELO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_MODELO_NOTIFICACAO_DECISAO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"GED: a notificação para decisão",
					"{caption:\"Localize um modelo\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.ModeloDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_MODELO\", dsField: \"NM_MODELO\"," +
							" filterFields: [[{label:\"Modelo\", reference:\"nm_modelo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Modelo\",reference:\"NM_MODELO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Habilitar impressão de protocolo de envio
			nmParametro = "LG_PRINT_TRAMITACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Protocolo: Imprimir protocolo de envio", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Por padrão, gerar ou não a numeração de documentos automaticamente
			nmParametro = "LG_PTC_NUMERACAO_AUTOMATICA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Protocolo: numeração automática", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Habilitar Workflow
			nmParametro = "LG_PTC_WORKFLOW";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Protocolo: Habilitar workflow de Documentos", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			//Exibir setores inativos ao enviar documento
			nmParametro = "LG_EXIBIR_SETOR_INATIVO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Protocolo: Exibir setores inativos ao tramitar", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			

			//ENVOLVIDOS
			nmParametro = "LG_ENVOLVIDOS_VINCULO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Protocolo: Envolvidos limitados por vinculo", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			nmParametro = "TP_ENVOLVIDOS_VINCULO";
			cdParametro = insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, LIST_BOX_MULTI_SELECTION /*tpApresentacao*/,
					"Protocolo: Envolvidos com os vinculos" /*nmRotulo*/, null /*txtUrlFiltro*/, 0 /*cdPessoa*/,
					TP_GERAL /*tpParametro*/, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR /*tpNivelAcesso*/), connect);

			if(cdParametro >= 0)	{

				cdParametro = getByName(nmParametro, connect).getCdParametro();
				PreparedStatement pstmtOpcoes = connect.prepareStatement("SELECT * " +
						"FROM grl_parametro_opcao " +
						"WHERE cd_parametro = ? " +
						"  AND vl_real = ?");
				PreparedStatement pstmtVinculos = connect.prepareStatement("SELECT * FROM grl_vinculo");
				ResultSetMap rsmVinculos = new ResultSetMap(pstmtVinculos.executeQuery());
				while(rsmVinculos.next()){
					pstmtOpcoes.setInt(1, cdParametro);
					pstmtOpcoes.setString(2, rsmVinculos.getString("CD_VINCULO"));

					if (!pstmtOpcoes.executeQuery().next()) {
						ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, rsmVinculos.getString("NM_VINCULO") /*vlApresentacao*/, 
								rsmVinculos.getString("CD_VINCULO") /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/));
					} 
				}
			}
			
			//Exibir Ordem de Serviço
			nmParametro = "LG_EXIBIR_OS";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Protocolo: Exibir Ordem de Serviço", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/*
	 * 
	 */
	public static void initGed()	{
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "ged");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;

			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = ?");

			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesGed(cdSistema, cdModulo, connect);
			
			
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/************************************************************************************************************
	 * MÓDULO MSG
	 ************************************************************************************************************/
	public static void initMsg() {
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "msg");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;
			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = ?");

			/**
			 * GRUPO NOTIFICACAO
			 */
			//notificacao de NOVA AUDIENCIA
			String nmParametro = "CD_GRUPO_AUDIENCIA_NOVO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Nova Audiência",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//notificacao de AUDIENCIA CUMPRIDA
			nmParametro = "CD_GRUPO_AUDIENCIA_CUMPRIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Audiência Cumprida",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//notificacao de AUDIENCIA CANCELADA
			nmParametro = "CD_GRUPO_AUDIENCIA_CANCELADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Audiência Cancelada",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//notificacao de AUDIENCIA EXCLUIDA
			nmParametro = "CD_GRUPO_AUDIENCIA_EXCLUIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Audiência Excluída",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			
			//notificacao de NOVO PRAZO
			nmParametro = "CD_GRUPO_PRAZO_NOVO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Novo Prazo",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//notificacao de PRAZO CUMPRIDA
			nmParametro = "CD_GRUPO_PRAZO_CUMPRIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Prazo Cumprido",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//notificacao de PRAZO CANCELADA
			nmParametro = "CD_GRUPO_PRAZO_CANCELADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Prazo Cancelado",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//notificacao de PRAZO EXCLUIDO
			nmParametro = "CD_GRUPO_PRAZO_EXCLUIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Prazo Excluído",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//notificacao de NOVA DILIGENCIA
			nmParametro = "CD_GRUPO_DILIGENCIA_NOVO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Nova Diligência",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//notificacao de DILIGENCIA CUMPRIDA
			nmParametro = "CD_GRUPO_DILIGENCIA_CUMPRIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Diligência Cumprida",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			//notificacao de DILIGENCIA CANCELADA
			nmParametro = "CD_GRUPO_AUDIENCIA_CANCELADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Diligência Cancelada",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			//notificacao de DILIGENCIA EXCLUIDA
			nmParametro = "CD_GRUPO_DILIGENCIA_EXCLUIDO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Notificação: Diligência Excluída",
					"{caption:\"Localize e selecione\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.msg.GrupoNotificacaoDAO\",method:\"find\"," +
							" cdField: \"CD_GRUPO_NOTIFICACAO\", dsField: \"NM_GRUPO_NOTIFICACAO\"," +
							" filterFields: [[{label:\"Grupo\", reference:\"nm_grupo_notificacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Grupo\",reference:\"NM_GRUPO_NOTIFICACAO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
//			nmParametro = "VL_REFRESH_NOTIFICACAO";
//			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT, 
//					"Notificação: Tempo para verificação de novas notificações (em segundos)", "",
//					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO OS
	 ************************************************************************************************************/
	public static void initOrd() {
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? ");
			pstmt.setString(1, "os");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo = isNext ? rs.getInt("cd_modulo") : 0;
			pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = ?");

			String nmParametro = "CD_VINCULO_TECNICO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"OS: Vínculo Técnicos",
					"{caption:\"Localize e selecione vinculo referente a Técnicos\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.VinculoDAO\",method:\"find\"," +
							" cdField: \"CD_VINCULO\", dsField: \"NM_VINCULO\"," +
							" filterFields: [[{label:\"Vínculo\", reference:\"nm_vinculo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Vínculo\",reference:\"NM_VINCULO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_SITUACAO_EM_ABERTO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"OS: Situação: Em aberto",
					"{caption:\"Localize e selecione situação para OS: EM ABERTO\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ord.SituacaoServicoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_SERVICO\", dsField: \"NM_SITUACAO_SERVICO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_servico\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_SERVICO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_SITUACAO_ENCERRADA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"OS: Situação: Encerrada",
					"{caption:\"Localize e selecione situação para OS: ENCERRADA\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ord.SituacaoServicoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_SERVICO\", dsField: \"NM_SITUACAO_SERVICO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_servico\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_SERVICO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			nmParametro = "CD_SITUACAO_CANCELADA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"OS: Situação: Cancelada",
					"{caption:\"Localize e selecione situação para OS: CANCELADA\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ord.SituacaoServicoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_SERVICO\", dsField: \"NM_SITUACAO_SERVICO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"nm_situacao_servico\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_SERVICO\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Situação FINALIZADA
			nmParametro = "CD_SITUACAO_FINALIZADA";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"OS: Situação Finalizada" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ord.SituacaoServicoDAO\",method:\"find\"," +
							" cdField: \"CD_SITUACAO_SERVICO\", dsField: \"NM_SITUACAO_SERVICO\"," +
							" filterFields: [[{label:\"Situação\", reference:\"A.nm_situacao_servico\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Situação\",reference:\"NM_SITUACAO_SERVICO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// Tipo de documento padrão para OS
			nmParametro = "CD_TIPO_DOCUMENTO_ORDEM_SERVICO";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"OS: Tipo de Documento para OS" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ptc.TipoDocumentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_DOCUMENTO\", dsField: \"NM_TIPO_DOCUMENTO\"," +
							" filterFields: [[{label:\"Tipo de documento\", reference:\"A.nm_tipo_documento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Documento\",reference:\"NM_TIPO_DOCUMENTO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			//Local de Armazenamento padrao para OS
			nmParametro	= "CD_LOCAL_ARMAZENAMENTO_DEFAULT_OS";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"OS: Local de Armazenamento Padrão",
					"{caption:\"Selecione Local de Armazenamento Default\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.alm.LocalArmazenamentoServices\",method: \"findCompleto\"," +
							" cdField: \"CD_LOCAL_ARMAZENAMENTO\", dsField: \"NM_LOCAL_ARMAZENAMENTO\"," +
							" filterFields: [[{label:\"Local de Armazenamento\",reference:\"nm_local_armazenamento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Local de Armazenamento\",reference:\"NM_LOCAL_ARMAZENAMENTO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			
			// Tipo de documento padrão para OS
			nmParametro = "CD_TIPO_ATENDIMENTO_CARRO_PIPA";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"OS: Tipo de Atendimento Carro-pipa" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ord.TipoAtendimentoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_ATENDIMENTO\", dsField: \"NM_TIPO_ATENDIMENTO\"," +
							" filterFields: [[{label:\"Tipo de Atendimento\", reference:\"A.nm_tipo_atendimento\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo Atendimento\",reference:\"NM_TIPO_ATENDIMENTO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// Tipo de documento padrão para OS
			nmParametro = "CD_TECNICO_CARRO_PIPA";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"OS: Técnico padrão Carro-pipa" /*nmRotulo*/,
					"{caption:\"Técnico\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.PessoaDAO\",method:\"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Técnico\", reference:\"A.nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Pessoa\",reference:\"NM_PESSOA\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			// Tipo de documento padrão para OS
			nmParametro = "CD_TIPO_LAUDO_CONCLUIDO";
			insertUpdate(new Parametro(0 /*cdParametro*/, 0 /*cdEmpresa*/, nmParametro, NUMERICO /*tpDado*/, FILTER /*tpApresentacao*/,
					"OS: Laudo: Concluído" /*nmRotulo*/,
					"{caption:\"Tipo de Documento\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.ord.TipoLaudoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_LAUDO\", dsField: \"NM_TIPO_LAUDO\"," +
							" filterFields: [[{label:\"Tipo de Atendimento\", reference:\"A.nm_tipo_laudo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tipo de Laudo\",reference:\"NM_TIPO_LAUDO\"}]}}",
							0 /*cdPessoa*/, ParametroServices.TP_GERAL /*tpParametro*/,
							cdModulo, cdSistema, ParametroServices.NIVEL_ACESSO_SISTEMA), connect);
			
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}


	/************************************************************************************************************
	 * MÓDULO GOVERNO ELETRÔNICO
	 ************************************************************************************************************/
	public static void initEgov()	{
		Connection connect = Conexao.conectar();
		try	{
			ResultSet rs = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo  = \'egov\'").executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo  = isNext ? rs.getInt("cd_modulo") : 0;

			//PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = ?");
			// % Multa DAM/IPTU
			String nmParametro = "PR_MULTA_DAM_IPTU";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"% Multa (DAM/IPTU)",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Ano Base
			nmParametro = "NR_ANO_BASE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Ano Base",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Juros DAM/IPTU
			nmParametro = "PR_JUROS_DAM_IPTU";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"% Juros (DAM/IPTU)",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Valor da taxa de expediente
			nmParametro = "VL_TAXA_EXPEDIENTE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Valor da taxa de expediente:",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Valor da taxa de expediente
			nmParametro = "PR_DESCONTO_COTA_UNICA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"% Desconto cota única (DAM/IPTU):",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// impressão do DAM-IPTU com/sem CPF
			nmParametro = "LG_PERMITE_DAM_SEM_CPF";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Permitir impressão de DAM/IPTU sem o CPF do Contribuinte?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Imprimir somente parcela única?
			nmParametro = "LG_SOMENTE_PARCELA_UNICA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Permitir impressão somente da parcela única?", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// URL do Banco de dados do DAM/IPTU
			nmParametro = "URL_BD_NFE";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Banco de Dados - URL para acesso ao DAM/IPTU",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Driver para acesso ao Banco de dados do DAM/IPTU
			nmParametro = "DRIVER_BD_NFE";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Banco de Dados - Driver para acesso ao DAM/IPTU:",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Usuário para acesso ao Banco de dados do DAM/IPTU
			nmParametro = "USER_BD_NFE";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Banco de Dados - Usuário para acesso ao DAM/IPTU:",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Usuário para acesso ao Banco de dados do DAM/IPTU
			nmParametro = "PASS_BD_NFE";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, PASSWORD,
					"Banco de Dados - Senha para acesso ao DAM/IPTU:",
					null, 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Habilitar integração com a E&L
			nmParametro = "LG_INTEGRACAO_EL";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Habilitar integração com a E&L", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO GESTÃO FISCAL
	 ************************************************************************************************************/
	public static void initFsc()	{
		Connection connect = Conexao.conectar();
		try	{
			ResultSet rs = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo  = \'fsc\'").executeQuery();
			boolean isNext = rs.next();
			int cdSistema  = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo   = isNext ? rs.getInt("cd_modulo") : 0;

			// Nï¿½ Nota Fiscal
			String nmParametro = "NR_NOTA_FISCAL";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Nï¿½ Nota Fiscal", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Permitir que nota possa ser criada com documento em conferencia
			nmParametro = "LG_NUMERACAO_PARAMETRO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Deseja que a serializaï¿½ï¿½o da numeração das notas fiscais sejam feitas pelo parï¿½metro?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Nï¿½ Nota Fiscal - via SCAN
			nmParametro = "NR_NOTA_FISCAL_SCAN";
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Nï¿½ Nota Fiscal (SCAN)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Natureza da Operação [CFOP] - Substituição de Cupons
			nmParametro = "CD_NATUREZA_OPERACAO_SUBST_CUPOM";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza da Operação - Substituição de Cupons",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"				 {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Natureza da Operação [CFOP] - Substituição de Cupons para fora do estado
			nmParametro = "CD_NATUREZA_OPERACAO_SUBST_CUPOM_OUTRO_ESTADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza da Operação - Substituição de Cupons (para outro estado)",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"				 {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Natureza da Operação [CFOP] - Substituição de Cupons para fora do estado
			nmParametro = "CD_NATUREZA_OPERACAO_OUTRO_ESTADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza da Operação (para outro estado)",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"				 {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Natureza da Operação [CFOP] - Substituição de Cupons
			nmParametro = "CD_NATUREZA_OPERACAO_SUBST_TRIBUTARIA";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza da Operação - Substituição Tributaria",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"				 {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Natureza da Operação [CFOP] - Substituição de Cupons
			nmParametro = "CD_NATUREZA_OPERACAO_SUBST_TRIBUTARIA_OUTRO_ESTADO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Natureza da Operação - Substituição Tributaria para outro estado",
					"{caption:\"Selecionando Natureza da Operação\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.adm.NaturezaOperacaoDAO\",method:\"find\"," +
							" cdField: \"CD_NATUREZA_OPERACAO\", dsField: \"NM_NATUREZA_OPERACAO\"," +
							" filterFields: [[{label:\"CFOP\",reference:\"nr_codigo_fiscal\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}, " +
							"				 {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80,charcase:\'normal\'}]],"+
							" gridOptions: {columns:[{label:\"CFOP\",reference:\"nr_codigo_fiscal\"}, " +
							"			               {label:\"Natureza de Operação\",reference:\"nm_natureza_operacao\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Permitir substituir cupons e outros tipos de documentos
			insertUpdate(new Parametro(0, 0, "LG_NFE_SUBST_CUPOM_OUTROS", LOGICO, CHECK_BOX,
					"Permitir substituir cupons e outros tipos de documentos na mesma nota", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Série da NFE
			nmParametro = "NR_SERIE"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Nï¿½ Série", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Série da NFE
			nmParametro = "NR_SERIE_SCAN"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Nï¿½ Série (SCAN)", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Código UF Padrão
			nmParametro = "CD_UF_PADRAO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Código UF Padrão", null,
					0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Arquivo Cacerts
			nmParametro = "NM_FILE_CACERTS"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Arquivo Cacerts", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Arquivo do Certificado
			nmParametro = "NM_FILE_CERTIFICADO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"A3: Arquivo de Configuração (Cartï¿½o:SmartCard.cfg,Token:Token.cfg)/A1: Certificado", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Senha do Certificado
			nmParametro = "DS_PASS_CERTIFICADO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, PASSWORD,
					"Senha do Certificado", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tipo de Certificados
			nmParametro = "TP_CERTIFICADO"; 
			int cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, COMBO_BOX,
					"Tipo de Certificado", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] labels = {"A1", "A3"};
				String[] values = {"1", "3"};
				for (int i=0; i<labels.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, labels[i] /*vlApresentacao*/, values[i] /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			// Tipo de Ambiente
			nmParametro = "TP_AMBIENTE"; 
			cdParametro = insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, COMBO_BOX,
					"Tipo de Ambiente", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] labels = {"Produï¿½ï¿½o", "Homologaï¿½ï¿½o"};
				String[] values = {"1", "2"};
				for (int i=0; i<labels.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, labels[i] /*vlApresentacao*/, values[i] /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}
			// Arquivo Xsd de Envio
			nmParametro = "NM_FILE_XSD_ENVIO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Arquivo Xsd de Envio", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Arquivo Xsd de Cancelamento
			nmParametro = "NM_FILE_XSD_CANCELAMENTO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Arquivo Xsd de Cancelamento", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Arquivo Xsd de Inutilizaï¿½ï¿½o
			nmParametro = "NM_FILE_XSD_INUTILIZACAO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Arquivo Xsd de Inutilizaï¿½ï¿½o", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Arquivo Xsd de Consulta de Cadastro
			nmParametro = "NM_FILE_XSD_CONSULTA"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Arquivo Xsd de Consulta", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Arquivo Xsd de Envio de DPEC
			nmParametro = "NM_FILE_XSD_ENVIO_DPEC"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT, "Arquivo Xsd de Envio via DPEC", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Parametro para ICMS (PDV)
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_ICMS", NUMERICO, FILTER,
					"Tributo ICMS",
					"{caption:\"Selecionando Tributo\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TributoDAO\",method: \"find\"," +
							" cdField: \"CD_TRIBUTO\", dsField: \"NM_TRIBUTO\"," +
							" filterFields: [[{label:\"Tributo\",reference:\"nm_tributo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tributo\",reference:\"NM_TRIBUTO\"}," +
							"                        {label:\"Esfera Governamental\",reference:\"CL_TIPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para IPI 
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_IPI", NUMERICO, FILTER,
					"Tributo IPI",
					"{caption:\"Selecionando Tributo\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TributoDAO\",method: \"find\"," +
							" cdField: \"CD_TRIBUTO\", dsField: \"NM_TRIBUTO\"," +
							" filterFields: [[{label:\"Tributo\",reference:\"nm_tributo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tributo\",reference:\"NM_TRIBUTO\"}," +
							"                        {label:\"Esfera Governamental\",reference:\"CL_TIPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para II 
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_II", NUMERICO, FILTER,
					"Tributo II",
					"{caption:\"Selecionando Tributo\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TributoDAO\",method: \"find\"," +
							" cdField: \"CD_TRIBUTO\", dsField: \"NM_TRIBUTO\"," +
							" filterFields: [[{label:\"Tributo\",reference:\"nm_tributo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tributo\",reference:\"NM_TRIBUTO\"}," +
							"                        {label:\"Esfera Governamental\",reference:\"CL_TIPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para PIS 
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_PIS", NUMERICO, FILTER,
					"Tributo PIS",
					"{caption:\"Selecionando Tributo\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TributoDAO\",method: \"find\"," +
							" cdField: \"CD_TRIBUTO\", dsField: \"NM_TRIBUTO\"," +
							" filterFields: [[{label:\"Tributo\",reference:\"nm_tributo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tributo\",reference:\"NM_TRIBUTO\"}," +
							"                        {label:\"Esfera Governamental\",reference:\"CL_TIPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para COFINS 
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_COFINS", NUMERICO, FILTER,
					"Tributo COFINS",
					"{caption:\"Selecionando Tributo\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TributoDAO\",method: \"find\"," +
							" cdField: \"CD_TRIBUTO\", dsField: \"NM_TRIBUTO\"," +
							" filterFields: [[{label:\"Tributo\",reference:\"nm_tributo\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Tributo\",reference:\"NM_TRIBUTO\"}," +
							"                        {label:\"Esfera Governamental\",reference:\"CL_TIPO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Arquivo Cacerts
			nmParametro = "NM_REPOSITORIO_XML"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Caminho dos Arquivos XML", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Contato
			nmParametro = "CD_CONTATO";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Contato",
					"{caption:\"Selecionando Contato da Empresa (SINTEGRA)\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.grl.PessoaDAO\",method:\"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Nome\",reference:\"nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome da Pessoa\",reference:\"nm_pessoa\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Contato
			nmParametro = "CD_CONTADOR";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Contador",
					"{caption:\"Selecionando Contato\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.grl.PessoaDAO\",method:\"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Nome\",reference:\"nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome da Pessoa\",reference:\"nm_pessoa\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Empresa de Contabilidade
			nmParametro = "CD_EMPRESA_CONTABILIDADE";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Empresa de Contabilidade (Opcional)",
					"{caption:\"Selecionando Contato\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.grl.PessoaDAO\",method:\"find\"," +
							" cdField: \"CD_PESSOA\", dsField: \"NM_PESSOA\"," +
							" filterFields: [[{label:\"Nome\",reference:\"nm_pessoa\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],"+
							" gridOptions: {columns:[{label:\"Nome da Pessoa\",reference:\"nm_pessoa\"}]}}",
							0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// CRC do contador
			nmParametro = "NR_CRC"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"CRC do Contador", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Permitir que nota possa ser criada com documento em conferencia
			nmParametro = "LG_PERMITIR_NOTA_DOC_CONFERENCIA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Permitir que nota possa ser criada com documento em conferï¿½ncia?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);
			// Cidade Exterior Padrão
			insertUpdate(new Parametro(0, 0, "CD_CIDADE_EXTERIOR", NUMERICO, FILTER,
					"Cidade - Exterior",
					"{caption:\"Selecionando a Cidade\", width:680, height:350, modal:true," +
							" className: \"com.tivic.manager.grl.CidadeServices\",method:\"find\"," +
							" cdField: \"CD_CIDADE\", dsField: \"NM_CIDADE\"," +
							" filterFields: [[{label:\"Cidade\", reference:\"NM_CIDADE\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:90}, " +
							"					{label:\"UF\", reference:\"SG_ESTADO\",datatype:_VARCHAR,comparator:_EQUAL,width:10}]],"+
							" gridOptions: {columns:[{label:\"Cidade\",reference:\"NM_CIDADE\"}, " +
							"						   {label:\"UF\",reference:\"SG_ESTADO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Utilizar as tributacoes padroes do sistema (ICMS 102)
			insertUpdate(new Parametro(0, 0, "LG_TRIBUTACAO", LOGICO, CHECK_BOX,
					"Usar Tributaï¿½ï¿½o Padrão?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Parametro para Tributo Aliquota ICMS - Vendas
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_ALIQUOTA_ICMS_VENDAS", NUMERICO, FILTER,
					"Tributo Aliquota ICMS - Vendas",
					"{caption:\"Selecionando Tributo Aliquota\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TributoAliquotaServices\",method: \"find\"," +
							" cdField: \"CD_TRIBUTO_ALIQUOTA\", dsField: \"CL_RESULTADO\"," +
							" filterFields: [[]],"+
							" gridOptions: {columns:[{label:\"Tributo Alï¿½quota\",reference:\"CL_RESULTADO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Parametro para Tributo Aliquota ICMS - Compras
			insertUpdate(new Parametro(0, 0/*cdEmpresa*/, "CD_TRIBUTO_ALIQUOTA_ICMS_COMPRAS", NUMERICO, FILTER,
					"Tributo Aliquota ICMS - Compras",
					"{caption:\"Selecionando Tributo Aliquota\",width:680,height:350,modal:true," +
							" className: \"com.tivic.manager.adm.TributoAliquotaServices\",method: \"find\"," +
							" cdField: \"CD_TRIBUTO_ALIQUOTA\", dsField: \"CL_RESULTADO\"," +
							" filterFields: [[]],"+
							" gridOptions: {columns:[{label:\"Tributo Alï¿½quota\",reference:\"CL_RESULTADO\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Configuração de Regime Tributario
			nmParametro	= "TP_REGIME_TRIBUTARIO";
			cdParametro = insertUpdate(new Parametro(0, 0/*cdEmpresa*/, nmParametro, NUMERICO, COMBO_BOX,
					"Tipo de Regime Tributï¿½rio", "", 0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			if (cdParametro > 0) {
				String[] tiposRegime = {"Simples Nacional",
						"Simples Nacional - excesso de sublimite da receita bruta",
				"Regime Normal"};
				for (int i=0; i<tiposRegime.length; i++) {
					ParametroOpcao opcao = new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, tiposRegime[i] /*vlApresentacao*/, Integer.toString(i+1) /*vlReal*/, 0 /*cdPessoa*/, 0 /*cdEmpresa*/);
					ParametroOpcaoDAO.insert(opcao, connect);
				}
			}

			// Arquivo Cacerts
			nmParametro = "NM_CAMINHO_RESULTADO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Caminho do arquivo de ECF para SPED", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Ativar validadores nos cadastros e liberacoes para o Sintegra e o SPED
			nmParametro = "LG_GERADOR_SPED_SINTEGRA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Cliente gerador de SPED e/ou SINTEGRA", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Tipo de Credito Padrão
			nmParametro = "CD_TIPO_CREDITO_DEFAULT";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Tipo de Crï¿½dito Padrão",
					"{caption:\"Selecionando Tipo de Crï¿½dito\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.fsc.TipoCreditoDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_CREDITO\", dsField: \"NM_TIPO_CREDITO\"," +
							" filterFields: [[{label:\"Nï¿½mero\",reference:\"nr_tipo_credito\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:30}," +
							"				 {label:\"Nome\",reference:\"nm_tipo_credito\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70}]],"+
							" gridOptions: {columns:[{label:\"Nï¿½mero\",reference:\"nr_tipo_credito\"}," +
							"						{label:\"Nome\",reference:\"nm_tipo_credito\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);
			// Tipo de Credito Padrão
			nmParametro = "CD_TIPO_CONTRIBUICAO_SOCIAL_DEFAULT";
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Tipo de Contribuição Social Padrão",
					"{caption:\"Selecionando Tipo de Contribuição Social\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.fsc.TipoContribuicaoSocialDAO\",method:\"find\"," +
							" cdField: \"CD_TIPO_CONTRIBUICAO_SOCIAL\", dsField: \"NM_TIPO_CONTRIBUICAO_SOCIAL\"," +
							" filterFields: [[{label:\"Nï¿½mero\",reference:\"nr_tipo_contribuicao_social\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:30}," +
							"				 {label:\"Nome\",reference:\"nm_tipo_contribuicao_social\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70}]],"+
							" gridOptions: {columns:[{label:\"Nï¿½mero\",reference:\"nr_tipo_contribuicao_social\"}," +
							"						{label:\"Nome\",reference:\"nm_tipo_contribuicao_social\"}]}}",
							0/*cdPessoa*/, TP_GERAL, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Gera numero da nota fiscal na autorização
			nmParametro = "LG_GERAR_NUMERO_NOTA_AUTORIZACAO";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Gerar o número da nota fiscal na autorização", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);

			// Exibir ou não o número do documento na nota fiscal
			nmParametro = "LG_EXIBIR_NUMERO_NOTA";
			insertUpdate(new Parametro(0, 0, nmParametro, LOGICO, CHECK_BOX,
					"Deseja exibir o número do documento na nota fiscal?", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_ADMINISTRADOR), connect);


			// Texto para justificativa de inutilizaï¿½ï¿½o de números de nota fiscal
			nmParametro = "TXT_JUSTIFICATIVA_INUTILIZACAO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, STRING, SINGLE_TEXT,
					"Justificativa para inutilizaï¿½ï¿½o de uma faixa de números de nota fiscal", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

			// Dia final para inutilizar numeração de nota fiscal 
			nmParametro = "DIA_FINAL_INUTILIZACAO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, SINGLE_TEXT,
					"Dia final para inutilizar números da nota fiscal", null,
					0/*cdPessoa*/, TP_EMPRESA, cdModulo, cdSistema, NIVEL_ACESSO_OPERADOR), connect);

		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/************************************************************************************************************
	 * MÓDULO SEG - Painel de Controle
	 ************************************************************************************************************/
	public static void initSeg() {
		Connection connect = Conexao.conectar();
		try	{

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo  = ? ");
			pstmt.setString(1, "seg");
			ResultSet rs = pstmt.executeQuery();
			boolean isNext = rs.next();
			int cdSistema = isNext ? rs.getInt("cd_sistema") : 0;
			int cdModulo  = isNext ? rs.getInt("cd_modulo") : 0;

			if( cdSistema > 0 && cdModulo > 0 )
				AcaoServices.initPermissoesSeg(cdSistema, cdModulo, connect);

			//SISTEMA JURISMANAGER
			String nmParametro = "CD_SISTEMA_JURISMANAGER"; 
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Sistema JurisManager",
					"{caption:\"Selecionando Sistema JurisManager\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.seg.SistemaDAO\",method:\"find\"," +
							" cdField: \"CD_SISTEMA\", dsField: \"NM_SISTEMA\"," +
							" filterFields: [[{label:\"Sistema\",reference:\"nm_sistema\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}]],"+
							" gridOptions: {columns:[{label:\"Sistema\",reference:\"nm_sistema\"}]}}",
							0/*cdPessoa*/, TP_GERAL, 0/*cdModulo*/, 0/*cdSistema*/, NIVEL_ACESSO_OPERADOR), connect);

			//SISTEMA FINANCEIRO
			nmParametro = "CD_SISTEMA_FINANCEIRO"; 
			insertUpdate(new Parametro(0, 0, nmParametro, NUMERICO, FILTER,
					"Sistema Financeiro",
					"{caption:\"Selecionando Sistema Financeiro\", width:680, height:350, modal:true, " +
							" className: \"com.tivic.manager.seg.SistemaDAO\",method:\"find\"," +
							" cdField: \"CD_SISTEMA\", dsField: \"NM_SISTEMA\"," +
							" filterFields: [[{label:\"Sistema\",reference:\"nm_sistema\",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}]],"+
							" gridOptions: {columns:[{label:\"Sistema\",reference:\"nm_sistema\"}]}}",
							0/*cdPessoa*/, TP_GERAL, 0/*cdModulo*/, 0/*cdSistema*/, NIVEL_ACESSO_OPERADOR), connect);

			//REALIZAR LOGIN COM HASH OU TEXTO PLANO
			insertUpdate(new Parametro(0, 0, "LG_LOGIN_HASH", LOGICO, CHECK_BOX,
					"Realizar Login com hash", null,
					0/*cdPessoa*/, TP_GERAL, 0, 0, NIVEL_ACESSO_ADMINISTRADOR), connect);


		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		finally	{
			Conexao.desconectar(connect);
		}

	}


	public static ResultSetMap getPrevisaoTempo() {
		Connection connection = null;
		try	{
			connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.cd_opcao " +
					"FROM grl_parametro A " +
					"LEFT OUTER JOIN grl_parametro_valor B ON (A.cd_parametro = B.cd_parametro AND " +
					"										   A.tp_apresentacao <> ?) " +
					"WHERE nm_parametro = \'PREVISAO_TEMPO\'");
			pstmt.setInt(1, LIST_BOX_MULTI_SELECTION);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next())	{
				int cdOpcao = rsm.getInt("cd_opcao");
				rsm = getOpcoesOfParametro(rsm.getInt("cd_parametro"), rsm.getInt("tp_apresentacao")==LIST_BOX_MULTI_SELECTION, 0, 0, connection);
				while(rsm.next())	{
					rsm.setValueToField("SELECTED", new Boolean(cdOpcao==rsm.getInt("cd_opcao")));
					rsm.setValueToField("URL", com.tivic.manager.util.HttpProxy.read(rsm.getString("VL_REAL")));
				}
			}
			else	{
				HashMap<String,Object> reg = new HashMap<String,Object>();
				reg.put("VL_APRESENTACAO", "Vitoria da Conquista");
				reg.put("VL_REAL", "http://g1.globo.com/Portal/G1V2/prevtempo/glb_g1v2_clima_previsao_cidade_content/0,,0-0-290,00.html");
				reg.put("SELECTED", new Boolean(true));
				reg.put("URL", com.tivic.manager.util.HttpProxy.read("http://g1.globo.com/Portal/G1V2/prevtempo/glb_g1v2_clima_previsao_cidade_content/0,,0-0-290,00.html"));
				rsm = new ResultSetMap();
				rsm.addRegister(reg);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int lgManutencaoParametros = 0;
			int cdEmpresa = 0;
			int cdPessoa = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("cd_empresa")) {
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cd_pessoa")) {
					cdPessoa = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgManutencaoParametros")) {
					lgManutencaoParametros = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
			}
			String sql = "SELECT A.*, B.nm_pessoa, C.id_modulo, C.nm_modulo " +
					"FROM grl_parametro A " +
					"LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
					"LEFT OUTER JOIN seg_modulo C ON (A.cd_sistema = C.cd_sistema " +
					"                             AND A.cd_modulo  = C.cd_modulo) " +
					"WHERE ((A.tp_parametro = " + TP_GERAL + ")" +
					(lgManutencaoParametros==1 || cdEmpresa > 0 ? " OR (A.tp_parametro = " + TP_EMPRESA + ") " : "") +
					(lgManutencaoParametros==1 || cdPessoa > 0 ? " OR (A.tp_parametro = " + TP_PESSOA + ")" : "") + ")";
			ResultSetMap rsm = Search.find(sql, "ORDER BY nm_modulo, nm_rotulo", criterios, connection, false);
			
			while (rsm != null && rsm.next()) {
				HashMap<String,Object> register = rsm.getRegister();
				connection = connection==null || connection.isClosed() ? Conexao.conectar() : connection;
				register.put("OPCOES", getOpcoesOfParametro(rsm.getInt("cd_parametro"), rsm.getInt("tp_apresentacao")==LIST_BOX_MULTI_SELECTION, 0, 0, connection));
				connection = connection==null || connection.isClosed() ? Conexao.conectar() : connection;
				register.put("VALORES", getValoresOfParametro(rsm.getInt("cd_parametro"), cdEmpresa, cdPessoa, connection));
			}
			if (rsm != null)
				rsm.beforeFirst();
			return rsm;
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

	public static int transferParamsMultiText() {
		return transferParamsMultiText(null);
	}

	public static int insertUpdate(Parametro parametro, Connection connect) {
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = ?");
			pstmt.setString(1, parametro.getNmParametro());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				System.out.println("UPDATE");
				parametro.setCdParametro(rs.getInt("cd_parametro"));
				ParametroDAO.update(parametro, connect);
			}
			else {
				System.out.println("INSERT");
				return ParametroDAO.insert(parametro, connect);
			}

			return 0; // Indica pra quem chamou que não foi feita uma inserção
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}

	}
	
	public static int insertUpdateMob(Parametro parametro, int cdGrupoParametro, Connection connect) {
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = ?");
			pstmt.setString(1, parametro.getNmParametro());
			ResultSet rs = pstmt.executeQuery();
			
			int cdParametro = -1;
			if(rs.next())	{
				System.out.printf("UPDATE -> %s \n", rs.getString("nm_parametro"));
				parametro.setCdParametro(rs.getInt("cd_parametro"));
				ParametroDAO.update(parametro, connect);
				cdParametro = parametro.getCdParametro();
			} else {
				System.out.printf("INSERT -> %s \n", parametro.getNmParametro());
				cdParametro = ParametroDAO.insert(parametro, connect);
			}
			
			ParametroGrupoDAO _parametroGrupoDAO = new ParametroGrupoDAO();
			ParametroGrupo parametroGrupo = _parametroGrupoDAO.get(cdParametro, connect);
			
			if(parametroGrupo == null) {
				ParametroGrupo _nPg = new ParametroGrupo(cdParametro, cdGrupoParametro);
				_parametroGrupoDAO.insert(_nPg, connect);
			}
			
			return cdParametro;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
	}
	
	public static int insertUpdateGrupo(Parametro parametro, int cdGrupoParametro, Connection connect) {
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_parametro WHERE nm_parametro = ?");
			pstmt.setString(1, parametro.getNmParametro());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				System.out.println("UPDATE");
				parametro.setCdParametro(rs.getInt("cd_parametro"));
				ParametroDAO.update(parametro, connect);
			}
			else {
				System.out.println("INSERT");
				return ParametroDAO.insert(parametro, connect);
			}

			return 0; // Indica pra quem chamou que n�o foi feita uma inser��o
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
	}

	public static int transferParamsMultiText(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int registros = 0;
			ResultSet rs = connection.prepareStatement("SELECT A.cd_parametro, A.cd_valor " +
					"FROM grl_parametro_valor A, grl_parametro B " +
					"WHERE A.cd_parametro    = B.cd_parametro " +
					"  AND B.tp_apresentacao = "+MULTI_TEXT).executeQuery();
			while (rs.next()) {
				ParametroValor valor = ParametroValorDAO.get(rs.getInt("cd_parametro"), rs.getInt("cd_valor"), connection);
				valor.setBlbValor(valor.getVlInicial()==null ? null : valor.getVlInicial().getBytes());
				if (ParametroValorDAO.update(valor, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
				}
				registros++;
			}

			if (isConnectionNull)
				connection.commit();

			return registros;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static String getDateTime() {
		Connection connect = Conexao.conectar();
		try {
			ResultSet rs = connect.prepareStatement("SELECT current_timestamp").executeQuery();
			if(rs.next())
				return com.tivic.manager.util.Util.formatDate(com.tivic.manager.util.Util.convTimestampToCalendar(rs.getTimestamp(1)), "dd/MM/yyyy HH:mm:ss");

			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @category SICOE
	 * @return
	 */
	public static ResultSetMap getAll() {
		return getAll(null);
	}
	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa AS nm_empresa FROM GRL_PARAMETRO A " +
											"	LEFT OUTER JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) " +
											"	LEFT OUTER JOIN grl_pessoa C ON (A.cd_empresa = C.cd_pessoa) ");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result atualizarParametros(int cdContaGridGrid, int cdCarteiraGrid, int cdFormaPagamentoGrid, String nrAgenciaGrid,
			int lgParametrosObrigatoriosGrid, int lgAutorizacaoBloqueadoGrid, int lgSubstituicaoValores, int lgBloquearClienteGrid,
			int lgBloquearEmitenteGrid, int cdCategoriaChequeAVistaGrid, int cdCategoriaChequeAPrazo, int cdCategoriaDescontoChequeAVistaGrid,
			int cdCategoriaJurosAtrasoGrid, int cdCategoriaMultaDevolucao, int cdCategoriaMultaProrrogacao, int cdCategoriaDescontoCompensacaoGrid,
			int cdCategoriaDescontoCompensacaoBalancoGrid, int qtIdadeMinima, int qtPrazoMaximo, int qtPrazoMinimo,
			float vlTaxaDevolucao, float prTaxaJuros, float prTaxaMinima, float prTaxaPadrao,
			float prTaxaProrrogacao, float vlGanhoMinimo, float vlLimiteFactoring, float vlLimiteFactoringEmissor,
			float vlLimiteFactoringUnitario) {
		return atualizarParametros(cdContaGridGrid, cdCarteiraGrid, cdFormaPagamentoGrid, nrAgenciaGrid,
				lgParametrosObrigatoriosGrid, lgAutorizacaoBloqueadoGrid, lgSubstituicaoValores, lgBloquearClienteGrid,
				lgBloquearEmitenteGrid, cdCategoriaChequeAVistaGrid, cdCategoriaChequeAPrazo, cdCategoriaDescontoChequeAVistaGrid,
				cdCategoriaJurosAtrasoGrid, cdCategoriaMultaDevolucao, cdCategoriaMultaProrrogacao, cdCategoriaDescontoCompensacaoGrid,
				cdCategoriaDescontoCompensacaoBalancoGrid, qtIdadeMinima, qtPrazoMaximo, qtPrazoMinimo,
				vlTaxaDevolucao, prTaxaJuros, prTaxaMinima, prTaxaPadrao,
				prTaxaProrrogacao, vlGanhoMinimo, vlLimiteFactoring, vlLimiteFactoringEmissor,
				vlLimiteFactoringUnitario, null);
	}

	public static Result atualizarParametros(int cdContaGridGrid, int cdCarteiraGrid, int cdFormaPagamentoGrid, String nrAgenciaGrid,
			int lgParametrosObrigatoriosGrid, int lgAutorizacaoBloqueadoGrid, int lgSubstituicaoValores, int lgBloquearClienteGrid,
			int lgBloquearEmitenteGrid, int cdCategoriaChequeAVistaGrid, int cdCategoriaChequeAPrazo, int cdCategoriaDescontoChequeAVistaGrid,
			int cdCategoriaJurosAtrasoGrid, int cdCategoriaMultaDevolucao, int cdCategoriaMultaProrrogacao, int cdCategoriaDescontoCompensacaoGrid,
			int cdCategoriaDescontoCompensacaoBalancoGrid, int qtIdadeMinima, int qtPrazoMaximo, int qtPrazoMinimo,
			float vlTaxaDevolucao, float prTaxaJuros, float prTaxaMinima, float prTaxaPadrao,
			float prTaxaProrrogacao, float vlGanhoMinimo, float vlLimiteFactoring, float vlLimiteFactoringEmissor,
			float vlLimiteFactoringUnitario, Connection connection) {
		boolean isConnectionNull = true;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			updateValueOfParametro(ParametroServices.getByName("CD_CONTA").getCdParametro(), String.valueOf(cdContaGridGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_CARTEIRA").getCdParametro(), String.valueOf(cdCarteiraGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_FORMA_PAGAMENTO_COMPENSACAO").getCdParametro(), String.valueOf(cdFormaPagamentoGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("NR_AGENCIA_FACTORING").getCdParametro(), nrAgenciaGrid, connection);
			updateValueOfParametro(ParametroServices.getByName("LG_PARAMETROS_OBRIGATORIOS").getCdParametro(), String.valueOf(lgParametrosObrigatoriosGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("LG_AUTORIZACAO_CHEQUE_PESSOA_BLOQUEADA").getCdParametro(), String.valueOf(lgAutorizacaoBloqueadoGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("LG_PERMITIR_SUBSTITUICAO").getCdParametro(), String.valueOf(lgSubstituicaoValores), connection);
			updateValueOfParametro(ParametroServices.getByName("LG_BLOQUEAR_CLIENTE_DEVOLUCAO").getCdParametro(), String.valueOf(lgBloquearClienteGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("LG_BLOQUEAR_EMITENTE_DEVOLUCAO").getCdParametro(), String.valueOf(lgBloquearEmitenteGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_CATEGORIA_ECONOMICA_CHEQUE_VISTA").getCdParametro(), String.valueOf(cdCategoriaChequeAVistaGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_CATEGORIA_ECONOMICA_CHEQUE_A_PRAZO").getCdParametro(), String.valueOf(cdCategoriaChequeAPrazo), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_CATEGORIA_ECONOMICA_DESCONTO_CHEQUE_VISTA").getCdParametro(), String.valueOf(cdCategoriaDescontoChequeAVistaGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_CATEGORIA_ECONOMICA_JUROS_ATRASO").getCdParametro(), String.valueOf(cdCategoriaJurosAtrasoGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_CATEGORIA_ECONOMICA_MULTA_DEVOLUCAO").getCdParametro(), String.valueOf(cdCategoriaMultaDevolucao), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_CATEGORIA_ECONOMICA_MULTA_PRORROGACAO").getCdParametro(), String.valueOf(cdCategoriaMultaProrrogacao), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_CATEGORIA_ECONOMICA_DESCONTO_COMPENSACAO").getCdParametro(), String.valueOf(cdCategoriaDescontoCompensacaoGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("CD_CATEGORIA_ECONOMICA_DESCONTO_BALANCO").getCdParametro(), String.valueOf(cdCategoriaDescontoCompensacaoBalancoGrid), connection);
			updateValueOfParametro(ParametroServices.getByName("QT_IDADE_MINIMA").getCdParametro(), String.valueOf(qtIdadeMinima), connection);
			updateValueOfParametro(ParametroServices.getByName("QT_PRAZO_MAXIMO").getCdParametro(), String.valueOf(qtPrazoMaximo), connection);
			updateValueOfParametro(ParametroServices.getByName("QT_PRAZO_MINIMO").getCdParametro(), String.valueOf(qtPrazoMinimo), connection);
			updateValueOfParametro(ParametroServices.getByName("VL_TAXA_DEVOLUCAO").getCdParametro(), String.valueOf(vlTaxaDevolucao), connection);
			updateValueOfParametro(ParametroServices.getByName("VL_TAXA_JUROS").getCdParametro(), String.valueOf(prTaxaJuros), connection);
			updateValueOfParametro(ParametroServices.getByName("VL_TAXA_MINIMA").getCdParametro(), String.valueOf(prTaxaMinima), connection);
			updateValueOfParametro(ParametroServices.getByName("VL_TAXA_PADRAO").getCdParametro(), String.valueOf(prTaxaPadrao), connection);
			updateValueOfParametro(ParametroServices.getByName("VL_TAXA_PRORROGACAO").getCdParametro(), String.valueOf(prTaxaProrrogacao), connection);
			updateValueOfParametro(ParametroServices.getByName("VL_GANHO_MINIMO").getCdParametro(), String.valueOf(vlGanhoMinimo), connection);
			updateValueOfParametro(ParametroServices.getByName("VL_LIMITE_FACTORING").getCdParametro(), String.valueOf(vlLimiteFactoring), connection);
			updateValueOfParametro(ParametroServices.getByName("VL_LIMITE_FACTORING_EMISSOR").getCdParametro(), String.valueOf(vlLimiteFactoringEmissor), connection);
			updateValueOfParametro(ParametroServices.getByName("VL_LIMITE_FACTORING_UNITARIO").getCdParametro(), String.valueOf(vlLimiteFactoringUnitario), connection);

			if(isConnectionNull)
				connection.commit();

			return new Result(1, "Atualizado com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar atualizar!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllOcorrenciasParametrosEdf() {
		return getAllOcorrenciasParametrosEdf(null);
	}

	public static ResultSetMap getAllOcorrenciasParametrosEdf(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario, D.vl_apresentacao AS nm_opcao_anterior, E.vl_apresentacao AS nm_opcao_posterior FROM acd_ocorrencia_parametro A"
					+ "							JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							LEFT OUTER JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "							LEFT OUTER JOIN grl_parametro_opcao D ON (A.cd_parametro = D.cd_parametro AND A.cd_opcao_anterior = D.cd_opcao) "
					+ "							LEFT OUTER JOIN grl_parametro_opcao E ON (A.cd_parametro = E.cd_parametro AND A.cd_opcao_anterior = E.cd_opcao) "
					+ "						  WHERE B.cd_tipo_ocorrencia IN ("+InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERAR_PARAMETRO_CONTROLE_EDF+")"
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static List<HashMap<String, Object>> getParamsBaseAntiga(String[] ids) {
		return getParamsBaseAntiga(ids, null);
	}

	public static List<HashMap<String, Object>> getParamsBaseAntiga(String[] ids, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			pstmt = connect.prepareStatement("SELECT * FROM PARAMETRO");
			ResultSet rs = pstmt.executeQuery();
			List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

			@SuppressWarnings("unchecked")
			List<String> params = Arrays.asList(ids);
			
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			HashMap<String, Object> row = new HashMap<String, Object>(columns);
			
			while (rs.next()){
				for(int i=1; i<=columns; ++i){
					if(params.contains(md.getColumnName(i).toUpperCase())) {
						row.put(md.getColumnName(i).toUpperCase(), rs.getObject(i));
					}
			    }
			}
			
			result.add(row);

			return result;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getParamsBaseAntiga: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getParamsBaseAntiga: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ParametroValor getParamsBaseAntigaByNome(String nmParametro) {
		try {
			String[] idsArray = new String[1];
			idsArray[0] = nmParametro;

			List<HashMap<String, Object>> list = ParametroServices.getParamsBaseAntiga(idsArray);
			return new ParametroValor(-1, -1, -1, -1, -1, null, String.valueOf(list.get(0).get(nmParametro)), "");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParametroServices.getParamsBaseAntigaByNome: " + e);
			return null;
		}
	}
	
	public static List<String> getByModulo(String idModulo, Connection connection) { 
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			List<String> ids = new ArrayList<String>();
			
			PreparedStatement ps = connection
					.prepareStatement("SELECT A.nm_parametro "
							+ " FROM grl_parametro A "
							+ " JOIN seg_modulo B ON (A.cd_modulo = B.cd_modulo)"
							+ " WHERE B.id_modulo = ?");
			ps.setString(1, idModulo);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				ids.add(rs.getString("nm_parametro"));
			}
						
			return ids;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static List<Parametro> getAllMob() {
		return getAllMob(null);
	}
	
	public static List<Parametro> getAllMob(Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_PARAMETRO WHERE CD_MODULO = 27");
						
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			List<Parametro> params = new ResultSetMapper<Parametro>(rsm, Parametro.class).toList();
			
			for(Parametro param : params) {
				
				List<ParametroValor> valores = new ResultSetMapper<ParametroValor>(getValoresOfParametro(param.getCdParametro()), ParametroValor.class).toList();
				
				if(valores.size() > 0) {
					ParametroValor[] valor = new ParametroValor[valores.size()];
					
					for(int i = 0; i < valores.size() ; i++) {
						valor[i] = valores.get(i);
					}
				
					param.setValores(valor);
				}
			}
			
			return params;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static List<String> getImages(String nmParametro) {
		return getImages(nmParametro, null);
	}
	
	public static List<String> getImages(String nmParametro, Connection connect) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); //Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			if (lgBaseAntiga)
			{
				PreparedStatement pstmt = connect.prepareStatement("SELECT "+ nmParametro + " FROM PARAMETRO");
				ResultSet rs = pstmt.executeQuery();
				List<String> imgs = new ArrayList<String>();
				
				if(rs.next())
					imgs.add(convertByteArray(rs.getBytes(nmParametro)));
			
				return imgs;
			}
			else
			{
			
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.BLB_VALOR FROM GRL_PARAMETRO_VALOR A INNER JOIN GRL_PARAMETRO B" + 
																	" ON (A.CD_PARAMETRO = B.CD_PARAMETRO) WHERE B.NM_PARAMETRO = ?");
				pstmt.setString(1, nmParametro);
				ResultSet rs = pstmt.executeQuery();
				List<ParametroValor> valores = new ResultSetMapper<ParametroValor>(new ResultSetMap(rs), ParametroValor.class).toList();
			
				List<String> imgs = new ArrayList<String>();
			
				for(ParametroValor valor : valores) {
					imgs.add(convertByteArray(valor.getBlbValor()));
				}
			
				return imgs;
			}
			
		}catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static byte[] convertBase64(String base64) {
		//base64 = base64.replace("data:image/png;base64,", "");
		byte[] conv = base64.getBytes();
		
		return conv;
	}
	
	private static String convertByteArray(byte[] img) {
		if(img != null) {
			String data = new String(img);
			if(data.contains("data:image/png;base64,"))
				return data;
			
			String conv = "data:image/png;base64," + new String(img);
			return conv;
		}
		
		return null;
	}
	
	public static String RecImg(String nome) throws ValidacaoException {
		return  RecImg(nome, null);
	}
	
	public static String RecImg(String nome, Connection connect) throws ValidacaoException {
		boolean isConnectionNull = connect==null;	
		if (isConnectionNull) 
			connect = Conexao.conectar();
		
		try {
			List<String> Imagens = ParametroServices.getImages(nome, connect);
			
			String imgC = "";
			
			for(String img : Imagens) {
				if(img != "" && img != null) {
					imgC = img;
				}
			}
			
			return imgC.replace("data:image/png;base64,", "");
		}catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}