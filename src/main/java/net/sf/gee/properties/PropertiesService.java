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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import net.sf.gee.common.util.string.StringUtil;
import net.sf.gee.logger.factory.GLogFactory;
import net.sf.gee.logger.log.SimpleGLogger;

/**
 * The properties service manager.
 *
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 */
public class PropertiesService {

  private static final SimpleGLogger LOGGER =
      GLogFactory.getInstance().getLogger(SimpleGLogger.class, PropertiesService.class);

  /** The MONITOR. */
  private static final Object MONITOR = new Object();

  /** The singleton instance. */
  private static PropertiesService INSTANCE = null;

  /** The properties container. */
  private final Properties properties = new Properties();

  /**
   * Instantiates a new properties service.
   */
  private PropertiesService() {

    // read configuration
    readConfiguration();

    // read classpath files
    readClasspathFiles();

    // read external files
    readExternalFiles();

    LOGGER.logInfo("Properties contains [%s] values", properties.size());
  }

  /**
   * Read configuration.
   */
  private final void readConfiguration() {

    LOGGER.logInfo("Looking for properties file [%s] inside classapth.",
        PropertiesNameEnum.DEFAULT_CONFIGURATION_FILE_NAME.getPropertyName());

    // open file
    try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(
        PropertiesNameEnum.DEFAULT_CONFIGURATION_FILE_NAME.getPropertyName());) {

      // load properties
      if (in != null) {
        properties.load(in);
      }

    }
    catch (IOException e) {
      LOGGER.logError("Cannot read configuration file [%s] inside classpath.", e,
          PropertiesNameEnum.DEFAULT_CONFIGURATION_FILE_NAME.getPropertyName());
    }

  }

  /**
   * Read classpath files.
   */
  private final void readClasspathFiles() {

    // check
    String files =
        properties.getProperty(PropertiesNameEnum.CLASSPATH_FILES_PROPERTY_NAME.getPropertyName());

    if (StringUtil.isEmpty(files)) {
      LOGGER.logInfo("No need to read other properties files inside classpath.");

      return;
    }

    // go on
    String[] filesArray = files.split(",");

    // check
    if (filesArray != null && filesArray.length > 0) {

      // iterate
      for (String current : filesArray) {

        // trim file name
        String file = current.trim();

        LOGGER.logDebug("Load file [%s]", file);

        // open file
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);) {

          // temporary container
          final Properties pTemp = new Properties();

          // load temp container
          pTemp.load(in);

          // put all in main container
          properties.putAll(pTemp);

          LOGGER.logDebug("Load file [%s]. OK", file);
        }
        catch (IOException e) {
          LOGGER.logError("Cannot read file [%s] inside classpath.", e, file);
        }
      }
    }

  }

  /**
   * Read external files.
   */
  private final void readExternalFiles() {

    // check
    String files =
        properties.getProperty(PropertiesNameEnum.EXTERNAL_FILES_PROPERTY_NAME.getPropertyName());

    if (StringUtil.isEmpty(files)) {
      LOGGER.logInfo("No need to read other properties files from external parts.");

      return;
    }

    // go on
    String[] filesArray = files.split(",");

    // check
    if (filesArray != null && filesArray.length > 0) {

      // iterate
      for (String current : filesArray) {

        // trim file name
        String file = current.trim();

        LOGGER.logDebug("Load file [%s]", file);

        LOGGER.logDebug("Load external file [%s]", file);

        // open file
        try (InputStream in = new FileInputStream(new File(new URI(file)));) {

          // temporary container
          final Properties pTemp = new Properties();

          // load temp container
          pTemp.load(in);

          // put all in main container
          properties.putAll(pTemp);

          LOGGER.logDebug("Load external file [%s]", file);
        }
        catch (Exception e) {
          LOGGER.logError("Cannot read external file [%s].", e, file);
        }
      }
    }

  }

  /**
   * Gets the single instance of PropertiesService.
   *
   * @return single instance of PropertiesService
   */
  public static PropertiesService getInstance() {

    // check instance
    if (INSTANCE == null) {

      synchronized (MONITOR) {
        if (INSTANCE == null) {
          INSTANCE = new PropertiesService();
        }
      }
    }

    return INSTANCE;
  }

  /**
   * Get value.
   * 
   * @param name property name
   * @param defaultValue default value to return
   * 
   * @return value if property name is found, otherwise default value.
   */
  private String getValue(String name, String defaultValue) {

    // get value
    String value = (String) properties.get(name);

    // check
    if (StringUtil.isEmpty(value)) {
      // set as default
      value = defaultValue;
    }

    return value;
  }

  /**
   * Get value using the enumeration value
   * 
   * @param enumValue {@link GPropertiesNames}
   * 
   * @return value if property name is found, otherwise default value.
   */
  public String getValue(GPropertiesNames enumValue) {

    return getValue(enumValue.getPropertyName(), enumValue.getDefaultValue());
  }

  /**
   * Gets the properties value.
   *
   * @param name the name of properties
   * 
   * @return if property not exists than method returns the input value, otherwise is
   *         <code>null</code>
   */
  public String getValue(String name) {
    return getValue(name, null);
  }

  /**
   * Gets the value as {@linkplain java.lang.Integer}.
   *
   * @param name the name of properties
   * 
   * @return if property exists and its value is parsable as Integer, method returns
   *         {@linkplain java.lang.Integer}, otherwise method returns <code>null</code>.
   */
  public Integer getValueAsInteger(String name) {

    // get value
    String value = getValue(name);

    // return null because property not found.
    if (value == null) {
      return null;

    } // parse value
    else {

      Integer valueInt = null;

      try {
        // try to parse value
        valueInt = Integer.parseInt(value);
      }
      catch (NumberFormatException e) {
        // not parsable value
        valueInt = null;
      }

      return valueInt;
    }
  }

  /**
   * Reload.
   */
  public void reload() {

    synchronized (properties) {

      // flush all
      properties.clear();

      // read configuration
      readConfiguration();

      // check
      if (!properties.isEmpty()) {

        // read classpath files
        readClasspathFiles();

        // read external files
        readExternalFiles();

        LOGGER.logInfo("Properties contains [%s] values", properties.size());
      }
      else {
        // log information
        LOGGER.logInfo("Properties object is empty. No files read.");
      }
    }
  }

}
