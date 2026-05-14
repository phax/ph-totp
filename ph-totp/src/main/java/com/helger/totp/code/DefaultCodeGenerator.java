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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import com.helger.totp.CTotp;
import com.helger.totp.exception.CodeGenerationException;

/**
 * Default HOTP-style code generator producing zero-padded numeric codes.
 *
 * @author Philip Helger
 */
@NullMarked
public class DefaultCodeGenerator implements ICodeGenerator
{
  private final EHashingAlgorithm m_eAlgorithm;
  private final int m_nDigits;

  public DefaultCodeGenerator ()
  {
    this (EHashingAlgorithm.SHA1, CTotp.DEFAULT_CODE_DIGITS);
  }

  public DefaultCodeGenerator (@NonNull final EHashingAlgorithm eAlgorithm)
  {
    this (eAlgorithm, CTotp.DEFAULT_CODE_DIGITS);
  }

  public DefaultCodeGenerator (@NonNull final EHashingAlgorithm eAlgorithm, final int nDigits)
  {
    if (eAlgorithm == null)
      throw new IllegalArgumentException ("HashingAlgorithm must not be null.");
    if (nDigits < 1)
      throw new IllegalArgumentException ("Number of digits must be higher than 0.");

    m_eAlgorithm = eAlgorithm;
    m_nDigits = nDigits;
  }

  @NonNull
  public final EHashingAlgorithm getAlgorithm ()
  {
    return m_eAlgorithm;
  }

  public final int getDigits ()
  {
    return m_nDigits;
  }

  @NonNull
  private byte [] _generateHash (@NonNull final String sKey, final long nCounter) throws Exception
  {
    final byte [] aData = new byte [8];
    long nValue = nCounter;
    for (int i = 8; i-- > 0; nValue >>>= 8)
      aData[i] = (byte) nValue;

    final byte [] aDecodedKey = new Base32 ().decode (sKey);
    final SecretKeySpec aSignKey = new SecretKeySpec (aDecodedKey, m_eAlgorithm.getHmacAlgorithm ());
    final Mac aMac = Mac.getInstance (m_eAlgorithm.getHmacAlgorithm ());
    aMac.init (aSignKey);
    return aMac.doFinal (aData);
  }

  @NonNull
  private String _getDigitsFromHash (@NonNull final byte [] aHash)
  {
    final int nOffset = aHash[aHash.length - 1] & 0xF;

    long nTruncated = 0;
    for (int i = 0; i < 4; ++i)
    {
      nTruncated <<= 8;
      nTruncated |= (aHash[nOffset + i] & 0xFF);
    }
    nTruncated &= 0x7FFFFFFF;
    nTruncated %= (long) Math.pow (10, m_nDigits);

    final String s = Long.toString (nTruncated);
    return "0".repeat (Math.max (0, m_nDigits - s.length ())) + s;
  }

  @NonNull
  public String generate (@NonNull final String sSecret, final long nCounter) throws CodeGenerationException
  {
    try
    {
      final byte [] aHash = _generateHash (sSecret, nCounter);
      return _getDigitsFromHash (aHash);
    }
    catch (final Exception ex)
    {
      throw new CodeGenerationException ("Failed to generate code. See nested exception.", ex);
    }
  }
}
