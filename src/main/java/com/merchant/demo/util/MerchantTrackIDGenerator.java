package com.merchant.demo.util;

import java.security.SecureRandom;

public class MerchantTrackIDGenerator
{
	
	public synchronized static String getMerchantTrackID()
	{
		SecureRandom random = new SecureRandom();
        StringBuilder resultTrackId = new StringBuilder();
        String dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(dic.length());
            resultTrackId.append(dic.charAt(index));
        }
		return resultTrackId.toString();
	}

}
