package com.idmworks.bitbucket.listener.api;

import com.idmworks.bitbucket.listener.config.ApplicationConfig;
import com.idmworks.bitbucket.listener.config.JenkinsConfig;
import com.idmworks.bitbucket.listener.models.*;
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
            logger.warn("Webhook request has no full name for the repository");
            return;
        }

        final Map<String, Map<String, String>> appTriggers = this.applicationConfig.getTriggers();
        if (appTriggers == null) {
            logger.info("There are no triggers configured - see application-default.yml.example");
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


        // TODO: validate jenkinsConfig, username & pwd

        final String url = jenkinsConfig.getHost() + String.format(tagCreatedUrl, tagName);
        final RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthorization(jenkinsConfig.getUsername(), jenkinsConfig.getPassword())
                .build();

        final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, tagName);

        // TODO: validate response
        logger.info(response.toString());
    }
}
