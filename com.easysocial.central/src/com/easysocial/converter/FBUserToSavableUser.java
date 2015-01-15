package com.easysocial.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.downloaded.FBUser;
import com.easysocial.types.packed.FBSavableUser;
import com.easysocial.types.packed.SavableObject;
import com.restfb.types.FacebookType;
import com.restfb.types.User;

public class FBUserToSavableUser implements Converter{

	@Override
	public Collection<SavableObject> convert(DownloadedObject obj) {
		FBUser t = (FBUser) obj;
		FBSavableUser user = new FBSavableUser();
		user.first_name = t.getFirstName();
		user.last_name = t.getLastName();
		user.id = t.getId();
		user.locale = t.getLocale();
		user.male = t.getGender();
		user.name = t.getName();
		user.isMe = t.isMe()?"true":"false";
		List<SavableObject> result = new ArrayList<SavableObject>();
		result.add(user);
		return result;
	}
}
