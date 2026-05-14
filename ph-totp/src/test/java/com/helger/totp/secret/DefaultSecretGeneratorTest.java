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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link DefaultSecretGenerator}.
 *
 * @author Philip Helger
 */
public final class DefaultSecretGeneratorTest
{
  @Test
  public void testSecretGenerated ()
  {
    final String sSecret = new DefaultSecretGenerator ().generate ();
    assertNotNull (sSecret);
    assertTrue (sSecret.length () > 0);
  }

  @Test
  public void testCharacterLengths ()
  {
    for (final int nCharCount : new int [] { 16, 32, 64, 128 })
    {
      final String sSecret = new DefaultSecretGenerator (nCharCount).generate ();
      assertEquals (nCharCount, sSecret.length ());
    }
  }

  @Test
  public void testValidBase32Encoded ()
  {
    final String sSecret = new DefaultSecretGenerator ().generate ();
    // A-Z, 2-7, optional padding =
    assertTrue (sSecret, sSecret.matches ("^[A-Z2-7]+=*$"));
    // length must be a multiple of 8
    assertEquals (0, sSecret.length () % 8);
  }
}
