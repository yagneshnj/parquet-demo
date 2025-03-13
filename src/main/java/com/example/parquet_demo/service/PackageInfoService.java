package com.example.parquet_demo.service;

import com.example.parquet_demo.model.PackageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class PackageInfoService {
    private final List<PackageInfo> packages = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            // Locate the Parquet file from the classpath
            URL resourceUrl = getClass().getClassLoader().getResource("package_metadata.parquet");
            if (resourceUrl == null) {
                throw new IllegalStateException("Resource package_metadata.parquet not found in classpath.");
            }
            URI resourceURI = resourceUrl.toURI();
            File parquetFile;
            if ("jar".equals(resourceURI.getScheme())) {
                // Resource is packaged inside a jar: copy to a temporary file
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("package_metadata.parquet")) {
                    if (is == null) {
                        throw new IllegalStateException("Could not open resource stream for package_metadata.parquet");
                    }
                    parquetFile = File.createTempFile("package_metadata", ".parquet");
                    parquetFile.deleteOnExit();
                    Files.copy(is, parquetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                // Resource is directly available on the filesystem
                parquetFile = new File(resourceURI);
            }
            String parquetFilePath = parquetFile.getAbsolutePath();

            // Load DuckDB driver and create a connection (in-memory database)
            Class.forName("org.duckdb.DuckDBDriver");
            try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
                // Construct and execute SQL query using DuckDB's read_parquet function
                String sql = "SELECT package_manager, package_name, package_version, spdx_license_id, metadata_source " +
                        "FROM read_parquet('" + parquetFilePath.replace("\\", "\\\\") + "')";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        PackageInfo info = new PackageInfo();
                        info.setPackageManager(rs.getString("package_manager"));
                        info.setPackageName(rs.getString("package_name"));
                        info.setPackageVersion(rs.getString("package_version"));
                        info.setSpdxLicenseId(rs.getString("spdx_license_id"));
                        info.setMetadataSource(rs.getString("metadata_source"));
                        packages.add(info);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PackageInfo> getAllPackages() {
        return packages;
    }

    public PackageInfo getPackageByName(String packageName) {
        return packages.stream()
                .filter(pkg -> pkg.getPackageName().equalsIgnoreCase(packageName))
                .findFirst()
                .orElse(null);
    }
}
