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

import java.util.List;
import java.util.Locale;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * The structured search request.
 *
 * @author Christian Bremer
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class StructuredSearchRequest extends AbstractSearchRequest {

  private String street;

  private String city;

  private String county;

  private String state;

  private String country;

  private String postalCode;

  /**
   * Instantiates a new structured search request.
   */
  public StructuredSearchRequest() {
  }

  /**
   * Instantiates a new structured search request.
   *
   * @param acceptLanguage  the accept language
   * @param addressDetails  the address details
   * @param email           the email
   * @param polygon         the polygon
   * @param extraTags       the extra tags
   * @param nameDetails     the name details
   * @param countryCodes    the country codes
   * @param viewBox         the view box
   * @param bounded         the bounded
   * @param excludePlaceIds the exclude place ids
   * @param limit           the limit
   * @param dedupe          the dedupe
   * @param debug           the debug
   * @param street          the street
   * @param city            the city
   * @param county          the county
   * @param state           the state
   * @param country         the country
   * @param postalCode      the postal code
   */
  @Builder
  public StructuredSearchRequest(
      final String acceptLanguage,
      final Boolean addressDetails,
      final String email,
      final Boolean polygon,
      final Boolean extraTags,
      final Boolean nameDetails,
      final List<Locale> countryCodes,
      final double[] viewBox,
      final Boolean bounded,
      final List<String> excludePlaceIds,
      final Integer limit,
      final Boolean dedupe,
      final Boolean debug,
      final String street,
      final String city,
      final String county,
      final String state,
      final String country,
      final String postalCode) {
    super(acceptLanguage, addressDetails, email, polygon, extraTags, nameDetails, countryCodes,
        viewBox, bounded, excludePlaceIds, limit, dedupe, debug);
    this.street = street;
    this.city = city;
    this.county = county;
    this.state = state;
    this.country = country;
    this.postalCode = postalCode;
  }

  @Override
  protected MultiValueMap<String, String> buildSearchParameters(final boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    if (StringUtils.hasText(street)) {
      map.set("street", encodeQueryParameter(street, urlEncode));
    }
    if (StringUtils.hasText(city)) {
      map.set("city", encodeQueryParameter(city, urlEncode));
    }
    if (StringUtils.hasText(county)) {
      map.set("county", encodeQueryParameter(county, urlEncode));
    }
    if (StringUtils.hasText(state)) {
      map.set("state", encodeQueryParameter(state, urlEncode));
    }
    if (StringUtils.hasText(country)) {
      map.set("country", encodeQueryParameter(country, urlEncode));
    }
    if (StringUtils.hasText(postalCode)) {
      map.set("postalcode", encodeQueryParameter(postalCode, urlEncode));
    }
    return map;
  }

}
