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

import org.bremersee.nominatim.NominatimProperties;
import org.bremersee.nominatim.model.AbstractReverseSearchRequest;
import org.bremersee.nominatim.model.AbstractSearchRequest;
import org.bremersee.nominatim.model.SearchResult;
import org.bremersee.web.ErrorDetectors;
import org.bremersee.web.reactive.function.client.DefaultWebClientErrorDecoder;
import org.bremersee.web.reactive.function.client.WebClientErrorDecoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The reactive nominatim client implementation.
 *
 * @author Christian Bremer
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ReactiveNominatimClientImpl
    extends AbstractNominatimClient<Flux<SearchResult>, Mono<SearchResult>>
    implements ReactiveNominatimClient<Flux<SearchResult>, Mono<SearchResult>> {

  private WebClient.Builder webClientBuilder;

  private WebClientErrorDecoder<? extends Throwable> webClientErrorDecoder;

  /**
   * Instantiates a new reactive nominatim client.
   */
  public ReactiveNominatimClientImpl() {
    this(null, null);
  }

  /**
   * Instantiates a new reactive nominatim client.
   *
   * @param webClientBuilder the web client builder
   */
  public ReactiveNominatimClientImpl(WebClient.Builder webClientBuilder) {
    this(null, webClientBuilder);
  }

  /**
   * Instantiates a new reactive nominatim client.
   *
   * @param properties the properties
   */
  public ReactiveNominatimClientImpl(NominatimProperties properties) {
    this(properties, null);
  }

  /**
   * Instantiates a new reactive nominatim client.
   *
   * @param properties the properties
   * @param webClientBuilder the web client builder
   */
  public ReactiveNominatimClientImpl(
      NominatimProperties properties,
      WebClient.Builder webClientBuilder) {
    super(properties);
    this.webClientBuilder = webClientBuilder != null
        ? webClientBuilder.clone()
        : WebClient
            .builder()
            .exchangeStrategies(
                ExchangeStrategies
                    .builder()
                    .codecs((clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs()
                        .jackson2JsonDecoder(new Jackson2JsonDecoder(getDefaultObjectMapper()))))
                    .build());
  }

  /**
   * Gets web client error decoder.
   *
   * @return the web client error decoder
   */
  protected WebClientErrorDecoder<? extends Throwable> getWebClientErrorDecoder() {
    if (webClientErrorDecoder == null) {
      webClientErrorDecoder = new DefaultWebClientErrorDecoder();
    }
    return webClientErrorDecoder;
  }

  /**
   * Sets web client error decoder.
   *
   * @param webClientErrorDecoder the web client error decoder
   */
  public void setWebClientErrorDecoder(
      final WebClientErrorDecoder<? extends Throwable> webClientErrorDecoder) {
    this.webClientErrorDecoder = webClientErrorDecoder;
  }

  @Override
  public Flux<SearchResult> geocode(final AbstractSearchRequest request) {
    return webClientBuilder
        .baseUrl(getProperties().getSearchUri())
        .build()
        .get()
        .uri(uriBuilder -> uriBuilder.queryParams(request.buildParameters(true)).build())
        .header("User-Agent", getProperties().getUserAgent())
        .retrieve()
        .onStatus(ErrorDetectors.DEFAULT, getWebClientErrorDecoder())
        .bodyToFlux(SearchResult.class);
  }

  @Override
  public Mono<SearchResult> reverseGeocode(AbstractReverseSearchRequest request) {
    return webClientBuilder
        .baseUrl(getProperties().getReverseUri())
        .build()
        .get()
        .uri(uriBuilder -> uriBuilder.queryParams(request.buildParameters(true)).build())
        .header("User-Agent", getProperties().getUserAgent())
        .retrieve()
        .onStatus(ErrorDetectors.DEFAULT, getWebClientErrorDecoder())
        .bodyToMono(SearchResult.class);
  }

}
