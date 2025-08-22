package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;
import sol.util.Result;

public class PessoaFisicaServices {

	public static final String[][] siglaOrgaoExpedidor = {{"CNT", "CNT"}, {"DIC", "DIC"},
		  {"IFP", "IFP"}, {"IPF", "IPF"},
		  {"MAE", "MAE"}, {"MEX", "MEX"},
		  {"MMA", "MMA"}, {"POF", "POF"},
		  {"POM", "POM"}, {"SES", "SES"},
		  {"SSP", "SSP"}};

	public static String[] tipoCategoriaHabilitacao = {"A","B","C","D","E","A/B","A/C","A/D","A/E"};

	public static String[] situacaoEstadoCivil = {"Solteiro(a)",
		"Casado(a)[Com.Parc.Bens]",
		"Separado(a) Judicialmente",
		"Divorciado(a)",
		"Viúvo(a)",
		"Outros",
		"Casado(a)[Com.Total Bens]",
		"Casado(a)[Sep.Total Bens]",
		"União Estável",
		"Casado"};
	
	public static final int TP_SEXO_MASCULINO = 0;
	public static final int TP_SEXO_FEMININO  = 1;
	
	public static String[] tipoSexo = {"Masculino",
		"Feminino"};

	public static String[][] formaTratamento = {{"Sr(a)","Sr(a)"}, {"Srta","Srta"}, {"Você","Você"}, {"Dr(a)","Dr(a)"}, {"V. Ex.ª","V. Ex.ª"}};
	
	
	/*Tipo de Raça*/
	public static final int TP_RACA_NAO_DECLARADA = 0;
	public static final int TP_RACA_BRANCA 		  = 1;
	public static final int TP_RACA_PRETA 		  = 2;
	public static final int TP_RACA_PARDA		  = 3;
	public static final int TP_RACA_AMARELA		  = 4;
	public static final int TP_RACA_INDIGENA 	  = 5;
	
	public static final String[] tipoRaca = {"Não Declarada", "Branca", "Preta", "Parda", "Amarela", "Indigena"};
	
	/*Tipo de Nacionalidade*/
	public static final int TP_NACIONALIDADE_BRASILEIRA = 1;
	public static final int TP_NACIONALIDADE_BRASILEIRA_EXTERIOR = 2;
	public static final int TP_NACIONALIDADE_ESTRANGEIRA = 3;
	
	public static final String TP_ESCOLARIDADE_FUND_INCOMPLETO   = "1";
	public static final String TP_ESCOLARIDADE_FUND_COMPLETO     = "2";
	public static final String TP_ESCOLARIDADE_ENS_MEDIO_MAG     = "3";
	public static final String TP_ESCOLARIDADE_ENS_MEDIO_MAG_IND = "4"; 
	public static final String TP_ESCOLARIDADE_ENS_MEDIO         = "5";
	public static final String TP_ESCOLARIDADE_ENS_SUPERIOR      = "6";
	
	/* Tipo de categoria da CNH */
	public static final int TP_CATEGORIA_CNH_A  = 0;
	public static final int TP_CATEGORIA_CNH_B  = 1;
	public static final int TP_CATEGORIA_CNH_AB = 2;
	public static final int TP_CATEGORIA_CNH_C  = 3;
	public static final int TP_CATEGORIA_CNH_AC = 4;
	public static final int TP_CATEGORIA_CNH_D  = 5;
	public static final int TP_CATEGORIA_CNH_AD = 6;
	public static final int TP_CATEGORIA_CNH_E  = 7;
	public static final int TP_CATEGORIA_CNH_AE = 8;
	
	
	public static Result save(PessoaFisica pessoaFisica){
		return save(pessoaFisica, null, null);
	}
	
	public static Result save(PessoaFisica pessoaFisica, Connection connect){
		return save(pessoaFisica, null, connect);
	}
	
	public static Result save(PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco){
		return save(pessoaFisica, pessoaEndereco, null);
	}
	
	public static Result save(PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco, Connection connect){
		return save(pessoaFisica, pessoaEndereco, 0, 0, null);
	}
	
	public static Result save(PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco, int cdVinculo, int cdEmpresa){
		return save(pessoaFisica, pessoaEndereco, 0, 0, null);
	}

	public static Result save(PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco, int cdVinculo, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaFisica==null)
				return new Result(-1, "Erro ao salvar. PessoaFisica é nulo");

			int retorno;
			if(pessoaFisica.getCdPessoa()==0){
				retorno = PessoaFisicaDAO.insert(pessoaFisica, connect);
				pessoaFisica.setCdPessoa(retorno);
			}
			else {
				retorno = PessoaFisicaDAO.update(pessoaFisica, connect);
			}
			
			if(pessoaEndereco!=null){
				pessoaEndereco.setCdPessoa(pessoaFisica.getCdPessoa());
				Result result = PessoaEnderecoServices.save(pessoaEndereco, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			if(cdVinculo>0 && cdEmpresa>0) {
				Result result = PessoaEmpresaServices.save(new PessoaEmpresa(cdEmpresa, pessoaFisica.getCdPessoa(), cdVinculo, new GregorianCalendar(), PessoaEmpresaServices.ST_ATIVO), connect);
				if( retorno <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOAFISICA", pessoaFisica);
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

			/* remove vinculados de ocupacao - cbo */
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM grl_cbo_pessoa_fisica " +
					"WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();

			/* remove vinculados de aluno - acd */
			pstmt = connection.prepareStatement("DELETE FROM acd_aluno " +
					"WHERE cd_aluno = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();
			
			if (!notDeleteRoot && PessoaFisicaDAO.delete(cdPessoa, connection)<=0) {
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
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaServices.getAll: " + e);
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
		String sql =
		  "SELECT A.*, B.*, C.nm_pais, D.nm_forma_divulgacao, " +
		  "       E.nm_classificacao, F.cd_cidade AS cd_naturalidade, " +
		  "       F.nm_cidade AS nm_naturalidade, G.nm_escolaridade, G.id_escolaridade "+
		  "FROM grl_pessoa A " +
		  "JOIN grl_pessoa_fisica B ON (A.cd_pessoa = B.cd_pessoa) " +
		  "LEFT OUTER JOIN grl_pais C ON (A.cd_pais = C.cd_pais) " +
		  "LEFT OUTER JOIN grl_forma_divulgacao D ON (A.cd_forma_divulgacao = D.cd_forma_divulgacao) "+
		  "LEFT OUTER JOIN adm_classificacao E ON (A.cd_classificacao = E.cd_classificacao) "+
	      "LEFT OUTER JOIN grl_cidade F ON (B.cd_naturalidade = F.cd_cidade) " +
	      "LEFT OUTER JOIN grl_escolaridade G ON (B.cd_escolaridade = G.cd_escolaridade) ";
		return Search.find(sql, "",criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap findPessoaFisica(ArrayList<ItemComparator> criterios) {
		return findPessoaFisica(criterios, null);
	}
	
	public static ResultSetMap findPessoaFisica(ArrayList<ItemComparator> criterios, Connection connect) {
		int qtRegistros = 0;
		
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			 if (criterios.get(i).getColumn().equalsIgnoreCase("LIMIT")) {
				qtRegistros = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				criterios.remove(i);
				i--;
			}
		}
		
		String sql = "SELECT * FROM grl_pessoa_fisica A LEFT JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) ";
		
		ResultSetMap rsm = Search.find(sql, (qtRegistros > 0? " LIMIT " + qtRegistros: ""), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		return rsm;
	}

	public static ResultSetMap getAllCbos(int cdPessoa) {
		return getAllCbos(cdPessoa, null);
	}

	public static ResultSetMap getAllCbos(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.lg_principal " +
					"FROM grl_cbo A, grl_cbo_pessoa_fisica B " +
					"WHERE A.cd_cbo = B.cd_cbo " +
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

	public static PessoaFisica getByCpf(String nrCpf) {
		return getByCpf(nrCpf, null);
	}

	public static PessoaFisica getByCpf(String nrCpf, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			nrCpf = nrCpf.replaceAll("[\\.-]", "");

			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica A, grl_pessoa B WHERE A.cd_pessoa=B.cd_pessoa AND A.nr_cpf=?");
			pstmt.setString(1, nrCpf);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaFisica(rs.getInt("cd_pessoa"),
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
						rs.getInt("cd_naturalidade"),
						rs.getInt("cd_escolaridade"),
						(rs.getTimestamp("dt_nascimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_nascimento").getTime()),
						rs.getString("nr_cpf"),
						rs.getString("sg_orgao_rg"),
						rs.getString("nm_mae"),
						rs.getString("nm_pai"),
						rs.getInt("tp_sexo"),
						rs.getInt("st_estado_civil"),
						rs.getString("nr_rg"),
						rs.getString("nr_cnh"),
						(rs.getTimestamp("dt_validade_cnh")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade_cnh").getTime()),
						(rs.getTimestamp("dt_primeira_habilitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeira_habilitacao").getTime()),
						rs.getInt("tp_categoria_habilitacao"),
						rs.getInt("tp_raca"),
						rs.getInt("lg_deficiente_fisico"),
						rs.getString("nm_forma_tratamento"),
						rs.getInt("cd_estado_rg"),
						(rs.getTimestamp("dt_emissao_rg")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao_rg").getTime()),
						rs.getBytes("blb_fingerprint")==null?null:rs.getBytes("blb_fingerprint"),
						rs.getInt("cd_conjuge"),
						rs.getInt("qt_membros_familia"),
						rs.getFloat("vl_renda_familiar_per_capta"),
						rs.getInt("tp_nacionalidade"),
						rs.getInt("tp_filiacao_pai"));
			}
			else{
				return null;
			}
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

	public static ResultSetMap loadByCpf(String nrCpf) {
		return loadByCpf(nrCpf, null);
	}

	public static ResultSetMap loadByCpf(String nrCpf, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			nrCpf = nrCpf.replaceAll("[\\.-]", "");

			pstmt = connect.prepareStatement("SELECT AA.*, BB.*, A.*, B.nm_tipo_endereco, C.nm_tipo_logradouro, C.sg_tipo_logradouro, " +
                                          "       G.nm_tipo_logradouro AS nm_tipo_logradouro_enderecamento, " +
										  "       G.sg_tipo_logradouro AS sg_tipo_logradouro_enderecamento, " +
										  "       D.nm_logradouro AS nm_logradouro_enderecamento, D.id_logradouro, " +
										  "       E.nm_cidade, F.nm_bairro AS nm_bairro_enderecamento, F.id_bairro," +
										  "       H.nm_estado, H.sg_estado " +
										  "FROM grl_pessoa_fisica AA " +
										  "JOIN grl_pessoa BB ON (AA.cd_pessoa=BB.cd_pessoa) " +
										  "LEFT OUTER JOIN grl_pessoa_endereco A ON (A.cd_pessoa = AA.cd_pessoa AND A.lg_principal=1) " +
										  "LEFT OUTER JOIN grl_tipo_endereco B ON (A.cd_tipo_endereco = B.cd_tipo_endereco) " +
										  "LEFT OUTER JOIN grl_tipo_logradouro C ON (A.cd_tipo_logradouro = C.cd_tipo_logradouro) " +
										  "LEFT OUTER JOIN grl_logradouro D ON (A.cd_logradouro = D.cd_logradouro) "+
										  "LEFT OUTER JOIN grl_tipo_logradouro G ON (D.cd_tipo_logradouro = G.cd_tipo_logradouro) " +
										  "LEFT OUTER JOIN grl_cidade E ON (A.cd_cidade = E.cd_cidade) " +
										  "LEFT OUTER JOIN grl_bairro F ON (A.cd_bairro = F.cd_bairro) " +
										  "LEFT OUTER JOIN grl_estado H ON (E.cd_estado = H.cd_estado) " +
										  "WHERE AA.nr_cpf=?");
			pstmt.setString(1, nrCpf);
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

	public static HashMap<String, Object> getAsRegister(int cdPessoa) {
		return getAsRegister(cdPessoa, null);
	}

	public static HashMap<String, Object> getAsRegister(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_pessoa, A.cd_pessoa_superior, A.cd_pais, A.nm_pessoa, " +
					"A.nr_telefone1, A.nr_telefone2, A.nr_celular, A.nr_fax, A.nm_email, A.dt_cadastro, A.gn_pessoa, A.img_foto, " +
					"A.st_cadastro, A.nm_url, A.nm_apelido, A.txt_observacao, A.lg_notificacao, A.id_pessoa, A.cd_classificacao, " +
					"A.cd_forma_divulgacao, A.dt_chegada_pais, B.cd_naturalidade, B.cd_escolaridade, B.dt_nascimento, B.nr_cpf, " +
					"B.sg_orgao_rg, B.nm_mae, B.nm_pai, B.tp_sexo, B.st_estado_civil, B.nr_rg, B.nr_cnh, B.dt_validade_cnh, " +
					"B.dt_primeira_habilitacao, B.tp_categoria_habilitacao, B.tp_raca, B.lg_deficiente_fisico, B.nm_forma_tratamento, " +
					"B.cd_estado_rg, B.dt_emissao_rg, B.blb_fingerprint, C.nm_escolaridade, D.sg_estado AS sg_estado_rg " +
					"FROM grl_pessoa_fisica B " +
					"JOIN grl_pessoa A ON (B.cd_pessoa = A.cd_pessoa) " +
					"LEFT OUTER JOIN grl_escolaridade C ON (B.cd_escolaridade = C.cd_escolaridade) " +
					"LEFT OUTER JOIN grl_estado D ON (B.cd_estado_rg = D.cd_estado) " +
					"WHERE B.cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return !rsm.next() ? null : rsm.getRegister();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaServices.getAsRegister: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}