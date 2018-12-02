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

package org.bremersee.nominatim.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * The reverse search request.
 *
 * @author Christian Bremer
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ReverseSearchRequest extends AbstractReverseSearchRequest {

  private BigDecimal lat;

  private BigDecimal lon;

  /**
   * Instantiates a new reverse search request.
   */
  public ReverseSearchRequest() {
  }

  /**
   * Instantiates a new reverse search request.
   *
   * @param acceptLanguage the accept language
   * @param addressDetails the address details
   * @param email the email
   * @param polygon the polygon
   * @param extraTags the extra tags
   * @param nameDetails the name details
   * @param zoom the zoom
   * @param lat the lat
   * @param lon the lon
   */
  @Builder
  public ReverseSearchRequest(
      final String acceptLanguage,
      final Boolean addressDetails,
      final String email,
      final Boolean polygon,
      final Boolean extraTags,
      final Boolean nameDetails,
      final Integer zoom,
      final BigDecimal lat,
      final BigDecimal lon) {
    super(acceptLanguage, addressDetails, email, polygon, extraTags, nameDetails, zoom);
    this.lat = lat;
    this.lon = lon;
  }

  @Override
  protected MultiValueMap<String, String> buildReverseSearchParameters(final boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("lat", lat != null ? String.valueOf(lat) : "0");
    map.set("lon", lon != null ? String.valueOf(lon) : "0");
    return map;
  }

}
