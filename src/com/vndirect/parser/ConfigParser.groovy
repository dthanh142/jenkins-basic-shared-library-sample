package com.vndirect.parser;

import com.vndirect.ProjectConfiguration;

class ConfigParser {

    static ProjectConfiguration parse(def yaml, def buildNumber) {
        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        projectConfiguration.buildNumber = buildNumber;

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

        // load run command
        projectConfiguration.runCommand = parseRunCommand(yaml.Docker.runCommand)

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
        if (!config) {
            return "vnds-language"
        }

        return language
    }

    static def parseFramework(def framework) {
        if (!config) {
            return "vnds-framework"
        }

        return framework
    }

    static def parseVersion(def version) {
        if (!config) {
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
        if (!config) {
            return "no port"
        }s

        return port
    }

    static def parseRunCommand(def runCommand) {
        return runCommand
}