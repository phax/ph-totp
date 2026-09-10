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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    final DefaultCodeVerifier aVerifier = new DefaultCodeVerifier (aFailingGen, aTime).setAllowedTimePeriodDiscrepancy (
                                                                                                                        1);
    assertFalse (aVerifier.isValidCode (sSecret, "1234"));
  }

  @Test
  public void testGetMatchingTimeSlot ()
  {
    final String sSecret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
    final long nTimeToRunAt = 1567975936L;
    final String sCorrectCode = "862707";
    final int nPeriod = 30;

    // The slot the code really belongs to - independent of when it is presented
    final Long aExpectedSlot = Long.valueOf (Math.floorDiv (nTimeToRunAt, nPeriod));

    // Presented one period early, on time, and one period late - always the same slot
    assertEquals (aExpectedSlot, _slot (sSecret, sCorrectCode, nTimeToRunAt - nPeriod, nPeriod));
    assertEquals (aExpectedSlot, _slot (sSecret, sCorrectCode, nTimeToRunAt, nPeriod));
    assertEquals (aExpectedSlot, _slot (sSecret, sCorrectCode, nTimeToRunAt + nPeriod, nPeriod));

    // Outside the window and wrong codes yield null
    assertNull (_slot (sSecret, sCorrectCode, nTimeToRunAt + nPeriod + 15, nPeriod));
    assertNull (_slot (sSecret, "123", nTimeToRunAt, nPeriod));
  }

  @Test
  public void testMatchingTimeSlotEnablesReplayProtection ()
  {
    final String sSecret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
    final long nTimeToRunAt = 1567975936L;
    final String sCorrectCode = "862707";
    final int nPeriod = 30;

    // An application remembers the slot of the last accepted code
    final Long aFirstUse = _slot (sSecret, sCorrectCode, nTimeToRunAt, nPeriod);
    assertNotNull (aFirstUse);

    // Replaying the very same code one period later still verifies - it is inside the
    // discrepancy window - but it resolves to the very same slot, so the application can
    // reject it. Using the current slot instead would yield a higher value and let it through.
    final Long aReplay = _slot (sSecret, sCorrectCode, nTimeToRunAt + nPeriod, nPeriod);
    assertTrue (_verify (sSecret, sCorrectCode, nTimeToRunAt + nPeriod, nPeriod));
    assertEquals (aFirstUse, aReplay);
    assertTrue (aReplay.longValue () <= aFirstUse.longValue ());
  }

  @Test
  public void testGetMatchingTimeSlotGenerationFailureReturnsNull ()
  {
    final String sSecret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
    final ITimeProvider aTime = () -> 1567975936L;
    final ICodeGenerator aFailingGen = (sec, ctr) -> {
      throw new CodeGenerationException ("Test", new RuntimeException ());
    };
    final DefaultCodeVerifier aVerifier = new DefaultCodeVerifier (aFailingGen, aTime).setAllowedTimePeriodDiscrepancy (
                                                                                                                        1);
    assertNull (aVerifier.getMatchingTimeSlot (sSecret, "1234"));
  }

  @Test
  public void testGetCurrentTimeSlot ()
  {
    final ITimeProvider aTime = () -> 1567975936L;
    final DefaultCodeVerifier aVerifier = new DefaultCodeVerifier (new DefaultCodeGenerator (), aTime).setTimePeriod (
                                                                                                                      30);
    assertEquals (Math.floorDiv (1567975936L, 30), aVerifier.getCurrentTimeSlot ());
  }

  private static boolean _verify (final String sSecret, final String sCode, final long nTime, final int nPeriod)
  {
    final ITimeProvider aTime = () -> nTime;
    return new DefaultCodeVerifier (new DefaultCodeGenerator (), aTime).setTimePeriod (nPeriod)
                                                                       .isValidCode (sSecret, sCode);
  }

  private static Long _slot (final String sSecret, final String sCode, final long nTime, final int nPeriod)
  {
    final ITimeProvider aTime = () -> nTime;
    return new DefaultCodeVerifier (new DefaultCodeGenerator (), aTime).setTimePeriod (nPeriod)
                                                                       .getMatchingTimeSlot (sSecret, sCode);
  }
}
