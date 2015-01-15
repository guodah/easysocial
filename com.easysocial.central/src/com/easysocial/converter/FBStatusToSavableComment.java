package com.easysocial.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.downloaded.FBStatus;
import com.easysocial.types.packed.FBSavableComment;
import com.easysocial.types.packed.SavableObject;
import com.restfb.types.Comment;
import com.restfb.types.FacebookType;
import com.restfb.types.StatusMessage;

public class FBStatusToSavableComment implements Converter{


	@Override
	public Collection<SavableObject> convert(DownloadedObject obj) {
		FBStatus status = (FBStatus)obj;
		List<SavableObject> comments = new ArrayList<SavableObject>();
		List<Comment> _comments = status.getComments();
		String id = status.getFrom().getId();
		for(Comment _comment:_comments){
			FBSavableComment comment = new FBSavableComment();
			comment.id = _comment.getId();
			comment.created_time = new Long(_comment.getCreatedTime().
					getTime()).toString();
			comment.from_id = _comment.getFrom().getId();
			comment.target_type = "status";
			comment.target_id = status.getId();
			comment.content = _comment.getMessage();
			comments.add(comment);
		}
		
		return comments;
	}
}