package com.partharoypc.smartads.house;

import androidx.annotation.DrawableRes;

/**
 * Represents a custom internal ad (House Ad) to be shown when network ads fail.
 */
public class HouseAd {
    private final String id;
    private final String title;
    private final String description;
    private final String ctaText; // "Install Now", "Play", etc.
    private final String iconUrl; // URL or empty for resource fallback
    private final String imageUrl; // Banner/Media
    private final String clickUrl; // Destination URL
    private final int iconResId; // Local drawable resource fallback
    private final int imageResId; // Local drawable resource fallback
    private final float rating; // 0.0 to 5.0

    private HouseAd(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.ctaText = builder.ctaText;
        this.iconUrl = builder.iconUrl;
        this.imageUrl = builder.imageUrl;
        this.clickUrl = builder.clickUrl;
        this.iconResId = builder.iconResId;
        this.imageResId = builder.imageResId;
        this.rating = builder.rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCtaText() {
        return ctaText;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public float getRating() {
        return rating;
    }

    /**
     * Builder for creating {@link HouseAd} instances.
     */
    public static class Builder {
        private String id;
        private String title;
        private String description;
        private String ctaText = "Install";
        private String iconUrl;
        private String imageUrl;
        private String clickUrl;
        private int iconResId = 0;
        private int imageResId = 0;
        private float rating = 5.0f;

        /** Sets a unique ID for this house ad. */
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        /** Sets the title/headline of the ad. */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /** Sets the body text/description of the ad. */
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        /** Sets the Call To Action button text (e.g., "Install", "Play Now"). */
        public Builder setCtaText(String text) {
            this.ctaText = text;
            return this;
        }

        /** Sets the URL for the app icon image. */
        public Builder setIconUrl(String url) {
            this.iconUrl = url;
            return this;
        }

        /** Sets the URL for the main media/banner image. */
        public Builder setImageUrl(String url) {
            this.imageUrl = url;
            return this;
        }

        /** Sets the destination URL (e.g., App Store link) when clicked. */
        public Builder setClickUrl(String url) {
            this.clickUrl = url;
            return this;
        }

        /** Sets a local drawable resource ID for the app icon. */
        public Builder setIconResId(@DrawableRes int resId) {
            this.iconResId = resId;
            return this;
        }

        /** Sets a local drawable resource ID for the main media/banner image. */
        public Builder setImageResId(@DrawableRes int resId) {
            this.imageResId = resId;
            return this;
        }

        /** Sets the rating stars (0.0 to 5.0). */
        public Builder setRating(float rating) {
            this.rating = rating;
            return this;
        }

        public HouseAd build() {
            return new HouseAd(this);
        }
    }
}
