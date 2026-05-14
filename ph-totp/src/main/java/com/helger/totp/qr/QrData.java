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
package com.helger.totp.qr;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import com.helger.totp.CTotp;
import com.helger.totp.code.EHashingAlgorithm;

/**
 * Immutable description of a TOTP enrollment, rendered as the <code>otpauth://totp/</code> URI per
 * the <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">Google
 * Authenticator Key URI Format</a>.
 *
 * @author Philip Helger
 */
@NullMarked
public class QrData
{
  private static final String TYPE = "totp";

  private final @Nullable String m_sLabel;
  private final @Nullable String m_sSecret;
  private final @Nullable String m_sIssuer;
  private final String m_sAlgorithm;
  private final int m_nDigits;
  private final int m_nPeriod;

  private QrData (@Nullable final String sLabel,
                  @Nullable final String sSecret,
                  @Nullable final String sIssuer,
                  @NonNull final String sAlgorithm,
                  final int nDigits,
                  final int nPeriod)
  {
    m_sLabel = sLabel;
    m_sSecret = sSecret;
    m_sIssuer = sIssuer;
    m_sAlgorithm = sAlgorithm;
    m_nDigits = nDigits;
    m_nPeriod = nPeriod;
  }

  @NonNull
  public String getType ()
  {
    return TYPE;
  }

  @Nullable
  public String getLabel ()
  {
    return m_sLabel;
  }

  @Nullable
  public String getSecret ()
  {
    return m_sSecret;
  }

  @Nullable
  public String getIssuer ()
  {
    return m_sIssuer;
  }

  @NonNull
  public String getAlgorithm ()
  {
    return m_sAlgorithm;
  }

  public int getDigits ()
  {
    return m_nDigits;
  }

  public int getPeriod ()
  {
    return m_nPeriod;
  }

  /**
   * @return The <code>otpauth://</code> URI representing this enrollment.
   */
  @NonNull
  public String getUri ()
  {
    return "otpauth://" +
           _uriEncode (TYPE) +
           "/" +
           _uriEncode (m_sLabel) +
           "?" +
           "secret=" +
           _uriEncode (m_sSecret) +
           "&issuer=" +
           _uriEncode (m_sIssuer) +
           "&algorithm=" +
           _uriEncode (m_sAlgorithm) +
           "&digits=" +
           m_nDigits +
           "&period=" +
           m_nPeriod;
  }

  @NonNull
  private static String _uriEncode (@Nullable final String sText)
  {
    if (sText == null)
      return "";
    return URLEncoder.encode (sText, StandardCharsets.UTF_8).replace ("+", "%20");
  }

  public static class Builder
  {
    private @Nullable String m_sLabel;
    private @Nullable String m_sSecret;
    private @Nullable String m_sIssuer;
    private EHashingAlgorithm m_eAlgorithm = EHashingAlgorithm.SHA1;
    private int m_nDigits = CTotp.DEFAULT_CODE_DIGITS;
    private int m_nPeriod = CTotp.DEFAULT_TIME_PERIOD_SECS;

    @NonNull
    public Builder label (@Nullable final String sLabel)
    {
      m_sLabel = sLabel;
      return this;
    }

    @NonNull
    public Builder secret (@Nullable final String sSecret)
    {
      m_sSecret = sSecret;
      return this;
    }

    @NonNull
    public Builder issuer (@Nullable final String sIssuer)
    {
      m_sIssuer = sIssuer;
      return this;
    }

    @NonNull
    public Builder algorithm (final EHashingAlgorithm eAlgorithm)
    {
      m_eAlgorithm = eAlgorithm;
      return this;
    }

    @NonNull
    public Builder digits (final int nDigits)
    {
      m_nDigits = nDigits;
      return this;
    }

    @NonNull
    public Builder period (final int nPeriod)
    {
      m_nPeriod = nPeriod;
      return this;
    }

    @NonNull
    public QrData build ()
    {
      return new QrData (m_sLabel, m_sSecret, m_sIssuer, m_eAlgorithm.getFriendlyName (), m_nDigits, m_nPeriod);
    }
  }
}
