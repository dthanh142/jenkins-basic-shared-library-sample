package com.vndirect.parser;

import com.vndirect.ProjectConfiguration;

class ConfigParser {

    static ProjectConfiguration parse(def yaml, def buildNumber) {
        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        projectConfiguration.buildNumber = buildNumber;

        // parse the environment variables
        projectConfiguration.environment    = parseEnvironment(yaml.template.language);

        // load the project name
        projectConfiguration.projectName = parseProjectName(yaml);

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
}
