package org.openo.nfvo.vimadapter.service.dao.inf;

import java.util.List;

import org.openo.nfvo.vimadapter.service.entity.Vim;

public interface VimDao {

	public List <Vim> indexVims(int i, int j);
	int getVimByUpdateName(String name, String vimId);

}
