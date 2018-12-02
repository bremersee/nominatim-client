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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Christian Bremer
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReverseSearchRequest extends AbstractReverseSearchRequest {

  @Getter
  @Setter
  private BigDecimal lat;

  @Getter
  @Setter
  private BigDecimal lon;

  @Override
  protected MultiValueMap<String, String> buildReverseSearchParameters(boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("lat", lat != null ? String.valueOf(lat) : "0");
    map.set("lon", lon != null ? String.valueOf(lon) : "0");
    return map;
  }

  public static ReverseSearchRequestBuilder builder() {
    return new ReverseSearchRequestBuilder();
  }

  public static class ReverseSearchRequestBuilder
      extends AbstractReverseSearchRequestBuilder<ReverseSearchRequest> {

    private BigDecimal lat;
    private BigDecimal lon;

    public ReverseSearchRequestBuilder lat(BigDecimal lat) {
      this.lat = lat;
      return this;
    }

    public ReverseSearchRequestBuilder lon(BigDecimal lon) {
      this.lon = lon;
      return this;
    }

    @Override
    protected ReverseSearchRequest doReverseSearchRequestBuild() {
      ReverseSearchRequest target = new ReverseSearchRequest();
      target.setLon(lon);
      target.setLat(lat);
      return target;
    }

  }
}
