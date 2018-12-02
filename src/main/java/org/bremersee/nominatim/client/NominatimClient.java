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

import org.bremersee.nominatim.model.AbstractReverseSearchRequest;
import org.bremersee.nominatim.model.AbstractSearchRequest;

/**
 * General nominatim client interface.
 *
 * @param <S> the search response return type, e. g. {@literal List<SearchResult>} or {@literal
 * Flux<SearchResult>}
 * @param <R> the reverse search response type, e. g. {@literal SearchResult} or {@literal
 * Mono<SearchResult>}
 * @author Christian Bremer
 */
public interface NominatimClient<S, R> {

  /**
   * Makes a geocode search request.
   *
   * @param request the request
   * @return the response
   */
  S geocode(AbstractSearchRequest request);

  /**
   * Makes a reverse geocode search request.
   *
   * @param request the request
   * @return the response
   */
  R reverseGeocode(AbstractReverseSearchRequest request);

}
