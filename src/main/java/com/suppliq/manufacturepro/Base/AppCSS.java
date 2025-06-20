package com.suppliq.manufacturepro.Base;

import java.net.URL;

public enum AppCSS {

    APP_STYLE("application.css");

    private final String fileName;

    AppCSS(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCssPath() {
        URL url = AppCSS.class.getResource("/com/suppliq/manufacturepro/Styles/" + fileName);
        return url != null ? url.toExternalForm() : null;
    }


}
