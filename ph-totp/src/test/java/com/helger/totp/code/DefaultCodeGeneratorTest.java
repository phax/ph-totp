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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.helger.totp.exception.CodeGenerationException;

/**
 * Test class for {@link DefaultCodeGenerator}.
 *
 * @author Philip Helger
 */
public final class DefaultCodeGeneratorTest
{
  @Test
  public void testCodeIsGenerated () throws CodeGenerationException
  {
    _assertCode ("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536, EHashingAlgorithm.SHA1, "082371");
    _assertCode ("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536, EHashingAlgorithm.SHA256, "272978");
    _assertCode ("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536, EHashingAlgorithm.SHA512, "325200");
    _assertCode ("makrzl2hict4ojeji2iah4kndmq6sgka", 1582750403, EHashingAlgorithm.SHA1, "848586");
    _assertCode ("makrzl2hict4ojeji2iah4kndmq6sgka", 1582750403, EHashingAlgorithm.SHA256, "965726");
    _assertCode ("makrzl2hict4ojeji2iah4kndmq6sgka", 1582750403, EHashingAlgorithm.SHA512, "741306");
  }

  @Test
  public void testDigitLength () throws CodeGenerationException
  {
    DefaultCodeGenerator aGen = new DefaultCodeGenerator (EHashingAlgorithm.SHA1);
    assertEquals (6, aGen.generate ("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536L).length ());

    aGen = new DefaultCodeGenerator (EHashingAlgorithm.SHA1, 8);
    assertEquals (8, aGen.generate ("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536L).length ());

    aGen = new DefaultCodeGenerator (EHashingAlgorithm.SHA1, 4);
    assertEquals (4, aGen.generate ("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536L).length ());
  }

  @Test
  public void testInvalidHashingAlgorithmThrowsException ()
  {
    try
    {
      new DefaultCodeGenerator (null, 6);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testInvalidDigitLengthThrowsException ()
  {
    try
    {
      new DefaultCodeGenerator (EHashingAlgorithm.SHA1, 0);
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }

  @Test
  public void testInvalidKeyThrowsCodeGenerationException ()
  {
    final DefaultCodeGenerator aGen = new DefaultCodeGenerator (EHashingAlgorithm.SHA1, 4);
    try
    {
      // Empty Base32 input → empty key → SecretKeySpec rejects empty key
      aGen.generate ("", 1567631536L);
      fail ();
    }
    catch (final CodeGenerationException ex)
    {
      assertNotNull (ex.getCause ());
    }
  }

  private static void _assertCode (final String sSecret,
                                   final int nTime,
                                   final EHashingAlgorithm eAlgo,
                                   final String sExpected) throws CodeGenerationException
  {
    final long nBucket = Math.floorDiv (nTime, 30);
    final DefaultCodeGenerator aGen = new DefaultCodeGenerator (eAlgo);
    assertEquals (sExpected, aGen.generate (sSecret, nBucket));
  }
}
