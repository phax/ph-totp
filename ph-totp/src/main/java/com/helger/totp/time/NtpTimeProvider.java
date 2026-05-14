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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import com.helger.totp.exception.TimeProviderException;

/**
 * Time provider that fetches the current time from an NTP server.
 * <p>
 * Requires the optional <code>commons-net</code> dependency on the classpath at runtime.
 *
 * @author Philip Helger
 */
@NullMarked
public class NtpTimeProvider implements ITimeProvider
{
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds (3);
  private static final String COMMONS_NET_PROBE_CLASS = "org.apache.commons.net.ntp.NTPUDPClient";

  private final NTPUDPClient m_aClient;
  private final InetAddress m_aNtpHost;

  private static void _checkHasDependency (@NonNull final String sDependentClass)
  {
    try
    {
      Class.forName (sDependentClass);
    }
    catch (final ClassNotFoundException ex)
    {
      throw new IllegalStateException ("The Apache Commons Net library must be on the classpath to use the NtpTimeProvider.");
    }
  }

  public NtpTimeProvider (@NonNull final String sNtpHostname) throws UnknownHostException
  {
    this (sNtpHostname, DEFAULT_TIMEOUT);
  }

  public NtpTimeProvider (@NonNull final String sNtpHostname, @NonNull final Duration aTimeout)
                                                                                                throws UnknownHostException
  {
    this (sNtpHostname, aTimeout, COMMONS_NET_PROBE_CLASS);
  }

  /**
   * Package-private constructor used by tests to inject a non-existing probe class name.
   */
  NtpTimeProvider (@NonNull final String sNtpHostname, @NonNull final Duration aTimeout, final String sDependentClass)
                                                                                                                       throws UnknownHostException
  {
    _checkHasDependency (sDependentClass);

    m_aClient = new NTPUDPClient ();
    m_aClient.setDefaultTimeout (aTimeout);
    m_aNtpHost = InetAddress.getByName (sNtpHostname);
  }

  @Override
  public long getTime ()
  {
    final TimeInfo aTimeInfo;
    try
    {
      aTimeInfo = m_aClient.getTime (m_aNtpHost);
      aTimeInfo.computeDetails ();
    }
    catch (final Exception ex)
    {
      throw new TimeProviderException ("Failed to provide time from NTP server. See nested exception.", ex);
    }

    if (aTimeInfo.getOffset () == null)
      throw new TimeProviderException ("Failed to calculate NTP offset");

    return (System.currentTimeMillis () + aTimeInfo.getOffset ().longValue ()) / 1000L;
  }
}
