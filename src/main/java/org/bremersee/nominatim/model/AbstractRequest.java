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
 * The abstract request of all other requests.
 *
 * @author Christian Bremer
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode
public abstract class AbstractRequest {

  /**
   * Preferred language order for showing search results, overrides the value specified in the
   * "Accept-Language" HTTP header. Either uses standard rfc2616 accept-language string or a simple
   * comma separated list of language codes.
   *
   * <p>Query parameter name is {@code accept-language}.
   */
  private String acceptLanguage = "en";

  /**
   * Include a breakdown of the address into elements.
   *
   * <p>addressDetails=[0|1]
   */
  private Boolean addressDetails = Boolean.TRUE;

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
  private Boolean polygon = Boolean.TRUE;

  /**
   * Include additional information in the result if available, e.g. wikipedia link, opening hours.
   *
   * <p>extraTags=1
   */
  private Boolean extraTags = Boolean.TRUE;

  /**
   * Include a list of alternative names in the results. These may include language variants,
   * references, operator and brand.
   *
   * <p>nameDetails=1
   */
  private Boolean nameDetails = Boolean.TRUE;

  /**
   * Instantiates a new nbstract request.
   */
  AbstractRequest() {
  }

  /**
   * Instantiates a new abstract request.
   *
   * @param acceptLanguage the accept language
   * @param addressDetails the address details
   * @param email the email
   * @param polygon the polygon
   * @param extraTags the extra tags
   * @param nameDetails the name details
   */
  AbstractRequest(
      final String acceptLanguage,
      final Boolean addressDetails,
      final String email,
      final Boolean polygon,
      final Boolean extraTags,
      final Boolean nameDetails) {
    // ensure default values
    this.acceptLanguage = StringUtils.hasText(acceptLanguage) ? acceptLanguage : "en";
    this.addressDetails = !Boolean.FALSE.equals(addressDetails);
    this.email = email;
    this.polygon = !Boolean.FALSE.equals(polygon);
    this.extraTags = !Boolean.FALSE.equals(extraTags);
    this.nameDetails = !Boolean.FALSE.equals(nameDetails);
  }

  /**
   * Build the request parameter map.
   *
   * @param urlEncode if {@code true}, the parameter values will be encoded, otherwise not.
   * @return the request parameter map
   */
  public final MultiValueMap<String, String> buildParameters(final boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("format", "jsonv2");
    if (StringUtils.hasText(acceptLanguage)) {
      map.set("accept-language", acceptLanguage);
    } else {
      map.set("accept-language", "en");
    }
    map.set("addressdetails", Boolean.FALSE.equals(addressDetails) ? "0" : "1");
    if (StringUtils.hasText(email)) {
      map.set("email", encodeQueryParameter(email, urlEncode));
    }
    if (polygon == null || Boolean.TRUE.equals(polygon)) {
      map.set("polygon_geojson", "1");
    }
    map.set("extratags", Boolean.FALSE.equals(extraTags) ? "0" : "1");
    map.set("namedetails", Boolean.FALSE.equals(nameDetails) ? "0" : "1");
    map.putAll(buildRequestParameters(urlEncode));
    return map;
  }

  /**
   * Build request parameters of sub classes.
   *
   * @param urlEncode the url encode
   * @return the multi value map
   */
  protected abstract MultiValueMap<String, String> buildRequestParameters(boolean urlEncode);

  /**
   * Encode query parameter.
   *
   * @param value the parameter value
   * @param encode if {@code true} the parameter will be url encoded, otherwise not
   * @return the (encoded) parameter value
   */
  String encodeQueryParameter(final String value, final boolean encode) {
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

}
