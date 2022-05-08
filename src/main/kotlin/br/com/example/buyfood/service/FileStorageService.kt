package br.com.example.buyfood.service

import br.com.example.buyfood.config.property.FileStorageProperty
import br.com.example.buyfood.constant.ErrorMessages
import br.com.example.buyfood.enums.FileStorageFolder
import br.com.example.buyfood.exception.FileNotFoundException
import br.com.example.buyfood.exception.FileStorageException
import br.com.example.buyfood.model.dto.response.ImageResponseDTO
import br.com.example.buyfood.model.dto.response.UploadFileResponseDTO
import br.com.example.buyfood.model.entity.EstablishmentEntity
import br.com.example.buyfood.model.entity.ImageEntity
import br.com.example.buyfood.model.entity.ProductEntity
import mu.KotlinLogging
import org.apache.commons.io.FilenameUtils
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.Arrays
import java.util.Objects
import java.util.UUID
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

@Service
class FileStorageService(
  private val fileStorageProperty: FileStorageProperty
) {
  private lateinit var fileStorageLocation: Path

  private val log = KotlinLogging.logger {}

  private fun getFileStorageLocation(
    fileStorageProperty: FileStorageProperty,
    fileStorageFolder: FileStorageFolder,
    id: Long
  ) {
    fileStorageLocation =
      Paths.get(fileStorageProperty.uploadDir + "/" + fileStorageFolder.name.lowercase() + "/" + id)
        .toAbsolutePath()
        .normalize()
    createDirectories()
  }

  private fun getFileStorageLocation(fileStorageProperty: FileStorageProperty) {
    fileStorageLocation = fileStorageProperty.uploadDir?.let {
      Paths.get(it).toAbsolutePath().normalize()
    } ?: run {
      throw FileStorageException(ErrorMessages.COULD_NOT_CREATE_DIRECTORY)
    }
    createDirectories()
  }

  private fun createDirectories() {
    try {
      Files.createDirectories(fileStorageLocation)
    } catch (ex: Exception) {
      throw FileStorageException(ErrorMessages.COULD_NOT_CREATE_DIRECTORY)
    }
  }

  fun saveFile(
    file: MultipartFile,
    fileStorageFolder: FileStorageFolder,
    id: Long,
    fileUri: String
  ): UploadFileResponseDTO {
    val uuidImage = UUID.randomUUID()
    val extension = FilenameUtils.getExtension(Objects.requireNonNull(file.originalFilename))
    val fileName = StringUtils.cleanPath("$uuidImage.$extension")
    getFileStorageLocation(fileStorageProperty, fileStorageFolder, id)
    try {
      isValidFilePath(fileName)
      copyFileToTargetLocation(file, fileName)
    } catch (ex: IOException) {
      throw FileStorageException(ErrorMessages.COULD_NOT_STORE_FILE + " " + FILE_NAME_TEXT + " " + fileName)
    }
    val fileDownloadUri = fileDownloadURI(fileUri, fileName)
    return UploadFileResponseDTO(
      fileName, fileDownloadUri, file.contentType, file.size
    )
  }

  fun saveFileList(
    files: Array<MultipartFile>,
    fileStorageFolder: FileStorageFolder,
    id: Long,
    fileUri: String
  ): List<UploadFileResponseDTO> {
    return Arrays.stream(files)
      .map { i: MultipartFile ->
        saveFile(
          i,
          fileStorageFolder,
          id,
          fileUri
        )
      }
      .collect(Collectors.toList())
  }

  private fun fileDownloadURI(fileUri: String, fileName: String): String {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
      .path("/$fileUri/")
      .path(fileName)
      .toUriString()
  }

  @Throws(IOException::class)
  private fun copyFileToTargetLocation(file: MultipartFile, fileName: String) {
    val targetLocation = fileStorageLocation.resolve(fileName)
    Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
  }

  private fun isValidFilePath(fileName: String) {
    if (fileName.contains("..")) {
      throw FileStorageException(
        ErrorMessages.INVALID_FILE_NAME_PATH + " " + FILE_NAME_TEXT + " " + fileName
      )
    }
  }

  fun loadFileAsResource(
    fileStorageFolder: FileStorageFolder,
    id: Long,
    fileName: String
  ): Resource {
    return try {
      getResource(fileStorageFolder, id, fileName)
    } catch (ex: MalformedURLException) {
      throw FileNotFoundException(ErrorMessages.FILE_NOT_FOUND + " " + FILE_NAME_TEXT + " " + fileName)
    }
  }

  @Throws(MalformedURLException::class)
  private fun getResource(fileStorageFolder: FileStorageFolder, id: Long, fileName: String): Resource {
    getFileStorageLocation(fileStorageProperty, fileStorageFolder, id)
    val filePath = fileStorageLocation.resolve(fileName).normalize()
    val resource: Resource = UrlResource(filePath.toUri())
    return if (resource.exists()) {
      resource
    } else {
      throw FileNotFoundException(ErrorMessages.FILE_NOT_FOUND + " " + FILE_NAME_TEXT + " " + fileName)
    }
  }

  fun downloadFile(
    fileStorageFolder: FileStorageFolder,
    id: Long,
    fileName: String,
    request: HttpServletRequest
  ): ResponseEntity<Resource> {
    val resource = loadFileAsResource(fileStorageFolder, id, fileName)
    var contentType: String? = null
    try {
      contentType = request.servletContext.getMimeType(resource.file.absolutePath)
    } catch (ex: IOException) {
      log.warn(ErrorMessages.COULD_NOT_DETERMINE_FILE_TYPE)
    }
    if (contentType == null) {
      contentType = "application/octet-stream"
    }
    return ResponseEntity.ok()
      .contentType(MediaType.parseMediaType(contentType))
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + resource.filename + "\""
      )
      .body(resource)
  }

  fun createImageEntity(
    establishment: EstablishmentEntity?,
    uploadFileResponse: UploadFileResponseDTO
  ): ImageEntity {
    return ImageEntity(
      establishment,
      uploadFileResponse.fileName,
      uploadFileResponse.fileUri,
      uploadFileResponse.fileType,
      uploadFileResponse.size
    )
  }

  fun createImageEntity(
    product: ProductEntity?,
    uploadFileResponse: UploadFileResponseDTO
  ): ImageEntity {
    return ImageEntity(
      product,
      uploadFileResponse.fileName,
      uploadFileResponse.fileUri,
      uploadFileResponse.fileType,
      uploadFileResponse.size
    )
  }

  fun createImageResponseDTO(
    id: Long?,
    uploadFileResponse: UploadFileResponseDTO,
    status: Int
  ): ImageResponseDTO {
    return ImageResponseDTO(
      id,
      uploadFileResponse.fileName,
      uploadFileResponse.fileUri,
      uploadFileResponse.fileType,
      uploadFileResponse.size,
      status
    )
  }

  companion object {
    private const val FILE_NAME_TEXT = "filename="
  }
}
