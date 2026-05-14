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
package com.helger.totp.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;

import java.net.UnknownHostException;
import java.time.Duration;

import org.junit.Test;

import com.helger.totp.exception.TimeProviderException;

/**
 * Test class for {@link NtpTimeProvider}.
 *
 * @author Philip Helger
 */
public final class NtpTimeProviderTest
{
  @Test
  public void testProvidesTime ()
  {
    final ITimeProvider aTime;
    try
    {
      aTime = new NtpTimeProvider ("pool.ntp.org");
    }
    catch (final UnknownHostException ex)
    {
      // No network in CI? Skip rather than fail.
      assumeNoException (ex);
      return;
    }
    try
    {
      final long nCurrentTime = aTime.getTime ();
      // epoch should be 10 digits for the foreseeable future
      assertEquals (10, String.valueOf (nCurrentTime).length ());
    }
    catch (final TimeProviderException ex)
    {
      // NTP unreachable? Skip.
      assumeNoException (ex);
    }
  }

  @Test
  public void testUnknownHostThrowsException ()
  {
    try
    {
      new NtpTimeProvider ("sdfsf/safsf");
      fail ();
    }
    catch (final UnknownHostException ex)
    {}
  }

  @Test
  public void testNonNtpHostThrowsException () throws UnknownHostException
  {
    final ITimeProvider aTime = new NtpTimeProvider ("www.example.com");
    try
    {
      aTime.getTime ();
      fail ();
    }
    catch (final TimeProviderException ex)
    {
      assertNotNull (ex.getCause ());
    }
  }

  @Test
  public void testRequiresDependency ()
  {
    try
    {
      // Package-private constructor: probe a non-existing class
      new NtpTimeProvider ("www.example.com", Duration.ofSeconds (3), "fake.class.Here");
      fail ();
    }
    catch (final UnknownHostException ex)
    {
      fail ("Wrong exception type: " + ex);
    }
    catch (final RuntimeException ex)
    {
      assertEquals ("The Apache Commons Net library must be on the classpath to use the NtpTimeProvider.",
                    ex.getMessage ());
    }
  }
}
