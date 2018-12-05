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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bremersee.plain.model.UnknownAware;

/**
 * Nominatim address response object.
 *
 * @author Christian Bremer
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("WeakerAccess")
public class Address extends UnknownAware {

  private String address26;

  private String address29;

  @JsonProperty("building")
  private String building; // "Kommandantenhaus"

  @JsonProperty("city")
  private String city; // "Berlin",

  @JsonProperty("city_district")
  private String cityDistrict; // "Mitte",

  @JsonProperty("road")
  private String road; // "Unter den Linden",

  @JsonProperty("continent")
  private String continent; // "European Union",

  @JsonProperty("country")
  private String country; // "Deutschland",

  @JsonProperty("country_code")
  private String countryCode; // "de",

  @JsonProperty("house_number")
  private String houseNumber; // "1",

  @JsonProperty("neighbourhood")
  private String neighbourhood; // "Spandauer Vorstadt",

  @JsonProperty("postcode")
  private String postcode; // "10117",

  @JsonProperty("public_building")
  private String publicBuilding; // "Kommandantenhaus",

  @JsonProperty("state")
  private String state; // "Berlin",

  @JsonProperty("suburb")
  private String suburb; // "Mitte"

  @JsonProperty("tram_stop")
  private String tramStop;

}
