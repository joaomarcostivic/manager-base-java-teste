package com.tivic.manager.gpn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.manager.grl.Formulario;
import com.tivic.manager.grl.FormularioAtributoOpcaoServices;
import com.tivic.manager.grl.FormularioAtributoServices;
import com.tivic.manager.grl.FormularioDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ParametroValor;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TipoDocumentoServices {
	
	public static final int TP_INC_SEM_PREFIXO 		= 0;
	public static final int TP_INC_COM_PREXIFO 		= 1;
	public static final int TP_MASCARA         		= 2;
	public static final int TP_INC_COM_ANO     		= 3;
	public static final int TP_INC_SEQ_GERAL   		= 4;
	public static final int TP_INC_COM_PREFIXO_ANO 	= 5;
	public static final int TP_EXPRESSAO 			= 6;
	
	public static final String[] tiposNumeracao = {"Incremental (sem prefixo)", "Incremental (com prefixo)", "Máscara", "Incremental (por Ano)", "Sequencial Geral (por Ano)", "Incremental com prefixo e ano"};

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;

	public static final String[] situacoes = {"Inativo", "Ativo"};

	public static Result save(TipoDocumento tipoDocumento){
		return save(tipoDocumento, null, null);
	}

	public static Result save(TipoDocumento tipoDocumento, AuthData authData){
		return save(tipoDocumento, authData, null);
	}

	public static Result save(TipoDocumento tipoDocumento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoDocumento==null)
				return new Result(-1, "Erro ao salvar. TipoDocumento é nulo");

			int retorno;
			if(tipoDocumento.getCdTipoDocumento()==0){
				retorno = TipoDocumentoDAO.insert(tipoDocumento, connect);
				tipoDocumento.setCdTipoDocumento(retorno);
			}
			else {
				retorno = TipoDocumentoDAO.update(tipoDocumento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPODOCUMENTO", tipoDocumento);
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
	public static Result remove(int cdTipoDocumento){
		return remove(cdTipoDocumento, false, null);
	}
	public static Result remove(int cdTipoDocumento, boolean cascade){
		return remove(cdTipoDocumento, cascade, null);
	}
	public static Result remove(int cdTipoDocumento, boolean cascade, Connection connect){
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
			retorno = TipoDocumentoDAO.delete(cdTipoDocumento, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluí-lo!");
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
			pstmt = connect.prepareStatement("SELECT * FROM gpn_tipo_documento ORDER BY nm_tipo_documento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM gpn_tipo_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static final String getNumeracaoProtocolo(int cdTipoDocumento, Connection connect) {
		return getNextNumeracao(cdTipoDocumento, 0, true, connect);
	}

	public static String getNextNumeracao(int cdTipoDocumento) {
		return getNextNumeracao(cdTipoDocumento, 0, false, null);
	}

	public static String getNextNumeracao(int cdTipoDocumento, boolean save) {
		return getNextNumeracao(cdTipoDocumento, 0, save, null);
	}

	public static String getNextNumeracao(int cdTipoDocumento, int cdEmpresa, boolean save, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			String nrProcesso = "";
			TipoDocumento tipoDocumento = TipoDocumentoDAO.get(cdTipoDocumento, connect);

			if(tipoDocumento.getLgNumeracaoSuperior() != 1 && tipoDocumento.getCdTipoDocumentoSuperior()>0) {
				tipoDocumento = TipoDocumentoDAO.get(tipoDocumento.getCdTipoDocumentoSuperior(), connect);
			} else {
				tipoDocumento = TipoDocumentoDAO.get(tipoDocumento.getCdTipoDocumento(), connect);
			}
			
			tipoDocumento.setNrUltimaNumeracao(tipoDocumento.getNrUltimaNumeracao() + 1);
			if (TipoDocumentoDAO.update(tipoDocumento, connect) <= 0)
				return null;
			switch(tipoDocumento.getTpNumeracao()) {
				case TP_INC_SEM_PREFIXO:
					nrProcesso = Integer.toString(tipoDocumento.getNrUltimaNumeracao());
					break;
				case TP_INC_COM_PREXIFO:
					nrProcesso = tipoDocumento.getIdPrefixoNumeracao() + Integer.toString(tipoDocumento.getNrUltimaNumeracao());
					break;
				case TP_MASCARA:
					DecimalFormat df = new DecimalFormat(tipoDocumento.getDsMascaraNumeracao());
					nrProcesso       = df.format(tipoDocumento.getNrUltimaNumeracao());
					break;
				case TP_INC_COM_ANO:
					df = new DecimalFormat("0000");
					nrProcesso = df.format(tipoDocumento.getNrUltimaNumeracao())+"/"+new GregorianCalendar().get(GregorianCalendar.YEAR);
					break;
				//Alteração	
				case TP_INC_SEQ_GERAL:
					df = new DecimalFormat("0000");
					int numeracao   = ParametroServices.getValorOfParametroAsInteger("NR_SEQUENCIAL_GERAL", 0, cdEmpresa, connect);
					numeracao++;
					int cdParametro = ParametroServices.getByName("NR_SEQUENCIAL_GERAL").getCdParametro();
					nrProcesso = df.format( numeracao )+"/"+new GregorianCalendar().get(GregorianCalendar.YEAR);
					ArrayList<ParametroValor> values = new ArrayList<ParametroValor>();
					values.add(new ParametroValor(cdParametro,0,0,cdEmpresa,0, null, String.valueOf(numeracao), null));
					ParametroServices.setValoresOfParametro(cdParametro, cdEmpresa, 0, values, connect);
					break;
				case TP_INC_COM_PREFIXO_ANO:
					nrProcesso = tipoDocumento.getIdPrefixoNumeracao() + Integer.toString(tipoDocumento.getNrUltimaNumeracao())+"/"+new GregorianCalendar().get(GregorianCalendar.YEAR);
					break;
				case TP_EXPRESSAO:					
					GregorianCalendar today = new GregorianCalendar();
					StringBuilder builder = new StringBuilder();
					String[] parts = tipoDocumento.getDsMascaraNumeracao().split("\\+");
					System.out.println(parts);
					for (int i = 0; i < parts.length; i++) {
						String part = parts[i];
						
						if(part.equals("\\PF")) {
							builder.append(tipoDocumento.getIdPrefixoNumeracao());
						} else if(part.equals("\\YY")) {
							builder.append(Util.formatDate(today, "YY"));
						} else if(part.equals("\\YYYY")) {
							builder.append(Util.formatDate(today, "YYYY"));							
						} else if(part.contains("\\MM")) {
							builder.append(Util.formatDate(today, "MM"));
						} else if(part.contains("\\DD")) {
							builder.append(Util.formatDate(today, "dd"));							
						} else if(part.contains("\\NR")) {
							int num = tipoDocumento.getNrUltimaNumeracao();
							int length = Integer.parseInt(part.replace("\\NR{", "").replace("}", ""));
							builder.append(Util.fillNum(num, length));							
						} else {
							builder.append(part);
						}
					}
					
					nrProcesso = builder.length()==0 ? Integer.toString(tipoDocumento.getNrUltimaNumeracao()) : builder.toString();
					
					break;
			}

			return nrProcesso;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que busca o formulário dinâmico correspondente
	 * a um tipo de documento
	 * 
	 * @param cdTipoDocumento tipo de documento
	 * @return ResultSetMap com o formulário dinâmico
	 */
	public static Result getFormulario(int cdTipoDocumento) {
		return getFormulario(cdTipoDocumento, null);
	}
	
	public static Result getFormulario(int cdTipoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			TipoDocumento tipoDocumento = TipoDocumentoDAO.get(cdTipoDocumento, connect);
			if(tipoDocumento==null) {
				return new Result(-1, "Erro ao buscar tipo de documento.");
			}
			
			if(tipoDocumento.getCdFormulario()<=0) {
				return new Result(-2, "O tipo de documento selecionado não possui formulário dinâmico.");
			}			
			int cdFormulario = tipoDocumento.getCdFormulario();
			
			Formulario formulario = FormularioDAO.get(cdFormulario, connect);
			
			/*
			 * Busca dos atributos
			 */
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_FORMULARIO", Integer.toString(cdFormulario), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmFormularioAtributo = FormularioAtributoServices.find(criterios, connect);
			
			/*
			 * Busca as opcoes dos atributos, caso existam
			 */
			while(rsmFormularioAtributo.next()) {
				if(rsmFormularioAtributo.getInt("TP_DADO") == 6) { //OPCOES
					int cdFormularioAtributo = rsmFormularioAtributo.getInt("cd_formulario_atributo");
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("CD_FORMULARIO_ATRIBUTO", Integer.toString(cdFormularioAtributo), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmFormularioAtributoOpcao = FormularioAtributoOpcaoServices.find(criterios, connect);
					
					rsmFormularioAtributo.setValueToField("RSM_OPCOES", rsmFormularioAtributoOpcao);
				}
			}
			
			Result r = new Result(1, "");
			
			r.addObject("FORMULARIO", formulario);
			r.addObject("RSM_ATRIBUTOS", rsmFormularioAtributo);
			
			return r;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca os tipos de documento que possuem formulários dinâmicos
	 * 
	 * @return ResultSetMap
	 */
	public static ResultSetMap getTipoDocumentoFormulario() {
		return getTipoDocumentoFormulario(null);
	}

	public static ResultSetMap getTipoDocumentoFormulario(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					" SELECT A.*, B.nm_formulario, B.id_formulario " +
					" FROM gpn_tipo_documento A " +
					" LEFT JOIN grl_formulario B ON (A.cd_formulario = B.cd_formulario) " +
					" WHERE A.cd_formulario IS NOT NULL" +
					" ORDER BY nm_tipo_documento ");

			return (new ResultSetMap(pstmt.executeQuery()));
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	/**
	 * Busca os atributos de um formulario a partir do tipo de documento
	 * 
	 * @param cdTipoDocumento Código do Tipo do Documento
	 * @return ResultSetMap atributos do formulário
	 */
	public static ResultSetMap getAllAtributos(int cdTipoDocumento) {
		return getAllAtributos(cdTipoDocumento, null);
	}

	public static ResultSetMap getAllAtributos(int cdTipoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT B.* FROM gpn_tipo_documento A, grl_formulario_atributo B " +
															   "WHERE A.cd_tipo_documento = " +cdTipoDocumento+
															   "  AND A.cd_formulario     = B.cd_formulario " +
															   "ORDER BY nr_ordem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}