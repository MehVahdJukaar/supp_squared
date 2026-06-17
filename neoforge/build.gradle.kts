plugins {
    id("com.possible-triangle.neoforge")
}

neoforge {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

val moonlight_version: String by extra
val supplementaries_version: String by extra

dependencies {
    modImplementation("net.mehvahdjukaar:moonlight-neoforge:${moonlight_version}")
    accessTransformers("net.mehvahdjukaar:moonlight-neoforge:${moonlight_version}")

    modImplementation("net.mehvahdjukaar:supplementaries-neoforge:${supplementaries_version}")

    modCompileOnly("curse.maven:jei-238222:7420587")

    modImplementation("curse.maven:forgified-fabric-api-889079:6289153")
    modCompileOnly("curse.maven:configured-457570:5180900")

    modCompileOnly("curse.maven:quark-243121:5594847")
    modCompileOnly("curse.maven:zeta-968868:5597406")

    modCompileOnly("curse.maven:emi-580555:5704405")

    modRuntimeOnly("curse.maven:biomes-o-plenty-220318:5645384")
    modRuntimeOnly("curse.maven:terrablender-neoforge-940057:5864140")
    modRuntimeOnly("curse.maven:glitchcore-955399:5660740")

    implementation("org.jetbrains:annotations:22.0.0")
}

sourceSets.named("main") {
    resources.srcDir("src/generated/resources")
}

tasks.withType<JavaCompile>().configureEach {
    val docsOut = rootProject.layout.buildDirectory.dir("docsOut").get().asFile
    options.compilerArgs.add("-Acrafttweaker.processor.document.output_directory=${docsOut.absolutePath}")
    options.compilerArgs.add("-Acrafttweaker.processor.document.multi_source=true")
}
