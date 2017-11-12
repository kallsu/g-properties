/**
 * -------------------------------------------------------------------------------------------------
 * 
 * Copyright 2015 - Giorgio Desideri
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * 
 * See the License for the specific language governing permissions and limitations under the
 * License.
 * 
 */
package net.sf.gee.properties;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public enum PropertiesNameEnum implements GPropertiesNames {

  // default configuration file name
  DEFAULT_CONFIGURATION_FILE_NAME("g-properties.conf", "g-properties.conf"),

  // classpath files property
  CLASSPATH_FILES_PROPERTY_NAME("classpath.property.files", "classpath.property.files"),

  // external files property
  EXTERNAL_FILES_PROPERTY_NAME("external.property.files", "external.property.files")

  ;

  private String propertyName = null;

  private String defaultValue = null;

  /**
   * Instantiates a new properties name enum.
   *
   * @param propertyName the property name
   */
  private PropertiesNameEnum(String propertyName, String defaultValue) {
    this.propertyName = propertyName;
    this.defaultValue = defaultValue;
  }

  @Override
  public String getPropertyName() {
    return propertyName;
  }

  @Override
  public String getDefaultValue() {
    return defaultValue;
  }

}
