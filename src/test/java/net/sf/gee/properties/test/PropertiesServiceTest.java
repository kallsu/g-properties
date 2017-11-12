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
package net.sf.gee.properties.test;

import org.junit.Assert;
import org.junit.Test;

import net.sf.gee.properties.PropertiesNameEnum;
import net.sf.gee.properties.PropertiesService;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class PropertiesServiceTest {

  @Test
  public void test() {
    PropertiesService.getInstance();
  }

  @Test
  public void testGetValue() {

    String value =
        PropertiesService.getInstance().getValue(PropertiesNameEnum.CLASSPATH_FILES_PROPERTY_NAME);
    Assert.assertEquals(value, PropertiesNameEnum.CLASSPATH_FILES_PROPERTY_NAME.getDefaultValue());

    Integer intObj = PropertiesService.getInstance().getValueAsInteger("test.property.integer");
    Assert.assertNotNull(intObj);

  }
}
