package kr.or.ddit.common.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.wrapper.PartWrapper;

@Controller
public class FileUploadTestController {
	
	@RequestMapping(value="/file/upload.do", method=RequestMethod.POST)
	public String upload(HttpServletRequest req, HttpServletResponse resp ) throws IOException, ServletException {
		String uploader = req.getParameter("uploader");
//		Part uploader req.getPart("uploader");
		System.out.println(uploader);
		
		if(req instanceof MultipartFile) {
			Multipart requestWapper = (MultipartFile) req;
			PartWrapper[] array = requestWapper.getPartWrappers("uploadFile");
			
			//1.저장위치(/prodImages)
			String saveFolderUrl = "/prodImages";
			String saveFolderPath = req.getServletContext().getRealPath(saveFolderUrl);
			File saveFolder = new File(saveFolderPath);
			//배포 시 폴더 안에 파일이 없을 때 이클립스는 빼고 배포할 수 있으므로 없을 때 새로 만들어주기
			if(!saveFolder.exists()) {
				saveFolder.mkdirs();
			}
			//2.저장명 (원본파일이름 기준)
			List<String> saveFiles = new ArrayList<>();
			for(PartWrapper partWrapper :array) {
				String originalFileName = partWrapper.getFileName();
				System.out.println(originalFileName);
				String savename = UUID.randomUUID().toString();
				File saveFile = new File(saveFolder, savename);
				String fileMIME = partWrapper.getContentType();
				if(!StringUtils.startsWith(fileMIME, "image/")) {
					resp.sendError(400);
					return null;
				}
				long filesize =partWrapper.getSize();
				try(
						InputStream is = partWrapper.getInputStream();
						
						){	
					FileUtils.copyInputStreamToFile(is, saveFile);
				}
				String saveFileURL = saveFolderUrl+"/"+savename;
				saveFiles.add(saveFileURL);
			}
			req.getSession().setAttribute("saveFiles", saveFiles);
			req.getSession().setAttribute("uploader", uploader);
		}
		
		return "redirect:/13/fileUploadForm.jsp";
	}
}
