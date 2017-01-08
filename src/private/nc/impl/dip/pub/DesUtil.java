package nc.impl.dip.pub;
import java.io.File;

import com.spt.bank.platform.utils.crypt.BytesHelper;
import com.spt.bank.platform.utils.crypt.cipher.ThreeDes;
import com.spt.bank.platform.utils.crypt.hash.HashFile;
import com.spt.bank.platform.utils.crypt.hash.HashFileFactory;
import com.spt.bank.platform.utils.pkg.IPackage;
import com.spt.bank.platform.utils.pkg.zip.ZipImpl;
/**
 * 上海邮政提供的加密类
 * @author admin
 *
 */
public class DesUtil {
	private static final byte[] key = BytesHelper.revert("A2F167FDBCBF2A0D70B0FDD5C81CF8C71991409D2C2F1CE3");
	public static String encrypt(String str){
		
		byte[] encBytes = null;
		try{
			byte[] srcBytes = str.getBytes("gbk");
		//BytesHelper.toHexWellFormat(srcBytes, -1, System.out);
			encBytes = ThreeDes.encryptMode(key, srcBytes);
		
		}catch(Exception e){
			
		}
		return BytesHelper.toHex(encBytes);
	}
	
	public static String decrypt(String str){
		byte[] decBytes = null;
		try{
			byte[] encBytes = BytesHelper.revert(str);
			decBytes = ThreeDes.decryptMode(key, encBytes);
		}catch(Exception e){
			
		}
		return new String(decBytes);
	}
	
	public static String getMD5Code(String file){
		String md5code = null;
		try{
			HashFile hash = HashFileFactory.getInstance().getHash("md5", file);
			md5code = BytesHelper.toHex(hash.getHashValue());
		}catch(Exception e){
			
		}
		return md5code;

	}
	
	public static boolean unZipFile(String zipName,String outPath){
		boolean result = false;
		IPackage pkg= new ZipImpl();
		try{
			pkg.unpack(zipName,outPath);
			File f = new File(zipName);
			f.delete();
			result = true;
		}catch(Exception e){
			
		}
		
		return result;
	}

}
