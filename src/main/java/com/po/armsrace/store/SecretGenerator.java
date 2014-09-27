package com.po.armsrace.store;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SecretGenerator {

	public static String nextSessionId() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

}
