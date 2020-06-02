package v3nue.application.controllers;

import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import v3nue.application.service.services.FileService;

@Controller
@RequestMapping("/api/file")
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024
		* 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileController extends BaseController {

	protected final String defaultImageName = "default.jpg";

	@GetMapping("/image/{image:.+}")
	public @ResponseBody byte[] obtainImage(@PathVariable("image") String image,
			@RequestParam(name = "default", required = true, defaultValue = "true") boolean getDefaultOnNull)
			throws IOException {
		byte[] bytes = FileService.getImageBytes(image);

		if (bytes == null) {
			bytes = FileService.getImageBytes(defaultImageName);
		}

		return bytes;
	}

}
