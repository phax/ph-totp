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
import org.jspecify.annotations.Nullable;

/**
 * Verifies user-submitted TOTP codes against a shared secret.
 *
 * @author Philip Helger
 */
@NullMarked
public interface ICodeVerifier
{
  /**
   * Determine the time slot ("bucket") the provided code is valid for.
   * <p>
   * This is the basis for replay protection. A code is accepted anywhere inside the discrepancy
   * window, so remembering the <em>current</em> time slot at the moment of verification is not
   * sufficient: a code matched in slot <code>N</code> can be presented again during slot
   * <code>N+1</code> and is then still inside the window. An application that remembers the slot
   * returned here, and rejects every code whose slot is less than or equal to the remembered one,
   * can use each one-time password exactly once.
   *
   * @param sSecret
   *        The Base32-encoded shared secret/key to check the code against.
   * @param sCode
   *        The n-digit code given by the end user to check.
   * @return The time slot the code matched, or <code>null</code> if the code is not valid. If the
   *         code matches more than one slot, the latest one is returned.
   * @since 2.1.0
   */
  @Nullable
  Long getMatchingTimeSlot (@NonNull String sSecret, @NonNull String sCode);

  /**
   * @param sSecret
   *        The Base32-encoded shared secret/key to check the code against.
   * @param sCode
   *        The n-digit code given by the end user to check.
   * @return <code>true</code> if the code is valid, <code>false</code> otherwise.
   * @see #getMatchingTimeSlot(String, String) for the variant needed to implement replay protection
   */
  default boolean isValidCode (@NonNull final String sSecret, @NonNull final String sCode)
  {
    return getMatchingTimeSlot (sSecret, sCode) != null;
  }
}
