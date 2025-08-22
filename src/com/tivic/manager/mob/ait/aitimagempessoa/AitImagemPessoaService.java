package com.tivic.manager.mob.ait.aitimagempessoa;

import com.tivic.manager.mob.ait.aitpessoa.AitPessoa;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class AitImagemPessoaService implements IAitImagemPessoaService {
	
	private AitImagemPessoaRepository aitImagemPessoaRepository;
	
	public AitImagemPessoaService() throws Exception {
		aitImagemPessoaRepository = (AitImagemPessoaRepository) BeansFactory.get(AitImagemPessoaRepository.class);
	}

	@Override
	public void insert(AitImagemPessoa aitImagemPessoa) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(aitImagemPessoa, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void insert(AitImagemPessoa aitImagemPessoa, CustomConnection customConnection) throws Exception {
		aitImagemPessoaRepository.insert(aitImagemPessoa, customConnection);		
	}

	@Override
	public void update(AitImagemPessoa aitImagemPessoa) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(aitImagemPessoa, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void update(AitImagemPessoa aitImagemPessoa, CustomConnection customConnection) throws Exception {
		aitImagemPessoaRepository.update(aitImagemPessoa, customConnection);		
	}

	@Override
	public void insertImagePessoaSync(AitPessoa aitPessoa) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		 try {
			 customConnection.initConnection(true);
			 insertImagePessoaSync(aitPessoa, customConnection);
			 customConnection.finishConnection();
		 } finally {
			 customConnection.closeConnection();
		 }
		
	}

	@Override
	public void insertImagePessoaSync(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception {
		if(aitPessoa.getImagens() != null && !aitPessoa.getImagens().isEmpty()) {			
			for(AitImagemPessoa aitImagempessoa: aitPessoa.getImagens()) {
				aitImagempessoa.setCdAitPessoa(aitImagempessoa.getCdAitPessoa());			
				aitImagemPessoaRepository.insert(aitImagempessoa, customConnection);
			}
		}
	}
}
