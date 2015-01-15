package com.easysocial.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.downloaded.FBStatus;
import com.easysocial.types.packed.FBSavableStatus;
import com.easysocial.types.packed.SavableObject;
import com.restfb.types.FacebookType;
import com.restfb.types.StatusMessage;

public class FBStatusToSavableStatus implements Converter{

	@Override
	public Collection<SavableObject> convert(DownloadedObject obj) {
		FBStatus status = (FBStatus)obj;
		List<SavableObject> statuses = new ArrayList<SavableObject>();
		FBSavableStatus savableStatus = new FBSavableStatus();
		savableStatus.from_id = status.getFrom().getId();
		savableStatus.id = status.getId();
		savableStatus.message = status.getMessage();
		savableStatus.update_time = new Long(status.getUpdatedTime().getTime()).toString();
		statuses.add(savableStatus);
		return statuses;
	}
	
}