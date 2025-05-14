package com.suppliq.manufacturepro.Base;

import java.util.LinkedHashMap;
import java.util.Map;

public class BenchmarkTimer {

    private static final Map<String, Long> startTimes = new LinkedHashMap<>();
    private static final Map<String, Long> durations = new LinkedHashMap<>();
    private static long globalStart;
    private static boolean firstPrint = true;

    public static void startAppBenchmark() {
        startTimes.clear();
        durations.clear();
        globalStart = System.currentTimeMillis();
        firstPrint = true;
        System.out.println("⏳ Benchmarking started...");
    }

    public static void markStart(String label) {
        startTimes.put(label, System.currentTimeMillis());
    }

    public static void markEnd(String label) {
        Long start = startTimes.get(label);
        if (start == null) {
            System.err.println("⚠️ markStart חסר עבור שלב: " + label);
            return;
        }
        durations.put(label, System.currentTimeMillis() - start);
    }

    public static void printResults() {
        if (firstPrint) {
            System.out.println("\n📊 תוצאות אתחול:");
        } else {
            System.out.println("\n📊 תוצאה חדשה:");
        }

        for (Map.Entry<String, Long> entry : durations.entrySet()) {
            System.out.printf("⏱️ %-30s משך: %3d ms\n", entry.getKey(), entry.getValue());
        }

        if (firstPrint) {
            long total = System.currentTimeMillis() - globalStart;
            System.out.println("🚀 זמן אתחול כולל: " + total + " ms");
            firstPrint = false;
        }

        durations.clear(); // ננקה את התוצאות לאחר ההדפסה כדי לא להדפיס שוב
        startTimes.clear();

        System.out.println("─────────────────────────────\n");
    }
}
