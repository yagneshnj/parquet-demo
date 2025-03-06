package com.example.parquet_demo.model;

public class PackageInfo {
    private String packageManager;
    private String packageName;
    private String packageVersion;
    private String spdxLicenseId;
    private String metadataSource;

    public PackageInfo() {}

    public PackageInfo(String packageManager, String packageName, String packageVersion, String spdxLicenseId, String metadataSource) {
        this.packageManager = packageManager;
        this.packageName = packageName;
        this.packageVersion = packageVersion;
        this.spdxLicenseId = spdxLicenseId;
        this.metadataSource = metadataSource;
    }

    // Getters and Setters
    public String getPackageManager() {
        return packageManager;
    }
    public void setPackageManager(String packageManager) {
        this.packageManager = packageManager;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getPackageVersion() {
        return packageVersion;
    }
    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }
    public String getSpdxLicenseId() {
        return spdxLicenseId;
    }
    public void setSpdxLicenseId(String spdxLicenseId) {
        this.spdxLicenseId = spdxLicenseId;
    }
    public String getMetadataSource() {
        return metadataSource;
    }
    public void setMetadataSource(String metadataSource) {
        this.metadataSource = metadataSource;
    }

    @Override
    public String toString() {
        return "PackageInfo{" +
                "packageManager='" + packageManager + '\'' +
                ", packageName='" + packageName + '\'' +
                ", packageVersion='" + packageVersion + '\'' +
                ", spdxLicenseId='" + spdxLicenseId + '\'' +
                ", metadataSource='" + metadataSource + '\'' +
                '}';
    }
}

