package com.easysocial.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.downloaded.FBOwner;
import com.easysocial.types.packed.FBSavableFriendship;
import com.easysocial.types.packed.SavableObject;
import com.restfb.types.FacebookType;


public class FBOwnerToSavableFriendship implements Converter{


	@Override
	public Collection<SavableObject> convert(DownloadedObject obj) {
		FBOwner owner = (FBOwner)obj;
		List<SavableObject> result = new ArrayList<SavableObject>();
		FBSavableFriendship friendship = new FBSavableFriendship();
		friendship.id1 = owner.getOwner();
		friendship.id2 = owner.getId();
		result.add(friendship);
		return result;
	}
	
}
