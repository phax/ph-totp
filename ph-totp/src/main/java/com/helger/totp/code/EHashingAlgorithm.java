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
package com.helger.totp.code;

import org.jspecify.annotations.NullMarked;

/**
 * Hashing algorithms supported by the TOTP code generator. The friendly name matches the
 * <code>algorithm</code> field of an <code>otpauth://</code> URI per the Google Authenticator
 * Key URI Format.
 *
 * @author Philip Helger
 */
@NullMarked
public enum EHashingAlgorithm
{
  SHA1 ("HmacSHA1", "SHA1"),
  SHA256 ("HmacSHA256", "SHA256"),
  SHA512 ("HmacSHA512", "SHA512");

  private final String m_sHmacAlgorithm;
  private final String m_sFriendlyName;

  EHashingAlgorithm (final String sHmacAlgorithm, final String sFriendlyName)
  {
    m_sHmacAlgorithm = sHmacAlgorithm;
    m_sFriendlyName = sFriendlyName;
  }

  public String getHmacAlgorithm ()
  {
    return m_sHmacAlgorithm;
  }

  public String getFriendlyName ()
  {
    return m_sFriendlyName;
  }
}
