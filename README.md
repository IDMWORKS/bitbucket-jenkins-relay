# Bitbucket Jenkins Relay

This application serves as an HTTP server that listens for webhook requests from Bitbucket and then triggers parameterized builds in Jenkins. See `application-default.yml` for more information on configuring the mapping between Bitbucket webhooks and the triggered jobs in Jenkins.

## Building

Use Maven to package the build:

```
mvn package
```

## Running

After packaging, edit the `application-default.yml` file found alongside the `.jar` file and then run the server using `java -jar`:

```
java -jar bitbucket-jenkins-relay-1.0.0.jar
```
