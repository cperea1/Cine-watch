package com.example.cinewatch20.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class WatchProviders {

    @SerializedName("results")
    private Map<String, ProviderInfo> providerMap;

    public Map<String, ProviderInfo> getProviderMap() {
        return providerMap;
    }

    public void setProviderMap(Map<String, ProviderInfo> providerMap) {
        this.providerMap = providerMap;
    }

    public ProviderInfo getProviderInfoForRegion(String region) {
        return providerMap.get(region);
    }


    public static class Region {

    } //end Region Class

    public static class ProviderInfo {
        @SerializedName("link")
        private String providerLink;
        @SerializedName("flatrate")
        private List<Provider> flatrateProviders;
        @SerializedName("rent")
        private List<Provider> rentProviders;
        @SerializedName("buy")
        private List<Provider> buyProviders;

        public String getProviderLink() {
            return providerLink;
        }

        public void setProviderLink(String providerLink) {
            this.providerLink = providerLink;
        }

        public List<Provider> getFlatrateProviders() {
            return flatrateProviders;
        }

        public void setFlatrateProviders(List<Provider> flatrateProviders) {
            this.flatrateProviders = flatrateProviders;
        }

        public List<Provider> getRentProviders() {
            return rentProviders;
        }

        public void setRentProviders(List<Provider> rentProviders) {
            this.rentProviders = rentProviders;
        }

        public List<Provider> getBuyProviders() {
            return buyProviders;
        }

        public void setBuyProviders(List<Provider> buyProviders) {
            this.buyProviders = buyProviders;
        }
    }

    public static class Provider {
        @SerializedName("provider_id")
        private int providerId;
        @SerializedName("provider_name")
        private String providerName;
        @SerializedName("logo_path")
        private String logoPath;

        public int getProviderId() {
            return providerId;
        }

        public void setProviderId(int providerId) {
            this.providerId = providerId;
        }

        public String getProviderName() {
            return providerName;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

        public String getLogoPath() {
            return logoPath;
        }

        public void setLogoPath(String logoPath) {
            this.logoPath = logoPath;
        }
    } //end Provider class
} //end class
