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

package org.bremersee.nominatim.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.AccessLevel;
import lombok.Getter;
import org.bremersee.exception.ServiceException;
import org.bremersee.geojson.GeoJsonObjectMapperModule;
import org.bremersee.nominatim.NominatimProperties;
import org.bremersee.nominatim.exception.ErrorCodeConstants;
import org.bremersee.nominatim.model.AbstractSearchRequest;
import org.bremersee.nominatim.model.SearchRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

/**
 * Base implementation of a nominatim client.
 *
 * @param <S> the type parameter
 * @param <R> the type parameter
 * @author Christian Bremer
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbstractNominatimClient<S, R> implements NominatimClient<S, R> {

  /**
   * The Default object mapper.
   */
  @Getter(AccessLevel.PROTECTED)
  protected final ObjectMapper defaultObjectMapper;

  @Getter(AccessLevel.PROTECTED)
  private final NominatimProperties properties;

  /**
   * Instantiates a new abstract nominatim client.
   */
  public AbstractNominatimClient() {
    this(null);
  }

  /**
   * Instantiates a new abstract nominatim client.
   *
   * @param properties the properties
   */
  public AbstractNominatimClient(final NominatimProperties properties) {
    this.properties = properties != null ? properties : new NominatimProperties();
    defaultObjectMapper = new ObjectMapper();
    defaultObjectMapper.registerModule(new GeoJsonObjectMapperModule());
  }

  /**
   * Build the search url url.
   *
   * @param request the request
   * @return the url
   */
  protected URL buildSearchUrl(final SearchRequest request) {
    return buildUrl(getProperties().getSearchUri(), request);
  }

  private URL buildUrl(final String baseUri, final AbstractSearchRequest request) {
    boolean baseUriContainsQuery = baseUri.contains("?");
    final StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(baseUri);
    final MultiValueMap<String, String> params = request.buildParameters(true);
    for (String key : params.keySet()) {
      if (baseUriContainsQuery) {
        urlBuilder.append('&');
      } else {
        urlBuilder.append('?');
        baseUriContainsQuery = true;
      }
      urlBuilder.append(key).append('=').append(params.getFirst(key));
    }
    try {
      return new URL(urlBuilder.toString());

    } catch (final MalformedURLException e) {
      throw new ServiceException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeConstants.MALFORMED_URL, e);
    }
  }
}
