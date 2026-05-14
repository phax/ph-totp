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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.helger.totp.exception.QrGenerationException;
import com.helger.totp.qr.QrData;

/**
 * Test class for {@link ZxingPngQrCodeImageGenerator}.
 *
 * @author Philip Helger
 */
public final class ZxingPngQrCodeImageGeneratorTest
{
  @Test
  public void testGenerate () throws QrGenerationException
  {
    final byte [] aBytes = new ZxingPngQrCodeImageGenerator ().generate (_buildData ());
    assertNotNull (aBytes);
    assertTrue (aBytes.length > 0);
    // PNG magic number: 0x89 P N G
    assertEquals ((byte) 0x89, aBytes[0]);
    assertEquals ((byte) 0x50, aBytes[1]);
    assertEquals ((byte) 0x4E, aBytes[2]);
    assertEquals ((byte) 0x47, aBytes[3]);
  }

  @Test
  public void testMimeType ()
  {
    assertEquals ("image/png", new ZxingPngQrCodeImageGenerator ().getImageMimeType ());
  }

  @Test
  public void testImageSize () throws QrGenerationException, IOException
  {
    final ZxingPngQrCodeImageGenerator aGen = new ZxingPngQrCodeImageGenerator ().setImageSize (500);
    final byte [] aBytes = aGen.generate (_buildData ());

    final BufferedImage aImage = ImageIO.read (new ByteArrayInputStream (aBytes));
    assertEquals (500, aGen.getImageSize ());
    assertEquals (500, aImage.getWidth ());
    assertEquals (500, aImage.getHeight ());
  }

  @Test
  public void testExceptionIsWrapped ()
  {
    final RuntimeException aWriterFailure = new RuntimeException ();
    final Writer aFailingWriter = new Writer ()
    {
      @Override
      public BitMatrix encode (final String sContents, final BarcodeFormat eFormat, final int nWidth, final int nHeight)
                                                                                                                        throws WriterException
      {
        throw aWriterFailure;
      }

      @Override
      public BitMatrix encode (final String sContents,
                               final BarcodeFormat eFormat,
                               final int nWidth,
                               final int nHeight,
                               final java.util.Map <EncodeHintType, ?> aHints) throws WriterException
      {
        throw aWriterFailure;
      }
    };
    final ZxingPngQrCodeImageGenerator aGen = new ZxingPngQrCodeImageGenerator (aFailingWriter);
    try
    {
      aGen.generate (_buildData ());
      fail ();
    }
    catch (final QrGenerationException ex)
    {
      assertEquals ("Failed to generate QR code. See nested exception.", ex.getMessage ());
      assertSame (aWriterFailure, ex.getCause ());
    }
  }

  private static QrData _buildData ()
  {
    return new QrData.Builder ().label ("example@example.com")
                                .secret ("EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB")
                                .issuer ("AppName")
                                .digits (6)
                                .period (30)
                                .build ();
  }
}
