package org.example

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConfigurerPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.configure(project, configurer())

        project.subprojects configurer()
    }

    private Closure<Void> configurer() {
        return {
            apply plugin: 'java'
            apply plugin: 'org.springframework.boot'
            apply plugin: 'io.spring.dependency-management'
            apply plugin: 'io.freefair.lombok'
            apply plugin: 'com.google.cloud.tools.jib'

            repositories {
                mavenLocal()
                mavenCentral()
            }

            jib {
                if(project.hasProperty('local')) allowInsecureRegistries = true
                to {
                    image = project.hasProperty('imageName') ? project.property('imageName') : 'my-image'
                    if (project.hasProperty('registryAddress')) image = "${registryAddress}/${image}".toString()
                }
                container {
                    if(project.hasProperty('jvmDebug')) jvmFlags = ['-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=65001']
                    def profiles = project.hasProperty('activeProfiles') ? project.property('activeProfiles') : 'default'
                    args = ["--spring.profiles.active=${profiles}".toString()]
                    ports = ['8080']
                }
            }
        }
    }
}
