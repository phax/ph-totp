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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.helger.totp.code.EHashingAlgorithm;

/**
 * Test class for {@link QrData}.
 *
 * @author Philip Helger
 */
public final class QrDataTest
{
  @Test
  public void testUriGeneration ()
  {
    final QrData aData = new QrData.Builder ().label ("example@example.com")
                                              .secret ("the-secret-here")
                                              .issuer ("AppName AppCorp")
                                              .algorithm (EHashingAlgorithm.SHA256)
                                              .digits (6)
                                              .period (30)
                                              .build ();

    assertEquals ("otpauth://totp/example%40example.com?secret=the-secret-here&issuer=AppName%20AppCorp&algorithm=SHA256&digits=6&period=30",
                  aData.getUri ());
  }

  @Test
  public void testNullFieldUriGeneration ()
  {
    final QrData aData = new QrData.Builder ().label (null)
                                              .secret (null)
                                              .issuer ("AppName AppCorp")
                                              .algorithm (EHashingAlgorithm.SHA256)
                                              .digits (6)
                                              .period (30)
                                              .build ();

    assertEquals ("otpauth://totp/?secret=&issuer=AppName%20AppCorp&algorithm=SHA256&digits=6&period=30", aData.getUri ());
  }
}
