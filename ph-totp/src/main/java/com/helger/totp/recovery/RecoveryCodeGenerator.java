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
package com.helger.totp.recovery;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import org.jspecify.annotations.NullMarked;

/**
 * Generates MFA recovery codes.
 * <p>
 * Codes are 16 lowercase alphanumeric characters split into 4 dash-separated groups
 * (e.g. <code>4ckn-xspn-et8t-xgr0</code>), giving ~82 bits of entropy:
 * <code>log(36^16) / log(2) ≈ 82.7</code>.
 *
 * @author Philip Helger
 */
@NullMarked
public class RecoveryCodeGenerator
{
  private static final char [] CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray ();
  private static final int CODE_LENGTH = 16;
  private static final int GROUPS_NBR = 4;

  private final Random m_aRandom = new SecureRandom ();

  public String [] generateCodes (final int nAmount)
  {
    if (nAmount < 1)
      throw new IllegalArgumentException ("Amount must be at least 1.");

    final String [] aCodes = new String [nAmount];
    Arrays.setAll (aCodes, i -> _generateCode ());
    return aCodes;
  }

  private String _generateCode ()
  {
    final StringBuilder aCode = new StringBuilder (CODE_LENGTH + (CODE_LENGTH / GROUPS_NBR) - 1);
    for (int i = 0; i < CODE_LENGTH; i++)
    {
      aCode.append (CHARACTERS[m_aRandom.nextInt (CHARACTERS.length)]);
      if ((i + 1) % GROUPS_NBR == 0 && (i + 1) != CODE_LENGTH)
        aCode.append ('-');
    }
    return aCode.toString ();
  }
}
