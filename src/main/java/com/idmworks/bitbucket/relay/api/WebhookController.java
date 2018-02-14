package com.idmworks.bitbucket.relay.api;

import com.idmworks.bitbucket.relay.config.ApplicationConfig;
import com.idmworks.bitbucket.relay.config.JenkinsConfig;
import com.idmworks.bitbucket.relay.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class WebhookController {

    public static final String CHANGE_TYPE_TAG = "tag";
    public static final String TRIGGER_TAG_CREATED = "tag-created";

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private JenkinsConfig jenkinsConfig;

    /**
     * Default constructor
     */
    public WebhookController() {
        this(null, null, null);
    }

    /**
     * DI constructor for Unit Tests
     */
    public WebhookController(final RestTemplateBuilder restTemplateBuilder, final ApplicationConfig applicationConfig,
                             final JenkinsConfig jenkinsConfig) {
        if (restTemplateBuilder == null) {
            this.restTemplateBuilder = new RestTemplateBuilder();
        } else {
            this.restTemplateBuilder = restTemplateBuilder;
        }
        this.applicationConfig = applicationConfig;
        this.jenkinsConfig = jenkinsConfig;
    }

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

        final String repoFullName = repo.getFullName();
        if (StringUtils.isEmpty(repoFullName)) {
            logger.warn("Webhook request has no full name for the repository '{}'", repo.getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Processing {} change(s)", pushChanges.size());
        for (final Change pushChange : pushChanges) {
            handlePushedChange(repoFullName, pushChange);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void handlePushedChange(final String repoFullName, final Change pushChange) {
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
                    logger.info("Detected newly created tag '{}'", tagName);
                    handleTagCreated(repoFullName, tagName);
                }
            } else {
                logger.info("Webhook request has unhandled state type '{}' - skipping", stateType);
            }
        }
    }

    private void handleTagCreated(final String repoFullName, final String tagName) {
        final Map<String, Map<String, String>> appTriggers = this.applicationConfig.getTriggers();
        if (appTriggers == null) {
            logger.info("There are no triggers configured - see application-default.yml");
            return;
        }

        final Map<String, String> repoTriggers = appTriggers.get(repoFullName);
        if (repoTriggers == null) {
            logger.info("There are no triggers configured for repo '{}'", repoFullName);
            return;
        }

        final String tagCreatedUrl = repoTriggers.get(TRIGGER_TAG_CREATED);
        if (tagCreatedUrl == null) {
            logger.info("There are no '{}' triggers configured for repo '{}'", TRIGGER_TAG_CREATED, repoFullName);
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
        final RestTemplate restTemplate = this.restTemplateBuilder
                .basicAuthorization(jenkinsUsername, jenkinsPassword)
                .build();

        try {
            final URI expandedUri = restTemplate.getUriTemplateHandler().expand(url, tagName);
            logger.info("Triggering parameterized Jenkins job at '{}'", expandedUri.toString());
            final ResponseEntity<String> response = restTemplate.getForEntity(expandedUri, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Successfully triggered parameterized job in Jenkins - HTTP '{}' returned", response.getStatusCodeValue());
            } else {
                logger.error("Error triggering parameterized job in Jenkins - HTTP '{}' returned", response.getStatusCodeValue());
            }

            logger.debug(response.toString());
        } catch (HttpStatusCodeException e) {
            logger.error(String.format("Error triggering parameterized job in Jenkins - HTTP '%s' returned",
                    e.getStatusCode().toString()), e);
        }
    }
}
