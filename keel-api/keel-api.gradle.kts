plugins {
  `java-library`
  id("kotlin-spring")
  application
}

dependencies {
  api(project(":keel-actuator"))
  api(project(":keel-plugin"))
  api(project(":keel-eureka"))
  api(project(":keel-artifact"))
  api(project(":keel-veto"))
  api(project(":keel-sql"))

  implementation(project(":keel-bakery-plugin"))
  implementation(project(":keel-ec2-plugin"))
  implementation(project(":keel-tagging-plugin"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("com.netflix.spinnaker.kork:kork-core")
  implementation("com.netflix.spinnaker.kork:kork-web")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.security:spring-security-config")

  implementation("com.netflix.spinnaker.fiat:fiat-api:${property("fiatVersion")}")
  implementation("com.netflix.spinnaker.fiat:fiat-core:${property("fiatVersion")}")

  implementation("net.logstash.logback:logstash-logback-encoder")

  testImplementation("io.strikt:strikt-jackson")
  testImplementation(project(":keel-core-test"))
  testImplementation(project(":keel-spring-test-support"))
  testImplementation(project(":keel-retrofit"))
  testImplementation(project(":keel-clouddriver"))
  testImplementation("com.netflix.spinnaker.kork:kork-security")
  testImplementation("com.squareup.okhttp3:mockwebserver")
}

application {
  mainClassName = "com.netflix.spinnaker.keel.MainKt"
}
