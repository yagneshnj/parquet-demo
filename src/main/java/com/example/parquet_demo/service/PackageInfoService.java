package com.example.parquet_demo.service;

import com.example.parquet_demo.model.PackageInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class PackageInfoService {
    private final List<PackageInfo> packages = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            URL resourceUrl = getClass().getClassLoader().getResource("package_metadata.parquet");
            if (resourceUrl == null) {
                throw new IllegalStateException("Resource package_metadata.parquet not found in classpath.");
            }
            URI resourceURI = resourceUrl.toURI();
            Path parquetPath;
            if ("jar".equals(resourceURI.getScheme())) {
                // If the resource is inside a jar, copy it to a temporary file.
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("package_metadata.parquet")) {
                    if (is == null) {
                        throw new IllegalStateException("Could not open resource stream for package_metadata.parquet");
                    }
                    File tempFile = File.createTempFile("package_metadata", ".parquet");
                    tempFile.deleteOnExit();
                    Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    parquetPath = new Path(tempFile.getAbsolutePath());
                }
            } else {
                // Resource is directly available on the filesystem
                parquetPath = new Path(resourceURI);
            }

            Configuration conf = new Configuration();
            ParquetReader<Group> reader = ParquetReader.builder(new GroupReadSupport(), parquetPath)
                    .withConf(conf)
                    .build();
            Group group;
            while ((group = reader.read()) != null) {
                PackageInfo info = new PackageInfo();
                info.setPackageManager(group.getString("package_manager", 0));
                info.setPackageName(group.getString("package_name", 0));
                info.setPackageVersion(group.getString("package_version", 0));
                info.setSpdxLicenseId(group.getString("spdx_license_id", 0));
                info.setMetadataSource(group.getString("metadata_source", 0));
                packages.add(info);
            }
            reader.close();
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


