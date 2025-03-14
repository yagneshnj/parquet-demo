package com.example.parquet_demo;

import com.example.parquet_demo.model.PackageInfo;
import com.example.parquet_demo.service.PackageInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PackageInfoServiceTest {

    @Autowired
    private PackageInfoService packageInfoService;

    @Test
    public void testGetAllPackages() {
        List<PackageInfo> packages = packageInfoService.getAllPackages();
        assertNotNull(packages, "Package list should not be null");
        assertFalse(packages.isEmpty(), "Package list should not be empty");
    }

    @Test
    public void testPackageDataValidity() {
        List<PackageInfo> packages = packageInfoService.getAllPackages();
        for (PackageInfo info : packages) {
            assertNotNull(info.getPackageManager(), "Package manager should not be null");
            assertNotNull(info.getPackageName(), "Package name should not be null");
            assertNotNull(info.getPackageVersion(), "Package version should not be null");
            assertNotNull(info.getSpdxLicenseId(), "SPDX License ID should not be null");
            assertNotNull(info.getMetadataSource(), "Metadata source should not be null");
        }
    }

    @Test
    public void testGetPackageByName() {
        List<PackageInfo> packages = packageInfoService.getAllPackages();
        // If we have at least one record, test retrieval by name.
        if (!packages.isEmpty()) {
            String testName = packages.get(0).getPackageName();
            PackageInfo result = packageInfoService.getPackageByName(testName);
            assertNotNull(result, "Should retrieve a package info for the given package name");
            assertEquals(testName, result.getPackageName(), "Package name should match");
        } else {
            fail("No packages were found in the Parquet file to test getPackageByName");
        }
    }
}
