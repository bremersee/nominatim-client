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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * The abstract search request.
 *
 * @author Christian Bremer
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public abstract class AbstractSearchRequest extends AbstractRequest {

  /**
   * Limit search results to a specific country (or a list of countries). {@literal <countrycode>}
   * should be the ISO 3166-1alpha2 code, e.g. gb for the United Kingdom, de for Germany, etc.
   *
   * <p>countrycodes={@literal <countrycode>[,<countrycode>][,<countrycode>]} ...
   */
  @Setter
  private List<Locale> countryCodes;

  /**
   * The preferred area to find search results. Any two corner points of the box are accepted in any
   * order as long as they span a real box.
   */
  @Getter
  @Setter
  private double[] viewBox;

  /**
   * Restrict the results to only items contained with the viewBox (see above). Restricting the
   * results to the bounding box also enables searching by amenity only. For example a search query
   * of just "[pub]" would normally be rejected but with bounded=1 will result in a list of items
   * matching within the bounding box.
   *
   * <p>bounded=[0|1]
   */
  @Getter
  @Setter
  private Boolean bounded = Boolean.FALSE;

  /**
   * If you do not want certain openstreetmap objects to appear in the search result, give a comma
   * separated list of the place_id's you want to skip. This can be used to broaden search results.
   * For example, if a previous query only returned a few results, then including those here would
   * cause the search to return other, less accurate, matches (if possible).
   *
   * <p>exclude_place_ids={@literal <place_id,[place_id],[place_id]>}
   */
  @Setter
  private List<String> excludePlaceIds;

  /**
   * Limit the number of returned results. Default is 10.
   *
   * <p>limit={@literal <integer>}
   */
  @Getter
  @Setter
  private Integer limit = 10;

  /**
   * Sometimes you have several objects in OSM identifying the same place or object in reality. The
   * simplest case is a street being split in many different OSM ways due to different
   * characteristics. Nominatim will attempt to detect such duplicates and only return one match;
   * this is controlled by the dedupe parameter which defaults to 1. Since the limit is, for reasons
   * of efficiency, enforced before and not after de-duplicating, it is possible that de-duplicating
   * leaves you with less results than requested.
   *
   * <p>dedupe=[0|1]
   */
  @Getter
  @Setter
  private Boolean dedupe = Boolean.TRUE;

  /**
   * Output assorted developer debug information. Data on internals of nominatim "Search Loop"
   * logic, and SQL queries. The output is (rough) HTML format. This overrides the specified machine
   * readable format.
   *
   * <p>debug=[0|1]
   */
  @Getter
  @Setter
  private Boolean debug = Boolean.FALSE;

  /**
   * Instantiates a new abstract search request.
   */
  AbstractSearchRequest() {
  }

  /**
   * Instantiates a new abstract search request.
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
   */
  AbstractSearchRequest(
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
      final Boolean debug) {
    super(acceptLanguage, addressDetails, email, polygon, extraTags, nameDetails);
    this.countryCodes = countryCodes;
    this.viewBox = viewBox;
    this.bounded = Boolean.TRUE.equals(bounded);
    this.excludePlaceIds = excludePlaceIds;
    this.limit = limit == null || limit < 1 ? 10 : limit;
    this.dedupe = !Boolean.FALSE.equals(dedupe);
    this.debug = Boolean.TRUE.equals(debug);
  }

  /**
   * Limit search results to a specific country (or a list of countries). {@literal <countrycode>}
   * should be the ISO 3166-1alpha2 code, e.g. gb for the United Kingdom, de for Germany, etc.
   *
   * @return the country codes
   */
  public List<Locale> getCountryCodes() {
    if (countryCodes == null) {
      countryCodes = new ArrayList<>();
    }
    return countryCodes;
  }

  /**
   * If you do not want certain openstreetmap objects to appear in the search result, give a comma
   * separated list of the place_id's you want to skip. This can be used to broaden search results.
   * For example, if a previous query only returned a few results, then including those here would
   * cause the search to return other, less accurate, matches (if possible).
   *
   * @return the excluded place IDs
   */
  public List<String> getExcludePlaceIds() {
    if (excludePlaceIds == null) {
      excludePlaceIds = new ArrayList<>();
    }
    return excludePlaceIds;
  }

  @Override
  protected final MultiValueMap<String, String> buildRequestParameters(final boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    if (countryCodes != null && !countryCodes.isEmpty()) {
      map.set("countrycodes", StringUtils.collectionToCommaDelimitedString(
          countryCodes
              .stream()
              .map(Locale::getCountry)
              .filter(Objects::nonNull)
              .collect(Collectors.toSet())));
    }
    if (viewBox != null && viewBox.length == 4) {
      map.set("viewbox", viewBox[0] + "," + viewBox[1] + "," + viewBox[2] + "," + viewBox[3]);
    }
    map.set("bounded", Boolean.TRUE.equals(bounded) ? "1" : "0");
    if (excludePlaceIds != null && !excludePlaceIds.isEmpty()) {
      map.set("exclude_place_ids",
          encodeQueryParameter(
              StringUtils.collectionToCommaDelimitedString(excludePlaceIds), urlEncode));
    }
    map.set("limit", limit == null || limit < 1 ? "10" : String.valueOf(limit));
    map.set("dedupe", Boolean.FALSE.equals(dedupe) ? "0" : "1");
    map.set("debug", Boolean.TRUE.equals(debug) ? "1" : "0");
    map.putAll(buildSearchParameters(urlEncode));
    return map;
  }

  /**
   * Build search parameters.
   *
   * @param urlEncode the url encode
   * @return the search parameters
   */
  protected abstract MultiValueMap<String, String> buildSearchParameters(boolean urlEncode);

}
