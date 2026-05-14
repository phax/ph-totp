/*
 * Copyright (C) 2026 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Forked from samdjstevens/java-totp (Sam Stevens, MIT License); see NOTICE.txt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.totp.secret;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import com.helger.totp.CTotp;

/**
 * Default secret generator backed by {@link SecureRandom} and Base32 encoding.
 *
 * @author Philip Helger
 */
@NullMarked
public class DefaultSecretGenerator implements ISecretGenerator
{
  private static final Base32 ENCODER = new Base32 ();

  private final SecureRandom m_aRandom = new SecureRandom ();
  private final int m_nNumCharacters;

  public DefaultSecretGenerator ()
  {
    this (CTotp.DEFAULT_SECRET_LENGTH);
  }

  /**
   * @param nNumCharacters
   *        The number of Base32 characters the secret should consist of. Must be a multiple of 8 to
   *        avoid Base32 padding.
   */
  public DefaultSecretGenerator (final int nNumCharacters)
  {
    m_nNumCharacters = nNumCharacters;
  }

  public final int getNumCharacters ()
  {
    return m_nNumCharacters;
  }

  @NonNull
  public String generate ()
  {
    // 5 bits per Base32 character
    final byte [] aBytes = new byte [(m_nNumCharacters * 5) / 8];
    m_aRandom.nextBytes (aBytes);
    return new String (ENCODER.encode (aBytes), StandardCharsets.US_ASCII);
  }
}
