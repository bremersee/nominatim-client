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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bremersee.geojson.GeoJsonObjectMapperModule;
import org.bremersee.nominatim.model.ReverseSearchRequest;
import org.bremersee.nominatim.model.SearchRequest;
import org.bremersee.nominatim.model.SearchResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Default nominatim client tests.
 *
 * @author Christian Bremer
 */
public class DefaultNominatimClientTest {

  private static final DefaultNominatimClient client = new DefaultNominatimClient();

  private static ObjectMapper objectMapper;

  /**
   * Test setup.
   */
  @BeforeClass
  public static void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModules(
        new Jdk8Module(),
        new JavaTimeModule(),
        new ParameterNamesModule(),
        new GeoJsonObjectMapperModule());
  }

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    Assert.assertNotNull(client.getProperties());
    Assert.assertNotNull(client.getDefaultObjectMapper());
  }

  /**
   * Test real call.
   *
   * @throws Exception the exception
   */
  @Ignore
  @Test
  public void testRealCall() throws Exception {
    final SearchRequest request = SearchRequest
        .builder()
        .query("Unter den Linden 1, Berlin")
        //.query("35 Route de Pont Audemer, 27260 Cormeilles")
        .countryCodes(Collections.singletonList(Locale.GERMANY))
        //.countryCodes(Collections.singletonList(Locale.FRANCE))
        .build();

    List<SearchResult> results = client.geocode(request);

    Assert.assertNotNull(results);
    Assert.assertFalse(results.isEmpty());

    System.out.println("### Search Result #############################");
    String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(results);
    System.out.println(json);
    System.out.println("\n");

    SearchResult sr = results.get(0);
    Assert.assertTrue(sr.hasLatLon());

    SearchResult reverseResult = client.reverseGeocode(
        ReverseSearchRequest
            .builder()
            .lat(new BigDecimal(sr.getLat()))
            .lon(new BigDecimal(sr.getLon()))
            .build());

    Assert.assertNotNull(reverseResult);
    Assert.assertNotNull(reverseResult.getAddress());
    Assert.assertNotNull(reverseResult.getAddress().getRoad());
    Assert.assertEquals("Unter den Linden", reverseResult.getAddress().getRoad());
    Assert.assertNotNull(reverseResult.getAddress().findCity());
    Assert.assertEquals("Berlin", reverseResult.getAddress().findCity());
    Assert.assertNotNull(reverseResult.getAddress().getCountryCode());
    Assert.assertEquals("DE", reverseResult.getAddress().getCountryCode());
    Assert.assertNotNull(reverseResult.getAddress().getFormattedAddress());
    Assert.assertTrue(reverseResult.getAddress().getFormattedAddress()
        .contains(reverseResult.getAddress().getCountry()));

    System.out.println("### Reverse Result #############################");
    json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reverseResult);
    System.out.println(json);
    System.out.println("\n");
  }

}
