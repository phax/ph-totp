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

import java.io.ByteArrayOutputStream;

import org.jspecify.annotations.NullMarked;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.helger.totp.exception.QrGenerationException;
import com.helger.totp.qr.QrData;

/**
 * ZXing-based PNG QR code image generator.
 *
 * @author Philip Helger
 */
@NullMarked
public class ZxingPngQrCodeImageGenerator implements IQrCodeImageGenerator
{
  public static final String MIME_TYPE = "image/png";
  public static final int DEFAULT_IMAGE_SIZE = 350;

  private final Writer m_aWriter;
  private int m_nImageSize = DEFAULT_IMAGE_SIZE;

  public ZxingPngQrCodeImageGenerator ()
  {
    this (new QRCodeWriter ());
  }

  public ZxingPngQrCodeImageGenerator (final Writer aWriter)
  {
    m_aWriter = aWriter;
  }

  public final int getImageSize ()
  {
    return m_nImageSize;
  }

  public final ZxingPngQrCodeImageGenerator setImageSize (final int nImageSize)
  {
    m_nImageSize = nImageSize;
    return this;
  }

  @Override
  public String getImageMimeType ()
  {
    return MIME_TYPE;
  }

  @Override
  public byte [] generate (final QrData aData) throws QrGenerationException
  {
    try
    {
      final BitMatrix aMatrix = m_aWriter.encode (aData.getUri (), BarcodeFormat.QR_CODE, m_nImageSize, m_nImageSize);
      try (ByteArrayOutputStream aOut = new ByteArrayOutputStream ())
      {
        MatrixToImageWriter.writeToStream (aMatrix, "PNG", aOut);
        return aOut.toByteArray ();
      }
    }
    catch (final Exception ex)
    {
      throw new QrGenerationException ("Failed to generate QR code. See nested exception.", ex);
    }
  }
}
