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
        projectConfiguration.framework = parseFramework(yaml)

        // load project language
        projectConfiguration.language = parseLanguage(yaml)

        // load project build steps
        projectConfiguration.build = parseBuildSteps(yaml.build)

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

    static def parseLanguage(def config) {
        if (!config || !config["template"]["language"]) {
            return "vnds-language"
        }

        return config["template"]["language"]
    }

    static def parseFramework(def config) {
        if (!config || !config["template"]["framework"]) {
            return "vnds-framework"
        }

        return config["template"]["framework"]
    }

    static def parseBuildSteps(def config) {
        return config.each {$it}
    }
}
