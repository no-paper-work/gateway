/**
 * @package com.nopaper.work.gateway.util -> gateway
 * @author saikatbarman
 * @date 2025 12-Jul-2025 1:27:20 am
 * @git 
 */
package com.nopaper.work.gateway.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

import org.springframework.stereotype.Service;

/**
 * 
 */

import lombok.SneakyThrows;

@Service
public class ObjectHelper {
	@SneakyThrows
	public String toStringBase64(Serializable serializable) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(serializable);
		objectOutputStream.close();
		return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
	}

	@SneakyThrows
	public Object fromStringBase64(String base64String) {
		byte[] bytes = Base64.getDecoder().decode(base64String);
		ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		return object;
	}
}