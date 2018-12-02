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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * The main search request.
 *
 * @author Christian Bremer
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class SearchRequest extends AbstractSearchRequest {

  private String query;

  /**
   * Instantiates a new search request.
   */
  public SearchRequest() {
  }

  /**
   * Instantiates a new search request.
   *
   * @param acceptLanguage the accept language
   * @param addressDetails the address details
   * @param email the email
   * @param polygon the polygon
   * @param extraTags the extra tags
   * @param nameDetails the name details
   * @param countryCodes the country codes
   * @param viewBox the view box
   * @param bounded the bounded
   * @param excludePlaceIds the exclude place ids
   * @param limit the limit
   * @param dedupe the dedupe
   * @param debug the debug
   * @param query the query
   */
  @Builder
  public SearchRequest(
      final String acceptLanguage,
      final Boolean addressDetails,
      final String email,
      final Boolean polygon,
      final Boolean extraTags,
      final Boolean nameDetails,
      final List<String> countryCodes,
      final Double[] viewBox,
      final Boolean bounded,
      final List<String> excludePlaceIds,
      final Integer limit,
      final Boolean dedupe,
      final Boolean debug,
      final String query) {
    super(acceptLanguage, addressDetails, email, polygon, extraTags, nameDetails, countryCodes,
        viewBox, bounded, excludePlaceIds, limit, dedupe, debug);
    this.query = query;
  }

  @Override
  protected MultiValueMap<String, String> buildSearchParameters(final boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    if (StringUtils.hasText(query)) {
      map.set("q", encodeQueryParameter(query, urlEncode));
    } else {
      map.set("q", "");
    }
    return map;
  }

}
