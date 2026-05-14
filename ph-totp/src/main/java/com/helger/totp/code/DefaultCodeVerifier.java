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

import java.nio.charset.StandardCharsets;

import org.jspecify.annotations.NullMarked;

import com.helger.totp.CTotp;
import com.helger.totp.exception.CodeGenerationException;
import com.helger.totp.time.ITimeProvider;

/**
 * Default TOTP code verifier with configurable time period and discrepancy window.
 * <p>
 * Two security invariants are intentional:
 * <ul>
 *   <li>The verifier always iterates over the full discrepancy window, even after a successful
 *   match, to avoid timing leaks.</li>
 *   <li>Code comparison is constant-time on the byte representation.</li>
 * </ul>
 *
 * @author Philip Helger
 */
@NullMarked
public class DefaultCodeVerifier implements ICodeVerifier
{
  private final ICodeGenerator m_aCodeGenerator;
  private final ITimeProvider m_aTimeProvider;
  private int m_nTimePeriod = CTotp.DEFAULT_TIME_PERIOD_SECS;
  private int m_nAllowedTimePeriodDiscrepancy = CTotp.DEFAULT_TIME_PERIOD_DISCREPANCY;

  public DefaultCodeVerifier (final ICodeGenerator aCodeGenerator, final ITimeProvider aTimeProvider)
  {
    m_aCodeGenerator = aCodeGenerator;
    m_aTimeProvider = aTimeProvider;
  }

  public final int getTimePeriod ()
  {
    return m_nTimePeriod;
  }

  public final DefaultCodeVerifier setTimePeriod (final int nTimePeriod)
  {
    m_nTimePeriod = nTimePeriod;
    return this;
  }

  public final int getAllowedTimePeriodDiscrepancy ()
  {
    return m_nAllowedTimePeriodDiscrepancy;
  }

  public final DefaultCodeVerifier setAllowedTimePeriodDiscrepancy (final int nAllowedTimePeriodDiscrepancy)
  {
    m_nAllowedTimePeriodDiscrepancy = nAllowedTimePeriodDiscrepancy;
    return this;
  }

  @Override
  public boolean isValidCode (final String sSecret, final String sCode)
  {
    final long nCurrentBucket = Math.floorDiv (m_aTimeProvider.getTime (), m_nTimePeriod);

    // Iterate over the full window even after a match — avoids timing leak
    boolean bSuccess = false;
    for (int i = -m_nAllowedTimePeriodDiscrepancy; i <= m_nAllowedTimePeriodDiscrepancy; i++)
      bSuccess = _checkCode (sSecret, nCurrentBucket + i, sCode) || bSuccess;
    return bSuccess;
  }

  private boolean _checkCode (final String sSecret, final long nCounter, final String sCode)
  {
    try
    {
      final String sActualCode = m_aCodeGenerator.generate (sSecret, nCounter);
      return _timeSafeStringComparison (sActualCode, sCode);
    }
    catch (final CodeGenerationException ex)
    {
      return false;
    }
  }

  private static boolean _timeSafeStringComparison (final String sA, final String sB)
  {
    final byte [] aA = sA.getBytes (StandardCharsets.UTF_8);
    final byte [] aB = sB.getBytes (StandardCharsets.UTF_8);
    if (aA.length != aB.length)
      return false;

    int nResult = 0;
    for (int i = 0; i < aA.length; i++)
      nResult |= aA[i] ^ aB[i];
    return nResult == 0;
  }
}
