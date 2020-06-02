package v3nue.application.service.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import v3nue.core.service.ServiceResult;
import v3nue.core.utils.Constants;
import v3nue.core.utils.StringUtil;

@Service
public class FileService {

	public static ServiceResult<String> uploadFile(MultipartFile file) {
		if (file.isEmpty()) {
			return new ServiceResult<String>(null, new HashMap<>(Map.of("id", "NOT FOUND")), 400);
		}
		
		String filename = new Date().getTime() + '-' + StringUtil.hash(file.getOriginalFilename()) + '.'
				+ FilenameUtils.getExtension(file.getOriginalFilename());

		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(Constants.IMAGE_FILE_PATH + filename);

			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();

			return new ServiceResult<String>(null, new HashMap<>(Map.of("Server", "FAILED")), 500);
		}

		return new ServiceResult<String>(filename, new HashMap<>(Map.of("Server", "OK")), 200);
	}

	public static byte[] getImageBytes(String filename) throws IOException {
		File file = new File(Constants.IMAGE_FILE_PATH + filename);

		if (!file.exists()) {
			return null;
		}

		byte[] data = Files.readAllBytes(file.toPath());

		return data;
	}
}