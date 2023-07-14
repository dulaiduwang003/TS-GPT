package com.cn.app.superbot.types.chat;

/**
 * The type Location hints.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class LocationHints {
    /**
     * The Country.
     */
    private final String country;
    /**
     * The State.
     */
    private final String state;
    /**
     * The City.
     */
    private final String city;
    /**
     * The Zipcode.
     */
    private final String zipcode;
    /**
     * The Timezoneoffset.
     */
    private final int timezoneoffset;
    /**
     * The Country confidence.
     */
    private final int countryConfidence;
    /**
     * The City confidence.
     */
    private final int cityConfidence;
    /**
     * The Center.
     */
    private final Location Center;
    /**
     * The Region type.
     */
    private final int RegionType = 2;
    /**
     * The Source type.
     */
    private final int SourceType = 1;

    /**
     * Instantiates a new Location hints.
     *
     * @param country           the country
     * @param state             the state
     * @param city              the city
     * @param zipcode           the zipcode
     * @param timezoneoffset    the timezoneoffset
     * @param countryConfidence the country confidence
     * @param cityConfidence    the city confidence
     * @param center            the center
     */
    public LocationHints(String country, String state, String city, String zipcode, int timezoneoffset, int countryConfidence, int cityConfidence, Location center) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipcode = zipcode;
        this.timezoneoffset = timezoneoffset;
        this.countryConfidence = countryConfidence;
        this.cityConfidence = cityConfidence;
        Center = center;
    }
}
