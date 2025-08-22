package com.tivic.manager.srh;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.manager.flp.*;
import com.tivic.manager.grl.*;
import com.tivic.manager.srh.Empresa;
import com.tivic.manager.srh.EmpresaDAO;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class EmpresaServices{

	public static final int ERR_GERAL = -1;
	public static final int ERR_NR_DOCUMENTO = -2;

	public static int init()	{
//		AgenteNocivoServices.init();
//		CategoriaFgtsServices.init();
		EscolaridadeServices.init();
		TipoAdmissaoServices.init();
		EventoFinanceiroServices.init();
		return 1;
	}

	public static Result save(Empresa empresa, PessoaEndereco pessoaEndereco) {
		return save(empresa, pessoaEndereco, null);
	}

	public static Result save(Empresa empresa, PessoaEndereco pessoaEndereco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if (empresa == null)
				return new Result(-1, "Erro ao tentar recuperar dados da empresa!");

			Result result = new Result(0);
			//Empresa
			if(empresa.getCdEmpresa() <= 0)	{
				empresa.setCdEmpresa(EmpresaDAO.insert(empresa, connect));
				if (empresa.getCdEmpresa() <= 0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(empresa.getCdEmpresa(), "Erro ao tentar salvar dados da empresa");
				}
			}
			else {
				result.setCode(EmpresaDAO.update(empresa, connect));
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					result.setMessage("Erro ao tentar atualizar dados da empresa!");
					return result;
				}
			}
			//Endereço
			if(pessoaEndereco != null){
				if(pessoaEndereco.getCdPessoa() <= 0)
					pessoaEndereco.setCdPessoa(empresa.getCdEmpresa());
				//
				if(pessoaEndereco.getCdEndereco() <= 0)	{
					pessoaEndereco.setCdEndereco(PessoaEnderecoDAO.insert(pessoaEndereco, connect));
					result.setCode(pessoaEndereco.getCdEndereco());
				}
				else
					result.setCode(PessoaEnderecoDAO.update(pessoaEndereco, connect));

				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					result.setMessage("Erro ao tentar salvar dados do endereço!");
					return result;
				}
			}

			if(result.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			result.addObject("empresa", empresa);
			result.addObject("endereco", pessoaEndereco);
			result.addObject("cdEmpresa", empresa.getCdEmpresa());
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

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, " +
						   "G.nm_razao_social, G.nr_cnpj, G.nr_inscricao_estadual, G.nr_inscricao_municipal, " +
				           "G.nr_funcionarios, G.dt_inicio_atividade, G.cd_natureza_juridica, G.tp_empresa, " +
				           "H.nm_pessoa AS nm_fantasia, H.nr_telefone1, H.nr_telefone2, H.nr_fax, " +
				           "H.nm_email, H.nm_url, " +
				           "I.cd_endereco, I.ds_endereco, I.cd_tipo_logradouro, I.cd_tipo_endereco, I.cd_logradouro, " +
				           "I.cd_bairro, I.cd_cidade, I.nm_logradouro, I.nm_bairro, I.nr_cep, I.nr_endereco, " +
				           "I.nm_complemento, I.nr_telefone, I.nm_ponto_referencia, " +
						   "B.*, A.cd_empresa AS cd_empresa, " +
				           "C.nm_cidade, C.id_ibge, D.id_fpas, E.id_terceiros, E.pr_terceiros, " +
				           "F.nr_mes, F.nr_ano, F.tp_folha_pagamento, F.st_folha_pagamento " +
				           "FROM grl_empresa A " +
				           "LEFT OUTER JOIN grl_pessoa H ON (A.cd_empresa = H.cd_pessoa) " +
                           "LEFT OUTER JOIN grl_pessoa_juridica G ON (A.cd_empresa  = G.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa_endereco I ON (A.cd_empresa = I.cd_pessoa AND I.lg_principal = 1) " +
				           "LEFT OUTER JOIN grl_cidade C ON (I.cd_cidade = C.cd_cidade) " +
				           "LEFT OUTER JOIN srh_empresa B ON (A.cd_empresa = B.cd_empresa) "+
				           "LEFT OUTER JOIN srh_tabela_fpas D ON (B.cd_fpas = D.cd_fpas) " +
				           "LEFT OUTER JOIN srh_tabela_terceiro E ON (B.cd_fpas = E.cd_fpas " +
				           "                                      AND B.cd_terceiros = E.cd_terceiros) " +
				           "LEFT OUTER JOIN flp_folha_pagamento F ON (B.cd_folha_pagamento = F.cd_folha_pagamento)",
				           criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
