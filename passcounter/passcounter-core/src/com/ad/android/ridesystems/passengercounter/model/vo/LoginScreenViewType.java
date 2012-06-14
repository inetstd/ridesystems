package com.ad.android.ridesystems.passengercounter.model.vo;

import java.util.Arrays;

public enum LoginScreenViewType {
	USERNAME,
	USERNAME_PASSWORD;
	
	public static int indexOf (LoginScreenViewType l) {
		return Arrays.asList(LoginScreenViewType.values()).indexOf(l);
	}
	
}
	