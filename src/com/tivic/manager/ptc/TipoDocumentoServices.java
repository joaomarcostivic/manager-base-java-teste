package com.tivic.manager.ptc;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Formulario;
import com.tivic.manager.grl.FormularioAtributo;
import com.tivic.manager.grl.FormularioAtributoOpcaoServices;
import com.tivic.manager.grl.FormularioAtributoServices;
import com.tivic.manager.grl.FormularioDAO;
import com.tivic.manager.grl.FormularioDTO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ParametroValor;
import com.tivic.manager.util.EelUtils;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.UtilServices;

public class TipoDocumentoServices {

	public static final int TP_INC_SEM_PREFIXO = 0;
	public static final int TP_INC_COM_PREXIFO = 1;
	public static final int TP_MASCARA         = 2;
	public static final int TP_INC_COM_ANO     = 3;
	public static final int TP_INC_SEQ_GERAL   = 4;
	
	public static final String[] tiposNumeracao = {"Incremental (sem prefixo)", "Incremental (com prefixo)", "Máscara", "Incremental (por Ano)", "Sequencial Geral (por Ano)"};

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;

	public static final String[] situacoes = {"Inativo", "Ativo"};

	
	public static Result save(TipoDocumento tipoDocumento){
		return save(tipoDocumento, null);
	}
	
	public static Result save(TipoDocumento tipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoDocumento==null)
				return new Result(-1, "Erro ao salvar. Tipo de documento é nulo");
			
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
				return new Result(-2, "Este tipo de documento está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de documento excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de documento!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
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
			LogUtils.debug("cdTipoDocumento: "+cdTipoDocumento);
			LogUtils.debug("tipoDocumento: "+tipoDocumento);
			
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


	public TipoDocumento insert(TipoDocumento tipoDocumento) {
		return insert(tipoDocumento, null, null);
	}

	public TipoDocumento insert(TipoDocumento tipoDocumento, Formulario formulario) {
		return insert(tipoDocumento, formulario, null);
	}

	public TipoDocumento insert(TipoDocumento tipoDocumento, boolean hasFormulario) {
		Formulario formulario = !hasFormulario ? null : new Formulario(tipoDocumento.getCdFormulario() /*cdFormulario*/,
				tipoDocumento.getNmTipoDocumento() + " - Campos personalidados" /*nmFormulario*/,
				"" /*idFormulario*/, null, 0, "", "", null, null);
		return insert(tipoDocumento, formulario, null);
	}

	public TipoDocumento insert(TipoDocumento tipoDocumento, Formulario formulario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int cdFormulario = 0;
			if (formulario != null) {
				cdFormulario = FormularioDAO.insert(formulario, connect);
				if (cdFormulario <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
				tipoDocumento.setCdFormulario(cdFormulario);
			}

			int cdTipoDocumento = TipoDocumentoDAO.insert(tipoDocumento, connect);
			if (cdTipoDocumento <= 0) {
				if (isConnectionNull)
					connect.rollback();
				return null;
			}

			if (isConnectionNull)
				connect.commit();

			return tipoDocumento;
		}
		catch (SQLException e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public TipoDocumento update(TipoDocumento tipoDocumento) {
		return update(tipoDocumento, null, null);
	}

	public TipoDocumento update(TipoDocumento tipoDocumento, Formulario formulario) {
		return update(tipoDocumento, formulario, null);
	}

	public TipoDocumento update(TipoDocumento tipoDocumento, boolean hasFormulario) {
		Formulario formulario = !hasFormulario ? null : new Formulario(tipoDocumento.getCdFormulario() /*cdFormulario*/,
				tipoDocumento.getNmTipoDocumento() + " - Campos personalidados" /*nmFormulario*/,
				"" /*idFormulario*/, null, 0, "", "", null, null);
		return update(tipoDocumento, formulario, null);
	}

	public TipoDocumento update(TipoDocumento tipoDocumento, boolean hasFormulario, Connection connect) {
		Formulario formulario = !hasFormulario ? null : new Formulario(tipoDocumento.getCdFormulario() /*cdFormulario*/,
				tipoDocumento.getNmTipoDocumento() + " - Campos personalidados" /*nmFormulario*/,
				"" /*idFormulario*/, null, 0, "", "", null, null);
		return update(tipoDocumento, formulario, connect);
	}

	public TipoDocumento update(TipoDocumento tipoDocumento, Formulario formulario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			if (formulario != null) {
				if (tipoDocumento.getCdFormulario()==0) {
					int cdFormulario = FormularioDAO.insert(formulario, connect);
					if (cdFormulario <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
					tipoDocumento.setCdFormulario(cdFormulario);
				}
				else {
					formulario.setCdFormulario(tipoDocumento.getCdFormulario());
					if (FormularioDAO.update(formulario, connect) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
				}
			}

			int cdTipoDocumento = TipoDocumentoDAO.update(tipoDocumento, connect);
			if (cdTipoDocumento <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}

			if (isConnectionNull)
				connect.commit();

			return tipoDocumento;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement(
					" SELECT A.*, B.nm_formulario, B.id_formulario, C.nm_tipo_documento as nm_tipo_documento_superior, " +
					" D.nm_setor, D.sg_setor, E.nm_razao_social"+
					" FROM ptc_tipo_documento A1 " +
					" JOIN gpn_tipo_documento A ON (A.cd_tipo_documento=A1.cd_tipo_documento)" +
					" LEFT JOIN grl_formulario B ON (A.cd_formulario = B.cd_formulario) " +
					" LEFT JOIN gpn_tipo_documento C ON (A.cd_tipo_documento_superior = C.cd_tipo_documento) " +
					" LEFT OUTER JOIN grl_setor D ON (A.cd_setor = D.cd_setor)"+
					" LEFT OUTER JOIN grl_pessoa_juridica E ON (A.cd_empresa = E.cd_pessoa)"+
					" WHERE A.cd_formulario = 100 ORDER BY nm_tipo_documento ");

			if (isConnectionNull) {
				connect.commit();
			}

			return (new ResultSetMap(pstmt.executeQuery()));
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findAllByTroca() {
		ArrayList<Object> tiposDocumentos = ParametroServices.getValoresOfParametroAsArrayList("CDS_TIPO_DOCUMENTO_TROCA_TAXI");
		ArrayList<CharSequence> charSequence = new ArrayList<>();
		charSequence.add("[");
		charSequence.add("]");
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_TIPO_DOCUMENTO", "" + UtilServices.replaceSequence(tiposDocumentos.toString(), charSequence, ""), ItemComparator.IN, Types.INTEGER));
		
		return find(criterios);
	}
	
	/**
	 * Método para pegar todos os documento de um determinado documento superior
	 * @param cdDocumentoSuperior
	 * @return documento de um documento superior
	 */
	public static ResultSetMap getAllByDocumentoSuperior(int cdDocumentoSuperior) {
		return getAllByDocumentoSuperior(cdDocumentoSuperior, null);
	}

	public static ResultSetMap getAllByDocumentoSuperior(int cdDocumentoSuperior, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement(
					" SELECT A.*" +
					" FROM ptc_tipo_documento A1" +
					" LEFT OUTER JOIN gpn_tipo_documento A on (A1.cd_tipo_documento=A.cd_tipo_documento)"	+	
					" WHERE A.cd_tipo_documento_superior = " + cdDocumentoSuperior);
			
			if (isConnectionNull) {
				connect.commit();
			}
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			return (rsm);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_tipo_documento as nm_tipo_documento_superior,"
				+ " C.nm_razao_social, D.nm_setor, D.sg_setor "
				+ " FROM ptc_tipo_documento A1 "
				+ " JOIN gpn_tipo_documento A ON (A1.cd_tipo_documento=A.cd_tipo_documento)"
				+ " LEFT OUTER JOIN gpn_tipo_documento B ON (A.cd_tipo_documento_superior = B.cd_tipo_documento) "
				+ " LEFT OUTER JOIN grl_pessoa_juridica C ON (A.cd_empresa = C.cd_pessoa)"
				+ " LEFT OUTER JOIN grl_setor D ON (A.cd_setor = D.cd_setor)", 
				" ORDER BY nm_tipo_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static boolean findThisNumber(String nrDocumento, int cdTipoDocumento, int cdSetor, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement pstmt = connect.prepareStatement(" SELECT nr_documento FROM ptc_documento " +
																" WHERE UPPER (nr_documento) LIKE UPPER('"+nrDocumento+"%') " +
																" AND cd_empresa = "+cdEmpresa +
																" AND cd_tipo_documento ="+cdTipoDocumento+
																" AND cd_setor = "+cdSetor);
			
			ResultSet rs = pstmt.executeQuery();
			String dsValue = "";
			if(rs.next()) 
				dsValue = rs.getString("NR_DOCUMENTO");

			if(dsValue==null || dsValue.trim().equals(""))
				return false;
			else
				return dsValue.equalsIgnoreCase(nrDocumento);
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
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
	 * @return ResultSetMap formulário dinâmico com atributos
	 * @author Maurício
	 * @since 22/07/2014
	 * @see com.tivic.manager.grl.TipoDocumentoServices2.getFormulario
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
	
	public static FormularioDTO get(int cdTipoDocumento, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			Result result = getFormulario(cdTipoDocumento);
			
			FormularioDTO form = new FormularioDTO((Formulario) result.getObjects().get("FORMULARIO"));
			form.setAtributos(new ResultSetMapper<FormularioAtributo>((ResultSetMap) result.getObjects().get("RSM_ATRIBUTOS"), FormularioAtributo.class).toList());
			
			return form;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllTipoDocumentoEel() {
		return getAllTipoDocumentoEel(null);
	}
	
	public static ResultSetMap getAllTipoDocumentoEel(Connection connect) {
		return EelUtils.getAllTiposDocumento(connect);
	}
	
	public static int getTipoDocumentoByTipoDocumentoEel(String cdTipoDocumento) {
		return getTipoDocumentoByTipoDocumentoEel(cdTipoDocumento, null);
	}
	
	public static int getTipoDocumentoByTipoDocumentoEel(String cdTipoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int tpDoc = 0;
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A1.cd_tipo_documento "
					+ " FROM ptc_tipo_documento A1"
					+ " JOIN gpn_tipo_documento A on (a.cd_tipo_documento=A1.cd_tipo_documento)"
					+ " WHERE A.nr_externo ='"+cdTipoDocumento+"'");
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				tpDoc = rs.getInt("cd_tipo_documento");
			}
			
			return tpDoc;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! TipoDocumentoServices.getTipoDocumentoByTipoDocumentoEel: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! TipoDocumentoServices.getTipoDocumentoByTipoDocumentoEel: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getTxtTipoDocumento(int cdTipoDocumento) {
		return getTxtTipoDocumento(cdTipoDocumento, null);
	}
	
	public static String getTxtTipoDocumento(int cdTipoDocumento, Connection connect) {
		
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT NM_TIPO_DOCUMENTO FROM GPN_TIPO_DOCUMENTO WHERE CD_TIPO_DOCUMENTO = ?");
			pstmt.setInt(1, cdTipoDocumento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			String tpDocumento = null;
			
			if(rsm.next())
				tpDocumento = rsm.getString("NM_TIPO_DOCUMENTO");
			
			return tpDocumento;
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static TipoDocumento getTpDocumentoByNome(String nmTipoDocumento, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GPN_TIPO_DOCUMENTO WHERE NM_TIPO_DOCUMENTO ILIKE ?");
			pstmt.setString(1, nmTipoDocumento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			TipoDocumento tpDocumento;
			
			if(rsm.next()) {
				tpDocumento = new TipoDocumento(
						rsm.getInt("cd_tipo_documento"),
						rsm.getString("nm_tipo_documento"),
						rsm.getString("id_tipo_documento"),
						rsm.getInt("st_tipo_documento"),
						rsm.getInt("cd_empresa"),
						rsm.getInt("cd_setor"),
						rsm.getInt("cd_formulario"),
						rsm.getInt("tp_formulario"),
						rsm.getString("id_prefixo_numeracao"),
						rsm.getString("ds_mascara_numeracao"),
						rsm.getInt("nr_ultima_numeracao"),
						rsm.getInt("cd_tipo_documento_superior"),
						rsm.getInt("lg_numeracao_superior"),
						rsm.getString("nr_externo")
						);				
				
				return tpDocumento;
			}
			
			return null;
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
