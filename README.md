# objcopier

Copy Java Object fields from multiple sources into a target with option for greater value. Ignores null fields. Most useful for model update with PATCH method.

___

## Table of content
- [Why](#why)
- [Installation](#installation)
    - [Maven](#maven)
    - [Gradle](#gradle)

## Why

## Installation

Download the jar file from the [releases](https://github.com/thehackersdeck/objcopier/releases) and add the downloaded konfiger-$.jar to your java or android project class path or library folder.

### Maven

Add the following repository and dependency detail to your pom.xml

Using mvn-repo:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.thecarisma</groupId>
        <artifactId>objcopier</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>objcopier</id>
        <url>https://raw.github.com/thehackersdeck/objcopier/mvn-repo/</url>
    </repository>
</repositories>
```

Using jitpack.io:

```xml
<dependencies>
    <dependency>
        <groupId>com.github.thehackersdeck</groupId>
        <artifactId>objcopier</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### Gradle

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:

```gradle
dependencies {
        implementation 'com.github.thehackersdeck:objcopier:1.0'
}
```

### Usage

Copy two from a source into target

```java
ObjCopier.copyFields(target, source);
```

Copy two from multiple sources into a single target.

```java
ObjCopier.copyFields(target, source1, source2, source);
```