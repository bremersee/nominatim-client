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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Christian Bremer
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractReverseSearchRequest extends AbstractRequest {

  public static int MIN_ZOOM = 0; // = country

  public static int MAX_ZOOM = 18; // = house/building

  /**
   * Level of detail required where 0 is country and 18 is house/building.
   */
  @Getter
  private int zoom = 18;

  /**
   * Sets the level of detail required where 0 is country and 18 is house/building.
   *
   * @param zoom the level of detail required where 0 is country and 18 is house/building
   */
  public void setZoom(int zoom) {
    if (zoom >= MIN_ZOOM && zoom <= MAX_ZOOM) {
      this.zoom = zoom;
    }
  }

  @Override
  protected final MultiValueMap<String, String> buildRequestParameters(boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("zoom", String.valueOf(getZoom()));
    map.putAll(buildReverseSearchParameters(urlEncode));
    return map;
  }

  protected abstract MultiValueMap<String, String> buildReverseSearchParameters(boolean urlEncode);

  public abstract static class AbstractReverseSearchRequestBuilder<T extends
      AbstractReverseSearchRequest> extends AbstractRequestBuilder<T> {

    private int zoom = 18;

    public AbstractReverseSearchRequestBuilder zoom(int zoom) {
      this.zoom = zoom;
      return this;
    }

    protected abstract T doReverseSearchRequestBuild();

    @Override
    protected final T doRequestBuild() {
      T target = doReverseSearchRequestBuild();
      target.setZoom(zoom);
      return target;
    }
  }
}
