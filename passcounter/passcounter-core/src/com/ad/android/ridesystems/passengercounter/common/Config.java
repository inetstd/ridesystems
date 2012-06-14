package com.ad.android.ridesystems.passengercounter.common;

/**
 * Class for constant or per installation variables.
 * 
 * Singleton.
 * 
 *
 */
public class Config {
	
	
	public static final String C_PREFS_NAME = "RIDE_SYSTEM_PREFERENCES";
	
	public static final String C_LAST_LOCAL_DATA_SYNC = "C_LAST_LOCAL_DATE_SYNC";
	
	public static final String C_LAST_APP_DATA_SYNC = "C_LAST_APP_DATA_SYNC";
	
	public static String C_LAST_SCHEDULE_DATA_SYNC = "C_LAST_SCHEDULE_DATA_SYNC";
	
	public static final String C_DEFAULT_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
	
	public static final String C_LOG_DATE_FORMAT = "dd.MM HH:mm";

	public static final String C_LOGIN_TYPE = "C_LOGIN_TYPE";

	public static final String C_WS_TOKEN = "C_WS_TOKEN";
	
	public static final String C_WS_URL = "C_WS_URL";
	
	public static final String C_MAP_URL = "C_WS_SERVICE_URL";
	
	public static final String C_DEVICE_NAME = "C_DEVICE_NAME";
	
	public static final String C_WEB_ADDRESS = "C_WEB_ADDRESS";

	public static final long C_SYNC_SERVICE_PERIOD = 60000; // 30 seconds
	
	public static final long C_SYNC_SCHEDULE_PERIOD = 1000 * 60 * 60 * 24; // 1 day
	
	public static final boolean DEBUG = true;

	public static final String C_SCHEDULE_FILE_NAME = "schedule.dat";	
		
	private String webServiceUrl = "http://www.ridesystems.net/Rideservices1_2/RideWCFService.svc";
	
	private String webServiceToken = "";
	
	private String mapUrl = "http://{webAddress}/m/Route.aspx?RouteID={RouteID}";
	
	private String applicationSettingsConfigPassword = "1791";
	
	private String dataBaseName = "ride_systems_passenget_counter_db";
	
	private String imei = "";
	
	private String deviceName = "noname";
	
	private String webAddress = "";
	
	private Integer dataBaseVersion = 30;
	
	private static Config instance = null;  
	

	
	/**
	 * Private constructor for a singleton pattern
	 */
	private Config() {}
	
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
			instance.readConfig();
		}
		return instance;
	}
	

	/**
	 * Read config variables
	 */
	private void readConfig() {
		
	}

	public String getWebServiceUrl() {
		return webServiceUrl;
	}

	public void setWebServiceUrl(String webServiceUrl) {
		this.webServiceUrl = webServiceUrl;
	}

	public String gebWebServiceToken() {
		return webServiceToken;
	}

	public void setWebServiceToken(String webServiceToken) {
		this.webServiceToken = webServiceToken;
	}

	public String getDataBaseName() {
		return dataBaseName;
	}

	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	public String getWebServiceToken() {	
		return webServiceToken;
	}

	public Integer getDataBaseVersion() {
		return dataBaseVersion;
	}

	public void setDataBaseVersion(Integer dataBaseVersion) {
		this.dataBaseVersion = dataBaseVersion;
	}

	public String getApplicationSettingsConfigPassword() {
		return applicationSettingsConfigPassword;
	}

	public void setApplicationSettingsConfigPassword(String applicationSettingsConfig) {
		this.applicationSettingsConfigPassword = applicationSettingsConfig;
	}

	public String getMapUrl() {
		return mapUrl;
	}
	
	public String getMapUrl(int id) {
		mapUrl = mapUrl.replace("{webAddress}", getWebAddress()); 
		return mapUrl.replace("{RouteID}", id + "");
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the webAddress
	 */
	public String getWebAddress() {		
		return webAddress;
	}

	/**
	 * @param webAddress the webAddress to set
	 */
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	
	
		
}
