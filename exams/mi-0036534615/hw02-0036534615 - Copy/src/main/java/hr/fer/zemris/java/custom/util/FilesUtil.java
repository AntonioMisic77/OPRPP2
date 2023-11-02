package hr.fer.zemris.java.custom.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesUtil {

	public static String readFromFile(String filePath) throws IOException {
		
		byte[] bytes = Files.readAllBytes(Paths.get(filePath));
		return new String(bytes,StandardCharsets.UTF_8);
	}
}
