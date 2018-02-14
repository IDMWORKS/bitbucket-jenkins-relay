package com.idmworks.bitbucket.relay.api;

import com.idmworks.bitbucket.relay.config.ApplicationConfig;
import com.idmworks.bitbucket.relay.config.JenkinsConfig;
import com.idmworks.bitbucket.relay.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

@RestController
@Configuration
@ConfigurationProperties(prefix = "triggers")
public class WebhookController {

    private static final String CHANGE_TYPE_TAG = "tag";
    private static final String TRIGGER_TAG_CREATED = "tag-created";

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private JenkinsConfig jenkinsConfig;

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public ResponseEntity<?> postWebhook(@RequestBody final Webhook webhook) {

        logger.info("Webhook request received");
        logger.debug(webhook.toString());

        final Push pushInfo = webhook.getPush();
        if (pushInfo == null) {
            logger.warn("Webhook request has no push info");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final List<Change> pushChanges = pushInfo.getChanges();
        if (pushChanges == null) {
            logger.warn("Webhook request has no change info");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Repository repo = webhook.getRepository();
        if (repo == null) {
            logger.warn("Webhook request has no repository info");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        for (final Change pushChange : pushChanges) {
            handlePushedChange(repo, pushChange);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void handlePushedChange(final Repository repo, final Change pushChange) {
        final RefState newState = pushChange.getNewState();
        if (newState == null) {
            // "new" state is null for deletions
            logger.info("Webhook request has no 'new' state - skipping");
        } else {
            final String stateType = newState.getType();
            if (CHANGE_TYPE_TAG.equalsIgnoreCase(stateType)) {
                final String tagName = newState.getName();
                if (StringUtils.isEmpty(tagName)) {
                    logger.warn("Webhook request contains no name for the new tag");
                } else {
                    handleTagCreated(repo, tagName);
                }
            } else {
                logger.info("Webhook request has unhandled state type '{}' - skipping", stateType);
            }
        }
    }

    private void handleTagCreated(final Repository repo, final String tagName) {

        final String repoFullName = repo.getFullName();
        if (StringUtils.isEmpty(repoFullName)) {
            logger.warn("Webhook request has no full name for the repository '{}'", repo.getName());
            return;
        }

        final Map<String, Map<String, String>> appTriggers = this.applicationConfig.getTriggers();
        if (appTriggers == null) {
            logger.info("There are no triggers configured - see application-default.yml");
            return;
        }

        final Map<String, String> repoTriggers = appTriggers.get(repoFullName);
        if (repoTriggers == null) {
            logger.info("There are no triggers configured configured for repo '{}'", repoFullName);
            return;
        }

        final String tagCreatedUrl = repoTriggers.get(TRIGGER_TAG_CREATED);
        if (tagCreatedUrl == null) {
            logger.info("There are no '{}' triggers configured configured for repo '{}'", TRIGGER_TAG_CREATED, repoFullName);
            return;
        }

        if (jenkinsConfig == null) {
            logger.info("There is no configuration provided for Jenkins - see application-default.yml", TRIGGER_TAG_CREATED, repoFullName);
            return;
        }

        final String jenkinsHost = this.jenkinsConfig.getHost();
        if (StringUtils.isEmpty(jenkinsHost)) {
            logger.error("There is no Jenkins host configured in application-default.yml");
            return;
        }

        final String jenkinsUsername = this.jenkinsConfig.getUsername();
        if (StringUtils.isEmpty(jenkinsUsername)) {
            logger.error("There is no Jenkins username configured in application-default.yml");
            return;
        }

        final String jenkinsPassword = this.jenkinsConfig.getPassword();
        if (StringUtils.isEmpty(jenkinsPassword)) {
            logger.error("There is no Jenkins password configured in application-default.yml");
            return;
        }

        final String url = jenkinsHost + String.format(tagCreatedUrl, tagName);
        final RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthorization(jenkinsUsername, jenkinsPassword)
                .build();

        final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, tagName);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Successfully triggered parameterized job in Jenkins");
        } else {
            logger.error("Error triggering parameterized job in Jenkins - HTTP {} returned", response.getStatusCodeValue());
        }

        logger.debug(response.toString());
    }
}
