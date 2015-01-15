package com.easysocial.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.downloaded.FBStatus;
import com.easysocial.types.packed.FBSavableLike;
import com.easysocial.types.packed.SavableObject;
import com.restfb.types.FacebookType;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.StatusMessage;

public class FBStatusToSavableLike implements Converter{

	@Override
	public Collection<SavableObject> convert(DownloadedObject obj) {
		FBStatus t = (FBStatus)obj;

		List<SavableObject> likes = new ArrayList<SavableObject>();
		List<NamedFacebookType> _likes = t.getLikes();
		String id = t.getId();
		for(NamedFacebookType _like:_likes){
			FBSavableLike like = new FBSavableLike();
			like.category = "status";
			like.created_time = null;
			like.name = t.getMessage();
			like.id = id;
			like.from_id = _like.getId();
			likes.add(like);
		}
		
		return likes;
	}
}
