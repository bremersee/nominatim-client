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
public class StructuredSearchRequest extends AbstractSearchRequest {

  private String street;

  private String city;

  private String county;

  private String state;

  private String country;

  private String postalcode;

  @Override
  protected MultiValueMap<String, String> buildSearchParameters(boolean urlEncode) {
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
    if (StringUtils.hasText(postalcode)) {
      map.set("postalcode", encodeQueryParameter(postalcode, urlEncode));
    }
    return map;
  }

  public static StructuredSearchRequestBuilder builder() {
    return new StructuredSearchRequestBuilder();
  }

  public static class StructuredSearchRequestBuilder extends
      AbstractSearchRequestBuilder<StructuredSearchRequest> {

    private String street;

    private String city;

    private String county;

    private String state;

    private String country;

    private String postalcode;

    public StructuredSearchRequestBuilder street(String street) {
      this.street = street;
      return this;
    }

    public StructuredSearchRequestBuilder city(String city) {
      this.city = city;
      return this;
    }

    public StructuredSearchRequestBuilder county(String county) {
      this.county = county;
      return this;
    }

    public StructuredSearchRequestBuilder state(String state) {
      this.state = state;
      return this;
    }

    public StructuredSearchRequestBuilder country(String country) {
      this.country = country;
      return this;
    }

    public StructuredSearchRequestBuilder postalcode(String postalcode) {
      this.postalcode = postalcode;
      return this;
    }

    @Override
    protected StructuredSearchRequest doSearchRequestBuild() {
      final StructuredSearchRequest searchRequest = new StructuredSearchRequest();
      searchRequest.setStreet(street);
      searchRequest.setCity(city);
      searchRequest.setCounty(county);
      searchRequest.setState(state);
      searchRequest.setCountry(country);
      searchRequest.setPostalcode(postalcode);
      return searchRequest;
    }
  }
}
