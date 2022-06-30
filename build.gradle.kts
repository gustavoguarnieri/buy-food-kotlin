import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springBootVersion = "2.5.13"
val springfoxVersion = "3.0.0"
val keycloakVersion = "17.0.1"
val ktlintVersion = "0.41.0"
val ktlintUserData = mapOf("indent_size" to "2", "continuation_indent_size" to "2")

plugins {
  id("org.springframework.boot") version "2.5.13"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("org.jetbrains.kotlin.plugin.jpa") version "1.5.21"
  id("org.jetbrains.kotlin.plugin.allopen") version "1.5.21"
  id("com.diffplug.spotless") version "6.5.2"
  id("jacoco")
  id("org.sonarqube") version "3.3"
  kotlin("jvm") version "1.6.20"
  kotlin("plugin.spring") version "1.6.20"
}

allOpen {
  annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embedabble")
}

group = "br.com.example.buyfood"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-actuator:$springBootVersion")
  implementation("io.springfox:springfox-swagger2:$springfoxVersion")
  implementation("io.springfox:springfox-swagger-ui:$springfoxVersion")
  implementation("io.springfox:springfox-boot-starter:$springfoxVersion")
  implementation("org.modelmapper:modelmapper:3.1.0")
  implementation("org.keycloak:keycloak-spring-boot-starter:$keycloakVersion")
  implementation("org.keycloak:keycloak-admin-client:$keycloakVersion")
  implementation("org.springframework.security:spring-security-core:5.6.2")
  implementation("org.springframework.retry:spring-retry:1.3.2")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("io.github.microutils:kotlin-logging:2.1.21")
  developmentOnly("org.springframework.boot:spring-boot-devtools:$springBootVersion")
  compileOnly("org.springframework.boot:spring-boot-starter-undertow:$springBootVersion")
  runtimeOnly("mysql:mysql-connector-java:8.0.28")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test:3.4.16")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

spotless {
  format("misc") {
    target("*.md", ".gitignore")
    trimTrailingWhitespace()
    indentWithSpaces(2)
    endWithNewline()
  }
  kotlin {
    target("**/*.kt")
    ktlint(ktlintVersion).userData(ktlintUserData)
    trimTrailingWhitespace()
    endWithNewline()
  }
  kotlinGradle {
    ktlint(ktlintVersion).userData(ktlintUserData)
    trimTrailingWhitespace()
    endWithNewline()
  }
}
