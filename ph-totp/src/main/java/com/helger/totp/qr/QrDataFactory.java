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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import com.helger.totp.code.EHashingAlgorithm;

/**
 * Produces preconfigured {@link QrData.Builder} instances with shared defaults for algorithm, digit
 * count, and period.
 *
 * @author Philip Helger
 */
@NullMarked
public class QrDataFactory
{
  private final EHashingAlgorithm m_eDefaultAlgorithm;
  private final int m_nDefaultDigits;
  private final int m_nDefaultTimePeriod;

  public QrDataFactory (@NonNull final EHashingAlgorithm eDefaultAlgorithm,
                        final int nDefaultDigits,
                        final int nDefaultTimePeriod)
  {
    m_eDefaultAlgorithm = eDefaultAlgorithm;
    m_nDefaultDigits = nDefaultDigits;
    m_nDefaultTimePeriod = nDefaultTimePeriod;
  }

  @NonNull
  public final EHashingAlgorithm getDefaultAlgorithm ()
  {
    return m_eDefaultAlgorithm;
  }

  public final int getDefaultDigits ()
  {
    return m_nDefaultDigits;
  }

  public final int getDefaultTimePeriod ()
  {
    return m_nDefaultTimePeriod;
  }

  public QrData.@NonNull Builder newBuilder ()
  {
    return new QrData.Builder ().algorithm (m_eDefaultAlgorithm)
                                .digits (m_nDefaultDigits)
                                .period (m_nDefaultTimePeriod);
  }
}
