# CrystalShard
## Forging a Discord Library

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