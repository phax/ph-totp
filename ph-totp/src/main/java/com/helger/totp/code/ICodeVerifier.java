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
package com.helger.totp.code;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

/**
 * Verifies user-submitted TOTP codes against a shared secret.
 *
 * @author Philip Helger
 */
@NullMarked
public interface ICodeVerifier
{
  /**
   * @param sSecret
   *        The Base32-encoded shared secret/key to check the code against.
   * @param sCode
   *        The n-digit code given by the end user to check.
   * @return <code>true</code> if the code is valid, <code>false</code> otherwise.
   */
  boolean isValidCode (@NonNull String sSecret, @NonNull String sCode);
}
