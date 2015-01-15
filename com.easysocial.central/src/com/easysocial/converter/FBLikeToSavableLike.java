package com.easysocial.converter;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.downloaded.FBLike;
import com.easysocial.types.packed.FBSavableLike;
import com.easysocial.types.packed.SavableObject;


public class FBLikeToSavableLike implements Converter{

	@Override
	public Collection<SavableObject> convert(DownloadedObject obj) {
		FBLike t = (FBLike) obj;
		List<SavableObject> likes = new ArrayList<SavableObject>();
		FBSavableLike like = new FBSavableLike();
		like.category = t.getCategory();
		like.created_time = (t.getCreatedTime()!=null)?new Long(t.getCreatedTime().
				getTime()).toString():null;
		like.id = t.getId();
		like.name = t.getName();
		like.from_id = t.getOwner();
		likes.add(like);

		return likes;
	}
	
}
