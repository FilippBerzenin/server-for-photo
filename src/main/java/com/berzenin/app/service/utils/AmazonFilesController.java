package com.berzenin.app.service.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AmazonFilesController {
	
	private AmazonS3 s3Client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3Client = new AmazonS3Client(credentials);
	}

//	public String uploadFile(MultipartFile multipartFile) {
//		String fileUrl = "";
//		try {
//			File file = convertMultiPartToFile(multipartFile);
//			String fileName = generateFileName(multipartFile);
//			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
//			uploadFileTos3bucket(fileName, file);
//			file.delete();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return fileUrl;
//	}
//	
	private File convertMultiPartToFile(MultipartFile file) {
		log.info("Get files with size " + file.getSize());
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convFile;
	}
//	
//	private boolean uploadFileTos3bucket(String fileName, File file) {
//	try {
//
//		log.info("File " + fileName + " send to amazone basket " + bucketName);
//		s3Client.putObject(
//				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
//		return true;
//	} catch (RuntimeException e) {
//		log.info("File " + fileName + " failed upload" + e.getLocalizedMessage());
//		return false;
//
//	}
//}

//	public String deleteFileFromS3Bucket(String fileUrl) {
//		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
//		s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
//		return "Successfully deleted";
//	}
//
//	@Getter
//	protected static final String pathToResource = "web-stell-searcher";
	
	public Optional<Path> copyFileForlocalDirectory(MultipartFile file) {
		try {
			log.info("File upload started " + file.getOriginalFilename());
			String path = this.getLocalPathForPdf(file).toString().replace("\\", "/");
		    ObjectMetadata metadata = new ObjectMetadata();
		    metadata.setContentLength(file.getSize());
			s3Client.putObject(new PutObjectRequest(bucketName, path, file.getInputStream(), metadata));
			log.info("File set security Permission started " + file.getOriginalFilename());
			final AccessControlList acl = s3Client.getObjectAcl(bucketName, path);
			acl.grantPermission(GroupGrantee.AllUsers, Permission.FullControl);
			log.info("File set security Permission finished " + file.getOriginalFilename());
			s3Client.setObjectAcl(bucketName, path, acl);
			log.info("File upload " + file.getOriginalFilename() + " successful");		
			return Optional.of(Paths.get(path));
		} catch (NullPointerException | AmazonClientException | IOException e) {
			log.error("File upload failed " + file.getOriginalFilename() + e.getLocalizedMessage());
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	public Path getLocalPathForPdf(MultipartFile file) {
//		Path path = Paths.get("localfiles\\" + file.getOriginalFilename());
		Path path = Paths.get(file.getOriginalFilename());
		return path;
	}
	
	public String getPathTOS3Bukcet (Path path) {
		URL url = s3Client.getUrl(bucketName, path.toString());
		return url.toString();
	}

//	public boolean deleteFile(String fileUrl) {
//		String fileName = fileUrl.replace("\\", "/");
//		log.info("File " + fileName + " start deleting");
//		try {
//			s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
//			log.info("File " + fileName + " successful deleted");
//			return true;
//		} catch (RuntimeException e) {
//			log.info("File " + fileName + " failed deleted" + e.getLocalizedMessage());
//			return false;
//		}
//	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

//	public void copyPdfMultipartFile(LinkForMetalResources entity, MultipartFile file) {
//		try {
//			TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3Client)
//					.withMultipartUploadThreshold((long) (5 * 1024 * 1025)).build();
//			Upload upload = tm.upload(bucketName, file.getName(), convertMultiPartToFile(file));
//			upload.waitForCompletion();
//		} catch (AmazonClientException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	public boolean upload(File file, String pathToPutInStorage) {
//		try {
//			log.info("File upload started " + file.getName());
//			s3Client.putObject(bucketName, pathToPutInStorage, file);
//			log.info("File upload " + file.getName() + " successful");
//			return true;
//		} catch (AmazonClientException e) {
//			log.error("File upload failed " + file.getName() + e.getLocalizedMessage());
//			log.error(e.getStackTrace().toString());
//			return false;
//		}
//	}

//	@Override
//	public boolean downloadPdfFileFromUrl(LinkForMetalResources res) {
//		if (res.getUrlForResource() == null || res.getUrlForResource().length() == 0) {
//			return false;
//		}
//		URL url = null;
//		try {
//			url = new URL(res.getUrlForResource());
//		} catch (MalformedURLException e1) {
//			e1.printStackTrace();
//			return false;
//		}
//		try (InputStream in = url.openStream()) {
//			File f = stream2file(in, ".pdf");
//			if (upload(f, res.getLocalPathForPdfFile().replace("\\", "/"))) {
//				return true;
//			}
//		} catch (IOException e) {
//			log.error(e.getMessage());
//			e.printStackTrace();
//			return false;
//		}
//		return false;
//	}
//
//	public File stream2file(InputStream in, String suffix) throws IOException {
//		final Path path = Files.createTempFile("myTempFile", suffix);
//		Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
//		return path.toFile();
//	}
//
//	
//	public String getAmazonPathForPdf(String key) {
//		return s3Client.getUrl(bucketName, key).toString().replace("%5C", "/");
//	}
//
//	@Override
//	public String setPathForFile(String url) {
//		String path = "";
//		try {
//			URL partOfurl = new URL(url);
//			path = partOfurl.getHost() + "\\";
//		} catch (IOException e) {
//			log.error(e.getMessage());
//			e.printStackTrace();
//		}
//		return path;
//	}
//
//	@Override
//	public String setPdfFileName(String url) {
//		String pdfFileName = "";
//		try {
//			URL partOfurl = new URL(url.trim());
//			pdfFileName = FilenameUtils.getName(partOfurl.getPath());
//			if (pdfFileName == null || pdfFileName.length() == 0) {
//				if (url.endsWith("/")) {
//					url = url.substring(0, url.length() - 2);
//				}
//				pdfFileName = url.substring(url.lastIndexOf("/") + 1);
//			}
//		} catch (IOException e) {
//			log.error(e.getMessage());
//		}
//		return pdfFileName;
//	}
//
//	@Override
//	public Optional<Path> generateTxtFromPDF(String txtFile, String pathToPdfFile) {
//		Path file = Paths.get(txtFile);
//		if (getObjectFromStorage(pathToPdfFile.replace("\\", "/")) == null) {
//			log.info("File don't find: " + pathToPdfFile);
//			return Optional.ofNullable(file);
//		}
//		S3Object pdfFile = getObjectFromStorage(pathToPdfFile.replace("\\", "/"));
//		PDFTextStripper tStripper;
//		try (PDDocument document = PDDocument.load(pdfFile.getObjectContent().getDelegateStream())) {
//			document.getClass();
//			if (!document.isEncrypted()) {
//				tStripper = new PDFTextStripper();
//				String pdfFileInText = tStripper.getText(document);
//				document.close();
//				String lines[] = pdfFileInText.split("\\r?\\n");
//				Path p = this.writeBytesForTxtFile(txtFile, lines).get();
//				return Optional.of(p);
//			}
//		} catch (NoClassDefFoundError e) {
//			log.info("txt " + txtFile + " pdf " + pdfFile);
//			log.error("NoClassDefFoundError " + e);
//			e.printStackTrace();
//			return Optional.ofNullable(file);
//		} catch (InvalidPasswordException e) {
//			log.error("invalid password" + e);
//			e.printStackTrace();
//			return Optional.ofNullable(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return Optional.ofNullable(file);
//		}
//		return Optional.ofNullable(file);
//	}
//
//	@Override
//	public Optional<List<String>> readAllLines(String url) {
//		S3Object file = getObjectFromStorage(url.replace("\\", "/"));
//		try {
//			File f = stream2file(file.getObjectContent().getDelegateStream(), ".txt");
//			return Optional.of(Files.readAllLines(f.toPath()));
//		} catch (IOException e) {
//			e.printStackTrace();
//			return Optional.empty();
//		}
//	}
//
//	@Override
//	public Optional<Path> writeBytesForTxtFile(String localPathForTxtFile, String[] lines) {
//		Path path = Paths.get(localPathForTxtFile);
//		List<String> lin = Arrays.asList(lines);
//		lin = this.removeWhitespaces(lin);
//		File file = new File("test.txt");
//		try (OutputStream out = new FileOutputStream(file)) {
//			String pathTxt = localPathForTxtFile.replace("\\", "/");
//			byte[] bytes = lin.stream().collect(Collectors.joining("\n")).getBytes();
//			s3Client.putObject(bucketName, pathTxt, new ByteArrayInputStream(bytes), new ObjectMetadata());
//			return Optional.ofNullable(path);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return Optional.ofNullable(path);
//	}
//
//	public void createBucket(String bucketName) {
//		if (s3Client.doesBucketExistV2(bucketName)) {
//			log.info("Bucket name is not available." + " Try again with a different Bucket name.");
//			return;
//		}
//		s3Client.createBucket(bucketName);
//	}
//
//	public Optional<List<Bucket>> getAllBucket() {
//		return Optional.of(s3Client.listBuckets());
//	}
//
//	public boolean deleteBacket(String bucketName) {
//		try {
//			s3Client.deleteBucket(bucketName);
//			return true;
//		} catch (AmazonServiceException e) {
//			log.error("Backet failed deleted " + bucketName);
//			return false;
//		}
//	}
//
//	public Optional<ObjectListing> getAllObject() {
//		return Optional.of(s3Client.listObjects(bucketName));
//	}
//
//	public S3Object getObjectFromStorage(String pathToGetFileInStorage) {
//		return Optional.of(s3Client.getObject(bucketName, pathToGetFileInStorage)).orElseThrow(RuntimeException::new);
//	}
//
//	public boolean uploadFile() {
//		String fileUrl = "";
//		try {
//			File file = new File("C:\\Users\\Fylyp\\Desktop\\resume\\FilippBerzeninENG.pdf");
//			String fileName = file.getName();
//			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
//			if (uploadFileTos3bucket(fileName, file)) {
//				log.info("Files upload " + file.getName() + " successful");
//			}
////			file.delete();
//			return true;
//		} catch (Exception e) {
//			log.info("Files upload failed");
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//
//
//	public Optional<File> read(String filesname) {
//		try {
//			log.info("File " + filesname + " start reading");
//			S3Object s3object = s3Client.getObject(bucketName, filesname);
//			S3ObjectInputStream inputStream = s3object.getObjectContent();
//			File file = new File(filesname);
//			FileUtils.copyInputStreamToFile(inputStream, file);
//			log.info("File " + filesname + " successful readed");
//			return Optional.of(file);
//		} catch (IOException e) {
//			log.info("File " + filesname + " failed reding" + e.getLocalizedMessage());
//			e.printStackTrace();
//			return Optional.empty();
//
//		}
//	}
}
