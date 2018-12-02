/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bremersee.nominatim.exception;

/**
 * @author Christian Bremer
 */
public abstract class ErrorCodeConstants {

  public static final String MALFORMED_URL = "NOMINATIM_CLIENT:MALFORMED_URL";

  public static final String GENERAL_REQUEST_ERROR = "NOMINATIM_CLIENT:GENERAL_REQUEST_ERROR";

  private ErrorCodeConstants() {
  }
}
