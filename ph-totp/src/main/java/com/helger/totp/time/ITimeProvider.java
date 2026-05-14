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

import org.jspecify.annotations.NullMarked;

import com.helger.totp.exception.TimeProviderException;

/**
 * Source of the current time used by the TOTP verifier.
 *
 * @author Philip Helger
 */
@NullMarked
public interface ITimeProvider
{
  /**
   * @return The current number of seconds since 1970-01-01T00:00:00Z.
   * @throws TimeProviderException
   *         if the time cannot be fetched.
   */
  long getTime ();
}
