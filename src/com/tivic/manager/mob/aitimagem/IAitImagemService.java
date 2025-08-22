package com.tivic.manager.mob.aitimagem;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitImagem;
import com.tivic.sol.connection.CustomConnection;
import java.util.List;
import com.tivic.sol.search.SearchCriterios;

public interface IAitImagemService {
	AitImagem getFromAit(int cdAit) throws Exception;
	public void insertImageSync(Ait ait) throws Exception;
	public void insertImageSync(Ait ait, CustomConnection customConnection) throws Exception;
	public AitImagem insert(AitImagem aitImagem) throws Exception;
	public AitImagem insert(AitImagem aitImagem, CustomConnection customConnection) throws Exception;
	public AitImagem update(AitImagem aitImagem) throws Exception;
	public AitImagem update(AitImagem aitImagem, CustomConnection customConnection) throws Exception;
	public AitImagem get(int cdEvento, int cdAit) throws Exception;
	public AitImagem get(int cdEvento,int cdAit, CustomConnection customConnection) throws Exception;
	public List<AitImagem> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitImagem> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
