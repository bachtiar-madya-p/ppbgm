package id.io.portal.manager;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import javax.inject.Singleton;
import id.io.portal.util.property.Property;

@Singleton
public class PropertyManager {

    private static PropertyManager instance;

    public static File BASE_DIRECTORY;
    public static File ASSET_DIRECTORY;
    private Properties properties = new Properties();

    private PropertyManager() {
        URL url = null;

        try {
            url = Thread.currentThread().getContextClassLoader().getResource(Property.CONFIGURATION_FILE);
            if (url != null) {
                properties.load(url.openStream());
                File file = new File(url.getFile());
                BASE_DIRECTORY = file.getParentFile();
                ASSET_DIRECTORY = new File(BASE_DIRECTORY.getParentFile().getParentFile(), "asset");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public boolean getBoolProperty(String key) {
        return Boolean.getBoolean(properties.getProperty(key, "false"));

    }

}
