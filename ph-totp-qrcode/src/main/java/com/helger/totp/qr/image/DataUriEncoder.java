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

import java.util.Base64;

import org.jspecify.annotations.NullMarked;

/**
 * Encodes raw image bytes as an RFC 2397 <code>data:</code> URI for direct embedding in HTML/CSS.
 *
 * @author Philip Helger
 */
@NullMarked
public final class DataUriEncoder
{
  private DataUriEncoder ()
  {}

  /**
   * @param aData
   *        The raw bytes of the image.
   * @param sMimeType
   *        The MIME type of the image, e.g. <code>image/png</code>.
   * @return A <code>data:</code> URI representing the image.
   */
  public static String getDataUriForImage (final byte [] aData, final String sMimeType)
  {
    return "data:" + sMimeType + ";base64," + Base64.getEncoder ().encodeToString (aData);
  }
}
