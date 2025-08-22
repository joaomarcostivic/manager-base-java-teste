package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.Aluno;
import com.tivic.manager.acd.AlunoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.DneUtilities;

public class PessoaEnderecoServices {

	public static final int TP_DIGITAVEL = 0;
	public static final int TP_END_LOGRADOUROS_BAIRROS = 1;

	/*Tipo de Zona*/
	public static final int TP_ZONA_URBANA = 1;
	public static final int TP_ZONA_RURAL  = 2;
	
	public static final String[] tiposZona = {"Nenhuma", "Urbana", "Rural"};
	
	public static final int TP_LOCALIZACAO_URBANA = 1;
	public static final int TP_LOCALIZACAO_RURAL  = 2;
	
	public static final String[] tiposLocalizacao = {"Nenhuma", "Urbana", "Rural"};
	
	public static final int ENDERECO_SECUNDARIO  = 0;
	public static final int ENDERECO_PRINCIPAL   = 1;
	
	public static Result saveCep(int cdEndereco, int cdPessoa, String nrCep){
		return saveCep(cdEndereco, cdPessoa, nrCep, null);
	}
	
	public static Result saveCep(int cdEndereco, int cdPessoa, String nrCep, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Pessoa pessoa = PessoaDAO.get(cdPessoa, connect);
			
			if(nrCep.length() < 8 || nrCep.length() > 8){
				return new Result(-1, "Aluno: "+pessoa.getNmPessoa()+", CEP "+nrCep.replaceAll("[^0-9]+", "")+" não encontrado na base");
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nr_cep", nrCep.replaceAll("[^0-9]+", ""), ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsmCep = LogradouroCepDAO.find(criterios, connect);
			if(!rsmCep.next()){
				JSONObject object = new JSONObject(DneUtilities.findEnderecoByCep(nrCep.replaceAll("[^0-9]+", ""), "json"));
				if(object == null || object.has("erro") && object.getBoolean("erro") == true) {
					return new Result(-1, "Aluno: "+pessoa.getNmPessoa()+", CEP "+nrCep.replaceAll("[^0-9]+", "")+" não encontrado na base");	
				}
			}
			
			
			int retorno = connect.prepareStatement("UPDATE grl_pessoa_endereco SET nr_cep = '"+nrCep.replaceAll("[^0-9]+", "")+"' WHERE cd_pessoa = " + cdPessoa + " AND cd_endereco = " +cdEndereco + " AND lg_principal = 1").executeUpdate();

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Aluno: "+pessoa.getNmPessoa()+", CEP " + nrCep.replaceAll("[^0-9]+", "") + " cadastrado com sucesso!");
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
	
	public static Result save(PessoaEndereco pessoaEndereco){
		return save(pessoaEndereco, null);
	}

	public static Result save(PessoaEndereco pessoaEndereco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaEndereco==null)
				return new Result(-1, "Erro ao salvar. PessoaEndereco é nulo");
			
			int retorno;
			
			// Codigo Herdado: faz a mudanca do endereco principal, caso o endereco novo passe a ser o principal
			if (pessoaEndereco.getLgPrincipal() == 1) {
				PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_endereco " +
						"SET lg_principal = 0 " +
						"WHERE lg_principal = 1 " +
						"  AND cd_endereco <> ? " +
						"  AND cd_pessoa = ?");
				pstmt.setInt(1, pessoaEndereco.getCdEndereco());
				pstmt.setInt(2, pessoaEndereco.getCdPessoa());
				pstmt.execute();
			}

//			int cdTipoCorrespodencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ENDERECO_CORRESPONDENCIA", 0);
//			System.out.println(pessoaEndereco.getCdTipoEndereco());
//			System.out.println(cdTipoCorrespodencia);
//			if (pessoaEndereco.getCdTipoEndereco() == cdTipoCorrespodencia) {
//				System.out.println("teste");
//				PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_endereco " +
//						"SET cd_tipo_endereco = null " +
//						"WHERE cd_tipo_endereco = ? " +
//						"  AND cd_endereco <> ? " +
//						"  AND cd_pessoa = ?");
//				pstmt.setInt(1, pessoaEndereco.getCdTipoEndereco());
//				pstmt.setInt(2, pessoaEndereco.getCdEndereco());
//				pstmt.setInt(3, pessoaEndereco.getCdPessoa());
//				pstmt.execute();
//			}
			if(pessoaEndereco.getCdEndereco()==0){
				
				retorno = PessoaEnderecoDAO.insert(pessoaEndereco, connect);
				pessoaEndereco.setCdEndereco(retorno);
			}
			else {
				retorno = PessoaEnderecoDAO.update(pessoaEndereco, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOAENDERECO", pessoaEndereco);
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
	public static Result remove(int cdEndereco, int cdPessoa){
		return remove(cdEndereco, cdPessoa, false, null);
	}
	public static Result remove(int cdEndereco, int cdPessoa, boolean cascade){
		return remove(cdEndereco, cdPessoa, cascade, null);
	}
	public static Result remove(int cdEndereco, int cdPessoa, boolean cascade, Connection connect){
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
			if(!cascade || retorno>0) {
				
				// Codigo Herdado: faz a mudanca do endereco principal, caso o endereco a ser excluido seja o principal
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(cdEndereco, cdPessoa);
				if (pessoaEndereco.getLgPrincipal() == 1) {
					PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco " +
					"WHERE cd_pessoa = ? AND cd_endereco <> ? ORDER BY cd_endereco");
					pstmt.setInt(1, cdPessoa);
					pstmt.setInt(2, cdEndereco);
					ResultSet rs = pstmt.executeQuery();
					if(rs.next()){
						PessoaEndereco newEnderecoPrincipal = PessoaEnderecoDAO.get(rs.getInt("CD_ENDERECO"), rs.getInt("CD_PESSOA"));
						newEnderecoPrincipal.setLgPrincipal(1);
						retorno = save(newEnderecoPrincipal, connect).getCode();
						if (retorno < 0) {
							return new Result(-3, "Este endereço é o único desta pessoa, para excluí-lo é necessário cadastrar outro!");
						}
					}
				}
				
				retorno = PessoaEnderecoDAO.delete(cdEndereco, cdPessoa, connect);
				
			}
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estão vinculado a outros e não pode ser excluído!");
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoServices.getAll: " + e);
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
		boolean isConnectionNull = connect==null;
		try {
			String sql 
					= "SELECT DISTINCT "
					+ "A.nr_endereco, A.ds_endereco, A.cd_endereco, A.cd_tipo_logradouro, A.cd_logradouro, A.cd_bairro, A.cd_cidade, A.cd_pessoa, A. nm_complemento, A.lg_cobranca, A.lg_principal, "
					+ "A.nm_logradouro AS nm_logradouro_nodne, A.nm_bairro AS nm_bairro_nodne, A.nr_cep AS nr_cep_nodne, "
					+ "A1.*, " 
					+ "B.nm_tipo_logradouro, B.sg_tipo_logradouro, " 
					+ "K.nm_tipo_logradouro as nm_tipo_logradouro_nodne, K.sg_tipo_logradouro as sg_tipo_logradouro_nodne, " 
					+ "C.nm_cidade, C1.nm_cidade AS nm_cidade_nodne, "
					+ "D.nm_distrito, " 
					+ "E.sg_estado, E.nm_estado, E1.sg_estado AS sg_estado_nodne, E1.nm_estado as nm_estado_nodne, " 
					+ "I.nm_bairro, " 
					+ "G.nr_cep, "
					+ "J.cd_tipo_endereco, J.nm_tipo_endereco "
					+ "FROM grl_pessoa_endereco A "
					+ "LEFT OUTER JOIN grl_logradouro A1 ON (A.cd_logradouro = A1.cd_logradouro) "
					+ "LEFT OUTER JOIN grl_tipo_logradouro B ON (A1.cd_tipo_logradouro = B.cd_tipo_logradouro) "
					+ "LEFT OUTER JOIN grl_cidade C ON (A1.cd_cidade = C.cd_cidade) "
					+ "LEFT OUTER JOIN grl_cidade C1 ON (A.cd_cidade = C1.cd_cidade) "
					+ "LEFT OUTER JOIN grl_distrito D ON (A1.cd_distrito = D.cd_distrito AND A1.cd_cidade = D.cd_cidade) "
					+ "LEFT OUTER JOIN grl_estado E ON (C.cd_estado = E.cd_estado) "
					+ "LEFT OUTER JOIN grl_estado E1 ON (C1.cd_estado = E1.cd_estado) "
					+ "LEFT OUTER JOIN grl_bairro F ON (C.cd_cidade = F.cd_cidade AND D.cd_distrito = F.cd_distrito) "
					+ "LEFT OUTER JOIN grl_logradouro_cep G ON (G.cd_logradouro = A1.cd_logradouro) "
					+ "LEFT OUTER JOIN grl_logradouro_bairro H ON (H.cd_logradouro = A1.cd_logradouro) "
					+ "LEFT OUTER JOIN grl_tipo_endereco J ON (J.cd_tipo_endereco = A.cd_tipo_endereco) "
					+ "LEFT OUTER JOIN grl_tipo_logradouro K ON (A.cd_tipo_logradouro = K.cd_tipo_logradouro)"
					+ "LEFT OUTER JOIN grl_bairro I ON (I.cd_bairro = H.cd_bairro) WHERE 1=1 ";
	
			ResultSetMap rsm = Search.find(sql, " ", criterios, connect!=null ? connect : Conexao.conectar(), false);
			
			while(rsm.next()){
				if(rsm.getString("NM_LOGRADOURO") == null || rsm.getString("NM_LOGRADOURO").equals("")){
					rsm.setValueToField("NM_LOGRADOURO", !rsm.getString("NM_LOGRADOURO_NODNE").equals("") ? rsm.getString("NM_LOGRADOURO_NODNE") : ""); //NM_LOGRADOURO_NODNE
				}
				if(rsm.getString("NM_BAIRRO") == null || rsm.getString("NM_BAIRRO").equals("")){
					rsm.setValueToField("NM_BAIRRO", rsm.getString("NM_BAIRRO_NODNE") != null ? rsm.getString("NM_BAIRRO_NODNE") : "");
				}
				if(rsm.getString("NR_CEP") == null || rsm.getString("NR_CEP").equals("")){
					rsm.setValueToField("NR_CEP", rsm.getString("NR_CEP_NODNE") != null ? rsm.getString("NR_CEP_NODNE") : "");
				}
				if(rsm.getString("NM_CIDADE") == null || rsm.getString("NM_CIDADE").equals("")){
					rsm.setValueToField("NM_CIDADE", rsm.getString("NM_CIDADE_NODNE") != null ? rsm.getString("NM_CIDADE_NODNE") : ""); //NM_CIDADE_NODNE
				}
				if(rsm.getString("SG_ESTADO") == null || rsm.getString("SG_ESTADO").equals("")){
					rsm.setValueToField("SG_ESTADO", rsm.getString("SG_ESTADO_NODNE") != null ? rsm.getString("SG_ESTADO_NODNE") : ""); //SG_ESTADO_NODNE
				}
				if(rsm.getString("NM_ESTADO") == null || rsm.getString("NM_ESTADO").equals("")){
					rsm.setValueToField("NM_ESTADO", rsm.getString("NM_ESTADO_NODNE") != null ? rsm.getString("NM_ESTADO_NODNE") : ""); //NM_ESTADO_NODNE
				}
				
				rsm.setValueToField("NM_ENDERECO", rsm.getString("NM_LOGRADOURO") + ", " + rsm.getString("NR_ENDERECO") + ", " + rsm.getString("NM_BAIRRO") + " - " + rsm.getString("NM_CIDADE") + "/" + rsm.getString("SG_ESTADO") + " - " + rsm.getString("NR_CEP"));
			}
			rsm.beforeFirst();
			
			return rsm; 
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoServices.find: " + e);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllContatos(int cdPessoa, int cdEndereco) {
		return getAllContatos(cdPessoa, cdEndereco, null);
	}
	
	/**
	 * Método criada pra buscar de grl_pessoa_endereço, 
	 * repositório antigo de endereços para atender demanda emergencial de entrega para o sistema de mobilidade
	 * antes da migração de endereço utilizando a base de DNE
	 * @deprecated
	 * @author Alvaro
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findPessoaEndereco(ArrayList<ItemComparator> criterios) {
		return findPessoaEndereco(criterios, null);
	}

	public static ResultSetMap findPessoaEndereco(ArrayList<ItemComparator> criterios, Connection connect) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM grl_pessoa_endereco A "
				+ " LEFT OUTER JOIN grl_tipo_endereco B ON (A.cd_tipo_endereco = B.cd_tipo_endereco) "
				+ " LEFT OUTER JOIN grl_tipo_logradouro C ON (A.cd_tipo_logradouro = C.cd_tipo_logradouro) "
				+ " WHERE 1=1 ");

		return Search.find(sql.toString(), criterios, connect!=null ? connect : Conexao.conectar());
	}
	/**
	 * Obter Endereço completo
	 * @author Alvaro
	 * @param cdPessoa
	 * @return
	 */
	public static Result getEnderecoPrincipalCompleto( int cdPessoa ){
		return getEnderecoPrincipalCompleto(cdPessoa, null);
	}

	public static Result getEnderecoPrincipalCompleto( int cdPessoa, Connection connect ){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.*, C.*, D.* " +
					"FROM grl_pessoa_endereco A " +
					"LEFT JOIN grl_tipo_logradouro B ON ( A.cd_tipo_logradouro = B.cd_tipo_logradouro ) " +
					"LEFT JOIN grl_cidade C ON ( A.cd_cidade = C.cd_cidade ) " +
					"LEFT JOIN grl_estado D ON ( C.cd_estado = D.cd_estado ) " +
					"WHERE A.cd_pessoa  = ? " +
					"  AND A.lg_principal = 1");
			pstmt.setInt(1, cdPessoa);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			rsm.beforeFirst();
			
			if( rsm.next() )
				return new Result(1, "Endereço recuperado com sucesso!", "PESSOAENDERECO", rsm.getRegister() );
			return new Result(-1, "Nenhum endereço encontrado.", "PESSOAENDERECO", new HashMap<String, Object>() );
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao buscar endereço" );
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static PessoaEndereco getEnderecoPrincipal(int cdPessoa) {
		return getEnderecoPrincipal(cdPessoa, null);
	}
	
	public static PessoaEndereco getEnderecoPrincipal(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSet rs = connect.prepareStatement("SELECT A.* FROM grl_pessoa_endereco A " +
					"WHERE cd_pessoa    = "+cdPessoa+
					"  AND lg_principal = 1").executeQuery();
			if(rs.next())	{
				return new PessoaEndereco(rs.getInt("cd_endereco"),
						rs.getInt("cd_pessoa"),
						rs.getString("ds_endereco"),
						rs.getInt("cd_tipo_logradouro"),
						rs.getInt("cd_tipo_endereco"),
						rs.getInt("cd_logradouro"),
						rs.getInt("cd_bairro"),
						rs.getInt("cd_cidade"),
						rs.getString("nm_logradouro"),
						rs.getString("nm_bairro"),
						rs.getString("nr_cep"),
						rs.getString("nr_endereco"),
						rs.getString("nm_complemento"),
						rs.getString("nr_telefone"),
						rs.getString("nm_ponto_referencia"),
						rs.getInt("lg_cobranca"),
						rs.getInt("lg_principal"),
						rs.getInt("tp_zona"));
			}
			return null;
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
	
	public static ResultSetMap getAllEnderecoByPessoa(int cdPessoa) {
		return getAllEnderecoByPessoa(cdPessoa, null);
	}
	
	public static ResultSetMap getAllEnderecoByPessoa(int cdPessoa, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("CD_PESSOA", ""+cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = find(criterios, connect);
 		return rsm;
	}

	public static ResultSetMap getAllContatos(int cdPessoa, int cdEndereco, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("CD_ENDERECO", Integer.toString(cdEndereco), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("CD_PESSOA_JURIDICA", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
		return PessoaContatoServices.find(criterios, connection);
	}

	@Deprecated
	public static int update(PessoaEndereco endereco) {
		return update(endereco, null);
	}

	@Deprecated
	public static int update(PessoaEndereco endereco, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if (endereco.getLgPrincipal() == 1) {
				PreparedStatement pstmt = connection.prepareStatement("UPDATE grl_pessoa_endereco " +
						"SET lg_principal = 0 " +
						"WHERE lg_principal = 1 " +
						"  AND cd_endereco <> ? " +
						"  AND cd_pessoa = ?");
				pstmt.setInt(1, endereco.getCdEndereco());
				pstmt.setInt(2, endereco.getCdPessoa());
				pstmt.execute();
			}

			if (PessoaEnderecoDAO.update(endereco, connection) <= 0) {
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

	@Deprecated
	public static int saveEndereco(PessoaEndereco pessoaEndereco){
		return saveEndereco(pessoaEndereco, null);
	}

	@Deprecated
	public static int saveEndereco(PessoaEndereco pessoaEndereco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			int retorno = 1;

			if (pessoaEndereco.getLgPrincipal() == 1) {
				PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_endereco " +
						"SET lg_principal = 0 " +
						"WHERE lg_principal = 1 " +
						"  AND cd_endereco <> ? " +
						"  AND cd_pessoa = ?");
				pstmt.setInt(1, pessoaEndereco.getCdEndereco());
				pstmt.setInt(2, pessoaEndereco.getCdPessoa());
				pstmt.execute();
			}

			if(pessoaEndereco.getCdEndereco()==0){
				retorno = PessoaEnderecoDAO.insert(pessoaEndereco, connect);
				if(retorno > 0){
					pessoaEndereco.setCdEndereco(retorno);
				}
			}
			else {
				retorno = PessoaEnderecoDAO.update(pessoaEndereco, connect);
			}

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	@Deprecated
	public static int delete(int cdEndereco, int cdPessoa) {
		return delete(cdEndereco, cdPessoa, null);
	}

	@Deprecated
	public static int delete(int cdEndereco, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			int retorno = 1;

			PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(cdEndereco, cdPessoa);
			if (pessoaEndereco.getLgPrincipal() == 1) {
				PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco " +
				"WHERE cd_pessoa = ? AND cd_endereco <> ? ORDER BY cd_endereco");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, cdEndereco);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()){
					PessoaEndereco newEnderecoPrincipal = PessoaEnderecoDAO.get(rs.getInt("CD_ENDERECO"), rs.getInt("CD_PESSOA"));
					newEnderecoPrincipal.setLgPrincipal(1);
					retorno = saveEndereco(newEnderecoPrincipal, connect);
					if (retorno < 0) {
						return retorno;
					}
				}
			}
			retorno = PessoaEnderecoDAO.delete(cdEndereco, cdPessoa, connect);
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveTipoZona(int cdPessoa, int tipoZona){
		return saveTipoZona(cdPessoa, tipoZona, null);
	}
	
	public static Result saveTipoZona(int cdPessoa, int tipoZona, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_pessoa", "" + cdPessoa, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPessoaEndereco = PessoaEnderecoDAO.find(criterios, connect);
			if(rsmPessoaEndereco.next()){
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(rsmPessoaEndereco.getInt("cd_endereco"), rsmPessoaEndereco.getInt("cd_pessoa"), connect);
				pessoaEndereco.setTpZona(tipoZona);
				if(PessoaEnderecoDAO.update(pessoaEndereco, connect) <= 0){
					return new Result(-1, "Erro ao atualizar zona da pessoa");
				}
				
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Alteração realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na evasão");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que busca o endereço de uma pessoa em forma de texto
	 * @param cdPessoa
	 * @return
	 */
	public static String getEnderecoCompleto( int cdPessoa ){
		return getEnderecoCompleto(cdPessoa, null);
	}

	public static String getEnderecoCompleto( int cdPessoa, Connection connect ){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.*, C.*, D.* " +
					"FROM grl_pessoa_endereco A " +
					"LEFT JOIN grl_tipo_logradouro B ON ( A.cd_tipo_logradouro = B.cd_tipo_logradouro ) " +
					"LEFT JOIN grl_cidade C ON ( A.cd_cidade = C.cd_cidade ) " +
					"LEFT JOIN grl_estado D ON ( C.cd_estado = D.cd_estado ) " +
					"WHERE A.cd_pessoa  = ? " +
					"  AND A.lg_principal = 1");
			pstmt.setInt(1, cdPessoa);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			rsm.beforeFirst();
			
			if( rsm.next() ){
				return rsm.getString("nm_logradouro") + ", " + rsm.getString("nr_endereco") + ", " + rsm.getString("nm_bairro") + ", " + rsm.getString("nm_cidade");
			}
			
			return "";
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

}