package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.log.Sistema;
import com.tivic.manager.log.SistemaDAO;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class OrgaoServices {
	
	public static Result save(Orgao orgao){
		return save(orgao, null, null, 0, 0, null);
	}
	
	public static Result save(Orgao orgao, Connection connect){
		return save(orgao, null, null, 0, 0, connect);
	}
	
	public static Result save(Orgao orgao, PessoaJuridica pessoa, PessoaEndereco pessoaEndereco, int cdEmpresa, int cdVinculo){
		return save(orgao, pessoa, pessoaEndereco, cdEmpresa, cdVinculo, null);
	}
	
	public static Result save(Orgao orgao, PessoaJuridica pessoa, PessoaEndereco pessoaEndereco, int cdEmpresa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(orgao==null)
				return new Result(-1, "Erro ao salvar. Órgão é nulo");
			
			int retorno = 1;

			/*
			 * Se o orgão não existir, cria a pessoa e o orgão.
			 * Caso contário, apenas atualiza o orgão
			 */
			if(orgao.getCdOrgao()<=0) {
				// cria a pessoa do orgão, com vínculo CORRESPONDENTE
				Result r = com.tivic.manager.grl.PessoaServices.save(pessoa, pessoaEndereco, cdEmpresa, cdVinculo); 
				retorno = r.getCode();
				
				if(retorno<=0) {
					Conexao.rollback(connect);
					new Result(-2, "Erro ao salvar o registro de pessoa vinculado ao Órgão");
				}

				int cdPessoa = ((Pessoa)r.getObjects().get("PESSOA")).getCdPessoa();
				orgao.setCdPessoa(cdPessoa);
				
				retorno = OrgaoDAO.insert(orgao, connect);
			}
			else {				
				retorno = OrgaoDAO.update(orgao, connect);
			}
			
			if(retorno<=0) {
				Conexao.rollback(connect);
				new Result(-3, "Erro ao salvar Orgão");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORGAO", orgao);
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
	
	public static Result remove(int cdOrgao){
		return remove(cdOrgao, false, null);
	}
	
	public static Result remove(int cdOrgao, boolean cascade){
		return remove(cdOrgao, cascade, null);
	}
	
	public static Result remove(int cdOrgao, boolean cascade, Connection connect){
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
				PreparedStatement pstmt = null;
				int cdPessoa = OrgaoDAO.get(cdOrgao, connect).getCdPessoa();
				
				//excluir orgão
				retorno = OrgaoDAO.delete(cdOrgao, connect);
				
				//excluir pessoa_empresa
				if(retorno>0) {
					pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_empresa WHERE cd_pessoa = "+cdPessoa);
					retorno = pstmt.executeUpdate();
				}
				
				//excluir pessoa_endereco
				if(retorno>0) {
					pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_endereco WHERE cd_pessoa = "+cdPessoa);
					retorno = pstmt.executeUpdate();
				}
					
				//excluir pessoa_juridica
				if(retorno>0)
					retorno = PessoaJuridicaDAO.delete(cdPessoa, connect);
				
				//excluir pessoa
				if(retorno>0)
					retorno = PessoaDAO.delete(cdPessoa, connect);
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este órgão está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Órgão excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir órgão!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result autenticar(String nmLogin, String nmSenha) {
		return autenticar(nmLogin, nmSenha, null);
	}

	public static Result autenticar(String nmLogin, String nmSenha, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_usuario " +
															   "WHERE nm_login	 = ? " +
															   "  AND st_usuario = 1");
			pstmt.setString(1, nmLogin);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				Usuario usuario = new Usuario(rs.getInt("cd_usuario"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_pergunta_secreta"),
						rs.getString("nm_login"),
						rs.getString("nm_senha"),
						rs.getInt("tp_usuario"),
						rs.getString("nm_resposta_secreta"),
						rs.getInt("st_usuario"));
				
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
				//TODO Remover teste de loginHash após atualização do banco
				int lgLoginHash = ParametroServices.getValorOfParametroAsInteger("LG_LOGIN_HASH", 0, 0, connect);
				if(lgLoginHash > 0)
					nmSenha = UsuarioServices.getPasswordHash(nmSenha);
				if(nmSenha.equals(usuario.getNmSenha())){
					Result r = new Result(usuario.getCdUsuario(), "Autenticado com sucesso...");
					
					Empresa empresa = EmpresaServices.getDefaultEmpresa(connect);
					
					r.addObject("USUARIO", usuario);
					r.addObject("PESSOA", pessoa);
					r.addObject("EMPRESA", empresa);
					
					ResultSetMap rsmOrgaos = new ResultSetMap();
					rsmOrgaos = UsuarioOrgaoServices.getAllByUsuario(usuario.getCdUsuario(), connect);
					
					if(rsmOrgaos==null || rsmOrgaos.getLines().size()==0)
						return new Result(-4, "Este usuário não está vinculado à nenhum correspondente.");
					
					r.addObject("ORGAOS", rsmOrgaos);
					
					
					/* REGISTRAR LOG */
					if(usuario!=null && pessoa!=null){
						GregorianCalendar dtLog = new GregorianCalendar();
						String remoteIP = null;
						String txtExecucao = "Autenticação de acesso ao módulo de correspondente às " + Util.formatDateTime(dtLog, "dd/MM/yyyy HH:mm:ss", "") + " por " +
							pessoa.getNmPessoa() + ", através da conta " + usuario.getNmLogin() +
							((remoteIP!=null)?", IP "+remoteIP:"");
						
						Sistema log = new Sistema(0, //cdLog
								dtLog, //dtLog
								txtExecucao, //txtLog
								com.tivic.manager.log.SistemaServices.TP_LOGIN, //tpLog
								usuario.getCdUsuario());//cdUsuario
						SistemaDAO.insert(log, connect);
					}
					
					return r;
				}
				else
					return new Result(-2, "Senha inválida...");
			}
			else
				return new Result(-1, "Login inválido...");
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-3, "Erro ao autenticar...");
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
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa as nm_responsavel, B.nm_email, C.nm_pessoa" + 
							" FROM prc_orgao A " +
							" LEFT OUTER JOIN grl_pessoa B ON (A.cd_responsavel = B.cd_pessoa) " +
							" LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) " +
							" WHERE C.st_cadastro="+PessoaServices.ST_ATIVO +
							" ORDER BY nm_orgao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca a lista de correspondentes/órgãos de uma cidade.
	 * Se a cidade tiver apenas um principal, ele é retornado. Caso contrários,
	 * todos os correspondentes da codade são retornados, ordenados por LG_PRINCIPAL.
	 * 
	 * @param cdCidade código da cidade
	 * @return ResultSetMap Lista dos órgãos
	 * 
	 * @author Maurício
	 * @since 21/09/2015
	 */
	public static ResultSetMap getOrgaoByCidade(int cdCidade) {
		return getOrgaoByCidade(cdCidade, null);
	}
	
	public static ResultSetMap getOrgaoByCidade(int cdCidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_orgao, C.nm_pessoa " + 
					" FROM prc_cidade_orgao A " +
					" LEFT OUTER JOIN prc_orgao B ON (A.cd_orgao = B.cd_orgao) "+
					" LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
					" WHERE C.st_cadastro="+PessoaServices.ST_ATIVO +
					"   AND A.cd_cidade = ? ");
			pstmt.setInt(1, cdCidade);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			ResultSetMap rsmAux = new ResultSetMap();
			
			while(rsm.next()) {
				if(rsm.getInt("LG_PRINCIPAL")==1) {
					rsmAux.addRegister(rsm.getRegister());
				}
			}
			rsm.beforeFirst();
			rsmAux.beforeFirst();
			
			ArrayList<String> columns = new ArrayList<String>();
			columns.add("LG_PRINCIPAL DESC");
			rsm.orderBy(columns);
			rsm.beforeFirst();
			
			return (rsmAux.getLines().size()==1 ? rsmAux : rsm);
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
	
	public static Orgao getOrgaoByPessoa(int cdPessoa) {
		return getOrgaoByPessoa(cdPessoa, null);
	}
	
	public static Orgao getOrgaoByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_orgao WHERE cd_pessoa = ? ");
			pstmt.setInt(1, cdPessoa);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next())
				return OrgaoDAO.get(rsm.getInt("CD_ORGAO"), connect);
			else
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
	
	public static Pessoa getResponsavelByOrgao(int cdOrgao) {
		return getResponsavelByOrgao(cdOrgao, null);
	}
	
	public static Pessoa getResponsavelByOrgao(int cdOrgao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_responsavel FROM prc_orgao WHERE cd_orgao = ? ");
			pstmt.setInt(1, cdOrgao);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next())
				return PessoaDAO.get(rsm.getInt("cd_responsavel"), connect);
			else
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
	
	public static Result setResponsavel(int cdResponsavel, int cdOrgao) {
		return setResponsavel(cdResponsavel, cdOrgao, null);
	}
	
	public static Result setResponsavel(int cdResponsavel, int cdOrgao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			Orgao orgao = OrgaoDAO.get(cdOrgao, connect);
			if(orgao==null)
				return new Result(-2, "Erro ao buscar orgão.");
			
			orgao.setCdResponsavel(cdResponsavel);
			
			return OrgaoServices.save(orgao, connect);
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		String nmPessoa = null;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("C.NM_PESSOA") || criterios.get(i).getColumn().equalsIgnoreCase("A.NM_PESSOA")) {
				nmPessoa = Util.limparTexto(criterios.get(i).getValue());
				nmPessoa = "%"+nmPessoa.replaceAll(" ", "%")+"%";
				criterios.remove(i);
				i--;
			}
		}
		
		return Search.find("SELECT A.*, B.nm_pessoa as nm_responsavel, C.*, D.nr_cnpj, E.*, F.nm_cidade"+ 
							" FROM prc_orgao A " +
							" LEFT OUTER JOIN grl_pessoa B ON (A.cd_responsavel = B.cd_pessoa) " +
							" LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa AND C.st_cadastro="+PessoaServices.ST_ATIVO+") " +
							" LEFT OUTER JOIN grl_pessoa_juridica D ON (A.cd_pessoa = D.cd_pessoa) " +
							" LEFT OUTER JOIN grl_pessoa_endereco E ON (A.cd_pessoa = E.cd_pessoa AND E.lg_principal = 1)" +
							" LEFT OUTER JOIN grl_cidade F ON (E.cd_cidade = F.cd_cidade)"+
							" WHERE 1=1 "+
							(nmPessoa!=null ? 
									   (Util.getConfManager().getIdOfDbUsed().equals("FB") ? (" AND C.nm_pessoa LIKE '"+nmPessoa+"' ") :  
										   (" AND TRANSLATE(C.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmPessoa+"' "))
									:
										""),
							" ORDER BY nm_orgao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getOrgaoAgrupado(String nmAgrupamento, ArrayList<Integer> processos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT B.NM_PESSOA AS NM_AGRUPAMENTO, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / cast ((SELECT COUNT(*) FROM PRC_PROCESSO) as numeric(15,5)) * 100) AS QT_PERCENTUAL " +
					"FROM PRC_PROCESSO A " +
					(nmAgrupamento.equals("AGRUPADO_ADVOGADO_RESPONSAVEL") ? "JOIN GRL_PESSOA B ON (A.CD_ADVOGADO = B.CD_PESSOA) " : "JOIN GRL_PESSOA B ON (A.CD_ADVOGADO_CONTRARIO = B.CD_PESSOA) ") +
					(processos.size()>0 ? " WHERE A.cd_processo IN ("+Util.join(processos)+")" : "")+
					"GROUP BY B.NM_PESSOA " +
					"ORDER BY B.NM_PESSOA ");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result setTpContratacao(int tpContratacao, int cdOrgao) {
		return setTpContratacao(tpContratacao, cdOrgao, null);
	}
	
	public static Result setTpContratacao(int tpContratacao, int cdOrgao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			Orgao orgao = OrgaoDAO.get(cdOrgao, connect);
			if(orgao==null)
				return new Result(-2, "Erro ao buscar orgão.");
			
			orgao.setTpContratacao(tpContratacao);
			
			Result result = save(orgao, connect);
			if(result.getCode()>0)
				result.setMessage("Tipo de Contratação editada com sucesso.");
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "OrgaoServices.setTpContratacao: Erro ao salvar tipo de contratação.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getTpContratacao(int cdOrgao) {
		return getTpContratacao(cdOrgao, null);
	}
	
	public static Result getTpContratacao(int cdOrgao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			Orgao orgao = OrgaoDAO.get(cdOrgao, connect);
			if(orgao==null)
				return new Result(-2, "Erro ao buscar orgão.");
			
			return new Result(1, "", "TP_CONTRATACAO", orgao.getTpContratacao());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "OrgaoServices.setTpContratacao: Erro ao buscar tipo de contratação.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}