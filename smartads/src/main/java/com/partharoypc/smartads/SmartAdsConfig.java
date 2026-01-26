package com.partharoypc.smartads;

import com.google.android.gms.ads.RequestConfiguration;

public class SmartAdsConfig {
    // Ad Unit IDs (minimal set)
    private final String adMobAppOpenId;
    private final String adMobBannerId;
    private final String adMobInterstitialId;
    private final String adMobRewardedId;
    private final String adMobNativeId;

    // Basic settings
    private final boolean adsEnabled;
    private final boolean isTestMode;
    private final boolean collapsibleBannerEnabled;
    private final boolean useUmpConsent;

    // Targeting
    private final String maxAdContentRating;
    private final int tagForChildDirectedTreatment;
    private final int tagForUnderAgeOfConsent;

    // Dialog Styling
    private final Integer dialogBackgroundColor;
    private final Integer dialogTextColor;
    private final String dialogText;
    private final long frequencyCapSeconds;
    private final java.util.List<String> testDeviceIds;

    private SmartAdsConfig(Builder builder) {
        this.adMobAppOpenId = builder.adMobAppOpenId;
        this.adMobBannerId = builder.adMobBannerId;
        this.adMobInterstitialId = builder.adMobInterstitialId;
        this.adMobRewardedId = builder.adMobRewardedId;
        this.adMobNativeId = builder.adMobNativeId;
        this.adsEnabled = builder.adsEnabled;
        this.isTestMode = builder.isTestMode;
        this.collapsibleBannerEnabled = builder.collapsibleBannerEnabled;
        this.useUmpConsent = builder.useUmpConsent;
        this.dialogBackgroundColor = builder.dialogBackgroundColor;
        this.dialogTextColor = builder.dialogTextColor;
        this.dialogText = builder.dialogText;
        this.frequencyCapSeconds = builder.frequencyCapSeconds;
        this.testDeviceIds = java.util.Collections.unmodifiableList(new java.util.ArrayList<>(builder.testDeviceIds));
        this.maxAdContentRating = builder.maxAdContentRating;
        this.tagForChildDirectedTreatment = builder.tagForChildDirectedTreatment;
        this.tagForUnderAgeOfConsent = builder.tagForUnderAgeOfConsent;
    }

    // Getters
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

    public boolean isCollapsibleBannerEnabled() {
        return collapsibleBannerEnabled;
    }

    public boolean useUmpConsent() {
        return useUmpConsent;
    }

    public Integer getDialogBackgroundColor() {
        return dialogBackgroundColor;
    }

    public Integer getDialogTextColor() {
        return dialogTextColor;
    }

    public String getDialogText() {
        return dialogText;
    }

    public java.util.List<String> getTestDeviceIds() {
        return testDeviceIds;
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
        return isAppOpenConfigured() || isBannerConfigured() || isInterstitialConfigured() || isRewardedConfigured()
                || isNativeConfigured();
    }

    public static class Builder {
        private String adMobAppOpenId = "";
        private String adMobBannerId = "";
        private String adMobInterstitialId = "";
        private String adMobRewardedId = "";
        private String adMobNativeId = "";

        private boolean adsEnabled = false;
        private boolean isTestMode = false;
        private boolean collapsibleBannerEnabled = false;
        private boolean useUmpConsent = false;
        private Integer dialogBackgroundColor = null;
        private Integer dialogTextColor = null;
        private String dialogText = "Loading Ad...";
        private long frequencyCapSeconds = 30; // 30 seconds default cap
        private java.util.List<String> testDeviceIds = new java.util.ArrayList<>();

        // Defaults
        private String maxAdContentRating = RequestConfiguration.MAX_AD_CONTENT_RATING_G;
        private int tagForChildDirectedTreatment = RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_UNSPECIFIED;
        private int tagForUnderAgeOfConsent = RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_UNSPECIFIED;

        public Builder setLoadingDialogColor(@androidx.annotation.ColorInt int backgroundColor,
                @androidx.annotation.ColorInt int textColor) {
            this.dialogBackgroundColor = backgroundColor;
            this.dialogTextColor = textColor;
            return this;
        }

        public Builder setLoadingDialogText(String text) {
            this.dialogText = text;
            return this;
        }

        public Builder setAdsEnabled(boolean enabled) {
            this.adsEnabled = enabled;
            return this;
        }

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

        public Builder enableTestMode(boolean testMode) {
            this.isTestMode = testMode;
            return this;
        }

        public Builder enableCollapsibleBanner(boolean enabled) {
            this.collapsibleBannerEnabled = enabled;
            return this;
        }

        public Builder setUseUmpConsent(boolean useUmp) {
            this.useUmpConsent = useUmp;
            return this;
        }

        public Builder setFrequencyCap(long seconds) {
            this.frequencyCapSeconds = seconds;
            return this;
        }

        public Builder addTestDeviceId(String testDeviceId) {
            if (testDeviceId != null && !testDeviceId.isEmpty()) {
                this.testDeviceIds.add(testDeviceId);
            }
            return this;
        }

        /**
         * Sets max ad content rating.
         * 
         * @param rating One of RequestConfiguration.MAX_AD_CONTENT_RATING_*.
         */
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

        public SmartAdsConfig build() {
            return new SmartAdsConfig(this);
        }
    }
}
