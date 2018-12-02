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

import org.bremersee.nominatim.model.SearchRequest;
import org.bremersee.nominatim.model.SearchResult;
import org.junit.Test;
import reactor.core.publisher.Flux;

/**
 * @author Christian Bremer
 */
public class ReactiveNominatimClientTest {

  private static final ReactiveNominatimClientImpl client = new ReactiveNominatimClientImpl();

  @Test
  public void testReal() {
    final SearchRequest request = SearchRequest
        .builder()
        .query("Unter den Linden 1, Berlin")
        .build();

    Flux<SearchResult> flux = client.geocode(request);
    System.out.println(flux.collectList().block());
  }

}
