package com.le.localexplorer.services.placesapi;

import lombok.Data;

import java.util.List;

@Data
public class Place {
    public String id;
    public DisplayName displayName;
    public String primaryType;
    public List<String> types;

    @Data
    static public class DisplayName {

        public String text;
        public String languageCode;

    }

}