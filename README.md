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
