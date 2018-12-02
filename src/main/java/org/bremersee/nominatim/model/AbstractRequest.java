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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
@ToString
@EqualsAndHashCode
public abstract class AbstractRequest {

  /**
   * Preferred language order for showing search results, overrides the value specified in the
   * "Accept-Language" HTTP header. Either uses standard rfc2616 accept-language string or a simple
   * comma separated list of language codes.
   *
   * <p>Query parameter name is {@code accept-language}.
   */
  private String acceptLanguage = "de";

  /**
   * Include a breakdown of the address into elements.
   *
   * <p>addressDetails=[0|1]
   */
  private boolean addressDetails = true;

  /**
   * If you are making large numbers of request please include a valid email address or
   * alternatively include your email address as part of the User-Agent string. This information
   * will be kept confidential and only used to contact you in the event of a problem, see Usage
   * Policy for more details.
   *
   * <p>email={@literal <valid email address>}
   */
  private String email;

  /**
   * Output geometry of results in geojson format.
   *
   * <p>polygon_geojson=1
   */
  private boolean polygon = true;

  /**
   * Include additional information in the result if available, e.g. wikipedia link, opening hours.
   *
   * <p>extraTags=1
   */
  private boolean extraTags = true;

  /**
   * Include a list of alternative names in the results. These may include language variants,
   * references, operator and brand.
   *
   * <p>nameDetails=1
   */
  private boolean nameDetails = true;

  /**
   * Build the common request parameter map.
   *
   * @param urlEncode if {@code true}, the parameter values will be encoded, otherwise not.
   * @return the common request parameter map
   */
  public final MultiValueMap<String, String> buildParameters(boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("format", "jsonv2");
    if (StringUtils.hasText(acceptLanguage)) {
      map.set("accept-language", acceptLanguage);
    }
    map.set("addressdetails", addressDetails ? "1" : "0");
    if (StringUtils.hasText(email)) {
      map.set("email", encodeQueryParameter(email, urlEncode));
    }
    if (polygon) {
      map.set("polygon_geojson", "1");
    }
    map.set("extratags", extraTags ? "1" : "0");
    map.set("namedetails", nameDetails ? "1" : "0");
    map.putAll(buildRequestParameters(urlEncode));
    return map;
  }

  protected abstract MultiValueMap<String, String> buildRequestParameters(boolean urlEncode);

  String encodeQueryParameter(String value, boolean encode) {
    if (!StringUtils.hasText(value)) {
      return "";
    }
    if (encode) {
      try {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.name());

      } catch (UnsupportedEncodingException e) {
        // ignore
      }
    }
    return value;
  }

  @SuppressWarnings("unused")
  public static abstract class AbstractRequestBuilder<T extends AbstractRequest> {

    private String acceptLanguage;
    private boolean addressDetails = true;
    private String email;
    private boolean polygon = true;
    private boolean extraTags = true;
    private boolean nameDetails = true;

    public AbstractRequestBuilder<T> acceptLanguage(final String acceptLanguage) {
      this.acceptLanguage = acceptLanguage;
      return this;
    }

    public AbstractRequestBuilder<T> addressDetails(final boolean addressDetails) {
      this.addressDetails = addressDetails;
      return this;
    }

    public AbstractRequestBuilder<T> email(final String email) {
      this.email = email;
      return this;
    }

    public AbstractRequestBuilder<T> polygon(final boolean polygon) {
      this.polygon = polygon;
      return this;
    }

    public AbstractRequestBuilder<T> extraTags(final boolean extraTags) {
      this.extraTags = extraTags;
      return this;
    }

    public AbstractRequestBuilder<T> nameDetails(final boolean nameDetails) {
      this.nameDetails = nameDetails;
      return this;
    }

    protected abstract T doRequestBuild();

    public final T build() {
      T target = doRequestBuild();
      target.setAcceptLanguage(acceptLanguage);
      target.setAddressDetails(addressDetails);
      target.setEmail(email);
      target.setExtraTags(extraTags);
      target.setNameDetails(nameDetails);
      target.setPolygon(polygon);
      return target;
    }

  }
}
