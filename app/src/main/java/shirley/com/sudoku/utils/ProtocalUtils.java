package shirley.com.sudoku.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import android.text.TextUtils;

import cn.safetrip.edog.net.RequestParams;
import shirley.com.sudoku.net.model.FileItem;

public class ProtocalUtils {
	public static final String HSKEY_2 = "aee0135dac5f5c64";
	public static final String AUTHKEY_2 = "2681ed82b006fc78";

	public static String addEncrpt(String title, String url, RequestParams params) {
		if (title == null) {
			title = "";
		}
		RequestParams temp = null; // 为了log显示参数用
		String newUrl = url;
		String encryptString = "";
		String md5Content = "";
		if (params != null && !TextUtils.isEmpty(params.getParamString())) {
			temp = params;
			// 加密并把code和token添加到url
			String post = params.getPostString();
			md5Content = MD5.hexdigest(post);

		} else {
			temp = new RequestParams();
		}
		try {
			encryptString = encrypt(md5Content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		encryptString = encryptString.toLowerCase();
		newUrl = newUrl + encryptString;
		return newUrl;
	}

	/**
	 * 功能: 加密
	 * 
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String post) throws Exception {
		String code = "" + System.currentTimeMillis() + (new Random().nextInt(260000));
		String md5content = post + HSKEY_2 + code;
		String md5String = MD5.hexdigest(md5content);
		String hs = md5String.substring(0, 16);
		String key = hs + AUTHKEY_2;

		String aescontent = md5String.substring(16);
		String es = Crypto.encrypt(key, aescontent);
		String pj = "&token=" + es + "&code=" + code;
		return pj;
	}

	private static String methodFromField(Field field, String prefix) {
		return prefix + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
	}

	/**
	 * Find and return the appropriate getter method for field.
	 * 
	 * @return Get method or null if none found.
	 */
	public static Method findGetMethod(Field field, boolean throwExceptions) {
		String methodName = methodFromField(field, "get");
		Method fieldGetMethod;
		try {
			fieldGetMethod = field.getDeclaringClass().getMethod(methodName);
		} catch (Exception e) {
			if (throwExceptions) {
				throw new IllegalArgumentException("Could not find appropriate get method for " + field);
			} else {
				return null;
			}
		}
		if (fieldGetMethod.getReturnType() != field.getType()) {
			if (throwExceptions) {
				throw new IllegalArgumentException("Return type of get method " + methodName + " does not return " + field.getType());
			} else {
				return null;
			}
		}
		return fieldGetMethod;
	}

	/**
	 * 从srcData中取出各字段作为参数加到RequestParams对象中
	 * 
	 * @param srcData
	 * @return
	 */
	public static RequestParams extractParams(Object srcData) {
		RequestParams netParams = new RequestParams();
		ArrayList<Field> reqList = new ArrayList<Field>();
		Class dataClass = srcData.getClass();

		Field[] fields = dataClass.getDeclaredFields();
		for (Field field : fields) {
			// TODO 暂时没看明白
			// if (!field.isAnnotationPresent(NoReqParams.class)) {
			// }
			reqList.add(field);
		}
		// 从data里取出要发送的参数
		for (Field reqField : reqList) {
			reqField.setAccessible(true);
			Object value = null;
			try {
				value = reqField.get(srcData);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (value != null) {
				if (value instanceof FileItem) {
					FileItem file = (FileItem) value;
					try {
						netParams.put(reqField.getName(), new FileInputStream(file.getPath()), file.getName(), file.getContentType());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					netParams.put(reqField.getName(), value.toString());
				}
			}

		}
		return netParams;
	}
}
