import org.gradle.kotlin.dsl.modCompileOnly

plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

loom {
}

val moonlight_version: String by extra
val cloth_version: String by extra

dependencies {
    modImplementation("net.mehvahdjukaar:moonlight-fabric:${moonlight_version}")

    modImplementation("curse.maven:supplementaries-412082:8044264")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${cloth_version}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modImplementation("curse.maven:yacl-667299:4574163")

    modCompileOnly("com.terraformersmc:modmenu:4.0.6") {
        exclude(module = "fabric-api")
    }

    modRuntimeOnly("maven.modrinth:frozenlib:1.9.1-mc1.21.1")
    modRuntimeOnly("maven.modrinth:wilder-wild:3.0.4-mc1.21.1")
}
