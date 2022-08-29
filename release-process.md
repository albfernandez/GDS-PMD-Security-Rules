# Release Process

This guide provides a chronological steps which goes through release tagging, staging, verification and publishing.

To see the original Jboss Seam guide, see [release-process.txt](release-process.txt)

## Check the SNAPSHOT builds and pass the tests

Check that the project builds in java 8 and java 11.

```bash
mvn clean package install verify 
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/ mvn clean package install verify
```

## Set version and build 

```bash
# change release in pom.xml
mvn clean package install verify
mvn clean package install deploy
git add -A
git commit -S -m 'Release <2.29.0>'
git tag -a <v.2.29.0> -m "Tagging release <2.29.0>"
git push
git push --tags
```


## Prepare next iteration

```bash
# change release in pom.xml to next -SNAPSHOT
git add -A
git commit -S -m 'Next release cycle'
git push
```

## Create release and upload artifacts to Github

Manually creating the release in Github project page, and upload generated artifacts.