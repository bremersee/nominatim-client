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
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bremersee.exception.ServiceException;
import org.bremersee.geojson.GeoJsonObjectMapperModule;
import org.bremersee.nominatim.NominatimProperties;
import org.bremersee.nominatim.exception.ErrorCodeConstants;
import org.bremersee.nominatim.model.ReverseSearchRequest;
import org.bremersee.nominatim.model.ReverseSearchResult;
import org.bremersee.nominatim.model.SearchRequest;
import org.bremersee.nominatim.model.SearchResult;
import org.springframework.http.HttpStatus;

/**
 * @author Christian Bremer
 */
public class DefaultNominatimClient
    extends AbstractNominatimClient<List<SearchResult>, List<ReverseSearchResult>>
    implements TraditionalNominatimClient<List<SearchResult>, List<ReverseSearchResult>> {

  private ObjectMapper objectMapper;

  public DefaultNominatimClient() {
    this(null, null);
  }

  public DefaultNominatimClient(ObjectMapper objectMapper) {
    this(null, objectMapper);
  }

  public DefaultNominatimClient(NominatimProperties properties) {
    this(properties, null);
  }

  public DefaultNominatimClient(NominatimProperties properties, ObjectMapper objectMapper) {
    super(properties);
    this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    this.objectMapper.registerModule(new GeoJsonObjectMapperModule());
  }

  public List<SearchResult> geocode(final SearchRequest request) {
    final URL url = buildSearchUrl(request);
    final SearchResult[] resultArray;
    try {
      resultArray = objectMapper.readValue(url, SearchResult[].class);
    } catch (final Exception e) {
      throw new ServiceException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeConstants.GENERAL_REQUEST_ERROR, e);
    }
    if (resultArray == null) {
      return Collections.emptyList();
    }
    return Arrays.asList(resultArray);
  }

  @Override
  public List<ReverseSearchResult> reverseGeocode(ReverseSearchRequest request) {
    return Collections.emptyList();
  }

}
