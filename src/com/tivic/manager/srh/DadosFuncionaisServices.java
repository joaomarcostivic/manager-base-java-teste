package com.tivic.manager.srh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.util.LogUtils;

public class DadosFuncionaisServices {

	public static final int sfDEMITIDO = 0;
	public static final int sfATIVO    = 1;
	public static final int sfAPOSENTADO 		 = 2;
	public static final int sfAPOSENTADO_PROPRIO = 3;

	public static String[] situacaoFuncional = {"Desligado","Ativo","Aposentado","Aposentado Próprio"};

	public static String[] tipoSalario = {"Mensal","Quinzenal","Semanal","Diarista","Horista","Por projeto"};

	public static String[] valeTransporte = {"Não optante","Optante"};

	public static String[] tipoProventoPrincipal = {"Tabela de salário","Valor contratual","Plano de cargos"};

	public static String[] tipoPagamento = {"Depósito em conta","Cheque","Dinheiro"};

	public static Result save(DadosFuncionais dadosFuncionais){
		return save(dadosFuncionais, null);
	}
	
	public static Result save(DadosFuncionais dadosFuncionais, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//System.out.println("\ndadosfuncionais = "+ dadosFuncionais);
			if(dadosFuncionais==null)
				return new Result(-1, "Erro ao salvar. Dados Funcionais enviados são nulos");
			
			Pessoa pessoa = PessoaDAO.get(dadosFuncionais.getCdPessoa(), connect);
			
			//System.out.println("\npessoa = "+ pessoa);
			if(pessoa==null)
				return new Result(-2, "Erro ao salvar. Pessoa indicada não existe");
			int retorno;
			
			LogUtils.debug("DadosFuncionais.save");
			LogUtils.debug("dadosFuncionais: "+dadosFuncionais);
			
			if(dadosFuncionais.getCdMatricula()==0){
				//System.out.println("\ninsert - dados funcionais\n");
				retorno = DadosFuncionaisDAO.insert(dadosFuncionais, connect);
				dadosFuncionais.setCdMatricula(retorno);
			}
			else {
				//System.out.println("\nupdate - dados funcionais\n");
				retorno = DadosFuncionaisDAO.update(dadosFuncionais, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DADOSFUNCIONAIS", dadosFuncionais);
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
	
	public static Result remove(int cdMatricula){
		return remove(cdMatricula, false, null);
	}
	
	public static Result remove(int cdMatricula, boolean cascade){
		return remove(cdMatricula, cascade, null);
	}
	
	public static Result remove(int cdMatricula, boolean cascade, Connection connect){
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
				retorno = DadosFuncionaisDAO.delete(cdMatricula, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este funcionario está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Funcionário excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir setor!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insertAndUpdate(Pessoa pessoa, int cdEmpresa, int cdVinculo, int cdVinculoOld, DadosFuncionais dadosFuncionais) {
		return insertAndUpdate(pessoa, null, cdEmpresa, cdVinculo, cdVinculoOld, dadosFuncionais);
	}
	public static Result insertAndUpdate(Pessoa pessoa, PessoaEndereco endereco, int cdEmpresa, int cdVinculo, int cdVinculoOld, DadosFuncionais dadosFuncionais) {
		Connection connect = Conexao.conectar();
		try {
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
			//
			boolean isUpdate = pessoa.getCdPessoa() > 0;
			Result result = new Result(0);
			if(isUpdate) 
				result = PessoaServices.update(pessoa, endereco, null, cdEmpresa, cdVinculo, cdVinculoOld, null, 0, null, 0/*cdTipoDocumento*/, null/*nrDocumento*/, connect);
			else	{
				result = PessoaServices.insert(pessoa, endereco, null, cdEmpresa, cdVinculo, null, 0, null, 0/*cdTipoDocumento*/, null/*nrDocumento*/, connect);
				pessoa.setCdPessoa(result.getCode());
			}	
			
			if(result.getCode() <= 0) {
				Conexao.rollback(connect);
				return result;
			}

			dadosFuncionais.setCdPessoa(pessoa.getCdPessoa());
			if (dadosFuncionais.getCdMatricula() > 0)
				result.setCode(DadosFuncionaisDAO.update(dadosFuncionais, connect));
			else
				result.setCode(DadosFuncionaisDAO.insert(dadosFuncionais, connect));

			if(result.getCode() <= 0) {
				Conexao.rollback(connect);
				result.setMessage("Erro ao tentar atualizar dados funcionais! [ERRO: "+result.getCode()+"]");
				return result;
			}
				
			connect.commit();
			
			result.addObject("cdMatricula", dadosFuncionais.getCdMatricula());
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar incluir ou atualizar dados do colaborador!");
		}
		finally {
			Conexao.rollback(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, ArrayList<String> groupBy) {
		return find(criterios, groupBy, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, ArrayList<String> groupBy, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(groupBy==null)
				groupBy = new ArrayList<String>();
			if (isConnectionNull)
				connect = Conexao.conectar();
			String groups = "";
			int cdEmpresa = 0;
			for (int i=0; criterios!= null && i<criterios.size(); i++)
				if (criterios.get(i).getColumn().toUpperCase().indexOf("CD_EMPRESA")!=-1) {
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					break;
				}
			//System.out.println(criterios);
			String fields = " A.*, B.nm_pais, E.*, Q.sg_estado AS sg_estado_rg, F.*, K.nm_razao_social AS nm_empresa, " +
			 			  	" H.*, I.nr_banco, I.nm_banco, " +
			 			  	" J.nm_tipo_desligamento, " +
			 			  	" L.id_setor, L.nm_setor, " +
			 			  	" M.nm_cidade AS nm_naturalidade, " +
			 			  	" N.nm_escolaridade, N.id_escolaridade, " +
			 			  	" O.nm_funcao, P.nm_vinculo_empregaticio, R.nm_tipo_admissao, S.nm_tabela_horario, " +
			 			  	" A.cd_pessoa AS cd_pessoa " ;
			// Processa agrupamentos enviados em groupBy
			String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups, "COUNT(*) AS QT_COLABORADOR");
			fields = retorno[0];
			groups = retorno[1];

			String sql =
 			  "SELECT "+ fields + " " +
			  "FROM grl_pessoa A " +
			  /* Pessoa */
			  "LEFT OUTER JOIN grl_pais 			B ON (A.cd_pais = B.cd_pais) " +
			  "LEFT OUTER JOIN grl_forma_divulgacao C ON (A.cd_forma_divulgacao = C.cd_forma_divulgacao) " +
			  "LEFT OUTER JOIN adm_classificacao 	D ON (A.cd_classificacao = D.cd_classificacao) " +
			  "JOIN grl_pessoa_fisica 				E ON (A.cd_pessoa = E.cd_pessoa) " +
			  /* Pessoa Física */
	      	  "LEFT OUTER JOIN grl_cidade 			M ON (E.cd_naturalidade = M.cd_cidade) " +
	          "LEFT OUTER JOIN grl_escolaridade 	N ON (E.cd_escolaridade = N.cd_escolaridade) " +
	      	  "LEFT OUTER JOIN grl_estado 			Q ON (Q.cd_estado = E.cd_estado_rg) " +
			  /* Dados Funcionais */
			  "LEFT OUTER JOIN srh_dados_funcionais 	 F ON (A.cd_pessoa = F.cd_pessoa " +
			  									(cdEmpresa>0 ? "AND F.cd_empresa = " + cdEmpresa  : "")+ ") " +
			  "LEFT OUTER JOIN grl_empresa 				 G ON (F.cd_empresa = G.cd_empresa) " +
			  "LEFT OUTER JOIN grl_pessoa_juridica 		 K ON (G.cd_empresa  = K.cd_pessoa) " +
			  "LEFT OUTER JOIN grl_pessoa_conta_bancaria H ON (F.cd_pessoa = H.cd_pessoa " +
			  "                                            AND F.cd_conta_bancaria = H.cd_conta_bancaria) " +
			  "LEFT OUTER JOIN grl_banco 			 I ON (H.cd_banco = I.cd_banco) " +
			  "LEFT OUTER JOIN srh_tipo_desligamento J ON (F.cd_tipo_desligamento = J.cd_tipo_desligamento) " +
			  "LEFT OUTER JOIN grl_setor 			 L ON (F.cd_setor = L.cd_setor) " +
			  "LEFT OUTER JOIN srh_funcao 			 O ON (F.cd_funcao = O.cd_funcao) "+
			  "LEFT OUTER JOIN srh_vinculo_empregaticio P ON (F.cd_vinculo_empregaticio = P.cd_vinculo_empregaticio) " +
			  "LEFT OUTER JOIN srh_tipo_admissao     R ON (F.cd_tipo_admissao = R.cd_tipo_admissao) " +
			  "LEFT OUTER JOIN srh_tabela_horario    S ON (F.cd_tabela_horario = S.cd_tabela_horario) ";
				
			  if (ItemComparator.getItemComparatorByColumn(criterios, "PE.CD_VINCULO") != null) {
				  sql += " JOIN grl_pessoa_empresa PE ON (A.cd_pessoa = PE.cd_pessoa AND PE.cd_empresa = "+cdEmpresa+") ";
			  }
			
			  ResultSetMap rsm = Search.find(sql, groups+(groupBy.size()==0?" ORDER BY A.nm_pessoa":""), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			  return rsm;

		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setSituacaoFuncional(Connection connect, int cdMatricula, int stFuncional,
			int cdDesligamento, GregorianCalendar dtDesligamento)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"UPDATE srh_dados_funcionais " +
					"SET cd_desligamento=?, dt_desligamento=?, st_funcional=? "+
					"WHERE cd_matricula=? ");
			if(cdDesligamento==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1, cdDesligamento);
			if(dtDesligamento==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2, new Timestamp(dtDesligamento.getTimeInMillis()));
			pstmt.setInt(3, stFuncional);
			pstmt.setInt(4, cdMatricula);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static ResultSetMap getAllFuncionariosSetor(int cdSetor) {
		return getAllFuncionariosSetor(cdSetor, null);
	}
	
	public static ResultSetMap getAllFuncionariosSetor(int cdSetor, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM srh_dados_funcionais A, grl_pessoa B " +
															 "WHERE A.cd_pessoa   = B.cd_pessoa " +
															 "  AND A.cd_setor  = "+cdSetor).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getByProfessor(int cdProfessor, int cdInstituicao) {
		return getByProfessor(cdProfessor, cdInstituicao, null);
	}
	
	public static ResultSetMap getByProfessor(int cdProfessor, int cdInstituicao, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM srh_dados_funcionais A, srh_lotacao B, grl_setor C " +
															 "WHERE A.cd_matricula = B.cd_matricula "+ 
															 "  AND B.cd_setor = C.cd_setor"+
															 "  AND A.cd_pessoa = "+cdProfessor+
															 "  AND C.cd_empresa = "+cdInstituicao+
															 "  AND C.nm_setor = 'CORPO DOCENTE'").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result setSetor(int cdPessoa, int cdSetor, int cdEmpresa, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			Pessoa pessoa = PessoaDAO.get(cdPessoa, connect);
			if(pessoa == null) {
				return new Result(-1, "Pessoa indicada não está cadastrada.");
			}
						
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT cd_matricula FROM srh_dados_funcionais WHERE cd_pessoa=? AND cd_empresa=? ");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdEmpresa);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				
				pstmt = connect.prepareStatement(
						"UPDATE srh_dados_funcionais " +
						"SET cd_setor=? WHERE cd_matricula=? ");
				pstmt.setInt(1, cdSetor);
				pstmt.setInt(2, rs.getInt("CD_MATRICULA"));
				
				if(pstmt.executeUpdate()>0) {
					return new Result(1, "Funcionário vinculado ao setor com sucesso.");
				}
				else {
					return new Result(-2, "Erro ao atualizar Dados Funcionais.");
				}
			}
			else {
				DadosFuncionais dadosFuncionais = new DadosFuncionais(0, //cdMatricula, 
						0, //cdTabelaHorario, 
						cdSetor, 
						0, //cdFuncao, 
						0, //cdTurma, 
						cdEmpresa, 
						0, //cdGrupoPagamento, 
						0, //cdAgenteNocivo, 
						0, //cdTipoAdmissao, 
						0, //cdVinculoEmpregaticio, 
						0, //cdCategoriaFgts, 
						0, //cdTabelaSalario, 
						cdPessoa, 
						0, //cdContaBancaria, 
						0, //tpSalario, 
						null, //nrMatricula, 
						null, //dtMatricula, 
						null, //dtDesligamento, 
						null, //nrCartao, 
						(float)0, //vlPrevidenciaOutraFonte, 
						(float)0, //vlSalarioContratual, 
						0, //qtLicencasGozadas, 
						0, //qtFeriasGozadas, 
						null, //dtOpcaoFgts, 
						1, //stFuncional, 
						0, //tpStatusRais, 
						0, //tpProventoPrincipal, 
						null,//nrContaFgts, 
						0, //cdConvenio, 
						null, //dtFinalContrato, 
						0, //tpPagamento, 
						0, //cdTipoDesligamento, 
						0, //cdBancoFgts, 
						null, //nrAgenciaFgts, 
						null, //blbBiometria, 
						0, //qtDependenteIr, 
						0, //qtDependenteSalFam, 
						0, //lgValeTransporte, 
						0,
						0,
						null,
						null,
						null,
						0,
						0,
						0); //nrHorasMes)
				int cdMatricula = DadosFuncionaisDAO.insert(dadosFuncionais, connect);
				
				if(cdMatricula>0) {
					return new Result(1, "Funcionário vinculado ao setor com sucesso.");
				}
				else {
					return new Result(-3, "Erro ao inserir Dados Funcionais");
				}
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao atualizar setor do funcionario.");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSetorOf(int cdEmpresa, int cdPessoa) {
		return getSetorOf(cdEmpresa, cdPessoa, null);
	}
	
	public static ResultSetMap getSetorOf(int cdEmpresa, int cdPessoa, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
						
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.cd_setor, B.nm_setor, B.nr_setor_externo, B.sg_setor "+
															 "FROM srh_dados_funcionais A, grl_setor B " +
															 "WHERE A.cd_setor   = B.cd_setor " +
															 "  AND A.cd_empresa = "+cdEmpresa+
															 "  AND A.cd_pessoa  = "+cdPessoa).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}