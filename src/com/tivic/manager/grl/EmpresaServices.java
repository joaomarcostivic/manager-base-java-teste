package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.adm.TabelaCatEconomica;
import com.tivic.manager.adm.TabelaCatEconomicaDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.util.ResultSetMapper;

public class EmpresaServices {

	public static final int ERR_GERAL = -1;
	public static final int ERR_NR_DOCUMENTO = -2;

	public static ResultSetMap init(){
		
		Connection connection = null;
		
		boolean isConnectionNull = true;
		try {
			connection = Conexao.conectar();
			
			Empresa empresa = null;
			
			if(EmpresaDAO.getAll().size() == 0){
				int cdPais = PaisServices.getById("76", connection).getCdPais();
				empresa = new Empresa(0, 0, cdPais, "EMPRESA", null, null, null, null, null, null, PessoaServices.TP_JURIDICA, null, 1, null, "EMPRESA", "EMPRESA", 0, null, 0, 0, null, null, "EMPRESA", null, null, 0, null, 0, 0, null, 0, null, "EMPRESA", 0);
			
				if(EmpresaServices.insert(empresa, connection) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
		
		
		return null;
	}
	
	public static byte[] getBytesLogo() {
		return getBytesLogo(0);
	}

	public static byte[] getBytesLogo(int cdEmpresa) {
		return getBytesLogo(cdEmpresa, null);
	}

	public static byte[] getBytesLogo(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			if (cdEmpresa <= 0) {
				ResultSetMap rsm = EmpresaDAO.getAll();
				cdEmpresa = rsm.next() ? rsm.getInt("CD_EMPRESA") : 0;
			}

			Empresa empresa = cdEmpresa<=0 ? null : EmpresaDAO.get(cdEmpresa, connection);
			return empresa==null ? null : empresa.getImgLogomarca();
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

	public static Empresa getDefaultEmpresa() {
		return getDefaultEmpresa(null);
	}

	public static Empresa getDefaultEmpresa(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			ResultSetMap rsm = Search.find("SELECT * FROM grl_empresa WHERE lg_matriz = 1", null,  false, connection, false);
			int cdEmpresa = rsm.next() ? rsm.getInt("CD_EMPRESA") : 0;

			Empresa empresa = cdEmpresa<=0 ? null : EmpresaDAO.get(cdEmpresa, connection);
//			if (empresa != null)
//				empresa.setImgLogomarca(null);
			return empresa;
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

	public static int getCidadeOfEmpresa(int cdEmpresa) {
		return getCidadeOfEmpresa(cdEmpresa, null);
	}

	public static int getCidadeOfEmpresa(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
																  "FROM grl_cidade A, grl_empresa B, grl_pessoa_juridica C, grl_pessoa_endereco D " +
																  "WHERE (B.cd_empresa = C.cd_pessoa) " +
																  "  AND (C.cd_pessoa = D.cd_pessoa AND D.lg_principal = 1) " +
																  "  AND A.cd_cidade = D.cd_cidade " +
																  "  AND B.cd_empresa = ?");
			pstmt.setInt(1, cdEmpresa);
			ResultSet rs = pstmt.executeQuery();
			return !rs.next() ? 0 : rs.getInt("cd_cidade");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int getEstadoOfEmpresa(int cdEmpresa) {
		return getEstadoOfEmpresa(cdEmpresa, null);
	}

	public static int getEstadoOfEmpresa(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
																  "FROM grl_cidade A, grl_empresa B, grl_pessoa_juridica C, grl_pessoa_endereco D " +
																  "WHERE (B.cd_empresa = C.cd_pessoa) " +
																  "  AND (C.cd_pessoa = D.cd_pessoa AND D.lg_principal = 1) " +
																  "  AND A.cd_cidade = D.cd_cidade " +
																  "  AND B.cd_empresa = ?");
			pstmt.setInt(1, cdEmpresa);
			ResultSet rs = pstmt.executeQuery();
			return !rs.next() ? 0 : rs.getInt("cd_estado");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_pessoa AS nm_empresa, B.nm_pessoa AS nm_fantasia, B.nr_telefone1, B.nr_telefone2, B.nr_fax, " +
				           "       B.nm_email, B.nm_url, C.nr_cnpj, C.nm_razao_social, C.nr_inscricao_estadual, C.nr_inscricao_municipal, " +
				           "       C.nr_funcionarios, C.dt_inicio_atividade, C.cd_natureza_juridica, C.tp_empresa, F.sg_estado, " +
				           "       D.cd_endereco, D.ds_endereco, D.cd_tipo_logradouro, D.cd_tipo_endereco, D.cd_logradouro, " +
				           "       D.cd_bairro, D.cd_cidade, D.nm_logradouro, D.nm_bairro, D.nr_cep, D.nr_endereco, " +
				           "       D.nm_complemento, D.nr_telefone, D.nm_ponto_referencia, E.nm_cidade, E.id_ibge " +
				           "FROM grl_empresa A " +
				           "JOIN grl_pessoa                     B ON (A.cd_empresa = B.cd_pessoa) " +
				           "JOIN grl_pessoa_juridica            C ON (B.cd_pessoa = C.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa_endereco D ON (C.cd_pessoa = D.cd_pessoa AND D.lg_principal = 1) " +
				           "LEFT OUTER JOIN grl_cidade          E ON (D.cd_cidade = E.cd_cidade) " +
				           "LEFT OUTER JOIN grl_estado          F ON (E.cd_estado = F.cd_estado)" , "ORDER BY B.nm_pessoa ASC", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					   "SELECT  A.*, B.nm_pessoa AS nm_empresa, " +
					           "B.nm_pessoa AS nm_fantasia, B.nr_telefone1, B.nr_telefone2, B.nr_fax, " +
					           "B.nm_email, B.nm_url, B.nm_pessoa as nm_pessoa, " +
					           "C.nr_cnpj, C.nm_razao_social, C.nr_inscricao_estadual, C.nr_inscricao_municipal, " +
					           "C.nr_funcionarios, C.dt_inicio_atividade, C.cd_natureza_juridica, C.tp_empresa, " +
					           "D.cd_endereco, D.ds_endereco, D.cd_tipo_logradouro, D.cd_tipo_endereco, D.cd_logradouro, " +
					           "D.cd_bairro, D.cd_cidade, D.nm_logradouro, D.nm_bairro, D.nr_cep, D.nr_endereco, " +
					           "D.nm_complemento, D.nr_telefone, D.nm_ponto_referencia, " +
					           "E.nm_cidade " +
			           "FROM grl_empresa A " +
			           "JOIN grl_pessoa B ON (A.cd_empresa = B.cd_pessoa) " +
			           "LEFT OUTER JOIN grl_pessoa_juridica C ON (B.cd_pessoa = C.cd_pessoa) " +
			           "LEFT OUTER JOIN grl_pessoa_endereco D ON (B.cd_pessoa = D.cd_pessoa AND D.lg_principal = 1) " +
			           "LEFT OUTER JOIN grl_cidade E ON (D.cd_cidade = E.cd_cidade)");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("nm_fantasia");
			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllAtivos() {
		return getAllAtivos(null);
	}
	
	public static ResultSetMap getAllAtivos(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_empresa, " +
					"B.nm_pessoa AS nm_fantasia, B.nr_telefone1, B.nr_telefone2, B.nr_fax, " +
					"B.nm_email, B.nm_url, " +
					"C.nr_cnpj, C.nm_razao_social, C.nr_inscricao_estadual, C.nr_inscricao_municipal, " +
					"C.nr_funcionarios, C.dt_inicio_atividade, C.cd_natureza_juridica, C.tp_empresa, " +
					"D.ds_endereco, D.cd_tipo_logradouro, D.cd_tipo_endereco, D.cd_logradouro, " +
					"D.cd_bairro, D.cd_cidade, D.nm_logradouro, D.nm_bairro, D.nr_cep, D.nr_endereco, " +
					"D.nm_complemento, D.nr_telefone, D.nm_ponto_referencia, " +
					"E.nm_cidade " +
					"FROM grl_empresa A " +
					"JOIN grl_pessoa B ON (A.cd_empresa = B.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_juridica C ON (B.cd_pessoa = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_endereco D ON (B.cd_pessoa = D.cd_pessoa AND D.lg_principal = 1) " +
					"LEFT OUTER JOIN grl_cidade E ON (D.cd_cidade = E.cd_cidade) " +
					"WHERE B.st_cadastro = 1");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("nm_fantasia");
			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public HashMap<String, Object> getAsRegister(int cdEmpresa) {
		ResultSetMap rsm = getAsResultSet(cdEmpresa);
		return rsm.next() ? rsm.getRegister() : null ;
	}
	
	public static ResultSetMap getAsResultSet(int cdEmpresa) {
		return getAsResultSet(cdEmpresa, null);
	}

	public static ResultSetMap getAsResultSet(int cdEmpresa, Connection connect){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, connect);
	}

	public static Result save(Empresa empresa, PessoaEndereco endereco) {
		return save(empresa, endereco, null);
	}

	public static Result save(Empresa empresa, PessoaEndereco endereco, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			if (empresa == null)
				return new Result(-1, "Dados da empresa não localizados!");
			Result result = new Result(-1);
			result.setCode((empresa.getCdEmpresa() == 0) ? insert(empresa, connect) : update(empresa, connect));

			//Empresa
			if(result.getCode() <= 0)
				result.setMessage("Erro ao tentar salvar dados da empresa! [ERRO: "+result.getCode()+"]");

			if(empresa.getCdEmpresa() <= 0 && result.getCode() > 0)
				empresa.setCdEmpresa(result.getCode());

			//Endereço
			if(endereco != null && empresa.getCdEmpresa()>0) {
				endereco.setCdPessoa(empresa.getCdEmpresa());
				result.setCode((endereco.getCdEndereco() <= 0) ? PessoaEnderecoDAO.insert(endereco, connect) : PessoaEnderecoDAO.update(endereco, connect));
				if(result.getCode() < 0)
					result.setMessage("Erro ao tentar salvar endereço da empresa! [ERRO: "+result.getCode()+"]");
				else if (endereco.getCdEndereco() <= 0)
					endereco.setCdEndereco(result.getCode());
				
			}
			if(result.getCode() < 0)	{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			else if (isConnectionNull){
				connect.commit();
			}
			// Retorno
			result.setCode(empresa.getCdEmpresa());
			result.setMessage("Salvo com sucesso!");
			result.addObject("empresa", empresa);
			result.addObject("endereco", endereco);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-3, "Erro ao tentar salvar dados de empresa!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int insert(Empresa empresa) {
		return insert(empresa, null);
	}

	public static int insert(Empresa empresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdEmpresa = EmpresaDAO.insert(empresa, connection);
			if (cdEmpresa <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			int catEconomicasByEmpresa = ParametroServices.getValorOfParametroAsInteger("LG_CATEGORIAS_ECONOMICAS_POR_EMPRESA", 0, 0, connection);
			if (catEconomicasByEmpresa == 1) {
				int cdTabela = TabelaCatEconomicaDAO.insert(new TabelaCatEconomica(0 /*cdtabelaCatEconomica*/, "TABELA CAT ECONOMICAS - EMPRESA COD " + cdEmpresa), connection);
				if (cdTabela <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				empresa.setCdEmpresa(cdEmpresa);
				empresa.setCdTabelaCatEconomica(cdTabela);
				if (EmpresaDAO.update(empresa, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return cdEmpresa;
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

	public static int update(Empresa empresa) {
		return update(empresa, null);
	}

	public static int update(Empresa empresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int catEconomicasByEmpresa = ParametroServices.getValorOfParametroAsInteger("LG_CATEGORIAS_ECONOMICAS_POR_EMPRESA", 0, 0, connection);
			if (catEconomicasByEmpresa == 1) {
				ResultSet rs = connection.prepareStatement("SELECT * FROM grl_empresa WHERE cd_empresa = "+empresa.getCdEmpresa()).executeQuery();

				int cdTabela = !rs.next() ? 0 : rs.getInt("cd_tabela_cat_economica");
				if (cdTabela <= 0) {
					cdTabela = TabelaCatEconomicaDAO.insert(new TabelaCatEconomica(0 /*cdtabelaCatEconomica*/,
							"TABELA CAT ECONOMICAS - EMPRESA COD " + empresa.getCdEmpresa()), connection);
					if (cdTabela <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}

				empresa.setCdTabelaCatEconomica(cdTabela);
			}

			if (EmpresaDAO.update(empresa, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return empresa.getCdEmpresa();
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

	public static int delete(int cdEmpresa) {
		return delete(cdEmpresa, null);
	}

	public static int delete(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			connection.prepareStatement("DELETE FROM grl_parametro_valor WHERE cd_empresa = "+cdEmpresa).execute();

			connection.prepareStatement("DELETE FROM grl_empresa WHERE cd_empresa = "+cdEmpresa).execute();

			if (PessoaServices.delete(cdEmpresa, PessoaServices.TP_JURIDICA, connection) <= 0) {
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
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
	 * @category JURIS
	 */
	public static HashMap<String, Object> getAllParametrosVinculo() {
		return getAllParametrosVinculo(0, null);
	}
	
	/**
	 * @category JURIS
	 */
	public static HashMap<String, Object> getAllParametrosVinculo(int cdEmpresa) {
		return getAllParametrosVinculo(cdEmpresa, null);
	}
	
	/**
	 * @category JURIS
	 */
	public static HashMap<String, Object> getAllParametrosVinculo(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			params.put("CD_VINCULO_ADVERSO", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVERSO", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_ADVOGADO", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVOGADO", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_ADVOGADO_ADVERSO", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVOGADO_ADVERSO", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_AGENCIA_FINANCIADORA", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_AGENCIA_FINANCIADORA", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_AGENTE_CREDITO", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_AGENTE_CREDITO", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_AVALISTA", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_AVALISTA", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_CLIENTE", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_COLABORADOR", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_PROFESSOR", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PROFESSOR", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_CIDADAO", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CIDADAO", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_CORRESPONDENTE", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CORRESPONDENTE", 0, cdEmpresa, connection));
			params.put("CD_VINCULO_OFICIAL_JUSTICA", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_OFICIAL_JUSTICA", 0, cdEmpresa, connection));
			
			return params;
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
	
	public static ResultSetMap get(int cdEmpresa, String dtBalanco){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_empresa A, grl_pessoa_juridica B, grl_pessoa C WHERE A.cd_empresa=B.cd_pessoa AND B.cd_pessoa = C.cd_pessoa AND A.cd_empresa=?");
			pstmt.setInt(1, cdEmpresa);
			rs = pstmt.executeQuery();
			ResultSetMap rsm = new ResultSetMap(rs);
			rsm.next();
			rsm.setValueToField("DT_BALANCO", dtBalanco);
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Empresa getById(String idEmpresa) {
		return getById(idEmpresa, null);
	}

	public static Empresa getById(String idEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_empresa WHERE id_empresa=?");
			pstmt.setString(1, idEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return EmpresaDAO.get(rs.getInt("cd_empresa"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Empresa getEmpresaById(String idEmpresa) {
		return getEmpresaById(idEmpresa, null);
	}

	public static Empresa getEmpresaById(String idEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_empresa WHERE id_empresa=?");
			pstmt.setString(1, idEmpresa);
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			if(_rsm.next()){
				ResultSetMapper<Empresa> _conv = new ResultSetMapper<Empresa>(_rsm, Empresa.class);
				return _conv.toList().get(0);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static Result remove(int cdEmpresa){
		return remove(cdEmpresa, false, null);
	}
	
	public static Result remove(int cdEmpresa, boolean cascade){
		return remove(cdEmpresa, cascade, null);
	}
	
	public static Result remove(int cdEmpresa, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){				
				/*
				 * TODO:
				 * Excluir registros de das tabelas que fazem referência à empresa
				 */
			}
				
			if(!cascade || retorno>=0) {
				retorno = EmpresaDAO.delete(cdEmpresa, connect);
								
				if(retorno>=0) {
					retorno = PessoaJuridicaDAO.delete(cdEmpresa, connect);
				}
				
				if(retorno>=0) {
					retorno = PessoaDAO.delete(cdEmpresa, connect);
				}
			}
			
			if(retorno<0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta empresa está vinculado a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Empresa excluído com sucesso!");
		}
		catch(Exception e){
			System.out.println("Erro! EmpresaServices.remove: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir empresa!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}