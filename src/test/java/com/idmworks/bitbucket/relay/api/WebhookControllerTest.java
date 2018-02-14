package com.idmworks.bitbucket.relay.api;

import com.idmworks.bitbucket.relay.config.ApplicationConfig;
import com.idmworks.bitbucket.relay.config.JenkinsConfig;
import com.idmworks.bitbucket.relay.models.*;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

public class WebhookControllerTest {

    /* Test cases */

    @Test
    public void postWebhookWithoutPushInfoReturnsBadRequest() {
        // arrange
        final RestTemplateBuilder mockBuilder = mock(RestTemplateBuilder.class);
        final ApplicationConfig appConfig = new ApplicationConfig();
        final JenkinsConfig jenkinsConfig = new JenkinsConfig();
        final WebhookController controller = new WebhookController(mockBuilder, appConfig, jenkinsConfig);
        final Webhook webhook = new Webhook();

        // act
        final ResponseEntity<?> response = controller.postWebhook(webhook);

        // assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void postWebhookWithoutChangeInfoReturnsBadRequest() {
        // arrange
        final RestTemplateBuilder mockBuilder = mock(RestTemplateBuilder.class);
        final ApplicationConfig appConfig = new ApplicationConfig();
        final JenkinsConfig jenkinsConfig = new JenkinsConfig();
        final WebhookController controller = new WebhookController(mockBuilder, appConfig, jenkinsConfig);
        final Webhook webhook = new Webhook();
        final Push pushInfo = new Push();
        webhook.setPush(pushInfo);

        // act
        final ResponseEntity<?> response = controller.postWebhook(webhook);

        // assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void postWebhookWithoutRepositoryInfoReturnsBadRequest() {
        // arrange
        final RestTemplateBuilder mockBuilder = mock(RestTemplateBuilder.class);
        final ApplicationConfig appConfig = new ApplicationConfig();
        final JenkinsConfig jenkinsConfig = new JenkinsConfig();
        final WebhookController controller = new WebhookController(mockBuilder, appConfig, jenkinsConfig);
        final Webhook webhook = new Webhook();
        final Push pushInfo = new Push();
        webhook.setPush(pushInfo);
        final List<Change> changes = new ArrayList<>();
        pushInfo.setChanges(changes);

        // act
        final ResponseEntity<?> response = controller.postWebhook(webhook);

        // assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void postWebhookWithoutRepoFullNameReturnsBadRequest() {
        // arrange
        final RestTemplateBuilder mockBuilder = mock(RestTemplateBuilder.class);
        final ApplicationConfig appConfig = new ApplicationConfig();
        final JenkinsConfig jenkinsConfig = new JenkinsConfig();
        final WebhookController controller = new WebhookController(mockBuilder, appConfig, jenkinsConfig);
        final Webhook webhook = new Webhook();
        final Push pushInfo = new Push();
        webhook.setPush(pushInfo);
        final List<Change> changes = new ArrayList<>();
        pushInfo.setChanges(changes);
        final Repository repo = new Repository();
        webhook.setRepository(repo);

        // act
        final ResponseEntity<?> response = controller.postWebhook(webhook);

        // assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void postWebhookTriggersJenkinsJob() {
        // arrange
        final String repoFullName = "acme/widget";
        final String tagName = "1.0.0";

        // setup mocks
        final RestTemplate mockTemplate = mock(RestTemplate.class);
        when(mockTemplate.getUriTemplateHandler())
                .thenReturn(new DefaultUriTemplateHandler());
        when(mockTemplate.getForEntity(isA(URI.class), eq(String.class)))
                .thenReturn(new ResponseEntity<String>(HttpStatus.OK));

        final RestTemplateBuilder mockBuilder = mock(RestTemplateBuilder.class);
        when(mockBuilder.basicAuthorization(any(String.class), any(String.class)))
                .thenReturn(mockBuilder);
        when(mockBuilder.build())
                .thenReturn(mockTemplate);

        // define webhook payload
        final Webhook webhook = createTagCreatedPayload(repoFullName, tagName);

        // setup SUT
        final ApplicationConfig appConfig = createAppConfig(repoFullName);
        final JenkinsConfig jenkinsConfig = createJenkinsConfig();
        final WebhookController controller = new WebhookController(mockBuilder, appConfig, jenkinsConfig);

        // act
        final ResponseEntity<?> response = controller.postWebhook(webhook);

        // assert
        assertEquals(200, response.getStatusCodeValue());
        verify(mockBuilder).build();
        verify(mockTemplate).getForEntity(isA(URI.class), eq(String.class));
    }

    /* Utility methods */

    private Webhook createTagCreatedPayload(final String repoFullName, final String tagName) {
        final Webhook webhook = new Webhook();
        final Push pushInfo = new Push();
        webhook.setPush(pushInfo);
        final List<Change> changes = new ArrayList<>();
        pushInfo.setChanges(changes);
        final Repository repo = new Repository();
        webhook.setRepository(repo);
        repo.setFullName(repoFullName);
        final Change change = new Change();
        changes.add(change);
        final RefState newState = new RefState();
        change.setNewState(newState);
        newState.setType(WebhookController.CHANGE_TYPE_TAG);
        newState.setName(tagName);
        return webhook;
    }

    private ApplicationConfig createAppConfig(final String repoFullName) {
        final ApplicationConfig appConfig = new ApplicationConfig();
        final Map<String, Map<String, String>> appTriggers = new HashMap<>();
        appConfig.setTriggers(appTriggers);
        final Map<String, String> repoTriggers = new HashMap<>();
        appTriggers.put(repoFullName, repoTriggers);
        repoTriggers.put(WebhookController.TRIGGER_TAG_CREATED, "/some/path");
        return appConfig;
    }

    private JenkinsConfig createJenkinsConfig() {
        final JenkinsConfig jenkinsConfig = new JenkinsConfig();
        jenkinsConfig.setHost("http://jenkins.example.com");
        jenkinsConfig.setUsername("my-username");
        jenkinsConfig.setPassword("my-password");
        return jenkinsConfig;
    }
}