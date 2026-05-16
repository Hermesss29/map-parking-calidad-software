package com.map.parking.selenium.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Local: Brave (Chromium). CI/GitHub Actions: Chrome headless.
 */
public final class WebDriverFactory {

    public static WebDriver createDriver() {
        return createDriver(SeleniumConfig.BROWSER);
    }

    public static WebDriver createDriver(String browser) {
        String normalized = browser == null ? "brave" : browser.trim().toLowerCase();
        return switch (normalized) {
            case "chrome" -> createChrome(null);
            case "brave" -> createBrave();
            default -> braveBinary().isPresent() ? createBrave() : createChrome(null);
        };
    }

    private static WebDriver createBrave() {
        Path binary = braveBinary().orElseThrow(() -> new IllegalStateException(
                "Brave no encontrado. Instala Brave o usa -Dbrowser=chrome"));
        System.out.println("[WebDriver] Usando Brave: " + binary);
        return createChrome(binary);
    }

    private static WebDriver createChrome(Path binary) {
        ChromeOptions options = new ChromeOptions();
        if (binary != null) {
            options.setBinary(binary.toString());
        }
        applyChromiumArgs(options);
        return new ChromeDriver(options);
    }

    private static void applyChromiumArgs(ChromeOptions options) {
        options.addArguments("--start-maximized");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }
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
