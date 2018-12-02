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

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * The type OSM ID aware reverse search request.
 *
 * @author Christian Bremer
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class OsmIdAwareReverseSearchRequest extends AbstractReverseSearchRequest {

  private OsmType osmType;

  private String osmId;

  /**
   * Instantiates a new OSM ID aware reverse search request.
   */
  public OsmIdAwareReverseSearchRequest() {
  }

  /**
   * Instantiates a new OSM ID aware reverse search request.
   *
   * @param acceptLanguage the accept language
   * @param addressDetails the address details
   * @param email the email
   * @param polygon the polygon
   * @param extraTags the extra tags
   * @param nameDetails the name details
   * @param zoom the zoom
   * @param osmType the osm type
   * @param osmId the osm id
   */
  @Builder
  public OsmIdAwareReverseSearchRequest(
      String acceptLanguage,
      Boolean addressDetails,
      String email,
      Boolean polygon,
      Boolean extraTags,
      Boolean nameDetails,
      Integer zoom,
      OsmType osmType,
      String osmId) {
    super(acceptLanguage, addressDetails, email, polygon, extraTags, nameDetails, zoom);
    this.osmType = osmType;
    this.osmId = osmId;
  }

  @Override
  protected MultiValueMap<String, String> buildReverseSearchParameters(boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    if (osmType != null) {
      map.set("osm_type", osmType.getValue());
    }
    if (StringUtils.hasText(osmId)) {
      map.set("osm_id", osmId);
    }
    return map;
  }

  /**
   * The enum OSM type.
   */
  public enum OsmType {

    /**
     * Node osm type.
     */
    NODE("N"),

    /**
     * Way osm type.
     */
    WAY("W"),

    /**
     * Relation osm type.
     */
    RELATION("R");

    @Getter
    private String value;

    OsmType(String value) {
      this.value = value;
    }

    /**
     * Find OSM type from value.
     *
     * @param value the value
     * @return the osm type (can be {@code null})
     */
    public static OsmType fromValue(final String value) {
      if (StringUtils.hasText(value)) {
        for (OsmType osmType : OsmType.values()) {
          if (value.equalsIgnoreCase(osmType.getValue())) {
            return osmType;
          }
        }
      }
      return null;
    }
  }
}
