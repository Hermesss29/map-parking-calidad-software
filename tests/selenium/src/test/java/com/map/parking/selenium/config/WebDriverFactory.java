package com.map.parking.selenium.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Brave (Chromium) controlado con ChromeDriver apuntando a brave.exe.
 */
public final class WebDriverFactory {

    public static WebDriver createDriver() {
        return createBrave();
    }

    public static WebDriver createDriver(String browser) {
        String normalized = browser == null ? "brave" : browser.trim().toLowerCase();
        if (!"brave".equals(normalized)) {
            System.out.println("[WebDriver] Solo Brave está soportado; usando Brave.");
        }
        return createBrave();
    }

    private static WebDriver createBrave() {
        Path binary = braveBinary().orElseThrow(() -> new IllegalStateException(
                "Brave no encontrado. Instala Brave: https://brave.com/download/"));
        System.out.println("[WebDriver] Usando Brave: " + binary);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(binary.toString());
        options.addArguments("--start-maximized");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--remote-allow-origins=*");
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private static Optional<Path> braveBinary() {
        return firstExisting(
                Path.of(System.getenv("ProgramFiles"), "BraveSoftware", "Brave-Browser", "Application", "brave.exe"),
                Path.of(System.getenv("ProgramFiles(x86)"), "BraveSoftware", "Brave-Browser", "Application", "brave.exe"),
                Path.of(System.getenv("LOCALAPPDATA"), "BraveSoftware", "Brave-Browser", "Application", "brave.exe")
        );
    }

    private static Optional<Path> firstExisting(Path... paths) {
        for (Path path : paths) {
            if (path != null && Files.isRegularFile(path)) {
                return Optional.of(path);
            }
        }
        return Optional.empty();
    }

    private WebDriverFactory() {
    }
}
