package com.example.parquet_demo.controller;

import com.example.parquet_demo.model.PackageInfo;
import com.example.parquet_demo.service.PackageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageInfoController {

    @Autowired
    private PackageInfoService packageInfoService;

    @GetMapping
    public List<PackageInfo> getAllPackages() {
        return packageInfoService.getAllPackages();
    }

    @GetMapping("/{packageName}")
    public PackageInfo getPackageByName(@PathVariable String packageName) {
        return packageInfoService.getPackageByName(packageName);
    }
}
