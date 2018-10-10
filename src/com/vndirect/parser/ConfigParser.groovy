package com.vndirect.parser;

import com.vndirect.ProjectConfiguration;

class ConfigParser {

    static ProjectConfiguration parse(def yaml, def buildTag) {
        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        projectConfiguration.buildTag = buildTag;

        // load project environment
        projectConfiguration.environment = parseEnvironment(yaml.environment)
        // load project name
        projectConfiguration.projectName = parseProjectName(yaml)
        // load project framework
        projectConfiguration.buildTool = parseBuildTool(yaml.template.buildTool)
        // load project language
        projectConfiguration.language = parseLanguage(yaml.template.language)
        // parse celery option
        projectConfiguration.celery = parseCelery(yaml.template.celery)
        // parse start celery command
        projectConfiguration.startCelery = parseStartCelery(yaml.template.celery)
        // load version
        projectConfiguration.version = parseVersion(yaml.template.version)
        // load project build steps
        projectConfiguration.build = parseBuildSteps(yaml.build)
        // load memLimit
        projectConfiguration.memLimit = parseMemLimit(yaml.Docker.memLimit)
        // load cpuLimit
        projectConfiguration.cpuLimit = parseCpuLimit(yaml.Docker.cpuLimit)
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

    static def parseEnvironment(def environment) {
        if (!environment) {
            return "UAT"
        }
        return environment
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

    static def parseCelery(def celery) {
        if (!celery) {
            return false
        }
        return celery
    }

    static def parseStartCelery(def config) {
        if (!config) {
            return null
        }
        if (config && !config.startCelery) {
            return "celery worker -A admin.celery --loglevel=info --autoscale=10,4"
        }
        return config.startCelery
    }

    static def parseBuildTool(def buildTool) {
        if (!buildTool) {
            return null
        }
        return buildTool
    }

    static def parseVersion(def version) {
        if (!version) {
            return null
        }
        try {
            version = version.tokenize(".")[0]
            return version
        } catch(ignored) {
            return version
        }
    }

    static def parseBuildSteps(def config) {
        if ( !config) {
            return null
        }
        return config.each {"$it"}
    }

    static def parseMemLimit(def memLimit) {
        if ( !memLimit) {
            return "1g"
        }
        return memLimit
    }

    static def parseCpuLimit(def cpuLimit) {
        if ( !cpuLimit) {
            return "1000"
        }
        return cpuLimit
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
        return dependencies.collect { it.trim() }.join(" ")
    }

    static def parseRunCommand(def runCommand) {
        if (!runCommand) {
            return "No run"
        }
        // return runCommand.split().collect {"\"" +  it.trim() + "\"" }.join(",")
        return runCommand
    }

    static def parseConfigfiles(def configFiles){
        if ( !configFiles) {
            return null
        }
        return configFiles
    }

    static def parseEnvironmentVariables(def environmentVariables) {
        if ( !environmentVariables) {
            return "Maintainer Teehee"
        }
        return environmentVariables.collect { it.trim() }.join(" ")
    }
}