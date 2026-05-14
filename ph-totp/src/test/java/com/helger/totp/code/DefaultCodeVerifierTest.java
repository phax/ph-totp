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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.totp.exception.CodeGenerationException;
import com.helger.totp.time.ITimeProvider;

/**
 * Test class for {@link DefaultCodeVerifier}.
 *
 * @author Philip Helger
 */
public final class DefaultCodeVerifierTest
{
  @Test
  public void testCodeIsValid ()
  {
    final String sSecret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
    final long nTimeToRunAt = 1567975936L;
    final String sCorrectCode = "862707";
    final int nPeriod = 30;

    // allow for a +/- ~30 second discrepancy
    assertTrue (_verify (sSecret, sCorrectCode, nTimeToRunAt - nPeriod, nPeriod));
    assertTrue (_verify (sSecret, sCorrectCode, nTimeToRunAt, nPeriod));
    assertTrue (_verify (sSecret, sCorrectCode, nTimeToRunAt + nPeriod, nPeriod));

    // but no more
    assertFalse (_verify (sSecret, sCorrectCode, nTimeToRunAt + nPeriod + 15, nPeriod));

    // wrong code fails
    assertFalse (_verify (sSecret, "123", nTimeToRunAt, nPeriod));
  }

  @Test
  public void testCodeGenerationFailureReturnsFalse ()
  {
    final String sSecret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
    final ITimeProvider aTime = () -> 1567975936L;
    final ICodeGenerator aFailingGen = (sec, ctr) -> {
      throw new CodeGenerationException ("Test", new RuntimeException ());
    };
    final DefaultCodeVerifier aVerifier = new DefaultCodeVerifier (aFailingGen, aTime).setAllowedTimePeriodDiscrepancy (1);
    assertFalse (aVerifier.isValidCode (sSecret, "1234"));
  }

  private static boolean _verify (final String sSecret, final String sCode, final long nTime, final int nPeriod)
  {
    final ITimeProvider aTime = () -> nTime;
    return new DefaultCodeVerifier (new DefaultCodeGenerator (), aTime).setTimePeriod (nPeriod).isValidCode (sSecret, sCode);
  }
}
