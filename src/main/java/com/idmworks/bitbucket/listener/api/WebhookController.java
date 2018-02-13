package com.idmworks.bitbucket.listener.api;

import com.idmworks.bitbucket.listener.models.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

@RestController
public class WebhookController {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public ResponseEntity<?> postWebhook(@RequestBody final Webhook webhook) {
        logger.info(webhook.toString());
        return new ResponseEntity<>( HttpStatus.OK );
    }
}
