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

import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * The abstract reverse search request.
 *
 * @author Christian Bremer
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractReverseSearchRequest extends AbstractRequest {

  /**
   * The constant MIN_ZOOM.
   */
  public static int MIN_ZOOM = 0; // = country

  /**
   * The constant MAX_ZOOM.
   */
  public static int MAX_ZOOM = 18; // = house/building

  /**
   * Level of detail required where 0 is country and 18 is house/building.
   */
  private Integer zoom = 18;

  /**
   * Instantiates a new abstract reverse search request.
   */
  AbstractReverseSearchRequest() {
  }

  /**
   * Instantiates a new abstract reverse search request.
   *
   * @param acceptLanguage the accept language
   * @param addressDetails the address details
   * @param email the email
   * @param polygon the polygon
   * @param extraTags the extra tags
   * @param nameDetails the name details
   * @param zoom the zoom
   */
  AbstractReverseSearchRequest(
      final String acceptLanguage,
      final Boolean addressDetails,
      final String email,
      final Boolean polygon,
      final Boolean extraTags,
      final Boolean nameDetails,
      final Integer zoom) {
    super(acceptLanguage, addressDetails, email, polygon, extraTags, nameDetails);
    setZoom(zoom);
  }

  /**
   * Gets the level of detail required where 0 is country and 18 is house/building.
   *
   * @return the level of detail required where 0 is country and 18 is house/building
   */
  @NotNull
  public Integer getZoom() {
    if (zoom != null && zoom >= MIN_ZOOM && zoom <= MAX_ZOOM) {
      return zoom;
    }
    return 18;
  }

  /**
   * Sets the level of detail required where 0 is country and 18 is house/building.
   *
   * @param zoom the level of detail required where 0 is country and 18 is house/building
   */
  public void setZoom(final Integer zoom) {
    if (zoom != null && zoom >= MIN_ZOOM && zoom <= MAX_ZOOM) {
      this.zoom = zoom;
    }
  }

  @Override
  protected final MultiValueMap<String, String> buildRequestParameters(final boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("zoom", String.valueOf(getZoom()));
    map.putAll(buildReverseSearchParameters(urlEncode));
    return map;
  }

  /**
   * Build reverse search parameters.
   *
   * @param urlEncode the url encode
   * @return the reverse search parameters
   */
  protected abstract MultiValueMap<String, String> buildReverseSearchParameters(boolean urlEncode);

}
