package br.com.example.buyfood.api

import br.com.example.buyfood.exception.ApiException
import br.com.example.buyfood.exception.BusinessException
import br.com.example.buyfood.exception.ConflictException
import br.com.example.buyfood.exception.FileNotFoundException
import br.com.example.buyfood.exception.NotFoundException
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.ConnectException
import java.time.OffsetDateTime
import java.util.function.Consumer
import javax.ws.rs.ForbiddenException

@ControllerAdvice
class ApiExceptionHandler(private val messageSource: MessageSource) : ResponseEntityExceptionHandler() {

  @ExceptionHandler(ApiException::class)
  fun handleApi(ex: ApiException, request: WebRequest): ResponseEntity<Any> {
    val status = HttpStatus.INTERNAL_SERVER_ERROR
    val error = Error(status.value(), ex.message, OffsetDateTime.now())
    return handleExceptionInternal(ex, error, HttpHeaders(), status, request)
  }

  @ExceptionHandler(ConnectException::class)
  fun handleConnect(ex: ConnectException, request: WebRequest): ResponseEntity<Any> {
    val status = HttpStatus.INTERNAL_SERVER_ERROR
    val error = Error(status.value(), ex.message, OffsetDateTime.now())
    return handleExceptionInternal(ex, error, HttpHeaders(), status, request)
  }

  @ExceptionHandler(BusinessException::class)
  fun handleBusiness(ex: BusinessException, request: WebRequest): ResponseEntity<Any> {
    val status = HttpStatus.BAD_REQUEST
    val error = Error(status.value(), ex.message, OffsetDateTime.now())
    return handleExceptionInternal(ex, error, HttpHeaders(), status, request)
  }

  @ExceptionHandler(NotFoundException::class)
  fun handleNotFound(ex: NotFoundException, request: WebRequest): ResponseEntity<Any> {
    val status = HttpStatus.NOT_FOUND
    val error = Error(status.value(), ex.message, OffsetDateTime.now())
    return handleExceptionInternal(ex, error, HttpHeaders(), status, request)
  }

  @ExceptionHandler(FileNotFoundException::class)
  fun handleFileNotFound(ex: FileNotFoundException, request: WebRequest): ResponseEntity<Any> {
    val status = HttpStatus.NOT_FOUND
    val error = Error(status.value(), ex.message, OffsetDateTime.now())
    return handleExceptionInternal(ex, error, HttpHeaders(), status, request)
  }

  @ExceptionHandler(ForbiddenException::class)
  fun handleForbidden(ex: ForbiddenException, request: WebRequest): ResponseEntity<Any> {
    val status = HttpStatus.FORBIDDEN
    val error = Error(status.value(), ex.message, OffsetDateTime.now())
    return handleExceptionInternal(ex, error, HttpHeaders(), status, request)
  }

  @ExceptionHandler(AccessDeniedException::class)
  fun handleAccessDenied(ex: AccessDeniedException, request: WebRequest): ResponseEntity<Any> {
    val status = HttpStatus.FORBIDDEN
    val error = Error(status.value(), ex.message, OffsetDateTime.now())
    return handleExceptionInternal(ex, error, HttpHeaders(), status, request)
  }

  @ExceptionHandler(ConflictException::class)
  fun handleConflict(ex: ConflictException, request: WebRequest): ResponseEntity<Any> {
    val status = HttpStatus.CONFLICT
    val error = Error(status.value(), ex.message, OffsetDateTime.now())
    return handleExceptionInternal(ex, error, HttpHeaders(), status, request)
  }

  @ExceptionHandler(IllegalArgumentException::class)
  fun handleIllegalArgument(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
    val status = HttpStatus.BAD_REQUEST
    val error = Error(status.value(), ex.message, OffsetDateTime.now())
    return handleExceptionInternal(ex, error, HttpHeaders(), status, request)
  }

  override fun handleMethodArgumentNotValid(
    ex: MethodArgumentNotValidException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    val fields: ArrayList<Error.Field> = ArrayList()
    ex.bindingResult
      .allErrors
      .forEach(
        Consumer { error: ObjectError ->
          val name = (error as FieldError).field
          val message = messageSource.getMessage(error, LocaleContextHolder.getLocale())
          fields.add(Error.Field(name, message))
        }
      )
    val error = Error(status.value(), "Please check the fields", OffsetDateTime.now(), fields)
    return handleExceptionInternal(ex, error, headers, status, request)
  }
}
