# tqks-webpage-scraper
Simple scraper which grabs the social network of a given URL out to a limited number of links and creates JSON files of title, url, and outbound links

This project requires a couple of topicquests maven dependencies which are not in a maven repo yet. They must be cloned and installed with maven:
* git@github.com:topicquests/tqks-neo4j-client.git
* git@github.com:topicquests/tq-support.git

To compile:
* mvn clean install -DskipTests -Dgpg.skip
