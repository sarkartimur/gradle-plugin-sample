package org.example

import org.gradle.api.Plugin
import org.gradle.api.Project

import java.nio.file.Files

class ConfigurerPlugin implements Plugin<Project> {
    private static final String LOMBOK_CONFIG =
            'lombok.copyableAnnotations += org.springframework.beans.factory.annotation.Qualifier\n' +
                    'lombok.copyableAnnotations += org.springframework.beans.factory.annotation.Value'
    private static final String GITIGNORE =
            '.gradle\n' +
                    'build\n' +
                    '.idea\n' +
                    '.iml'


    void apply(Project project) {
        project.configure(project, configurer(this.&createFile))

        project.subprojects configurer(this.&createFile)
    }

    private Closure<Void> configurer(createFile) {
        return {
            apply plugin: 'java'
            apply plugin: 'org.springframework.boot'
            apply plugin: 'io.spring.dependency-management'
            apply plugin: 'io.freefair.lombok'
            apply plugin: 'com.google.cloud.tools.jib'

            createFile(project, 'lombok.config', LOMBOK_CONFIG)
            createFile(project, '.gitignore', GITIGNORE)

            repositories {
                mavenLocal()
                mavenCentral()
            }

            jib {
                if (project.hasProperty('local')) allowInsecureRegistries = true
                to {
                    image = project.hasProperty('imageName') ? project.property('imageName') : 'my-image'
                    if (project.hasProperty('registryAddress')) image = "${registryAddress}/${image}".toString()
                }
                container {
                    if (project.hasProperty('jvmDebug')) jvmFlags = ['-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005']
                    def profiles = project.hasProperty('activeProfiles') ? project.property('activeProfiles') : 'default'
                    args = ["--spring.profiles.active=${profiles}".toString()]
                    ports = ['8080']
                }
            }
        }
    }

    private createFile(Project project, String fileName, String content) {
        File file = project.file(fileName)
        if (!file.exists()) {
            file.createNewFile()
            Files.write(file.toPath(), content.getBytes())
        }
    }
}
