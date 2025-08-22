package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;
import sol.util.Result;

public class PessoaJuridicaServices {

	public static final int ERR_GERAL = -1;
	public static final int ERR_NR_DOCUMENTO = -2;

	public static final int TP_MICRO_EMPRESA = 0;
	public static final int TP_EMPRESA_PEQUENO_PORTE = 1;
	public static final int TP_MEDIO_GRANDE = 2;
	public static final int TP_SEM_FINS_LUCRATIVOS = 3;
	public static final int TP_OUTROS = 4;

	public static String[] tipoEmpresa = {"Micro-empresa",
		"Empresa de pequeno porte",
		"Médio ou grande porte",
		"Sem fins lucrativos"};

	public static int delete(int cdPessoa) {
		return delete(cdPessoa, false, null);
	}

	public static int delete(int cdPessoa, Connection connection) {
		return delete(cdPessoa, true, connection);
	}

	public static int delete(int cdPessoa, boolean notDeleteRoot) {
		return delete(cdPessoa, notDeleteRoot, null);
	}

	public static int delete(int cdPessoa, boolean notDeleteRoot, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			/* remove vinculacoes de atividades - cnae */
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_cnae_pessoa_juridica " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			/* remove contatos */
			pstmt = connection.prepareStatement("DELETE FROM grl_pessoa_contato " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			if (!notDeleteRoot && PessoaJuridicaDAO.delete(cdPessoa, connection)<=0) {
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
			System.err.println("Erro! PessoaFisicaServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		};
		
	
		ResultSetMap rsm = Search.find("SELECT A.*, B.*, C.nm_pais, D.nm_forma_divulgacao, E.nm_classificacao, " +
			      "       F.id_natureza_juridica, F.nm_natureza_juridica, "+
			      "	      I.cd_cidade, I.nm_cidade " +
				  "FROM grl_pessoa A " +
				  "JOIN grl_pessoa_juridica              B ON (A.cd_pessoa = B.cd_pessoa) " +
				  "JOIN grl_pessoa_empresa               G ON (G.cd_pessoa = B.cd_pessoa) " +
				  "LEFT OUTER JOIN grl_pais              C ON (A.cd_pais = C.cd_pais) " +
				  "LEFT OUTER JOIN grl_forma_divulgacao  D ON (A.cd_forma_divulgacao = D.cd_forma_divulgacao) "+
				  "LEFT OUTER JOIN adm_classificacao     E ON (A.cd_classificacao = E.cd_classificacao) " +
				  "LEFT OUTER JOIN grl_natureza_juridica F ON (B.cd_natureza_juridica = F.cd_natureza_juridica) "+
				  "LEFT OUTER JOIN grl_pessoa_endereco   H ON (A.cd_pessoa = H.cd_pessoa AND H.lg_principal = 1) "+
				  "LEFT OUTER JOIN grl_cidade 			 I ON (H.cd_cidade = I.cd_cidade)" +limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		return rsm;
		
	}

	public static ResultSetMap getAllCnaes(int cdPessoa) {
		return getAllCnaes(cdPessoa, null);
	}

	public static ResultSetMap getAllCnaes(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.lg_principal " +
					"FROM grl_cnae A, grl_cnae_pessoa_juridica B " +
					"WHERE A.cd_cnae = B.cd_cnae " +
					"  AND B.cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
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

	public static PessoaJuridica getByCnpj(String nrCnpj) {
		return getByCnpj(nrCnpj, null);
	}

	public static PessoaJuridica getByCnpj(String nrCnpj, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_juridica A, grl_pessoa B WHERE A.cd_pessoa=B.cd_pessoa AND A.nr_cnpj=?");
			pstmt.setString(1, nrCnpj);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaJuridica(rs.getInt("cd_pessoa"),
						rs.getInt("cd_pessoa_superior"),
						rs.getInt("cd_pais"),
						rs.getString("nm_pessoa"),
						rs.getString("nr_telefone1"),
						rs.getString("nr_telefone2"),
						rs.getString("nr_celular"),
						rs.getString("nr_fax"),
						rs.getString("nm_email"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getInt("gn_pessoa"),
						rs.getBytes("img_foto")==null?null:rs.getBytes("img_foto"),
						rs.getInt("st_cadastro"),
						rs.getString("nm_url"),
						rs.getString("nm_apelido"),
						rs.getString("txt_observacao"),
						rs.getInt("lg_notificacao"),
						rs.getString("id_pessoa"),
						rs.getInt("cd_classificacao"),
						rs.getInt("cd_forma_divulgacao"),
						(rs.getTimestamp("dt_chegada_pais")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_chegada_pais").getTime()),
						rs.getString("nr_cnpj"),
						rs.getString("nm_razao_social"),
						rs.getString("nr_inscricao_estadual"),
						rs.getString("nr_inscricao_municipal"),
						rs.getInt("nr_funcionarios"),
						(rs.getTimestamp("dt_inicio_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_atividade").getTime()),
						rs.getInt("cd_natureza_juridica"),
						rs.getInt("tp_empresa"),
						(rs.getTimestamp("dt_termino_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino_atividade").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap loadByCnpj(String nrCnpj) {
		return loadByCnpj(nrCnpj, null);
	}

	public static ResultSetMap loadByCnpj(String nrCnpj, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			nrCnpj = nrCnpj.replaceAll("[\\.-/]", "");

			pstmt = connect.prepareStatement("SELECT AA.*, BB.*, A.*, B.nm_tipo_endereco, C.nm_tipo_logradouro, C.sg_tipo_logradouro, " +
                                          "       G.nm_tipo_logradouro AS nm_tipo_logradouro_enderecamento, " +
										  "       G.sg_tipo_logradouro AS sg_tipo_logradouro_enderecamento, " +
										  "       D.nm_logradouro AS nm_logradouro_enderecamento, D.id_logradouro, " +
										  "       E.nm_cidade, F.nm_bairro AS nm_bairro_enderecamento, F.id_bairro," +
										  "       H.nm_estado, H.sg_estado " +
										  "FROM grl_pessoa_juridica AA " +
										  "JOIN grl_pessoa BB ON (AA.cd_pessoa=BB.cd_pessoa) " +
										  "LEFT OUTER JOIN grl_pessoa_endereco A ON (A.cd_pessoa = AA.cd_pessoa AND A.lg_principal=1) " +
										  "LEFT OUTER JOIN grl_tipo_endereco B ON (A.cd_tipo_endereco = B.cd_tipo_endereco) " +
										  "LEFT OUTER JOIN grl_tipo_logradouro C ON (A.cd_tipo_logradouro = C.cd_tipo_logradouro) " +
										  "LEFT OUTER JOIN grl_logradouro D ON (A.cd_logradouro = D.cd_logradouro) "+
										  "LEFT OUTER JOIN grl_tipo_logradouro G ON (D.cd_tipo_logradouro = G.cd_tipo_logradouro) " +
										  "LEFT OUTER JOIN grl_cidade E ON (A.cd_cidade = E.cd_cidade) " +
										  "LEFT OUTER JOIN grl_bairro F ON (A.cd_bairro = F.cd_bairro) " +
										  "LEFT OUTER JOIN grl_estado H ON (E.cd_estado = H.cd_estado) " +
										  "WHERE AA.nr_cnpj=?");
			pstmt.setString(1, nrCnpj);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaServices.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result save(PessoaJuridica pessoaJuridica) {
		return save(pessoaJuridica, null, null);
	}

	public static Result save(PessoaJuridica pessoaJuridica, PessoaEndereco pessoaEndereco) {
		return save(pessoaJuridica, pessoaEndereco, null);
	}

	public static Result save(PessoaJuridica pessoaJuridica, PessoaEndereco pessoaEndereco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if (pessoaJuridica == null)
				return new Result(-1, "Pessoa jurídica inválida!");

			int retorno;

			//Empresa
			if(pessoaJuridica.getCdPessoa() == 0){
				retorno = PessoaJuridicaDAO.insert(pessoaJuridica, connect);
				if (retorno > 0)
					pessoaJuridica.setCdPessoa(retorno);
				else {
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar incluir pessoa jurídica!");
				}
			}
			else {
				retorno = PessoaJuridicaDAO.update(pessoaJuridica, connect);
				retorno = retorno > 0?pessoaJuridica.getCdPessoa():retorno;
				if(retorno < 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar alterar dados de pessoa jurídica!");
				}
			}

			//Endereço
			if(pessoaEndereco != null){
				if(pessoaEndereco.getCdPessoa() == 0){
					pessoaEndereco.setCdPessoa(pessoaJuridica.getCdPessoa());
					retorno = PessoaEnderecoDAO.insert(pessoaEndereco, connect);
					pessoaEndereco.setCdEndereco(retorno);
				}
				else
					retorno = PessoaEnderecoDAO.update(pessoaEndereco, connect);

				if(retorno < 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar incluir dados do endereço na pessoa jurídica!");
				}
			}

			if(retorno < 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();


			Result result = new Result(retorno);
			result.addObject("pessoaJuridica", pessoaJuridica);
			result.addObject("endereco", pessoaEndereco);
			result.addObject("cdPessoa", pessoaJuridica.getCdPessoa());
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-3, "Erro ao tentar salvar dados de pessoa jurídica!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}