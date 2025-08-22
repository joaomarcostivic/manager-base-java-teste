package com.tivic.manager.flp;

import java.sql.*;
import java.util.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class FolhaPagamentoFuncionarioServices	{
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql =
			  "SELECT A.*, B.nm_pessoa, " +
			  "		C.nr_rg, N.sg_estado AS sg_orgao_rg, C.sg_uf_rg, C.nr_cpf, " +
			  "		D.nr_matricula, D.dt_matricula, " +
			  "		M.nm_razao_social, M.nr_cnpj," +
			  "		F.*, " +
			  "		G.nr_banco, G.nm_banco, " +
			  "     H.nm_setor, " +
		      "		I.nm_funcao, " +
		      "		J.nr_contrato, " +
		      "		K.nr_mes, K.nr_ano, " +
		      "		L.nm_vinculo_empregaticio, " +
		      "     B.cd_pessoa AS cd_pessoa " +
		      "FROM flp_folha_pagamento_funcionario A " +
			  "JOIN srh_dados_funcionais D ON (A.cd_matricula = D.cd_matricula) " +
			  "JOIN flp_folha_pagamento K ON (A.cd_folha_pagamento = K.cd_folha_pagamento) " +
			  "LEFT OUTER JOIN grl_pessoa B ON (D.cd_pessoa = B.cd_pessoa) " +
			  "JOIN grl_pessoa_fisica C ON (B.cd_pessoa = C.cd_pessoa) " +
			  "LEFT OUTER JOIN grl_estado N ON (C.cd_estado_rg = N.cd_estado) " +
			  "LEFT OUTER JOIN grl_empresa E ON (D.cd_empresa = E.cd_empresa) " +
			  "LEFT OUTER JOIN grl_pessoa_juridica M ON (E.cd_empresa  = M.cd_pessoa) " +
			  "LEFT OUTER JOIN grl_pessoa_conta_bancaria F ON (D.cd_pessoa = F.cd_pessoa " +
			  "                                            AND D.cd_conta_bancaria = F.cd_conta_bancaria) " +
			  "LEFT OUTER JOIN grl_banco G ON (F.cd_banco = G.cd_banco) " +
			  "LEFT OUTER JOIN grl_setor H ON (D.cd_setor = H.cd_setor) " +
			  "LEFT OUTER JOIN srh_funcao I ON (D.cd_funcao = I.cd_funcao) " +
			  "LEFT OUTER JOIN adm_contrato J ON (A.cd_convenio = J.cd_contrato) " +
			  "LEFT OUTER JOIN srh_vinculo_empregaticio L ON (A.cd_vinculo_empregaticio = L.cd_vinculo_empregaticio) ";
		return Search.find(sql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}