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

import java.time.Instant;

import org.jspecify.annotations.NullMarked;

/**
 * Time provider backed by {@link Instant#now()}.
 *
 * @author Philip Helger
 */
@NullMarked
public class SystemTimeProvider implements ITimeProvider
{
  @Override
  public long getTime ()
  {
    return Instant.now ().getEpochSecond ();
  }
}
