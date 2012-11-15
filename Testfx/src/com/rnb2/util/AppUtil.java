package com.rnb2.util;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public final class AppUtil {

	private static final String SUB_DIR_IDS_UGDT = ".javaFx_Test";
	
	/**
	 * cumpute simple file name
	 * @param prefix - file name
	 * @return
	 */
	public static final String computeFileName(String prefix){
		MessageDigest alg;
		try {
			alg = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			//throw new Exception(e);
		}
		StringBuffer fileName = new StringBuffer();
		fileName.append(System.getProperties().get("user.home")
				+ (String) System.getProperties().get("file.separator")
				+ AppUtil.SUB_DIR_IDS_UGDT
				+ (String) System.getProperties().get("file.separator")				
				+ (prefix != null ? prefix : ""));
		return fileName.toString();
	}
	
	public static final String computeFileName(String id, String prefix){
		MessageDigest alg = null;
		try {
			alg = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			//throw new Exception(e);
		}
		alg.update(id.getBytes());
		byte[] bytes = alg.digest();
		StringBuffer fileName = new StringBuffer();
		fileName.append(System.getProperties().get("user.home")
				+ (String) System.getProperties().get("file.separator")
				+ AppUtil.SUB_DIR_IDS_UGDT
				+ (String) System.getProperties().get("file.separator")				
				+ (prefix != null ? prefix : ""));
		for (byte i : bytes) {
			int v = i & 0xFF;
			if (v < 16) {
				fileName.append('0');
			}
			fileName.append(Integer.toHexString(v));
		}

		return fileName.toString();
	}
	
	public static boolean deleteFile(File file){
		boolean retVal = false;
        if(file.isDirectory())
        {
            File afile[] = file.listFiles();
            int i = afile.length;
            for(int j = 0; j < i; j++)
            {
                File file1 = afile[j];
                deleteFile(file1);
            }

        }
        retVal = file.delete();
    return retVal;
	}

}
