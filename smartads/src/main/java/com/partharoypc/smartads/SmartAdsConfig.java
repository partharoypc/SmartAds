package com.partharoypc.smartads;

import com.google.android.gms.ads.RequestConfiguration;
import com.partharoypc.smartads.house.HouseAd;

import java.util.ArrayList;
import java.util.List;

public class SmartAdsConfig {
    // Ad Unit IDs
    private final String adMobAppOpenId;
    private final String adMobBannerId;
    private final String adMobInterstitialId;
    private final String adMobRewardedId;
    private final String adMobNativeId;

    // Flags
    private final boolean adsEnabled;
    private final boolean isTestMode;
    private final boolean isLoggingEnabled;
    private final boolean collapsibleBannerEnabled;
    private final boolean useUmpConsent;
    private final boolean houseAdsEnabled;

    // Targeting & Limits
    private final String maxAdContentRating;
    private final int tagForChildDirectedTreatment;
    private final int tagForUnderAgeOfConsent;
    private final long frequencyCapSeconds;

    // UI Customization
    private final Integer dialogBackgroundColor;
    private final Integer dialogTextColor;
    private final Integer dialogSubTextColor;
    private final Integer dialogProgressColor;
    private final String dialogText;
    private final String dialogSubText;

    // Testing & House Ads
    private final List<String> testDeviceIds;
    private final List<HouseAd> houseAds;

    public Builder toBuilder() {
        return new Builder(this);
    }

    private SmartAdsConfig(Builder builder) {
        this.adMobAppOpenId = builder.adMobAppOpenId;
        this.adMobBannerId = builder.adMobBannerId;
        this.adMobInterstitialId = builder.adMobInterstitialId;
        this.adMobRewardedId = builder.adMobRewardedId;
        this.adMobNativeId = builder.adMobNativeId;

        this.adsEnabled = builder.adsEnabled;
        this.isTestMode = builder.isTestMode;
        this.isLoggingEnabled = builder.isLoggingEnabled;
        this.collapsibleBannerEnabled = builder.collapsibleBannerEnabled;
        this.useUmpConsent = builder.useUmpConsent;
        this.houseAdsEnabled = builder.houseAdsEnabled;

        this.maxAdContentRating = builder.maxAdContentRating;
        this.tagForChildDirectedTreatment = builder.tagForChildDirectedTreatment;
        this.tagForUnderAgeOfConsent = builder.tagForUnderAgeOfConsent;
        this.frequencyCapSeconds = builder.frequencyCapSeconds;

        this.dialogBackgroundColor = builder.dialogBackgroundColor;
        this.dialogTextColor = builder.dialogTextColor;
        this.dialogSubTextColor = builder.dialogSubTextColor;
        this.dialogProgressColor = builder.dialogProgressColor;
        this.dialogText = builder.dialogText;
        this.dialogSubText = builder.dialogSubText;

        this.testDeviceIds = List.copyOf(builder.testDeviceIds);
        this.houseAds = List.copyOf(builder.houseAds);
    }

    // =============================================================================================
    // GETTERS
    // =============================================================================================

    public String getAdMobAppOpenId() {
        return adMobAppOpenId;
    }

    public String getAdMobBannerId() {
        return adMobBannerId;
    }

    public String getAdMobInterstitialId() {
        return adMobInterstitialId;
    }

    public String getAdMobRewardedId() {
        return adMobRewardedId;
    }

    public String getAdMobNativeId() {
        return adMobNativeId;
    }

    public boolean isAdsEnabled() {
        return adsEnabled;
    }

    public boolean isTestMode() {
        return isTestMode;
    }

    public boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public boolean isCollapsibleBannerEnabled() {
        return collapsibleBannerEnabled;
    }

    public boolean useUmpConsent() {
        return useUmpConsent;
    }

    public boolean isHouseAdsEnabled() {
        return houseAdsEnabled;
    }

    public String getMaxAdContentRating() {
        return maxAdContentRating;
    }

    public int getTagForChildDirectedTreatment() {
        return tagForChildDirectedTreatment;
    }

    public int getTagForUnderAgeOfConsent() {
        return tagForUnderAgeOfConsent;
    }

    public long getFrequencyCapSeconds() {
        return frequencyCapSeconds;
    }

    public Integer getDialogBackgroundColor() {
        return dialogBackgroundColor;
    }

    public Integer getDialogTextColor() {
        return dialogTextColor;
    }

    public Integer getDialogSubTextColor() {
        return dialogSubTextColor;
    }

    public Integer getDialogProgressColor() {
        return dialogProgressColor;
    }

    public String getDialogText() {
        return dialogText;
    }

    public String getDialogSubText() {
        return dialogSubText;
    }

    public List<String> getTestDeviceIds() {
        return testDeviceIds;
    }

    public List<HouseAd> getHouseAds() {
        return houseAds;
    }

    // =============================================================================================
    // HELPER METHODS
    // =============================================================================================

    public boolean isAppOpenConfigured() {
        return adMobAppOpenId != null && !adMobAppOpenId.isEmpty();
    }

    public boolean isBannerConfigured() {
        return adMobBannerId != null && !adMobBannerId.isEmpty();
    }

    public boolean isInterstitialConfigured() {
        return adMobInterstitialId != null && !adMobInterstitialId.isEmpty();
    }

    public boolean isRewardedConfigured() {
        return adMobRewardedId != null && !adMobRewardedId.isEmpty();
    }

    public boolean isNativeConfigured() {
        return adMobNativeId != null && !adMobNativeId.isEmpty();
    }

    public boolean isAnyAdConfigured() {
        return isAppOpenConfigured() || isBannerConfigured() || isInterstitialConfigured() ||
                isRewardedConfigured() || isNativeConfigured();
    }

    // =============================================================================================
    // BUILDER
    // =============================================================================================

    public static class Builder {
        // IDs
        private String adMobAppOpenId = "";
        private String adMobBannerId = "";
        private String adMobInterstitialId = "";
        private String adMobRewardedId = "";
        private String adMobNativeId = "";

        // Flags (All Defaulted to FALSE)
        private boolean adsEnabled = false;
        private boolean isTestMode = false;
        private boolean isLoggingEnabled = false;
        private boolean collapsibleBannerEnabled = false;
        private boolean useUmpConsent = false;
        private boolean houseAdsEnabled = false;

        // Targeting
        private String maxAdContentRating = RequestConfiguration.MAX_AD_CONTENT_RATING_G;
        private int tagForChildDirectedTreatment = RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_UNSPECIFIED;
        private int tagForUnderAgeOfConsent = RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_UNSPECIFIED;

        // Limits
        private long frequencyCapSeconds = 30;

        // UI
        private Integer dialogBackgroundColor = null;
        private Integer dialogTextColor = null;
        private Integer dialogSubTextColor = null;
        private Integer dialogProgressColor = null;
        private String dialogText = "Loading Ad...";
        private String dialogSubText = "Optimizing experience...";

        // Lists
        private final List<String> testDeviceIds = new ArrayList<>();
        private List<HouseAd> houseAds = new ArrayList<>();

        public Builder() {
        }

        /**
         * Copy constructor.
         */
        public Builder(SmartAdsConfig config) {
            this.adMobAppOpenId = config.adMobAppOpenId;
            this.adMobBannerId = config.adMobBannerId;
            this.adMobInterstitialId = config.adMobInterstitialId;
            this.adMobRewardedId = config.adMobRewardedId;
            this.adMobNativeId = config.adMobNativeId;

            this.adsEnabled = config.adsEnabled;
            this.isTestMode = config.isTestMode;
            this.isLoggingEnabled = config.isLoggingEnabled;
            this.collapsibleBannerEnabled = config.collapsibleBannerEnabled;
            this.useUmpConsent = config.useUmpConsent;
            this.houseAdsEnabled = config.houseAdsEnabled;

            this.maxAdContentRating = config.maxAdContentRating;
            this.tagForChildDirectedTreatment = config.tagForChildDirectedTreatment;
            this.tagForUnderAgeOfConsent = config.tagForUnderAgeOfConsent;
            this.frequencyCapSeconds = config.frequencyCapSeconds;

            this.dialogBackgroundColor = config.dialogBackgroundColor;
            this.dialogTextColor = config.dialogTextColor;
            this.dialogSubTextColor = config.dialogSubTextColor;
            this.dialogProgressColor = config.dialogProgressColor;
            this.dialogText = config.dialogText;
            this.dialogSubText = config.dialogSubText;

            this.testDeviceIds.addAll(config.testDeviceIds);
            this.houseAds.addAll(config.houseAds);
        }

        // --- Ad Unit IDs ---

        public Builder setAdMobAppOpenId(String id) {
            this.adMobAppOpenId = id;
            return this;
        }

        public Builder setAdMobBannerId(String id) {
            this.adMobBannerId = id;
            return this;
        }

        public Builder setAdMobInterstitialId(String id) {
            this.adMobInterstitialId = id;
            return this;
        }

        public Builder setAdMobRewardedId(String id) {
            this.adMobRewardedId = id;
            return this;
        }

        public Builder setAdMobNativeId(String id) {
            this.adMobNativeId = id;
            return this;
        }

        // --- Feature Flags ---

        /**
         * Sets whether ads are globally enabled. Default: false
         */
        public Builder setAdsEnabled(boolean enabled) {
            this.adsEnabled = enabled;
            return this;
        }

        /**
         * Sets whether Test Mode is enabled. Default: false
         */
        public Builder setTestModeEnabled(boolean enabled) {
            this.isTestMode = enabled;
            return this;
        }

        /**
         * Sets whether debug logging is enabled. Default: false
         */
        public Builder setLoggingEnabled(boolean enabled) {
            this.isLoggingEnabled = enabled;
            return this;
        }

        /**
         * Sets whether House Ads (internal cross-promotion) are enabled. Default: false
         */
        public Builder setHouseAdsEnabled(boolean enabled) {
            this.houseAdsEnabled = enabled;
            return this;
        }

        /**
         * Sets whether collapsible banners are enabled. Default: false
         */
        public Builder setCollapsibleBannerEnabled(boolean enabled) {
            this.collapsibleBannerEnabled = enabled;
            return this;
        }

        /**
         * Sets whether to use Google UMP for consent. Default: false
         */
        public Builder setUseUmpConsent(boolean useUmp) {
            this.useUmpConsent = useUmp;
            return this;
        }

        // --- Configuration & Limits ---

        public Builder setMaxAdContentRating(String rating) {
            this.maxAdContentRating = rating;
            return this;
        }

        public Builder setTagForChildDirectedTreatment(int tag) {
            this.tagForChildDirectedTreatment = tag;
            return this;
        }

        public Builder setTagForUnderAgeOfConsent(int tag) {
            this.tagForUnderAgeOfConsent = tag;
            return this;
        }

        /**
         * Sets frequency cap for full-screen ads in seconds. Default: 30s
         */
        public Builder setFrequencyCapSeconds(long seconds) {
            this.frequencyCapSeconds = seconds;
            return this;
        }

        // --- UI & Customization ---

        public Builder setLoadingDialogColor(@androidx.annotation.ColorInt int backgroundColor,
                @androidx.annotation.ColorInt int textColor) {
            this.dialogBackgroundColor = backgroundColor;
            this.dialogTextColor = textColor;
            return this;
        }

        public Builder setLoadingDialogSubTextColor(@androidx.annotation.ColorInt int subTextColor) {
            this.dialogSubTextColor = subTextColor;
            return this;
        }

        public Builder setLoadingDialogProgressColor(@androidx.annotation.ColorInt int progressColor) {
            this.dialogProgressColor = progressColor;
            return this;
        }

        public Builder setLoadingDialogText(String headline, String subHeadline) {
            this.dialogText = headline;
            this.dialogSubText = subHeadline;
            return this;
        }

        public Builder setLoadingDialogText(String headline) {
            this.dialogText = headline;
            return this;
        }

        // --- Lists ---

        public Builder addTestDeviceId(String id) {
            if (id != null && !id.isEmpty()) {
                this.testDeviceIds.add(id);
            }
            return this;
        }

        public Builder addHouseAd(HouseAd ad) {
            if (ad != null) {
                this.houseAds.add(ad);
            }
            return this;
        }

        public Builder setHouseAds(List<HouseAd> ads) {
            if (ads != null) {
                this.houseAds = new ArrayList<>(ads);
            }
            return this;
        }

        public SmartAdsConfig build() {
            return new SmartAdsConfig(this);
        }
    }
}
