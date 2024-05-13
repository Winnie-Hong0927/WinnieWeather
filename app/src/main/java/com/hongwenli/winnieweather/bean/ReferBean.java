package com.hongwenli.winnieweather.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferBean {
    private List<String> sources;
    private List<String> license;
}
