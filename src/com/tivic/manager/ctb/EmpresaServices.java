package com.tivic.manager.ctb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class EmpresaServices {

	private static String sqlEmpresa = "SELECT DISTINCT A.*, " +
	   "	G.nm_razao_social, G.nr_cnpj, G.nr_inscricao_estadual, G.nr_inscricao_municipal, " +
	    "	G.nr_funcionarios, G.dt_inicio_atividade, G.cd_natureza_juridica, G.tp_empresa, " +
	    "	H.nm_pessoa AS nm_fantasia, H.nr_telefone1, H.nr_telefone2, H.nr_fax, " +
	    "	H.nm_email, H.nm_url, H.txt_observacao, " +
	    "	I.cd_endereco, I.ds_endereco, I.cd_tipo_logradouro, I.cd_tipo_endereco, I.cd_logradouro, " +
	    "	I.cd_bairro, I.cd_cidade, I.nm_logradouro, I.nm_bairro, I.nr_cep, I.nr_endereco, " +
	    "	I.nm_complemento, I.nr_telefone, I.nm_ponto_referencia, " +
	    "	C.nm_cidade, C.id_ibge, " +
	    "	D.nm_pessoa AS nm_contador, K.nr_cpf, " +
	    "	F.sg_estado AS sg_estado_crc, F.nm_estado AS nm_estado_crc, " +
	    "	J.cd_cnae, N.nm_cnae, N.nr_cnae, " +
	    "   M.id_natureza_juridica || ' - ' || M.nm_natureza_juridica AS nm_natureza_juridica, " +
	    "	P.nm_pessoa AS nm_responsavel_encerramento, " +
	    "	Q.nm_plano_contas, Q.cd_conta_resultado, " +
	    "	R.nm_conta AS nm_conta_resultado, " +
		   "	B.*, L.*, A.cd_empresa AS cd_empresa, H.nm_pessoa AS nm_empresa " +
	    "FROM grl_empresa A " +
	    "	LEFT OUTER JOIN grl_pessoa H ON (A.cd_empresa = H.cd_pessoa) " +
	    "	LEFT OUTER JOIN grl_pessoa_juridica G ON (A.cd_empresa  = G.cd_pessoa) " +
	    "	LEFT OUTER JOIN grl_pessoa_endereco I ON (A.cd_empresa = I.cd_pessoa AND I.lg_principal = 1) " +
	    "	LEFT OUTER JOIN grl_cidade C ON (I.cd_cidade = C.cd_cidade) " +
	    "	LEFT OUTER JOIN ctb_empresa B ON (A.cd_empresa = B.cd_empresa) "+
	    "	LEFT OUTER JOIN ctb_empresa_exercicio L ON (B.cd_empresa = L.cd_empresa) "+
	    "	LEFT OUTER JOIN grl_pessoa D ON (L.cd_contador = D.cd_pessoa) " +
	    "	LEFT OUTER JOIN grl_pessoa_fisica K ON (L.cd_contador = K.cd_pessoa) " +
	    "	LEFT OUTER JOIN grl_estado F ON (L.cd_estado_crc = F.cd_estado) " +
	    "   LEFT OUTER JOIN seg_usuario O ON (O.cd_usuario = L.cd_responsavel_encerramento) " +
	    "	LEFT OUTER JOIN grl_pessoa P ON (L.cd_responsavel_encerramento = P.cd_pessoa) " +
	    "	LEFT OUTER JOIN grl_cnae_pessoa_juridica J ON (A.cd_empresa = J.cd_pessoa AND J.lg_principal = 1) " +
	    "   LEFT OUTER JOIN grl_cnae N ON (J.cd_cnae = N.cd_cnae) " +
	    "   LEFT OUTER JOIN grl_natureza_juridica M ON (G.cd_natureza_juridica = M.cd_natureza_juridica) " +
	    "   LEFT OUTER JOIN ctb_plano_contas Q ON (Q.cd_plano_contas = L.cd_plano_contas) " +
	    "   LEFT OUTER JOIN ctb_conta R ON (Q.cd_conta_resultado = R.cd_conta) ";

	public static ResultSetMap getEmpresaAsResultSet(int cdEmpresa) {
		return getEmpresaAsResultSet(cdEmpresa, null);
	}

	public static ResultSetMap getEmpresaAsResultSet(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			PreparedStatement pstmt = connect.prepareStatement(sqlEmpresa);
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

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find(sqlEmpresa, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap loadExercicios(int cdEmpresa) {
		return loadExercicios(cdEmpresa, null);
	}

	public static ResultSetMap loadExercicios(int cdEmpresa, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_empresa", "cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return Search.find("SELECT A.* " +
						   "FROM ctb_empresa_exercicio A ", "ORDER BY A.nr_ano_exercicio DESC ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
