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
 * Test class for {@link QrDataFactory}.
 *
 * @author Philip Helger
 */
public final class QrDataFactoryTest
{
  @Test
  public void testFactorySetsDefaultsOnBuilder ()
  {
    final QrDataFactory aFactory = new QrDataFactory (EHashingAlgorithm.SHA256, 6, 30);
    final QrData aData = aFactory.newBuilder ().build ();

    assertEquals (EHashingAlgorithm.SHA256.getFriendlyName (), aData.getAlgorithm ());
    assertEquals (6, aData.getDigits ());
    assertEquals (30, aData.getPeriod ());
  }
}
