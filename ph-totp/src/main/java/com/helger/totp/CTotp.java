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
package com.helger.totp;

/**
 * Constants for the ph-totp library.
 *
 * @author Philip Helger
 */
public final class CTotp
{
  /** Default TOTP time period in seconds. */
  public static final int DEFAULT_TIME_PERIOD_SECS = 30;

  /** Default discrepancy used when verifying a code (number of buckets before/after the current one). */
  public static final int DEFAULT_TIME_PERIOD_DISCREPANCY = 1;

  /** Default number of digits in a generated code. */
  public static final int DEFAULT_CODE_DIGITS = 6;

  /** Default number of characters in a generated Base32-encoded secret. */
  public static final int DEFAULT_SECRET_LENGTH = 32;

  private CTotp ()
  {}
}
