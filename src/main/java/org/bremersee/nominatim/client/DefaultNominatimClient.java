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
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bremersee.exception.ServiceException;
import org.bremersee.geojson.GeoJsonObjectMapperModule;
import org.bremersee.nominatim.NominatimProperties;
import org.bremersee.nominatim.exception.ErrorCodeConstants;
import org.bremersee.nominatim.model.AbstractReverseSearchRequest;
import org.bremersee.nominatim.model.AbstractSearchRequest;
import org.bremersee.nominatim.model.SearchResult;
import org.springframework.util.FileCopyUtils;

/**
 * Default nominatim client that uses {@link URL#openConnection()}.
 *
 * @author Christian Bremer
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DefaultNominatimClient
    extends AbstractNominatimClient<List<SearchResult>, SearchResult>
    implements TraditionalNominatimClient<List<SearchResult>, SearchResult> {

  private final ObjectMapper objectMapper;

  /**
   * Instantiates a new default nominatim client.
   */
  public DefaultNominatimClient() {
    this(null, null);
  }

  /**
   * Instantiates a new default nominatim client.
   *
   * @param objectMapper the object mapper (can be {@code null} - a default one will be used then)
   */
  public DefaultNominatimClient(final ObjectMapper objectMapper) {
    this(null, objectMapper);
  }

  /**
   * Instantiates a new default nominatim client.
   *
   * @param properties the properties
   */
  public DefaultNominatimClient(final NominatimProperties properties) {
    this(properties, null);
  }

  /**
   * Instantiates a new default nominatim client.
   *
   * @param properties the properties
   * @param objectMapper the object mapper (can be {@code null} - a default one will be used then)
   */
  public DefaultNominatimClient(
      final NominatimProperties properties,
      final ObjectMapper objectMapper) {

    super(properties);
    if (objectMapper == null) {
      this.objectMapper = new ObjectMapper();
      this.objectMapper.registerModules(
          new Jdk8Module(),
          new JavaTimeModule(),
          new ParameterNamesModule());
    } else {
      this.objectMapper = objectMapper;
    }
    this.objectMapper.registerModule(new GeoJsonObjectMapperModule());
  }

  @Override
  public List<SearchResult> geocode(final AbstractSearchRequest request) {
    final URL url = buildSearchUrl(request);
    final SearchResult[] resultArray = call(url, SearchResult[].class);
    if (resultArray == null) {
      return Collections.emptyList();
    }
    return Arrays.asList(resultArray);
  }

  @Override
  public SearchResult reverseGeocode(final AbstractReverseSearchRequest request) {
    final URL url = buildReverseSearchUrl(request);
    return call(url, SearchResult.class);
  }

  private <T> T call(final URL url, final Class<T> responseClass) {
    HttpURLConnection con = null;
    try {
      con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty("User-Agent", getProperties().getUserAgent());
      con.connect();
      int statusCode = con.getResponseCode();
      if (statusCode >= 400) {
        try (InputStream errorStream = con.getErrorStream()) {
          final String body = new String(
              FileCopyUtils.copyToByteArray(errorStream), StandardCharsets.UTF_8);
          throw new ServiceException(statusCode, body, ErrorCodeConstants.GENERAL_REQUEST_ERROR);

        } catch (IOException e) {
          throw new ServiceException(
              statusCode,
              "Reading error stream failed.",
              ErrorCodeConstants.GENERAL_REQUEST_ERROR,
              e);
        }
      }
      try (InputStream inputStream = con.getInputStream()) {
        return objectMapper.readValue(inputStream, responseClass);

      } catch (IOException e) {
        throw new ServiceException(
            500, "Reading input stream failed.", ErrorCodeConstants.GENERAL_REQUEST_ERROR, e);
      }
    } catch (IOException e) {
      throw new ServiceException(
          500, "Connecting to " + url + " failed.", ErrorCodeConstants.GENERAL_REQUEST_ERROR, e);
    } finally {
      if (con != null) {
        con.disconnect();
      }
    }
  }

}
