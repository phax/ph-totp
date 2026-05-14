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
package com.helger.totp.secret;

import org.jspecify.annotations.NullMarked;

/**
 * Generates a shared TOTP secret.
 *
 * @author Philip Helger
 */
@NullMarked
public interface ISecretGenerator
{
  /**
   * @return A random Base32-encoded string suitable for use as the shared secret between the
   *         server and the authenticator client.
   */
  String generate ();
}
