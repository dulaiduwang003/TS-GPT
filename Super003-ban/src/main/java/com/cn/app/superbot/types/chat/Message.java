package com.cn.app.superbot.types.chat;

/**
 * The type Message.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class Message {
    /**
     * The Locale.
     */
    private final String locale;
    /**
     * The Market.
     */
    private final String market;
    /**
     * The Region.
     */
    private final String region;
    /**
     * The Location.
     */
    private final String location;
    /**
     * The Location hints.
     */
    private final LocationHints locationHints;
    /**
     * The Timestamp.
     */
    private final String timestamp;
    /**
     * The Author.
     */
    private final String author = "user";
    /**
     * The Input method.
     */
    private final String inputMethod = "Keyboard";
    /**
     * The Text.
     */
    private final String text;
    /**
     * The Message type.
     */
    private final String messageType = "Chat";

    /**
     * Instantiates a new Message.
     *
     * @param locale        the locale
     * @param market        the market
     * @param region        the region
     * @param location      the location
     * @param locationHints the location hints
     * @param timestamp     the timestamp
     * @param text          the text
     */
    public Message(String locale, String market, String region, String location, LocationHints locationHints, String timestamp, String text) {
        this.locale = locale;
        this.market = market;
        this.region = region;
        this.location = location;
        this.locationHints = locationHints;
        this.timestamp = timestamp;
        this.text = text;
    }
}
