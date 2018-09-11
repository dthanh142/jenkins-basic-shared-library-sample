package com.vndirect.parser;

import com.vndirect.ProjectConfiguration;

class ConfigParser {

    static ProjectConfiguration parse(def yaml, def buildTag) {
        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        projectConfiguration.buildTag = buildTag;

        // load the project name
        projectConfiguration.projectName = parseProjectName(yaml)
        // load project framework
        projectConfiguration.framework = parseFramework(yaml.template.framework)
        // load project language
        projectConfiguration.language = parseLanguage(yaml.template.language)
        // load version
        projectConfiguration.version = parseVersion(yaml.template.version)
        // load project build steps
        projectConfiguration.build = parseBuildSteps(yaml.build)
        // load dockerfile
        projectConfiguration.dockerfile = parseDockerfile(yaml.Docker.dockerfile)
        // load docker-compose
        projectConfiguration.dockerCompose = parseDockerCompose(yaml.Docker.dockerCompose)
        // load project port
        projectConfiguration.port = parsePort(yaml.Docker.port)
        // load docker dependencies
        projectConfiguration.dependencies = parseDependencies(yaml.Docker.dependencies)
        // load run command
        projectConfiguration.runCommand = parseRunCommand(yaml.Docker.runCommand)
        // load config files
        projectConfiguration.configFiles = parseConfigfiles(yaml.Docker.configFiles)
        // load environmentVariables
        projectConfiguration.environmentVariables = parseEnvironmentVariables(yaml.Docker.env)

        return projectConfiguration;
    }

    static def parseProjectName(def config) {
        if (!config || !config["projectName"]) {
            return "woloxci-project";
        }

        return config["projectName"];
    }

    static def parseLanguage(def language) {
        if (!language) {
            return "vnds-language"
        }

        return language
    }

    static def parseFramework(def framework) {
        if (!framework) {
            return "vnds-framework"
        }

        return framework
    }

    static def parseVersion(def version) {
        if (!version) {
            return "default"
        }
        
        return version
    }

    static def parseBuildSteps(def config) {
        if ( !config) {
            return null
        }
        return config.each {"$it"}
    }

    static def parseDockerfile(def dockerfile) {
        if (!dockerfile) {
            return "Dockerfile-default"
        }

        return dockerfile
    }

    static def parseDockerCompose(def dockerCompose) {
        if (!dockerCompose) {
            return "docker-compose-default.yml"
        }

        return dockerCompose
    }

    static def parsePort(def port) {
        if (!port) {
            return "no port"
        }

        return port
    }

    static def parseDependencies(def dependencies){
        if (!dependencies) {
            return null
        }

        return dependencies
    }

    static def parseRunCommand(def runCommand) {
        if (!runCommand) {
            return "No run"
        }
        return runCommand.split().collect {"\"" +  it.trim() + "\"" }.join(",")
    }

    static def parseConfigfiles(def configFiles){
        if ( !configFiles) {
            return null
        }
        return configFiles
    }

    static def parseEnvironmentVariables(def environmentVariables) {
        if ( !environmentVariables) {
            return ""
        }

        // return environmentVariables.collect { k, v -> "${k}=${v}"}
        return environmentVariables
    }
}