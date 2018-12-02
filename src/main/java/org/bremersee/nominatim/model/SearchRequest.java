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

/**
 * @author Christian Bremer
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SearchRequest extends AbstractSearchRequest {

  private String query;

  @Override
  protected MultiValueMap<String, String> buildSearchParameters(boolean urlEncode) {
    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("q", encodeQueryParameter(query, urlEncode));
    return map;
  }

  public static SearchRequestBuilder builder() {
    return new SearchRequestBuilder();
  }

  public static class SearchRequestBuilder extends AbstractSearchRequestBuilder<SearchRequest> {

    private String query;

    public SearchRequestBuilder query(String query) {
      this.query = query;
      return this;
    }

    @Override
    protected SearchRequest doSearchRequestBuild() {
      final SearchRequest target = new SearchRequest();
      target.setQuery(query);
      return target;
    }
  }
}
