package com.vndirect.parser;

import com.vndirect.ProjectConfiguration;

class ConfigParser {

    static ProjectConfiguration parse(def yaml, def buildTag) {
        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        projectConfiguration.buildTag = buildTag;

        // parse the environment variables
        projectConfiguration.environment = parseEnvironment(yaml.template.language);

        // load the project name
        projectConfiguration.projectName = parseProjectName(yaml);

        // load project framework
        projectConfiguration.framework = parseFramework(yaml.template.framework)

        // load project language
        projectConfiguration.language = parseLanguage(yaml.template.language)

        // load version
        projectConfiguration.version = parseVersion(yaml.template.version)

        // load project build steps
        projectConfiguration.build = parseBuildSteps(yaml.build)

        // load project port
        projectConfiguration.port = parsePort(yaml.Docker.port)

        // load docker dependencies
        projectConfiguration.dependencies = parseDependencies(yaml.Docker.dependencies)

        // load run command
        projectConfiguration.runCommand = parseRunCommand(yaml.Docker.runCommand)

        // load config files
        projectConfiguration.configFiles = parseConfigfiles(yaml.Docker.configFiles)

        return projectConfiguration;
    }

    static def parseEnvironment(def environment) {
        if (!environment) {
            return "";
        }

        return environment.collect { k, v -> "${k}=${v}"};
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
        // if (!config || !config['build']) {
        //     return "No build"
        // }
        return config.each {"$it"}
    }

    static def parsePort(def port) {
        if (!port) {
            return "no port"
        }

        return port
    }

    static def parseDependencies(def dependencies){
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
            return false
        }
        return configFiles
    }
}