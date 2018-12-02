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
import java.math.BigDecimal;
import java.util.List;
import org.bremersee.geojson.GeoJsonObjectMapperModule;
import org.bremersee.nominatim.model.ReverseSearchRequest;
import org.bremersee.nominatim.model.SearchRequest;
import org.bremersee.nominatim.model.SearchResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive nominatim client tests.
 *
 * @author Christian Bremer
 */
public class ReactiveNominatimClientTest {

  private static final ReactiveNominatimClientImpl client = new ReactiveNominatimClientImpl();

  private static ObjectMapper objectMapper;

  /**
   * Test setup.
   */
  @BeforeClass
  public static void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new GeoJsonObjectMapperModule());
  }

  @Test
  public void testContructor() {
    Assert.assertNotNull(client.getProperties());
    Assert.assertNotNull(client.getDefaultObjectMapper());
  }

  @Ignore
  @Test
  public void testRealCall() throws Exception {
    final SearchRequest request = SearchRequest
        .builder()
        .query("Unter den Linden 1, Berlin")
        .build();

    Flux<SearchResult> flux = client.geocode(request);
    List<SearchResult> results = flux.collectList().block();

    Assert.assertNotNull(results);
    Assert.assertFalse(results.isEmpty());

    System.out.println("### Search Result #############################");
    String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(results);
    System.out.println(json);
    System.out.println("\n");

    SearchResult sr = results.get(0);
    Assert.assertTrue(sr.hasLatLon());

    Mono<SearchResult> mono = client.reverseGeocode(
        ReverseSearchRequest
            .builder()
            .lat(new BigDecimal(sr.getLat()))
            .lon(new BigDecimal(sr.getLon()))
            .build());

    SearchResult reverseResult = mono.block();

    Assert.assertNotNull(reverseResult);
    Assert.assertNotNull(reverseResult.getAddress());
    Assert.assertNotNull(reverseResult.getAddress().getRoad());
    Assert.assertEquals("Unter den Linden", reverseResult.getAddress().getRoad());

    System.out.println("### Reverse Result #############################");
    json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reverseResult);
    System.out.println(json);
    System.out.println("\n");
  }

}
