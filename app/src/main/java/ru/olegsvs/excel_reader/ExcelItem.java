package ru.olegsvs.excel_reader;

/**
 * Created by olegsvs on 09.04.2018.
 */

class ExcelItem {
    private String country;
    private String sended8;
    private String sended16;
    private String sended8caption;
    private String sended16caption;
    private String cutCaption;
    private String output;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSended8() {
        return sended8;
    }

    public void setSended8(String sended8) {
        this.sended8 = sended8;
    }

    public String getSended16() {
        return sended16;
    }

    public void setSended16(String sended16) {
        this.sended16 = sended16;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getSended8caption() {
        return sended8caption;
    }

    public void setSended8caption(String sended8caption) {
        this.sended8caption = sended8caption;
    }

    public String getSended16caption() {
        return sended16caption;
    }

    public void setSended16caption(String sended16caption) {
        this.sended16caption = sended16caption;
    }

    public String getCutCaption() {
        return cutCaption;
    }

    public void setCutCaption(String cutCaption) {
        this.cutCaption = cutCaption;
    }
}
