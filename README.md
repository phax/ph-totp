# ph-totp

[![javadoc](https://javadoc.io/badge2/com.helger/ph-totp/javadoc.svg)](https://javadoc.io/doc/com.helger/ph-totp)
[![Maven Central](https://img.shields.io/maven-central/v/com.helger/ph-totp.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.helger%22%20AND%20a:%22ph-totp%22)

A Java 17+ library for generating and verifying [Time-based One-Time
Passwords (TOTP)](https://datatracker.ietf.org/doc/html/rfc6238) for Multi-Factor
Authentication, plus an optional ZXing-based QR-code image generator.

Forked and refactored from [`samdjstevens/java-totp`](https://github.com/samdjstevens/java-totp)
v1.7.1 (MIT). See [`NOTICE.txt`](NOTICE.txt) for upstream attribution.

## Modules

| Module | Description | Compile-scope deps |
| ------ | ----------- | ------------------ |
| `ph-totp` | Secret/code/time/recovery logic and the `otpauth://` URI builder (`QrData`). No image dependencies. | `commons-codec`, `commons-net` (optional, only for `NtpTimeProvider`) |
| `ph-totp-qrcode` | ZXing-based PNG QR code image generator and `data:` URI helper. | `ph-totp`, `zxing-core`, `zxing-javase` |

## Maven coordinates

```xml
<dependency>
  <groupId>com.helger</groupId>
  <artifactId>ph-totp</artifactId>
  <version>2.0.0</version>
</dependency>

<!-- only if you need QR-code image generation -->
<dependency>
  <groupId>com.helger</groupId>
  <artifactId>ph-totp-qrcode</artifactId>
  <version>2.0.0</version>
</dependency>
```

## Usage

### Generate a shared secret

```java
ISecretGenerator aGen = new DefaultSecretGenerator ();
String sSecret = aGen.generate ();
// sSecret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV"
```

`DefaultSecretGenerator(int)` accepts a custom Base32 character count
(multiple of 8 recommended to avoid padding).

### Build the otpauth:// URI

```java
QrData aData = new QrData.Builder ()
    .label ("example@example.com")
    .secret (sSecret)
    .issuer ("AppName")
    .algorithm (EHashingAlgorithm.SHA1)
    .digits (6)
    .period (30)
    .build ();
String sUri = aData.getUri ();
```

### Generate a PNG QR code (requires `ph-totp-qrcode`)

```java
IQrCodeImageGenerator aImg = new ZxingPngQrCodeImageGenerator ();
byte [] aPng = aImg.generate (aData);
String sDataUri = DataUriEncoder.getDataUriForImage (aPng, aImg.getImageMimeType ());
// embed in HTML: <img src="${sDataUri}" />
```

### Verify a user-submitted code

```java
ITimeProvider aTime = new SystemTimeProvider ();
ICodeGenerator aCodeGen = new DefaultCodeGenerator ();
ICodeVerifier aVerifier = new DefaultCodeVerifier (aCodeGen, aTime);
boolean bOk = aVerifier.isValidCode (sSecret, sUserSubmittedCode);
```

`DefaultCodeVerifier` defaults to a 30-second period and ±1 bucket discrepancy
window. Both can be configured via fluent setters. The hashing algorithm and
digit count **must match** what was encoded in the `otpauth://` URI.

The verifier loops over the full discrepancy window even after a successful
match (avoids timing leaks) and uses constant-time byte comparison.

### NTP time provider

If the system clock is unreliable, fetch time from an NTP server. Requires the
optional `commons-net` dependency on the classpath at runtime:

```java
ITimeProvider aTime = new NtpTimeProvider ("pool.ntp.org");
```

### Recovery codes

```java
String [] aCodes = new RecoveryCodeGenerator ().generateCodes (16);
// e.g. "tf8i-exmo-3lcb-slkm", "boyv-yq75-z99k-r308", ...
```

16 lowercase alphanumeric characters in 4 dash-separated groups; ~82 bits of
entropy. Storage, hashing, and association with users are the application's
responsibility.

## Build

```sh
mvn clean install
```

Skip GPG signing for local builds:

```sh
mvn -Dgpg.skip=true clean install
```

## License

Apache License 2.0. See [`LICENSE.txt`](LICENSE.txt). The original `java-totp`
codebase by Sam Stevens was MIT-licensed; that attribution is preserved in
[`NOTICE.txt`](NOTICE.txt).


# News and Noteworthy

v2.0.0 - 2026-05-14
* Forked from `samdjstevens/java-totp` v1.7.1 and relicensed Apache 2.0 (original MIT terms preserved in `NOTICE.txt`)
* Java baseline raised to 17 (parent: `com.helger:parent-pom:3.0.3`)
* Group ID changed to `com.helger`; root package changed to `com.helger.totp`
* Split into two modules: `ph-totp` (logic + `otpauth://` URI builder, no image deps) and `ph-totp-qrcode` (ZXing PNG generator + `data:` URI helper)
* Removed the `totp-spring-boot-starter` module
* Removed the CircleCI configuration
* Interfaces re-prefixed (`ICodeGenerator`, `ICodeVerifier`, `ITimeProvider`, `ISecretGenerator`, `IQrCodeImageGenerator`); enum re-prefixed (`EHashingAlgorithm`)
* Renamed `ZxingPngQrGenerator` → `ZxingPngQrCodeImageGenerator`; moved to `ph-totp-qrcode`
* Renamed `Utils.getDataUriForImage` → `DataUriEncoder.getDataUriForImage`; moved to `ph-totp-qrcode`
* New constants holder `CTotp` for defaults (time period 30 s, discrepancy 1, code digits 6, secret length 32)
* `TotpInfo` class removed
* `InvalidParameterException` replaced with `IllegalArgumentException` in argument-validation paths
* Applied ph- code style throughout (Hungarian notation, JSpecify annotations, Allman braces)
* Tests migrated from JUnit 5 + Mockito to JUnit 4 with hand-rolled fakes
* Dependency bumps: `commons-codec` → 1.22.0, `commons-net` → 3.13.0, `zxing` → 3.5.4
* Added `org.jspecify:jspecify` 1.0.0 as a compile dependency

v1.7.1 and earlier - upstream `samdjstevens/java-totp`. See its repository for history.
