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

import org.jspecify.annotations.NullMarked;

import com.helger.totp.exception.CodeGenerationException;

/**
 * Generates an HOTP-style numeric code from a shared secret and a counter value.
 *
 * @author Philip Helger
 */
@NullMarked
public interface ICodeGenerator
{
  /**
   * @param sSecret
   *        The Base32-encoded shared secret to generate the code with.
   * @param nCounter
   *        The current bucket number (seconds since epoch / time period).
   * @return The zero-padded n-digit code for the given secret/counter pair.
   * @throws CodeGenerationException
   *         if the code generation fails for any reason.
   */
  String generate (String sSecret, long nCounter) throws CodeGenerationException;
}
