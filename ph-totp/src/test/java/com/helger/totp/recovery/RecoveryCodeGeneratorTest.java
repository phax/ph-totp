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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * Test class for {@link RecoveryCodeGenerator}.
 *
 * @author Philip Helger
 */
public final class RecoveryCodeGeneratorTest
{
  @Test
  public void testCorrectAmountGenerated ()
  {
    final String [] aCodes = new RecoveryCodeGenerator ().generateCodes (16);
    assertEquals (16, aCodes.length);
    for (final String sCode : aCodes)
      assertNotNull (sCode);
  }

  @Test
  public void testCodesMatchFormat ()
  {
    final String [] aCodes = new RecoveryCodeGenerator ().generateCodes (16);
    for (final String sCode : aCodes)
      assertTrue (sCode, sCode.matches ("[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}"));
  }

  @Test
  public void testCodesAreUnique ()
  {
    final String [] aCodes = new RecoveryCodeGenerator ().generateCodes (25);
    final Set <String> aUnique = new HashSet <> (Arrays.asList (aCodes));
    assertEquals (25, aUnique.size ());
  }

  @Test
  public void testInvalidNumberThrowsException ()
  {
    try
    {
      new RecoveryCodeGenerator ().generateCodes (-1);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
