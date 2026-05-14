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
package com.helger.totp.qr.image;

import static com.helger.totp.qr.image.DataUriEncoder.getDataUriForImage;
import static org.junit.Assert.assertEquals;

import java.util.Base64;

import org.junit.Test;

/**
 * Test class for {@link DataUriEncoder}.
 *
 * @author Philip Helger
 */
public final class DataUriEncoderTest
{
  @Test
  public void testDataUriEncode ()
  {
    // 1x1 white-pixel PNG, Base64-encoded
    final String sPngImage = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII=";
    final byte [] aImageData = Base64.getDecoder ().decode (sPngImage);

    assertEquals ("data:image/png;base64," + sPngImage, getDataUriForImage (aImageData, "image/png"));
  }
}
