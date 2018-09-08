# CrystalShard
## Forging a Discord Library

Support Discord Server: https://discord.gg/QrgfQG3

CrystalShard is a free to use discord bot library, built upon Java 11.
We do not have a Java 1.8 version yet. But until that one is ready, be sure to take a long look at http://javacord.org!

How to use:
## Gradle
### build.gradle
```
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        implementation 'com.github.CrystalShardDiscord:CrystalShard:development-SNAPSHOT'
	}
```

## Maven
### pom.xml
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	<dependency>
	    <groupId>com.github.CrystalShardDiscord</groupId>
	    <artifactId>CrystalShard</artifactId>
	    <version>development-SNAPSHOT</version>
	</dependency>
```
