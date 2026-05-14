# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

`ph-totp` — a Java 17+ TOTP library (RFC 6238) split into two modules:

- `ph-totp` — TOTP secret/code/time/recovery logic and the `otpauth://` URI builder. **No image dependencies.**
- `ph-totp-qrcode` — ZXing-based PNG QR code image generator + `data:` URI helper.

Maven coordinates: `com.helger:ph-totp` and `com.helger:ph-totp-qrcode`. Parent: `com.helger:parent-pom` (Java 17). Originated as a refactor of `samdjstevens/java-totp` 1.7.1 (MIT → Apache 2.0; see `NOTICE.txt`).

## Build & Test

- Build everything: `mvn -Dgpg.skip=true clean install`
- Run tests for one module: `mvn -pl ph-totp -Dgpg.skip=true test`
- Run a single test class: `mvn -pl ph-totp -Dgpg.skip=true -Dtest=DefaultCodeVerifierTest test`
- Run a single test method: `mvn -pl ph-totp -Dgpg.skip=true -Dtest=DefaultCodeVerifierTest#testCodeIsValid test`
- Build only the core (skip qrcode): `mvn -pl ph-totp -am -Dgpg.skip=true package`

`-Dgpg.skip=true` is needed locally because `parent-pom`'s `release` profile wires GPG signing. The build also pulls plugin versions (license-maven-plugin, ph-buildinfo-maven-plugin, central-publishing-maven-plugin, jacoco coverage profile, etc.) from `com.helger:parent-pom`; do not redeclare those locally.

## Architecture

Same pipeline as before, now split across two modules:

1. `ISecretGenerator` / `DefaultSecretGenerator` — Base32 secret from `SecureRandom`. Length is in characters; bytes = `length * 5 / 8`. Multiple of 8 recommended to avoid Base32 padding.
2. `QrData` + `QrData.Builder` (in `ph-totp`, **no image deps**) — builds `otpauth://totp/...` URI per Google Authenticator Key URI Format. `QrDataFactory` produces preconfigured builders.
3. `IQrCodeImageGenerator` / `ZxingPngQrCodeImageGenerator` (in `ph-totp-qrcode`) — encodes the URI as a PNG via ZXing. `DataUriEncoder.getDataUriForImage` produces an RFC 2397 `data:` URI for HTML embedding.
4. `ICodeGenerator` / `DefaultCodeGenerator` — HMAC-based code generation. Algorithm (`EHashingAlgorithm.SHA1/256/512`) and digit count must match what was encoded in the QR URI, otherwise verification silently fails.
5. `ICodeVerifier` / `DefaultCodeVerifier` — given a `ICodeGenerator` and `ITimeProvider`, compares user-submitted code against codes for the current bucket ± `allowedTimePeriodDiscrepancy`. Two invariants are intentional security choices, **do not collapse them**:
   - The verifier iterates over the full discrepancy window even after a match (avoids timing leaks).
   - Comparison is constant-time over the byte representation.
6. `ITimeProvider` — `SystemTimeProvider` uses `Instant.now().getEpochSecond()`. `NtpTimeProvider` uses Apache Commons Net (declared `<optional>true</optional>`); it probes for `org.apache.commons.net.ntp.NTPUDPClient` at construction time and throws `RuntimeException` if missing.
7. `RecoveryCodeGenerator` — 16 lowercase alphanumeric chars in 4 dash-separated groups, ~82 bits of entropy. Format constants are intentionally not configurable.

Exceptions live in `com.helger.totp.exception`: `CodeGenerationException` and `TimeProviderException` ship with `ph-totp`; `QrGenerationException` ships with `ph-totp-qrcode`. `DefaultCodeVerifier` deliberately swallows `CodeGenerationException` and returns `false` — propagating it would let an attacker probe for malformed secrets.

`CTotp` holds defaults: time period 30 s, discrepancy 1, code digits 6, secret length 32.

## Code Style

Standard ph- conventions — matches sibling repos (`ph-css`, `ph-asic`, etc.):

- **Hungarian notation** on all variables: `s` String, `n` int/long, `b` boolean, `e` enum, `a` object/array. Prefix `m_` for instance fields, `s_` for static fields (uppercase constants are exempt). Method parameters are always `final`.
- **JSpecify annotations**: `@NullMarked` at type/package level, `@Nullable` for nullable fields/params.
- **Allman braces**, space before parentheses, 2-space indent.
- **Apache 2.0 license header** on every Java file (matches `src/etc/license-template.txt`). Enforced by `com.mycila:license-maven-plugin`.
- **JUnit 4** tests, simple hand-rolled fakes — no Mockito. Tests are `public final class` named `*Test`.
- Type prefixes: interfaces `I`, enums `E`, abstract classes `Abstract`. Constants classes `C` (e.g. `CTotp`).
- Methods returning `this` for fluent setters.
- ID is fully uppercase (`getMimeType`, not `getMimeTYpe` — but `getID()` not `getId()` when "Id" appears).

## Dependencies (managed in parent pom)

- `commons-codec` 1.18.0 — Base32 encoding
- `commons-net` 3.11.1 — **optional**, only for `NtpTimeProvider`
- `com.google.zxing:core` + `:javase` 3.5.3 — QR rendering (qrcode module only)
- `org.jspecify:jspecify` 1.0.0 — null annotations
- `junit` 4.13.2 (managed by parent-pom), `slf4j-simple` for tests
- Plugin versions and `slf4j-api` come from `com.helger:parent-pom` `dependencyManagement`/`pluginManagement` — do not redeclare.

## Conventions when extending

- **Do not add `ph-commons` as a dependency.** This library is intentionally tiny — `ValueEnforcer`/`ToStringGenerator` would inflate the dep tree. Stdlib only.
- New image formats (e.g. SVG): add a sibling `*QrCodeImageGenerator` in `ph-totp-qrcode` implementing `IQrCodeImageGenerator`. Keep the URI builder (`QrData`) in `ph-totp` — it has no image dependency.
- Anything depending on `commons-net` must declare it `<optional>true</optional>`.
