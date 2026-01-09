package com.system.schedule.dto.photo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 头像审核列表项（不包含图片数据，用于列表显示）
public class PhotoHandleItem {
    private String id = "";
    private String type = "";
    private boolean hasPhoto;
    private String typeDisplay = "";
}