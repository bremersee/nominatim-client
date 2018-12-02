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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * @author Christian Bremer
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractSearchRequest extends AbstractRequest {

  /**
   * Limit search results to a specific country (or a list of countries). {@literal <countrycode>}
   * should be the ISO 3166-1alpha2 code, e.g. gb for the United Kingdom, de for Germany, etc.
   *
   * <p>countrycodes={@literal <countrycode>[,<countrycode>][,<countrycode>]} ...
   */
  private List<String> countryCodes = new ArrayList<>();

  /**
   * The preferred area to find search results. Any two corner points of the box are accepted in any
   * order as long as they span a real box.
   */
  private Double[] viewBox;

  /**
   * Restrict the results to only items contained with the viewBox (see above). Restricting the
   * results to the bounding box also enables searching by amenity only. For example a search query
   * of just "[pub]" would normally be rejected but with bounded=1 will result in a list of items
   * matching within the bounding box.
   *
   * <p>bounded=[0|1]
   */
  private boolean bounded = false;

  /**
   * If you do not want certain openstreetmap objects to appear in the search result, give a comma
   * separated list of the place_id's you want to skip. This can be used to broaden search results.
   * For example, if a previous query only returned a few results, then including those here would
   * cause the search to return other, less accurate, matches (if possible).
   *
   * <p>exclude_place_ids={@literal <place_id,[place_id],[place_id]>}
   */
  private List<String> excludePlaceIds = new ArrayList<>();

  /**
   * Limit the number of returned results. Default is 10.
   *
   * <p>limit={@literal <integer>}
   */
  private int limit = 10;

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
  private boolean dedupe = true;

  /**
   * Output assorted developer debug information. Data on internals of nominatim "Search Loop"
   * logic, and SQL queries. The output is (rough) HTML format. This overrides the specified machine
   * readable format.
   *
   * <p>debug=[0|1]
   */
  private boolean debug = false;

  public final MultiValueMap<String, String> buildRequestParameters(boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    if (countryCodes != null && !countryCodes.isEmpty()) {
      map.set("countrycodes", StringUtils.collectionToCommaDelimitedString(countryCodes));
    }
    if (viewBox != null && viewBox.length == 4) {
      map.set("viewbox", StringUtils.arrayToCommaDelimitedString(viewBox));
    }
    map.set("bounded", bounded ? "1" : "0");
    if (excludePlaceIds != null && !excludePlaceIds.isEmpty()) {
      map.set("exclude_place_ids",
          encodeQueryParameter(
              StringUtils.collectionToCommaDelimitedString(excludePlaceIds), urlEncode));
    }
    map.set("limit", limit < 1 ? "10" : String.valueOf(limit));
    map.set("dedupe", dedupe ? "1" : "0");
    map.set("debug", debug ? "1" : "0");
    map.putAll(buildSearchParameters(urlEncode));
    return map;
  }

  protected abstract MultiValueMap<String, String> buildSearchParameters(boolean urlEncode);

  @SuppressWarnings("unused")
  public static abstract class AbstractSearchRequestBuilder<T extends AbstractSearchRequest>
      extends AbstractRequestBuilder<T> {

    private List<String> countryCodes = new ArrayList<>();
    private Double[] viewBox;
    private boolean bounded;
    private List<String> excludePlaceIds = new ArrayList<>();
    private int limit = 10;
    private boolean dedupe = true;
    private boolean debug = false;

    public AbstractSearchRequestBuilder<T> countryCodes(final List<String> countryCodes) {
      if (countryCodes != null) {
        this.countryCodes = countryCodes;
      }
      return this;
    }

    public AbstractSearchRequestBuilder<T> viewBox(final Double[] viewBox) {
      this.viewBox = viewBox;
      return this;
    }

    public AbstractSearchRequestBuilder<T> bounded(final boolean bounded) {
      this.bounded = bounded;
      return this;
    }

    public AbstractSearchRequestBuilder<T> excludePlaceIds(final List<String> excludePlaceIds) {
      if (excludePlaceIds != null) {
        this.excludePlaceIds = excludePlaceIds;
      }
      return this;
    }

    public AbstractSearchRequestBuilder<T> limit(final int limit) {
      this.limit = limit;
      return this;
    }

    public AbstractSearchRequestBuilder<T> dedupe(final boolean dedupe) {
      this.dedupe = dedupe;
      return this;
    }

    public AbstractSearchRequestBuilder<T> debug(final boolean debug) {
      this.debug = debug;
      return this;
    }

    protected abstract T doSearchRequestBuild();

    public final T doRequestBuild() {
      T target = doSearchRequestBuild();
      target.setBounded(bounded);
      target.setCountryCodes(countryCodes);
      target.setViewBox(viewBox);
      target.setDebug(debug);
      target.setDedupe(dedupe);
      target.setExcludePlaceIds(excludePlaceIds);
      target.setLimit(limit);
      return target;
    }

  }

}
