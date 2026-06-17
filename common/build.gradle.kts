import org.gradle.kotlin.dsl.accessTransformers

plugins {
    id("com.possible-triangle.common")
}

common {
    accessWidener()
}

val moonlight_version: String by extra

dependencies {
    modCompileOnly("net.mehvahdjukaar:moonlight-neoforge:${moonlight_version}")
    accessTransformers("net.mehvahdjukaar:moonlight-neoforge:${moonlight_version}")

    modImplementation("curse.maven:supplementaries-412082:8044262")
}

tasks.withType<JavaCompile>().configureEach {
    val docsOut = rootProject.layout.buildDirectory.dir("docsOut").get().asFile
    options.compilerArgs.add("-Acrafttweaker.processor.document.output_directory=${docsOut.absolutePath}")
    options.compilerArgs.add("-Acrafttweaker.processor.document.multi_source=true")
}
